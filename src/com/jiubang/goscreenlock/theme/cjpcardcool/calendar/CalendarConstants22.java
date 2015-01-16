package com.jiubang.goscreenlock.theme.cjpcardcool.calendar;

/**
 * 
 * <br>类描述:Calendar常量
 * 
 * @date  [2013-1-5]
 */
public class CalendarConstants22
{
	public static final String		LOCATION		= "eventLocation";
	/**
	 * 查询的字段
	 */
	public static final String[]	PROJECTION		= { CalendarConstants.ID,
			CalendarConstants.TITLE, CalendarConstants.TIME_START, CalendarConstants.TIME_END,
			LOCATION, CalendarConstants.ALLDAY, CalendarConstants.EVENT_ID,
			CalendarConstants.HASALARM				};

	/**
	 * 查询的字段
	 */
	public static final String[]	PROJECTION_WARM	= { CalendarConstants.EVENT_ID,
			CalendarConstants.EVENT_MINUTE			};
	/**
	 * 排序方式
	 */
	public static final String		SORTBYDATA		= "begin ASC,title ASC";
}
