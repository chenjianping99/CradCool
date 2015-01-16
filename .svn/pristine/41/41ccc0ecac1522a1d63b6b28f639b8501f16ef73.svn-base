package com.jiubang.goscreenlock.theme.cjpcardcool.weather.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * <br>类描述: 
 * 
 * @author  liuwenqin
 * @date  [2012-9-14]
 */
public class HttpUtil
{

	/** 输入流转为String */
	public static String transferInputStreamToString(InputStream is)
	{
		StringBuilder builder = new StringBuilder();
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = reader.readLine()) != null)
			{
				builder.append(line);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return builder.toString();
	}

	/**
	 * <br>功能简述: 读取输入流，转为字符串
	 * @param in
	 * @param charset 字符格式
	 * @return
	 * @throws IOException
	 */
	public static String readInputStream(InputStream in, String charset) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		final int bufferLength = 1024;
		byte[] buf = new byte[bufferLength];
		int len = 0;
		while ((len = in.read(buf)) > 0)
		{
			out.write(buf, 0, len);
		}
		byte[] data = out.toByteArray();
		in.close();
		out.close();
		return new String(data, charset);
	}
}