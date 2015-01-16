package com.jiubang.goscreenlock.theme.cjpcardcool.weather.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.text.TextUtils;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.CommonConstants;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.Util;

/**
 * 
 * <br>类描述: 业务请求
 * 
 * @author  liuwenqin
 * @date  [2012-9-17]
 */
public class Request
{

	/**请求链接的地址*/
	private final String			mUrl;
	/**以Get方式获取数据时的多个请求参数键值对*/
	private final ArrayList<Header>	mHeaders;
	/**Post请求方式的上传数据 */
	private byte[]					mPostData;
	/**Post请求方式是提交的表单数据**/
	private List<NameValuePair>		mPostParams;
	/**数据请求方式。Get/Post*/
	private String					mMethod;
	/**代表当前请求的ID，可以用在网络请求任务队列中，标识某个请求任务。暂时没用到*/
	@SuppressWarnings("unused")
	private final int				mTransactionID;
	/**标识客户端是否需要伪装成浏览器。防止访问第三方服务器时，客户端被屏蔽*/
	private boolean					mIsUseAgent;
	/**链接超时时间*/
	private int						mConnectionTimeOut	= Constants.CONNECTION_TIMEOUT;
	/**读取超时时间*/
	private int						mReadTimeOut		= Constants.READ_TIMEOUT;

	public Request(String url)
	{
		this.mUrl = url;
		this.mMethod = "GET";
		this.mHeaders = new ArrayList<Header>();
		this.mTransactionID = -1;
		this.mIsUseAgent = false;
	}

	public Request(String url, String method)
	{
		this.mUrl = url;
		this.mMethod = method;
		this.mHeaders = new ArrayList<Header>();
		this.mTransactionID = -1;
		this.mIsUseAgent = false;
	}

	public Request(String url, int connectionTimeOut, int readTimeOut)
	{
		this.mUrl = url;
		this.mMethod = "GET";
		this.mHeaders = new ArrayList<Header>();
		this.mTransactionID = -1;
		this.mIsUseAgent = false;
		if (connectionTimeOut > 0)
		{
			this.mConnectionTimeOut = connectionTimeOut;
		}
		if (readTimeOut > 0)
		{
			this.mReadTimeOut = readTimeOut;
		}
	}

	public boolean isUseAgent()
	{
		return mIsUseAgent;
	}

	public void setUseAgent(boolean isUseAgent)
	{
		this.mIsUseAgent = isUseAgent;
	}

	public int getConnectionTimeOut()
	{
		return mConnectionTimeOut;
	}

	public int getReadTimeOut()
	{
		return mReadTimeOut;
	}

	/**
	 * <br>功能简述: 添加请求参数
	 * @param key 请求参数键
	 * @param val 请求参数值
	 * @return 请求参数键值对
	 */
	public Header addHeader(String key, String val)
	{
		Header header = new Header(key, val);
		this.mHeaders.add(header);
		return header;
	}

	public void setPostData(byte[] postData)
	{
		this.mPostData = postData;
	}

	public byte[] getPostData()
	{
		return this.mPostData;
	}

	public List<NameValuePair> getPostParams()
	{
		return mPostParams;
	}

	public void setPostParams(List<NameValuePair> postParams)
	{
		this.mPostParams = postParams;
	}

	public void setMethod(String method)
	{
		this.mMethod = method;
	}

	public String getMethod()
	{
		return this.mMethod;
	}

	/**
	 * <br>功能简述: 组成完整的请求路径
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String composeCompleteURL() throws UnsupportedEncodingException
	{
		StringBuilder requestURL = new StringBuilder();
		requestURL.append(mUrl);
		int size = mHeaders.size();
		if (size > 0)
		{
			requestURL.append("?");
		}
		Header header = null;
		for (int i = 0; i < size; i++)
		{
			header = mHeaders.get(i);
			requestURL.append(URLEncoder.encode(header.getKey(), "utf-8"));
			requestURL.append("=");
			requestURL.append(URLEncoder.encode(header.getValue(), "utf-8"));
			if (i + 1 < size)
			{ //当前参数不是最后一个
				requestURL.append("&");
			}
		}
		return requestURL.toString();
	}

	public void addDefaultHeader(Context context, String language)
	{
		this.addHeader(Constants.STR_API_EXTRA_LAUGUAGE, TextUtils.isEmpty(language)
				? "en_US"
				: language);
		this.addHeader(Constants.STR_API_EXTRA_SYSTEM_VERSION, android.os.Build.VERSION.RELEASE);
		this.addHeader(Constants.STR_API_EXTRA_PROTOCOL_VERSION, Constants.PROTOCOL_VERSION);
		this.addHeader(Constants.STR_API_EXTRA_PROTOCOL_ID,
				String.valueOf(CommonConstants.WEATHER_INTERFACE_CHANNEL)); //新增代码
		this.addHeader(Constants.STR_API_EXTRA_PROTOCOL_VERSION_NAME, Util.getVersion(context)); //新增代码 补充应用的VersionName
	}
}