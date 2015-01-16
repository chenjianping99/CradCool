package com.jiubang.goscreenlock.theme.cjpcardcool.weather.util;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.CityBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.R;

/**
 * 
 * <br>类描述: 热门城市界面
 * <br>功能详细描述:
 * 
 * @author  lishen
 * @date  [2012-9-4]
 */
public class PopularcityActivity extends Activity implements OnClickListener, OnItemClickListener
{
	private LayoutInflater			mInflate;

	private ListView				mListView;
	private ListAdapter				mListAdapter;
	private ArrayList<CityBean>		mLstPopular;
	private ImageView				mBackImg;
	private LocationQueryHandler	mLocationQueryHandler;
	private BroadcastReceiver		mReceiver;
	/** 是否正在执行数据库的查询和添加城市操作。true，是；false，否 */
	private boolean					mDBOperating			= false;

	/**查询将要添加的城市是否已经存在**/
	private static final int		TOKEN_QUERY_ADD_CITY	= 0x01;
	
	View mV;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		mInflate = getLayoutInflater();

		setContentView(R.layout.weather_popular_city_layout);

		mBackImg = (ImageView) findViewById(R.id.add_city_title_back);
		mBackImg.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.add_city_popular_list);
		mListAdapter = new ListAdapter();
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(this);

		mLstPopular = new ArrayList<CityBean>();
		setPopularCityContent();
		mLocationQueryHandler = new LocationQueryHandler(getContentResolver());

		mV = findViewById(R.id.weather_guanggao_popular);
		mV.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				boolean result = Global.gotoMarket(PopularcityActivity.this, Global.SURISTRING);
				if (!result)
				{
					Global.gotoBrowser(PopularcityActivity.this, Global.SPATHSTRING);
				}
			}
		});
	}

	private void setPopularCityContent()
	{
		if (mLstPopular.size() == 0)
		{
			String[] admins = this.getResources().getStringArray(R.array.hot_location);
			for (int i = 0; i < admins.length; i++)
			{
				CityBean city = getCityBeanByString(admins[i]);
				if (city != null)
				{
					mLstPopular.add(city);
				}
			}
		}
	}

	private CityBean getCityBeanByString(String location)
	{
		String[] data = location.split("#");
		// xml中存放的元素一共有五段，用“#”分割
		if (data.length != 6)
		{
			return null;
		}
		String cityName = data[0];
		String stateName = data[1];
		String countryName = data[2];
		// 这个现在用不到
		String latleg = data[3];
		String cityId = data[4];
		String timeZone = data[5];
		String label = cityName + ", " + stateName + ", (" + countryName + ")";
		return new CityBean(cityId, cityName, stateName, countryName, timeZone, label);
	}

	@Override
	public void onClick(View v)
	{
		if (mBackImg == v)
		{
			finish();
		}
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
		//					new String[] { CityNowTable.CITY_ID }, CityNowTable.CITY_ID + " in " + "('"
		//							+ city.getCityId() + "') and " + CityNowTable.MY_LOCATION + " = '"
		//							+ Constants.FLAG_CITY_ADDED + "'", null, null);
		//			*/
		//		}
	}

	@Override
	public void onItemClick(AdapterView<?> l, View v, int position, long id)
	{
		if (mLstPopular != null && position >= 0 && position < mLstPopular.size())
		{
			CityBean city = mLstPopular.get(position);
			selectCity(city);
		}
	}

	/**
	 * 
	 * <br>类描述:
	 * <br>功能详细描述:
	 * 
	 * @date  [2012-9-4]
	 */
	private class ListAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return mLstPopular != null ? mLstPopular.size() : 0;
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
			if (mLstPopular != null && mLstPopular.size() > 0)
			{
				View item = convertView;
				if (item == null)
				{
					item = mInflate.inflate(R.layout.weather_addcity_list_item_layout, parent, false);
				}
				CityBean city = mLstPopular.get(position);
				TextView tv = (TextView) item.findViewById(R.id.addcity_list_item_title);
				ImageView iv = (ImageView) item.findViewById(R.id.addcity_list_item_more);
				tv.setText(city.getLabel());
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
	 * <br>类描述:
	 * <br>功能详细描述:
	 * 
	 * @date  [2012-9-4]
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
								Toast toast = Toast.makeText(PopularcityActivity.this,
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
							values.put(CityNowTable.TIMESTAMP, 0);
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
					PopularcityActivity.this,
					PopularcityActivity.this.getString(R.string.add_city_successfully,
							city.getCityName()), Toast.LENGTH_SHORT).show();
			setResult(0, new Intent());
			PopularcityActivity.this.finish();
		}

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (mReceiver != null)
		{
			unregisterReceiver(mReceiver);
			mReceiver = null;
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

	@Override
	protected void onDestroy()
	{
		if (mReceiver != null)
		{
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
		super.onDestroy();
	}

	/**
	 * 
	 * <br>类描述:
	 * <br>功能详细描述:
	 * 
	 * @date  [2012-9-5]
	 */
	class LacleChangedBroadcastReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String lang = Locale.getDefault().getLanguage();
			if (lang.equalsIgnoreCase("zh"))
			{
				finish();
			}
		}
	}

}
