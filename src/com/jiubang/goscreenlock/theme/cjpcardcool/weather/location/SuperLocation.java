package com.jiubang.goscreenlock.theme.cjpcardcool.weather.location;

import android.content.Context;

/**
 * 
 * 类描述:定位基类
 * 
 * @author liuwenqin
 * @date [2012-9-4]
 */
public abstract class SuperLocation
{
	protected Context				mContext;
	/** 定位回调接口 */
	protected ReqLocationListener	mReqLocationListener;
	/** 本地存放城市地址信息数据库的资源ID */
	protected int					mGoCity;
	protected ReqLocation			mReqLocation;

	/**
	 * @param context
	 * @param go_city
	 *            本地存放城市地址信息数据库的资源ID
	 */
	protected SuperLocation(Context context, ReqLocation reqLocation)
	{
		mContext = context;
		this.mReqLocation = reqLocation;
	}

	public abstract boolean startLocation(int prevLocationWay,
			ReqLocationListener reqLocationListener);

	/** 超时，取消定位 */
	public abstract void cancel();
}