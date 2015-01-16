package com.jiubang.goscreenlock.theme.cjpcardcool.calendar;

import java.util.Calendar;
import java.util.List;

/**
 * 
 * <br>类描述:日程bean
 * 
 * @date  [2013-1-5]
 */
public class CalendarEvent
{
	public final static long	NO_AGENDA	= -100;

	public long					mStartTime;

	public long					mEndTime;

	public long					mFinalStartTime;

	public String				mTitle;

	public long					mId			= NO_AGENDA;

	public long					mRemindersTime;

	public List<Long>			mRemindersTimeList;

	public boolean				mHasAlarm;

	/**
	 * 事件在instance中的主键
	 */
	public long					mEventId;
	/**
	 * 位置
	 */
	public String				mLocation;

	public boolean				mIsAgenda	= true;

	/**
	 * 是否全天
	 */
	public int					mAllDay;
	public boolean				mIsVacation	= false;

	public CalendarEvent()
	{
	}

	public CalendarEvent(Calendar calendar, String vacationString)
	{
		long time = 0;
		mTitle = vacationString;
		calendar.add(Calendar.MINUTE, -2);
		time = calendar.getTimeInMillis();
		mEndTime = time;
		mStartTime = time;
		mFinalStartTime = time;
		mAllDay = 1;
		mIsVacation = true;
	}
	public CalendarEvent(CalendarEvent bean)
	{
		mIsAgenda = bean.mIsAgenda;
		mId = bean.mId;
		mTitle = bean.mTitle;
		mEndTime = bean.mEndTime;
		mStartTime = bean.mStartTime;
		mAllDay = bean.mAllDay;
		mLocation = bean.mLocation;
		mEventId = bean.mEventId;
		mFinalStartTime = bean.mFinalStartTime;
	}

	public void clearData()
	{
		mIsAgenda = true;
		mId = NO_AGENDA;
		mTitle = "";
		mEndTime = 0;
		mStartTime = 0;
		mAllDay = 0;
		mLocation = "";
		mEventId = 0;
		mFinalStartTime = 0;
	}

	public void cloneData(CalendarEvent bean)
	{
		mIsAgenda = bean.mIsAgenda;
		mId = bean.mId;
		mTitle = bean.mTitle;
		mEndTime = bean.mEndTime;
		mStartTime = bean.mStartTime;
		mAllDay = bean.mAllDay;
		mLocation = bean.mLocation;
		mEventId = bean.mEventId;
		mFinalStartTime = bean.mFinalStartTime;
	}
}
