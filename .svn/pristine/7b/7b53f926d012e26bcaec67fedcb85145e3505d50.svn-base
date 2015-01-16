package com.jiubang.goscreenlock.theme.cjpcardcool.weather.search;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.SearchCitiesResultBean;

/**
 * 
 * 类描述: 搜索城市接口回调
 * 
 * @author  liuwenqin
 * @date  [2012-9-4]
 */
public interface SearchCityListener
{
	/**
	 * 功能简述:搜索与关键字匹配的城市的回调
	 * 功能详细描述:
	 * 注意:
	 * @param searchResult 搜索到的内容
	 * @param searchType 搜索类型。1，首页搜索；2，本地搜索；3，翻页搜索
	 */
	public void onSearchComplete(SearchCitiesResultBean searchResultBean, int searchType);

	/**
	 * 功能简述:无网络时的回调
	 * 功能详细描述:
	 * 注意:
	 */
	public void onSearchNoNetWorkConnection();

	/**
	 * 功能简述:搜索城市失败时的回调
	 * 功能详细描述:
	 * 注意:
	 */
	public void onSearchFailed();

	/**
	 * 功能简述:搜索城市没网络时的回调
	 * 功能详细描述:
	 * 注意:
	 */
	public void onSearchNoResult();
}
