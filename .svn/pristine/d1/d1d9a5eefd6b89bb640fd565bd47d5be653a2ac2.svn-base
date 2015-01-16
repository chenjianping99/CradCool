package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;

/**
 *  Battery Server
 * @author zhongwenqi
 *
 */
public class BatteryHandler implements ISwitcherable {

	private Context mContext;
	private int mLevel;
	private int mStatus; //UNKNOWN = 1; CHARGING = 2; DISCHARGING = 3;NOT_CHARGING = 4;FULL = 5;
	public BatteryHandler(Context mContext) {
		super();
		this.mContext = mContext;
		monitorBatteryState();
	}
	
	@Override
	public void switchState() {
		// TODO Auto-generated method stub

	}

	@Override
	public void broadCastState() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(BroadcastBean.BATTERY_CHANGE);
		intent.putExtra(BroadcastBean.STATUS1, mLevel);
		intent.putExtra(BroadcastBean.STATUS2, mStatus);
		mContext.sendBroadcast(intent);
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		if (mReceiver != null) {
			mContext.unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	@Override
	public int getSwitchType() {
		// TODO Auto-generated method stub
		return SWITCH_TYPE_BATTERY;
	}
	
	private BroadcastReceiver mReceiver;
	private IntentFilter mIntentFilter;
	private void monitorBatteryState() {
		mReceiver = new BroadcastReceiver() {
			
			public void onReceive(Context context, Intent intent) {
				StringBuilder sb = new StringBuilder();
				int rawlevel = intent.getIntExtra("level", -1);
				int scale = intent.getIntExtra("scale", -1);
				mStatus = intent.getIntExtra("status", -1);
				int health = intent.getIntExtra("health", -1);
				mLevel = -1; // percentage, or -1 for unknown
				if (rawlevel >= 0 && scale > 0) {
					mLevel = (rawlevel * 100) / scale;
				}
				broadCastState();
				
				/*sb.append("The phone");
				if (BatteryManager.BATTERY_HEALTH_OVERHEAT == health) {
					sb.append("'s battery feels very hot!");
				} else {
					switch (mStatus) {
					case BatteryManager.BATTERY_STATUS_UNKNOWN:
						sb.append("no battery.");
						break;
					case BatteryManager.BATTERY_STATUS_CHARGING:
						sb.append("'s battery");
						if (mLevel <= 33)
							sb.append(" is charging, battery level is low"
									+ "[" + mLevel + "]");
						else if (mLevel <= 84)
							sb.append(" is charging." + "[" + mLevel + "]");
						else
							sb.append(" will be fully charged.");
						break;
					case BatteryManager.BATTERY_STATUS_DISCHARGING:
					case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
						if (mLevel == 0)
							sb.append(" needs charging right away.");
						else if (mLevel > 0 && mLevel <= 33)
							sb.append(" is about ready to be recharged, battery level is low"
									+ "[" + mLevel + "]");
						else
							sb.append("'s battery level is" + "[" + mLevel + "]");
						break;
					case BatteryManager.BATTERY_STATUS_FULL:
						sb.append(" is fully charged.");
						break;
					default:
						sb.append("'s battery is indescribable!");
						break;
					}
				}
				sb.append(' ');*/
				//batterLevel.setText(sb.toString());
			}
		};
		mIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		mContext.registerReceiver(mReceiver, mIntentFilter);
	}

}
