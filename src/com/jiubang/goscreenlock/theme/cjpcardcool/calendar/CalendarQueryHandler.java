package com.jiubang.goscreenlock.theme.cjpcardcool.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

/**
 * 
 * <br>类描述:日历查询处理
 * 
 * @author  guoyiqing
 * @date  [2013-1-5]
 */
public class CalendarQueryHandler extends AsyncQueryHandler
{

	private volatile ReentrantLock		mLock	= new ReentrantLock();
	private ArrayList<CalendarEvent>	mAgendaBeanList;
	private CalendarEvent				mAgendaBean;
	private OnQueryDataListener			mQueryListener;
	private int							mCount	= 0;
	public CalendarQueryHandler(ContentResolver cr, OnQueryDataListener listener)
	{
		super(cr);
		mAgendaBeanList = new ArrayList<CalendarEvent>();
		mAgendaBean = new CalendarEvent();
		setQueryListener(listener);
	}

	public void setQueryListener(OnQueryDataListener listener)
	{
		mQueryListener = listener;
	}

	/**
	 * 异步访问数据库
	 */
	public void startQuery()
	{
		Calendar calendar = Calendar.getInstance();
		long start = calendar.getTimeInMillis();
		// 往后推30天
		calendar.add(Calendar.DAY_OF_MONTH, 30);
		// 直到那一天的24：00：000
		calendar.set(Calendar.HOUR_OF_DAY, DateConstants.MAX_HOUR_OF_DAY);
		calendar.set(Calendar.MINUTE, DateConstants.MAX_MINUTE);
		calendar.set(Calendar.SECOND, DateConstants.MAX_SECOND);
		calendar.set(Calendar.MILLISECOND, DateConstants.MAX_MILLISECOND);
		long end = calendar.getTimeInMillis();
		String selection = null;
		if (Build.VERSION.SDK_INT >= 14)
		{
			selection = null;
		}
		else if (Build.VERSION.SDK_INT >= 8)
		{
			selection = "(deleted = 0) AND (selected = 1)" + " AND (("
					+ CalendarConstants.EVNET_STATUS + " <> " + CalendarConstants.STATUS_CANCELED
					+ ") or (" + CalendarConstants.EVNET_STATUS + " is null ))";

		}
		else
		{
			selection = "(selected = 1) AND ((" + CalendarConstants.EVNET_STATUS + " <> "
					+ CalendarConstants.STATUS_CANCELED + ") or (" + CalendarConstants.EVNET_STATUS
					+ " is null ))";
		}
		// 异步查询
		Uri uri = Uri.parse(CalendarProvider.CONTENT2_URI);
		uri = ContentUris.withAppendedId(uri, start);
		uri = ContentUris.withAppendedId(uri, end);
		startQuery(-1, null, uri, CalendarConstants22.PROJECTION, selection, null,
				CalendarConstants22.SORTBYDATA);
	}

	@Override
	protected void onQueryComplete(int token, Object cookie, Cursor cursor)
	{
		mLock.lock();
		try
		{
			if (token < 0)
			{
				mCount = 0;
				mAgendaBeanList.clear();
				if (cursor != null)
				{
					cursor.moveToFirst();
					// 筛选第一项开始时间比当前时间大的数据
					Calendar calendar = Calendar.getInstance();
					while (!cursor.isAfterLast())
					{
						String[] names = cursor.getColumnNames();
						CalendarEvent bean = new CalendarEvent();
						for (int i = 0; i < names.length; i++)
						{
							String name = names[i];
							int index = -1;
							try
							{
								index = cursor.getColumnIndex(name);
							}
							catch (Exception e)
							{
								// TODO: handle exception
							}
							if (index != -1)
							{
								if (name.equals(CalendarConstants.ID))
								{
									bean.mId = cursor.getLong(index);
								}
								else if (name.equals(CalendarConstants.TITLE))
								{
									bean.mTitle = cursor.getString(index);
								}
								else if (name.equals(CalendarConstants.TIME_START))
								{
									bean.mStartTime = cursor.getLong(index); // 新数据中的开始时间
								}
								else if (name.equals(CalendarConstants.TIME_END))
								{
									bean.mEndTime = cursor.getLong(index); // 新数据中的结束时间
								}
								else if (name.equals(CalendarConstants.ALLDAY))
								{
									bean.mAllDay = cursor.getInt(index);
								}
								else if (name.equals(CalendarConstants22.LOCATION))
								{
									bean.mLocation = cursor.getString(index);
								}
								else if (name.equals(CalendarConstants.EVENT_ID))
								{
									bean.mEventId = cursor.getLong(index);
								}
								else if (name.equals(CalendarConstants.HASALARM))
								{
									int hasAlarm = cursor.getInt(index);
									bean.mHasAlarm = hasAlarm == 1 ? true : false;
								}
							}
						}
						if (bean.mAllDay == 1)
						{
							long dstoff = calendar.get(Calendar.DST_OFFSET);
							long gmtoff = calendar.get(Calendar.ZONE_OFFSET);
							bean.mFinalStartTime = bean.mStartTime - gmtoff - dstoff;
						}
						else
						{
							bean.mFinalStartTime = bean.mStartTime;
						}
						mAgendaBeanList.add(bean);
						cursor.moveToNext();
					}
				}
				Collections.sort(mAgendaBeanList, new Comparator<CalendarEvent>()
				{
					public int compare(CalendarEvent object1, CalendarEvent object2)
					{
						int i = -1;
						if (object1.mFinalStartTime < object2.mFinalStartTime)
						{
							i = -1;
						}
						else if (object1.mFinalStartTime == object2.mFinalStartTime)
						{
							i = 0;
						}
						else
						{
							i = 1;
						}
						return i;
					}
				});
				updateAgendaData();
				if (mAgendaBeanList != null)
				{
					for (int i = 0; i < mAgendaBeanList.size(); i++)
					{
						startQuery(i + 1, null, Uri.parse(CalendarProvider.EVENT2_URI),
								CalendarConstants22.PROJECTION_WARM, CalendarConstants.EVENT_ID
										+ "='" + mAgendaBeanList.get(i).mEventId + "'", null,
								CalendarConstants.EVENT_MINUTE + " DESC");
					}
				}
			}
			else
			{
				mCount++;
				if (mCount <= mAgendaBeanList.size())
				{
					if (cursor != null)
					{
						cursor.moveToFirst();
						long eventId = -1;
						long minute = 0;
						List<Long> remiders = new ArrayList<Long>();
						while (!cursor.isAfterLast())
						{
							if (eventId <= 0)
							{
								eventId = 0;
								try
								{
									eventId = cursor.getLong(cursor
											.getColumnIndex(CalendarConstants.EVENT_ID));
								}
								catch (Exception e)
								{
									// TODO: handle exception
								}
							}
							long m = 0;
							try
							{
								m = cursor.getLong(cursor
										.getColumnIndex(CalendarConstants.EVENT_MINUTE));
							}
							catch (Exception e)
							{
								// TODO: handle exception
							}
							remiders.add(m * 60 * 1000);
							if (m > minute)
							{
								minute = m;
							}
							cursor.moveToNext();
						}
						if (eventId > 0)
						{
							setRemineTimeIntoAgendaBeanListByEventId(eventId, minute * 60 * 1000,
									remiders);
						}
					}
				}
				if (mCount >= mAgendaBeanList.size())
				{
					// 通知取得数据成功
					if (mQueryListener != null)
					{
						mQueryListener.onQueryFinish(false, mAgendaBean, mAgendaBeanList);
					}
				}
			}
		}
		finally
		{
			mLock.unlock();
			if (cursor != null)
			{
				cursor.close();
			}
		}
	}
	/**
	 * 更新显示的日程显示数据
	 */
	private void updateAgendaData()
	{
		mAgendaBean.clearData(); // 用于记录最接近当前时间的日程
		Calendar now = Calendar.getInstance();
		long nowTime = now.getTimeInMillis();
		for (CalendarEvent agendaBeanBuf : mAgendaBeanList)
		{
			// 如果到目前时间还没有开始，跳过
			if (agendaBeanBuf.mFinalStartTime < nowTime)
			{
				continue;
			}
			if (agendaBeanBuf.mIsVacation)
			{
				continue;
			}
			mAgendaBean.cloneData(agendaBeanBuf);
			break;
		}
	}

	private void setRemineTimeIntoAgendaBeanListByEventId(long eventId, long time,
			List<Long> remiders)
	{
		if (mAgendaBeanList != null)
		{
			for (CalendarEvent agendaBeanBuf : mAgendaBeanList)
			{
				if (agendaBeanBuf.mEventId == eventId)
				{
					agendaBeanBuf.mRemindersTime = time;
					agendaBeanBuf.mRemindersTimeList = remiders;
					break;
				}
			}
		}
	}
}
