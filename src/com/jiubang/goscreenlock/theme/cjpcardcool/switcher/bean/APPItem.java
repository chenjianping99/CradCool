package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.bean;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * 
 * <br>类描述:app的每一个数据项
 * 
 * @author  guoyiqing
 * @date  [2013-1-12]
 */
public class APPItem extends BaseItem {

	private static final String TAG = "APPItem";

	//public boolean mIsChecked;  // 是否在多项框中选中
	
	/**
	 * 在常用应用的位置
	 */
	public int mIndex; 
	
	public String mPkgName;
	
	public String mAPPName;
	
	public Drawable mIcon;
	
	public String mMainClassName;
	
	public APPItem() {
		super();
		// TODO Auto-generated constructor stub
		this.mItemType = ItemType.APP;
	}

	public void startAPP(Context context) {
		if (context == null || mPkgName == null || mMainClassName == null) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_MAIN);
		ComponentName cn = new ComponentName(mPkgName, mMainClassName);
		intent.setComponent(cn);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
