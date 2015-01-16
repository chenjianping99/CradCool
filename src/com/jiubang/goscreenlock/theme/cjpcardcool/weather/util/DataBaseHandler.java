package com.jiubang.goscreenlock.theme.cjpcardcool.weather.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * 数据库操作业务类
 * @author liangdaijian
 *
 */
public class DataBaseHandler
{
	private Context	mContext	= null;

	public DataBaseHandler(Context context)
	{
		mContext = context;
	}

	private int delete()
	{
		int result;
		ContentResolver resolver = mContext.getContentResolver();
		result = resolver.delete(DatabaseContentProvider.URI_DELETE_INFO, null, null);
		return result;
	}

	public boolean insert(WeatherDataBean info)
	{
		if (info == null)
		{
			return false;
		}
		// 要插入数据，先删除
		delete();

		ArrayList<WeatherDataBean> list = info.getWeaterThemePreList();
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream os = null;
		byte[] weaterpre = null;
		try
		{

			os = new ObjectOutputStream(bo);
			os.writeObject(list);
			os.flush();
			weaterpre = bo.toByteArray();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (null != os)
			{
				try
				{
					os.close();
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != bo)
			{
				try
				{
					bo.close();
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Log.d("ContentValues", "inser db");
		ContentValues cv = new ContentValues();
		cv.put(DataBaseHelper.COL_CITYID, info.getmCityId());
		cv.put(DataBaseHelper.COL_CITYNAME, info.getmCityName());
		cv.put(DataBaseHelper.COL_WEATHERTYPE, info.getmWeatherType());
		cv.put(DataBaseHelper.COL_WEATHERCURRT, info.getmWeatherCurrT());
		cv.put(DataBaseHelper.COL_WEATHERHIGHT, info.getmWeatherHighT());
		cv.put(DataBaseHelper.COL_WEATHERLOWT, info.getmWeatherLowT());
		cv.put(DataBaseHelper.COL_WEATHERBUILDTIME, info.getmBuildTime());
		cv.put(DataBaseHelper.COL_WEATHERPREVIEW, weaterpre);

		//		ArrayList<String> list = new ArrayList<String>();
		//		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		//		byte[] aaaa = null;
		//		try
		//		{
		//			ObjectOutputStream os = new ObjectOutputStream(bo);
		//			os.writeObject(list);
		//			os.flush();
		//			aaaa = bo.toByteArray();
		//			os.close();
		//			bo.close();
		//		}
		//		catch (IOException e)
		//		{
		//			e.printStackTrace();
		//		}
		//		

		mContext.getContentResolver().insert(DatabaseContentProvider.URI_INSERT_INFO, cv);
		return true;
	}

	/**
	 * 查询数据表中所有的数据
	 * @return
	 */
	public WeatherDataBean query()
	{
		WeatherDataBean info = null;
		ContentResolver resolver = mContext.getContentResolver();
		Cursor cursor = resolver.query(DatabaseContentProvider.URI_QUERY_INFO, null, null, null,
				null);
		if (cursor != null)
		{
			if (cursor.moveToNext())
			{
				info = new WeatherDataBean();
				info.setmCityId(cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL_CITYID)));
				info.setmCityName(cursor.getString(cursor
						.getColumnIndex(DataBaseHelper.COL_CITYNAME)));
				info.setmWeatherType(cursor.getInt(cursor
						.getColumnIndex(DataBaseHelper.COL_WEATHERTYPE)));
				info.setmWeatherCurrT(cursor.getFloat(cursor
						.getColumnIndex(DataBaseHelper.COL_WEATHERCURRT)));
				info.setmWeatherHighT(cursor.getFloat(cursor
						.getColumnIndex(DataBaseHelper.COL_WEATHERHIGHT)));
				info.setmWeatherLowT(cursor.getFloat(cursor
						.getColumnIndex(DataBaseHelper.COL_WEATHERLOWT)));
				info.setmBuildTime(cursor.getLong(cursor
						.getColumnIndex(DataBaseHelper.COL_WEATHERBUILDTIME)));
				byte pre[] = cursor.getBlob(cursor
						.getColumnIndex(DataBaseHelper.COL_WEATHERPREVIEW));

				ArrayList<WeatherDataBean> list2 = null;
				if (null != pre)
				{

					InputStream is = new ByteArrayInputStream(pre);
					ObjectInputStream oi = null;
					try
					{
						oi = new ObjectInputStream(is);
						list2 = (ArrayList<WeatherDataBean>) oi.readObject();
						is.close();
						oi.close();
						//						Log.i("ldj",list2.toString());
					}
					catch (StreamCorruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (ClassNotFoundException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally
					{
						try
						{
							if (null != is)
							{
								is.close();
							}
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try
						{
							if (null != oi)
							{
								oi.close();
							}
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					for (int i = 0; i < list2.size(); i++)
					{
						Log.d("weater_pre", list2.get(i).getWeekDate());
					}
				}
				//				cursor.getBlob((cursor.getColumnIndex(DataBaseHelper.COL_WEATHERPREVIEW));
				info.setWeaterThemePreList(list2);
			}
			cursor.close();
		}
		return info;
	}
}
