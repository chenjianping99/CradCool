package com.jiubang.goscreenlock.theme.cjpcardcool.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.Window;

import com.jiubang.goscreenlock.theme.cjpcardcool.CustomDateFormat;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.GetPictureActivity;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.RootView;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ViewUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.R;

/**
 * 全局变量
 * 
 * @author lailele
 */
public class Global {
	private static final int NUM_480 = 480;
	private static final int NUM_320 = 320;
	private static final int NUM_800 = 800;
	private static final int NUM_25 = 25;
	private static final int NUM_50 = 50;
	private static final float NUM_5F = 0.5f;
	public static int sScreenWidth = NUM_480;
	public static int sScreenHeight = NUM_800;
	public static float sScale = 1;
	public static int statusBarHeight = NUM_25;
	public static boolean sIsRunning = false;

	/*------------------------------------集中各个provider，统一修改名称-----------------------------------------*/
	/*
	public static final String THEME_PACKAGE_NAME = "com.jiubang.goscreenlock.theme.cjpcardcool";
	public static final String WEATHERBROADCASTFILTER = THEME_PACKAGE_NAME
			+ ".weatherdfilter";
	public static final String WEATHER_AUTHORITY = THEME_PACKAGE_NAME
			+ ".weather.util.provider";
	public static final String WEATHEREFLUSH = THEME_PACKAGE_NAME
			+ ".weatherreflush";
	public static final String WEATHER_FLUSH_FORCE = "weather_isforce_reflush";
	public static final String WEATHER_MY_PROVIDER_AUTHORITY = THEME_PACKAGE_NAME
			+ ".util.myprovider";

	public static final String SWITCH_NOTIFY_ACTION = THEME_PACKAGE_NAME
			+ ".switch";
	public static final String SWITCH_STATE = "switch_state";
	public static final int SWITCH_STATE_ALL =  0;
	public static final int SWITCH_STATE_WIFI =  1;
	public static final int SWITCH_STATE_BT =  2;
	public static final int SWITCH_STATE_AIRMODE =  3;
	public static final int SWITCH_STATE_RING =  4;
	public static final int SWITCH_STATE_GPRS =  5;*/

	/********************** 天气新模板 ********************************/
	public static final String PACKAGE = "com.android.vending";
	public static final String SURISTRING = "market://details?id=com.gau.go.launcherex.gowidget.weatherwidget&referrer=utm_source%3Dweatherinfo%26utm_medium%3DHyperlink%26utm_campaign%3DGOLocker";
	public static final String SPATHSTRING = "https://play.google.com/store/apps/details?id=com.gau.go.launcherex.gowidget.weatherwidget";
	public static final String SWEATHERPACK = "com.gau.go.launcherex.gowidget.weatherwidget";

	/********************** 天气新模板 ********************************/
	/**
	 * 获取手机的屏幕的密度
	 * 
	 * @param context
	 */
	public static void setScale(Context context) {
		sScale = context.getResources().getDisplayMetrics().density;
	}

	/**
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(float dipValue) {
		return (int) (dipValue * sScale + NUM_5F);
	}

	public static int px2dip(float pxValue) {
		return (int) (pxValue / sScale + NUM_5F);
	}

	/**
	 * 判断是否是320*480的小分辨率手机
	 * 
	 * @return
	 */
	public static boolean isSmallScreenMobile() {
		boolean result = false;
		if (sScreenWidth <= NUM_320 && sScreenHeight <= NUM_480) {
			result = true;
		}
		return result;
	}

	/**
	 * 判断目前是否处于中文语言环境下
	 * 
	 * @param context
	 * @return 返回true代表是中文语言环境，返回false则是其他。
	 */
	public static boolean isChinese(Context context) {
		return context.getResources().getConfiguration().locale
				.equals(Locale.SIMPLIFIED_CHINESE)
				|| context.getResources().getConfiguration().locale
						.equals(Locale.TRADITIONAL_CHINESE);
	}

	public static Drawable getDrawable(Context context, String strRes) {
		Drawable icon = null;
		if (strRes == null) {
			return icon;
		}
		try {
			if (strRes.lastIndexOf(".9") > 0) {
				strRes = strRes.substring(0, strRes.lastIndexOf(".9"));
				int resId = getResId(context, strRes);
				icon = context.getResources().getDrawable(resId);
			} else {
				int resId = getResId(context, strRes);
				Bitmap bmp = BitmapFactory.decodeResource(
						context.getResources(), resId);
				if (bmp != null) {
					icon = new MyBitmapDrawable(context.getResources(), bmp);
				}
			}
			return icon;
		} catch (NotFoundException e) {
			// Log.d("RobotAnimationView--->>>", "getDrawable() " + strRes +
			// " NotFoundException");
		} catch (OutOfMemoryError e) {
			// Log.d("RobotAnimationView--->>>", "getDrawable() " + strRes +
			// " OutOfMemoryError");
		} catch (Exception e) {
			// Log.d("RobotAnimationView--->>>", "getDrawable()" + strRes +
			// " has Exception");
		}
		return icon;
	}

	/**
	 * 根据图片文件名称获取相应资源id
	 * 
	 * @param context
	 * @param strRes
	 * @return
	 */
	public static int getResId(Context context, String strRes) {
		int ret = -1;
		ret = context.getResources().getIdentifier(
				context.getPackageName() + ":drawable/" + strRes, null, null);
		return ret;
	}

	public static String getDateTime(String strFormat, Context context) {
		String dateStr = CustomDateFormat
				.format(strFormat, new Date(), context).toString();
		return dateStr;
	}

	/**
	 * 启动指定的程序
	 * 
	 * @param packName
	 *            应用程序包名
	 * @param className
	 *            应用程序类名
	 * @param context
	 */
	public static void startComponent(String packName, String className,
			Context context) {
		// 打开用户自定义程序
		ComponentName c = new ComponentName(packName, className);
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(c);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取应用程序的标题
	 * 
	 * @param context
	 * @param packageName
	 * @param className
	 * @return 指定包名的应用程序的标题。若指定的包名不存在则返回字符串"None"
	 */
	public static String getAppName(Context context, String packageName,
			String className) {
		if (context == null || packageName == null || packageName.equals("")
				|| className == null || className.equals("")) {
			return "None";
		}

		PackageManager pm = context.getPackageManager();
		ComponentName cn = new ComponentName(packageName, className);
		String label = null;
		try {
			ActivityInfo info = pm.getActivityInfo(cn, 0);
			label = (String) info.loadLabel(pm);
		} catch (NameNotFoundException e) {
			label = "None";
		}
		return label;
	}

	/**
	 * 判断参数是否数字
	 * 
	 * @param str
	 * @return true说明是数字，false则不是数字
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 调用通话
	 * 
	 * @param context
	 */
	public static final void startDial(final Context context) {
		Intent intent;
		// if (MobileState.mMissCount > 0)
		// {
		// intent = new Intent(Intent.ACTION_VIEW);
		// intent.setType("vnd.android.cursor.dir/calls");
		// } else
		// {
		PackageManager pManager = context.getPackageManager();

		// 方式1问题：有些机型找不到拨号程序
		intent = new Intent(Intent.ACTION_DIAL);

		List<ResolveInfo> infos = pManager.queryIntentActivities(intent,
				PackageManager.GET_RESOLVED_FILTER
						| PackageManager.GET_INTENT_FILTERS
						| PackageManager.MATCH_DEFAULT_ONLY);

		if (null == infos || infos.size() == 0) {
			// 方式2问题：这种方式在HD 2.3.3系统上这样起拨号，进入界面无历史纪录显示
			intent.setAction(Intent.ACTION_VIEW);
			Uri uri = Uri.parse("tel:");
			intent.setData(uri);
		}

		if (null != infos) {
			infos.clear();
			infos = null;
		}
		// }

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 调用短信
	 * 
	 * @param context
	 * @param action
	 */
	public static final void startSMS(final Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setType("vnd.android.cursor.dir/mms");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			intent.setType("vnd.android-dir/mms-sms");
			try {
				context.startActivity(intent);
			} catch (Exception e1) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 分离字符串
	 * 
	 * @param text
	 * @return
	 */
	public static String[] splitText(String text, String regular) {
		if (text == null || text.equals("") || regular == null) {
			return null;
		}
		String[] words = null;
		String[] temps = null;
		ArrayList<String> list = new ArrayList<String>();

		if (text == null || text.equals("")) {
			return null;
		}
		temps = text.split(regular);
		if (temps != null) {
			for (int i = 0; i < temps.length; i++) {
				String temp = temps[i];
				if (!temp.equals("")) {
					list.add(temp);
				}
			}
		}
		words = new String[list.size()];
		list.toArray(words);
		return words;
	}

	/**
	 * 将View映射成Bitmap
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap changeToBitmap(View view) {
		if (view == null) {
			return null;
		}
		Bitmap bm = null;
		view.setDrawingCacheEnabled(true);
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		bm = view.getDrawingCache();
		return bm;
	}

	/**
	 * 获取在功能菜单出现的程序列表
	 * 
	 * @param context
	 *            上下文
	 * @return 程序列表，类型是 List<ResolveInfo>
	 */
	public static List<ResolveInfo> getLauncherApps(Context context) {
		if (context == null) {
			return null;
		}
		List<ResolveInfo> infos = null;
		PackageManager pm = context.getPackageManager();
		Intent intent = new Intent("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		try {
			infos = pm.queryIntentActivities(intent, 0);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}

	public static void getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 判断是否是电话程序
	 * 
	 * @param context
	 * @param packageName
	 * @param classname
	 * @return
	 */
	public static boolean isDialApp(Context context, String packageName,
			String className) {
		if (context == null || packageName == null || packageName.equals("")
				|| className == null || className.equals("")) {
			return false;
		}
		ComponentName c = new ComponentName(packageName, className);
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(c);

		Uri uri = intent.getData();
		if (null != uri) {
			if (uri.toString().contains("tel:")) {
				return true;
			}
		}

		String dialArray[] = null;
		try {
			// 捕获包括context=null或取不到相应资源的情况
			dialArray = context.getResources().getStringArray(
					R.array.notifiction_dialers_array);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return contains(intent, dialArray);
	}

	/**
	 * 判断是否是短信程序
	 * 
	 * @param context
	 * @param packageName
	 * @param classname
	 * @return
	 */
	public static boolean isMessageApp(Context context, String packageName,
			String className) {
		if (context == null || packageName == null || packageName.equals("")
				|| className == null || className.equals("")) {
			return false;
		}
		ComponentName c = new ComponentName(packageName, className);
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(c);
		String type = intent.getType();
		if (null != type) {
			if (type.contains("vnd.android-dir/mms-sms")) {
				// 这是GO桌面初始化时的type识别
				return true;
			}
		}

		String mDefaultMessageArray[] = null;
		String mFullMessageArray[] = null;

		try {
			// 捕获包括context=null或取不到相应资源的情况
			mDefaultMessageArray = context.getResources().getStringArray(
					R.array.notification_default_sms_array);
			mFullMessageArray = context.getResources().getStringArray(
					R.array.notification_full_sms_array);

		} catch (Throwable e) {
			e.printStackTrace();
		}

		return equals(intent, mFullMessageArray)
				|| contains(intent, mDefaultMessageArray);
	}

	private static boolean contains(Intent intent, String[] contentArray) {
		boolean bRet = false;

		if (contentArray == null || intent == null) {
			return false;
		}

		String intentString = null;
		try {
			intentString = intent.toUri(0);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		int len = contentArray.length;
		for (int i = 0; i < len; i++) {
			bRet |= intentString.contains(contentArray[i]);
		}
		return bRet;
	}

	// 按包全名的比较方法
	private static boolean equals(Intent intent, String[] contentArray) {
		boolean bRet = false;
		if (contentArray == null || intent == null) {
			return false;
		}

		String intentString = null;
		try {
			/*
			 * 例子：Go短信的facebook插件intent.toUri(0)返回的结果为
			 * #Intent;action=android.intent
			 * .action.MAIN;category=android.intent.
			 * category.LAUNCHER;launchFlags
			 * =0x10200000;component=com.jb.gosms.chat
			 * /com.jb.gosms.facebook.Facebook.LoginActivity;end
			 */
			ComponentName componentName = intent.getComponent();
			if (componentName != null) {
				intentString = componentName.getPackageName();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		if (intentString != null) {
			int len = contentArray.length;
			for (int i = 0; i < len; i++) {
				bRet |= intentString.equals(contentArray[i]);
			}
		}
		return bRet;
	}

	/**
	 * 向主程序发送解锁消息
	 */
	public static void sendUnlockWithIntent(Context context, String action,
			String pkgName, String className, Intent shareIntent) {
		Intent intent = new Intent("com.jiubang.goscreenlock.unlock");
		intent.putExtra("theme", context.getPackageName());

		if (shareIntent != null) {
			intent.putExtra("type", 3);
			Bundle b = new Bundle();
			b.putParcelable("shareIntent", shareIntent);
			intent.putExtra("shareIntent", b);
		} else if (action == null && pkgName != null && className != null) {
			intent.putExtra("type", 2); // 0:表示纯解锁，1,表示调用仅有的默认解锁.2,通过包名和类名调用应用程序
		} else if (action != null) {
			intent.putExtra("type", 1); // 0:表示纯解锁，1,表示调用仅有的默认解锁.2,通过包名和类名调用应用程序
		} else {
			intent.putExtra("type", 0); // 0:表示纯解锁，1,表示调用仅有的默认解锁.2,通过包名和类名调用应用程序
		}
		intent.putExtra("action", action);
		intent.putExtra("pkgname", pkgName);
		intent.putExtra("classname", className);
		context.sendBroadcast(intent);
	}

	/**
	 * 震动
	 * 
	 * @param context
	 */
	public static void getvibrator(Context context) {
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { NUM_50, NUM_50 }; // 停止 开启 停止 开启
		vibrator.vibrate(pattern, -1); // 重复两次上面的pattern 如果只想震动一次，index设为-1
	}

	/**
	 * 防止状态栏下拉
	 */
	public static final void collapseStatusbar(final Context context) {
		Object localObject1 = context.getSystemService("statusbar");

		if (localObject1 == null) {
			return;
		}

		Method[] arrayOfMethod = localObject1.getClass().getMethods();
		int nCount = arrayOfMethod.length;
		for (int j = 0; j < nCount; j++) {
			Method localMethod = arrayOfMethod[j];
			if (localMethod.getName().compareTo("collapse") == 0) {
				try {
					localMethod.invoke(localObject1, (Object[]) null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	public static void deleteFile(String file) {
		try {
			File f = new File(file);
			if (f.exists()) {
				f.delete();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/********************** 天气新模板 ********************************/
	/**
	 * 浏览器直接访问uri
	 * 
	 * @param uriString
	 * @return 成功打开返回true
	 */
	public static boolean gotoBrowser(Context context, String uriString) {
		boolean ret = false;
		if (uriString == null) {
			return ret;
		}
		Uri browserUri = Uri.parse(uriString);
		if (null != browserUri) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
			browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				context.startActivity(browserIntent);
				ret = true;
			} catch (ActivityNotFoundException e) {
				// Log.i(LogConstants.HEART_TAG, "gotoBrowser error, uri = " +
				// uriString);
			} catch (Exception e) {
				// Log.i(LogConstants.HEART_TAG, "gotoBrowser error, uri = " +
				// uriString);
			}
		}
		return ret;
	}

	/**
	 * 跳转到Android Market
	 * 
	 * @param uriString
	 *            market的uri
	 * @return 成功打开返回true
	 */
	public static boolean gotoMarket(Context context, String uriString) {
		boolean ret = false;
		Intent marketIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(uriString));
		marketIntent.setPackage(PACKAGE);
		marketIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(marketIntent);
			ret = true;
		} catch (ActivityNotFoundException e) {
			// Log.i(LogConstants.HEART_TAG, "gotoMarketForAPK error, uri = " +
			// uriString);
		} catch (Exception e) {
			// Log.i(LogConstants.HEART_TAG, "gotoMarketForAPK error, uri = " +
			// uriString);
		}
		return ret;
	}

	/**
	 * 检查是安装某包
	 * 
	 * @param context
	 * @param packageName
	 *            包名
	 * @return
	 */
	public static boolean isAppExist(final Context context,
			final String packageName) {
		if (context == null || packageName == null) {
			return false;
		}
		boolean result = false;
		try {
			// context.createPackageContext(packageName,
			// Context.CONTEXT_IGNORE_SECURITY);
			context.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_SHARED_LIBRARY_FILES);
			result = true;
		} catch (NameNotFoundException e) {
			// TODO: handle exception
			result = false;
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/********************** 天气新模板 ********************************/
	public static final String FROM_GOLOCKER = "fromgolocker";
	public static void sendUnlockWithIntent(Context context, String action,
			String pkgName, String className) {
		Intent intent = new Intent("com.jiubang.goscreenlock.unlock");
		intent.putExtra("theme", context.getPackageName());
		if (action == null && pkgName != null && className != null) {
			intent.putExtra("type", 2); // 0:表示纯解锁，1,表示调用仅有的默认解锁.2,通过包名和类名调用应用程序
		} else if (action != null) {
			intent.putExtra("type", 1); // 0:表示纯解锁，1,表示调用仅有的默认解锁.2,通过包名和类名调用应用程序
		} else {
			intent.putExtra("type", 0); // 0:表示纯解锁，1,表示调用仅有的默认解锁.2,通过包名和类名调用应用程序
		}
		intent.putExtra("action", action);
		intent.putExtra("pkgname", pkgName);
		intent.putExtra("classname", className);
		intent.putExtra("fromgolocker", true);
		context.sendBroadcast(intent);
	}

	public static boolean isSameDay(long t1, long t2) {
		java.util.Calendar c1 = java.util.Calendar.getInstance();
		c1.setTimeInMillis(t1);
		int year1 = c1.get(java.util.Calendar.YEAR);
		int day1 = c1.get(java.util.Calendar.DAY_OF_YEAR);
		c1.setTimeInMillis(t2);
		int year2 = c1.get(java.util.Calendar.YEAR);
		int day2 = c1.get(java.util.Calendar.DAY_OF_YEAR);
		return year1 == year2 && day1 == day2;

	}

	public static void hardwareAcceleratedByWindow(Activity a) {
		// android 3.0 API Level = 11, android 4.0 API Level = 14
		if (Build.VERSION.SDK_INT < 11 /* || Build.VERSION.SDK_INT >= 14 */) {
			return;
		}
		Class[] arrayOfClass = new Class[] { Integer.TYPE, Integer.TYPE };
		Method localMethod;
		try {
			localMethod = Window.class.getMethod("setFlags", arrayOfClass);
			Object[] arrayOfObject = new Object[2];
			Integer localInteger1 = Integer.valueOf(0x01000000);
			arrayOfObject[0] = localInteger1;
			Integer localInteger2 = Integer.valueOf(0x01000000);
			arrayOfObject[1] = localInteger2;
			localMethod.invoke(a.getWindow(), arrayOfObject);
		} catch (Throwable e) {

		}
	}

	public static boolean isIntentAvailable(Context context, Intent intent) {
		boolean available = false;
		try {
			final PackageManager packageManager = context.getPackageManager();
			List<ResolveInfo> list = packageManager.queryIntentActivities(
					intent, PackageManager.MATCH_DEFAULT_ONLY);
			available = list.size() > 0;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return available;
	}

	public static Intent getIntentByPackageName(Context context,
			String packageName) {
		Intent intent = null;
		try {
			final PackageManager packageManager = context.getPackageManager();
			intent = packageManager.getLaunchIntentForPackage(packageName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return intent;
	}

	public static boolean launchActivity(Context context, Intent intent) {
		if (isIntentAvailable(context, intent)) {
			Global.sendUnlockWithIntent(context, null, null, null, intent);
			return true;
		}
		return false;
	}

	public static List<ResolveInfo> getActivities(Context context) {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		return context.getPackageManager().queryIntentActivities(intent, 0);
	}
	
	public static Bitmap getMyBgBitmap(Context context, int index) {
		Bitmap testBitmap = null;
		File f = new File("/data/data/" + Constant.THEME_PACKAGE_NAME + "/files" + GetPictureActivity.BGNAME[index]);
		if (f.isFile()) {
			testBitmap = ViewUtils.getBitmapByPath(context, f.getAbsolutePath());
		} 
		return testBitmap;
	}
	
	//
	private static RootView sRootView;
	public static RootView getRootView() {
		return sRootView;
	}

	public static void setRootView(RootView rootView) {
		sRootView = rootView;
	}

}
