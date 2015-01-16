package com.jiubang.goscreenlock.theme.cjpcardcool.calendar;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 
 * <br>类描述:处理Calendar的跳转逻辑处理
 * 
 * @author  guoyiqing
 * @date  [2013-1-6]
 */
public class CalendarJumper
{

	private static final String		TAG								= "CalendarJumper";
	private static final String		SYSTEM_CALENDAR_URI				= "content://com.android.calendar";
	private static final String		SYSTEM_CALENDAR_PKG				= "com.android.calendar";
	private static final String		SYSTEM_CALENDAR_MAIN_ACTIVITY	= "com.android.calendar.LaunchActivity";
	private static final String		CALENDAR_DATA_TYPE				= "time/epoch";
	private static final String		ANDROID_EDIT_INTENT				= "android.intent.action.EDIT";
	private static final String		CALENDAR_CURSOR_TYPE			= "vnd.android.cursor.item/event";
	private static final String		CALENDAR_EXTRA_STARTTIME		= "beginTime";
	private static final String		CALENDAR_EXTRA_ENDTIME			= "endTime";

	private static CalendarJumper	sJumper;

	private CalendarJumper()
	{
	}

	public static synchronized CalendarJumper getJumper()
	{
		if (sJumper == null)
		{
			sJumper = new CalendarJumper();
		}
		return sJumper;
	}

	public Intent getMainIntent(Context context)
	{
		Intent intent = null;
		try
		{
			if (context == null)
			{
				return null;
			}
			intent = new Intent(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setDataAndType(Uri.parse(SYSTEM_CALENDAR_URI), CALENDAR_DATA_TYPE);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//			context.startActivity(intent);
		}
		catch (ActivityNotFoundException e)
		{
			try
			{
				intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setClassName(SYSTEM_CALENDAR_PKG, SYSTEM_CALENDAR_MAIN_ACTIVITY);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//				context.startActivity(intent);
			}
			catch (Exception e2)
			{
				//				LogUtils.log(TAG, e2);
			}
		}
		return intent;
	}

	public void jumpToMain(Context context)
	{
		Intent intent = null;
		try
		{
			if (context == null)
			{
				return;
			}
			intent = new Intent(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setDataAndType(Uri.parse(SYSTEM_CALENDAR_URI), CALENDAR_DATA_TYPE);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
		catch (ActivityNotFoundException e)
		{
			try
			{
				intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setClassName(SYSTEM_CALENDAR_PKG, SYSTEM_CALENDAR_MAIN_ACTIVITY);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
			catch (Exception e2)
			{
				//				LogUtils.log(TAG, e2);
			}
		}
	}

	public void jumpToDetail(Context context, long eventId, long startTime, long endTime)
	{
		if (context == null)
		{
			return;
		}
		Uri uri = ContentUris.withAppendedId(CalendarConstants.CALENDER_EVENT_URL, eventId);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.putExtra(CALENDAR_EXTRA_STARTTIME, startTime);
		intent.putExtra(CALENDAR_EXTRA_ENDTIME, endTime);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try
		{
			context.startActivity(intent);
		}
		catch (Exception e)
		{
			//			LogUtils.log(TAG, e);
		}
	}

	public void jumpToEdit(Context context)
	{
		if (context == null)
		{
			return;
		}
		Intent intent = new Intent(ANDROID_EDIT_INTENT);
		intent.setType(CALENDAR_CURSOR_TYPE);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		long time = System.currentTimeMillis();
		intent.putExtra(CALENDAR_EXTRA_STARTTIME, time);
		intent.putExtra(CALENDAR_EXTRA_ENDTIME, time + 1000 * 60 * 60);
		try
		{
			context.startActivity(intent);
		}
		catch (ActivityNotFoundException e)
		{
			//			LogUtils.log(TAG, e);
		}
	}

}
