package com.jiubang.goscreenlock.theme.cjpcardcool.weather.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.CityBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.SearchCitiesResultBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.LocationConstants;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.Util;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.location.PlaceInfo;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.location.ReqLocation;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.location.ReqLocationListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.search.SearchCity;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.search.SearchCityInChinese;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.search.SearchCityListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.R;

/**
 * 
 * <br>类描述: 中文下添加城市的界面
 * <br>功能详细描述:
 * 
 * @author  lishen
 * @date  [2012-9-4]
 */
public class AddChinaCityActivity extends Activity
		implements
			OnClickListener,
			TextWatcher,
			OnKeyListener,
			OnItemClickListener
{
	private View					mCurCity;
	private ProgressBar				mProgress;
	private ImageView				mAddCurCityTarget;
	private EditText				mSearchInput;
	private ImageView				mDeleteSearchBtn;
	private ImageButton				mSearchDummy;
	private ListView				mListView;
	private ListAdapter				mListAdapter;

	private GridView				mGrid;
	private GridAdapter				mGridAdapter;
	private List<CityBean>			mLstCur;
	private List<CityBean>			mLstPopular;
	private List<CityBean>			mLstPopularSub;
	private List<CityBean>			mLstSearch;

	private List<CityBean>			mLstGridCur;
	private LayoutInflater			mInflater;
	private TextView				mLstLable;
	private SearchResultHandler		mSeacherResultHandler;
	private ReqLocation				mReqLocation;
	private boolean					mLocating;
	private LocationResultHandler	mLocHandler;
	private LocationQueryHandler	mLocationQueryHandler;
	private BroadcastReceiver		mReceiver;
	/** 搜索城市的异步Task*/
	private SearchCity				mSearchCity						= null;
	/** 当前界面是否被销毁 */
	private boolean					mIsDestroyed;
	private RelativeLayout			mSearchCityLabelLayout;
	private RelativeLayout			mAddChinaCityLineLayout;

	/** 是否正在执行数据库的查询和添加城市操作。true，是；false，否 */
	private boolean					mDBOperating					= false;

	private static final int		CELL_LOCATION_COUNT_TIME		= 15;
	private static final int		NETWORL_LOCATION_COUNT_TIME		= 15;
	private static final int		GPS_LOCATION_COUNT_TIME			= 50;

	/** 定位完成后返回的各种状态 */
	public static final int			LOCATION_CHANGED				= 1;		// 获取当前位置的经纬度成功
	public static final int			LOCATION_TIME_OUT				= 2;		// 获取经纬度超时
	public static final int			LOCATION_NULL					= 3;		// 无法获取当前城市
	public static final int			LOCATION_CITY					= 4;		// 获取当前位置所处的城市成功
	public static final int			LOCATION_PROVIDER_UNABLE		= 5;		// 没有开启定位功能
	public static final int			LOCATION_NETWORK_ERROR			= 6;		// 基站定位网络不可用
	public static final int			LOCATION_DUPLICATED				= 7;		// 我的位置重复
	public static final int			LOCATION_AUTO_LOCATION_CLOSED	= 8;		// 定位完成时，自动定位选项已关闭
	public static final int			LOCATION_EMPTY					= 9;		// 获取到城市ID为空的我的位置

	/**查询将要添加的城市是否已经存在**/
	private static final int		TOKEN_QUERY_ADD_CITY			= 0x01;
	
	View mV;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_act_add_china_city_layout);
		mInflater = getLayoutInflater();

		mCurCity = findViewById(R.id.add_china_city_title_curcity);
		mCurCity.setOnClickListener(this);
		mAddCurCityTarget = (ImageView) mCurCity.findViewById(R.id.add_china_city_target);
		mProgress = (ProgressBar) mCurCity.findViewById(R.id.add_china_city_curcity_progress);
		mSearchCityLabelLayout = (RelativeLayout) findViewById(R.id.search_city_label);
		mAddChinaCityLineLayout = (RelativeLayout) findViewById(R.id.search_city_label);
		mSearchInput = (EditText) findViewById(R.id.add_china_city_search_input);
		mSearchInput.addTextChangedListener(this);
		mSearchInput.setOnKeyListener(this);
		mDeleteSearchBtn = (ImageView) findViewById(R.id.add_china_city_search_submit);
		mDeleteSearchBtn.setOnClickListener(this);
		mSearchDummy = (ImageButton) findViewById(R.id.add_china_city_search_dummy);
		mListView = (ListView) findViewById(R.id.add_china_city_list);
		mListAdapter = new ListAdapter();
		mListView.setOnItemClickListener(this);
		mSearchDummy.requestFocus();

		mGrid = (GridView) findViewById(R.id.add_china_city_grid);
		mGridAdapter = new GridAdapter();
		mGrid.setAdapter(mGridAdapter);
		mGrid.setOnItemClickListener(this);

		mLstLable = (TextView) findViewById(R.id.china_popular_city_label);
		mLstLable.setText(this.getResources().getString(R.string.addcity_popular_city_label));

		// 列表中设置默认数据
		mLstPopular = new ArrayList<CityBean>();
		mLstSearch = new ArrayList<CityBean>();
		mLstPopularSub = new ArrayList<CityBean>();

		switchType();

		mSeacherResultHandler = new SearchResultHandler(this);
		mLocating = false;
		mReqLocation = new ReqLocation(this, Util.getCurLanguage(this));
		mLocHandler = new LocationResultHandler(this);
		mLocationQueryHandler = new LocationQueryHandler(getContentResolver());

		/**************联网搜索******************/
		mListLable = new ArrayList<String>();
		mWebSearchAdapter = new WebSearchAdapter();
		mWebSearchListHeader = (LinearLayout) mInflater.inflate(
				R.layout.weather_add_china_city_list_header, null);
		mWebSearchListHeader.setOnClickListener(this);
		mWebSearchListHeaderTv = (TextView) mWebSearchListHeader
				.findViewById(R.id.add_china_city_keyword);

		mListView.addHeaderView(mWebSearchListHeader);
		mListView.setAdapter(mListAdapter);

		mTipLabelV = (TextView) findViewById(R.id.add_china_city_search_tip_label);
		mTip1V = (TextView) findViewById(R.id.add_china_city_search_tip_1);
		mTip2V = (TextView) findViewById(R.id.add_china_city_search_tip_2);
		mTip3V = (TextView) findViewById(R.id.add_china_city_search_tip_3);
		/***************************************/

		mV = findViewById(R.id.weather_guanggao_china);
		mV.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				boolean result = Global.gotoMarket(AddChinaCityActivity.this, Global.SURISTRING);
				if (!result)
				{
					Global.gotoBrowser(AddChinaCityActivity.this, Global.SPATHSTRING);
				}
			}
		});
	}

	private void switchType()
	{
		// 更新数据
		mLstPopular.clear();
		setPopularCityContent();
		setGridContent(mLstPopular);
	}

	private void setListContent(List<CityBean> lstContent)
	{
		setListContent(lstContent, null);
	}

	private void setListContent(List<CityBean> lstContent, String searchkey)
	{
		mLstCur = lstContent;
		if (mLstLable != null)
		{
			mLstLable.setText(this.getResources().getString(R.string.addcity_search_result_label));
		}
		// 更新UI
		mListView.setAdapter(mListAdapter);
		mIsShowingWebSearchResult = false;
	}

	private void setGridContent(List<CityBean> lstContent)
	{
		setGridContent(lstContent, null);
	}

	private void setGridContent(List<CityBean> lstContent, String searchkey)
	{
		mLstGridCur = lstContent;
		if (mLstLable != null)
		{
			mLstLable.setText(this.getResources().getString(R.string.addcity_popular_city_label));
		}
		mGrid.setAdapter(mGridAdapter);
	}

	private void setPopularCityContent()
	{
		if (mLstPopular.size() == 0)
		{
			String[] admins = this.getResources().getStringArray(R.array.hot_location);
			for (int i = 0; i < admins.length; i++)
			{
				CityBean city = new CityBean(null, admins[i], null, null, null, null);
				mLstPopular.add(city);
			}
		}
	}

	private void deactivateInputBox()
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mSearchInput.getWindowToken(), 0);
		mSearchDummy.requestFocus();
	}

	private void selectCity(CityBeanContainer city)
	{
		WeatherSettingUtil.setCity(this, city.mCityBean);
		finish();
		//		if (!mDBOperating)
		//		{
		//			mDBOperating = true;
		//			// TODO：执行插入操作之前先检查要插入的城市在数据库中是否存在
		//			/*
		//			mLocationQueryHandler.startQuery(TOKEN_QUERY_ADD_CITY, city,
		//					WeatherContentProvider.TABLE_CITYNOW_URI,
		//					new String[] { CityNowTable.CITY_ID }, CityNowTable.CITY_ID + " in " + "('"
		//							+ city.mCityBean.getCityId() + "') and " + CityNowTable.MY_LOCATION
		//							+ " = '" + Constants.FLAG_CITY_ADDED + "'", null, null);
		//			*/
		//		}
	}

	@Override
	public void onClick(View v)
	{
		if (v.equals(mCurCity))
		{
			if (mAddCurCityTarget.getVisibility() == View.VISIBLE)
			{
				mCurCity.setClickable(false);
				mAddCurCityTarget.setVisibility(View.GONE);
				mProgress.setVisibility(View.VISIBLE);
				startLocation();
			}
		}
		else if (v.equals(mDeleteSearchBtn))
		{
			mSearchInput.setText("");
			deactivateInputBox();
			mGrid.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);

			mSearchCityLabelLayout.setVisibility(View.GONE);
			mAddChinaCityLineLayout.setVisibility(View.VISIBLE);

			setGridContent(mLstPopular);
		}
		/*********************联网搜索*************************/
		else if (v.equals(mWebSearchListHeader))
		{
			final String inputContent = mSearchInput.getText().toString();
			firstNetworkSearch(inputContent);
			deactivateInputBox();
			mLoadingDialog = createLoadingDialog(this,
					this.getResources().getString(R.string.addcity_search_dialog_title), this
							.getResources().getString(R.string.addcity_serach_dialog_content), null);

			// 这个操作要在ondismiss里处理，因为对话框会拦截Acticity的KeyDown事件。
			mLoadingDialog.setOnDismissListener(new OnDismissListener()
			{

				@Override
				public void onDismiss(DialogInterface dialog)
				{
					if (mSearchCity != null)
					{
						mSearchCity.cancel();
						mSearchCity = null;
					}
				}
			});
			mLoadingDialog.show();

			// 如果显示联网结果，则清空之前的搜索结果
			if (mIsShowingWebSearchResult)
			{
				mListLable.clear();
				mWebSearchAdapter.notifyDataSetChanged();
			}
			else
			{
				mListView.setAdapter(mWebSearchAdapter);
			}
			showHideSearchTip(false);
		}
		/****************************************************/
	}

	@Override
	public void afterTextChanged(Editable s)
	{
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		if (s.toString().length() == 0)
		{
			mLstSearch.clear();
			mListView.setVisibility(View.GONE);
			mGrid.setVisibility(View.VISIBLE);

			mSearchCityLabelLayout.setVisibility(View.GONE);
			mAddChinaCityLineLayout.setVisibility(View.VISIBLE);

			setListContent(mLstSearch);
			if (mLstLable != null)
			{
				mLstLable.setText(this.getResources()
						.getString(R.string.addcity_popular_city_label));
			}
			return;
		}

		mSearchCityLabelLayout.setVisibility(View.VISIBLE);
		mAddChinaCityLineLayout.setVisibility(View.GONE);

		if (mGrid.getVisibility() == View.VISIBLE)
		{
			mGrid.setVisibility(View.GONE);
		}

		if (mListView.getVisibility() == View.GONE)
		{
			mListView.setVisibility(View.VISIBLE);
		}

		// mLstSearch在searchCityInChinese中会被clear
		SearchCityInChinese.searchCityInChinese(AddChinaCityActivity.this, s.toString(),
				R.raw.go_city, mLstSearch);
		setListContent(mLstSearch);

		/*********************************联网搜索**********************************/
		showHideSearchTip(false);
		mWebSearchListHeaderTv.setText(s);
		/*************************************************************************/
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event)
	{
		if (v == mSearchInput && keyCode == KeyEvent.KEYCODE_ENTER
				&& event.getAction() == KeyEvent.ACTION_DOWN)
		{
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromInputMethod(mSearchInput.getWindowToken(), 0);
			return true;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{

			// 列表返回
			if (mListView.getVisibility() == View.VISIBLE)
			{

				// 防止本地无结果和联网无结果的提示同时出现
				showHideSearchTip(false);

				if (!mIsShowingWebSearchResult)
				{
					mLstSearch.clear();
					mListView.setVisibility(View.GONE);
					mGrid.setVisibility(View.VISIBLE);

					mSearchCityLabelLayout.setVisibility(View.GONE);
					mAddChinaCityLineLayout.setVisibility(View.VISIBLE);

					setGridContent(mLstPopular);

					mSearchInput.setText("");
				}
				else
				{
					SearchCityInChinese.searchCityInChinese(AddChinaCityActivity.this, mSearchInput
							.getText().toString(), R.raw.go_city, mLstSearch);
					setListContent(mLstSearch);
				}
				return true;
			}
			if (mGrid.getVisibility() == View.VISIBLE)
			{
				if (mLstGridCur.equals(mLstPopularSub))
				{
					setGridContent(mLstPopular);
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(AdapterView<?> l, View v, int position, long id)
	{
		if (l instanceof GridView)
		{
			if (mLstGridCur != null && position >= 0 && position < mLstGridCur.size())
			{
				CityBean city = mLstGridCur.get(position);
				if (mLstGridCur.equals(mLstPopular))
				{
					SearchCityInChinese.searchCityInNextLevel(AddChinaCityActivity.this,
							city.getCityName(), R.raw.go_city, mLstPopularSub);
					setGridContent(mLstPopularSub);
				}
				else if (mLstGridCur.equals(mLstPopularSub))
				{
					CityBeanContainer container = new CityBeanContainer();
					container.mCityBean = city;
					container.mType = PlaceInfo.DOMESTIC;
					selectCity(container);
				}
			}
		}
		else if (l instanceof ListView)
		{
			if (!mIsShowingWebSearchResult)
			{
				if (mLstCur != null && position > 0 && position <= mLstCur.size())
				{
					InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(mSearchInput.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);

					// 因为计算了Header，所以要减一
					CityBean city = mLstCur.get(position - 1);
					CityBeanContainer container = new CityBeanContainer();
					container.mCityBean = city;
					container.mType = PlaceInfo.DOMESTIC;
					selectCity(container);
				}
			}
			/***********************************联网搜索**************************************/
			// 加保护，如果在onSearchComplete的执行时间内用户点了列表，onItemClick就会在之后才被执行，这个时候结果可能会为null
			else if (mSearchResult != null)
			{
				// 先减去Header，如果点击的是Header，交由onClick处理
				if (position-- == 0)
				{
					return;
				}

				// 先判断是否需要显示上一页，处理类似getiew
				if (mSearchResult.getPrePage() != null)
				{
					// 第0项是"联网搜索"，所以这里上一页是第一项
					if (position == 0)
					{
						Message msg = new Message();
						msg.what = REC_DATA_READY;
						msg.obj = mSearchResult.getPrePage();
						mSeacherResultHandler.sendMessage(msg);
						return;
					}
					else
					{
						position--;
					}
				}

				// position在mListLable范围内，则是添加城市，否则是点了下一页
				if (position == mListLable.size())
				{
					//                    Log.d("CYN", mSearchResult.getMoreUrl());
					moreNetworkSearch(mSearchResult.getMoreUrl());
					if (mLoadingDialog != null)
					{
						mLoadingDialog.setTitle(this.getResources().getString(
								R.string.addcity_search_dialog_title));
						mLoadingDialog.setMessage(this.getResources().getString(
								R.string.addcity_serach_dialog_content));
						mLoadingDialog.show();
					}
				}
				else
				{
					CityBeanContainer container = new CityBeanContainer();
					container.mCityBean = mSearchResult.getCities().get(position);
					container.mType = PlaceInfo.WORLDWIDE;
					selectCity(container);
				}
			}
			/*******************************************************************************/
		}
	}

	/**
	 * List Adapter
	 */
	private class ListAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return mLstCur != null ? (mLstCur.size() > 0 ? mLstCur.size() : 1) : 0;
		}

		@Override
		public Object getItem(int position)
		{
			return Integer.valueOf(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public boolean isEnabled(int position)
		{
			return (mLstCur != null && mLstCur.size() > 0) ? true : false;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (mLstCur != null && mLstCur.size() > 0)
			{
				View item = convertView;
				if (item == null)
				{
					item = mInflater.inflate(R.layout.weather_china_list_city_item, parent, false);
				}
				CityBean city = mLstCur.get(position);
				TextView tv = (TextView) item.findViewById(R.id.addcity_list_item_title);
				ImageView iv = (ImageView) item.findViewById(R.id.addcity_list_item_more);
				int index = city.getLabel().indexOf(", ");

				SpannableStringBuilder style = new SpannableStringBuilder(city.getLabel());
				style.setSpan(new ForegroundColorSpan(Color.GRAY), index + 2, city.getLabel()
						.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				tv.setText(style);
				iv.setVisibility(View.GONE);
				return item;
			}
			else if (mLstCur != null && mLstCur.size() == 0)
			{
				View item = convertView;
				if (item == null)
				{
					item = mInflater.inflate(R.layout.weather_addcity_list_item_layout, parent, false);
				}
				TextView tv = (TextView) item.findViewById(R.id.addcity_list_item_title);
				ImageView iv = (ImageView) item.findViewById(R.id.addcity_list_item_more);
				String noFindStr = getString(R.string.none_try_search_again);
				SpannableStringBuilder style = new SpannableStringBuilder(noFindStr);
				style.setSpan(new ForegroundColorSpan(Color.GRAY), 0, noFindStr.length(),
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				tv.setText(style);
				iv.setVisibility(View.GONE);
				return item;
			}
			else
			{
				return null;
			}
		}
	}

	/**
	 * 
	 * <br>类描述: 城市列表的设配器
	 * <br>功能详细描述:
	 * 
	 * @author  lishen
	 * @date  [2012-9-4]
	 */
	private class GridAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return mLstGridCur != null ? mLstGridCur.size() : 0;
		}

		@Override
		public Object getItem(int position)
		{
			return Integer.valueOf(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (mLstGridCur != null && mLstGridCur.size() > 0)
			{
				View item;
				TextView tv;
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
				CityBean city = mLstGridCur.get(position);
				tv.setText(city.getCityName());
				return item;
			}
			else
			{
				return null;
			}
		}

	}

	/**
	 * 
	 * <br>类描述: 用于异步操作数据库
	 * <br>功能详细描述:
	 * 
	 * @author  lishen
	 * @date  [2012-9-4]
	 */
	class LocationQueryHandler extends AsyncQueryHandler
	{

		public LocationQueryHandler(ContentResolver cr)
		{
			super(cr);
		}

		protected void onQueryComplete(int token, Object cookie, Cursor cursor)
		{
			super.onQueryComplete(token, cookie, cursor);
			switch (token)
			{
				case TOKEN_QUERY_ADD_CITY :
					CityBeanContainer container = (CityBeanContainer) cookie;
					CityBean city = container.mCityBean;
					// 添加的城市是否已经存在
					boolean isExist = false;
					if (cursor != null)
					{
						try
						{
							if (cursor.getCount() > 0)
							{
								Toast toast = Toast.makeText(AddChinaCityActivity.this,
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
							values.put(CityNowTable.CITY_TYPE, container.mType);
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
							mLocationQueryHandler.startInsert(0, city,
									WeatherContentProvider.TABLE_CITYNOW_URI, values);
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
					AddChinaCityActivity.this,
					AddChinaCityActivity.this.getString(R.string.add_city_successfully,
							city.getCityName()), Toast.LENGTH_SHORT).show();
			// TODO：到这里插入城市操作完成，可以处理一些插入完成后的操作，例如请求天气
			AddChinaCityActivity.this.finish();
		}
	}

	private void showAlertDialog(final String title, final String message)
	{
		if (!mIsDestroyed)
		{
			new GoWeatherEXDialog.Builder(this).setTitle(title).setTips(message)
					.setPositiveButton(R.string.button_ok, null).show();
		}
	}

	protected void onDestroy()
	{
		mIsDestroyed = true;
		if (mReqLocation != null)
		{
			mReqLocation.cancel();
		}
		if (mSearchCity != null)
		{
			mSearchCity.cancel();
		}
		if (mReceiver != null)
		{
			unregisterReceiver(mReceiver);
		}

		super.onDestroy();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (mReceiver != null)
		{
			unregisterReceiver(mReceiver);
		}
		if (mV != null && Global.isAppExist(this, Global.SWEATHERPACK))
		{
			mV.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		IntentFilter counterActionFilter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
		mReceiver = new LacleChangedBroadcastReceiver();
		registerReceiver(mReceiver, counterActionFilter);
	}

	/**
	 * 
	 * <br>类描述:
	 * <br>功能详细描述:
	 * 
	 * @author  lishen
	 * @date  [2012-9-4]
	 */
	class LacleChangedBroadcastReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			finish();
		}

	}

	/**********************************************************联网搜索*************************************************************/
	private static final int		REC_SEARCH_ERROR			= 0X01;
	private static final int		REC_DATA_READY				= 0X02;
	private static final int		REC_NO_CITY					= 0X03;
	private static final int		REC_NO_NETWORK				= 0x04;

	/** 搜索结果*/
	private SearchCitiesResultBean	mSearchResult;
	/** ListView显示的标签*/
	private ArrayList<String>		mListLable;
	/** ListAdapter*/
	private WebSearchAdapter		mWebSearchAdapter;
	/** 联网搜素的布局(ListView顶部)*/
	private LinearLayout			mWebSearchListHeader;
	/** 显示搜索关键字的TextView*/
	private TextView				mWebSearchListHeaderTv;
	/** Loading对话框*/
	private ProgressDialog			mLoadingDialog;
	/** 联网搜索无结果的提示*/
	private TextView				mTipLabelV, mTip1V, mTip2V, mTip3V;
	/** 是否正在显示联网搜索结果*/
	private boolean					mIsShowingWebSearchResult	= false;
	/**
	 * 
	 * 类描述:用于联网搜索的ListAdapter
	 * 功能详细描述:
	 * 
	 * @author  chenyuning
	 * @date  [2012-11-2]
	 */
	private class WebSearchAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			int count = mListLable.size();
			// 3.1版在点击的时候就清空列表，但不清空结果（防止空指针），因此要判断列表为零
			if (mSearchResult != null && mListLable.size() != 0)
			{
				if (mSearchResult.isMutliPage())
				{
					count++;
				}
				if (mSearchResult.getPrePage() != null)
				{
					count++;
				}
			}
			return count;
		}

		@Override
		public Object getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View item;
			ViewHolder holder;
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

			holder.mImageView.setVisibility(View.GONE);

			// 先判断是否需要显示上一页
			// 若需要显示，则将第一项设为上一页，其他项的position-1（可以不需要改动原有代码）
			if (mSearchResult != null && mSearchResult.getPrePage() != null)
			{
				if (position == 0)
				{
					holder.mTextView.setText(R.string.previous_page);
					holder.mTextView.setGravity(Gravity.CENTER);
					return item;
				}
				else
				{
					position--;
				}
			}

			// 不管是不是有下一页，前面的Label都可以直接取
			if (position >= 0 && position < mListLable.size())
			{
				holder.mTextView.setText(mListLable.get(position));
				holder.mTextView.setGravity(Gravity.CENTER_VERTICAL);
			}

			// 如果有下一页，就显示下一页（只要position比标签数大，就有下一页）
			else if (position == mListLable.size() && mListLable.size() != 0)
			{
				holder.mTextView.setText(R.string.next_page);
				holder.mTextView.setGravity(Gravity.CENTER);
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

	private void firstNetworkSearch(String keyword)
	{
		if (mSearchCity != null)
		{
			mSearchCity.cancel();
		}
		mSearchCity = new SearchCity(AddChinaCityActivity.this, SearchCity.SEARCH_LS,
				mSearchCityListener, keyword, Util.getCurLanguage(this));
		mSearchCity.execute();
	}

	private void moreNetworkSearch(String more_url)
	{
		if (mSearchCity != null)
		{
			mSearchCity.cancel();
		}
		mSearchCity = new SearchCity(AddChinaCityActivity.this, SearchCity.SEARCH_MORE,
				mSearchCityListener, mSearchResult);
		mSearchCity.execute();
	}

	/** 搜索城市业务类 */
	private SearchCityListener	mSearchCityListener	= new SearchCityListener()
													{

														@Override
														public void onSearchComplete(
																SearchCitiesResultBean searchResultBean,
																int searchType)
														{

															// 搜索成功，有结果
															if (!searchResultBean.getCities()
																	.isEmpty())
															{
																Message msg = new Message();
																msg.what = REC_DATA_READY;
																msg.obj = searchResultBean;
																mSeacherResultHandler
																		.sendMessage(msg);
															}
															// 搜索成功，无结果(理论上不会出现，但为防止服务器出错，加入这个判断)
															else
															{
																mSeacherResultHandler
																		.sendEmptyMessage(REC_NO_CITY);
															}

														}

														@Override
														public void onSearchNoNetWorkConnection()
														{
															mSeacherResultHandler
																	.sendEmptyMessage(REC_NO_NETWORK);
														}

														@Override
														public void onSearchFailed()
														{
															mSeacherResultHandler
																	.sendEmptyMessage(REC_SEARCH_ERROR);
														}

														@Override
														public void onSearchNoResult()
														{
															mSeacherResultHandler
																	.sendEmptyMessage(REC_NO_CITY);
														}
													};

	/***/
	static class SearchResultHandler extends Handler
	{
		WeakReference<AddChinaCityActivity>	mOutClass;

		public SearchResultHandler(AddChinaCityActivity outClass)
		{
			mOutClass = new WeakReference<AddChinaCityActivity>(outClass);
		}

		public void handleMessage(Message msg)
		{
			AddChinaCityActivity outClass = mOutClass.get();
			if (outClass == null)
			{
				return;
			}

			// 收到结果，不管是什么，都先刷新视图
			if (outClass.mLoadingDialog != null && !outClass.mIsDestroyed)
			{
				outClass.mLoadingDialog.dismiss();
			}

			switch (msg.what)
			{
			// 搜索成功，没有结果
				case REC_NO_CITY :
					outClass.showHideSearchTip(true);
					break;
				// 无网络
				case REC_NO_NETWORK :
					Toast toast = Toast.makeText(outClass, R.string.addcity_search_no_network,
							Toast.LENGTH_SHORT);
					toast.show();
					outClass.showHideSearchTip(false);
					break;

				// 网络错误    
				case REC_SEARCH_ERROR :

					Toast toast2 = Toast.makeText(outClass, R.string.addcity_search_server_error,
							Toast.LENGTH_SHORT);
					toast2.show();
					outClass.showHideSearchTip(false);
					break;

				// 搜索成功，有结果
				case REC_DATA_READY :
					outClass.mSearchResult = null;
					outClass.mIsShowingWebSearchResult = true;
					outClass.mListLable.clear();

					outClass.mSearchResult = (SearchCitiesResultBean) msg.obj;
					// 取出数据
					for (CityBean city : outClass.mSearchResult.getCities())
					{
						outClass.mListLable.add(city.getLabel());
					}

					outClass.mWebSearchAdapter.notifyDataSetChanged();
					if (!outClass.mSearchResult.isPrePageExisted())
					{
						outClass.mListView.setSelection(0);
					}
					break;
				default :
					break;
			}

			super.handleMessage(msg);
		}
	};

	/*****************************************************************定位城市*******************************************************************/
	private void locationEnd()
	{
		mProgress.setVisibility(View.GONE);
		mAddCurCityTarget.setVisibility(View.VISIBLE);
		mCurCity.setClickable(true);
	}

	/***/
	static class LocationResultHandler extends Handler
	{
		WeakReference<AddChinaCityActivity>	mOutClass;

		public LocationResultHandler(AddChinaCityActivity outClass)
		{
			mOutClass = new WeakReference<AddChinaCityActivity>(outClass);
		}

		public void handleMessage(Message msg)
		{
			final AddChinaCityActivity outClass = mOutClass.get();
			if (outClass == null)
			{
				return;
			}

			switch (msg.what)
			{
				case LOCATION_TIME_OUT :
					outClass.locationEnd();
					Toast toast = Toast.makeText(outClass, R.string.locate_timeout,
							Toast.LENGTH_SHORT);
					toast.show();
					break;
				case LOCATION_PROVIDER_UNABLE :
					outClass.locationEnd();
					outClass.mLocating = false;
					outClass.showAlertDialog(null,
							outClass.getString(R.string.addcity_gps_result_no));
					break;
				case LOCATION_NULL :
					outClass.locationEnd();
					outClass.showAlertDialog(null,
							outClass.getString(R.string.addcity_gps_server_no));
					break;
				case LOCATION_CITY :
					outClass.locationEnd();
					final CityBean curCity = (CityBean) msg.obj;
					if (!outClass.mIsDestroyed)
					{
						StringBuilder label = new StringBuilder();
						if (!TextUtils.isEmpty(curCity.getCityName()))
						{
							label.append(curCity.getCityName());
						}
						if (!TextUtils.isEmpty(curCity.getStateName()))
						{
							label.append(",").append(curCity.getStateName());
						}
						if (!TextUtils.isEmpty(curCity.getCountryName()))
						{
							label.append("(").append(curCity.getCountryName()).append(")");
						}
						new GoWeatherEXDialog.Builder(outClass)
								.setTitle(R.string.addcity_title_curcity_label)
								.setTips(
										String.format(
												outClass.getString(R.string.addcity_gps_result_ok),
												label.toString()))
								.setPositiveButton(R.string.addcity_gps_add_button,
										new DialogInterface.OnClickListener()
										{
											@Override
											public void onClick(DialogInterface dialog, int which)
											{
												if (curCity != null)
												{
													CityBeanContainer container = new CityBeanContainer();
													container.mCityBean = curCity;
													container.mType = PlaceInfo.WORLDWIDE;
													outClass.selectCity(container);
												}
											}
										})
								.setNegativeButton(android.R.string.cancel,
										new DialogInterface.OnClickListener()
										{
											@Override
											public void onClick(DialogInterface dialog, int which)
											{
											}
										}).show();
					}
					break;
			}
		}
	};

	public void startLocation()
	{
		this.mLocating = true;
		doStartLocation(0, LocationConstants.WAY_CELL_LOCATION, CELL_LOCATION_COUNT_TIME);
	}

	private void doStartLocation(final int prevLocationWay, final int curLocationWay, int countTime)
	{
		mReqLocation.startLocation(prevLocationWay, curLocationWay, countTime,
				new ReqLocationListener()
				{

					@Override
					public void onLocationTimeout(int locationWay)
					{
						if (locationWay == LocationConstants.WAY_GPS_LOCATION)
						{
							// 按照定位顺序：基站定位->网络定位->GPS定位，最后一种定位方式为GPS
							mLocating = false;
							// sendBroadcast(Constants.LOCATION_TIME_OUT);
							mLocHandler.sendEmptyMessage(LocationConstants.LOCATION_TIME_OUT);
						}
						else
						{
							startNextLocationWay(prevLocationWay, curLocationWay,
									LocationConstants.LOCATION_TIME_OUT);
						}
					}

					@Override
					public void onLocationNull()
					{
						mLocating = false;
						mLocHandler.sendEmptyMessage(LOCATION_NULL);
					}

					@Override
					public void onLocationWayChanged(int switchType)
					{
					}

					@Override
					public void onLocationSuccess(CityBean curCity, Location curLocation)
					{
						Message message = Message.obtain();
						message.obj = curCity;
						message.what = LOCATION_CITY;
						mLocHandler.sendMessage(message);
					}

					@Override
					public void onLocationFailed(int status)
					{
						startNextLocationWay(prevLocationWay, curLocationWay, status);
					}

					@Override
					public void onLocationLatLngFectched(Location curLocation)
					{
						mReqLocation.fetchAddressInfo(curLocation);
					}
				});
	}

	private void startNextLocationWay(int prevLocationWay, int curLocationWay, int status)
	{
		switch (curLocationWay)
		{
			case LocationConstants.WAY_CELL_LOCATION :
				if (status == LocationConstants.LOCATION_SIMCARD_NOT_READY
						|| status == LocationConstants.LOCATION_NETWORK_ERROR)
				{
					// 基站定位不可用
					doStartLocation(prevLocationWay, LocationConstants.WAY_NETWORK_LOCATION,
							NETWORL_LOCATION_COUNT_TIME);
				}
				else
				{
					// 基站定位失败
					doStartLocation(curLocationWay, LocationConstants.WAY_NETWORK_LOCATION,
							NETWORL_LOCATION_COUNT_TIME);
				}
				break;
			case LocationConstants.WAY_NETWORK_LOCATION :
				if (status == LocationConstants.LOCATION_NETWORK_UNABLED
						|| status == LocationConstants.LOCATION_UNSUPPORTED_BY_SYSYTEM)
				{
					// 网络定位不可用
					doStartLocation(prevLocationWay, LocationConstants.WAY_GPS_LOCATION,
							GPS_LOCATION_COUNT_TIME);
				}
				else
				{
					// 网络定位失败
					doStartLocation(curLocationWay, LocationConstants.WAY_GPS_LOCATION,
							GPS_LOCATION_COUNT_TIME);
				}
				break;
			case LocationConstants.WAY_GPS_LOCATION :
				// 按照定位顺序：基站定位->网络定位->GPS定位，最后一种定位方式为GPS
				// sendBroadcast(Constants.LOCATION_PROVIDER_UNABLE);
				mLocHandler.sendEmptyMessage(LOCATION_PROVIDER_UNABLE);
				break;
			default :
				;
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

	private void showHideSearchTip(boolean bShow)
	{
		if (bShow)
		{
			mTipLabelV.setVisibility(View.VISIBLE);
			mTip1V.setVisibility(View.VISIBLE);
			mTip2V.setVisibility(View.VISIBLE);
			mTip3V.setVisibility(View.VISIBLE);
		}
		else
		{
			mTipLabelV.setVisibility(View.INVISIBLE);
			mTip1V.setVisibility(View.INVISIBLE);
			mTip2V.setVisibility(View.INVISIBLE);
			mTip3V.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 
	 * 类描述: 
	 * 功能详细描述:
	 * 
	 * @author  root
	 * @date  [2012-11-14]
	 */
	static class CityBeanContainer
	{
		public CityBean	mCityBean;
		public int		mType;
	}

}
