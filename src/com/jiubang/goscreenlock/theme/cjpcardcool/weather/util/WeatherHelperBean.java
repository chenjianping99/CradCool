package com.jiubang.goscreenlock.theme.cjpcardcool.weather.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.City;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.CityBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.ForecastInfo;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.LocationConstants;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.Result;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.location.ReqLocation;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.location.ReqLocationListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.tianqi.IHttpConnListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.tianqi.SyncWeatherInfoLoader;

/**
 * 
 * <br>类描述: 英文下添加城市的界面
 * <br>功能详细描述:
 * 
 * @date  [2012-9-5]
 */
public class WeatherHelperBean
{

	/** 定位完成后返回的各种状态 */
	public static final int	LOCATION_CHANGED				= 1;	// 获取当前位置的经纬度成功
	public static final int	LOCATION_TIME_OUT				= 2;	// 获取经纬度超时
	public static final int	LOCATION_NULL					= 3;	// 无法获取当前城市
	public static final int	LOCATION_CITY					= 4;	// 获取当前位置所处的城市成功
	public static final int	LOCATION_PROVIDER_UNABLE		= 5;	// 没有开启定位功能
	public static final int	LOCATION_NETWORK_ERROR			= 6;	// 基站定位网络不可用
	public static final int	LOCATION_DUPLICATED				= 7;	// 我的位置重复
	public static final int	LOCATION_AUTO_LOCATION_CLOSED	= 8;	// 定位完成时，自动定位选项已关闭
	public static final int	LOCATION_EMPTY					= 9;	// 获取到城市ID为空的我的位置

	/**获取天气回来的各种状态**/
	public static final int	WEATHER_NETWORK_ERROR			= 10;	// 获取天气网络错误
	public static final int	WEATHER_ERRORGENERAL			= 11;	// 获取天气发生错误
	public static final int	WEATHER_ONNONEWDATA				= 12;	// 获取天气时间戳一样，不更新数据
	public static final int	WEATHER_SUCCED					= 13;	// 获取天气成功

	private ReqLocation		mReqLocation;

	Context					mContext;

	private Handler			mHandler;

	private String			mLang;

	public WeatherHelperBean(Context mContext, Handler mHandler, String lang)
	{
		// 准备工具
		this.mContext = mContext;
		this.mHandler = mHandler;
		this.mLang = lang;
		mReqLocation = new ReqLocation(mContext, mLang);
	}

	/*******************************************************定位城市**************************************************************/
	public void startLocation(int second)
	{
		//		Toast.makeText(mContext.getApplicationContext(), "Location...", Toast.LENGTH_LONG).show();
		doStartLocation(0, LocationConstants.WAY_CELL_LOCATION, second);
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
							if (mHandler != null)
							{
								mHandler.sendEmptyMessage(LOCATION_TIME_OUT);
							}
						}
						else
						{
							startNextLocationWay(prevLocationWay, curLocationWay, LOCATION_TIME_OUT);
						}
					}

					@Override
					public void onLocationNull()
					{
						// sendBroadcast(Constants.LOCATION_NULL);
						if (mHandler != null)
						{
							mHandler.sendEmptyMessage(LOCATION_NULL);
						}
					}

					@Override
					public void onLocationWayChanged(int switchType)
					{
						// sendSwitchLocationWayBroadcast(switchType);
					}

					@Override
					public void onLocationSuccess(CityBean curCity, Location curLocation)
					{
						Message message = Message.obtain();
						message.obj = curCity;
						message.what = LOCATION_CITY;
						if (mHandler != null)
						{
							mHandler.sendMessage(message);
						}
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
					doStartLocation(prevLocationWay, LocationConstants.WAY_NETWORK_LOCATION, 15);
				}
				else
				{
					// 基站定位失败
					doStartLocation(curLocationWay, LocationConstants.WAY_NETWORK_LOCATION, 15);
				}
				break;
			case LocationConstants.WAY_NETWORK_LOCATION :
				if (status == LocationConstants.LOCATION_NETWORK_UNABLED
						|| status == LocationConstants.LOCATION_UNSUPPORTED_BY_SYSYTEM)
				{
					// 网络定位不可用
					doStartLocation(prevLocationWay, LocationConstants.WAY_GPS_LOCATION, 50);
				}
				else
				{
					// 网络定位失败
					doStartLocation(curLocationWay, LocationConstants.WAY_GPS_LOCATION, 50);
				}
				break;
			case LocationConstants.WAY_GPS_LOCATION :
				// 按照定位顺序：基站定位->网络定位->GPS定位，最后一种定位方式为GPS
				// sendBroadcast(Constants.LOCATION_PROVIDER_UNABLE);
				if (mHandler != null)
				{
					mHandler.sendEmptyMessage(LOCATION_PROVIDER_UNABLE);
				}
				break;
			default :
				;
		}
	}

	// ==============================================================================
	// 天气刷新的任务
	public void startWeather(final CityBean curCity)
	{
		//		Toast.makeText(mContext.getApplicationContext(), "Obtain Weather-info...", Toast.LENGTH_LONG).show();
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				new WeatherWorker(new City(curCity.getCityName(), curCity.getCityId(), 0))
						.startRefresh();
			}
		}).start();
	}

	/**
	 * 
	 * 类描述:处理天气刷新的工作任务
	 * 
	 */
	private class WeatherWorker implements IHttpConnListener
	{
		/**
		 * 需要服务器下发的时间制（只需要24小时制，默认为false）
		 */
		private final boolean	mIs12Hours	= false;
		private City			mRefreshCity;

		public WeatherWorker(City city)
		{
			mRefreshCity = city;
		}

		/** 同步刷新天气 */
		public void startRefresh()
		{
			SyncWeatherInfoLoader mSyncLoader = new SyncWeatherInfoLoader(mContext, mRefreshCity,
					this);
			mSyncLoader.setHourFormat(mIs12Hours);
			mSyncLoader.setLanguage(mLang);
			mSyncLoader.start();
		}

		@Override
		public void onNetworkUnavailable(List<Result> results)
		{
			// 没有网络的情况
			if (mHandler != null)
			{
				mHandler.sendEmptyMessage(WEATHER_NETWORK_ERROR);
			}
		}

		@Override
		public void onErrorGeneral(List<Result> results)
		{
			// 获取天气过程中发生错误
			if (mHandler != null)
			{
				mHandler.sendEmptyMessage(WEATHER_ERRORGENERAL);
			}
		}

		@Override
		public void onSuccess(final City city, List<Result> results)
		{
			// 成功
			//			StringBuffer buffer = new StringBuffer();
			//			buffer.append("当前温度：" + city.mNow.getRealTemp() + "\n");
			//			buffer.append("最高温度：" + city.mNow.getHigh() + " ~ 最低温度：" + city.mNow.getLow() + "\n");
			//			buffer.append(" === 多天预报 === \n");
			//			for (ForecastInfo info : city.mForecastList)
			//			{
			//				buffer.append("日期：" + info.getLongDate());
			//				buffer.append(" 温度：" + info.getHigh() + "~" + info.getLow() + "\n");
			//			}
			//			buffer.append(" === 小时预报 === \n");
			//			for (HourInfo info : city.mHourList)
			//			{
			//				buffer.append("时间：" + info.getDate() + " " + info.getHour() + "时");
			//				buffer.append(" 温度：" + info.getTemp() + "\n");
			//			}
			float curr = city.mNow.getRealTemp();
			float high = city.mNow.getHigh();
			float low = city.mNow.getLow();
			WeatherDataBean temp = new WeatherDataBean();
			temp.setmCityId(city.getCode());
			temp.setmCityName(city.getName());
			temp.setmWeatherType(city.mNow.getStatusType());
			temp.setmWeatherCurrT(curr);
			temp.setmWeatherHighT(high);
			temp.setmWeatherLowT(low);
			temp.setmBuildTime(System.currentTimeMillis());
			
			ArrayList<WeatherDataBean> weaterPreList = new ArrayList<WeatherDataBean>();
			
			// 组装天气预报
			for (int i = 0; i < city.mForecastList.size(); i++)
			{
				WeatherDataBean bean = new WeatherDataBean();
				ForecastInfo info = city.mForecastList.get(i);
				bean.setmCityId(city.getCode());
				bean.setmCityName(city.getName());
				bean.setmWeatherType(info.getStatusType());
				// 天气预报没有当前温度，所以算便传一个进去
				bean.setmWeatherCurrT(0);
				
				bean.setmWeatherHighT(info.getHigh());
				bean.setmWeatherLowT(info.getLow());
				bean.setWeekDate(info.getWeekDate());
				bean.setmBuildTime(System.currentTimeMillis());
				
				weaterPreList.add(bean);
			}
			temp.setWeaterThemePreList(weaterPreList);
			
			Message message = Message.obtain();
			message.obj = temp;
			message.what = WEATHER_SUCCED;
			if (mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
		@Override
		public void onNoNewData(List<Result> results)
		{
			// 时间戳与服务器上的一致，说明没有新数据
			if (mHandler != null)
			{
				mHandler.sendEmptyMessage(WEATHER_ONNONEWDATA);
			}
		}

	}

	public void onDestroy()
	{
		if (mReqLocation != null)
		{
			mReqLocation.cancel();
		}
	}
}
