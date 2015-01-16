package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.bean;

import android.graphics.Bitmap;

/**
 * 基本类型
 * @author zhangjie
 *
 */
public abstract class BaseApp {
	public static final int	DEFAULT_POSITION	= -1;
	protected int			mBaseType			= SwipeTypes.ILLEGLE_TYPE;	//基本类型
	protected int			mPosition			= DEFAULT_POSITION;		//在侧边栏显示的位置
	protected String		mID;											//在数据库的ID位置
	public Bitmap			mIcon				= null; //图标显示
	
	public BaseApp() {
		
	}

	public int getmBaseType() {
		return mBaseType;
	}
	public void setmBaseType(int mBaseType) {
		this.mBaseType = mBaseType;
	}
	public String getID() {
		return mID;
	}
	public void setID(int iD) {
		mID = String.valueOf(iD);
	}
	public int getPosition() {
		return mPosition;
	}
	public void setPosition(int position) {
		this.mPosition = position;
	}
	public Bitmap getIcon()
	{
		return mIcon;
	}
	public void setIcon(Bitmap icon)
	{
		this.mIcon = icon;
	}

	/**
	 * 比较两个应用是否相同
	 * @param baseApp
	 * @return true为相同，false为不同
	 */
	protected abstract boolean compare(BaseApp baseApp);
	/**
	 * 
	 */
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		return stringBuffer.append("BaseType is :").append(mBaseType).append("; the postion is：")
				.append(mPosition).toString();
	}

	protected boolean compareStr(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return true;
		}
		else if (str1 == null && (str2 != null && str2.length() <= 0)) {
			return true;
		}
		else if (str2 == null && (str1 != null && str1.length() <= 0)) {
			return true;
		}
		else if (str1 == null && (str2 != null && str2.length() > 0)) {
			return false;
		}
		else if (str2 == null && (str1 != null && str1.length() > 0)) {
			return false;
		}
		else {
			return str1.equals(str2);
		}
	}
	
	public void onDestroy() {
		if (mIcon != null && !mIcon.isRecycled()) {
			mIcon.recycle();
			mIcon = null;
		}
	}
}
