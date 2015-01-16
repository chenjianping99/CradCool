package com.jiubang.goscreenlock.theme.cjpcardcool.calendar;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;

import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;

//CHECKSTYLE:OFF
public class CalendarProvider extends ContentProvider
{

	// 为了兼容不同版本的日历,2以后url发生改变

	private static final String		URI_AUTHORITY			= Constant.THEME_PACKAGE_NAME + ".calendar.CalendarProvider";

	public static final String		URI_PATH				= "RecordSet";															//
	public static final String		URI_PATH2				= "RecordSet/*";
	public static final String		URI_PATH3				= "RecordSet/*/*";

	public static final String		URI_EVENT				= "Event";

	public static final Uri			CONTENT_URI				= Uri.parse("content://"
																	+ URI_AUTHORITY + "/"
																	+ URI_PATH);
	public static final Uri			EVENT_URI				= Uri.parse("content://"
																	+ URI_AUTHORITY + "/"
																	+ URI_EVENT);
	public static final String		EVENT2_URI				= EVENT_URI.toString();

	public static final String		CONTENT2_URI			= CONTENT_URI.toString();


	private static final UriMatcher	sMatcher;

	public static final int			ALL_EVENT_RECORDS		= 0;
	public static final int			SINGLE_EVENT_RECORDS	= 1;
	public static final int			EARLY_EVENT_RECORDS		= 2;
	public static final int			ALARM_EVENT_RECORDS		= 3;

	static
	{
		sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sMatcher.addURI(URI_AUTHORITY, URI_PATH3, EARLY_EVENT_RECORDS);
		sMatcher.addURI(URI_AUTHORITY, URI_PATH2, SINGLE_EVENT_RECORDS);
		sMatcher.addURI(URI_AUTHORITY, URI_PATH, ALL_EVENT_RECORDS);
		sMatcher.addURI(URI_AUTHORITY, URI_EVENT, ALARM_EVENT_RECORDS);
	}

	private Context					mContext;

	private SQLiteDatabase			mDB						= null;
	@Override
	public boolean onCreate()
	{
		if (mContext == null)
		{
			mContext = getContext();
		}
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder)
	{
		int match = sMatcher.match(uri);
		Cursor cur = null;
		switch (match)
		{
			case ALARM_EVENT_RECORDS :
				if (Build.VERSION.SDK_INT >= 14)
				{
					cur = loadEventAlarm14(projection, selection);
					if (cur == null || cur.getCount() == 0)
					{
						cur = loadEventAlarm(this, projection, selection);
					}
				}
				else
				{
					cur = loadEventAlarm(this, projection, selection);
				}
				break;
			case ALL_EVENT_RECORDS :
				break;
			case EARLY_EVENT_RECORDS :
				long begin;
				long end;
				try
				{
					begin = Long.valueOf(uri.getPathSegments().get(1));
				}
				catch (NumberFormatException nfe)
				{
					throw new IllegalArgumentException("Cannot parse begin "
							+ uri.getPathSegments().get(1));
				}
				try
				{
					end = Long.valueOf(uri.getPathSegments().get(2));
				}
				catch (NumberFormatException nfe)
				{
					throw new IllegalArgumentException("Cannot parse end "
							+ uri.getPathSegments().get(2));
				}
				if (Build.VERSION.SDK_INT >= 14)
				{
					cur = loadAllCalendarEvent14(projection, begin, end);
					if (cur == null || cur.getCount() == 0)
					{
						cur = loadAllCalendarEvent(this, projection, selection, selectionArgs,
								sortOrder, begin, end);
					}
				}
				else
				{
					cur = loadAllCalendarEvent(this, projection, selection, selectionArgs,
							sortOrder, begin, end);
				}
				break;
			default :
				break;
		}
		return cur;
	}

	private MatrixCursor loadEventAlarm14(String[] projection, String selection)
	{
		MatrixCursor mc = new MatrixCursor(projection);
		Cursor calendarCursor = null;
		try
		{
			long eventId = Long.parseLong(selection.split("'")[1].split("'")[0]);
			calendarCursor = CalendarContract.Reminders.query(getContext().getContentResolver(),
					eventId, projection);
			if (calendarCursor != null)
			{
				calendarCursor.moveToFirst();
				int size = calendarCursor.getCount();
				while (size > 0 && !calendarCursor.isAfterLast())
				{
					int length = projection.length;
					Object[] rowObject = new Object[length];
					for (int i = 0; i < length; i++)
					{
						String columnName = projection[i];
						if (columnName.equals(CalendarConstants.EVENT_ID))
						{
							rowObject[i] = calendarCursor.getLong(calendarCursor
									.getColumnIndex(CalendarConstants.EVENT_ID));
						}
						else if (columnName.equals(CalendarConstants.EVENT_MINUTE))
						{
							rowObject[i] = calendarCursor.getString(calendarCursor
									.getColumnIndex(CalendarConstants.EVENT_MINUTE));
						}
					}
					mc.addRow(rowObject);
					calendarCursor.moveToNext();
				}
			}
		}
		catch (SQLiteException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (calendarCursor != null)
				calendarCursor.close();
		}
		return mc;
	}

	private MatrixCursor loadEventAlarm(CalendarProvider calendarProvider, String[] projection,
			String selection)
	{
		MatrixCursor mc = new MatrixCursor(projection);
		Cursor calendarCursor = null;
		try
		{
			Uri uri = CalendarConstants.CALENDER_REMIDER_URL;
			calendarCursor = calendarProvider.getContext().getContentResolver()
					.query(uri, projection, selection, null, null); // order by
			if (calendarCursor != null)
			{
				calendarCursor.moveToFirst();
				int size = calendarCursor.getCount();
				while (size > 0 && !calendarCursor.isAfterLast())
				{
					int length = projection.length;
					Object[] rowObject = new Object[length];
					for (int i = 0; i < length; i++)
					{
						String columnName = projection[i];
						if (columnName.equals(CalendarConstants.EVENT_ID))
						{
							rowObject[i] = calendarCursor.getLong(calendarCursor
									.getColumnIndex(CalendarConstants.EVENT_ID));
						}
						else if (columnName.equals(CalendarConstants.EVENT_MINUTE))
						{
							rowObject[i] = calendarCursor.getString(calendarCursor
									.getColumnIndex(CalendarConstants.EVENT_MINUTE));
						}
					}
					mc.addRow(rowObject);
					calendarCursor.moveToNext();
				}
			}
		}
		catch (SQLiteException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (calendarCursor != null)
				calendarCursor.close();
		}
		return mc;
	}

	private MatrixCursor loadAllCalendarEvent(CalendarProvider calendarProvider,
			String[] projection, String selection, String[] selectionArgs, String sortOrder,
			long begin, long end)
	{
		MatrixCursor mc = new MatrixCursor(projection);
		Cursor calendarCursor = null;
		try
		{
			Uri uri = Uri.parse(CalendarConstants.CALENDER_INSTANCE_URL);
			uri = ContentUris.withAppendedId(uri, begin);
			uri = ContentUris.withAppendedId(uri, end);
			calendarCursor = calendarProvider.getContext().getContentResolver()
					.query(uri, projection, selection, // row filter
							selectionArgs, sortOrder); // order by
			if (calendarCursor != null)
			{
				calendarCursor.moveToFirst();
				int size = calendarCursor.getCount();
				while (size > 0 && !calendarCursor.isAfterLast())
				{
					int length = projection.length;
					Object[] rowObject = new Object[length];
					for (int i = 0; i < length; i++)
					{
						String columnName = projection[i];
						if (columnName.equals(CalendarConstants.ID))
						{
							rowObject[i] = calendarCursor.getLong(calendarCursor
									.getColumnIndex(CalendarConstants.ID));
						}
						else if (columnName.equals(CalendarConstants.TITLE))
						{
							rowObject[i] = calendarCursor.getString(calendarCursor
									.getColumnIndex(CalendarConstants.TITLE));
						}
						else if (columnName.equals(CalendarConstants.TIME_START))
						{
							rowObject[i] = calendarCursor.getLong(calendarCursor
									.getColumnIndex(CalendarConstants.TIME_START));
						}
						else if (columnName.equals(CalendarConstants.TIME_END))
						{
							rowObject[i] = calendarCursor.getLong(calendarCursor
									.getColumnIndex(CalendarConstants.TIME_END));
						}
						else if (columnName.equals(CalendarConstants22.LOCATION))
						{
							rowObject[i] = calendarCursor.getString(calendarCursor
									.getColumnIndex(CalendarConstants22.LOCATION));
						}
						else if (columnName.equals(CalendarConstants.COLOR))
						{
							rowObject[i] = calendarCursor.getInt(calendarCursor
									.getColumnIndex(CalendarConstants.COLOR));
						}
						else if (columnName.equals(CalendarConstants.ALLDAY))
						{
							rowObject[i] = calendarCursor.getInt(calendarCursor
									.getColumnIndex(CalendarConstants.ALLDAY));
						}
						else if (columnName.equals(CalendarConstants.EVENT_ID))
						{
							rowObject[i] = calendarCursor.getLong(calendarCursor
									.getColumnIndex(CalendarConstants.EVENT_ID));
						}
						else if (columnName.equals(CalendarConstants.HASALARM))
						{
							rowObject[i] = calendarCursor.getLong(calendarCursor
									.getColumnIndex(CalendarConstants.HASALARM));
						}
					}
					mc.addRow(rowObject);
					calendarCursor.moveToNext();
				}
			}
		}
		catch (SQLiteException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (calendarCursor != null)
				calendarCursor.close();
		}
		return mc;
	}

	private MatrixCursor loadAllCalendarEvent14(String[] projection, long begin, long end)
	{
		MatrixCursor mc = new MatrixCursor(projection);
		Cursor calendarCursor = null;
		try
		{
			calendarCursor = CalendarContract.Instances.query(getContext().getContentResolver(),
					projection, begin, end);
			if (calendarCursor != null)
			{
				calendarCursor.moveToFirst();
				int size = calendarCursor.getCount();
				while (size > 0 && !calendarCursor.isAfterLast())
				{
					int length = projection.length;
					Object[] rowObject = new Object[length];
					for (int i = 0; i < length; i++)
					{
						String columnName = projection[i];
						if (columnName.equals(CalendarConstants.ID))
						{
							rowObject[i] = calendarCursor.getLong(calendarCursor
									.getColumnIndex(CalendarConstants.ID));
						}
						else if (columnName.equals(CalendarConstants.TITLE))
						{
							rowObject[i] = calendarCursor.getString(calendarCursor
									.getColumnIndex(CalendarConstants.TITLE));
						}
						else if (columnName.equals(CalendarConstants.TIME_START))
						{
							rowObject[i] = calendarCursor.getLong(calendarCursor
									.getColumnIndex(CalendarConstants.TIME_START));
						}
						else if (columnName.equals(CalendarConstants.TIME_END))
						{
							rowObject[i] = calendarCursor.getLong(calendarCursor
									.getColumnIndex(CalendarConstants.TIME_END));
						}
						else if (columnName.equals(CalendarConstants22.LOCATION))
						{
							rowObject[i] = calendarCursor.getString(calendarCursor
									.getColumnIndex(CalendarConstants22.LOCATION));
						}
						else if (columnName.equals(CalendarConstants.COLOR_FOR_14))
						{
							rowObject[i] = calendarCursor.getInt(calendarCursor
									.getColumnIndex(CalendarConstants.COLOR_FOR_14));
						}
						else if (columnName.equals(CalendarConstants.ALLDAY))
						{
							rowObject[i] = calendarCursor.getInt(calendarCursor
									.getColumnIndex(CalendarConstants.ALLDAY));
						}
						else if (columnName.equals(CalendarConstants.EVENT_ID))
						{
							rowObject[i] = calendarCursor.getLong(calendarCursor
									.getColumnIndex(CalendarConstants.EVENT_ID));
						}
						else if (columnName.equals(CalendarConstants.HASALARM))
						{
							rowObject[i] = calendarCursor.getLong(calendarCursor
									.getColumnIndex(CalendarConstants.HASALARM));
						}
					}
					mc.addRow(rowObject);
					calendarCursor.moveToNext();
				}
			}
		}
		catch (SQLiteException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (calendarCursor != null)
				calendarCursor.close();
		}
		return mc;
	}

	@Override
	public String getType(Uri uri)
	{
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		int result = 0;
		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		return 0;
	}
}
