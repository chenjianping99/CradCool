package com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.CommonConstants;

/**
 * 未来七天的天气预报
 * 
 * @author liuwenqin
 * 
 */
public class ForecastInfo
{
	/**
	 * 预报日期（由mLongDate代替）
	 */
	@Deprecated
	private String	mDate;
	/**
	 * 星期几
	 */
	private String	mWeekDate;
	/**
	 * 天气状态：晴、雨等
	 */
	private String	mStatus;
	/**
	 * 各种天气状态对应的天气类型
	 */
	private int		mStatusType;
	/**
	 * 最高温度
	 */
	private float	mHigh;
	/**
	 * 最低温度
	 */
	private float	mLow;
	/**
	 * 风向
	 */
	private String	mWindDir;
	/**
	 * 风力
	 */
	private String	mWindStrength;
	/**
	 * 风力数值
	 */
	private float	mWindStrengthValue;
	/**
	 * 自定义风向类型
	 */
	private int		mWindDirType;
	/**
	 * 降水概率
	 */
	private int		mPop;
	/**
	 * 白天天气预报描述
	 */
	private String	mDayStatus;
	/**
	 * 夜晚天气预报描述
	 */
	private String	mNightStatus;
	/**
	 * 预报时间，包括年份。以当地时间为准
	 */
	private String	mLongDate;

	public ForecastInfo()
	{
		// 默认为无效天气信息
		mDate = CommonConstants.UNKNOWN_VALUE_STRING;
		mWeekDate = CommonConstants.UNKNOWN_VALUE_STRING;
		mStatus = CommonConstants.UNKNOWN_VALUE_STRING;
		mStatusType = CommonConstants.UNKNOWN_WEATHER_TYPE;
		mHigh = CommonConstants.UNKNOWN_VALUE_INT;
		mLow = CommonConstants.UNKNOWN_VALUE_INT;
		mWindDir = CommonConstants.UNKNOWN_VALUE_STRING;
		mWindDirType = CommonConstants.UNKNOWN_WIND_DIR_TYPE;
		mWindStrength = CommonConstants.UNKNOWN_VALUE_STRING;
		mWindStrengthValue = CommonConstants.UNKNOWN_VALUE_FLOAT;
		mPop = CommonConstants.UNKNOWN_VALUE_INT;
	}

	public float getWindStrengthValue()
	{
		return mWindStrengthValue;
	}

	/** 设置风力数值 */
	public void setWindStrengthInt(float windStrengthValue)
	{
		this.mWindStrengthValue = windStrengthValue;
	}

	public int getWindDirType()
	{
		return mWindDirType;
	}

	/** 设置风向类型 */
	public void setWindDirType(int wind_dir_type)
	{
		this.mWindDirType = wind_dir_type;
	}

	/**
	 * 设置风向
	 */
	public void setWindDir(String wind_dir)
	{
		this.mWindDir = wind_dir;
	}

	public String getWindDir()
	{
		return this.mWindDir;
	}

	/**
	 * 设置风力
	 */
	public void setWindStrength(String wind_strength)
	{
		this.mWindStrength = wind_strength;
	}

	public String getWindStrength()
	{
		return this.mWindStrength;
	}

	public String getDate()
	{ // getDate2
		return mDate;
	}

	public void setDate(String date)
	{
		this.mDate = date;
	}

	public String getWeekDate()
	{
		return mWeekDate;
	}

	public void setWeekDate(String week_date)
	{
		this.mWeekDate = week_date;
	}

	public String getStatus()
	{
		return mStatus;
	}

	public void setStatus(String status)
	{
		this.mStatus = status;
	}

	public int getStatusType()
	{
		return mStatusType;
	}

	public void setStatusType(int status_type)
	{
		this.mStatusType = status_type;
	}

	public float getHigh()
	{
		return mHigh;
	}

	public void setHigh(float high)
	{
		this.mHigh = high;
	}

	public float getLow()
	{
		return mLow;
	}

	public void setLow(float low)
	{
		this.mLow = low;
	}

	public void setPop(int pop)
	{
		this.mPop = pop;
	}

	public int getPop()
	{
		return this.mPop;
	}

	public String getDayStatus()
	{
		return mDayStatus;
	}

	public void setDayStatus(String dayStatus)
	{
		this.mDayStatus = dayStatus;
	}

	public String getNightStatus()
	{
		return mNightStatus;
	}

	public void setNightStatus(String nightStatus)
	{
		this.mNightStatus = nightStatus;
	}

	public String getLongDate()
	{
		return mLongDate;
	}

	public void setLongDate(String longDate)
	{
		this.mLongDate = longDate;
	}
}
