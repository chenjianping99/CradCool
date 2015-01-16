package com.jiubang.goscreenlock.theme.cjpcardcool;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.Window;

import com.jiubang.goscreenlock.theme.cjpcardcool.guide.ViewPageActivity;
/**
 * 
 * <br>类描述: 引导安装GO锁屏activity
 * <br>功能详细描述:
 * 
 * @author  hezhiyi
 * @date  [2012-9-12]
 */
public class GOScreenLockInstallDialog extends Activity
{
	/** 
	 *  普通模式
	 */
	public final static String	INTERNAL_NORMAL					= "200";
	/**
	 *  韩国TSTORE定制模式
	 */
	public final static String	INTERNAL_TSTORE					= "346";
	/**
	 *  韩国OLLEH定制模式
	 */
	public final static String	INTERNAL_OLLEH					= "518";
	/**
	 *  韩国OZ定制模式
	 */
	public final static String	INTERNAL_OZ						= "347";

	/**
	 * 安装GO锁屏的渠道
	 */
	public final static String	INTERNAL_528					= "528";
	public final static String	INTERNAL_540					= "540";
	public final static String	INTERNAL_541					= "541";
	public final static String	INTERNAL_542					= "542";
	public final static String	INTERNAL_543					= "543";
	public final static String	INTERNAL_524					= "524";

	public final static String	THEME_PACKAGENAME_PREFIX_KEY	= "THEME_PACKAGENAME_PREFIX_KEY";

	/**
	 *  定制模式变量
	 */
	public static String		sInternal						= INTERNAL_NORMAL;

	/**
	 * 
	 * <br>类描述:自定义对话框
	 * <br>功能详细描述:
	 * 
	 * @author  hezhiyi
	 * @date  [2012-9-12]
	 */
	class CustomAlertDialog extends AlertDialog
	{
		public CustomAlertDialog(Context context)
		{
			super(context);
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event)
		{
			boolean ret = super.onKeyDown(keyCode, event);
			finish();
			return ret;
		}
	}

	private CustomAlertDialog	mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.report);
		sInternal = ComponentUtils.getUid(getApplicationContext());
		
		if (ComponentUtils.isExistLocker(this))
		{
			// 直接调转到GO锁屏预览界面
			Intent intenttmpIntent = new Intent(Constants.GO_LOCKER_FOR_THEME_FILTER);
			intenttmpIntent.putExtra(THEME_PACKAGENAME_PREFIX_KEY, getPackageName());
			try
			{
				startActivity(intenttmpIntent);
			}
			catch (Exception e)
			{
				try
				{
					PackageManager pm = getApplicationContext().getPackageManager();
					Intent intent = pm.getLaunchIntentForPackage("com.jiubang.goscreenlock");
					intent.putExtra(THEME_PACKAGENAME_PREFIX_KEY, getPackageName());
					safeStartActivity(getApplicationContext(), intent);
				}
				catch (Exception e1)
				{
					e.printStackTrace();
				}
			}
			finish();
			return ;
		}
		else
		{
			Intent intent = new Intent();
			intent.setClass(GOScreenLockInstallDialog.this, ViewPageActivity.class);
			startActivity(intent);
			finish();
			return;
		}
	}

	/**
	 * 下载GO锁屏处理
	 */
	public static void disPatchDownLoad(Context appContext)
	{
		String marketuriString = "market://details?id=com.jiubang.goscreenlock&referrer=utm_source%3DGOLocker_Theme%26utm_medium%3DHyperlink%26utm_campaign%3DGOLocker_Theme";
		boolean isSuccess = gotoMarket(appContext, marketuriString);
		if (!isSuccess) {
			gotoBrowser(appContext, "http://play.google.com/store/apps/details?id=com.jiubang.goscreenlock");
		}
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
//		android.os.Process.killProcess(android.os.Process.myPid());
	}

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
	 * 跳转到Android Market 
	 * @param uriString market的uri
	 * @return 成功打开返回true
	 */
	public static boolean gotoMarket(Context context, String uriString)
	{
		boolean ret = false;
		Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
		marketIntent.setPackage("com.android.vending");
		marketIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try
		{
			context.startActivity(marketIntent);
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
		return ret;
	}

	public static void safeStartActivity(final Context context, Intent intent)
	{
		try
		{
			if (context != null)
			{
				context.startActivity(intent);
			}
		}
		catch (ActivityNotFoundException e)
		{
			e.printStackTrace();

		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 判定用户是否为中国大陆用户,用运营商的方式判断
	 * @param context
	 * @return
	 */
	public static boolean isCnUser(Context context)
	{

		// 在某些特殊情况下面，GoLauncher会死掉，导致取不到context，这是直接返回False吧
		if (null == context)
		{
			return false;
		}

		// 从系统服务上获取了当前网络的MCC(移动国家号)，进而确定所处的国家和地区
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		// SIM卡状态
		boolean simCardUnable = manager.getSimState() != TelephonyManager.SIM_STATE_READY;
		String simOperator = manager.getSimOperator();

		// 如果没有SIM卡的话simOperator为null，然后获取本地信息进行判断处理
		if (simOperator == null || simOperator.equals("") || simCardUnable)
		{
			// 获取当前国家或地区，如果当前手机设置为中文-中国，则使用此方法返回CN
			String curCountry = Locale.getDefault().getCountry();
			if (curCountry != null && curCountry.contains("CN"))
			{
				//				Log.v("System.out.print", "当前地区是中国");
				return true;
			}
			else
			{
				return false;
			}
		}
		String locationCode = null;
		if (simOperator.length() >= 3)
		{
			// 获取前3位
			locationCode = simOperator.substring(0, 3);

			// 中国大陆的前5位是(46000)，上边只取3前三位
			if (locationCode.equals("460"))
			{
				//				Log.v("System.out.print", "当前地区是中国");
				//				if (simOperator.startsWith("46000") || simOperator.startsWith("46002"))
				//				{
				//					Log.v("System.out.print", "中国移动");
				//				}
				//				else if (simOperator.startsWith("46001"))
				//				{
				//					Log.v("System.out.print", "中国联通");
				//				}
				//				else if (simOperator.startsWith("46003"))
				//				{
				//					Log.v("System.out.print", "中国电信");
				//				}
				// 简体中文
				return true;
			}
		}

		return false;
	}
}