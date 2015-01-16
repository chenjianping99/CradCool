package com.jiubang.goscreenlock.theme.cjpcardcool.weather.tianqi;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.content.Context;
import android.os.SystemClock;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.City;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.CommonConstants;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.WeatherConstants;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.HttpExecutor;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.HttpExecutorContext;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.HttpRequestStatus;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.Request;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.Result;

/**
 * 
 * 类描述: 同步刷新天气对象
 * 
 * @author liuwenqin
 * @date [2012-9-4]
 */
public class SyncWeatherInfoLoader
{
	/** 是否采用12小时制。是，true；否，false */
	private boolean				mIs12Hour;
	private Context				mContext;
	private City				mCityRefresh;
	private RemoteInfoParser	mParser;
	private IHttpConnListener	mListener;
	private int					mRefreshCount	= 0;
	/** 总共尝试3次 */
	private final int			mTotalTrials	= 3;
	private Request				mRequest;
	private ArrayList<Result>	mResults;

	private String				mLanguage;

	public SyncWeatherInfoLoader(Context context, City cityRefresh, IHttpConnListener listener)
	{
		
		this.mContext = context;
		this.mCityRefresh = cityRefresh;
		this.mListener = listener;
		this.mParser = new RemoteInfoParser(mCityRefresh);
		this.mResults = new ArrayList<Result>();
	}

	public void setHourFormat(boolean hourFormat)
	{
		this.mIs12Hour = hourFormat;
	}

	public void setLanguage(String language)
	{
		this.mLanguage = language;
	}

	/**
	 * <br>功能简述: 生成刷新天气的请求头部
	 * @return
	 */
	private Request composeRequestHeader()
	{
		Request request = new Request("http://" + WeatherConstants.WEATHER_SERVER_HOST
				+ WeatherConstants.GET_WEATHER_URL);
		request.addDefaultHeader(mContext, mLanguage);
		request.addHeader("w", mCityRefresh.getCode());
		request.addHeader("h", mIs12Hour
				? CommonConstants.HOUR_FORMAT_12
				: CommonConstants.HOUR_FORMAT_24);
		request.addHeader("timestamp", String.valueOf(mCityRefresh.getLastUpdateTimestamp()));
		return request;
	}

	/** 启动天气刷新 */
	public void start()
	{
		//初始化请求对象和结果对象
		mRequest = composeRequestHeader();
		String requestUrl = null;
		try
		{
			requestUrl = mRequest.composeCompleteURL();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		/** 总共3次尝试机会 */
		while (mRefreshCount < mTotalTrials)
		{
			Result result = refresh(requestUrl);
			int status = result.getStatus();
			mResults.add(result);
			if (status == HttpRequestStatus.REQUEST_SUCCESS)
			{
				// 获取到天气数据并解析成功
				mListener.onSuccess(mCityRefresh, mResults);
				break;
			}
			else if (status == HttpRequestStatus.REQUEST_DATA_LATEST)
			{
				// 时间戳一致，没有新的天气数据
				mListener.onNoNewData(mResults);
				break;
			}
			else if (status == HttpRequestStatus.REQUEST_NETWORK_UNAVAILABLE)
			{
				// 网络不可用
				mListener.onNetworkUnavailable(mResults);
				break;
			}
			else
			{
				if (status == HttpRequestStatus.REQUEST_SERVER_ERROR || mRefreshCount > 1)
				{
					// 服务器暂时无法获取该城市的天气信息，需要缓存30分钟后再取或者已经尝试3次获取天气信息
					mListener.onErrorGeneral(mResults);
					break;
				}
				else
				{
					// 还有 (2 - mRefreshCount)次尝试的机会
					mRefreshCount++;
					continue;
				}
			}
		}
	}

	/**
	 * 执行刷新请求，并返回本次刷新的结果
	 * @param requestUrl 执行刷新请求的url
	 * @return
	 */
	private Result refresh(String requestUrl)
	{
		Result result = new Result();
		HttpExecutor httpExecutor = HttpExecutorContext.getHttpExecutor();
		boolean isNetWorkAvailable = httpExecutor.checkNetwork(result, mContext);
		if (isNetWorkAvailable)
		{
			//记录开始刷新的时间
			result.setRequestStartTime(SystemClock.elapsedRealtime());
			InputStream is = httpExecutor.doRefresh(requestUrl, mRequest, result);
			if (is != null)
			{
				// 将数据流转化为json，并进行解析
				mParser.parseJSON(is, result);
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					is = null;
				}
			}
		}
		else
		{
			result.setStatus(HttpRequestStatus.REQUEST_NETWORK_UNAVAILABLE);
		}
		httpExecutor.release();
		final int status = result.getStatus();
		if (status == HttpRequestStatus.REQUEST_SUCCESS)
		{ //清除上次获取天气信息失败的残余信息
			if (mCityRefresh.getForecastInfoCount() <= 0)
			{ // 获取到的城市未来几天的天气预报天数少于1天，视为失败
				result.setStatus(HttpRequestStatus.REQUEST_FAILED);
			}
		}
		else if (status == HttpRequestStatus.REQUEST_DATA_LATEST)
		{
			//已经是最新数据，不做任何事情
		}
		else
		{
			//所有失败情况，清理数据
			mCityRefresh.clear();
		}
		return result;
	}
}