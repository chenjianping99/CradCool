package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/**
 * <br>
 * 类描述: <br>
 * 功能详细描述:
 * 
 * @author xuqian
 * @date [2013-7-2]
 */
public class SliderApp extends BaseApp {
	public static final int TYPE_DEFAULT = 0; // 照相机，拨号，短信
	public static final int TYPE_CUSTOM = 1; // 其它应用
	public static final int ICON_DEFAULT = 0; // 默认图标
	public static final int ICON_CUSTOM = 1; // 照相机，拨号，短信等自定义图标

	private String mPkgName = null;
	private String mClassName = null;
	private String mAppName = null;
	private int mAppType = TYPE_CUSTOM;
	private int mIconType = ICON_DEFAULT;
	private boolean mIsAdd = false; // 是否添加按钮的icon

	public SliderApp() {
		mBaseType = SwipeTypes.APP_TYPE;
	}

	public SliderApp(APPItem item) {
		this.mPkgName = item.mPkgName;
		this.mAppName = item.mAPPName;
		this.mClassName = item.mMainClassName;
		if (item.mIcon != null) {
			this.mIcon = ((BitmapDrawable) item.mIcon).getBitmap();
		}
		mPosition = item.mPosition;
		mBaseType = SwipeTypes.APP_TYPE;
	}
	
	public SliderApp(APPItem item, Bitmap bitmap) {
		this.mPkgName = item.mPkgName;
		this.mAppName = item.mAPPName;
		this.mClassName = item.mMainClassName;
		this.mIcon = bitmap;

		mPosition = item.mPosition;
		mBaseType = SwipeTypes.APP_TYPE;
	}

	public boolean ismIsAdd() {
		return mIsAdd;
	}

	public void setmIsAdd(boolean mIsAdd) {
		this.mIsAdd = mIsAdd;
	}

	public String getPkgName() {
		return mPkgName;
	}

	public void setPkgName(String pkgName) {
		this.mPkgName = pkgName;
	}

	public String getClassName() {
		return mClassName;
	}

	public void setClassName(String className) {
		this.mClassName = className;
	}

	public String getAppName() {
		return mAppName;
	}

	public void setAppName(String appName) {
		this.mAppName = appName;
	}

	public int getAppType() {
		return mAppType;
	}

	public void setAppType(int type) {
		this.mAppType = type;
	}

	public int getIconType() {
		return mIconType;
	}

	public void setIconType(int iconType) {
		this.mIconType = iconType;
	}

	@Override
	public String toString() {
		return mAppName;
	}

	/*
	 * @Override public int compareTo(SliderApp another) { if (another == null)
	 * { return 0; } if (mPosition != DEFAULT_POSITION && another.getPosition()
	 * != DEFAULT_POSITION) { return mPosition - another.getPosition(); } if
	 * (mAppName == null || another.getAppName() == null) { return 0; } return
	 * mAppName.compareTo(another.getAppName()); }
	 * 
	 * @Override public boolean equals(Object o) { if (o != null) { if (o
	 * instanceof SliderApp) { if (((SliderApp) o).getAppName() != null &&
	 * mAppName != null) { if (((SliderApp) o).getAppName().equals(mAppName)) {
	 * return true; } } } } return false; }
	 */
	@Override
	protected boolean compare(BaseApp baseApp) {
		if (baseApp == null) {
			return false;
		}
		if (baseApp instanceof SliderApp) {
			SliderApp sliderApp = (SliderApp) baseApp;
			if (compareStr(sliderApp.getAppName(), mAppName)) {
				return true;
			}
		}
		return false;
	}
}
