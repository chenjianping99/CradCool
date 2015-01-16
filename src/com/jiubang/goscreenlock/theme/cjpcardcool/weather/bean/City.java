package com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.CommonConstants;

/**
 * 
 * 类描述: 城市信息
 * 
 * @author liuwenqin
 * @date [2012-9-4]
 */
public class City
{
	// ================ 基本请求信息单元 ================ 
	/**
	 * 城市名
	 */
	public volatile String				mCityName;
	/**
	 * 城市id
	 */
	public volatile String				mCityId;
	/**
	 * 上次天气信息更新的服务器时间戳
	 */
	public volatile long				mLastUpdateTimestamp	= 0;

	// ================ 保存下发数据单元 ================ 
	/**
	 * 本次天气更新的服务器时间戳
	 */
	public volatile long				mCurUpdateTimestamp		= 0;
	/**
	 * 本次天气信息更新时间(转化为0时区的毫秒值)
	 */
	public volatile long				mCurUpdateTimeMs;
	/**
	 * 现在天气信息
	 */
	public volatile NowTimeInfo			mNow;
	/**
	 * 未来七天天气预报列表
	 */
	public volatile List<ForecastInfo>	mForecastList;
	/**
	 * 未来24小时天气预报
	 */
	public volatile List<HourInfo>		mHourList;
	/**
	 * 预警天气信息
	 */
	public volatile List<AlarmInfo>		mAlarmList;
	/**
	 * 极端天气预警信息
	 */
	public volatile List<ExtremeInfo>	mExtremeList;
	/**
	 * 州名
	 */
	private String						mState;
	/**
	 * 国家名
	 */
	private String						mCountry;
	/**
	 * 时区偏差值
	 */
	public volatile int					mTimezoneOffset			= CommonConstants.UNKNOWN_VALUE_INT;

	/**
	 * @param name
	 *            城市名
	 * @param id
	 *            城市新ID
	 * @param timestamp
	 *            上次更新天气的时间戳
	 * @param context
	 */
	public City(String name, String id, long timestamp)
	{
		init(name, id, timestamp);
	}

	private void init(String name, String id, long timestamp)
	{
		this.mCityName = name;
		this.mCityId = id;
		this.mLastUpdateTimestamp = timestamp;
		this.mCurUpdateTimeMs = CommonConstants.UNKNOWN_VALUE_LONG;
		this.mCurUpdateTimestamp = CommonConstants.UNKNOWN_VALUE_LONG;
		this.mNow = new NowTimeInfo();
		this.mForecastList = new ArrayList<ForecastInfo>();
		this.mHourList = new ArrayList<HourInfo>();
		this.mAlarmList = new ArrayList<AlarmInfo>();
		this.mExtremeList = new ArrayList<ExtremeInfo>();
	}

	public String getState()
	{
		return mState;
	}

	public void setState(String state)
	{
		this.mState = state;
	}

	public String getCountry()
	{
		return mCountry;
	}

	public void setCountry(String country)
	{
		this.mCountry = country;
	}

	public String getName()
	{
		return mCityName;
	}

	public void setName(String name)
	{
		this.mCityName = name;
	}

	public String getCode()
	{
		return mCityId;
	}

	public void setCode(String id)
	{
		this.mCityId = id;
	}

	public void setUpdateTime(String updateTime)
	{
		if (updateTime == null || updateTime.equals(CommonConstants.UNKNOWN_VALUE_STRING))
		{
			mCurUpdateTimeMs = CommonConstants.UNKNOWN_VALUE_LONG;
		}
		else
		{
			mCurUpdateTimeMs = getZeroTimezoneUpdateTime(updateTime);
		}
	}

	public void setUpdateTime(long updateTime)
	{
		mCurUpdateTimeMs = updateTime;
	}

	public long getUpdateTimeMs()
	{
		return mCurUpdateTimeMs;
	}

	public void setUpdateTimestamp(long updateTimestamp)
	{
		this.mCurUpdateTimestamp = updateTimestamp;
	}

	public long getUpdateTimestamp()
	{
		return mCurUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(long lastUpdateTimestamp)
	{
		this.mLastUpdateTimestamp = lastUpdateTimestamp;
	}

	public long getLastUpdateTimestamp()
	{
		return mLastUpdateTimestamp;
	}

	public NowTimeInfo getNow()
	{
		return mNow;
	}

	public ForecastInfo getForecastInfo(int index)
	{
		if (mForecastList == null || index < 0 || index >= mForecastList.size())
		{
			return null;
		}
		return mForecastList.get(index);
	}

	public int getForecastInfoCount()
	{
		return mForecastList == null ? 0 : mForecastList.size();
	}

	public HourInfo getHourInfo(int index)
	{
		if (mHourList == null || index < 0 || index >= mHourList.size())
		{
			return null;
		}
		return mHourList.get(index);
	}

	public int getHourInfoCount()
	{
		return mHourList == null ? 0 : mHourList.size();
	}

	public AlarmInfo getAlarmInfo(int index)
	{
		if (mAlarmList == null || index < 0 || index >= mAlarmList.size())
		{
			return null;
		}
		return mAlarmList.get(index);
	}

	public int getAlarmInfoCount()
	{
		return mAlarmList == null ? 0 : mAlarmList.size();
	}

	public ExtremeInfo getExtremeInfo(int index)
	{
		if (mExtremeList == null || index < 0 || index >= mExtremeList.size())
		{
			return null;
		}
		return mExtremeList.get(index);
	}

	public int getExtremeInfoCount()
	{
		return mExtremeList == null ? 0 : mExtremeList.size();
	}

	public int getTimeOffset()
	{
		return mTimezoneOffset;
	}

	public void setTimeOffset(int timeOffset)
	{
		this.mTimezoneOffset = timeOffset;
	}

	/** 清除之前解析一半获得天气信息 */
	public void clear()
	{
		mForecastList.clear();
		mHourList.clear();
		mAlarmList.clear();
		mExtremeList.clear();
	}

	/** 将天气更新时间修正到零时区 */
	public static long getZeroTimezoneUpdateTime(String updateTime)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm Z");
		long realUpdateTime = CommonConstants.UNKNOWN_VALUE_LONG;
		try
		{
			Date updateDate = formatter.parse(updateTime);
			// 修正时间到当天0点
			Calendar calendar = Calendar.getInstance();
			int offsetZone = calendar.get(Calendar.ZONE_OFFSET);
			int oddsetDst = calendar.get(Calendar.DST_OFFSET);
			realUpdateTime = updateDate.getTime() - (offsetZone + oddsetDst);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return realUpdateTime;
	}
}