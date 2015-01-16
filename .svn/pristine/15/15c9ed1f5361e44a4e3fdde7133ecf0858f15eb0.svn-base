package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;

/**
 * 
 * <br>类描述:震动设置器
 * <br>注意:从SwitchWidget抽离调整可以使用的代码
 * 
 * @author guoyiqing
 * @date  [2013-1-5]
 */
class VibrateHandler implements ISwitcherable {
	private AudioManager mAudioManager;
	private RangerBrocast mBrocastReciver;
	private IntentFilter mFilter;
	private Context mContext;

	public VibrateHandler(Context context) {
		mContext = context;
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		mBrocastReciver = new RangerBrocast();
		mFilter = new IntentFilter();
		mFilter.addAction(AudioManager.VIBRATE_SETTING_CHANGED_ACTION);
		context.registerReceiver(mBrocastReciver, mFilter);
	}

	/**
	 * 是否铃响
	 * 
	 * @return
	 */
	public boolean isRingerOn() {
		int ringerMode = mAudioManager.getRingerMode();
		if (ringerMode == AudioManager.RINGER_MODE_SILENT
				|| ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
			return false;
		}
		return true;
	}

	/**
	 * 是否震动
	 * 
	 * @return
	 */
	private boolean isVibrateOn() {
		if (mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION) == AudioManager.VIBRATE_SETTING_ON
				|| mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_ON) {
			return true;
		}
		return false;
	}


	/**
	 * 调整音量
	 * 
	 * @param directer
	 * @param much
	 */
	public void adjustVolume(int directer, int much) {
		int i = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
		i -= 3;
		mAudioManager.setStreamVolume(AudioManager.STREAM_RING, i, 1);
	}

	/**
	 * 
	 * <br>类描述:
	 * 
	 * @date  [2013-1-5]
	 */
	class RangerBrocast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (AudioManager.VIBRATE_SETTING_CHANGED_ACTION.equals(intent.getAction())) {
				broadCastState();
			}
		}
	}

	@Override
	public void switchState() {
		int ringerMode = mAudioManager.getRingerMode();
		if (ringerMode == AudioManager.RINGER_MODE_SILENT) {
			// 转换震动模式，打开震动
			mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
					AudioManager.VIBRATE_SETTING_ON);
			mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
					AudioManager.VIBRATE_SETTING_ON);
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		} else if (ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
			// 转换静音模式，关掉震动
			mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
					AudioManager.VIBRATE_SETTING_OFF);
			mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
					AudioManager.VIBRATE_SETTING_OFF);
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		} else {
			if (!isVibrateOn()) {
				mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
						AudioManager.VIBRATE_SETTING_ON);
				mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
						AudioManager.VIBRATE_SETTING_ON);
			} else {
				mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
						AudioManager.VIBRATE_SETTING_OFF);
				mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
						AudioManager.VIBRATE_SETTING_OFF);
			}
		}
	}

	@Override
	public void broadCastState() {
		Intent intents = new Intent(BroadcastBean.VIBRATE_CHANGE);
		if (isVibrateOn()) {
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
		if (mBrocastReciver != null) {
			mContext.unregisterReceiver(mBrocastReciver);
			mBrocastReciver = null;
		}
	}

	@Override
	public int getSwitchType() {
		return SWITCH_TYPE_VIBRATE;
	}
}
