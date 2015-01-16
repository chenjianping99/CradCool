package com.jiubang.goscreenlock.theme.cjpcardcool.util;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

/**
 * 
 * @author zhongwenqi
 *
 */
public class Util {
	public static boolean isIntentAvailable(Context context, Intent intent) {
		boolean available = false;
		try {
			final PackageManager packageManager = context.getPackageManager();
			List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
			available = list.size() > 0;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return available;
	}

	public static Intent getIntentByPackageName(Context context, String packageName) {
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
	/**
	 * 获取所有音乐打开软件
	 * @param context
	 * @return
	 */
	public static List<ResolveInfo> getMusicActivities(Context context) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File("/mnt/sdcard/ren.mp3"));
		intent.setDataAndType(uri, "audio/*");

		//		Intent intent = new Intent(Intent.ACTION_VIEW);
		//		intent.setType("audio/*");
		//		intent.addCategory(Intent.CATEGORY_DEFAULT);／
		return context.getPackageManager().queryIntentActivities(intent, 0);
	}
	/**
	 * 获取所有图片打开软件
	 * @param context
	 * @return
	 */
	public static List<ResolveInfo> getPhotoActivities(Context context) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
	}

	public static List<ResolveInfo> getActivities(Context context) {
		//		Intent intent = new Intent("android.intent.action.VIEW");
		//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//		intent.putExtra("oneshot", 0);
		//		intent.putExtra("configchange", 0);
		//		Uri uri = Uri.fromFile(new File("/mnt/sdcard/ren.mp3"));
		//		intent.setDataAndType(uri, "audio/*");
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		//		apps = packageManager.queryIntentActivities(mainIntent, 0);
		//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		//		intent.setType("image/*");
		return context.getPackageManager().queryIntentActivities(mainIntent, 0);

		/*Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);*/
		//		intent.addCategory(Intent.CATEGORY_DEFAULT);
		//		return context.getPackageManager().queryIntentActivities(intent, 0);
	}
}
