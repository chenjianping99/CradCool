package com.jiubang.goscreenlock.theme.cjpcardcool.weather.search;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.CityBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.SearchCitiesResultBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.LocationConstants;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.Util;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.Constants;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.HttpExecutor;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.HttpExecutorContext;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.HttpRequestStatus;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.Request;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.Result;

/** 英文下，搜索城市 */
public class SearchCity extends AsyncTask<Object, Object, Result>
{

	/** 首次网络搜索 */
	public static final int			SEARCH_LS	= 0x01;
	/** 网络翻页搜索 */
	public static final int			SEARCH_MORE	= 0x02;
	/** 1，首次网络搜索；2，本地搜索；3，网络翻页搜索 */
	private int						mSearchType;
	/** 搜索城市的请求 */
	private Request					mRequest;

	/** 是否取消搜索。true，取消；false，不取消 */
	private volatile boolean		mCanceled;
	/** 每次搜索的结果列表 */
	private SearchCityListener		mSearchCityListener;
	/** 本次搜索的结果 */
	private SearchCitiesResultBean	mSearchResult;
	private Context					mContext;

	/**
	 * 功能简述: 网络搜索首页构造方法
	 * 功能详细描述:
	 * 注意:
	 * @param context context
	 * @param searchType 搜索类型
	 * @param searchCityListener 回调接口
	 * @param keyword 关键字
	 * @param language 当前语言，按照“语言_国家”的字符串，例如“en_US”
	 */
	public SearchCity(Context context, int searchType, SearchCityListener searchCityListener,
			String keyword, String language)
	{
		this.mContext = context;
		this.mSearchType = searchType;
		this.mRequest = getRequest(keyword, language);
		this.mSearchCityListener = searchCityListener;
		this.mSearchResult = new SearchCitiesResultBean();
		this.mCanceled = false;
	}

	/**
	 * 功能简述: 翻页搜索构造方法
	 * 功能详细描述:
	 * 注意:
	 * @param context context
	 * @param searchType 搜索类型
	 * @param searchCityListener 回调接口
	 * @param SearchCitiesResultBean 上一次搜索结果
	 */
	public SearchCity(Context context, int searchType, SearchCityListener searchCityListener,
			SearchCitiesResultBean prePage)
	{
		this.mContext = context;
		this.mSearchType = searchType;
		this.mRequest = new Request(prePage.getMoreUrl());
		this.mSearchCityListener = searchCityListener;
		this.mSearchResult = new SearchCitiesResultBean(prePage);
		this.mCanceled = false;
	}

	@Override
	protected Result doInBackground(Object... params)
	{
		Result result = getSearchCity();
		return result;
	}

	@Override
	protected void onPostExecute(Result result)
	{
		super.onPostExecute(result);
		// 置搜索请求结束时间
		result.setRequestEndTime(SystemClock.elapsedRealtime());
		if (!mCanceled && mSearchCityListener != null)
		{
			switch (result.getStatus())
			{
				case HttpRequestStatus.REQUEST_NO_DATA :
					mSearchCityListener.onSearchNoResult();
					break;
				case HttpRequestStatus.REQUEST_SUCCESS :
					mSearchCityListener.onSearchComplete(mSearchResult, mSearchType);
					break;
				case HttpRequestStatus.REQUEST_NETWORK_UNAVAILABLE :
					mSearchCityListener.onSearchNoNetWorkConnection();
					break;

				// 其他结果都认为是网络错误
				default :
					mSearchCityListener.onSearchFailed();
					break;
			}
		}
	}

	/**
	 * 取消
	 */
	public void cancel()
	{
		this.mCanceled = true;
		mSearchCityListener = null;
	}

	/**
	 * 功能简述: 通过Url获取搜索结果
	 * 功能详细描述:
	 * 注意: 
	 * @param url
	 * @return 搜索结果代码
	 */
	private Result getSearchCity()
	{
		String json = null;
		Result result = new Result();
		result.setStatus(HttpRequestStatus.REQUEST_HTTP_FAILED);

		HttpExecutor httpExecutor = HttpExecutorContext.getHttpExecutor();
		if (!mCanceled)
		{
			if (!httpExecutor.checkNetwork(result, mContext))
			{
				result.setStatus(HttpRequestStatus.REQUEST_NETWORK_UNAVAILABLE);
			}
			else
			{
				// 置搜索请求开始时间
				result.setRequestStartTime(SystemClock.elapsedRealtime());
				InputStream inputStream = null;
				try
				{
					inputStream = httpExecutor.doRefresh(mRequest.composeCompleteURL(), mRequest,
							result);
				}
				catch (UnsupportedEncodingException e1)
				{
					e1.printStackTrace();
				}
				if (inputStream != null)
				{
					try
					{
						if (!mCanceled)
						{
							json = Util.transferInputStreamToString(inputStream);
							parseSearchResultJSON(json, result);
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
						result.setStatus(HttpRequestStatus.REQUEST_IO_EXCEPTION);
					}
					catch (JSONException e)
					{
						e.printStackTrace();
						result.setStatus(HttpRequestStatus.REQUEST_IO_EXCEPTION);
					}
					finally
					{
						try
						{
							inputStream.close();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
				httpExecutor.release();
			}
		}
		return result;
	}

	/**
	 * 功能简述:搜索与关键字匹配的城市，解析服务器返回的json文件
	 * 功能详细描述:
	 * 注意:
	 * @param json
	 * @param result
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	private void parseSearchResultJSON(String json, Result result) throws JSONException,
			UnsupportedEncodingException
	{
		JSONObject rootObject = new JSONObject(json);
		JSONObject headObject = rootObject.getJSONObject("head");
		int serverResult = headObject.getInt("result");
		if (serverResult == 0)
		{
			result.setStatus(HttpRequestStatus.REQUEST_NO_DATA);
		}
		if (serverResult == 1)
		{
			// 查询成功
			// cities字段
			JSONArray citiesArray = rootObject.getJSONArray("cities");
			if (citiesArray != null)
			{
				int length = citiesArray.length();
				for (int i = 0; i < length; i++)
				{
					JSONObject cityObject = citiesArray.getJSONObject(i);
					String cityName = cityObject.getString("city");
					String id = cityObject.getString("cityId");
					String stateName = cityObject.getString("state");
					String countryName = cityObject.getString("country");
					String timeZone = cityObject.getString("timeZone");
					String label = cityName + ", " + stateName + ", (" + countryName + ")";
					mSearchResult.getCities().add(
							new CityBean(id, cityName, stateName, countryName, timeZone, label));
				}
			}

			// more字段
			String more = rootObject.getString("more");
			if (!more.equalsIgnoreCase("NULL"))
			{
				mSearchResult.setMoreUrl(more);
				mSearchResult.setIsMutliPage(true);
			}
			else
			{
				mSearchResult.setMoreUrl(null);
				mSearchResult.setIsMutliPage(false);
			}
			result.setStatus(HttpRequestStatus.REQUEST_SUCCESS);
		}
	}

	private Request getRequest(String keyword, String language)
	{

		/**
		 * 搜素:
		 *     http://goweatherex.3g.cn/goweatherex/city/search?k=hongkong&lang=en&sys=4.0.4&ps=2.0
		 */
		StringBuilder baseUrl = new StringBuilder();
		baseUrl.append(LocationConstants.STR_HTTP).append(LocationConstants.LOCATION_SERVER_HOST)
				.append(LocationConstants.STR_API_SEARCH_CITY);

		Request req = new Request(baseUrl.toString());
		req.addDefaultHeader(mContext, language);
		req.addHeader(Constants.STR_API_EXTRA_KEYWORD, keyword);

		return req;
	}

}
