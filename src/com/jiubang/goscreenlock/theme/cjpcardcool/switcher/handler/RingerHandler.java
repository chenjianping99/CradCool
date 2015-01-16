package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;

/**
 * 
 * <br>类描述:铃声设置器
 * <br>注意:从SwitchWidget抽离调整可以使用的代码
 * 
 * @date  [2013-1-5]
 */
class RingerHandler implements ISwitcherable
{
	private static final String	TAG	= "RingerHandler";
	private AudioManager		mAudioManager;
	private RangerBrocast		mBrocastReciver;
	private IntentFilter		mFilter;
	private Context				mContext;

	public RingerHandler(Context context)
	{
		mContext = context;
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		mBrocastReciver = new RangerBrocast();
		mFilter = new IntentFilter();
		mFilter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
		context.registerReceiver(mBrocastReciver, mFilter);
	}

	/**
	 * 是否铃响
	 * 
	 * @return
	 */
	public boolean isRingerOn()
	{
		int ringerMode = mAudioManager.getRingerMode();
		if (ringerMode == AudioManager.RINGER_MODE_SILENT
				|| ringerMode == AudioManager.RINGER_MODE_VIBRATE)
		{
			return false;
		}
		return true;
	}

	/**
	 * 是否震动
	 * 
	 * @return
	 */
	private boolean isVibrateOn()
	{
		if (mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION) == AudioManager.VIBRATE_SETTING_ON
				|| mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_ON)
		{
			return true;
		}
		return false;
	}

	/**
	 * 
	 * <br>类描述:铃声状态监听
	 * 
	 * @date  [2013-1-5]
	 */
	class RangerBrocast extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (AudioManager.RINGER_MODE_CHANGED_ACTION.equals(intent.getAction()))
			{
				broadCastState();
			}
		}
	}

	@Override
	public void switchState()
	{
	    Log.i("ddd", "RingerHandler switchState");
		Intent i = new Intent(BroadcastBean.CLICKABLE_RING);
		mContext.sendBroadcast(i);
		try
		{
			int ringerMode = mAudioManager.getRingerMode();
			if (ringerMode == AudioManager.RINGER_MODE_NORMAL)
			{
				// 转换静音模式，关掉震动
				if (true  || isVibrateOn())
				{
					mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
				}
				else
				{
					mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
				}
			}
			else if (ringerMode == AudioManager.RINGER_MODE_SILENT)
			{
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			}
			else if (ringerMode == AudioManager.RINGER_MODE_VIBRATE)
			{
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			}
			broadCastState();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void broadCastState()
	{
		Intent intents = new Intent(BroadcastBean.RINGER_CHANGE);
		int ringerMode = mAudioManager.getRingerMode();
		intents.putExtra(BroadcastBean.STATUS1, ringerMode);
		/*if (ringerMode == AudioManager.RINGER_MODE_NORMAL)
		{
			//			intents.setFlags(SwitchConstants.RINGER_NORMAL);
			intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.RINGER_NORMAL);
		}
		else if (ringerMode == AudioManager.RINGER_MODE_SILENT
				|| ringerMode == AudioManager.RINGER_MODE_VIBRATE)
		{
			//			intents.setFlags(SwitchConstants.RINGER_SILENT);
			intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.RINGER_SILENT);
		}*/
		mContext.sendBroadcast(intents);
	}

	@Override
	public void cleanUp()
	{
		if (mBrocastReciver != null)
		{
			try
			{
				mContext.unregisterReceiver(mBrocastReciver);
			}
			catch (Exception e)
			{
				//				LogUtils.log(TAG, e);
			}
			mBrocastReciver = null;
		}
	}

	@Override
	public int getSwitchType()
	{
		return SWITCH_TYPE_RING;
	}
}
