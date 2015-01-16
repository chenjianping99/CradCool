package com.jiubang.goscreenlock.theme.cjpcardcool.weather.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.zip.GZIPInputStream;

import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.text.TextUtils;

/**
 * 
 * 类描述: 网络操作类
 * 功能详细描述: 
 * 1.通过HttpExecutorContext.getHttpExecutor()获取本类实例
 * 2.构建Request和Result对象
 * 3.通过doRefresh获取数据
 * 4.完成后通过release释放连接
 * 
 * @author  liuwenqin
 * @date  [2012-9-17]
 */
public class HttpClientExecutor extends HttpExecutor
{
	private HttpClient	mHttpClient;
	private HttpEntity	mHttpEntity;
	/**网络代理地址*/
	private String		mProxyHost;

	public HttpClientExecutor()
	{
		super();
	}

	/**
	 * <br>功能简述: 执行网络请求
	 * @param requestUrl 请求URL
	 * @param request 请求对象
	 * @param result 请求结果对象
	 */
	public InputStream doRefresh(String requestUrl, Request request, Result result)
	{
		InputStream inputStream = null;
		if (!TextUtils.isEmpty(requestUrl)
				&& (requestUrl.startsWith("http://") || requestUrl.startsWith("https://")))
		{
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, request.getConnectionTimeOut());
			HttpConnectionParams.setSoTimeout(httpParams, request.getReadTimeOut());
			if (mProxyHost != null && mProxyHost.length() > 0)
			{
				//设置代理
				int proxyPort = android.net.Proxy.getDefaultPort();
				HttpHost proxy = new HttpHost(mProxyHost, proxyPort);
				httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
			mHttpClient = new DefaultHttpClient(httpParams);
			HttpRequestBase httpRequest = null;
			if (request.getMethod().equalsIgnoreCase("GET"))
			{
				httpRequest = new HttpGet(requestUrl);
				//声明支持压缩下发，优先下发压缩
				httpRequest.addHeader("accept-encoding", "gzip,deflate");
			}
			else if (request.getMethod().equalsIgnoreCase("POST"))
			{
				httpRequest = new HttpPost(requestUrl);
				if (request.getPostData() != null)
				{
					ByteArrayEntity byteEntity = new ByteArrayEntity(request.getPostData());
					((HttpPost) httpRequest).setEntity(byteEntity);
				}
				else if (null != request.getPostParams())
				{
					try
					{
						((HttpPost) httpRequest).setEntity(new UrlEncodedFormEntity(request
								.getPostParams(), HTTP.UTF_8));
					}
					catch (UnsupportedEncodingException e)
					{
						e.printStackTrace();
					}
				}
			}
			if (httpRequest != null)
			{
				if (request.isUseAgent())
				{
					//设置伪装成浏览器
					httpRequest.addHeader("User-Agent", Constants.USER_AGENT);
				}
				HttpResponse httpResponse = null;
				try
				{
					httpResponse = mHttpClient.execute(httpRequest);
				}
				catch (ClientProtocolException e)
				{
					e.printStackTrace();
					result.setStatus(HttpRequestStatus.REQUEST_PROTOCOL_EXCEPTION);
				}
				catch (ConnectTimeoutException e)
				{
					e.printStackTrace();
					result.setStatus(HttpRequestStatus.REQUEST_TIMEOUT);
				}
				catch (SocketTimeoutException e)
				{
					e.printStackTrace();
					result.setStatus(HttpRequestStatus.REQUEST_TIMEOUT);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					result.setStatus(HttpRequestStatus.REQUEST_IO_EXCEPTION);
				}
				catch (NullPointerException e)
				{
					// TODO httpclient-4.0之前的系统代码在类DefaultRequestDirector.execute()方法存在空指针的Bug
					e.printStackTrace();
					httpRequest.abort();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				if (httpResponse != null)
				{
					HeaderIterator headerIterator = httpResponse.headerIterator();
					if (headerIterator != null)
					{
						org.apache.http.Header header = null;
						while (headerIterator.hasNext())
						{
							header = headerIterator.nextHeader();
							String key = header.getName().toLowerCase();
							String value = header.getValue();
							if (key != null
									&& (key.equalsIgnoreCase("Content-Encoding") || key
											.equalsIgnoreCase("X_Enc")))
							{
								if (value.indexOf("gzip") != -1 || value.indexOf("x-gzip") != -1)
								{
									result.setBGzip(true);
									break;
								}
							}
						}
					}

					int status = httpResponse.getStatusLine().getStatusCode();
					if (status == HttpStatus.SC_OK)
					{
						inputStream = processResponse(httpResponse, result);
					}
					else
					{
						result.setStatus(HttpRequestStatus.REQUEST_HTTP_FAILED);
					}
				}
			}
		}
		else
		{
			// 请求URL不合法
			result.setStatus(HttpRequestStatus.REQUEST_HTTP_FAILED);
		}
		return inputStream;
	}

	private InputStream processResponse(HttpResponse httpResponse, Result result)
	{
		InputStream inputStream = null;
		mHttpEntity = httpResponse.getEntity();
		if (mHttpEntity != null)
		{
			try
			{
				inputStream = mHttpEntity.getContent();
			}
			catch (IllegalStateException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				result.setStatus(HttpRequestStatus.REQUEST_IO_EXCEPTION);
			}
			if (inputStream != null && result.getBGzip())
			{
				try
				{
					inputStream = new GZIPInputStream(inputStream);
				}
				catch (IOException e)
				{
					//解压数据流出错，关闭输入流，置空
					e.printStackTrace();
					try
					{
						inputStream.close();
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
					inputStream = null;
					result.setStatus(HttpRequestStatus.REQUEST_ZIP_ERROR);
				}
			}
		}
		return inputStream;
	}

	/**
	 * <br>功能简述: 关闭连接对象，释放资源
	 */
	public void release()
	{
		if (mHttpEntity != null)
		{
			try
			{
				mHttpEntity.consumeContent();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			mHttpEntity = null;
		}

		if (mHttpClient != null)
		{
			mHttpClient.getConnectionManager().shutdown();
		}
	}

}