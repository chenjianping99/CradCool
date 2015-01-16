package com.jiubang.goscreenlock.theme.cjpcardcool.switcher;

import android.graphics.drawable.Drawable;

/**
 * 
 * <br>类描述:开关info
 * <br>功能详细描述:
 * 
 * @author  zhengxiangcan
 * @date  [2013-4-12]
 */
public class SwitchInfo {
	
	public int mSwitchId = -1;
	public int mIndex = -1;
	public Drawable mIcon = null;
	public String mSwitchName = null;
	public boolean mIsChecked = false;
	public Drawable[] mDrawables;
	
	public SwitchInfo(int index, int switchId) {
		mIndex = index;
		mSwitchId = switchId;
	}

	public void setIcon(Drawable drawable) {
		if (drawable != null) {
			mIcon = drawable;
		}
	}
	
	public Drawable getIcon() {
		return mIcon;
	}
}
