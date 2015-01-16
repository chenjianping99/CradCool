package com.jiubang.goscreenlock.theme.cjpcardcool.weather.util;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.CityBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.Util;
import com.jiubang.goscreenlock.theme.cjpcardcool.R;

/**
 * 
 * @author liangdaijian
 *
 */
public class WeatherService extends Service
{
	private WeatherHelperBean	mWeatherHelperBean;
	private Timer				mFlushTimer;
	private TimerTask			mFlushTimerTask;
	private Handler				mHandler;
	private DataBaseHandler		mDbHandler			= null;
	private WeatherDataBean		mWeatherDataBean;
	final int					mTimeForReflush		= 60 * 60 * 1000;
	final int					mTimeForLocation	= 15 * 60 * 1000;
	final int					mTimeoutLocation	= 15;
	CityBean					mCurCity;
	boolean						mIsAutoLocation		= true;

	public static final int		START_WEATHER		= 0x100;
	public static final int		STARTLOCATION		= 0x200;
	boolean						mIsFirstTime		= true;

	@Override
	public void onCreate()
	{
		registerReceiver();
		mHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				if (STARTLOCATION == msg.what)
				{
					if (mWeatherHelperBean != null)
					{
						mWeatherHelperBean.startLocation(mTimeoutLocation);
						WeatherSettingUtil.setLocationLastTime(WeatherService.this,
								System.currentTimeMillis());
					}
				}
				else if (START_WEATHER == msg.what)
				{
					if (mWeatherHelperBean != null && mCurCity != null)
					{
						mWeatherHelperBean.startWeather(mCurCity);
					}
				}
				else
				{
					switch (msg.what)
					{
						case WeatherHelperBean.LOCATION_TIME_OUT :
						case WeatherHelperBean.LOCATION_PROVIDER_UNABLE :
						case WeatherHelperBean.LOCATION_NULL :
						case WeatherHelperBean.LOCATION_NETWORK_ERROR :
						case WeatherHelperBean.LOCATION_DUPLICATED :
						case WeatherHelperBean.LOCATION_EMPTY :
						case WeatherHelperBean.WEATHER_NETWORK_ERROR :
						case WeatherHelperBean.WEATHER_ERRORGENERAL :
						case WeatherHelperBean.WEATHER_ONNONEWDATA :
//								Log.i("ldj", "定位或者获取天气失败" + msg.what);
							if (mStateReflush > 0)
							{
								// 因为是主动刷新，要返回失败结果
								sendBroadcast(false, "" + msg.what, null, 0, 0, 0, 0, null);
							}
							break;
						case WeatherHelperBean.LOCATION_CITY :
//								Log.i("ldj", "定位成功");	
							if (mStateReflush > 0)
							{
								mStateReflush++;
							}
							mCurCity = (CityBean) msg.obj;
							if (mCurCity != null
									&& (mWeatherDataBean == null
											|| System.currentTimeMillis()
													- mWeatherDataBean.getmBuildTime() > mTimeForReflush
											|| !mCurCity.getCityId().equalsIgnoreCase(
													mWeatherDataBean.getmCityId()) || mIsForceFlush))
							{
								mIsForceFlush = false;
								// 若空，先初始化
								if (mWeatherDataBean == null)
								{
									mWeatherDataBean = new WeatherDataBean();
								}
								mWeatherDataBean.setmCityId(mCurCity.getCityId());
								mWeatherDataBean.setmCityName(mCurCity.getCityName());
								mWeatherHelperBean.startWeather(mCurCity);
							}
							else if (mIsFirstTime
									&& mWeatherDataBean != null
									&& mIsAutoLocation
									&& System.currentTimeMillis()
											- mWeatherDataBean.getmBuildTime() <= mTimeForReflush)
							{
								mIsFirstTime = false;
								//								sendBroadcast(true, "" + msg.what, mWeatherDataBean.getmCityName(),
								//										mWeatherDataBean.getmWeatherType(),
								//										mWeatherDataBean.getmWeatherCurrT(),
								//										mWeatherDataBean.getmWeatherHighT(),
								//										mWeatherDataBean.getmWeatherLowT());
							}
							break;
						case WeatherHelperBean.WEATHER_SUCCED :
//								Log.i("ldj", "获取天气成功");
							mIsForceFlush = false;
							// 可以去掉第一次切换到自动定位标志
							WeatherSettingUtil.setIsManToAuto(WeatherService.this, 0);
							WeatherDataBean temp = (WeatherDataBean) msg.obj;
							if (mWeatherDataBean == null)
							{
								mWeatherDataBean = new WeatherDataBean();
								mWeatherDataBean.setmCityId(temp.getmCityId());
								mWeatherDataBean.setmCityName(temp.getmCityName());
							}
							mWeatherDataBean.setmCityId(temp.getmCityId());
							mWeatherDataBean.setmCityName(temp.getmCityName());
							mWeatherDataBean.setmWeatherType(temp.getmWeatherType());
							mWeatherDataBean.setmWeatherCurrT(temp.getmWeatherCurrT());
							mWeatherDataBean.setmWeatherHighT(temp.getmWeatherHighT());
							mWeatherDataBean.setmWeatherLowT(temp.getmWeatherLowT());
							mWeatherDataBean.setmBuildTime(temp.getmBuildTime());
							mWeatherDataBean.setWeaterThemePreList(temp.getWeaterThemePreList());
							
							mDbHandler.insert(mWeatherDataBean);
							sendBroadcast(true, "" + msg.what, mWeatherDataBean.getmCityName(),
									mWeatherDataBean.getmWeatherType(),
									mWeatherDataBean.getmWeatherCurrT(),
									mWeatherDataBean.getmWeatherHighT(),
									mWeatherDataBean.getmWeatherLowT(),
									mWeatherDataBean.getWeatherPreByteArray()
									);
							if (!mIsDestroy)
							{
								try
								{
									mFlushTimer.cancel();
									mFlushTimerTask.cancel();
									mFlushTimer = null;
									mFlushTimerTask = null;
									mFlushTimer = new Timer();
									mFlushTimerTask = new TimerTask()
									{
										@Override
										public void run()
										{
											if (mHandler != null)
											{
												if (!mIsAutoLocation)
												{
													mHandler.sendEmptyMessage(START_WEATHER);
												}
												else
												{
													mHandler.sendEmptyMessage(STARTLOCATION);
												}
											}
										}
									};
									// 每小时拿一次数据
									mFlushTimer.schedule(mFlushTimerTask, mTimeForReflush,
											mTimeForReflush);
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
							break;
					}
					if (mStateReflush > 0)
					{
						mStateReflush--;
					}
				}
			}
		};
		mFlushTimer = new Timer();
		mFlushTimerTask = new TimerTask()
		{
			@Override
			public void run()
			{
				if (mHandler != null)
				{
					if (!mIsAutoLocation)
					{
						mHandler.sendEmptyMessage(START_WEATHER);
					}
					else
					{
						mHandler.sendEmptyMessage(STARTLOCATION);
					}
				}
			}
		};
	}
	boolean	mIsDestroy	= false;
	@Override
	public void onDestroy()
	{
		try
		{
			mIsDestroy = true;
			unregisterReceiver();
			if (mFlushTimer != null)
			{
				mFlushTimer.cancel();
				mFlushTimer = null;
			}
			if (mFlushTimerTask != null)
			{
				mFlushTimerTask.cancel();
				mFlushTimerTask = null;
			}
			if (mWeatherHelperBean != null)
			{
				mWeatherHelperBean.onDestroy();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if (intent == null)
		{
			return START_NOT_STICKY;
		}

		if (mWeatherHelperBean == null)
		{
			// 语言记录在strings里面，从里面读
			String language = this.getResources().getString(R.string.weather_language);
			if (null == language || language.trim().length() <= 0
					|| language.equalsIgnoreCase("default"))
			{
				language = Util.getCurLanguage(this);
			}
			mWeatherHelperBean = new WeatherHelperBean(this, mHandler, language);

		}
		long delay = 0;
		if (mDbHandler == null)
		{
			mDbHandler = new DataBaseHandler(this);
		}
		mWeatherDataBean = mDbHandler.query();
		if (mWeatherDataBean != null)
		{
			delay = mTimeForReflush
					- (System.currentTimeMillis() - mWeatherDataBean.getmBuildTime());
			if (delay < 0)
			{
				delay = 0;
			}
		}
		// 取得用户设置的城市
		mCurCity = WeatherSettingUtil.getCity(this);
		// 取得用户是否自动定位
		mIsAutoLocation = WeatherSettingUtil.getLocationWay(this) == 1;
		if (!mIsAutoLocation)
		{
			if (mWeatherDataBean == null
					|| (mCurCity != null && !mCurCity.getCityId().equalsIgnoreCase(
							mWeatherDataBean.getmCityId())))
			{
				delay = mTimeForReflush;
				if (mHandler != null)
				{
					mHandler.sendEmptyMessage(START_WEATHER);
				}
			}
			else if (mWeatherDataBean != null)
			{
				// 拿到上次数据
				//				sendBroadcast(true, "onStartCommand", mWeatherDataBean.getmCityName(),
				//						mWeatherDataBean.getmWeatherType(), mWeatherDataBean.getmWeatherCurrT(),
				//						mWeatherDataBean.getmWeatherHighT(), mWeatherDataBean.getmWeatherLowT());
			}
		}
		else if (mIsAutoLocation && mWeatherDataBean != null)
		{
			// 拿到上次数据
			if (mCurCity == null)
			{
				//				sendBroadcast(true, "onStartCommand", mWeatherDataBean.getmCityName(),
				//						mWeatherDataBean.getmWeatherType(), mWeatherDataBean.getmWeatherCurrT(),
				//						mWeatherDataBean.getmWeatherHighT(), mWeatherDataBean.getmWeatherLowT());
			}
		}

		if (mIsAutoLocation && WeatherSettingUtil.getIsManToAuto(this) > 0)
		{
			if (mHandler != null)
			{
				mHandler.sendEmptyMessage(STARTLOCATION);
			}
		}
		else if (mIsAutoLocation && WeatherSettingUtil.getIsManToAuto(this) <= 0
				&& mWeatherDataBean != null)
		{
			//			sendBroadcast(true, "onStartCommand", mWeatherDataBean.getmCityName(),
			//					mWeatherDataBean.getmWeatherType(), mWeatherDataBean.getmWeatherCurrT(),
			//					mWeatherDataBean.getmWeatherHighT(), mWeatherDataBean.getmWeatherLowT());
		}
		//		else
		//		{
		//			long lastTime = WeatherSettingUtil.getLocationLastTime(WeatherService.this);
		//			if (System.currentTimeMillis() - lastTime > mTimeForLocation)
		//			{
		//				delay = mTimeForReflush;
		//				if (mHandler != null)
		//				{
		//					mHandler.sendEmptyMessage(STARTLOCATION);
		//				}
		//			}
		//		}
		try
		{
			// 每小时拿一次数据
			mFlushTimer.schedule(mFlushTimerTask, delay, mTimeForReflush);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private void sendBroadcast(boolean isSucced, String msg, String cityname, int type, float curr,
			float high, float low, byte[] preview)
	{
		Intent intent = new Intent(Constant.WEATHERBROADCASTFILTER);
		Bundle b = new Bundle();
		b.putBoolean("issucced", isSucced);
		b.putString("msg", msg);
		b.putString("cityname", cityname);
		b.putInt("type", type);
		b.putFloat("curr", curr);
		b.putFloat("high", high);
		b.putFloat("low", low);
		b.putByteArray(Constant.WEATHER_PREVIEW, preview);
		intent.putExtras(b);
		WeatherService.this.sendBroadcast(intent);
	}

	private BroadcastReceiver	mReceiver;
	private int					mStateReflush	= 0;
	private boolean				mIsForceFlush	= false;
	private void registerReceiver()
	{
		if (mReceiver == null)
		{
			mReceiver = new BroadcastReceiver()
			{
				@Override
				public void onReceive(Context context, Intent intent)
				{
					if (intent == null || intent.getExtras() == null)
					{
						return;
					}

					if (Constant.WEATHEREFLUSH.equals(intent.getAction()))
					{
						mIsForceFlush = intent.getExtras().getBoolean(Constant.WEATHER_FLUSH_FORCE,
								false);
						if (mIsAutoLocation)
						{
							long lastTime = WeatherSettingUtil
									.getLocationLastTime(WeatherService.this);
							if (System.currentTimeMillis() - lastTime > mTimeForLocation
									|| mIsForceFlush)
							{
								if (mHandler != null)
								{
									if (mStateReflush <= 0)
									{
										mStateReflush++;
										if (mHandler != null)
										{
											mHandler.sendEmptyMessage(STARTLOCATION);
										}
									}
								}
							}
						}
						else
						{
							// 非自动定位，只有强制刷新才相应刷新
							if (mIsForceFlush)
							{
								if (mHandler != null)
								{
									if (mStateReflush <= 0)
									{
										mStateReflush++;
										if (mHandler != null)
										{
											mHandler.sendEmptyMessage(START_WEATHER);
										}
									}
								}
							}
						}
					} else if (Constant.ACTION_GOLOCKER_UNLOCK.equals(intent.getAction())) {
						stopSelf();
					}
				}
			};
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.WEATHEREFLUSH);
		filter.addAction(Constant.ACTION_GOLOCKER_UNLOCK);
		this.registerReceiver(mReceiver, filter);
	}
	/**
	 * 反注册监听天气变化的广播
	 */
	private void unregisterReceiver()
	{
		this.unregisterReceiver(mReceiver);
	}
}
