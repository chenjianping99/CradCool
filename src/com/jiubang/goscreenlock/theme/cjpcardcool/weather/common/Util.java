package com.jiubang.goscreenlock.theme.cjpcardcool.weather.common;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 
 * 类描述: 获取天气信息工具类
 * 
 * @author liuwenqin
 * @date [2012-9-4]
 */
public class Util
{

	/**
	 *  获取sim卡状态
	 */
	public static int getSimState(Context context)
	{
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSimState();
	}

	/**
	 *  获取当前手机系统的语言
	 *  @return 返回按照“语言_国家”的字符串
	 */
	public static String getCurLanguage(Context context)
	{
		String defaultLanguage = "en_US";
		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		String country = locale.getCountry();
		if (language != null && country != null)
		{
			defaultLanguage = language + "_" + country;
		}
		else if (language != null)
		{
			defaultLanguage = language;
		}
		return defaultLanguage;
	}

	/**
	 * 识别当前系统是中文系统或英文系统。返回系统语言类型
	 */
	public static int getLanguageType(Context context)
	{
		Locale locale = context.getResources().getConfiguration().locale;
		String lang = locale.getCountry();
		if (lang.indexOf("CN") != -1 && locale.getLanguage().equalsIgnoreCase("zh"))
		{
			return CommonConstants.LANGUAGE_CN;
		}
		else
		{
			return CommonConstants.LANGUAGE_EN;
		}
	}

	/**
	 *  输入流转为String
	 */
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
	 *  输入流转为String
	 */
	public static String readInputStream(InputStream in) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] data = null;
		try
		{
			final int bufferLength = 1024;
			byte[] buf = new byte[bufferLength];
			int len = 0;
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			data = out.toByteArray();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (in != null)
			{
				in.close();
				out.close();
			}
		}
		return new String(data, "UTF-8");
	}

	/**
	 * 此方法用于判断用户是否为国内大陆境内用户,
	 * 判定规则为:
	 * 若有sim卡则通过获取网络运营商号码判断( 中国大陆的前5位是(46000) 中国移动：46000、46002 中国联通：46001 中国电信：46003),
	 * 若无sim卡则通过手机语言区域信息判断
	 * @param context
	 * @return true for yes, false for no
	 */
	public static boolean isInternalUser(Context context)
	{
		boolean result = false;
		if (context != null)
		{
			// 从系统服务上获取了当前网络的MCC(移动国家号)，进而确定所处的国家和地区
			TelephonyManager manager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			// SIM卡状态
			boolean simCardUnable = manager.getSimState() != TelephonyManager.SIM_STATE_READY;
			String simOperator = manager.getSimOperator();
			// 如果没有SIM卡的话simOperator为null，然后获取本地信息进行判断处理
			if (simCardUnable || TextUtils.isEmpty(simOperator))
			{
				// 获取当前国家或地区，如果当前手机设置为简体中文-中国，则使用此方法返回CN
				if (getLanguageType(context) == CommonConstants.LANGUAGE_CN)
				{
					// 如果获取的国家信息是CN，则返回TRUE
					result = true;
				}
				else
				{
					// 如果获取不到国家信息，或者国家信息不是CN
					result = false;
				}

			}
			// 如果有SIM卡，并且获取到simOperator信息。
			else if (simOperator.startsWith("460"))
			{
				//中国大陆的前5位是(46000) 中国移动：46000、46002 中国联通：46001 中国电信：46003
				result = true;
			}
		}
		return result;
	}

	/**
	 * 检查当前网络状态是否可用
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean isNetworkOK(Context mContext)
	{
		boolean result = false;
		if (mContext != null)
		{
			ConnectivityManager cm = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm != null)
			{
				android.net.NetworkInfo networkInfo = cm.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.isConnected())
				{
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * 获取当前软件的版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersion(Context context)
	{
		String versionName = null;
		PackageManager pm = context.getPackageManager();
		try
		{
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = info.versionName;
			if (versionName == null || versionName.length() < 0)
			{
				versionName = "";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			versionName = "";
		}
		return versionName;
	}

}