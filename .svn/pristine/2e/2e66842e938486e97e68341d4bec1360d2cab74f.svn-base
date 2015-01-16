package com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean;

import java.util.ArrayList;
/**
 * 
 * 类描述:保存大洲信息的bean
 * 功能详细描述:
 * 
 * @author  chenyuning
 * @date  [2012-11-5]
 */
public class ContinentBean
{
	private int						mContinentId;
	private String					mContinentName;
	private String					mLabel;

	private ArrayList<CountryBean>	mCounties;

	public ContinentBean(int id, String name, String label)
	{
		setContinentId(id);
		setContinentName(name);
		setLabel(label);
	}

	public int getContinentId()
	{
		return mContinentId;
	}

	public void setContinentId(int mContinentId)
	{
		this.mContinentId = mContinentId;
	}

	public String getContinentName()
	{
		return mContinentName;
	}

	public void setContinentName(String mContinentName)
	{
		this.mContinentName = mContinentName;
	}

	public String getLabel()
	{
		return mLabel;
	}

	public void setLabel(String mLabel)
	{
		this.mLabel = mLabel;
	}

	public ArrayList<CountryBean> getCounties()
	{
		if (mCounties == null)
		{
			mCounties = new ArrayList<CountryBean>();
		}
		return mCounties;
	}

	public void setCounties(ArrayList<CountryBean> mCounties)
	{
		this.mCounties = mCounties;
	}
}
