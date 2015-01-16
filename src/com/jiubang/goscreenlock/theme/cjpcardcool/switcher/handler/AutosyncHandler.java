package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Build;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;

/**
 * 
*  <br>类描述:同步开关器
 * <br>注意:从SwitchWidget抽离调整可以使用的代码
 * 
 * @author  guoyiqing
 * @date  [2013-1-7]
 */
class AutosyncHandler implements ISwitcherable {

	private Context mContext;
	private Observer mObserver;
	private Object mPendingObject;
	private Object mActiveObject;
	private Object mSettingObject;

	public AutosyncHandler(Context context) {
		this.mContext = context;
		broadCastState();
		mObserver = new Observer();
		if (Build.VERSION.SDK_INT >= 8) {
			mPendingObject = ContentResolver.addStatusChangeListener(
					ContentResolver.SYNC_OBSERVER_TYPE_PENDING, mObserver);
			mActiveObject = ContentResolver.addStatusChangeListener(
					ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE, mObserver);
			mSettingObject = ContentResolver.addStatusChangeListener(
					ContentResolver.SYNC_OBSERVER_TYPE_SETTINGS, mObserver);
		} else {
			mPendingObject = ContentResolver.addStatusChangeListener(2, mObserver);
			mActiveObject = ContentResolver.addStatusChangeListener(4, mObserver);
			mSettingObject = ContentResolver.addStatusChangeListener(1, mObserver);
		}
	}

	/**
	 * 是否打开了自动同步
	 * @return
	 */
	private boolean isOn() {
		return ContentResolver.getMasterSyncAutomatically();
	}

	/**
	 * 
	 * <br>类描述:同步数据监听
	 * 
	 * @date  [2013-1-5]
	 */
	class Observer implements SyncStatusObserver {
		@Override
		public void onStatusChanged(int which) {
			broadCastState();
		}
	}

	@Override
	public void switchState() {
		if (isOn()) {
			ContentResolver.setMasterSyncAutomatically(false);
		} else {
			ContentResolver.setMasterSyncAutomatically(true);
		}
	}

	@Override
	public void broadCastState() {
		Intent intents = new Intent(BroadcastBean.AUTO_SYNC_CHANGE);
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
		if (mPendingObject != null) {
			ContentResolver.removeStatusChangeListener(mPendingObject);
			mPendingObject = null;
		}
		if (mActiveObject != null) {
			ContentResolver.removeStatusChangeListener(mActiveObject);
			mActiveObject = null;
		}
		if (mSettingObject != null) {
			ContentResolver.removeStatusChangeListener(mSettingObject);
			mSettingObject = null;
		}
	}

	@Override
	public int getSwitchType() {
		return SWITCH_TYPE_AUTO_SYNC;
	};
}
