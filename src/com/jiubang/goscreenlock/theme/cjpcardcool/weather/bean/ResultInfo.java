package com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean;

/**
 * 请求天气信息结果
 * @author lishen
 *
 */
public class ResultInfo
{

	public final City	mCity;
	public int			mStatus;

	public ResultInfo(City city)
	{
		mCity = city;
	}

	public void setStatus(int status)
	{
		mStatus = status;
	}
}
