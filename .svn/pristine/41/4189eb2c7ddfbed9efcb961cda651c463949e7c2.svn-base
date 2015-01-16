package com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean;

import java.util.ArrayList;
/**
 * 
 * 类描述:保存国家信息的Bean
 * 功能详细描述:
 * 
 * @author  chenyuning
 * @date  [2012-11-5]
 */
public class CountryBean
{
	private int						mCountryId;
	private String					mCountryName;
	private String					mLabel;

	private ArrayList<StateBean>	mStates;

	public CountryBean(int id, String name, String label)
	{
		setCountryId(id);
		setCountryName(name);
		setLabel(label);
	}

	public int getCountryId()
	{
		return mCountryId;
	}

	public void setCountryId(int mCountryId)
	{
		this.mCountryId = mCountryId;
	}

	public String getCountryName()
	{
		return mCountryName;
	}

	public void setCountryName(String mCountryName)
	{
		this.mCountryName = mCountryName;
	}

	public String getLabel()
	{
		return mLabel;
	}

	public void setLabel(String mLabel)
	{
		this.mLabel = mLabel;
	}

	public ArrayList<StateBean> getStates()
	{
		if (mStates == null)
		{
			mStates = new ArrayList<StateBean>();
		}
		return mStates;
	}

	public void setCounties(ArrayList<StateBean> mStates)
	{
		this.mStates = mStates;
	}
}
