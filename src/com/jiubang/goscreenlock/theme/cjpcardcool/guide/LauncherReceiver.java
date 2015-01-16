package com.jiubang.goscreenlock.theme.cjpcardcool.guide;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jiubang.goscreenlock.theme.cjpcardcool.ComponentUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.Constants;
import com.jiubang.goscreenlock.theme.cjpcardcool.R;

/**
 * 桌面推送
 * @author zhangfanghua
 *
 */
public class LauncherReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		String packageName = context.getApplicationInfo().packageName;
		
		String action = intent.getAction();
		if (action.equals(packageName + Constants.ACTION_LAUNCHER_AD_FIRST))
		{
			
			String path = ComponentUtils.getFilePath(context);
			
			Log.d("zfh", "path : " + path);
			
			// SD卡不存在
			if (path == null)
			{
				return;
			}
			
			// 如果存在桌面，则不推送
			if (ComponentUtils.isExistLocker(context) || ComponentUtils.isExitsGOLauncher(context))
			{
				return;
			}
			
			File f = new File(path + Constants.LAUNCEHR_FILE_NAME);
			// 如果存在，说明已经有主题写过了，就直接返回
			if (f.exists()) {
				return;
			}
			// 如果不存在，发送推送栏消息，并写入文件
			ComponentUtils.sendNotification(context, R.drawable.icon, context.getResources().getString(R.string.app_name), 1);
		
			// 写入文件
			ComponentUtils.writeNotifationData(path, context);
			
			// 发送一个10天的通知栏消息
			ComponentUtils.scheduleNextCheck(context, Constants.TEN_DAYS,
					Constants.ACTION_LAUNCHER_AD_SECOND);
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		
	}

}
