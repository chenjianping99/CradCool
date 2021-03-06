package com.jiubang.goscreenlock.theme.cjpcardcool.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
/**
 * 
 * @author jiangxuwen
 *
 */
// CHECKSTYLE:OFF
public class Machine
{
	public static int				LEPHONE_ICON_SIZE			= 72;
	private static boolean			sCheckTablet				= false;
	private static boolean			sIsTablet					= false;

	// 硬件加速
	public static int				LAYER_TYPE_NONE				= 0x00000000;
	public static int				LAYER_TYPE_SOFTWARE			= 0x00000001;
	public static int				LAYER_TYPE_HARDWARE			= 0x00000002;
	public static boolean			IS_FROYO					= Build.VERSION.SDK_INT >= 8;
	public static boolean			IS_HONEYCOMB				= Build.VERSION.SDK_INT >= 11;
	public static boolean			IS_HONEYCOMB_MR1			= Build.VERSION.SDK_INT >= 12;
	public static boolean			IS_ICS						= Build.VERSION.SDK_INT >= 14;
	public static boolean			IS_ICS_MR1					= Build.VERSION.SDK_INT >= 15
																		&& Build.VERSION.RELEASE
																				.equals("4.0.4");		// HTC oneX 4.0.4系统
	public static boolean			IS_JELLY_BEAN				= Build.VERSION.SDK_INT >= 16;
	public static final boolean		IS_JELLY_BEAN_3				= Build.VERSION.SDK_INT >= 18;			//4.3
	public static final boolean		IS_SDK_ABOVE_KITKAT			= Build.VERSION.SDK_INT >= 19;			//sdk是否4.4或以上
	public static boolean			sLevelUnder3				= Build.VERSION.SDK_INT < 11;			// 版本小于3.0
	private static Method			sAcceleratedMethod			= null;

	private final static String		LEPHONEMODEL[]				= { "3GW100", "3GW101", "3GC100",
			"3GC101"											};
	private final static String		MEIZUBOARD[]				= { "m9", "M9", "mx", "MX" };
	private final static String		M9BOARD[]					= { "m9", "M9" };
	private final static String		ONE_X_MODEL[]				= { "HTC One X", "HTC One S",
			"HTC Butterfly", "HTC One XL", "htc one xl", "HTC Droid Incredible 4G LTE", "HTC 802w" };

	private final static String		HTC_MODEL[]					= { "HTC", "htc", "Htc" };
	
	private final static String		SAMSUNG_MODEL[]					= {"SAMSUNG GALAXY S4"};

	private final static String		USE_3DCORE_DEVICE_MODEL[]	= { "GT-I9300", "GT-N7000",
			"GT-I9100", "m0", "d2att", "d2spr", "d2vzw", "d2tmo", "SGH-T989", "SHW-M250S", "c1skt",
			"c1ktt", "SHV-E160S", "SPH-D710", "c1lgt", "d2can", "SHW-M250K", "gt-i9300", "m0skt",
			"s2vep", "SHV-E160K", "s2ve", "GT-I9100T", "SHV-E120S", "SGH-I717", "SHW-M250L",
			"SHV-E120L", "d2dcm", "d2ltetmo", "d2usc", "GT-I9103", "d2mtr", "SGH-I777",
			"SHV-E120K", "d2cri", "SCH-R760", "galaxy s3", "SC-03E", "d2vmu", "SC-02C", "SC-05D",
			"SGH-T989D", "SC-03D"								};										// 三星S3、三星Note、三星S2
	private final static String		KITKAT_WITHOUT_NAVBAR[]		= { "xt1030", "xt1080",
			"droid ultra", "droid maxx"						};

	//对以下机型，老用户升级后，如果之前是2D模式，自动转成3D模式，目前是S2、S3、S4
	private final static String		RECOMMEND_2D_TO_3D_MODEL[]	= { "GT-I9100", "GT-I9100T",
			"GT-I9103", "GT-I9210", "SC-02C", "SC-03D", "SGH-I727R", "SGH-I777", "SGH-T989",
			"SGH-T989D", "SHV-E120K", "SHV-E120L", "SHV-E120S", "SHW-M250K", "SHW-M250L",
			"SHW-M250S", "SPH-D710", "ISW11SC", "SCH-R760", "SGH-I757M", "logandsdtv", "s2ve",
			"s2vep", "SGH-I727", "GT-I9300", "gt-i9300", "GT-9300", "gt-i9500", "galaxy s4",
			"galaxy s3", "GT - I9300", "GT -I9300", "GT- I9300", "GT I9500", "GT- I9500",
			"GT_I9300", "GT-9300i", "GTI9300", "GT-I9300b", "GT-I9300G", "GT-I9300S", "GT-I9500G",
			"GT-l9300", "GT-l9500", "i9300", "i9500", "s3 19300", "S4_GT-I9500_Samsung",
			"S4_GT-I9502_Samsung", "s8977_GT-I9500_Samsung", "s8977a_GT-I9500_Samsung",
			"S8977ab_GT-I9502_Samsung", "S8977b_GT-I9502_Samsung", "S8977b_GT-I9502_Samsungg",
			"SAMSUNG GALASY S4", "SAMSUNG GALAXY S III GT-I9300", "Samsung galaxy s3",
			"Samsung Galaxy S3 (c1att)", "Samsung Galaxy S3 (c1ktt)", "Samsung Galaxy S3 (c1lgt)",
			"Samsung Galaxy S3 (c1skt)", "Samsung Galaxy S3 (d2att)", "Samsung Galaxy S3 (d2can)",
			"Samsung Galaxy S3 (d2cri)", "Samsung Galaxy S3 (d2dcm)",
			"Samsung Galaxy S3 (d2lteMetroPCS)", "Samsung Galaxy S3 (d2ltetmo)",
			"Samsung Galaxy S3 (d2mtr)", "Samsung Galaxy S3 (d2spi)", "Samsung Galaxy S3 (d2spr)",
			"Samsung Galaxy S3 (d2tfnspr)", "Samsung Galaxy S3 (d2tfnvzw)",
			"Samsung Galaxy S3 (d2tmo)", "Samsung Galaxy S3 (d2usc)", "Samsung Galaxy S3 (d2vmu)",
			"Samsung Galaxy S3 (d2vzw)", "Samsung Galaxy S3 (d2xar)", "Samsung Galaxy S3 (m0)",
			"Samsung Galaxy S3 (m0apt)", "Samsung Galaxy S3 (m0chn)", "Samsung Galaxy S3 (m0cmcc)",
			"Samsung Galaxy S3 (m0ctc)", "Samsung Galaxy S3 (m0ctcduos)",
			"Samsung Galaxy S3 (m0skt)", "Samsung Galaxy S3 (m3)", "Samsung Galaxy S3 (m3dcm)",
			"Samsung Galaxy S3 (SC-03E)", "samsung galaxy s4", "Samsung Galaxy S4 (ja3g)",
			"Samsung Galaxy S4 (ja3gduosctc)", "Samsung Galaxy S4 (jaltektt)",
			"Samsung Galaxy S4 (jaltelgt)", "Samsung Galaxy S4 (jalteskt)",
			"Samsung Galaxy S4 (jflte)", "Samsung Galaxy S4 (jflteaio)",
			"Samsung Galaxy S4 (jflteatt)", "Samsung Galaxy S4 (jfltecan)",
			"Samsung Galaxy S4 (jfltecri)", "Samsung Galaxy S4 (jfltecsp)",
			"Samsung Galaxy S4 (jfltelra)", "Samsung Galaxy S4 (jflterefreshspr)",
			"Samsung Galaxy S4 (jfltespr)", "Samsung Galaxy S4 (jfltetfntmo)",
			"Samsung Galaxy S4 (jfltetmo)", "Samsung Galaxy S4 (jflteusc)",
			"Samsung Galaxy S4 (jfltevzw)", "Samsung Galaxy S4 (jftdd)",
			"Samsung Galaxy S4 (jfwifi)", "Samsung Galaxy S4 (ks01lte)",
			"Samsung Galaxy S4 (ks01ltektt)", "Samsung Galaxy S4 (ks01ltelgt)",
			"Samsung Galaxy S4 (ks01lteskt)", "Samsung Galaxy S4 (SC-04E)",
			"Samsung Galaxy S4 Active (jactivelte)", "Samsung Galaxy S4 Active (jactivelteatt)",
			"Samsung Galaxy S4 Active (jactivelteskt)", "Samsung Galaxy S4 Duos (ja3gchnduos)",
			"Samsung Galaxy S4 Google Play Edition (jgedlte)", "Samsung Galaxy S4_gsm",
			"Samsung galaxy SII", "Samsung Galaxy SII Skyrocket (SGH-I727)", "SAMSUNG Galaxy SIII",
			"Samsung GT-I9300", "samsung GT-I9500", "Samsung i9500", "Samsung S3", "samsung S4",
			"Samsung Samsung Galaxy S4 (jflteMetroPCS)", "samsung_galaxy_s3", "Samsung_Galaxy_S4",
			"SAMSUNG_GT-I9500", "Samsung_I9300", "SAMSUNG-GTI9300", "SAMSUNG-I9500",
			"Samung_I9300", "SGH-i9300", "SHV-I9300"			};

	private final static String		PAY_NOT_BY_GETJER_COUNTRY[]	= { "us", "gb", "de", "ru", "jp",
			"au", "fr", "it", "ca", "br", "es", "se", "tw", "mx", "nl", "no", "kr", "cn" };			//不通过亚太付费规则购买付费功能的国家

	private final static String		AD_TEST_COUNTRY[]			= { "RU", "CA", "GB", "FR", "DE",
			"ES", "IT", "NL"									};										//广告测试国家

	private static boolean			sSupportGLES20				= false;
	private static boolean			sDetectedDevice				= false;

	// 用于判断设备是否支持绑定widget
	private static boolean			sSupportBindWidget			= false;
	// 是否已经进行过绑定widget的判断
	private static boolean			sDetectedBindWidget			= false;

	public final static String[]	S5360_MODEL					= { "GT-S5360" };

	private static String			sInstallDate				= null;								//apk安装时间,缓存起来，避免每次IO访问
	private static boolean			sDetectedSupportAPITransparentStatusBar;
	private static boolean			sIsSupportAPITransparentStatusBar;

	public static boolean isLephone()
	{
		final String model = android.os.Build.MODEL;
		if (model == null)
		{
			return false;
		}
		final int size = LEPHONEMODEL.length;
		for (int i = 0; i < size; i++)
		{
			if (model.equals(LEPHONEMODEL[i]))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isM9()
	{
		return isPhone(M9BOARD);
	}
	public static boolean isMeizu()
	{
		return isPhone(MEIZUBOARD);
	}
	public static boolean isONE_X()
	{
		return isModel(ONE_X_MODEL);
	}

	/**
	 * 检查机型是否需要默认开启3DCore
	 * @return bool
	 */
	public static boolean needToOpen3DCore()
	{
		//V4.05开始默认全部开放3DCore
		return true;//checkModel(USE_3DCORE_DEVICE_MODEL) || IS_JELLY_BEAN;
	}

	/**
	 * 检查是否符合特定机型，打开2D转3D的引导通知
	 */
	public static boolean recommendOpen3DCore()
	{
		return isModel(RECOMMEND_2D_TO_3D_MODEL);
	}

	private static boolean isPhone(String[] boards)
	{
		final String board = android.os.Build.BOARD;
		if (board == null)
		{
			return false;
		}
		final int size = boards.length;
		for (int i = 0; i < size; i++)
		{
			if (board.equals(boards[i]))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isHtcPhone()
	{
		final String board = android.os.Build.BOARD;
		if (board == null)
		{
			return false;
		}
		final int size = HTC_MODEL.length;
		for (int i = 0; i < size; i++)
		{
			if (board.contains(HTC_MODEL[i]))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isS4Phone()
	{
		final String board = android.os.Build.BOARD;
		if (board == null)
		{
			return false;
		}
		final int size = SAMSUNG_MODEL.length;
		for (int i = 0; i < size; i++)
		{
			if (board.toUpperCase().contains(SAMSUNG_MODEL[i]))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isSimilarModel(String[] models)
	{
		final String board = android.os.Build.MODEL;
		if (board == null)
		{
			return false;
		}
		final int size = models.length;
		try
		{
			for (int i = 0; i < size; i++)
			{
				if (board.contains(models[i]) || board.contains(models[i].toLowerCase())
						|| board.contains(models[i].toUpperCase()))
				{
					return true;
				}
			}
		}
		catch (Exception e)
		{
		}
		return false;
	}

	public static boolean isModel(String[] models)
	{
		final String board = android.os.Build.MODEL;
		if (board == null)
		{
			return false;
		}
		final int size = models.length;
		try
		{
			for (int i = 0; i < size; i++)
			{
				if (board.equals(models[i]) || board.equals(models[i].toLowerCase())
						|| board.equals(models[i].toUpperCase()))
				{
					return true;
				}
			}
		}
		catch (Exception e)
		{
		}
		return false;
	}

	/**
	 * 因为主题2.0新起进程，无法获取GoLauncher.getContext()， 所以重载此方法，以便主题2.0调用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isCnUser(Context context)
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

			if (simCardUnable || TextUtils.isEmpty(simOperator))
			{
				// 如果没有SIM卡的话simOperator为null，然后获取本地信息进行判断处理
				// 获取当前国家或地区，如果当前手机设置为简体中文-中国，则使用此方法返回CN
				String curCountry = Locale.getDefault().getCountry();
				if (curCountry != null && curCountry.contains("CN"))
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
			else if (simOperator.startsWith("460"))
			{
				// 如果有SIM卡，并且获取到simOperator信息。
				/**
				 * 中国大陆的前5位是(46000) 中国移动：46000、46002 中国联通：46001 中国电信：46003
				 */
				result = true;
			}
		}

		return result;
	}

	// 根据系统版本号判断时候为华为2.2 or 2.2.1, Y 则catch
	public static boolean isHuaweiAndOS2_2_1()
	{
		boolean resault = false;
		String androidVersion = Build.VERSION.RELEASE;// os版本号
		String brand = Build.BRAND;// 商标
		if (androidVersion == null || brand == null)
		{
			return resault;
		}
		if (brand.equalsIgnoreCase("Huawei")
				&& (androidVersion.equals("2.2") || androidVersion.equals("2.2.2")
						|| androidVersion.equals("2.2.1") || androidVersion.equals("2.2.0")))
		{
			resault = true;
		}
		return resault;
	}

	// 判断当前设备是否为平板
	private static boolean isPad(Context context)
	{
		//		if (DrawUtils.sDensity >= 1.5 || DrawUtils.sDensity <= 0) {
		//			return false;
		//		}
		//		if (DrawUtils.sWidthPixels < DrawUtils.sHeightPixels) {
		//			if (DrawUtils.sWidthPixels > 480 && DrawUtils.sHeightPixels > 800) {
		//				return true;
		//			}
		//		} else {
		//			if (DrawUtils.sWidthPixels > 800 && DrawUtils.sHeightPixels > 480) {
		//				return true;
		//			}
		//		}
		//		return false;
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isTablet(Context context)
	{
		if (sCheckTablet)
		{
			return sIsTablet;
		}
		sCheckTablet = true;
		sIsTablet = isPad(context);
		return sIsTablet;
	}

	/**
	 * 判断当前网络是否可以使用
	 * 
	 * @author huyong
	 * @param context
	 * @return
	 */
	public static boolean isNetworkOK(Context context)
	{
		boolean result = false;
		if (context != null)
		{
			try
			{
				ConnectivityManager cm = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				if (cm != null)
				{
					NetworkInfo networkInfo = cm.getActiveNetworkInfo();
					if (networkInfo != null && networkInfo.isConnected())
					{
						result = true;
					}
				}
			}
			catch (NoSuchFieldError e)
			{
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 设置硬件加速
	 * 
	 * @param view
	 * @param accelerate
	 */
	public static void setHardwareAccelerated(View view, int mode)
	{
		if (sLevelUnder3)
		{
			return;
		}
		try
		{
			if (null == sAcceleratedMethod)
			{
				sAcceleratedMethod = View.class.getMethod("setLayerType", new Class[] {
						Integer.TYPE, Paint.class });
			}
			sAcceleratedMethod.invoke(view, new Object[] { Integer.valueOf(mode), null });
		}
		catch (Throwable e)
		{
			sLevelUnder3 = true;
		}
	}

	public static boolean isIceCreamSandwichOrHigherSdk()
	{
		return Build.VERSION.SDK_INT >= 14;
	}

	/**
	 * 获取Android中的Linux内核版本号
	 * 
	 */
	public static String getLinuxKernel()
	{
		Process process = null;
		try
		{
			process = Runtime.getRuntime().exec("cat /proc/version");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (null == process)
		{
			return null;
		}

		// get the output line
		InputStream outs = process.getInputStream();
		InputStreamReader isrout = new InputStreamReader(outs);
		BufferedReader brout = new BufferedReader(isrout, 8 * 1024);
		String result = "";
		String line;

		// get the whole standard output string
		try
		{
			while ((line = brout.readLine()) != null)
			{
				result += line;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (result.equals(""))
		{
			String Keyword = "version ";
			int index = result.indexOf(Keyword);
			line = result.substring(index + Keyword.length());
			if (null != line)
			{
				index = line.indexOf(" ");
				return line.substring(0, index);
			}
		}
		return null;
	}

	/**
	 * 获得手机内存的可用空间大小
	 * 
	 * @author kingyang
	 */
	public static long getAvailableInternalMemorySize()
	{
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 获得手机内存的总空间大小
	 * 
	 * @author kingyang
	 */
	public static long getTotalInternalMemorySize()
	{
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	/**
	 * 获得手机sdcard的可用空间大小
	 * 
	 * @author kingyang
	 */
	public static long getAvailableExternalMemorySize()
	{
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 获得手机sdcard的总空间大小
	 * 
	 * @author kingyang
	 */
	public static long getTotalExternalMemorySize()
	{
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	/**
	 * 是否存在SDCard
	 * 
	 * @author chenguanyu
	 * @return
	 */
	public static boolean isSDCardExist()
	{
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 获取当前的语言
	 * 
	 * @author zhoujun
	 * @param context
	 * @return
	 */
	public static String getLanguage(Context context)
	{
		String language = context.getResources().getConfiguration().locale.getLanguage();
		return language;
	}

	/**
	 * 判断应用软件是否运行在前台
	 * 
	 * @param context
	 * @param packageName
	 *            应用软件的包名
	 * @return
	 */
	public static boolean isTopActivity(Context context, String packageName)
	{
		return isTopActivity(context, packageName, null);
	}

	/**
	 * 判断某一Activity是否运行在前台
	 * 
	 * @param context
	 * @param packageName
	 *            应用软件的包名
	 * @return
	 */
	public static boolean isTopActivity(Context context, String packageName, String className)
	{
		try
		{
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
			if (tasksInfo.size() > 0)
			{
				// Activity位于堆栈的顶层,如果Activity的类为空则判断的是当前应用是否在前台
				if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())
						&& (className == null || className.equals(tasksInfo.get(0).topActivity
								.getClassName())))
				{
					return true;
				}
			}
		}
		catch (Exception e)
		{
		}
		return false;
	}


	/**
	 * 获取国家
	 * @param context
	 * @return
	 */
	public static String getCountry(Context context)
	{
		String ret = null;

		try
		{
			TelephonyManager telManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (telManager != null)
			{
				ret = telManager.getSimCountryIso().toLowerCase();
			}
		}
		catch (Throwable e)
		{
			//			 e.printStackTrace();
		}
		if (ret == null || ret.equals(""))
		{
			ret = Locale.getDefault().getCountry().toLowerCase();
		}
		return ret;
	}

	/**
	 * 判断是否为韩国用户
	 * @return
	 */
	public static boolean isKorea(Context context)
	{
		boolean isKorea = false;

		String country = getCountry(context);
		if (country.equals("kr"))
		{
			isKorea = true;
		}

		return isKorea;
	}

	/**
	 * <br>功能简述: 判断当前用户是否是特定国家用户
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param context
	 * @param countryCodes 待检测的国家代号集合
	 * @return true 指定国家中包含用户当前所在国家 
	 * 		   false 指定国家中不包含用户当前所在国家
	 */
	public static boolean checkUserCountry(Context context, String... countryCodes)
	{
		boolean included = false;
		String localCountryCode = getCountry(context);
		for (String countryCode : countryCodes)
		{
			if (countryCode.equals(localCountryCode))
			{
				included = true;
				break;
			}
		}
		return included;
	}

	/**
	 * 是否支持OpenGL2.0
	 * @param context
	 * @return
	 */
	public static boolean isSupportGLES20(Context context)
	{
		if (!sDetectedDevice)
		{
			ActivityManager am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			ConfigurationInfo info = am.getDeviceConfigurationInfo();
			sSupportGLES20 = info.reqGlEsVersion >= 0x20000;
			sDetectedDevice = true;
		}
		return sSupportGLES20;
	}

	/**
	 * <br>功能简述:是否使用getjar付功能付费的国家
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param context
	 * @return
	 */
	public static boolean isPurchaseByGetjarContury(Context context)
	{
		boolean bRet = true;
		String country = getCountry(context);

		//圣诞限免活动 -----begin
		//		if (isLimitFreeDate() && isValidLimitFreeInstall(context)) {
		//			return true;
		//		}
		//圣诞限免活动结束
		for (int i = 0; i < PAY_NOT_BY_GETJER_COUNTRY.length; i++)
		{
			if (PAY_NOT_BY_GETJER_COUNTRY[i].equals(country))
			{
				bRet = false;
				break;
			}
		}
		return bRet;
	}

	/**
	 * <br>功能简述:圣诞限免 23-24
	 * <br>功能详细描述:
	 * <br>注意:
	 * @return
	 */
	public static boolean isLimitFreeDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String today = sdf.format(date);
		if (today.compareTo("2013-12-23") >= 0 && today.compareTo("2013-12-25") < 0)
		{
			return true;
		}
		return false;
	}

	public static boolean canHideNavBar()
	{
		if (isSimilarModel(KITKAT_WITHOUT_NAVBAR))
		{
			return false;
		}
		return true;
	}

	public static boolean isSupportBindWidget(Context context)
	{
		if (!sDetectedBindWidget)
		{
			sSupportBindWidget = false;
			if (Build.VERSION.SDK_INT >= 16)
			{
				try
				{
					// 在某些设备上，没有支持"android.appwidget.action.APPWIDGET_BIND"的activity
					Intent intent = new Intent("android.appwidget.action.APPWIDGET_BIND");
					PackageManager packageManager = context.getPackageManager();
					List<ResolveInfo> list = packageManager.queryIntentActivities(intent, 0);
					if (list == null || list.size() <= 0)
					{
						sSupportBindWidget = false;
					}
					else
					{
						// 假如有支持上述action的activity，还需要判断是否已经进行了授权创建widget
						AppWidgetManager.class.getMethod("bindAppWidgetIdIfAllowed", int.class,
								ComponentName.class);
						sSupportBindWidget = true;
					}
				}
				catch (NoSuchMethodException e)
				{ // 虽然是4.1以上系统，但是不支持绑定权限，仍按列表方式添加系统widget
					e.printStackTrace();
				}
			}
			sDetectedBindWidget = true;
		}
		return sSupportBindWidget;
	}

	/**
	 * <br>功能简述:判断api是否大于等于9
	 * <br>功能详细描述:
	 * <br>注意:
	 * @return
	 */
	public static boolean isSDKGreaterNine()
	{
		boolean result = false;
		if (Build.VERSION.SDK_INT >= 9)
		{
			result = true;
		}
		return result;
	}
}
