package com.jiubang.goscreenlock.theme.cjpcardcool.weather.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.telephony.TelephonyManager;

/**
 * 
 * 类描述: 获取城市位置用到的常量
 * 
 * @author liuwenqin
 * @date [2012-9-4]
 */
public class LocationConstants
{

	// ====================== 定位用到的常量 ======================

	/** 基站定位方式 */
	public static final int		WAY_CELL_LOCATION				= 1;
	/** 网络定位 */
	public static final int		WAY_NETWORK_LOCATION			= 2;
	/** GPS定位 */
	public static final int		WAY_GPS_LOCATION				= 3;

	/** 基站定位换为手机网络定位 */
	public static final int		CELL_TO_NETWORK					= 1;
	/** 手机网络定位换为GPS定位 */
	public static final int		NETWORK_TO_GPS					= 2;
	/** 基站定位换为GPS定位 */
	public static final int		CELL_TO_GPS						= 3;
	/** GPS定位切换为网络定位 */
	public static final int		GPS_TO_NETWORK					= 4;
	/** GPS定位方式切换为基站定位 */
	public static final int		GPS_TO_CELL						= 5;
	/** 网络定位方式切换为基站定位 */
	public static final int		NETWORK_TO_CELL					= 6;

	/** 系统不支持定位功能 */
	public static final int		LOCATION_UNSUPPORTED_BY_SYSYTEM	= 1;
	/** 网络定位没打开 */
	public static final int		LOCATION_NETWORK_UNABLED		= 2;
	/** GPS定位没打开 */
	public static final int		LOCATION_GPS_UNABLED			= 3;
	/** 网络定位和GPS定位都没有开启 */
	public static final int		LOCATION_ALL_UNABLED			= 4;
	/** 基站定位失败 */
	public static final int		LOCATION_CELL_FAILED			= 5;
	/** SIM卡不可用或者网络断开 */
	public static final int		LOCATION_SIMCARD_NOT_READY		= 6;
	/** 网络不可用 */
	public static final int		LOCATION_NETWORK_ERROR			= 7;
	/** 已经获取经纬度，但获取城市地址信息失败 */
	public static final int		LOCATION_CITY_INFO_NULL			= 8;
	/** 定位超时 */
	public static final int		LOCATION_TIME_OUT				= 9;

	/** 某项定位功能开启 */
	public static final int		LOCATION_PROVIDER_ENABLED		= 1;
	/** 某项定位功能关闭 */
	public static final int		LOCATION_PROVIDER_UNABLED		= 2;
	/** 某项定位功能系统不支持 */
	public static final int		LOCATION_PROVIDER_UNSUPPORTED	= 3;

	/** 英文搜索与关键字匹配的城市的请求链接 */
	public static final String	SEARCH_CITY_URL					= "weather/ensearch/searchcity.json";
	/** 协议版本号 */
	public static final String	LOCATION_PROTOCOL_VERSION		= "1.0";
	/** 外网服务器地址 */
	public static final String	LOCATION_SERVER_HOST			= "goweatherex.3g.cn";

	/** 语言类型 */
	/** 中文 */
	public static final String	ZH_CN							= "zh-CN";
	/** 英文 */
	public static final String	EN_US							= "en-US";

	/**连接超时时间*/
	public static final int		CONNECTION_TIMEOUT				= 15000;
	/**读取超时时间*/
	public static final int		READ_TIMEOUT					= 15000;

	public static final String	STR_HTTP						= "http://";
	public static final String	STR_API_GET_CONTINENTS			= "/goweatherex/guide/continent";
	public static final String	STR_API_GET_COUNTRIES			= "/goweatherex/guide/country";
	public static final String	STR_API_GET_STATES				= "/goweatherex/guide/state";
	public static final String	STR_API_GET_CITIES				= "/goweatherex/guide/cities";
	public static final String	STR_API_SEARCH_CITY				= "/goweatherex/city/search";
	public static final String	STR_API_GPS						= "/goweatherex/city/gps";

	public static final String	STR_API_EXTRA_LAUGUAGE			= "lang";
	public static final String	STR_API_EXTRA_ALPHABET			= "alphabet";
	public static final String	STR_API_EXTRA_CONTINENTID		= "continentid";
	public static final String	STR_API_EXTRA_COUNTRYID			= "countryid";
	public static final String	STR_API_EXTRA_STATEID			= "stateid";
	public static final String	STR_API_EXTRA_PROTOCOL_VERSION	= "ps";
	public static final String	STR_API_EXTRA_SYSTEM_VERSION	= "sys";
	public static final String	STR_API_EXTRA_KEYWORD			= "k";
	public static final String	PROTOCOL_VERSION				= "2.0";

	// ==============================================================

	public static SQLiteDatabase openDatabase(Context context, int go_city)
	{
		// 获得dictionary.db文件的绝对路径
		String dataBasePath = context.getFilesDir() + "/city";
		String databaseFilename = dataBasePath + "/" + "go_city.db";
		File dir = new File(dataBasePath);
		// 创建这个目录
		if (!dir.exists())
		{
			dir.mkdir();
		}
		SQLiteDatabase database = null;
		try
		{
			if (!(new File(databaseFilename)).exists())
			{
				// 获得封装go_city.db文件的InputStream对象
				InputStream is = context.getResources().openRawResource(go_city);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				final int bufferLength = 8192;
				byte[] buffer = new byte[bufferLength];
				int count = 0;
				// 开始复制dictionary.db文件
				while ((count = is.read(buffer)) > 0)
				{
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			// 打开/sdcard/dictionary目录中的go_city.db文件
			database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return database;
	}

	public static String readInputStream(InputStream in) throws IOException
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
		return new String(data, "UTF-8");
	}

	/** 获取sim卡状态 */
	public static int getSimState(Context context)
	{
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSimState();
	}

	/**
	 * 检查当前网络状态是否可用
	 * 
	 * @param mContext
	 * @return true，网络可用；false，网络不可用
	 */
	public static boolean isNetworkOK(Context mContext)
	{
		boolean result = false;
		if (mContext != null)
		{
			android.net.ConnectivityManager cm = (android.net.ConnectivityManager) mContext
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
	 * GPS或网络定位是否开启
	 * 
	 * @param provider
	 *            GPS_PROVIDER 或者 NETWORK_PROVIDER
	 * @return 1，定位已开启 2、定位关闭 3、系统不支持该定位功能
	 */
	public static int isProviderEnabled(LocationManager locationManager, String provider)
	{
		int providerStatus = LocationConstants.LOCATION_PROVIDER_UNABLED;
		/** 防止指定的Provider不存在 */
		try
		{
			if (locationManager.isProviderEnabled(provider))
			{
				providerStatus = LocationConstants.LOCATION_PROVIDER_ENABLED;
			}
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			providerStatus = LocationConstants.LOCATION_PROVIDER_UNSUPPORTED;
		}
		return providerStatus;
	}

	/** 获取设备ID */
	public static String getDeviceId(Context context)
	{
		return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
				.getDeviceId();
	}

	/**
	 * 获取客户端手机的语言,注意是帶地區的
	 * @return
	 */
	public static String getLanguage()
	{

		String language = "en_us";
		try
		{
			Locale locale = Locale.getDefault();
			language = locale.getLanguage() + "_" + locale.getCountry();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		return language;
	}

}