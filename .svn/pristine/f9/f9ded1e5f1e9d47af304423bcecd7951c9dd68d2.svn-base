package com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean;
/**
 * 保存城市信息的Bean
 * @author chenyuning
 *
 */
public class CityBean
{
	/** 城市Id*/
	private String	mCityId;
	/** 城市名*/
	private String	mCityName;
	/** 国家名*/
	private String	mCountryName;
	/** 州名*/
	private String	mStateName;
	/** 时区*/
	private String	mTimeZone;
	/** 标签（用于保存在列表中显示的文本）*/
	private String	mLabel;

	private String	mOldCityId;

	/**
	 * 构造函数
	 * @param id 城市Id
	 * @param cityName 城市名
	 * @param stateName 州名
	 * @param countryName 国家名
	 * @param timeZone 时区
	 * @param label 标签（用于保存在列表中显示的文本）
	 */
	public CityBean(String id, String cityName, String stateName, String countryName,
			String timeZone, String label)
	{
		setCityId(id);
		setCityName(cityName);
		setStateName(stateName);
		setCountryName(countryName);
		setTimeZone(timeZone);
		setLabel(label);
	}

	public CityBean()
	{
		super();
	}

	public String getCityId()
	{
		return mCityId;
	}
	public void setCityId(String mCityId)
	{
		this.mCityId = mCityId;
	}
	public String getCityName()
	{
		return mCityName;
	}
	public void setCityName(String mCityName)
	{
		this.mCityName = mCityName;
	}
	public String getCountryName()
	{
		return mCountryName;
	}
	public void setCountryName(String mCountryName)
	{
		this.mCountryName = mCountryName;
	}
	public String getStateName()
	{
		return mStateName;
	}
	public void setStateName(String mStateName)
	{
		this.mStateName = mStateName;
	}
	public String getTimeZone()
	{
		return mTimeZone;
	}
	public void setTimeZone(String mTimeZone)
	{
		this.mTimeZone = mTimeZone;
	}
	public String getLabel()
	{
		return mLabel;
	}
	public void setLabel(String mLabel)
	{
		this.mLabel = mLabel;
	}
	public String getOldCityId()
	{
		return mOldCityId;
	}
	public void setOldCityId(String oldCityId)
	{
		mOldCityId = oldCityId;
	}

	public String logCityBeanInfo()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("--------------CityBean-------------\n");
		sb.append("mCityId:" + mCityId + "\n");
		sb.append("mCityName:" + mCityName + "\n");
		sb.append("mStateName:" + mStateName + "\n");
		sb.append("mCountryName:" + mCountryName + "\n");
		sb.append("mTimeZone:" + mTimeZone + "\n");
		sb.append("mLabel:" + mLabel + "\n");
		return sb.toString();
	}

}
