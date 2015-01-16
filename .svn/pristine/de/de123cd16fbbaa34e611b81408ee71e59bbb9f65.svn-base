package com.jiubang.goscreenlock.theme.cjpcardcool.switcher;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;

/**
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  xuqian
 * @date  [2013-7-2]
 */
public class Util
{
	/** <br>功能简述:   根据包名得到图标，再转为byte［］用于保存
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param context
	 * @param pkgName
	 * @return
	 */
	public static byte[] getIconByteArrayFromBitmap(Context context, String pkgName,
			String className)
	{
		byte[] data = null;
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PackageManager pm = context.getPackageManager();
			BitmapDrawable drawable = (BitmapDrawable) pm.getActivityIcon(new ComponentName(
					pkgName, className));
			Bitmap icon = drawable.getBitmap();
			icon.compress(CompressFormat.PNG, 100, baos);
			data = baos.toByteArray();
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return data;
	}

	/** <br>功能简述:   直接提供bitmap
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param bitmap
	 * @return
	 */
	public static byte[] getIconByteArrayFromBitmap(Bitmap bitmap)
	{
		byte[] data = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, baos);
		data = baos.toByteArray();
		return data;
	}

	/** <br>功能简述: byte［］转图标
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param is
	 * @return
	 */
	public static Bitmap getIconBitmapFromByteArray(byte[] is)
	{
		if (is == null)
		{
			return null;
		}
		Bitmap bitmap = BitmapFactory.decodeByteArray(is, 0, is.length);
		return bitmap;
	}

	/** <br>功能简述:  根据包名获得应用名
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param context
	 * @param pkgName
	 * @return
	 */
	public static String getAppName(Context context, String pkgName, String className)
	{
		String appName = null;
		try
		{
			PackageManager pm = context.getPackageManager();
			ActivityInfo info = pm.getActivityInfo(new ComponentName(pkgName, className), 0);
			appName = (String) info.loadLabel(pm);
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return appName;
	}

	/**
	 * 震动
	 * 
	 * @param context
	 */
	public static void getvibrator(Context context)
	{
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 30, 30 }; // 停止 开启 停止 开启
		vibrator.vibrate(pattern, -1); // 重复两次上面的pattern 如果只想震动一次，index设为-1
	}
	
	/**
	 * 浏览器直接访问uri
	 * @param uriString
	 * @return 成功打开返回true
	 */
	public static boolean gotoBrowser(Context context, String uriString)
	{
		boolean ret = false;
		Uri browserUri = Uri.parse(uriString);
		if (null != browserUri)
		{
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
			browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try
			{
				context.startActivity(browserIntent);
				ret = true;
			}
			catch (ActivityNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * 调用通话
	 * 
	 * @param context
	 */
	public static final void startDial(final Context context)
	{
		Intent intent;
		PackageManager pManager = context.getPackageManager();

		// 方式1问题：有些机型找不到拨号程序
		intent = new Intent(Intent.ACTION_DIAL);

		List<ResolveInfo> infos = pManager.queryIntentActivities(intent,
				PackageManager.GET_RESOLVED_FILTER | PackageManager.GET_INTENT_FILTERS
						| PackageManager.MATCH_DEFAULT_ONLY);

		if (null == infos || infos.size() == 0)
		{
			// 方式2问题：这种方式在HD 2.3.3系统上这样起拨号，进入界面无历史纪录显示
			intent.setAction(Intent.ACTION_VIEW);
			Uri uri = Uri.parse("tel:");
			intent.setData(uri);
		}

		if (null != infos)
		{
			infos.clear();
			infos = null;
		}

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try
		{
			context.startActivity(intent);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 调用短信
	 * 
	 * @param context
	 * @param action
	 */
	public static final void startSMS(final Context context)
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setType("vnd.android.cursor.dir/mms");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try
		{
			context.startActivity(intent);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			intent.setType("vnd.android-dir/mms-sms");
			try
			{
				context.startActivity(intent);
			}
			catch (Exception e1)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 启动程序
	 * 
	 * @param context
	 * @param action
	 */
	public static final void startActivity(final Context context, String action)
	{
		Intent intent = new Intent();
		intent.setAction(action);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_SINGLE_TOP);
		try
		{
			context.startActivity(intent);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 向主程序发送解锁消息
	 */
	public static void sendUnlockWithIntent(Context context, String action,
			String pkgName, String className, Object toIntent) {
		Intent intent = new Intent("com.jiubang.goscreenlock.unlock");
		intent.putExtra("theme", context.getPackageName());
		
		if (toIntent != null && toIntent instanceof Intent) {
			intent.putExtra("type", 3);
			Bundle b = new Bundle();
			b.putParcelable("shareIntent", (Intent) toIntent);
			intent.putExtra("shareIntent", b);
		} else if (toIntent != null && toIntent instanceof PendingIntent) {
			intent.putExtra("type", 4);
			Bundle b = new Bundle();
			b.putParcelable("shareIntent", (PendingIntent) toIntent);
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
	 * 查询应用是否安装
	 * @param context
	 * @param intent
	 * @return
	 */
	public static boolean isAppExist(final Context context, final Intent intent) {
		List<ResolveInfo> infos = null;
		try {
			infos = context.getPackageManager().queryIntentActivities(intent, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (infos != null) && (infos.size() > 0);
	}
}
