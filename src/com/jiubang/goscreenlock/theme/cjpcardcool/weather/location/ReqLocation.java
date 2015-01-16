package com.jiubang.goscreenlock.theme.cjpcardcool.weather.location;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.LocationConstants;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.location.celllocation.CellLocation;

/**
 * 
 * 类描述: 请求定位类
 * 
 * @author liuwenqin
 * @date [2012-9-4]
 */
public class ReqLocation
{

	private Context				mContext;
	/** 定位回调接口 */
	private ReqLocationListener	mReqLocationListener;
	private SuperLocation		mLocationWay;
	private int					mCurLocationWay;

	private String				mLanguage			= "en_US";

	private Handler				mLocationHandler	= new Handler()
													{
														public void handleMessage(Message msg)
														{
															switch (msg.what)
															{
																case LocationConstants.LOCATION_TIME_OUT :
																	mLocationWay.cancel();
																	mReqLocationListener
																			.onLocationTimeout((Integer) msg.obj);
																	break;
															}
														}
													};

	//	/**
	//	 * @param context
	//	 * @param go_city
	//	 *            本地存放城市地址信息的数据库资源ID
	//	 */
	//	public ReqLocation(Context context) {
	//		this.mContext = context;
	//		this.mCurLocationWay = 0;
	//	}

	/**
	 * @param context
	 * @param go_city
	 *            本地存放城市地址信息的数据库资源ID
	 */
	public ReqLocation(Context context, String language)
	{
		this.mContext = context;
		this.mCurLocationWay = 0;
		this.mLanguage = language;
	}

	/**
	 * 开始定位
	 * 
	 * @param prevLocationWay
	 *            上一次的定位方式
	 * @param curLocationWay
	 *            当前定位方式
	 */
	public boolean startLocation(int prevLocationWay, int curLocationWay, int countTime,
			ReqLocationListener reqLocationListener)
	{
		this.mReqLocationListener = reqLocationListener;
		this.mCurLocationWay = curLocationWay;
		boolean enableLocation = false;
		switch (curLocationWay)
		{
			case LocationConstants.WAY_CELL_LOCATION :
				mLocationWay = new CellLocation(mContext, this);
				enableLocation = mLocationWay.startLocation(prevLocationWay, mReqLocationListener);
				break;
			case LocationConstants.WAY_GPS_LOCATION :
				mLocationWay = new GPSLocation(mContext, this);
				enableLocation = mLocationWay.startLocation(prevLocationWay, mReqLocationListener);
				break;
			case LocationConstants.WAY_NETWORK_LOCATION :
				mLocationWay = new NetLocation(mContext, this);
				enableLocation = mLocationWay.startLocation(prevLocationWay, mReqLocationListener);
				break;
			default :
				;
		}
		if (enableLocation)
		{
			final int delayTime = 1000;
			mLocationHandler.postDelayed(mRunnable, countTime * delayTime);
		}
		return enableLocation;
	}

	private Runnable	mRunnable	= new Runnable()
									{
										public void run()
										{
											Message msg = new Message();
											msg.what = LocationConstants.LOCATION_TIME_OUT;
											msg.obj = mCurLocationWay;
											mLocationHandler.sendMessage(msg);
											mLocationHandler.removeCallbacks(this);
										}
									};

	/**
	 * <br>功能简述: 获取经纬度对应的城市
	 * @param curLocation 定位获取到的经纬度
	 * @param go_city 本地城市数据库，用于辅助获取中文下的城市，注意从3.0起，不再使用
	 */
	public void fetchAddressInfo(Location curLocation)
	{
		new FetchCityTask(mContext, mReqLocationListener, curLocation, mLanguage)
				.execute(curLocation);
	}

	public void removeTimer()
	{
		mLocationHandler.removeCallbacks(mRunnable);
	}

	public void cancel()
	{
		removeTimer();
		if (mLocationWay != null)
		{
			mLocationWay.cancel();
		}
	}
}