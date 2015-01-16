package com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean;

import java.util.ArrayList;
/**
 * 
 * 类描述:保存州信息的Bean
 * 功能详细描述:
 * 
 * @author  chenyuning
 * @date  [2012-11-5]
 */
public class StateBean
{
	private int					mStateId;
	private String				mStateName;
	private String				mLabel;

	private ArrayList<String>	mAlphabet;
	private ArrayList<CityBean>	mCities;
	private boolean				mIsAlphabet;

	public boolean isAlphabet()
	{
		return mIsAlphabet;
	}
	public void setIsAlphabet(boolean mIsAlphabet)
	{
		this.mIsAlphabet = mIsAlphabet;
	}
	public StateBean(int id, String name, String lable)
	{
		setStateId(id);
		setStateName(name);
		setLabel(lable);
	}
	public int getStateId()
	{
		return mStateId;
	}

	public void setStateId(int mStateId)
	{
		this.mStateId = mStateId;
	}

	public String getStateName()
	{
		return mStateName;
	}

	public void setStateName(String mStateName)
	{
		this.mStateName = mStateName;
	}

	public String getLabel()
	{
		return mLabel;
	}

	public void setLabel(String mLabel)
	{
		this.mLabel = mLabel;
	}
	public ArrayList<CityBean> getCities()
	{
		if (mCities == null)
		{
			mCities = new ArrayList<CityBean>();
		}
		return mCities;
	}

	public void setCities(ArrayList<CityBean> mCities)
	{
		this.mCities = mCities;
	}

	public ArrayList<String> getAlphabet()
	{
		if (mAlphabet == null)
		{
			mAlphabet = new ArrayList<String>();
		}
		return mAlphabet;
	}

	public void setAlphabet(ArrayList<String> mAlphabet)
	{
		this.mAlphabet = mAlphabet;
	}
}
