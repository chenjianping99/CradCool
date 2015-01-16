package com.jiubang.goscreenlock.theme.cjpcardcool.weather.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

/**
 * 
 * @author liangdaijian
 *
 */
public class WeatherDataBean implements Serializable
{
	/**
	 * 注释内容
	 */
	private static final long	serialVersionUID	= 1357L;
	
	private int		mId;
	private String	mCityId;
	private String	mCityName;
	private int		mWeatherType;
	private float	mWeatherCurrT;
	private float	mWeatherHighT;
	private float	mWeatherLowT;
	private long	mBuildTime;
	
	private ArrayList<WeatherDataBean> mWeatherPreList;
	
	private String mWeatherWeekDate;
	
	public void setWeekDate(String date)
	{
		mWeatherWeekDate = date;
	}
	public String getWeekDate()
	{
		return mWeatherWeekDate;
	}
	
	public void setWeaterThemePreList(ArrayList<WeatherDataBean> list)
	{
		mWeatherPreList = list;
	}
	// 天气预报
	public ArrayList<WeatherDataBean> getWeaterThemePreList()
	{
		return mWeatherPreList;
	}
	
	/**
	 * 反序列化
	 * 
	 * @param pre
	 */
	public void setWeatherPreByByteArray(byte pre[])
	{
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
//				Log.i("ldj",list2.toString());
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
//			for (int i = 0; i < list2.size(); i++)
//			{
//				Log.d("weater_pre", list2.get(i).getWeekDate());
//			}
		}
//		cursor.getBlob((cursor.getColumnIndex(DataBaseHelper.COL_WEATHERPREVIEW));
		if (null != list2)
		{
			setWeaterThemePreList(list2);
		}
	}
	
	/**
	 * 序列化天气预告
	 */
	public byte[] getWeatherPreByteArray()
	{
		ArrayList<WeatherDataBean> list = mWeatherPreList;
		if (null == list)
		{
			return null;
		}
		
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
		return weaterpre;
	}
	
	
	public long getmBuildTime()
	{
		return mBuildTime;
	}
	public void setmBuildTime(long mBuildTime)
	{
		this.mBuildTime = mBuildTime;
	}
	public int getmId()
	{
		return mId;
	}
	public void setmId(int mId)
	{
		this.mId = mId;
	}
	public String getmCityId()
	{
		return mCityId;
	}
	public void setmCityId(String mCityId)
	{
		this.mCityId = mCityId;
	}
	public String getmCityName()
	{
		return mCityName;
	}
	public void setmCityName(String mCityName)
	{
		this.mCityName = mCityName;
	}
	public int getmWeatherType()
	{
		return mWeatherType;
	}
	public void setmWeatherType(int mWeatherType)
	{
		this.mWeatherType = mWeatherType;
	}
	public float getmWeatherCurrT()
	{
		return mWeatherCurrT;
	}
	public void setmWeatherCurrT(float mWeatherCurrT)
	{
		this.mWeatherCurrT = mWeatherCurrT;
	}
	public float getmWeatherHighT()
	{
		return mWeatherHighT;
	}
	public void setmWeatherHighT(float mWeatherHighT)
	{
		this.mWeatherHighT = mWeatherHighT;
	}
	public float getmWeatherLowT()
	{
		return mWeatherLowT;
	}
	public void setmWeatherLowT(float mWeatherLowT)
	{
		this.mWeatherLowT = mWeatherLowT;
	}
	@Override
	public String toString()
	{
		return "WeatherDataBean [mCityId=" + mCityId + ", mCityName=" + mCityName
				+ ", mWeatherType=" + mWeatherType + ", mWeatherCurrT=" + mWeatherCurrT
				+ ", mWeatherHighT=" + mWeatherHighT + ", mWeatherLowT=" + mWeatherLowT
				+ ", mBuildTime=" + mBuildTime + "]";
	}
	
}
