package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;

/**
 * <br>类描述:屏幕暗屏时间
 * <br>注意:从SwitchWidget抽离调整可以使用的代码
 * 
 * @author guoyiqing
 * @date  [2013-1-5]
 */
class ToggleScreenTimeoutHandler implements ISwitcherable {

	private static final String TAG = "ToggleScreenTimeoutHandler";
	private Context mContext;
	private ContentResolver mResolver;
	private ToggleScreenObserver mObserver;
	private Handler mHandler;
	private String mTimeout;
	private int mIndex;
	private final int[] mTimePiont = { 15000, 30000, 60000, 300000, 600000, -1 };

	public ToggleScreenTimeoutHandler(Context context) {
		mContext = context;
		initHandler();
		mResolver = context.getContentResolver();
		mTimeout = android.provider.Settings.System.SCREEN_OFF_TIMEOUT;
		mObserver = new ToggleScreenObserver(mHandler);
		Uri uri = Settings.System.getUriFor(android.provider.Settings.System.SCREEN_OFF_TIMEOUT);
		context.getContentResolver().registerContentObserver(uri, true, mObserver);
	}

	private void initHandler() {
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				broadCastState();
			};
		};
	}

	/**
	 * 获取现在屏幕待机超时时间,并更新数组下标
	 */
	private void updateTimeOut() {
		try {
			int time = android.provider.Settings.System.getInt(mResolver, mTimeout);
			if (time <= 15000 && time > 0) {
				mIndex = 0;
			} else if (time > 15000 && time <= 30000) {
				mIndex = 1;
			} else if (time > 30000 && time <= 60000) {
				mIndex = 2;
			} else if (time > 60000 && time <= 300000) {
				mIndex = 3;
			} else if (time > 300000) {
				mIndex = 4;
			} else if (time == -1) {
				mIndex = 5;
			}
		} catch (SettingNotFoundException e) {
//			LogUtils.log(TAG, e);
		}
	}

	/**
	 * 
	 * <br>类描述:屏幕暗屏时间监听器
	 * 
	 * @date  [2013-1-5]
	 */
	private class ToggleScreenObserver extends ContentObserver {

		private final Handler mHandler;

		public ToggleScreenObserver(Handler handler) {
			super(handler);
			this.mHandler = handler;
		}

		public void onChange(boolean selfChange) {
			this.mHandler.sendEmptyMessage(0);
		}
	}

	@Override
	public void switchState() {
		if (mIndex >= 5) {
			mIndex = 0;
		} else {
			mIndex++;
		}
		android.provider.Settings.System.putInt(mResolver, mTimeout, mTimePiont[mIndex]);
	}

	@Override
	public void cleanUp() {
		if (mObserver != null && mContext != null) {
			mContext.getContentResolver().unregisterContentObserver(mObserver);
			mObserver = null;
		}
	}

	@Override
	public void broadCastState() {
		Intent intent = new Intent(BroadcastBean.TIMEOUT_CHANGE);
		updateTimeOut();
		intent.putExtra(BroadcastBean.STATUS1, mIndex);
		mContext.sendBroadcast(intent);
	}

	@Override
	public int getSwitchType() {
		return SWITCH_TYPE_SCREEN_TIMEOUT;
	}
}
