package com.jiubang.goscreenlock.theme.cjpcardcool.weather.util;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;

/**
 * 
 * <br>类描述:存取设置项对于天气的设置
 * <br>功能详细描述:
 * 
 * @author  jiezhang
 * @date  [2012-10-23]
 */
public class MyProvider extends ContentProvider
{
	// 本组件URI
	public static final Uri		CONTENT_URI	= Uri.parse("content://" + Constant.WEATHER_MY_PROVIDER_AUTHORITY);
	// 实际保存xml文件名字
	private final String		mFILENAME	= Constant.THEME_PACKAGE_NAME + ".themedate";

	// 设置项列
	public static final String	COLUMNS[]	= { "f_tscale", "f_location", "f_location_lasttime",
			"f_cityId", "f_cityName", "f_countryName", "f_stateName", "f_timeZone", "f_isManToAuto" };

	@Override
	public boolean onCreate()
	{
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder)
	{
		SharedPreferences sharedPreferences = getContext().getApplicationContext()
				.getSharedPreferences(mFILENAME, Context.MODE_PRIVATE);
		return combinCursor(new String[] { sharedPreferences.getString(COLUMNS[0] + "_key", "0"),
				sharedPreferences.getString(COLUMNS[1] + "_key", "1"),
				sharedPreferences.getString(COLUMNS[2] + "_key", "0"),
				sharedPreferences.getString(COLUMNS[3] + "_key", null),
				sharedPreferences.getString(COLUMNS[4] + "_key", null),
				sharedPreferences.getString(COLUMNS[5] + "_key", null),
				sharedPreferences.getString(COLUMNS[6] + "_key", null),
				sharedPreferences.getString(COLUMNS[7] + "_key", null),
				sharedPreferences.getString(COLUMNS[8] + "_key", "0") });
	}
	/**
	 * 组装cursor
	 * 
	 * @param cursor
	 * @return
	 */
	private MatrixCursor combinCursor(Object[] themeName)
	{
		MatrixCursor ret = null;
		ret = new MatrixCursor(COLUMNS);
		ret.addRow(themeName);
		return ret;
	}

	@Override
	public String getType(Uri uri)
	{
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		SharedPreferences sharedPreferences = getContext().getApplicationContext()
				.getSharedPreferences(mFILENAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		for (String key : COLUMNS)
		{
			if (values.get(key) != null)
			{
				editor.putString(key + "_key", (String) values.get(key));
			}
		}
		editor.commit();
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
