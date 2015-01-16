package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;

/**
 * 
*  <br>类描述:自动旋转屏幕开关器
 * <br>注意:从SwitchWidget抽离调整可以使用的代码
 * 
 * @author  guoyiqing
 * @date  [2013-1-7]
 */
class AutoRotateHandler implements ISwitcherable {

	private Context mContext;

	private AutoRotateObserver mObserver;
	private Handler mHandler;

	public AutoRotateHandler(Context context) {
		this.mContext = context;
		mHandler = new Handler();
		mObserver = new AutoRotateObserver(mHandler);
		Uri uri = android.provider.Settings.System
				.getUriFor(Settings.System.ACCELEROMETER_ROTATION);
		context.getContentResolver().registerContentObserver(uri, true, mObserver);
	}


	/**
	 * 是否打开了自动旋转
	 * 
	 * @return
	 */
	private boolean isOn() {
		String state = Settings.System.getString(mContext.getContentResolver(),
				Settings.System.ACCELEROMETER_ROTATION);
		if (state != null && state.equals("1")) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * <br>类描述:自动旋转数据监听器
	 * 
	 * @date  [2013-1-5]
	 */
	private class AutoRotateObserver extends ContentObserver {
		public AutoRotateObserver(Handler handler) {
			super(handler);
		}

		public void onChange(boolean selfChange) {
			broadCastState();
		}
	}

	@Override
	public void switchState() {
		if (isOn()) {
			Settings.System.putString(mContext.getContentResolver(),
					Settings.System.ACCELEROMETER_ROTATION, "0");
		} else {
			Settings.System.putString(mContext.getContentResolver(),
					Settings.System.ACCELEROMETER_ROTATION, "1");
		}
	}

	@Override
	public void broadCastState() {
		Intent intents = new Intent(BroadcastBean.AUTO_ROTATE_CHANGE);
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
		if (mObserver != null) {
			mContext.getContentResolver().unregisterContentObserver(mObserver);
			mObserver = null;
		}
	}

	@Override
	public int getSwitchType() {
		return SWITCH_TYPE_AUTO_ROTATE;
	}
}
