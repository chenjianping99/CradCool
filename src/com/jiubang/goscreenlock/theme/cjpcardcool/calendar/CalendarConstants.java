package com.jiubang.goscreenlock.theme.cjpcardcool.calendar;

import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
/**
 * 
 * <br>类描述:Calendar常量
 * 
 * @date  [2013-1-5]
 */
public class CalendarConstants {
	/**
	 * Log Key
	 */
	public final static String WIDGET = "GO_WIDGET";
	/**
	 * 消息ID：日程改变
	 */
	public final static int UPDATE_AGENDA = 1;
	/**
	 * 消息ID：时间改变
	 */
	public final static int UPDATE_TIME = 2;
	/**
	 * 消息ID：刷新数据
	 */
	public final static int REFRESH_DATA = 3;
	/**
	 * 消息ID：刷新时间
	 */
	public final static int REFRESH_TIME = 4;
	/**
	 * 消息ID：查询DB
	 */
	public final static int QUERY_DB = 5;

	/**
	 * 消息延迟时间,1000MS
	 */
	public final static int DELAY_TIME = 1000;
	/**
	 * 消息延迟时间,500MS
	 */
	public final static int DELAY_TIME_500 = 500;
	/**
	 * 一天的毫秒数
	 */
	public final static int DAY_TIME_MILLIS = 1000 * 60 * 60 * 24;

	/**
	 * 日程所属类型URL
	 */
	public static final Uri CALENDER_URL;

	/**
	 * 日程事件URL
	 */
	public static final Uri CALENDER_EVENT_URL;
	/**
	 * 日程提醒URL
	 */
	public static final Uri CALENDER_REMIDER_URL;
	/**
	 * instances表
	 */
	public static final String CALENDER_INSTANCE_URL;
	static long sMax = Long.MAX_VALUE;
	static {
		if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
			CALENDER_URL = Uri.parse("content://com.android.calendar/calendars");
			CALENDER_EVENT_URL = Uri.parse("content://com.android.calendar/events");
			CALENDER_REMIDER_URL = Uri.parse("content://com.android.calendar/reminders");
			// isntances表
			CALENDER_INSTANCE_URL = "content://com.android.calendar/instances/when";
		} else {
			CALENDER_URL = Uri.parse("content://calendar/calendars");
			CALENDER_EVENT_URL = Uri.parse("content://calendar/events");
			CALENDER_REMIDER_URL = Uri.parse("content://calendar/reminders");
			// isntances表
			CALENDER_INSTANCE_URL = "content://calendar/instances/when";
		}
	}

	public static final String ID = "_id";
	public static final String TIME_START = "begin";
	public static final String TIME_END = "end";

	public static final String TITLE = "title";

	public static final String ALLDAY = "allDay";

	public static final String HASALARM = "hasAlarm";

	public static final String COLOR = "color";
	public static final String COLOR_FOR_14 = CalendarContract.Instances.EVENT_COLOR;

	// 事件在各个表中统一的id，作为外键使用
	public static final String EVENT_ID = "event_id";
	/** The event status */
	public static final String EVNET_STATUS = "eventStatus";
	public static final String EVENT_MINUTE = "minutes";

	public static final int STATUS_TENTATIVE = 0;
	public static final int STATUS_CONFIRMED = 1;
	public static final int STATUS_CANCELED = 2;
}
