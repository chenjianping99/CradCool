
package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.System;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;


/**
 * 
 * <br>类描述:触感反馈开关器
 * <br>注意:从SwitchWidget抽离调整可以使用的代码
 * 
 * @author guoyiqing
 * @date  [2013-1-7]
 */
class HapticFeedbackHandler implements ISwitcherable {
	private Context mContext;
	private ContentResolver mContentResolver;
	private boolean mSettingEnabled;
//	private Handler mHandler;
	private ChangeObsert mChangeObsert;

	/* 触感反馈 */
	private Uri mBrightnessLevelUri = android.provider.Settings.System
			.getUriFor(Settings.System.HAPTIC_FEEDBACK_ENABLED);

	public HapticFeedbackHandler(Context context) {
		mContext = context;
		mContentResolver = mContext.getContentResolver();
		int val = 0;
		val = Settings.System.getInt(mContentResolver, System.HAPTIC_FEEDBACK_ENABLED, 0);
		mSettingEnabled = val != 0;
		mChangeObsert = new ChangeObsert(null);
		context.getContentResolver().registerContentObserver(mBrightnessLevelUri, true,
				mChangeObsert);
	}

	/**
	 * 开与关
	 * 
	 * @return
	 */
	public int isOn() {
		int val = 0;
		val = Settings.System.getInt(mContentResolver, System.HAPTIC_FEEDBACK_ENABLED, 0);
		mSettingEnabled = val != 0;
		if (mSettingEnabled) {
			return SwitchConstants.STATUS_ON;
		} else {
			return SwitchConstants.STATUS_OFF;
		}
	}

	/**
	 * 
	 * <br>类描述:触感反馈监听
	 * 
	 * @date  [2013-1-5]
	 */
	private class ChangeObsert extends ContentObserver {

		public ChangeObsert(Handler handler) {
			super(handler);
		}

		public void onChange(boolean selfChange) {
			broadCastState();
		}
	}

	@Override
	public void switchState() {
		int state = isOn();
		if (state == SwitchConstants.STATUS_OFF) {
			Settings.System.putInt(mContentResolver, System.HAPTIC_FEEDBACK_ENABLED, 1);
		} else if (state == SwitchConstants.STATUS_ON) {
			Settings.System.putInt(mContentResolver, System.HAPTIC_FEEDBACK_ENABLED, 0);
		}
	}

	@Override
	public void broadCastState() {
		Intent i = new Intent(BroadcastBean.HAPTIC_FEEDBACK_CHANGE);
		int val = 0;
		val = Settings.System.getInt(mContentResolver, System.HAPTIC_FEEDBACK_ENABLED, 0);
		mSettingEnabled = val != 0;
		if (mSettingEnabled) {
			i.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_ON);
		} else {
			i.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_OFF);
		}
		mContext.sendBroadcast(i);
	}

	@Override
	public void cleanUp() {
		if (mChangeObsert != null) {
			mContext.getContentResolver().unregisterContentObserver(mChangeObsert);
			mChangeObsert = null;
		}
	}

	@Override
	public int getSwitchType() {
		return SWITCH_TYPE_HAPTICFB;
	}

}
