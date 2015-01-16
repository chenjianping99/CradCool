package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.Machine;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;

/**
 * 
*  <br>类描述:飞行模式开关器
 * <br>注意:从SwitchWidget抽离调整可以使用的代码
 * 
 * @author  guoyiqing
 * @date  [2013-1-7]
 */
class AirplaneModeHandler implements ISwitcherable {

	private Context mContext;
	private BroadcastReceiver mReceiver;

	public AirplaneModeHandler(Context context) {
		this.mContext = context;
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				sendBroacast(context);
			}
		};
		context.registerReceiver(mReceiver, intentFilter);
	}

	/**
	 * 是否打开了飞行模式
	 * 
	 * @return
	 */
	private boolean isOn(Context context) {
		return Settings.System.getInt(mContext.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) == 1;
	}

	/**
	 * 仅供BroadcastReceiver使用
	 * 
	 * @param context
	 */
	private void sendBroacast(Context context) {
		Intent intent = new Intent(BroadcastBean.AIRPLANE_CHANGE);
		if (isOn(context)) {
			intent.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_ON);
		} else {
			intent.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_OFF);
		}
		context.sendBroadcast(intent);
	}

	/*@Override
	public void switchState() {
		boolean isEnabled = false;
		if (isOn(mContext)) {
			isEnabled = true;
		}
		Settings.System.putInt(mContext.getContentResolver(), "airplane_mode_on",
				isEnabled ? 0 : 1);

		// 发出通知
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", !isEnabled);
		mContext.sendBroadcast(intent);
	}*/
	
	public void switchState() {
        if (mContext == null) {
            return;
        }
        boolean isEnabled = false;
        if (isOn(mContext)) {
            isEnabled = true;
        }
        if (Machine.IS_JELLY_BEAN_2) {
            Intent sysettingIntent = new Intent(
                    android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            sysettingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            try {
            	Global.sendUnlockWithIntent(mContext, null, null, null, sysettingIntent);
                //mContext.startActivity(sysettingIntent);
            } catch (Exception e) {
            	e.printStackTrace();
            }
        } else {
            Settings.System.putInt(mContext.getContentResolver(), "airplane_mode_on",
                    isEnabled ? 0 : 1);
           
            // 发出通知
            Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            intent.putExtra("state", !isEnabled);
            mContext.sendBroadcast(intent);
        }
    }

	@Override
	public void broadCastState() {
		Intent intent = new Intent(BroadcastBean.AIRPLANE_CHANGE);
		if (isOn(mContext)) {
			intent.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_ON);
		} else {
			intent.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_OFF);
		}
		mContext.sendBroadcast(intent);
	}

	@Override
	public void cleanUp() {
		if (mReceiver != null) {
			mContext.unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	@Override
	public int getSwitchType() {
		return SWITCH_TYPE_AIRPLANE_MODE;
	}
}
