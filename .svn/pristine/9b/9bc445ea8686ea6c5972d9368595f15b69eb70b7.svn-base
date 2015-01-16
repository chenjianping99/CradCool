package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.bean;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler.ISwitcherable;

/**
 * Switch Item date
 * @author zhongwenqi
 *
 */
public class SwitchItem extends BaseItem {

	public boolean mIsChecked;
	public int mIndex;
	public int mSwitchType;
	public String mSwitchName;
	public Drawable mIconOn, mIconOff;
	public boolean mIsOnOff;
	
	public boolean mIsOn;
	public int mState1, mState2;
	public SwitchItem(Context context, int mSwitchType) {
		super();
		this.mItemType = ItemType.SWITCH;
		this.mSwitchType = mSwitchType;
		mIsChecked = false;
		mIndex = mSwitchType;
		mSwitchName = BroadcastBean.getName(context, mSwitchType);
		
		mIsOnOff = getIsOnOff(mSwitchType);
	}
	
	private boolean getIsOnOff(int mSwitchType) {
		switch (mSwitchType) {
		case ISwitcherable.SWITCH_TYPE_AIRPLANE_MODE:
			return true;
		case ISwitcherable.SWITCH_TYPE_AUTO_ROTATE:
			return true;
		case ISwitcherable.SWITCH_TYPE_AUTO_SYNC:
			return true;
		case ISwitcherable.SWITCH_TYPE_BATTERY:
			return false;
		case ISwitcherable.SWITCH_TYPE_BLUETOOTH:
			return true;
		case ISwitcherable.SWITCH_TYPE_BRIGHTNESS:
			return false;
		case ISwitcherable.SWITCH_TYPE_FLASHLIGHT:
			return false;
		case ISwitcherable.SWITCH_TYPE_GPRS:
			return true;
		case ISwitcherable.SWITCH_TYPE_GPS:
			return true;
		case ISwitcherable.SWITCH_TYPE_HAPTICFB:
			return true;
		case ISwitcherable.SWITCH_TYPE_RING:
			return true;
		case ISwitcherable.SWITCH_TYPE_SCREEN_TIMEOUT:
			return false;
		case ISwitcherable.SWITCH_TYPE_SDMASS:
			return true;
		case ISwitcherable.SWITCH_TYPE_SDMOUNT:
			return true;
		case ISwitcherable.SWITCH_TYPE_VIBRATE:
			return true;
		case ISwitcherable.SWITCH_TYPE_WIFI:
			return true;
		case ISwitcherable.SWITCH_TYPE_WIFI_AP:
			return true;
		default:
			return false;
		}
	}
	
	
	
}
