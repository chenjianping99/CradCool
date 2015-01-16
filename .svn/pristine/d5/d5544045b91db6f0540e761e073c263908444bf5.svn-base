package com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean;

import java.util.ArrayList;
/**
 * 
 * 类描述:保存搜索城市结果的Bean
 * 功能详细描述:
 * 
 * @author  chenyuning
 * @date  [2012-11-5]
 */
public class SearchCitiesResultBean
{
	/** 搜索结果*/
	private ArrayList<CityBean>		mCities;
	/** 下一页链接*/
	private String					mMoreUrl;
	/** 下一页是否存在*/
	private boolean					mNextPageExisted;
	@Deprecated
	/** 上一页的搜索结果*/
	private SearchCitiesResultBean	mPrePage	= null;

	/** 上一页是否存在*/
	private boolean					mIsPrePageExisted;

	public SearchCitiesResultBean()
	{
		mNextPageExisted = false;
		mIsPrePageExisted = false;
		mMoreUrl = null;
		mCities = new ArrayList<CityBean>();
	}

	public SearchCitiesResultBean(SearchCitiesResultBean prePage)
	{
		mNextPageExisted = false;
		mIsPrePageExisted = true;
		mMoreUrl = null;
		mCities = new ArrayList<CityBean>();
		if (prePage != null)
		{
			for (CityBean city : prePage.getCities())
			{
				mCities.add(city);
			}
		}
	}

	//	 往前翻页需要的构造方法，3.1版本废弃
	//		public SearchCitiesResultBean(SearchCitiesResultBean prePage) {
	//			mNextPageExisted = false;
	//			mPrePage = prePage;
	//		}

	@Deprecated
	public SearchCitiesResultBean getPrePage()
	{
		return mPrePage;
	}

	@Deprecated
	public void setPrePage(SearchCitiesResultBean prePage)
	{
		mPrePage = prePage;
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
		if (mCities != null)
		{
			mCities = new ArrayList<CityBean>();
		}
		this.mCities = mCities;
	}
	public String getMoreUrl()
	{
		return mMoreUrl;
	}
	public void setMoreUrl(String mMoreUrl)
	{
		this.mMoreUrl = mMoreUrl;
	}
	public boolean isMutliPage()
	{
		return mNextPageExisted;
	}
	public void setIsMutliPage(boolean mIsMutliPage)
	{
		this.mNextPageExisted = mIsMutliPage;
	}

	public void clear()
	{
		if (mCities != null)
		{
			mCities.clear();
		}
		mCities = null;
		mNextPageExisted = false;
		mMoreUrl = null;
	}

	public boolean isPrePageExisted()
	{
		return mIsPrePageExisted;
	}

	public void setIsPrePageExisted(boolean mIsPrePageExisted)
	{
		this.mIsPrePageExisted = mIsPrePageExisted;
	}
}