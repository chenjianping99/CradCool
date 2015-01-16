package com.jiubang.goscreenlock.theme.cjpcardcool.calendar;

import java.util.ArrayList;

/**
 * 匹配机型跳转到相应的系统闹钟界面
 * @author lishen
 *
 */
public class CalendarNameBean
{

	public final ArrayList<CalendarNameBean>	mList		= new ArrayList<CalendarNameBean>();
	// activity包名
	private String								mPackageName;
	// activity类名
	private String								mClassName;
	// activity类名(包含包名)
	private boolean								mHasCat;
	private boolean								mHatPaCak	= true;

	public CalendarNameBean()
	{
		loadIntents();
	}

	private CalendarNameBean(String packagename, String classname)
	{
		this.mClassName = classname;
		this.mPackageName = packagename;
	}

	private CalendarNameBean(String packagename, String classname, boolean hasCat)
	{
		this.mClassName = classname;
		this.mPackageName = packagename;
		mHasCat = hasCat;
	}

	private CalendarNameBean(String packagename, String classname, boolean hatPaCak,
			boolean hasCat)
	{
		this.mClassName = classname;
		this.mPackageName = packagename;
		mHasCat = hasCat;
		mHatPaCak = hatPaCak;
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
		}
		else
		{
			totalName = mClassName;
		}
		return totalName;
	}

	// 加载所有触发闹钟的Intents
	private void loadIntents()
	{
		// 乐Phone
		mList.add(new CalendarNameBean("com.lenovo.app.Calendar", "Calendar", true));
	}

	// 释放内存
	public void delete()
	{
		mList.clear();
	}
}
