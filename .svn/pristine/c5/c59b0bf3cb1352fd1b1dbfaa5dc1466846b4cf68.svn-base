package com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.CommonConstants;

/**
 * 未来一小时的天气信息
 * 
 * @author liuwenqin
 * 
 */
public class HourInfo
{
	/**
	 * 日期
	 */
	private String	mDate;
	/**
	 * 时
	 */
	private int		mHour;
	/**
	 * 温度
	 */
	private float	mTemp;
	/**
	 * 湿度
	 */
	private int		mHumidity;
	/**
	 * 风向
	 */
	private String	mWindDir;
	/**
	 * 自定义风向类型
	 */
	private int		mWindDirType;
	/**
	 * 风力(3.0版本已经不再使用)
	 */
	@Deprecated
	private String	mWindStrength;
	/**
	 * 风力数值
	 */
	private float	mWindStrengthValue;
	/**
	 * 天气状态
	 */
	private String	mStatus;
	/**
	 * 天气状态对应的天气类型
	 */
	private int		mStatusType;
	/**
	 * 下雨概率
	 */
	private int		mPop;

	public HourInfo()
	{
		// 默认无法从服务器获取正确内容
		mHour = CommonConstants.UNKNOWN_VALUE_INT;
		mStatus = CommonConstants.UNKNOWN_VALUE_STRING;
		mTemp = CommonConstants.UNKNOWN_VALUE_FLOAT;
		mHumidity = CommonConstants.UNKNOWN_VALUE_INT;
		mWindDir = CommonConstants.UNKNOWN_VALUE_STRING;
		mWindDirType = CommonConstants.UNKNOWN_WIND_DIR_TYPE;
		mWindStrength = CommonConstants.UNKNOWN_VALUE_STRING;
		mWindStrengthValue = CommonConstants.UNKNOWN_VALUE_FLOAT;
		mStatusType = CommonConstants.UNKNOWN_WEATHER_TYPE;
		mPop = CommonConstants.UNKNOWN_VALUE_INT;
		mDate = CommonConstants.UNKNOWN_VALUE_STRING;
	}

	public int getWindDirType()
	{
		return mWindDirType;
	}

	public void setWindDirType(int wind_dir_type)
	{
		this.mWindDirType = wind_dir_type;
	}

	public float getWindStrengthValue()
	{
		return mWindStrengthValue;
	}

	public void setWindStrengthValue(float windStrengthValue)
	{
		this.mWindStrengthValue = windStrengthValue;
	}

	public void setDate(String date)
	{
		this.mDate = date;
	}

	public String getDate()
	{
		return mDate;
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
		return mWindDir;
	}

	/**
	 * 设置风速
	 */
	public void setWindStrength(String wind_strength)
	{
		this.mWindStrength = wind_strength;
	}

	public String getWindStrength()
	{
		return this.mWindStrength;
	}

	public void setHour(int hour)
	{
		this.mHour = hour;
	}

	public int getHour()
	{
		return this.mHour;
	}

	public void setStatus(String status)
	{
		this.mStatus = status;
	}

	public String getStatus()
	{
		return mStatus;
	}

	public void setTemp(float temp)
	{
		this.mTemp = temp;
	}

	public float getTemp()
	{
		return mTemp;
	}

	public void setHumidity(int humidity)
	{
		this.mHumidity = humidity;
	}

	public int getHumidity()
	{
		return mHumidity;
	}

	public void setStatusType(int type)
	{
		this.mStatusType = type;
	}

	public int getStatusType()
	{
		return mStatusType;
	}

	/** 下雨概率 */
	public void setPop(int pop)
	{
		this.mPop = pop;
	}

	public int getPop()
	{
		return this.mPop;
	}
}