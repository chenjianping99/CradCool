package com.jiubang.goscreenlock.theme.cjpcardcool.weather.http;

/**
 * 
 * <br>类描述:
 * 
 * @author  liuwenqin
 * @date  [2012-10-10]
 */
public class HttpExecutorContext
{

	/**
	 * <br>功能简述: 获取网络连接的执行者
	 * @param flag true，使用HttpClient；false，使用HttpURLConnection
	 * @return
	 */
	public static HttpExecutor getHttpExecutor()
	{
		boolean flag = true;
		if (flag)
		{
			return new HttpClientExecutor();
		}
		else
		{
			return new HttpURLConnectionExecutor();
		}
	}
}