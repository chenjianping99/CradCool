package com.jiubang.goscreenlock.theme.cjpcardcool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 
 * @author zhangfanghua
 * @date [2013-01-23]
 */
public class ComponentControlReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();
		if (Constants.ACTION_HIDE_THEME_ICON.equals(action))
		{
			String pkgName = intent.getStringExtra(Constants.PKGNAME_STRING);

			if (Constants.PACKAGE_THEME.equals(pkgName))
			{
				ComponentUtils.disableComponent(context, context.getPackageName(),
						GOScreenLockInstallDialog.class.getName());
			}
		}
		else if (Intent.ACTION_PACKAGE_REMOVED.equals(action))
		{
			if (!isGoLocker(context.getApplicationContext(), intent))
			{
				ComponentUtils.enableComponent(context, context.getPackageName(),
						GOScreenLockInstallDialog.class.getName());
			}
		}
		else if (Intent.ACTION_PACKAGE_REPLACED.equals(action))
		{
			if (isGoLocker(context.getApplicationContext(), intent))
			{
				ComponentUtils.disableComponent(context, context.getPackageName(),
						GOScreenLockInstallDialog.class.getName());
			}
		}
		else if (Intent.ACTION_PACKAGE_ADDED.equals(action))
		{
			if (isGoLocker(context.getApplicationContext(), intent))
			{
				ComponentUtils.disableComponent(context, context.getPackageName(),
						GOScreenLockInstallDialog.class.getName());
			}
		}

		android.os.Process.killProcess(android.os.Process.myPid());
	}

	private boolean isGoLocker(Context appContext, Intent intent)
	{
		String dataString = intent.getDataString();
		if (dataString != null && dataString.length() > 8)
		{
			String packageName = dataString.substring(8);
			return ComponentUtils.isExistLocker(appContext);
		}
		else
		{
			return false;
		}
	}
}
