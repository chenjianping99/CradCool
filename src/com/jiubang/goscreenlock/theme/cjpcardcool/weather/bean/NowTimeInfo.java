package com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.CommonConstants;

/**
 * 当天天气信息
 * 
 * @author liuwenqin
 * 
 */
public class NowTimeInfo
{
	/**
	 * 天气状态
	 */
	private String	mStatus				= CommonConstants.UNKNOWN_VALUE_STRING;		;
	/**
	 * 各种天气状态对应的天气类型
	 */
	private int		mStatusType			= CommonConstants.UNKNOWN_WEATHER_TYPE;
	/**
	 * 当前温度
	 */
	private float	mRealTemp			= CommonConstants.UNKNOWN_VALUE_FLOAT;
	/**
	 * 湿度
	 */
	private int		mHumidity			= CommonConstants.UNKNOWN_VALUE_INT;
	/**
	 * 最高温度
	 */
	private float	mHigh				= CommonConstants.UNKNOWN_VALUE_FLOAT;
	/**
	 * 最低温度
	 */
	private float	mLow				= CommonConstants.UNKNOWN_VALUE_FLOAT;
	/**
	 * 中文天气指数
	 */
	public ZhiShu	mZhiShu;
	/**
	 * 可见度
	 */
	private float	mVisibility			= CommonConstants.UNKNOWN_VALUE_FLOAT;
	/**
	 * 气压
	 */
	private float	mBarometer			= CommonConstants.UNKNOWN_VALUE_FLOAT;
	/**
	 * 露点
	 */
	private float	mDewpoint			= CommonConstants.UNKNOWN_VALUE_FLOAT;
	/**
	 * 紫外线强度
	 */
	private float	mUvIndex			= CommonConstants.UNKNOWN_VALUE_FLOAT;
	/**
	 * 日出时间
	 */
	private String	mSunrise			= CommonConstants.UNKNOWN_VALUE_STRING;
	/**
	 * 日落时间
	 */
	private String	mSunset				= CommonConstants.UNKNOWN_VALUE_STRING;
	/**
	 * 降水概率
	 */
	private int		mPop				= CommonConstants.UNKNOWN_VALUE_INT;
	/**
	 * 体感温度
	 */
	private float	mFeelsLike			= CommonConstants.UNKNOWN_VALUE_FLOAT;
	/**
	 * 风向
	 */
	private String	mWind				= CommonConstants.UNKNOWN_VALUE_STRING;
	/**
	 * 风力(3.0版本已经不再使用)
	 */
	@Deprecated
	private String	mWindStrength		= CommonConstants.UNKNOWN_VALUE_STRING;
	/**
	 * 自定义风向类型
	 */
	private int		mWindDirType		= CommonConstants.UNKNOWN_WIND_DIR_TYPE;
	/**
	 * 风力数值
	 */
	private float	mWindStrengthValue	= CommonConstants.UNKNOWN_VALUE_FLOAT;
	/**
	 * 降雨量
	 */
	private float	mRainFall			= CommonConstants.UNKNOWN_VALUE_FLOAT;

	// ======== 空气指数单元 ========
	/**
	 * 环境空间质量指数
	 */
	private int		mAqi				= CommonConstants.UNKNOWN_VALUE_INT;
	/**
	 * 质量类型
	 */
	private int		mQualityType		= CommonConstants.UNKNOWN_VALUE_INT;
	/**
	 * pm2.5指数
	 */
	private int		mPM25				= CommonConstants.UNKNOWN_VALUE_INT;
	/**
	 * 可吸入颗粒物
	 */
	private int		mPM10				= CommonConstants.UNKNOWN_VALUE_INT;
	/**
	 * 二氧化硫指数
	 */
	private int		mSO2				= CommonConstants.UNKNOWN_VALUE_INT;
	/**
	 * 二氧化氮指数
	 */
	private int		mNO2				= CommonConstants.UNKNOWN_VALUE_INT;
	// =============================

	public NowTimeInfo()
	{
	}

	public int getQualityType()
	{
		return mQualityType;
	}

	public void setQualityType(int qualityType)
	{
		this.mQualityType = qualityType;
	}

	public int getPM25()
	{
		return mPM25;
	}

	public void setPM25(int PM25)
	{
		this.mPM25 = PM25;
	}

	public int getPM10()
	{
		return mPM10;
	}

	public void setPM10(int PM10)
	{
		this.mPM10 = PM10;
	}

	public int getSO2()
	{
		return mSO2;
	}

	public void setSO2(int SO2)
	{
		this.mSO2 = SO2;
	}

	public int getNO2()
	{
		return mNO2;
	}

	public void setNO2(int NO2)
	{
		this.mNO2 = NO2;
	}

	public float getWindStrengthValue()
	{
		return mWindStrengthValue;
	}

	/** 设置风力 */
	public void setWindStrengthValue(float windStrengthValue)
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

	/** 设置风力 */
	public void setWindStrength(String wind_strength)
	{
		this.mWindStrength = wind_strength;
	}

	public String getWindStrength()
	{
		return this.mWindStrength;
	}

	public float getFeelsLike()
	{
		return mFeelsLike;
	}

	public void setFeelsLike(float feelsLike)
	{
		this.mFeelsLike = feelsLike;
	}

	public void setVisibility(float visibility)
	{
		this.mVisibility = visibility;
	}

	public float getVisibility()
	{
		return this.mVisibility;
	}

	public void setBarometer(float barometer)
	{
		this.mBarometer = barometer;
	}

	public float getBarometer()
	{
		return this.mBarometer;
	}

	public void setDewpoint(float dewpoint)
	{
		this.mDewpoint = dewpoint;
	}

	public float getDewpoint()
	{
		return this.mDewpoint;
	}

	public float getUvIndex()
	{
		return mUvIndex;
	}

	public void setUvIndex(float uvIndex)
	{
		this.mUvIndex = uvIndex;
	}

	public String getSunrise()
	{
		return mSunrise;
	}

	public void setSunrise(String sunrise)
	{
		this.mSunrise = sunrise;
	}

	public String getSunset()
	{
		return mSunset;
	}

	public void setSunset(String sunset)
	{
		this.mSunset = sunset;
	}

	public void setPop(int pop)
	{
		this.mPop = pop;
	}

	public int getPop()
	{
		return this.mPop;
	}

	/** 中文，设置天气指数 */
	public void setZhiShu(ZhiShu zhishu)
	{
		this.mZhiShu = zhishu;
	}

	public ZhiShu getZhiShu()
	{
		return this.mZhiShu;
	}

	public String getWind()
	{
		return mWind;
	}

	public void setWind(String wind)
	{
		this.mWind = wind;
	}

	public void setHigh(float high)
	{
		this.mHigh = high;
	}

	public float getHigh()
	{
		return mHigh;
	}

	public void setLow(float low)
	{
		this.mLow = low;
	}

	public float getLow()
	{
		return this.mLow;
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

	public void setStatusType(int statusType)
	{
		this.mStatusType = statusType;
	}

	public float getRealTemp()
	{
		return mRealTemp;
	}

	public void setRealTemp(float realTemp)
	{
		this.mRealTemp = realTemp;
	}

	public int getHumidity()
	{
		return mHumidity;
	}

	public void setHumidity(int humidity)
	{
		this.mHumidity = humidity;
	}

	public float getRainFall()
	{
		return mRainFall;
	}

	public void setRainFall(float rainFall)
	{
		this.mRainFall = rainFall;
	}

	public int getAqi()
	{
		return mAqi;
	}

	public void setAqi(int aqi)
	{
		this.mAqi = aqi;
	}

}