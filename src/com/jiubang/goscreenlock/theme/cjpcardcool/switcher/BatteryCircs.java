package com.jiubang.goscreenlock.theme.cjpcardcool.switcher;

import java.util.ArrayList;
/**
 * 电量按钮点击发送的系统intent管理类，机型适配问题
 * @author oulingmei
 */

public class BatteryCircs {
	public final ArrayList<BatteryCircs> list = new ArrayList<BatteryCircs>();
	private String mPackageName; // activity包名
	private String mClassName; // activity类名
	private boolean mHasCat; // activity类名
	private boolean mHatPaCak = true;
	
	public BatteryCircs()
	{
		laodIntents();
	}
	
	private BatteryCircs(String packagename, String classname)
	{
		this.mClassName = classname;
		this.mPackageName = packagename;
	}
	
	public String getPackageName()
	{
		return mPackageName;
	}
	
	public String getClassName()
	{
		return mClassName;
	}
	
	public boolean isHasCat()
	{
		return mHasCat;
	}
	
	public String getTotalName()
	{
		String totalName = null;
		if (mHatPaCak)
		{
			totalName = mPackageName + "." + mClassName;
		} else
		{
			totalName = mClassName;
		}
		return totalName;
	}
	
	private void laodIntents()
	{
		//neo LgP900
		list.add(new BatteryCircs("com.android.settings", "fuelgauge.PowerUsageSummary"));
	}
	
	// 释放内存
	public void delete()
	{
		list.clear();
	}

}
