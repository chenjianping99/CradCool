package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.bean;

/**
 * Base Item date
 * @author zhongwenqi
 *
 */
public class BaseItem {
	
	public boolean mIsChecked;  // 是否在多项框中选中
	public ItemType mItemType;
	public int mPosition;
	
	/**
	 * Item Type
	 * @author zhongwenqi
	 *
	 */
	public enum ItemType {
		APP, SWITCH
	}
}
