package com.jiubang.goscreenlock.theme.cjpcardcool.weather.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.LocationConstants;

/**
 * 
 * 类描述: 网络定位类
 * 
 * @author liuwenqin
 * @date [2012-9-4]
 */
public class NetLocation extends SuperLocation
{

	/** 定位完成后返回的各种状态 */
	private static final int	LOCATION_CHANGED	= 1;	// 获取当前位置的经纬度成功
	private LocationManager		mLocationManager;
	/** 可以使用手机的网络或GPS定位 */
	private MyLocationListener	mLocationListener;

	private Handler				mLocationHandler;

	public NetLocation(Context context, ReqLocation reqLocation)
	{
		super(context, reqLocation);
		mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		initHandler();
	}

	private void initHandler()
	{
		mLocationHandler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
					case LOCATION_CHANGED :
						disablePhoneProvider();
						Location curLocation = (Location) msg.obj;
						mReqLocationListener.onLocationLatLngFectched(curLocation);
						break;
				}
			}
		};
	}

	@Override
	public void cancel()
	{
		disablePhoneProvider();
	}

	private void disablePhoneProvider()
	{
		if (mLocationListener == null)
		{
			return;
		}
		mLocationManager.removeUpdates(mLocationListener);
	}

	/**
	 * 
	 * 类描述: 定位结果监听者
	 * 
	 * @author liuwenqin
	 * @date [2012-9-4]
	 */
	class MyLocationListener implements LocationListener
	{

		@Override
		public void onLocationChanged(Location location)
		{
			if (location != null)
			{
				mReqLocation.removeTimer();
				Message message = new Message();
				message.what = LOCATION_CHANGED;
				message.obj = location;
				mLocationHandler.sendMessage(message);
			}
		}

		@Override
		public void onProviderDisabled(String provider)
		{
		}

		@Override
		public void onProviderEnabled(String provider)
		{
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
		}
	}

	@Override
	public boolean startLocation(int prevLocationWay, ReqLocationListener reqLocationListener)
	{
		this.mReqLocationListener = reqLocationListener;
		boolean enablePhone = false;
		int netProviderStatus = LocationConstants.isProviderEnabled(mLocationManager,
				LocationManager.NETWORK_PROVIDER);

		if (netProviderStatus == LocationConstants.LOCATION_PROVIDER_ENABLED)
		{
			mLocationListener = new MyLocationListener();
			// 网络定位开启
			if (prevLocationWay == LocationConstants.WAY_CELL_LOCATION)
			{
				mReqLocationListener.onLocationWayChanged(LocationConstants.CELL_TO_NETWORK);
			}
			else if (prevLocationWay == LocationConstants.WAY_GPS_LOCATION)
			{
				mReqLocationListener.onLocationWayChanged(LocationConstants.GPS_TO_NETWORK);
			}
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
					mLocationListener);
			enablePhone = true;
		}
		else if (netProviderStatus == LocationConstants.LOCATION_PROVIDER_UNABLED)
		{
			mReqLocation.removeTimer();
			mReqLocationListener.onLocationFailed(LocationConstants.LOCATION_NETWORK_UNABLED);
		}
		else
		{
			mReqLocation.removeTimer();
			mReqLocationListener
					.onLocationFailed(LocationConstants.LOCATION_UNSUPPORTED_BY_SYSYTEM);
		}
		return enablePhone;
	}

}