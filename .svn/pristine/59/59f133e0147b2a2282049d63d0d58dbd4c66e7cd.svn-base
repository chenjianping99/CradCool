package com.jiubang.goscreenlock.theme.cjpcardcool.weather.util;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;

/**
 * 存取
 * @author zhangjie
 *
 */
public class DatabaseContentProvider extends ContentProvider
{
	public static final String		SCHEMEN					= "content://";
	public static final String		AUTHORITY				= Constant.WEATHER_AUTHORITY;
	private static final String		TABLE_WEATHERINFO_AUTHORITY	= SCHEMEN + AUTHORITY + "/"
																	+ DataBaseHelper.TABLE_WEATHER;
	private static final int		CODE_QUERY_INFO		= 100;
	private static final int		CODE_INSERT_INFO	= 101;
	private static final int		CODE_DELETE_INFO	= 102;

	private static final UriMatcher	SURIMATCHER				= new UriMatcher(UriMatcher.NO_MATCH);
	static
	{
		SURIMATCHER.addURI(AUTHORITY, DataBaseHelper.TABLE_WEATHER, CODE_QUERY_INFO); // 查询全部记录
		SURIMATCHER.addURI(AUTHORITY, DataBaseHelper.TABLE_WEATHER + "/0", CODE_INSERT_INFO); // 插入数据
		SURIMATCHER.addURI(AUTHORITY, DataBaseHelper.TABLE_WEATHER + "/1", CODE_DELETE_INFO); // 删除单条记录
	}

	public static final Uri			URI_QUERY_INFO		= Uri.parse(TABLE_WEATHERINFO_AUTHORITY);
	public static final Uri			URI_INSERT_INFO		= Uri.parse(TABLE_WEATHERINFO_AUTHORITY
																	+ "/0");
	public static final Uri			URI_DELETE_INFO		= Uri.parse(TABLE_WEATHERINFO_AUTHORITY
																	+ "/1");

	private DataBaseHelper			mDbHelper				= null;
	private SQLiteDatabase			mDB						= null;

	@Override
	public boolean onCreate()
	{
		mDbHelper = new DataBaseHelper(getContext());
		try
		{
			mDB = mDbHelper.getReadableDatabase();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder)
	{
		Cursor cursor = null;
		switch (SURIMATCHER.match(uri))
		{
			case CODE_QUERY_INFO :
				if (mDB == null)
				{
					return null;
				}
				cursor = mDB.query(DataBaseHelper.TABLE_WEATHER, projection, selection, selectionArgs,
						null, null, sortOrder);
				return cursor;
		}
		return null;
	}

	@Override
	public String getType(Uri uri)
	{
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		if (uri == null || values == null)
		{
			return null;
		}
		if (SURIMATCHER.match(uri) == CODE_INSERT_INFO)
		{
			if (mDB == null)
			{
				return null;
			}
			mDB.insert(DataBaseHelper.TABLE_WEATHER, null, values);
		}
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		int result = 0;
		if (SURIMATCHER.match(uri) == CODE_DELETE_INFO)
		{
			if (mDB == null)
			{
				return result;
			}
			result = mDB.delete(DataBaseHelper.TABLE_WEATHER, selection, selectionArgs);
		}
		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		int result = 0;
		return result;
	}

}
