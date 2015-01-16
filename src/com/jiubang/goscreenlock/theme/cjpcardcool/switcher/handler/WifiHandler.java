package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;

/**
 * 
 * <br>
 * 类描述:wifi设置 <br>
 * 注意:由原有的widget代码整理而来
 * 
 * @author guoyiqing
 * @date [2013-1-7]
 */
class WifiHandler implements ISwitcherable {

	private static final String TAG = "WifiHandler";
	private static final String SYSTEM_SETTING_PKG = "com.android.settings";
	private static final String SYSTEM_SETTING_CLASS = "com.android.settings.wifi.WifiSettings";
	private Context mContext;
	private WifiManager mWiFiManager;
	private WiFiStateReceiver mReciver;

	public WifiHandler(Context context) {
		mContext = context;
		mWiFiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		mReciver = new WiFiStateReceiver();
		context.registerReceiver(mReciver, filter);
	}

	@Override
	public void switchState() {
		if (mWiFiManager == null || mContext == null) {
			return;
		}
		Intent i = new Intent(BroadcastBean.CLICKABLE_WIFI);
		mContext.sendBroadcast(i);
		if (mWiFiManager.isWifiEnabled()) {
			try {
				mWiFiManager.setWifiEnabled(false);
			} catch (SecurityException e) {
				// LogUtils.log(TAG, e);
				//Intent toastIntent = new Intent(
				//		GoWidgetConstant.ACTION_GO_WIDGET_TOAST);
				// toastIntent.putExtra(GoWidgetConstant.EXTRA_TOAST_STRING,
				// mContext.getResources()
				// .getString(R.string.wifi_internal_error));
				//mContext.sendBroadcast(toastIntent);
				try {
					Intent intent = new Intent(BroadcastBean.FAILED_WIFI);
					mContext.sendBroadcast(intent);
					// Intent intent = new Intent(Intent.ACTION_MAIN);
					// intent.setClassName(SYSTEM_SETTING_PKG,
					// SYSTEM_SETTING_CLASS);
					// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// mContext.startActivity(intent);
				} catch (ActivityNotFoundException ex) {
					// LogUtils.log(TAG, ex);
				}
			}
		} else {
			mWiFiManager.setWifiEnabled(true);
		}
	}

	@Override
	public void cleanUp() {
		if (mReciver != null) {
			mContext.unregisterReceiver(mReciver);
			mReciver = null;
		}
	}

	@Override
	public void broadCastState() {
		if (mWiFiManager == null || mContext == null) {
			return;
		}
		Intent intents = new Intent(BroadcastBean.WIFI_CHANGE);
		int wifiState = mWiFiManager.getWifiState();
		if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
			intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_ON);
			mContext.sendBroadcast(intents);
		} else if (wifiState == WifiManager.WIFI_STATE_DISABLED
				|| wifiState == WifiManager.WIFI_STATE_UNKNOWN) {
			intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_OFF);
			mContext.sendBroadcast(intents);
		}
	}

	/**
	 * 
	 * <br>
	 * 类描述:监听wifi状态
	 * 
	 * @author guoyiqing
	 * @date [2013-1-7]
	 */
	class WiFiStateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction() == WifiManager.WIFI_STATE_CHANGED_ACTION
					|| intent.getAction().equals(
							WifiManager.WIFI_STATE_CHANGED_ACTION)) {
				broadCastState();
			}
		}

	}

	@Override
	public int getSwitchType() {
		return SWITCH_TYPE_WIFI;
	}

}
