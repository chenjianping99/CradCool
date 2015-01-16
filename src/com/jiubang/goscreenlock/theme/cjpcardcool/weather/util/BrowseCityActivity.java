package com.jiubang.goscreenlock.theme.cjpcardcool.weather.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.CityBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.ContinentBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.CountryBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.StateBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.CommonConstants;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.LocationConstants;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.Util;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.HttpExecutor;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.HttpExecutorContext;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.HttpRequestStatus;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.Request;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.Result;
import com.jiubang.goscreenlock.theme.cjpcardcool.R;

/**
 * 
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @date  [2012-9-5]
 */
public class BrowseCityActivity extends Activity
		implements
			OnClickListener,
			OnItemClickListener,
			OnDismissListener
{

	private LayoutInflater				mInflater;

	private ListView					mList;
	private ListAdapter					mListAdapter;
	private ImageView					mBackImg;
	private TextView					mTitleTV;

	/** 用以存放ListView中显示内容的List*/
	private ArrayList<String>			mLabelList;

	/** 大洲List*/
	private ArrayList<ContinentBean>	mContinents;
	/** 当前大洲*/
	private ContinentBean				mCurContinent;
	/** 当前国家*/
	private CountryBean					mCurCountry;
	/** 当前州*/
	private StateBean					mCurState;

	private TextView					mGridTip;
	private GridView					mGrid;
	private GridAdapter					mGridAdapter;

	private static final int			CONTINENT_LEVEL						= 0x01;
	private static final int			COUNTRY_LEVEL						= 0x02;
	private static final int			STATE_LEVEL							= 0x03;
	private static final int			CITY_LEVEL							= 0x04;

	/** 当前的查询层次*/
	private int							mCurLevel;
	private final static String[]		ALPHABET							= new String[] { "A",
			"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X", "Y", "Z"							};

	private ProgressDialog				mLoadingDialog;

	private LocationQueryHandler		mLocationQueryHandler;
	/** 浏览城市界面是否已销毁 */
	private boolean						mIsDestroy							= false;
	/** 是否正在执行数据库的查询和添加城市操作。true，是；false，否 */
	private boolean						mDBOperating						= false;

	/*****服务器返回的状态码********/
	private static final String			STATUS_SERVER_ERROR					= "-1";
	private static final String			STATUS_SERVER_DONE					= "1";
	private static final String			STATUS_SERVER_EMPTY_DATA			= "0";

	/**查询将要添加的城市是否已经存在**/
	private static final int			TOKEN_QUERY_ADD_CITY				= 0x01;
	/**查询动态壁纸绑定的城市id**/
	private static final int			TOKEN_QUERY_LIVE_WALLPAPER_CITY_ID	= 0x02;
	/**更新壁纸绑定的城市**/
	private final static int			TOKEN_UPDATE_LIVE_WALLPAPER_CITY_ID	= 0x03;
	
	View mV;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 初始化UI对象
		mInflater = getLayoutInflater();
		setContentView(R.layout.weather_browsecity_layout);
		mBackImg = (ImageView) findViewById(R.id.add_city_title_back);
		mBackImg.setOnClickListener(this);
		mList = (ListView) findViewById(R.id.add_city_browse_list);
		mTitleTV = (TextView) findViewById(R.id.browse_title_label);
		mTitleTV.setText(R.string.addcity_browse_city_label);

		mGridTip = (TextView) findViewById(R.id.browse_alphabet_tip);

		mGrid = (GridView) findViewById(R.id.browse_city_alphabet_grid);
		mGridAdapter = new GridAdapter();
		mGrid.setAdapter(mGridAdapter);
		mGrid.setOnItemClickListener(this);

		mLabelList = new ArrayList<String>();
		mContinents = new ArrayList<ContinentBean>();
		mListAdapter = new ListAdapter();
		mList.setAdapter(mListAdapter);
		mList.setOnItemClickListener(this);

		if (mLoadingDialog == null)
		{
			mLoadingDialog = createLoadingDialog(this,
					this.getResources().getString(R.string.browse_locations_title), this
							.getResources().getString(R.string.addcity_serach_dialog_content), this);
		}
		mLocationQueryHandler = new LocationQueryHandler(getContentResolver());

		Intent intent = getIntent();

		// 请求大洲数据
		mCurLevel = CONTINENT_LEVEL;
		BrowseLocation browseContinent = new BrowseLocation();
		// 第一个参数可以传null和-10000之外的所有整型数字，这一层它不会被用到url中，仅用作有效判断。
		browseContinent.execute(0, null);
		if (!mIsDestroy && mLoadingDialog != null)
		{
			mLoadingDialog.show();
		}
		
		mV = findViewById(R.id.weather_guanggao_browser);
		mV.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				boolean result = Global.gotoMarket(BrowseCityActivity.this, Global.SURISTRING);
				if (!result)
				{
					Global.gotoBrowser(BrowseCityActivity.this, Global.SPATHSTRING);
				}
			}
		});
		
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (mV != null && Global.isAppExist(this, Global.SWEATHERPACK))
		{
			mV.setVisibility(View.GONE);
		}
	}

	public ProgressDialog createLoadingDialog(Context context, String title, String msg,
			OnDismissListener listener)
	{
		ProgressDialog loadingDialog = new ProgressDialog(context);
		loadingDialog.setTitle(title);
		loadingDialog.setMessage(msg);
		loadingDialog.setOnDismissListener(listener);
		return loadingDialog;
	}

	private void selectCity(CityBean city)
	{
		WeatherSettingUtil.setCity(this, city);
		finish();
		//		if (!mDBOperating) {
		//			mDBOperating = true;
		//			// TODO：执行插入操作之前先检查要插入的城市在数据库中是否存在
		//			/*
		//			mLocationQueryHandler.startQuery(TOKEN_QUERY_ADD_CITY, city,
		//					WeatherContentProvider.TABLE_CITYNOW_URI,
		//					new String[] { CityNowTable.CITY_ID },
		//					CityNowTable.CITY_ID + "='" + city.getCityId() + "' and "
		//							+ CityNowTable.MY_LOCATION + " = '" + Constants.FLAG_CITY_ADDED + "'",
		//					null, null);
		//			*/
		//		}
	}

	@Override
	public void onClick(View v)
	{
		// 基本和onKeyDown相同，但是不需要处理return
		if (mBackImg == v)
		{
			/**
			 * 点击返回键的流程：
			 * 1.若有对话框，隐藏它
			 * 2.若在最顶层，finish
			 * 3.否则清空当前层的数据，返回上一层并释放内存
			 * 4.设置标题
			 */
			if (mLoadingDialog != null)
			{
				if (mLoadingDialog.isShowing())
				{
					mLoadingDialog.dismiss();
				}
			}
			mLabelList.clear();
			switch (mCurLevel)
			{
				case CONTINENT_LEVEL :
					finish();
					break;

				case COUNTRY_LEVEL :
					mCurLevel = CONTINENT_LEVEL;
					if (mCurContinent != null)
					{
						mCurContinent.getCounties().clear();
						mCurCountry = null;
						for (ContinentBean continent : mContinents)
						{
							mLabelList.add(continent.getLabel());
						}
						mTitleTV.setText(R.string.addcity_browse_city_label);
					}
					break;

				case STATE_LEVEL :
					mCurLevel = COUNTRY_LEVEL;
					if (mCurCountry != null)
					{
						mCurCountry.getStates().clear();
						mCurState = null;
						for (CountryBean country : mCurContinent.getCounties())
						{
							mLabelList.add(country.getLabel());
						}

						mTitleTV.setText(mCurContinent.getContinentName());
					}
					break;

				case CITY_LEVEL :
					mCurLevel = STATE_LEVEL;
					if (mCurState != null)
					{
						mGrid.setVisibility(View.GONE);
						mGridTip.setVisibility(View.GONE);
						mCurState.getCities().clear();
						mCurState.getAlphabet().clear();
						for (StateBean state : mCurCountry.getStates())
						{
							mLabelList.add(state.getLabel());
						}
						mTitleTV.setText(mCurCountry.getCountryName());
						mGridAdapter.mIsInitized = false;
					}
					break;
				default :
					break;
			}
			mListAdapter.notifyDataSetChanged();
			mList.setSelection(0);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> l, View v, int position, long id)
	{
		if (l instanceof ListView)
		{

			/**
			 *  前三层点击列表项的操作步骤：
			 *  1.进入下一个操作层次
			 *  2.更新当前操作层的执行对象（如mCurContient）
			 *  3.获取当前操作层的数据
			 *  4.设置Title
			 *  5.显示Dialog
			 *  
			 *  后两层的操作步骤：
			 *  1.获取选中的城市
			 *  2.将这个城市插入数据库
			 *  3.finish
			 */
			switch (mCurLevel)
			{
				case CONTINENT_LEVEL :
					if (mContinents != null && position >= 0 && position < mContinents.size())
					{
						mCurLevel = COUNTRY_LEVEL;
						mCurContinent = mContinents.get(position);
						BrowseLocation browseCountries = new BrowseLocation();
						browseCountries.execute(mCurContinent.getContinentId(), null);
						mTitleTV.setText(mCurContinent.getLabel());
						if (!mIsDestroy && mLoadingDialog != null)
						{
							mLoadingDialog.show();
						}
					}
					break;

				case COUNTRY_LEVEL :
					if (mCurContinent != null && position >= 0
							&& position < mCurContinent.getCounties().size())
					{
						mCurLevel = STATE_LEVEL;
						mCurCountry = mCurContinent.getCounties().get(position);
						BrowseLocation browseStates = new BrowseLocation();
						browseStates.execute(mCurCountry.getCountryId(), null);
						mTitleTV.setText(mCurCountry.getLabel());
						if (!mIsDestroy && mLoadingDialog != null)
						{
							mLoadingDialog.show();
						}
					}
					break;

				case STATE_LEVEL :
					if (mCurCountry != null && position >= 0
							&& position < mCurCountry.getStates().size())
					{
						mCurLevel = CITY_LEVEL;
						mCurState = mCurCountry.getStates().get(position);
						BrowseLocation browseCities = new BrowseLocation();
						browseCities.execute(mCurState.getStateId(), null);
						mTitleTV.setText(mCurState.getLabel());
						if (!mIsDestroy && mLoadingDialog != null)
						{
							mLoadingDialog.show();
						}
					}
					break;

				case CITY_LEVEL :
					if (mCurState != null && position >= 0
							&& position < mCurState.getCities().size())
					{
						CityBean city = mCurState.getCities().get(position);
						selectCity(city);
					}
					break;
				default :
					break;
			}
		}

		// 不需要判断现在处于哪个操作层，因为这个操作只能在字母表层被触发
		/**
		 * 操作流程：
		 * 1.找到用户选择的字母
		 * 2.更新当前字母
		 * 3.用州Id和这个字母请求数据
		 */
		else if (l instanceof GridView)
		{
			if (mCurState != null && position >= 0 && position < ALPHABET.length)
			{
				String selectedLetter = ALPHABET[position];
				// 向服务器请求这个字母下的城市
				BrowseLocation browseCities = new BrowseLocation();
				browseCities.execute(mCurState.getStateId(), selectedLetter);

				if (!mIsDestroy && mLoadingDialog != null)
				{
					mLoadingDialog.show();
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		boolean bHandled = false;

		/**
		 * 点击返回键的流程：
		 * 1.若有对话框，隐藏它
		 * 2.若在最顶层，finish
		 * 3.否则清空当前层的数据，返回上一层并释放内存;如果是字母表层，隐藏GridView
		 * 4.设置标题
		 */
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			bHandled = true;
			if (mLoadingDialog != null)
			{
				if (mLoadingDialog.isShowing())
				{
					mLoadingDialog.dismiss();
				}
			}
			mLabelList.clear();
			switch (mCurLevel)
			{
				case CONTINENT_LEVEL :
					bHandled = false;
					break;

				case COUNTRY_LEVEL :
					mCurLevel = CONTINENT_LEVEL;
					if (mCurContinent != null)
					{
						mCurContinent.getCounties().clear();
						mCurCountry = null;
						for (ContinentBean continent : mContinents)
						{
							mLabelList.add(continent.getLabel());
						}
						mTitleTV.setText(R.string.addcity_browse_city_label);
					}
					break;

				case STATE_LEVEL :
					mCurLevel = COUNTRY_LEVEL;
					if (mCurCountry != null)
					{
						mCurCountry.getStates().clear();
						mCurState = null;
						for (CountryBean country : mCurContinent.getCounties())
						{
							mLabelList.add(country.getLabel());
						}
						mTitleTV.setText(mCurContinent.getContinentName());
					}
					break;

				case CITY_LEVEL :
					mCurLevel = STATE_LEVEL;
					if (mCurState != null)
					{
						mGrid.setVisibility(View.GONE);
						mGridTip.setVisibility(View.GONE);
						mGridAdapter.mIsInitized = false;
						mCurState.getCities().clear();
						mCurState.getAlphabet().clear();
						for (StateBean state : mCurCountry.getStates())
						{
							mLabelList.add(state.getLabel());
						}
						mTitleTV.setText(mCurCountry.getCountryName());
					}
					break;
				default :
					break;
			}
			mListAdapter.notifyDataSetChanged();
			mList.setSelection(0);
		}

		if (!bHandled)
		{
			finish();
			return super.onKeyDown(keyCode, event);
		}
		else
		{
			return true;
		}
	}

	protected void onDestroy()
	{
		mIsDestroy = true;
		super.onDestroy();
	}

	/**
	 * 
	 * <br>类描述:
	 * <br>功能详细描述:
	 * 
	 * @date  [2012-9-5]
	 */
	private class ListAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return mLabelList.size();
		}

		@Override
		public Object getItem(int position)
		{
			if (position >= 0 && position < mLabelList.size())
			{
				return mLabelList.get(position);
			}
			else
			{
				return 0;
			}
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{

			ViewHolder holder = null;
			View item = null;

			if (convertView == null)
			{
				item = mInflater.inflate(R.layout.weather_addcity_list_item_layout, parent, false);
				holder = new ViewHolder();
				holder.mTextView = (TextView) item.findViewById(R.id.addcity_list_item_title);
				holder.mImageView = (ImageView) item.findViewById(R.id.addcity_list_item_more);
				item.setTag(holder);
			}
			else
			{
				item = convertView;
				holder = (ViewHolder) item.getTag();
			}
			if (position >= 0 && position < mLabelList.size())
			{
				holder.mTextView.setText(mLabelList.get(position));
				if (mCurLevel == CITY_LEVEL)
				{
					holder.mImageView.setVisibility(View.GONE);
				}
				else
				{
					holder.mImageView.setVisibility(View.VISIBLE);
				}
			}

			return item;
		}
	}

	/**
	 * 
	 * 类描述:
	 * 功能详细描述:
	 * 
	 * @author  chenyuning
	 * @date  [2012-11-5]
	 */
	class ViewHolder
	{
		TextView	mTextView;
		ImageView	mImageView;
	}

	/**
	 * 
	 * <br>类描述:
	 * <br>功能详细描述:
	 * 
	 * @date  [2012-9-5]
	 */
	private class GridAdapter extends BaseAdapter
	{
		public boolean		mIsInitized		= false;
		private boolean[]	mIsEnabledArray	= new boolean[26];
		@Override
		public int getCount()
		{
			return ALPHABET.length;
		}

		@Override
		public Object getItem(int position)
		{
			return ALPHABET[position];
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public boolean isEnabled(int position)
		{

			// 未被初始化则遍历当前州的字母表，过滤不存在的字母
			// isisEnabled在点击的时候被调用，加在这里仅作保护
			if (!mIsInitized)
			{
				for (int i = 0; i < 26; i++)
				{
					mIsEnabledArray[i] = false;
				}
				for (int i = 0; i < 26; i++)
				{
					for (String letter : mCurState.getAlphabet())
					{
						if (letter.equals(ALPHABET[i]))
						{
							mIsEnabledArray[i] = true;
						}
					}
				}
			}
			return mIsEnabledArray[position];
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View item;
			TextView tv;

			if (!mIsInitized)
			{
				for (int i = 0; i < 26; i++)
				{
					mIsEnabledArray[i] = false;
				}
				for (int i = 0; i < 26; i++)
				{
					for (String letter : mCurState.getAlphabet())
					{
						if (letter.equals(ALPHABET[i]))
						{
							mIsEnabledArray[i] = true;
						}
					}
				}
			}

			if (convertView == null)
			{
				item = mInflater.inflate(R.layout.weather_grid_city_item, parent, false);
				tv = (TextView) item.findViewById(R.id.text);
				item.setTag(tv);
			}
			else
			{
				item = convertView;
				tv = (TextView) item.getTag();
			}
			if (mIsEnabledArray[position])
			{
				tv.setTextColor(Color.BLACK);
			}
			else
			{
				tv.setTextColor(Color.GRAY);
			}
			tv.setText(ALPHABET[position]);
			return item;
		}
	}

	/**
	 * 
	 * <br>类描述:
	 * <br>功能详细描述:
	 * 
	 * @date  [2012-9-5]
	 */
	class LocationQueryHandler extends AsyncQueryHandler
	{

		public LocationQueryHandler(ContentResolver cr)
		{
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor)
		{
			super.onQueryComplete(token, cookie, cursor);
			switch (token)
			{
				case TOKEN_QUERY_ADD_CITY :
					CityBean city = (CityBean) cookie;
					// 添加的城市是否已经存在
					boolean isExist = false;
					if (cursor != null)
					{
						try
						{
							if (cursor.getCount() > 0)
							{
								Toast toast = Toast.makeText(BrowseCityActivity.this,
										R.string.cityexists, Toast.LENGTH_SHORT);
								toast.show();
								mDBOperating = false;
								isExist = true;
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
							// 数据库操作异常，终止本次添加城市，更新数据库操作
							mDBOperating = false;
						}
						finally
						{
							cursor.close();
						}
						if (mDBOperating && !isExist)
						{
							// 数据库操作正常，并且添加的城市不存在数据库中
							// TODO：这里将所得到的城市插入到数据库中，插入完成后会调用onInsertComplete方法
							/*
							ContentValues values = new ContentValues();
							values.put(CityNowTable.CITY_ID, city.getCityId());
							values.put(CityNowTable.OLD_CITY_ID, city.getCityId());
							values.put(CityNowTable.CITY_NAME, city.getCityName());
							values.put(CityNowTable.STATE, city.getStateName());
							values.put(CityNowTable.COUNTRY, city.getCountryName());
							values.put(CityNowTable.TZ_OFFSET, city.getTimeZone());
							values.put(CityNowTable.MY_LOCATION, Constants.FLAG_CITY_ADDED);
							values.put(CityNowTable.WIND_DIRECTION, Constants.UNKNOWN_VALUE_STRING);
							values.put(CityNowTable.WIND_STRENGTH, Constants.UNKNOWN_VALUE_STRING);
							values.put(CityNowTable.WIND_TYPE, Constants.UNKNOWN_WIND_TYPE);
							values.put(CityNowTable.SUNRISE, Constants.UNKNOWN_VALUE_STRING);
							values.put(CityNowTable.SUNSET, Constants.UNKNOWN_VALUE_STRING);
							values.put(CityNowTable.TYPE, Constants.UNKNOWN_WEATHER_TYPE); // 未知天气类型
							values.put(CityNowTable.NOW_DESP, Constants.UNKNOWN_VALUE_STRING);
							values.put(CityNowTable.SEQUENCE, mLargestSeqNum);
							values.put(CityNowTable.POP, Constants.WEATHER_MIN_VALUE);
							values.put(CityNowTable.CITY_TYPE, PlaceInfo.WORLDWIDE);
							values.put(CityNowTable.FEELSLIKE_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.BAROMETER_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.DEWPOINT_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.HIGH_TEMP_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.LOW_TEMP_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.WIND_STRENGTH_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.NOW_TEMP_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.TIMESTAMP, Constants.UNKNOWN_VALUE_INT);
							values.put(CityNowTable.UVINDEX_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.HUMIDITY_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.VISIBILITY_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.UPDATE_TIME, Constants.UNKNOWN_VALUE_INT);
							mLocationQueryHandler.startInsert(0, city, WeatherContentProvider.TABLE_CITYNOW_URI, values);
							*/
						}
					}
					else
					{
						mDBOperating = false;
					}
					break;
				default :
					break;
			}

		}

		@Override
		protected void onInsertComplete(int token, Object cookie, Uri uri)
		{
			super.onInsertComplete(token, cookie, uri);
			mDBOperating = false;
			CityBean city = (CityBean) cookie;
			Toast.makeText(
					BrowseCityActivity.this,
					BrowseCityActivity.this.getString(R.string.add_city_successfully,
							city.getCityName()), Toast.LENGTH_SHORT).show();
			setResult(0, new Intent());
			// TODO：到这里插入城市操作完成，可以处理一些插入完成后的操作，例如请求天气
			BrowseCityActivity.this.finish();
		}

	}

	@Override
	public void onDismiss(DialogInterface arg0)
	{
	}

	/**
	 * 
	 * <br>类描述:
	 * <br>功能详细描述:
	 * 
	 * @date  [2012-9-5]
	 */
	class BrowseLocation extends AsyncTask<Object, Object, Result>
	{

		@Override
		protected Result doInBackground(Object... params)
		{
			int id = CommonConstants.UNKNOWN_VALUE_INT;
			String letter = null;
			if (params.length > 0)
			{
				id = (Integer) params[0];
			}
			if (params.length > 1)
			{
				letter = (String) params[1];
			}
			Result result = new Result();
			result.setStatus(HttpRequestStatus.REQUEST_HTTP_FAILED);

			if (id != CommonConstants.UNKNOWN_VALUE_INT)
			{
				Request request = getRequest(id, letter);
				getData(request, result);
			}
			return result;
		}

		@Override
		protected void onPostExecute(Result result)
		{
			super.onPostExecute(result);

			if (!mIsDestroy && mLoadingDialog != null)
			{
				if (mLoadingDialog.isShowing())
				{
					mLoadingDialog.dismiss();
				}
			}
			mLabelList.clear();

			switch (result.getStatus())
			{

				case HttpRequestStatus.REQUEST_SUCCESS :
					processDataReady();
					break;

				/**doInBackground
				 * 错误处理流程：
				 * 1.将当前操作层回滚
				 * 2.弹对应的toast
				 */
				case HttpRequestStatus.REQUEST_NO_DATA :
					Toast toast = Toast.makeText(BrowseCityActivity.this, R.string.no_result_list,
							Toast.LENGTH_SHORT);
					toast.show();
					break;

				case HttpRequestStatus.REQUEST_NETWORK_UNAVAILABLE :
					Toast toast2 = Toast.makeText(BrowseCityActivity.this,
							R.string.network_excepiton, Toast.LENGTH_SHORT);
					toast2.setGravity(Gravity.CENTER, 0, 0);
					toast2.show();
					break;

				case HttpRequestStatus.REQUEST_TIMEOUT :
					Toast toast3 = Toast.makeText(BrowseCityActivity.this,
							R.string.network_timeout, Toast.LENGTH_SHORT);
					toast3.setGravity(Gravity.CENTER, 0, 0);
					toast3.show();
					break;
				// 其他结果都认为是网络错误
				default :
					Toast toast4 = Toast.makeText(BrowseCityActivity.this,
							R.string.server_error_result, Toast.LENGTH_SHORT);
					toast4.setGravity(Gravity.CENTER, 0, 0);
					toast4.show();
					break;
			}

			mListAdapter.notifyDataSetChanged();
			mList.setSelection(0);
		}

		private Request getRequest(int id, String letter)
		{
			/**
			 * url例子：
			 * 大洲：
			 *     http://goweatherex.3g.cn/goweatherex/guide/continent?lang=en&sys=4.0.4&ps=2.0
			 * 国家：
			 *     http://goweatherex.3g.cn/goweatherex/guide/country?continentid=1&lang=en&sys=4.0.4&ps=2.0
			 * 州：
			 *     http://goweatherex.3g.cn/goweatherex/guide/state?countryid=1&lang=en&sys=4.0.4&ps=2.0
			 * 城市：
			 *     http://goweatherex.3g.cn/goweatherex/guide/cities?stateid=1&lang=en&sys=4.0.4&ps=2.0
			 * 字母：
			 *     http://goweatherex.3g.cn/goweatherex/guide/cities?stateid=1&alphabet=a&lang=en&sys=4.0.4&ps=2.0
			 */

			Request request = null;

			StringBuilder path = new StringBuilder();
			path.append(LocationConstants.STR_HTTP).append(LocationConstants.LOCATION_SERVER_HOST);
			switch (mCurLevel)
			{

				case CONTINENT_LEVEL :
					path.append(LocationConstants.STR_API_GET_CONTINENTS);
					request = new Request(path.toString());
					break;

				case COUNTRY_LEVEL :
					path.append(LocationConstants.STR_API_GET_COUNTRIES);
					request = new Request(path.toString());
					request.addHeader(LocationConstants.STR_API_EXTRA_CONTINENTID,
							String.valueOf(id));
					break;

				case STATE_LEVEL :
					path.append(LocationConstants.STR_API_GET_STATES);
					request = new Request(path.toString());
					request.addHeader(LocationConstants.STR_API_EXTRA_COUNTRYID, String.valueOf(id));
					break;

				case CITY_LEVEL :
					path.append(LocationConstants.STR_API_GET_CITIES);
					request = new Request(path.toString());
					request.addHeader(LocationConstants.STR_API_EXTRA_STATEID, String.valueOf(id));
					if (letter != null)
					{
						request.addHeader(LocationConstants.STR_API_EXTRA_ALPHABET, letter);
					}
					break;

				default :
					// 程序不应该执行到这里
					request = new Request(path.toString());
					break;
			}

			// 加上lang，sys，ps
			if (request != null)
			{
				request.addDefaultHeader(BrowseCityActivity.this,
						Util.getCurLanguage(BrowseCityActivity.this));
			}
			return request;
		}
	}

	/**
	 * 请求服务器获取列表数据
	 * @param request
	 * @param result
	 */
	private void getData(Request request, Result result)
	{
		// 检查网络情况，附带标识网络类型
		HttpExecutor httpExecutor = HttpExecutorContext.getHttpExecutor();
		if (!httpExecutor.checkNetwork(result, BrowseCityActivity.this))
		{
			result.setStatus(HttpRequestStatus.REQUEST_NETWORK_UNAVAILABLE);
		}
		else
		{
			InputStream inputStream = null;
			try
			{
				String url = request.composeCompleteURL();
				result.setRequestStartTime(SystemClock.elapsedRealtime());
				inputStream = httpExecutor.doRefresh(url, request, result);
			}
			catch (UnsupportedEncodingException e1)
			{
				// 请求URL不合法
				result.setStatus(HttpRequestStatus.REQUEST_HTTP_FAILED);
				e1.printStackTrace();
			}

			if (inputStream != null)
			{
				try
				{
					String json = Util.readInputStream(inputStream);
					String serverResult = parseInputStream(json);
					if (serverResult.equals(STATUS_SERVER_DONE))
					{
						// 置浏览城市请求结束时间
						result.setRequestEndTime(SystemClock.elapsedRealtime());
						result.setStatus(HttpRequestStatus.REQUEST_SUCCESS);
					}
					else if (serverResult.equals(STATUS_SERVER_EMPTY_DATA))
					{
						result.setStatus(HttpRequestStatus.REQUEST_NO_DATA);
					}
					else
					{
						result.setStatus(HttpRequestStatus.REQUEST_NO_DATA);
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
					result.setStatus(HttpRequestStatus.REQUEST_IO_EXCEPTION);
				}
				catch (JSONException e)
				{
					e.printStackTrace();
					result.setStatus(HttpRequestStatus.REQUEST_IO_EXCEPTION);
				}
				finally
				{
					try
					{
						inputStream.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			httpExecutor.release();
		}
	}
	/**
	 * 功能简述: 将Json解析后存入内存
	 * 功能详细描述:
	 * 解析JSON的步骤：
	 * 1.读head字段，读出result，若不为1，则直接返回
	 * 2.判断当前操作层，先把当前操作对象的下级数组清空
	 * 3.从JSONObject中读出JSONARRAY，解析后加入数组
	 * 4.若第三步成功，则发生消息通知当前操作成功，否则通知当前操作失败
	 * 
	 * 注意:
	 * @param json
	 * @return 服务器返回的状态码：-1 服务器错误；1 正常；0 无数据
	 * @throws JSONException 
	 */
	private String parseInputStream(String json) throws JSONException
	{

		// -1表示服务器错误
		String result = "-1";

		// 解析JSON
		JSONObject rootObject = new JSONObject(json);

		// 读response头
		JSONObject head = rootObject.getJSONObject("head");
		result = head.getString("result");

		if (result.equalsIgnoreCase("1"))
		{

			// 不同操作层次需要解析的字段不同
			switch (mCurLevel)
			{
				case CONTINENT_LEVEL :
					parseContinents(rootObject);
					break;

				case COUNTRY_LEVEL :
					parseCountries(rootObject);
					break;

				case STATE_LEVEL :
					parseStates(rootObject);
					break;

				case CITY_LEVEL :
					parseCities(rootObject);
					break;

				default :
					break;
			}
		}
		return result;
	}

	private void parseContinents(JSONObject rootObject) throws JSONException
	{
		JSONArray continents;
		continents = rootObject.getJSONArray("continents");
		if (!mContinents.isEmpty())
		{
			mContinents.clear();
		}
		for (int i = 0; i < continents.length(); i++)
		{
			JSONObject continent = continents.getJSONObject(i);
			String name = continent.getString("continent");
			int id = continent.getInt("continentId");
			if (mContinents != null)
			{
				mContinents.add(new ContinentBean(id, name, name));
			}
		}
	}

	private void parseCountries(JSONObject rootObject) throws JSONException
	{
		JSONArray countries;
		countries = rootObject.getJSONArray("countries");
		if (!mCurContinent.getCounties().isEmpty())
		{
			mCurContinent.getCounties().clear();
		}
		for (int i = 0; i < countries.length(); i++)
		{
			JSONObject country = countries.getJSONObject(i);
			String name = country.getString("country");
			int id = country.getInt("countryId");
			if (mCurContinent != null)
			{
				mCurContinent.getCounties().add(new CountryBean(id, name, name));
			}
		}
	}

	private void parseStates(JSONObject rootObject) throws JSONException
	{

		JSONArray states;
		states = rootObject.getJSONArray("states");
		if (!mCurCountry.getStates().isEmpty())
		{
			mCurCountry.getStates().clear();
		}
		for (int i = 0; i < states.length(); i++)
		{
			JSONObject state = states.getJSONObject(i);
			String name = state.getString("state");
			int id = state.getInt("stateId");
			if (mCurCountry != null)
			{
				mCurCountry.getStates().add(new StateBean(id, name, name));
			}
		}
	}

	private void parseCities(JSONObject rootObject) throws JSONException
	{
		String alphabet = rootObject.getString("filterList");

		/** 
		 * filter_list和cities必有一个为空，可以通过判断alphabet是否为空来判断是否需要显示字母表
		 * 显示字母表需要做的事：
		 * 1.使GridView可以见
		 * 2.GridiewAdapter的初始化标志位置为false；
		 */
		if (!TextUtils.isEmpty(alphabet) && !alphabet.equalsIgnoreCase("null"))
		{
			String[] letterList = alphabet.split(", ");
			int length = letterList.length;

			mCurState.setIsAlphabet(true);

			// 清空
			if (!mCurState.getAlphabet().isEmpty())
			{
				mCurState.getAlphabet().clear();
			}

			// 当前String是否为字母
			boolean isLetter = false;

			/**
			 * 如果有一个不是字母，则认为JSON解析错误，将操作回滚，并抛出异常
			 */
			for (int i = 0; i < length; i++)
			{
				isLetter = false;
				for (int j = 0; j < 26; j++)
				{
					if (letterList[i].equalsIgnoreCase(ALPHABET[j]))
					{
						isLetter = true;
					}
				}
				if (isLetter)
				{
					mCurState.getAlphabet().add(letterList[i]);
				}
				else
				{
					if (!mCurState.getAlphabet().isEmpty())
					{
						mCurState.getAlphabet().clear();
					}
					mCurState.setIsAlphabet(false);
					throw new JSONException("字母表格式不正确: " + alphabet);
				}
			}
		}
		else
		{

			JSONArray cities = rootObject.getJSONArray("cities");
			if (!mCurState.getCities().isEmpty())
			{
				mCurState.getCities().clear();
			}
			for (int i = 0; i < cities.length(); i++)
			{
				JSONObject city = cities.getJSONObject(i);
				String cityName = city.getString("city");
				String id = city.getString("cityId");
				String stateName = city.getString("state");
				String countryName = city.getString("country");
				String timeZone = city.getString("timeZone");

				if (mCurState != null)
				{
					mCurState.getCities().add(
							new CityBean(id, cityName, stateName, countryName, timeZone, cityName));
				}
			}
		}
	}

	/**
	 * 1.清空LabelList
	 * 2.将搜索结果装载入Labelist
	 * 3.若有字母表，则显示GridView
	 * 4.通知ListView刷新
	 * 5.若LabelList为空，提示
	 */
	private void processDataReady()
	{
		switch (mCurLevel)
		{
			case CONTINENT_LEVEL :
				for (ContinentBean continent : mContinents)
				{
					mLabelList.add(continent.getLabel());
				}
				break;

			case COUNTRY_LEVEL :
				for (CountryBean country : mCurContinent.getCounties())
				{
					mLabelList.add(country.getLabel());
				}
				break;

			case STATE_LEVEL :
				for (StateBean state : mCurCountry.getStates())
				{
					mLabelList.add(state.getLabel());
				}
				break;

			case CITY_LEVEL :
				for (CityBean city : mCurState.getCities())
				{
					mLabelList.add(city.getLabel());
				}
				if (mCurState.isAlphabet())
				{
					mGrid.setVisibility(View.VISIBLE);
					mGridTip.setVisibility(View.VISIBLE);
					mGridAdapter.notifyDataSetChanged();
				}
				else
				{
					mGrid.setVisibility(View.GONE);
				}
				break;
		}

		if (mLabelList.isEmpty() && !mCurState.isAlphabet() && mCurLevel == CITY_LEVEL)
		{
			Toast toast = Toast.makeText(BrowseCityActivity.this, R.string.no_result_list,
					Toast.LENGTH_SHORT);
			toast.show();
		}

	}

}
