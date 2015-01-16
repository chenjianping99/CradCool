package com.jiubang.goscreenlock.theme.cjpcardcool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.jiubang.goscreenlock.theme.cjpcardcool.guide.ViewPageActivity;

/**
 * 公共方法类
 * @author zhangfanghua
 *
 */
public class ComponentUtils
{
	public static void disableComponent(Context context, String pkg, String cls)
	{
		ComponentName component = new ComponentName(pkg, cls);
		context.getPackageManager().setComponentEnabledSetting(component,
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
	}

	public static void enableComponent(Context context, String pkg, String cls)
	{
		ComponentName component = new ComponentName(pkg, cls);
		context.getPackageManager().setComponentEnabledSetting(component,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
	}

	/**
	 * 从配置文件读取Uid
	 * 返回“1”则读取异常
	 * @param context
	 * @return 
	 */
	public static String getUid(Context context)
	{
		// 从资源获取流
		InputStream is = null;
		is = context.getResources().openRawResource(R.raw.uid);
		// 读取流内容
		byte[] buffer = new byte[64];
		try
		{
			int len = is.read(buffer);
			byte[] data = new byte[len];
			for (int i = 0; i < len; i++)
			{
				data[i] = buffer[i];
			}
			// 生成字符串
			String dataStr = new String(data);
			dataStr.trim();
			return dataStr;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			// IO异常
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return "200";
	}
	
	/**
	 * 发送通知栏消息
	 * @param context
	 * @param intent
	 * @param iconId
	 * @param tickerText
	 * @param notificationId
	 */
	public static void sendNotification(Context context, int iconId,
			CharSequence tickerText, int notificationId) {
		try {

			Intent intent = new Intent(context, ViewPageActivity.class);
			intent.putExtra(Constants.TYPE_DOWNLOAD_LAUNCHER, Constants.DOWNLOAD_LAUNCHER);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			
			Log.d("zfh", "sendNotification start");
			
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);

			Notification notification = new Notification(iconId, tickerText,
					System.currentTimeMillis());
			notification.setLatestEventInfo(context, context.getResources()
					.getString(R.string.app_name), context.getResources()
					.getString(R.string.notification_content), contentIntent);

			// 点击后自动消失
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			NotificationManager nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.notify(notificationId, notification);
			
			Log.d("zfh", "sendNotification end");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	  
    public static String getFilePath(Context context)
	{
		String state = Environment.getExternalStorageState();
		String path = null;
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			path = Environment.getExternalStorageDirectory().getPath() + "/GOLockerTheme/";
		}
		return path;
	}
    
	
	/**
	 * 发送消息
	 * @param time
	 * @param action
	 */
	public static void scheduleNextCheck(Context context, long time, String action) {
		try {
			AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			
			final long tiggertTime = System.currentTimeMillis() + time;
			
			Log.d("zfh", "action : " + context.getApplicationInfo().packageName + action);
			
			Intent updateIntent = new Intent(context.getApplicationInfo().packageName + action);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					context.getApplicationContext(), 1, updateIntent, 0);
			mAlarmManager.set(AlarmManager.RTC_WAKEUP, tiggertTime, pendingIntent);
			updateIntent = null;
			pendingIntent = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d("zfh", "scheduleNext end");
	}
	
	/**
	 * 写入文件
	 * @param path
	 * @param context
	 * @return
	 */
	public static boolean writeNotifationData(String path, Context context)
	{
		// 写入文件
		File fileDir = new File(path);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		File file = new File(path, Constants.LAUNCEHR_FILE_NAME);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);

			Log.d("zfh", "LauncherReceiver: "
					+ context.getApplicationInfo().packageName);

			byte[] buf = context.getApplicationInfo().packageName.getBytes();
			fos.write(buf);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	

	/**
	 * 锁屏是否存在
	 * @param context
	 * @return
	 */
	public static boolean isExistLocker(Context context)
	{
		if (isExistAppByFilter(Constants.GO_LOCKER_FOR_THEME_FILTER, context))
		{
			return true;
		}

		if (isExistAppByPkgName(Constants.GO_LOCKER_PKG_NAME, context))
		{
			return true;
		}

		if (isExistAppByPkgName(Constants.GO_LOCKER_LAUNCHER_PKG_NAME, context))
		{
			return true;
		}
		return false;
	}

	/**
	 * 根据报名搜索APP是否存在
	 * @param pkgName
	 * @return
	 */
	private static boolean isExistAppByPkgName(String pkgName, Context context)
	{
		try
		{
			context.createPackageContext(pkgName, Context.CONTEXT_IGNORE_SECURITY);
		}
		catch (NameNotFoundException e)
		{
			return false;
		}

		return true;
	}

	/**
	 * 通过IntentFilter搜索App是否存在
	 * @param filterName
	 * @return
	 */
	private static boolean isExistAppByFilter(String filterName, Context context)
	{
		PackageManager pm = context.getPackageManager();
		// Launcher
		Intent intent = new Intent(filterName);
		intent.addCategory("android.intent.category.DEFAULT");
		List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);

		int count = infos.size();
		if (count <= 0)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * GOLauncher是否存在
	 * @param context
	 * @return
	 */
	public static boolean isExitsGOLauncher(Context context)
	{
		boolean isExit = false;
		// 判断GO桌面是否存在
		Intent searchIntent = new Intent(Constants.GO_LAUNCHER_INTENT);
		searchIntent.addCategory(Constants.LAUNCHER_CATEGORY);

		isExit = isExitLauncher(context, searchIntent);
		
		// 如果存在，则返回true。
		if (isExit)
		{
			return isExit;
		}
		
		// 如果不存在，则判断是否存在3.20版本之前的GO桌面（通过包名判断）
		isExit = isExitLauncherByPackage(context, Constants.ACTION_GOLAUNCHEREX);

		return isExit;
	}
	
	/**
	 * 根据包名判断桌面是否存在
	 * @param context
	 * @param pacName
	 * @return
	 */
	private static boolean isExitLauncherByPackage(Context context, String pacName)
	{
		PackageManager pm = context.getPackageManager();
		// Launcher
		Intent intent = new Intent("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);

		int launcherSz = infos.size();
		for (int i = 0; i < launcherSz; i++)
		{
			ResolveInfo info = infos.get(i);
			if (null == info || null == info.activityInfo || null == info.activityInfo.packageName)
			{
				continue;
			}

			String packageStr = info.activityInfo.packageName;
			if (packageStr.contains(pacName))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 通过IntentFilter判断桌面是否存在
	 * @param context
	 * @param intent
	 * @return
	 */
	private static boolean isExitLauncher(Context context, Intent intent)
	{
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> launchers = pm.queryIntentActivities(intent, 0);
		if (launchers.size() > 0)
		{
			for (int i = 0; i < launchers.size(); i++)
			{
				ResolveInfo info = launchers.get(i);
				if (null == info || null == info.activityInfo || null == info.activityInfo.packageName)
				{
					continue;
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean isNeedBoardcast(Context context)
	{
		String path = ComponentUtils.getFilePath(context);
		
		Log.d("zfh", "path : " + path);
		
		// SD卡不存在
		if (path == null)
		{
			return false;
		}
		
		// 如果存在桌面，则不推送
		if (ComponentUtils.isExistLocker(context) || ComponentUtils.isExitsGOLauncher(context))
		{
			return false;
		}
		
		File f = new File(path + Constants.LAUNCEHR_FILE_NAME);
		// 如果存在，说明已经有主题写过了，就直接返回
		if (f.exists()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 获取预览名称列表
	 * @param context
	 * @return
	 */
	private static ArrayList<String> getPreviewList(Context context)
	{
		String[] previewList = null;
		
		try
		{
			// 扫描assets资源文件夹中的图片.
			previewList = context.getAssets().list(Constants.THEME_PREVIEW_FOLDER);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// 4.1，谷歌屏蔽第三方软件读取别人的asset
		if (null == previewList || previewList.length == 0)
		{
			try
			{
				int resId = getThemeResId(context, "array", "detail_preview");
				previewList = context.getResources().getStringArray(resId);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		ArrayList<String> list = new ArrayList<String>();
		if (previewList != null && previewList.length > 0)
		{
			for (int i = 0; i < previewList.length; i++) {
				if (previewList[i].contains(Constants.THEME_PREVIEW_FOLDER))
				{
					list.add(previewList[i]);
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 获取resId
	 * @param appContext
	 * @param Type
	 * @param resName
	 * @return
	 */
	private static int getThemeResId(Context appContext, String Type, String resName)
	{
		try
		{
			return appContext.getResources().getIdentifier(
					appContext.getPackageName() + ":" + Type + "/" + resName,
					null, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 获取流
	 * @param appContext
	 * @param resId
	 * @return
	 */
	private static InputStream getThemeRawInputStream(Context appContext, int resId)
	{
		try
		{
			return appContext.getResources().openRawResource(resId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据名称获取图片
	 * @param aCt
	 * @param aName
	 * @return
	 */
	private static Bitmap getPreViewImage(Context aCt, String aName)
	{
		Bitmap bmp = null;
		InputStream is = null;
		try
		{
			is = aCt.getAssets().open(Constants.THEME_PREVIEW_FOLDER + "/" + aName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (null == is)
		{
			try
			{
				is = aCt.getAssets().open(aName);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		// 修复4.1付费系统禁止获取第三方app asset目录
		if (null == is)
		{
			int resId = getThemeResId(aCt, "raw", aName);
			is = getThemeRawInputStream(aCt, resId);
		}
		try
		{
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			bmp = BitmapFactory.decodeStream(is, null, opts);
			opts.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeStream(is, null, opts);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return bmp;
	}
	
	/**
	 * 获取预览图，供外部使用
	 * @param context
	 * @return
	 */
	public static ArrayList<Bitmap> getPreviewBitmaps(Context context)
	{
		ArrayList<Bitmap> previewLists = new ArrayList<Bitmap>();
		ArrayList<String> previewNames = getPreviewList(context);
		Bitmap bm = null;
		
		if (previewNames != null && previewNames.size() > 0)
		{
			for (int i = 0; i < previewNames.size(); i++) {
				bm = getPreViewImage(context, previewNames.get(i));
				if (bm != null)
				{
					previewLists.add(bm);
				}
			}
		}
		return previewLists;
	}
}
