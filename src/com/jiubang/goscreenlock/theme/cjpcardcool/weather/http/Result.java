package com.jiubang.goscreenlock.theme.cjpcardcool.weather.http;

/**
 * 
 * <br>类描述: 网络请求结果
 * 
 * @author  liuwenqin
 * @date  [2012-9-17]
 */
public class Result
{

	/** 获取信息的状态 */
	private int				mStatus;
	/** 网络类型 */
	private int				mNetType;
	/** 是否采用压缩 */
	private boolean			mBGzip;
	/** 刷新开始时间 */
	private long			mRequestStartTime	= 0;
	/** 刷新结束时间 */
	private long			mRequestEndTime		= 0;
	/** 联网的类型 */
	private int				mRequestTYpe		= TYPE_UPDATE_WEATHER;

	// ====================== 网络状态 =====================

	/**Wifi网络*/
	public static final int	TYPE_WIFI			= 0x1;
	/**不使用代理的移动网络*/
	public static final int	TYPE_MOBILE			= 0x2;
	/**使用代理的移动网络*/
	public static final int	TYPE_MOBILE_PROXY	= 0x3;
	/**没有网络*/
	public static final int	TYPE_NETWORK_DOWN	= 0x4;

	// ====================== 联网的类型 =====================

	/**刷新天气*/
	public static final int	TYPE_UPDATE_WEATHER	= 0x1;
	/**定位位置*/
	public static final int	TYPE_LOCATION		= 0x2;
	/**搜索城市*/
	public static final int	TYPE_SEARCH_CITY	= 0x3;
	/**国际城市列表*/
	public static final int	TYPE_CITY_LIST		= 0x4;

	public Result()
	{
		this.mBGzip = false;
	}

	public boolean getBGzip()
	{
		return this.mBGzip;
	}

	public void setBGzip(boolean isBGzip)
	{
		this.mBGzip = isBGzip;
	}

	public int getNetType()
	{
		return mNetType;
	}

	public void setNetType(int NetType)
	{
		this.mNetType = NetType;
	}

	public void setStatus(int status)
	{
		this.mStatus = status;
	}

	public int getStatus()
	{
		return this.mStatus;
	}

	public void setRequestStartTime(long requestStartTime)
	{
		this.mRequestStartTime = requestStartTime;
	}

	public void setRequestEndTime(long requestEndTime)
	{
		this.mRequestEndTime = requestEndTime;
	}

	public int getRequestTime()
	{
		return (int) (mRequestEndTime - mRequestStartTime);
	}

	public int getRequestTYpe()
	{
		return mRequestTYpe;
	}

	public void setRequestType(int mRequestTYpe)
	{
		this.mRequestTYpe = mRequestTYpe;
	}
}