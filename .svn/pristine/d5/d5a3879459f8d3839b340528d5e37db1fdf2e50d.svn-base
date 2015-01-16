package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;

/**
 * 
 * <br>
 * 类描述:普通Gprs数据开关器 <br>
 * 注意:从SwitchWidget抽离调整可以使用的代码
 * 
 * @author guoyiqing
 * @date [2013-1-5]
 */
class NormalGprsHandler implements ISwitcherable {

	private static final String MOBILE_DATA = "mobile_data";

	private static final int ON = 1;
	private static final int OFF = 0;
	private static final int DISABLE = -1;
	private static final int DISABLE_BY_SDK = -2;

	private static final String TAG = "NormalGprsHandler";

	private Context mContext;
	private ConnectivityManager mManager;
	private Method mSetMobileDataEnabled;
	private Method mGetMobileDataEnabled;
	private GPRSReceiver mReceiver;
	private Handler mHandler;
	private boolean mIsSDKOver8;
	private boolean mIsSDKOver7;
	// ////////////////用于2.2系统控制GPRS//////////////////////////
	/**
	 * 手机电话管理类
	 */
	private TelephonyManager mTelephonyManager;
	private Object mTelephony;
	private Method mEnableApnType;
	private Method mDisableApnType;
	private Method mEnableDataConnectivity;
	private Method mDisableDataConnectivity;
	public boolean mIsNoConnectivity;

	private GprsObserver mGprsObserver;

	public NormalGprsHandler(Context context) {
		mContext = context;
		mManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		mTelephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		init();
		initHandle();
		if (!mIsSDKOver8) {
			IntentFilter filter = new IntentFilter();
			filter.addAction("com.android.internal.telephony.MOBILE_DATA_CHANGED");
			filter.addAction("com.android.internal.telephony.NETWORK_MODE_CHANGED");
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			mReceiver = new GPRSReceiver();
			context.registerReceiver(mReceiver, filter);

		} else {
			mGprsObserver = new GprsObserver(mHandler);
			context.getContentResolver().registerContentObserver(
					Settings.Secure.getUriFor(MOBILE_DATA), false,
					mGprsObserver);
			broadCastState();
		}
	}

	private void initHandle() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				broadCastState();
			}
		};
	}

	/**
	 * 获取隐藏的电话控制接口
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object getTelephony() throws ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, SecurityException, NoSuchMethodException {
		Class localClass = Class
				.forName(mTelephonyManager.getClass().getName());
		Method method = localClass.getDeclaredMethod("getITelephony");
		boolean accessible = method.isAccessible();
		method.setAccessible(true);
		Object telephony = method.invoke(mTelephonyManager);
		method.setAccessible(accessible);
		return telephony;
	}

	/**
	 * 初始化，用于区分不同版本SDK，需要采取不同方式控制GPRS开关的功能
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void init() {
		if (Build.VERSION.SDK_INT > 8) {
			mIsSDKOver8 = true;
		} else if (Build.VERSION.SDK_INT > 7) {
			mIsSDKOver7 = true;
			mIsSDKOver8 = false;
		} else {
			mIsSDKOver7 = false;
			mIsSDKOver8 = false;
		}
		Class<Boolean> booleanClass = (Class<Boolean>) new boolean[0]
				.getClass().getComponentType();
		try {
			mSetMobileDataEnabled = ConnectivityManager.class.getMethod(
					"setMobileDataEnabled", booleanClass);
		} catch (SecurityException e) {
			// LogUtils.log(TAG, e);
		} catch (NoSuchMethodException e) {
			// LogUtils.log(TAG, e);
		}
		try {
			mGetMobileDataEnabled = ConnectivityManager.class
					.getMethod("getMobileDataEnabled");
		} catch (SecurityException e) {
			// LogUtils.log(TAG, e);
		} catch (NoSuchMethodException e) {
			// LogUtils.log(TAG, e);
		}
		try {
			mTelephony = getTelephony();
		} catch (Exception e) {
			// LogUtils.log(TAG, e);
		}
		Class localClass = null;
		try {
			localClass = Class
					.forName("com.android.internal.telephony.ITelephony");
		} catch (ClassNotFoundException e) {
			// LogUtils.log(TAG, e);
		}
		Class[] classs = new Class[1];
		classs[0] = String.class;
		try {
			if (localClass != null) {
				mEnableApnType = localClass.getDeclaredMethod("enableApnType",
						classs);
			}
		} catch (SecurityException e) {
			// LogUtils.log(TAG, e);
		} catch (NoSuchMethodException e) {
			// LogUtils.log(TAG, e);
		}
		try {
			if (localClass != null) {
				mDisableApnType = localClass.getDeclaredMethod(
						"disableApnType", classs);
			}
		} catch (SecurityException e) {
			// LogUtils.log(TAG, e);
		} catch (NoSuchMethodException e) {
			// LogUtils.log(TAG, e);
		}
		try {
			if (localClass != null) {
				mEnableDataConnectivity = localClass
						.getDeclaredMethod("enableDataConnectivity");
			}
		} catch (SecurityException e) {
			// LogUtils.log(TAG, e);
		} catch (NoSuchMethodException e) {
			// LogUtils.log(TAG, e);
		}
		try {
			if (localClass != null) {
				mDisableDataConnectivity = localClass
						.getDeclaredMethod("disableDataConnectivity");
			}
		} catch (SecurityException e) {
			// LogUtils.log(TAG, e);
		} catch (NoSuchMethodException e) {
			// LogUtils.log(TAG, e);
		}
	}

	/**
	 * SDK2.2或者以下的版本操作GPGS的方式
	 * 
	 * @param enable
	 *            是否打开GPRS
	 */
	private void toggleApn(boolean enable) {
		try {
			if (mTelephony != null) {
				Object[] objects = new Object[1];
				objects[0] = "default";
				if (enable) {
					mEnableApnType.invoke(mTelephony, objects);
					mEnableDataConnectivity.invoke(mTelephony);
				} else {
					mDisableApnType.invoke(mTelephony, objects);
					mDisableDataConnectivity.invoke(mTelephony);
				}
			}
		} catch (Exception e) {
			Intent intent = new Intent(BroadcastBean.FAILED_APN);
			mContext.sendBroadcast(intent);
		}
	}

	private int isOn() {
		int isOn = OFF;
		try {
			if (mIsSDKOver8) {
				if (mGetMobileDataEnabled != null) {
					boolean on = (Boolean) mGetMobileDataEnabled
							.invoke(mManager);
					if (on) {
						isOn = ON;
					} else {
						isOn = OFF;
					}
				}
			} else if (mIsSDKOver7) {
				if (mGetMobileDataEnabled != null) {
					boolean on = (Boolean) mGetMobileDataEnabled
							.invoke(mManager);
					if (on) {
						if (!mIsNoConnectivity) {
							isOn = ON;
						} else {
							isOn = OFF;
						}
					} else {
						isOn = DISABLE;
					}
				}
			} else {
				isOn = DISABLE_BY_SDK;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOn;
	}

	/**
	 * 
	 * <br>
	 * 类描述:普通Gprs 开关监听器
	 * 
	 * @date [2013-1-5]
	 */
	class GPRSReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					ConnectivityManager.CONNECTIVITY_ACTION)) {
				mIsNoConnectivity = intent.getBooleanExtra(
						ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
				NetworkInfo mNetworkInfo = (NetworkInfo) intent
						.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

				NetworkInfo mOtherNetworkInfo = (NetworkInfo) intent
						.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

				String mReason = intent
						.getStringExtra(ConnectivityManager.EXTRA_REASON);
				// Log.v("wjm", "mReason: " + mReason);
				// Log.v("wjm",
				// "mReason: " + "mNetworkInfo: "
				// + mNetworkInfo.toString());
				if (mOtherNetworkInfo != null) {
					// Log.v("wjm", "mReason: " + "mOtherNetworkInfo: "
					// + mOtherNetworkInfo.toString());

				}
			}
			mHandler.removeMessages(0);
			broadCastState();
		}
	}

	/**
	 * 
	 * <br>
	 * 类描述:普通Gprs数据改变监听器
	 * 
	 * @date [2013-1-5]
	 */
	class GprsObserver extends ContentObserver {

		public GprsObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			mHandler.removeMessages(0);
			mHandler.sendEmptyMessage(0);
		}
	}

	@Override
	public void switchState() {
		Intent i = new Intent(BroadcastBean.CLICKABLE_APN);
		mContext.sendBroadcast(i);
		int status = isOn();
		if (status == ON) {
			try {
				if (mIsSDKOver8) {
					if (mSetMobileDataEnabled != null) {
						Object[] arrayOfObject = new Object[] { Boolean.FALSE };
						boolean accessible = mSetMobileDataEnabled
								.isAccessible();
						mSetMobileDataEnabled.setAccessible(true);
						mSetMobileDataEnabled.invoke(mManager, arrayOfObject);
						mSetMobileDataEnabled.setAccessible(accessible);
						mHandler.sendEmptyMessageDelayed(0, 3000);
					}
				} else {
					// SDK2.2或者以下的版本操作GPGS的方式
					// TODO:2.2上面已经有setMobileDataEnabled接口
					toggleApn(false);
					mHandler.sendEmptyMessageDelayed(0, 3000);
				}
			} catch (Exception e) {
				Intent intent = new Intent(BroadcastBean.FAILED_APN);
				mContext.sendBroadcast(intent);
			}
		} else if (status == OFF) {
			try {
				if (mIsSDKOver8) {
					if (mSetMobileDataEnabled != null) {
						Object[] arrayOfObject = new Object[] { Boolean.TRUE };
						boolean accessible = mSetMobileDataEnabled
								.isAccessible();
						mSetMobileDataEnabled.setAccessible(true);
						mSetMobileDataEnabled.invoke(mManager, arrayOfObject);
						mSetMobileDataEnabled.setAccessible(accessible);
						mHandler.sendEmptyMessageDelayed(0, 3000);
					}
				} else {
					// SDK2.2或者以下的版本操作GPGS的方式
					toggleApn(true);
					mHandler.sendEmptyMessageDelayed(0, 3000);
				}
			} catch (Exception e) {
				Intent intent = new Intent(BroadcastBean.FAILED_APN);
				mContext.sendBroadcast(intent);
			}
		} else if (status == DISABLE) {
			Intent intent = new Intent(BroadcastBean.FAILED_APN);
			mContext.sendBroadcast(intent);
			// 发出提示，不能使用GPRS
			// Intent intent = new Intent(mContext, ToastActivity.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// // intent.putExtra("stringId", R.string.gprs_func_tips);
			// mContext.startActivity(intent);
			mHandler.sendEmptyMessage(0);
		} else if (status == DISABLE_BY_SDK) {
			Intent intent = new Intent(BroadcastBean.FAILED_APN);
			mContext.sendBroadcast(intent);
			// 发出因为SDK版本过低提示，不能使用GPRS
			// Intent intent = new Intent(mContext, ToastActivity.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// // intent.putExtra("stringId", R.string.gprs_func_sdk_tips);
			// mContext.startActivity(intent);
			mHandler.sendEmptyMessage(0);
		}
	}

	@Override
	public void broadCastState() {
		Intent intent = new Intent(BroadcastBean.GPRS_CHANGE);
		int status = isOn();
		if (status == ON) {
			intent.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_ON);
		} else if (status == OFF) {
			intent.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_OFF);
		} else if (status == DISABLE) {
			intent.putExtra(BroadcastBean.STATUS1, 2);
		} else if (status == DISABLE_BY_SDK) {
			intent.putExtra(BroadcastBean.STATUS1, 2);
		}
		mContext.sendBroadcast(intent);
	}

	@Override
	public void cleanUp() {
		if (mIsSDKOver8) {
			if (mGprsObserver != null) {
				mContext.getContentResolver().unregisterContentObserver(
						mGprsObserver);
				mGprsObserver = null;
			}
		} else {
			if (mReceiver != null) {
				mContext.unregisterReceiver(mReceiver);
				mReceiver = null;
			}
		}
	}

	@Override
	public int getSwitchType() {
		return SWITCH_TYPE_NORMAL_GPRS;
	}
}
