package com.jiubang.goscreenlock.theme.cjpcardcool.weather.search;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.CityBean;

/** 中文下，搜索城市 */
public class SearchCityInChinese
{

	/** 中国*/
	public static final String	CHINA	= "中国";

	/**
	 * 功能简述: 中文下，搜索与关键字匹配的城市
	 * 功能详细描述:
	 * 注意:
	 * @param context
	 * @param keyword 输入关键字。关键字不能为空，长度必须大于0
	 * @param go_city 本地城市数据库资源ID
	 * @param lstSearch 结果数组
	 * @return
	 */
	public static List<CityBean> searchCityInChinese(Context context, String keyword, int go_city,
			List<CityBean> lstSearch)
	{
		if (lstSearch == null)
		{
			lstSearch = new ArrayList<CityBean>();
		}
		else
		{
			lstSearch.clear();
		}
		String sql = "select * from city where name like ? or pinyin like ? or parent like ? or root like ?";
		Cursor cursor = null;
		SQLiteDatabase database = null;
		try
		{
			database = openLocalCityDatabase(context, go_city);
			if (database != null)
			{
				cursor = database.rawQuery(sql, new String[] { keyword + "%", keyword + "%",
						keyword + "%", keyword + "%" });
				if (cursor != null)
				{
					while (cursor.moveToNext())
					{
						// 这里实际上是区／市／省三级
						String cityName = cursor.getString(cursor.getColumnIndex("name"));
						String cityId = cursor.getString(cursor.getColumnIndex("posID"));
						//						// Ex版本（2.0)开始，在本地缓存城市中新增新城市ID，兼容旧版本
						//						String oldCode = cursor.getString(cursor
						//								.getColumnIndex("oldPosID"));
						String stateName = cursor.getString(cursor.getColumnIndex("parent"));
						String countryName = cursor.getString(cursor.getColumnIndex("root"));
						String timeZone = cursor.getString(cursor.getColumnIndex("timeZone"));
						String id = cursor.getString(cursor.getColumnIndex("_id"));
						int idInt = Integer.parseInt(id);
						final int domesticOrLocal = 5000;
						if (idInt <= domesticOrLocal)
						{
							stateName = countryName;
							countryName = CHINA;
						}

						lstSearch.add(new CityBean(cityId, cityName, stateName, countryName,
								timeZone, cityName + ", " + stateName));
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
			if (database != null && database.isOpen())
			{
				database.close();
			}
		}
		return lstSearch;
	}

	/**
	 * 功能简述:点击中文热门城市，查找该热门城市的下一级地址列表
	 * 功能详细描述:
	 * 注意:
	 * @param context 
	 * @param label 城市名
	 * @param go_city 本地城市数据库资源ID
	 * @param lstSearch 结果数组
	 * @return
	 */
	public static List<CityBean> searchCityInNextLevel(Context context, String label, int go_city,
			List<CityBean> lstSearch)
	{
		if (lstSearch == null)
		{
			lstSearch = new ArrayList<CityBean>();
		}
		else
		{
			lstSearch.clear();
		}
		String sql = "select * from city where parent like ?";
		Cursor cursor = null;
		SQLiteDatabase database = null;
		try
		{
			database = openLocalCityDatabase(context, go_city);
			if (database != null)
			{
				cursor = database.rawQuery(sql, new String[] { label + "%" });
				// 如果查找单词，显示其中文信息
				if (cursor != null)
				{
					while (cursor.moveToNext())
					{
						String cityName = cursor.getString(cursor.getColumnIndex("name"));
						String cityId = cursor.getString(cursor.getColumnIndex("posID"));
						String stateName = cursor.getString(cursor.getColumnIndex("parent"));
						String countryName = cursor.getString(cursor.getColumnIndex("root"));
						String timeZone = cursor.getString(cursor.getColumnIndex("timeZone"));
						String id = cursor.getString(cursor.getColumnIndex("_id"));
						int idInt = Integer.parseInt(id);
						final int domesticOrLocal = 5000;
						if (idInt <= domesticOrLocal)
						{
							stateName = countryName;
							countryName = CHINA;
						}
						lstSearch.add(new CityBean(cityId, cityName, stateName, countryName,
								timeZone, cityName + ", " + stateName));
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
			if (database != null && database.isOpen())
			{
				database.close();
			}
		}
		return lstSearch;
	}

	/**
	 * 打开本地城市数据库
	 * @param context
	 * @param go_city
	 * @return
	 */
	public static SQLiteDatabase openLocalCityDatabase(Context context, int go_city)
	{
		// 获得dictionary.db文件的绝对路径
		String dataBasePath = context.getFilesDir() + "/city";
		String databaseFilename = dataBasePath + "/" + "go_city.db";
		File dir = new File(dataBasePath);
		// 创建这个目录
		if (!dir.exists())
		{
			dir.mkdir();
		}
		SQLiteDatabase database = null;
		try
		{
			if (!(new File(databaseFilename)).exists())
			{
				// 获得封装go_city.db文件的InputStream对象
				InputStream is = context.getResources().openRawResource(go_city);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				final int bufferLength = 8192;
				byte[] buffer = new byte[bufferLength];
				int count = 0;
				// 开始复制dictionary.db文件
				while ((count = is.read(buffer)) > 0)
				{
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			// 打开/sdcard/dictionary目录中的go_city.db文件
			database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return database;
	}
}
