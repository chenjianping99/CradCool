package com.jiubang.goscreenlock.theme.cjpcardcool.weather.tianqi;

import java.io.InputStream;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.SystemClock;
import android.text.TextUtils;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.City;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.ExtremeInfo;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.ForecastInfo;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.HourInfo;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.NowTimeInfo;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.ZhiShu;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.CommonConstants;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.HttpRequestStatus;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.HttpUtil;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.Result;

/**
 * 
 * 类描述: 解析天气信息json类
 * 
 * @author  liuwenqin
 * @date  [2012-9-4]
 */
public class RemoteInfoParser
{

	private City	mCityInfo;

	public RemoteInfoParser(City city)
	{
		mCityInfo = city;
	}

	/**
	 * <br>功能简述: 解析新协议服务器下发的json文件
	 * <br>服务器返回码：
	 * <li>-1，服务器处理天气信息异常；
	 * <li>0，服务器找不到该城市信息，不需要再尝试刷新；
	 * <li>1，查询成功；
	 * <li>2，没有新数据下发；
	 * @param is 输入流
	 * @param result 用来保存返回状态码
	 */
	public void parseJSON(InputStream is, Result result)
	{
		String jsonResult = HttpUtil.transferInputStreamToString(is);
		if (jsonResult.length() > 0)
		{
			JSONObject rootObject = null;
			try
			{
				rootObject = new JSONObject(jsonResult);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				result.setStatus(HttpRequestStatus.REQUEST_INVALID_JSON_FORMAT);
			}
			if (rootObject != null)
			{
				// 天气更新请求结果（可能值：-1，服务器处理异常；0，找不到该城市信息；1，查询成功；2，本地时间戳与服务器一致）
				JSONObject head = rootObject.optJSONObject("head");
				if (head != null)
				{
					result.setStatus(head.optInt("result", HttpRequestStatus.REQUEST_SERVER_ERROR));
				}
				if (result.getStatus() == HttpRequestStatus.REQUEST_SUCCESS)
				{
					// 刷新天气成功，置刷新结束时间
					result.setRequestEndTime(SystemClock.elapsedRealtime());
					// 解析获取天气数据更新时间
					long updateTime = rootObject.optLong("updateTimeLong",
							CommonConstants.UNKNOWN_VALUE_LONG);
					if (updateTime != CommonConstants.UNKNOWN_VALUE_LONG)
					{
						Calendar calendar = Calendar.getInstance();
						int offsetZone = calendar.get(Calendar.ZONE_OFFSET);
						int oddsetDst = calendar.get(Calendar.DST_OFFSET);
						updateTime = updateTime - (offsetZone + oddsetDst);
						mCityInfo.setUpdateTime(updateTime);
					}
					else
					{
						// 如果直接拿long值无效则使用回之前的解析方式
						String updateTimeString = rootObject.optString("updateTime");
						mCityInfo.setUpdateTime(TextUtils.isEmpty(updateTimeString)
								? CommonConstants.UNKNOWN_VALUE_STRING
								: updateTimeString);
					}
					// 服务器天气缓存的时间戳
					mCityInfo.setUpdateTimestamp(rootObject.optLong("timestamp", 0));
					JSONObject weather = rootObject.optJSONObject("weather");
					if (weather != null)
					{
						// 城市地址信息
						parseJSONCityInfo(weather);
						// 现在天气信息
						parseJSONNow(weather);
						// 空气信息指数
						parsePM25(weather);
						// 指数信息单元
						parseJSONZhiShu(weather);
						// 天气预报
						parseJSONForecast(weather);
						// 24小时天气
						parseJSONHourly(weather);
						//极端天气信息
						parseJSONExtremeInfo(weather);
					}
				}
			}
		}
	}

	/** 查询城市天气信息，解析服务器下发的json文件的cityinfo字段 */
	private void parseJSONCityInfo(JSONObject rootObject)
	{
		JSONObject cityInfo = rootObject.optJSONObject("city");
		if (cityInfo != null)
		{
			String cityId = cityInfo.optString("cityId");
			mCityInfo.mCityId = TextUtils.isEmpty(cityId) ? mCityInfo.getCode() : cityId;
			String cityName = cityInfo.optString("city");
			mCityInfo.mCityName = TextUtils.isEmpty(cityName) ? mCityInfo.mCityName : cityName;
			String state = cityInfo.optString("state");
			mCityInfo.setState(TextUtils.isEmpty(state)
					? CommonConstants.UNKNOWN_VALUE_STRING
					: state);
			String country = cityInfo.optString("country");
			mCityInfo.setCountry(TextUtils.isEmpty(country)
					? CommonConstants.UNKNOWN_VALUE_STRING
					: country);
			mCityInfo.setTimeOffset((int) cityInfo.optLong("timeZone",
					CommonConstants.UNKNOWN_VALUE_LONG));
		}
	}

	/**
	 * 当前天气状况单元
	 */
	private void parseJSONNow(JSONObject rootObject)
	{
		// 现在天气信息
		JSONObject nowObject = rootObject.optJSONObject("currentWeather");
		if (nowObject != null)
		{
			NowTimeInfo nowInfo = new NowTimeInfo();
			mCityInfo.mNow = nowInfo;
			String status = nowObject.optString("status");
			nowInfo.setStatus(TextUtils.isEmpty(status)
					? CommonConstants.UNKNOWN_VALUE_STRING
					: status);
			int weatherType = nowObject.optInt("statusType");
			nowInfo.setStatusType(weatherType == CommonConstants.UNKNOWN_VALUE_INT
					? CommonConstants.UNKNOWN_WEATHER_TYPE
					: weatherType);
			nowInfo.setRealTemp((float) nowObject.optDouble("realTemp",
					CommonConstants.UNKNOWN_VALUE_FLOAT));
			nowInfo.setFeelsLike((float) nowObject.optDouble("feelLike",
					CommonConstants.UNKNOWN_VALUE_FLOAT));
			nowInfo.setHumidity(nowObject.optInt("humidity", CommonConstants.UNKNOWN_VALUE_INT));
			nowInfo.setHigh((float) nowObject
					.optDouble("high", CommonConstants.UNKNOWN_VALUE_FLOAT));
			nowInfo.setLow((float) nowObject.optDouble("low", CommonConstants.UNKNOWN_VALUE_FLOAT));
			// 风向描述
			String windDir = nowObject.optString("windDir");
			nowInfo.setWind(TextUtils.isEmpty(windDir)
					? CommonConstants.UNKNOWN_VALUE_STRING
					: windDir);
			// 风向类型
			nowInfo.setWindDirType(nowObject.optInt("windDirType",
					CommonConstants.UNKNOWN_WIND_DIR_TYPE));
			// 风力数值
			nowInfo.setWindStrengthValue((float) nowObject.optDouble("windStrengthInt",
					CommonConstants.UNKNOWN_VALUE_FLOAT));
			nowInfo.setVisibility((float) nowObject.optDouble("visibility",
					CommonConstants.UNKNOWN_VALUE_FLOAT));
			nowInfo.setBarometer((float) nowObject.optDouble("barometer",
					CommonConstants.UNKNOWN_VALUE_FLOAT));
			nowInfo.setDewpoint((float) nowObject.optDouble("dewpoint",
					CommonConstants.UNKNOWN_VALUE_FLOAT));
			String sunrise = nowObject.optString("sunrise");
			nowInfo.setSunrise(TextUtils.isEmpty(sunrise)
					? CommonConstants.UNKNOWN_VALUE_STRING
					: sunrise);
			String sunset = nowObject.optString("sunset");
			nowInfo.setSunset(TextUtils.isEmpty(sunset)
					? CommonConstants.UNKNOWN_VALUE_STRING
					: sunset);
			nowInfo.setUvIndex((float) nowObject.optDouble("uvIndex",
					CommonConstants.UNKNOWN_VALUE_FLOAT));
			nowInfo.setPop(nowObject.optInt("pop", CommonConstants.UNKNOWN_VALUE_INT));
			nowInfo.setRainFall((float) nowObject.optDouble("rainfall",
					CommonConstants.UNKNOWN_VALUE_LONG));
		}
	}

	/**
	 * 空气质量指数单元
	 * @param weather
	 */
	private void parsePM25(JSONObject rootObject)
	{
		if (mCityInfo.mNow != null)
		{
			JSONObject pm25Obj = rootObject.optJSONObject("aqi");
			if (pm25Obj != null)
			{
				// 环境空间质量指数
				mCityInfo.mNow.setAqi(pm25Obj.optInt("aqi", CommonConstants.UNKNOWN_VALUE_INT));
				// 质量类型
				mCityInfo.mNow.setQualityType(pm25Obj.optInt("qualityType",
						CommonConstants.UNKNOWN_VALUE_INT));
				// pm2.5指数
				mCityInfo.mNow.setPM25(pm25Obj.optInt("pm25", CommonConstants.UNKNOWN_VALUE_INT));
				// 可吸入颗粒物
				mCityInfo.mNow.setPM10(pm25Obj.optInt("pm10", CommonConstants.UNKNOWN_VALUE_INT));
				// 二氧化硫指数
				mCityInfo.mNow.setSO2(pm25Obj.optInt("so2", CommonConstants.UNKNOWN_VALUE_INT));
				// 二氧化氮指数
				mCityInfo.mNow.setNO2(pm25Obj.optInt("no2", CommonConstants.UNKNOWN_VALUE_INT));
			}
		}
	}

	/**
	 * 指数信息单元
	 * @param rootObject
	 */
	private void parseJSONZhiShu(JSONObject rootObject)
	{
		JSONObject zhishuObject = rootObject.optJSONObject("zhishu");
		if (zhishuObject != null)
		{
			ZhiShu zhishu = new ZhiShu();
			zhishu.setShushi(zhishuObject.optString("shushi", CommonConstants.UNKNOWN_VALUE_STRING));
			zhishu.setChuanyi(zhishuObject.optString("chuanyi",
					CommonConstants.UNKNOWN_VALUE_STRING));
			zhishu.setLiangshai(zhishuObject.optString("liangshai",
					CommonConstants.UNKNOWN_VALUE_STRING));
			zhishu.setChenlian(zhishuObject.optString("chenlian",
					CommonConstants.UNKNOWN_VALUE_STRING));
			zhishu.setZiwaixian(zhishuObject.optString("ziwaixian",
					CommonConstants.UNKNOWN_VALUE_STRING));
			zhishu.setLvyou(zhishuObject.optString("lvyou", CommonConstants.UNKNOWN_VALUE_STRING));
			zhishu.setGuomin(zhishuObject.optString("guomin", CommonConstants.UNKNOWN_VALUE_STRING));
			zhishu.setXiche(zhishuObject.optString("xiche", CommonConstants.UNKNOWN_VALUE_STRING));
			mCityInfo.mNow.mZhiShu = zhishu;
		}
	}

	/** 解析未来的天气信息 */
	private void parseJSONForecast(JSONObject rootObject)
	{
		JSONArray forecastArray = rootObject.optJSONArray("forecasts");
		if (forecastArray != null)
		{
			int len = forecastArray.length();
			ForecastInfo forecastInfo = null;
			for (int i = 0; i < len; i++)
			{
				JSONObject object = forecastArray.optJSONObject(i);
				if (object != null)
				{
					forecastInfo = new ForecastInfo();
					String weekDate = object.optString("weekDate");
					forecastInfo.setWeekDate(TextUtils.isEmpty(weekDate)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: weekDate);
					String status = object.optString("status");
					forecastInfo.setStatus(TextUtils.isEmpty(status)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: status);
					forecastInfo.setStatusType(object.optInt("statusType",
							CommonConstants.UNKNOWN_WEATHER_TYPE));
					forecastInfo.setHigh((float) object.optDouble("high",
							CommonConstants.UNKNOWN_VALUE_FLOAT));
					forecastInfo.setLow((float) object.optDouble("low",
							CommonConstants.UNKNOWN_VALUE_FLOAT));
					String windDir = object.optString("windDir");
					forecastInfo.setWindDir(TextUtils.isEmpty(windDir)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: windDir);
					forecastInfo.setWindDirType(object.optInt("windDirType",
							CommonConstants.UNKNOWN_WIND_DIR_TYPE));
					forecastInfo.setWindStrengthInt((float) object.optDouble("windForceInt",
							CommonConstants.UNKNOWN_VALUE_FLOAT));
					forecastInfo.setPop(object.optInt("pop", CommonConstants.UNKNOWN_VALUE_INT));
					String statusDay = object.optString("statusDay");
					forecastInfo.setDayStatus(TextUtils.isEmpty(statusDay)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: statusDay);
					String statusNight = object.optString("statusNight");
					forecastInfo.setNightStatus(TextUtils.isEmpty(statusNight)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: statusNight);
					String date = object.optString("date");
					forecastInfo.setLongDate(TextUtils.isEmpty(date)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: date);
					mCityInfo.mForecastList.add(forecastInfo);
				}
			}
		}
	}

	/** 解析未来24小时的天气信息 */
	private void parseJSONHourly(JSONObject rootObject)
	{
		JSONArray hourlyArray = rootObject.optJSONArray("hourlies");
		if (hourlyArray != null)
		{
			int len = hourlyArray.length();
			for (int i = 0; i < len; i++)
			{
				JSONObject hourlyObject = hourlyArray.optJSONObject(i);
				if (hourlyObject != null)
				{
					HourInfo hourInfo = new HourInfo();
					String date = hourlyObject.optString("date");
					hourInfo.setDate(TextUtils.isEmpty(date)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: date);
					hourInfo.setHour(hourlyObject.optInt("hour", CommonConstants.UNKNOWN_VALUE_INT));
					hourInfo.setTemp((float) hourlyObject.optDouble("temp",
							CommonConstants.UNKNOWN_VALUE_FLOAT));
					hourInfo.setPop(hourlyObject.optInt("pop", CommonConstants.UNKNOWN_VALUE_INT));
					hourInfo.setHumidity(hourlyObject.optInt("humidity",
							CommonConstants.UNKNOWN_VALUE_INT));
					String windDir = hourlyObject.optString("windDir");
					hourInfo.setWindDir(TextUtils.isEmpty(windDir)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: windDir);
					hourInfo.setWindDirType(hourlyObject.optInt("windDirType",
							CommonConstants.UNKNOWN_WIND_DIR_TYPE));
					hourInfo.setWindStrengthValue((float) hourlyObject.optDouble("windForeInt",
							CommonConstants.UNKNOWN_VALUE_FLOAT));
					String status = hourlyObject.optString("status");
					hourInfo.setStatus(TextUtils.isEmpty(status)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: status);
					hourInfo.setStatusType(hourlyObject.optInt("statusType",
							CommonConstants.UNKNOWN_WEATHER_TYPE));
					mCityInfo.mHourList.add(hourInfo);
				}
			}
		}
	}

	/***
	 * 功能简述: 解析天气刷新时的极端天气预警信息
	 * 功能详细描述:
	 * 注意: 
	 * @param rootObject
	 */
	public void parseJSONExtremeInfo(JSONObject rootObject)
	{
		JSONArray extremeArray = rootObject.optJSONArray("alarms");
		if (extremeArray != null)
		{
			int len = extremeArray.length();
			for (int i = 0; i < len; i++)
			{
				JSONObject extremeObject = extremeArray.optJSONObject(i);
				if (extremeObject != null)
				{
					ExtremeInfo extremeInfo = new ExtremeInfo();
					extremeInfo.setExtremeId(extremeObject.optInt("alert_id",
							CommonConstants.UNKNOWN_VALUE_INT));
					// 协议中极端天气模块没有下发cityId，拿城市信息模块的cityId
					extremeInfo.setCityId(mCityInfo.getCode());
					String publishTime = extremeObject.optString("publish_time");
					extremeInfo.setPublishTime(TextUtils.isEmpty(publishTime)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: publishTime);
					String expTime = extremeObject.optString("exp_time");
					extremeInfo.setExpTime(TextUtils.isEmpty(expTime)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: expTime);
					String type = extremeObject.optString("type");
					extremeInfo.setType(TextUtils.isEmpty(type)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: type);
					String description = extremeObject.optString("description");
					extremeInfo.setDescription(TextUtils.isEmpty(description)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: description);
					String phenomena = extremeObject.optString("phenomena");
					extremeInfo.setPhenomena(TextUtils.isEmpty(phenomena)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: phenomena);
					extremeInfo.setLevel(extremeObject.optInt("level",
							CommonConstants.UNKNOWN_VALUE_INT));
					String levelStr = extremeObject.optString("level_str");
					extremeInfo.setLevelStr(TextUtils.isEmpty(levelStr)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: levelStr);
					String message = extremeObject.optString("message");
					extremeInfo.setMessage(TextUtils.isEmpty(message)
							? CommonConstants.UNKNOWN_VALUE_STRING
							: message);
					// 极端天气的时区偏移值不再下发，拿当前城市的就行
					extremeInfo.setTzOffset(mCityInfo.getTimeOffset());
					mCityInfo.mExtremeList.add(extremeInfo);
				}
			}
		}
	}
}