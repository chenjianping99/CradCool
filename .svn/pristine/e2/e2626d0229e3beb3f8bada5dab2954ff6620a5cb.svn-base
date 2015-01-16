package com.jiubang.goscreenlock.theme.cjpcardcool.switcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * <br>功能详细描述:监控由各个开关发过来的当前状态的显示
 * 
 * @author  guoyiqing
 * @date  [2013-2-4]
 */
public class SwitchBroacastReceiver extends BroadcastReceiver {
	
	private OnSwitchReceiverListener mListener;
	
	public void setReceiverListener(OnSwitchReceiverListener listener) {
		mListener = listener;
	}
	
	public SwitchBroacastReceiver(OnSwitchReceiverListener listener) {
		mListener = listener;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (mListener != null) {
			mListener.onReiceive(context, intent);
		}
	}
}

