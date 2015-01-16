package com.jiubang.goscreenlock.theme.cjpcardcool.weather.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.CityBean;

/**
 * 
 * @author liangdaijian
 *
 */
public class WeatherSettingUtil
{
	public static int getTemperateScale(Context aContext)
	{
		Cursor cursor = aContext.getContentResolver().query(MyProvider.CONTENT_URI, null, null,
				null, null);
		if (null != cursor)
		{
			try
			{
				cursor.moveToFirst();
				return Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(MyProvider.COLUMNS[0])));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				cursor.close();
				cursor = null;
			}
		}
		return 0;
	}
	public static void setTemperateScale(Context aContext, int scale)
	{
		ContentValues content = new ContentValues();
		content.put(MyProvider.COLUMNS[0], "" + scale);
		aContext.getContentResolver().insert(MyProvider.CONTENT_URI, content);
	}
	public static int getLocationWay(Context aContext)
	{
		Cursor cursor = aContext.getContentResolver().query(MyProvider.CONTENT_URI, null, null,
				null, null);
		if (null != cursor)
		{
			try
			{
				if (cursor.moveToFirst())
				{
					return Integer.parseInt(cursor.getString(cursor
							.getColumnIndex(MyProvider.COLUMNS[1])));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				cursor.close();
				cursor = null;
			}
		}
		return 1;
	}
	public static void setLocationWay(Context aContext, int way)
	{
		ContentValues content = new ContentValues();
		content.put(MyProvider.COLUMNS[1], "" + way);
		aContext.getContentResolver().insert(MyProvider.CONTENT_URI, content);
	}

	public static long getLocationLastTime(Context aContext)
	{
		Cursor cursor = aContext.getContentResolver().query(MyProvider.CONTENT_URI, null, null,
				null, null);
		if (null != cursor)
		{
			try
			{
				cursor.moveToFirst();
				return Long
						.parseLong(cursor.getString(cursor.getColumnIndex(MyProvider.COLUMNS[2])));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				cursor.close();
				cursor = null;
			}
		}
		return 1;
	}
	public static void setLocationLastTime(Context aContext, long time)
	{
		ContentValues content = new ContentValues();
		content.put(MyProvider.COLUMNS[2], "" + time);
		aContext.getContentResolver().insert(MyProvider.CONTENT_URI, content);
	}

	public static CityBean getCity(Context aContext)
	{
		Cursor cursor = aContext.getContentResolver().query(MyProvider.CONTENT_URI, null, null,
				null, null);
		CityBean city = new CityBean();
		if (null != cursor)
		{
			try
			{
				cursor.moveToFirst();
				city.setCityId(cursor.getString(cursor.getColumnIndex(MyProvider.COLUMNS[3])));
				city.setCityName(cursor.getString(cursor.getColumnIndex(MyProvider.COLUMNS[4])));
				city.setCountryName(cursor.getString(cursor.getColumnIndex(MyProvider.COLUMNS[5])));
				city.setStateName(cursor.getString(cursor.getColumnIndex(MyProvider.COLUMNS[6])));
				city.setTimeZone(cursor.getString(cursor.getColumnIndex(MyProvider.COLUMNS[7])));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				cursor.close();
				cursor = null;
			}
		}
		if (city.getCityId() == null || city.getCityId().trim().length() <= 0)
		{
			city = null;
		}
		return city;
	}
	public static void setCity(Context aContext, CityBean city)
	{
		ContentValues content = new ContentValues();
		content.put(MyProvider.COLUMNS[3], city.getCityId());
		content.put(MyProvider.COLUMNS[4], city.getCityName());
		content.put(MyProvider.COLUMNS[5], city.getCountryName());
		content.put(MyProvider.COLUMNS[6], city.getStateName());
		content.put(MyProvider.COLUMNS[7], city.getTimeZone());
		aContext.getContentResolver().insert(MyProvider.CONTENT_URI, content);
	}

	public static void setIsManToAuto(Context aContext, int scale)
	{
		ContentValues content = new ContentValues();
		content.put(MyProvider.COLUMNS[8], "" + scale);
		aContext.getContentResolver().insert(MyProvider.CONTENT_URI, content);
	}
	public static int getIsManToAuto(Context aContext)
	{
		Cursor cursor = aContext.getContentResolver().query(MyProvider.CONTENT_URI, null, null,
				null, null);
		if (null != cursor)
		{
			try
			{
				if (cursor.moveToFirst())
				{
					return Integer.parseInt(cursor.getString(cursor
							.getColumnIndex(MyProvider.COLUMNS[8])));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				cursor.close();
				cursor = null;
			}
		}
		return 1;
	}

	public static void resetCity(Context aContext)
	{
		ContentValues content = new ContentValues();
		content.put(MyProvider.COLUMNS[3], "");
		content.put(MyProvider.COLUMNS[4], "");
		aContext.getContentResolver().insert(MyProvider.CONTENT_URI, content);
	}
}
