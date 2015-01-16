package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;

/**
 * 
 * <br>类描述:wifi共享热点
 * 
 * @author  guoyiqing
 * @date  [2013-1-7]
 */
class WifiAPHandler implements ISwitcherable {

	private static final String SYSTEM_SETTING_PKG = "com.android.settings";
	private static final String SYSTEM_SETTING_CLASS = "com.android.settings.wifi.WifiSettings";
	private static final int WIFI_AP_STATE_DISABLED = 1;
	private static final int WIFI_AP_STATE_ENABLED = 3;
	/**
	 * 系统解释：手机在启动或者关闭过程中出错。 这里我们理解为手机没有WIFI热点功能。
	 */
	private final static int WIFI_AP_STATE_FAILED = 4;
	/**
	 * 摩托罗拉XOOM返回的值，暂时没法解释
	 */
	private final static int WIFI_AP_UNKNOWN = 11;
	private static final String TAG = "WifiAPHandler";
	public static final int WIFI_AP_STATE_DISABLED_17 = 11;
	public static final int WIFI_AP_STATE_ENABLED_17 = 13;
	public static final int WIFI_AP_STATE_FAILED_17 = 14;
	private Context mContext;
	private WifiManager mWiFiManager;
	private WiFiAPStateReceiver mReciver;
	private Method mIsWifiApEnabledMethod;
	private Method mSetWifiApEnabledMethod;
	private Method mGetWifiApConfigurationMethod;
	private Method mGetWifiApStateMethod;

	public WifiAPHandler(Context context) {
		mContext = context;
		mWiFiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED"); // 热点监控
		mReciver = new WiFiAPStateReceiver();
		context.registerReceiver(mReciver, filter);
		if (mWiFiManager != null) {
			Method methods[] = mWiFiManager.getClass().getMethods();
			for (Method method : methods) {
				if (method.getName().equals("isWifiApEnabled")) {
					mIsWifiApEnabledMethod = method;
				} else if (method.getName().equals("setWifiApEnabled")) {
					mSetWifiApEnabledMethod = method;
				} else if (method.getName().equals("getWifiApConfiguration")) {
					mGetWifiApConfigurationMethod = method;
				} else if (method.getName().equals("getWifiApState")) {
					mGetWifiApStateMethod = method;
				}
			}
		}
	}

	/**
	 * 获取当前wifi热点设置
	 * 
	 * @return
	 */
	private WifiConfiguration getWifiConfiguration() {
		WifiConfiguration configuration = null;
		try {
			if (mGetWifiApConfigurationMethod != null) {
				configuration = (WifiConfiguration) mGetWifiApConfigurationMethod.invoke(mWiFiManager);
			}
		} catch (IllegalArgumentException e) {
			// LogUtils.log(TAG, e);
		} catch (IllegalAccessException e) {
			// LogUtils.log(TAG, e);
		} catch (InvocationTargetException e) {
			// LogUtils.log(TAG, e);
		}
		return configuration;
	}

	private boolean isWifiApEnabled() {
		try {
			if (mIsWifiApEnabledMethod != null) {
				return (Boolean) mIsWifiApEnabledMethod.invoke(mWiFiManager);
			}
		} catch (IllegalArgumentException e) {
			// LogUtils.log(TAG, e);
		} catch (IllegalAccessException e) {
			// LogUtils.log(TAG, e);
		} catch (InvocationTargetException e) {
			// LogUtils.log(TAG, e);
		}
		return false;
	}

	private void setWifiState() {
		if (mWiFiManager == null || mContext == null) {
			return;
		}
		if (mWiFiManager.isWifiEnabled()) {
			try {
				mWiFiManager.setWifiEnabled(false);
			} catch (SecurityException e) {
				// LogUtils.log(TAG, e);
				//Intent toastIntent = new Intent(GoWidgetConstant.ACTION_GO_WIDGET_TOAST);
//				toastIntent.putExtra(GoWidgetConstant.EXTRA_TOAST_STRING, mContext.getResources()
//						.getString(R.string.wifi_internal_error));
				//mContext.sendBroadcast(toastIntent);
				try {
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.setClassName(SYSTEM_SETTING_PKG, SYSTEM_SETTING_CLASS);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				} catch (ActivityNotFoundException ex) {
					// LogUtils.log(TAG, ex);
				}
			}
		} else {
			mWiFiManager.setWifiEnabled(true);
		}
	}

	@Override
	public void switchState() {
		if (mWiFiManager == null || mContext == null) {
			return;
		}
		if (Build.VERSION.SDK_INT < 8) {
			broadCastState();
			//Intent toastIntent = new Intent(GoWidgetConstant.ACTION_GO_WIDGET_TOAST);
//			toastIntent.putExtra(GoWidgetConstant.EXTRA_TOAST_STRING, mContext.getResources()
//					.getString(R.string.wifi_ap_func_tips));
			//mContext.sendBroadcast(toastIntent);
			return;
		}
		WifiConfiguration configuration = getWifiConfiguration();
		if (configuration == null)// 弹toast说明未设置
		{
			// Log.i("ouTest", "configuration:  " + configuration);
		}
		Object[] para = { configuration, false };
		if (isWifiApEnabled()) {
			try {
				if (mSetWifiApEnabledMethod != null) {
					mSetWifiApEnabledMethod.invoke(mWiFiManager, para);
					setWifiState();
				}
			} catch (IllegalArgumentException e) {
				// LogUtils.log(TAG, e);
			} catch (IllegalAccessException e) {
				// LogUtils.log(TAG, e);
			} catch (InvocationTargetException e) {
				// LogUtils.log(TAG, e);
			}
		} else {
			int wifiApState = getWifiApState();
			if (Build.VERSION.SDK_INT <= 16) {
				if (wifiApState != WIFI_AP_STATE_FAILED && wifiApState != WIFI_AP_UNKNOWN) {
					if (mWiFiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
						setWifiState();
					}
					para[1] = true;
					try {
						if (mSetWifiApEnabledMethod != null) {
							mSetWifiApEnabledMethod.invoke(mWiFiManager, para);
						}
					} catch (IllegalArgumentException e) {
						// LogUtils.log(TAG, e);
					} catch (IllegalAccessException e) {
						// LogUtils.log(TAG, e);
					} catch (InvocationTargetException e) {
						// LogUtils.log(TAG, e);
					}
				} else {
					Intent intent = new Intent(mContext, ToastActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					intent.putExtra("stringId", R.string.no_wifi_ap_func_tips);
					mContext.startActivity(intent);
				}
			} else {
				if (wifiApState != WIFI_AP_STATE_FAILED_17) {
					if (mWiFiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
						setWifiState();
					}
					para[1] = true;
					try {
						if (mSetWifiApEnabledMethod != null) {
							mSetWifiApEnabledMethod.invoke(mWiFiManager, para);
						}
					} catch (IllegalArgumentException e) {
						// LogUtils.log(TAG, e);
					} catch (IllegalAccessException e) {
						// LogUtils.log(TAG, e);
					} catch (InvocationTargetException e) {
						// LogUtils.log(TAG, e);
					}
				} else {
					Intent intent = new Intent(mContext, ToastActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					intent.putExtra("stringId", R.string.no_wifi_ap_func_tips);
					mContext.startActivity(intent);
				}
			}

		}
	}

	private int getWifiApState() {
		try {
			if (mGetWifiApStateMethod != null) {
				return (Integer) mGetWifiApStateMethod.invoke(mWiFiManager);
			}
		} catch (IllegalArgumentException e) {
			// LogUtils.log(TAG, e);
		} catch (IllegalAccessException e) {
			// LogUtils.log(TAG, e);
		} catch (InvocationTargetException e) {
			// LogUtils.log(TAG, e);
		}
		return 4;
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
		if (mContext == null) {
			return;
		}
		if (Build.VERSION.SDK_INT < 8) {
			Intent intents = new Intent(BroadcastBean.WIFI_AP_CHANGE);
			intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.WIFI_AP_FALSE);
			mContext.sendBroadcast(intents);
		} else {
			Intent intents = new Intent(BroadcastBean.WIFI_AP_CHANGE);
			int wifiApState = getWifiApState();
			if (wifiApState == WIFI_AP_STATE_ENABLED) {
				intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_ON);
				mContext.sendBroadcast(intents);

			} else if (wifiApState == WIFI_AP_STATE_DISABLED) {
				intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_OFF);
				mContext.sendBroadcast(intents);
			} else if (wifiApState == WIFI_AP_STATE_FAILED || wifiApState == WIFI_AP_UNKNOWN) {
				intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.WIFI_AP_FALSE);
				mContext.sendBroadcast(intents);
			} else {
				intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.WIFI_AP_FALSE);
				mContext.sendBroadcast(intents);
			}
		}
	}

	public void sendWifiAPBroadcastForApi17() {
		Intent intents = new Intent(BroadcastBean.WIFI_AP_CHANGE);
		int wifiApState = getWifiApState();
		if (wifiApState == WIFI_AP_STATE_ENABLED_17) {
			intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_ON);
			mContext.sendBroadcast(intents);

		} else if (wifiApState == WIFI_AP_STATE_DISABLED_17) {
			intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_OFF);
			mContext.sendBroadcast(intents);
		} else if (wifiApState == WIFI_AP_STATE_FAILED_17) {
			intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.WIFI_AP_FALSE);
			mContext.sendBroadcast(intents);
		} else {
			intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.WIFI_AP_FALSE);
			mContext.sendBroadcast(intents);
		}
	}
	
	/**
	 * 
	 * <br>类描述:监听wifi 热点状态变化
	 * 
	 * @author  guoyiqing
	 * @date  [2013-1-7]
	 */
	class WiFiAPStateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction() == "android.net.wifi.WIFI_AP_STATE_CHANGED"
					|| intent.getAction().equals("android.net.wifi.WIFI_AP_STATE_CHANGED")) {
				Log.i("go switch", "WIFI_AP_STATE_CHANGED");
				if (Build.VERSION.SDK_INT <= 16) {
					broadCastState();
				} else {
					sendWifiAPBroadcastForApi17();
				}
			}
		}
	}

	@Override
	public int getSwitchType() {
		return SWITCH_TYPE_WIFI_AP;
	}
	
}
