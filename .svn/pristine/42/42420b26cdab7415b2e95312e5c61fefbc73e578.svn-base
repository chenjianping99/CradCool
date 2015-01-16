package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
/**
 * <br>类描述:Gps开关器
 * <br>注意:从SwitchWidget抽离调整可以使用的代码
 * 
 * @author guoyiqing
 * @date  [2013-1-5]
 */
class GpsHandler implements ISwitcherable {
	private ContentResolver mContentResolver;
	private Context mContext;
	private GPSReceiver mGpsReceiver;
	private GPSObserver mGpsObserver;
	private Handler mHandler;
	private int mHandleGPSType;

	private static final int UNKNOWNED = 0;
	private static final int SETTING_ACTIVITY = 1;
	private static final int TOAST = 2;
	private static final int BROADCAST = 3;

	public GpsHandler(Context context) {
		mContentResolver = context.getContentResolver();
		mContext = context;
		IntentFilter filter = new IntentFilter();

		// LocationManager.PROVIDERS_CHANGED_ACTION SDK=9;
		filter.addAction("android.location.PROVIDERS_CHANGED");
		mGpsReceiver = new GPSReceiver();
		context.registerReceiver(mGpsReceiver, filter);

		Uri uri = Settings.System.getUriFor(Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		Handler handler = new Handler();
		mGpsObserver = new GPSObserver(handler);
		mContentResolver.registerContentObserver(uri, true, mGpsObserver);
		initHandler();
	}

	private void initHandler() {
		mHandler = new Handler() {

			public void handleMessage(android.os.Message msg) {
				broadCastState();
				try {
					if (mHandleGPSType == UNKNOWNED) {
						startLoacationActivity();
						mHandleGPSType = SETTING_ACTIVITY;
					} else if (mHandleGPSType == SETTING_ACTIVITY) {
						startLoacationActivity();
					} else if (mHandleGPSType == TOAST) {
						sendToast();
					}
				} catch (Exception e) {
					e.printStackTrace();
					sendToast();
					mHandleGPSType = TOAST;
				}
			};
		};
	}

	/**
	 * 拉起一个设置的Activity
	 */
	private void startLoacationActivity() {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//mContext.startActivity(intent);
		Global.sendUnlockWithIntent(mContext, null, null, null, intent);
	}

	/**
	 * 发出Toast提示
	 */
	private void sendToast() {
		Intent intent = new Intent(mContext, ToastActivity.class);
//		intent.putExtra("stringId", R.string.gps_func_tips);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}

	/**
	 * 检查GPS开关
	 */
	private boolean isOn() {
		boolean isOn = false;
		String str = Settings.Secure.getString(mContentResolver,
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (str != null) {
			isOn = str.contains("gps");
		}
		return isOn;
	}

	/**
	 * 
	 * <br>类描述:Gps开关监听
	 * 
	 * @date  [2013-1-5]
	 */
	class GPSReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			mHandler.removeMessages(0);
			broadCastState();
		}
	}

	/**
	 * 
	 * <br>类描述:Gps开关监听
	 * 
	 * @date  [2013-1-5]
	 */
	class GPSObserver extends ContentObserver {

		public GPSObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			mHandler.removeMessages(0);
			if (mHandleGPSType == UNKNOWNED) {
				mHandleGPSType = BROADCAST;
			}
			broadCastState();
		}
	}

	@Override
	public void switchState() {
		if (mHandleGPSType == UNKNOWNED || mHandleGPSType == BROADCAST) {
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("custom:3"));
			// poke.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.sendBroadcast(poke);
			mHandler.removeMessages(0);
			mHandler.sendEmptyMessageDelayed(0, 1000);
		} else if (mHandleGPSType == SETTING_ACTIVITY) {
			startLoacationActivity();
			broadCastState();
		} else if (mHandleGPSType == TOAST) {
			sendToast();
			broadCastState();
		}
	}

	@Override
	public void broadCastState() {
		Intent intents = new Intent(BroadcastBean.GPS_CHANGE);
		if (isOn()) {
			//			intents.setFlags(SwitchConstants.STATUS_ON);
			intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_ON);
		} else {
			//			intents.setFlags(SwitchConstants.STATUS_OFF);
			intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_OFF);
		}
		mContext.sendBroadcast(intents);
	}

	@Override
	public void cleanUp() {
		if (mGpsReceiver != null) {
			mContext.unregisterReceiver(mGpsReceiver);
			mGpsReceiver = null;
		}
		if (mGpsObserver != null) {
			mContentResolver.unregisterContentObserver(mGpsObserver);
			mGpsObserver = null;
		}
		
	}

	@Override
	public int getSwitchType() {
		return SWITCH_TYPE_GPS;
	}

}
