package com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean;

/**
 * 
 * 类描述:预警信息
 * 
 * @author liuwenqin
 * @date [2012-9-4]
 */
public class ExtremeInfo
{
	/** 极端天气信息ID，唯一标识 */
	private int		mExtremeId;
	/** 发布时间 */
	private String	mPublishTime;
	/** 过期时间 */
	private String	mExpTime;
	/** 暂时这个字段没有什么用处 */
	private String	mType;
	/** 天气描述 */
	private String	mDescription;
	/** 极端天气现象类型 */
	private String	mPhenomena;
	/** 极端天气的级别 */
	private int		mLevel;
	/** 天气预警的详情 */
	private String	mMessage;
	/** 城市ID */
	private String	mCityId;
	/** 极端天气级别 */
	private String	mLevelStr;
	/** 与已有预警信息作比较，标识该预警信息是否重复 */
	private boolean	mIsDuplicate;
	//	/** 与现在的时间的12小时后作比较，标识该预警信息是否过期 */
	//	private boolean mIsExpired;
	/**时区偏移值*/
	private int		mTzOffset;

	public ExtremeInfo()
	{
	}

	public String getPublishTime()
	{
		return mPublishTime;
	}

	public void setPublishTime(String publish_time)
	{
		this.mPublishTime = publish_time;
	}

	public String getExpTime()
	{
		return mExpTime;
	}

	public void setExpTime(String exp_time)
	{
		this.mExpTime = exp_time;
	}

	public String getType()
	{
		return mType;
	}

	public void setType(String type)
	{
		this.mType = type;
	}

	public String getDescription()
	{
		return mDescription;
	}

	public void setDescription(String description)
	{
		this.mDescription = description;
	}

	public String getPhenomena()
	{
		return mPhenomena;
	}

	public void setPhenomena(String phenomena)
	{
		this.mPhenomena = phenomena;
	}

	public int getLevel()
	{
		return mLevel;
	}

	public void setLevel(int level)
	{
		this.mLevel = level;
	}

	public String getMessage()
	{
		return mMessage;
	}

	public void setMessage(String message)
	{
		this.mMessage = message;
	}

	public void setCityId(String cityId)
	{
		this.mCityId = cityId;
	}

	public String getCityId()
	{
		return mCityId;
	}

	public int getExtremeId()
	{
		return mExtremeId;
	}

	public void setExtremeId(int extremeId)
	{
		this.mExtremeId = extremeId;
	}

	public String getLevelStr()
	{
		return mLevelStr;
	}

	public void setLevelStr(String levelStr)
	{
		this.mLevelStr = levelStr;
	}

	public boolean isDuplicate()
	{
		return mIsDuplicate;
	}

	public void setDuplicate(boolean isDuplicate)
	{
		this.mIsDuplicate = isDuplicate;
	}

	//	public boolean isExpired() {
	//		return mIsExpired;
	//	}
	//
	//	public void setExpired(boolean isExpired) {
	//		this.mIsExpired = isExpired;
	//	}
	//	
	public void setTzOffset(int tzOffset)
	{
		this.mTzOffset = tzOffset;
	}

	public int getTzOffset()
	{
		return this.mTzOffset;
	}
}