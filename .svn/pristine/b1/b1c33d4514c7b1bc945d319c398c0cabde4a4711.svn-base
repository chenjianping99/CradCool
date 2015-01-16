package com.jiubang.goscreenlock.theme.cjpcardcool.weather.util;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;

/**
 * 数据库帮助类
 * @author lailele
 *
 */
public class DataBaseHelper extends SQLiteOpenHelper
{
	private static final String	DATABASE_NAME			= Constant.THEME_PACKAGE_SHORT_NAME + ".db";
	// 天气信息表
	public static final String	TABLE_WEATHER			= "tb_weather_info";
	public static final String	COL_ID					= "_id";
	public static final String	COL_CITYID				= "city_id";
	public static final String	COL_CITYNAME			= "city_name";
	public static final String	COL_WEATHERTYPE			= "weather_type";
	public static final String	COL_WEATHERCURRT		= "weather_curr_t";
	public static final String	COL_WEATHERHIGHT		= "weather_high_t";
	public static final String	COL_WEATHERLOWT			= "weather_low_t";
	public static final String	COL_WEATHERBUILDTIME	= "weather_build_time";
	// 天气预报（7天）
	public static final String	COL_WEATHERPREVIEW		= "weather_preview";
	
	// 数据库版本号码
	private static final int	DB_VERSION				= 1;

	public DataBaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String strSql = "CREATE TABLE IF NOT EXISTS " + TABLE_WEATHER + "(" + COL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_CITYID + " TEXT," + COL_CITYNAME
				+ " TEXT," + COL_WEATHERTYPE + " INTEGER," + COL_WEATHERCURRT + " FLOAT,"
				+ COL_WEATHERHIGHT + " FLOAT," + COL_WEATHERLOWT + " FLOAT,"
				+ COL_WEATHERBUILDTIME + " LONG," +  COL_WEATHERPREVIEW + " BLOB" + ");";

		try
		{
			db.execSQL(strSql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
		onCreate(db);
	}

}
