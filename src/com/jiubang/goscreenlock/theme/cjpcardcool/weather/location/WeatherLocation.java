package com.jiubang.goscreenlock.theme.cjpcardcool.weather.location;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.os.SystemClock;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.CityBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.LocationConstants;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.Util;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.Constants;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.HttpExecutor;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.HttpExecutorContext;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.HttpRequestStatus;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.HttpUtil;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.Request;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.Result;

/**
 * 
 * 类描述: 获取城市地址信息
 * 
 * @author liuwenqin
 * @date [2012-9-4]
 */
public class WeatherLocation
{

	private static final int	TIME_OUT	= 7000;

	/**
	 * 解析定位请求时，服务器返回的json文件
	 * 
	 * @onlyOne 是否只读取一个城市
	 */
	private static void parseCityInfoJSON(List<CityBean> locationCities, String json,
			boolean onlyOne, Result result)
	{
		JSONObject rootObject = null;
		try
		{
			rootObject = new JSONObject(json);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		if (rootObject != null)
		{
			int status = rootObject.optInt("result", HttpRequestStatus.REQUEST_SUCCESS);
			result.setStatus(status);
			if (status == HttpRequestStatus.REQUEST_SUCCESS)
			{
				result.setRequestEndTime(SystemClock.elapsedRealtime());
				JSONArray citiesArray = rootObject.optJSONArray("cities");
				if (citiesArray != null)
				{
					int length = citiesArray.length();
					CityBean locationCity = null;
					for (int i = 0; i < length; i++)
					{
						locationCity = new CityBean();
						JSONObject cityObject = citiesArray.optJSONObject(i);
						locationCity.setCityId(cityObject.optString("cityId",
								Constants.STR_NOT_AVAILABLE));
						locationCity.setCityName(cityObject.optString("city",
								Constants.STR_NOT_AVAILABLE));
						locationCity.setOldCityId(cityObject.optString("oldId",
								Constants.STR_NOT_AVAILABLE));
						locationCity.setStateName(cityObject.optString("state",
								Constants.STR_NOT_AVAILABLE));
						locationCity.setCountryName(cityObject.optString("country",
								Constants.STR_NOT_AVAILABLE));
						locationCity.setTimeZone(cityObject.optString("timeZone",
								Constants.STR_NOT_AVAILABLE));
						locationCities.add(locationCity);
						if (onlyOne)
						{
							break;
						}
					}
				}
			}
			else
			{
				if (status == HttpRequestStatus.REQUEST_SERVER_ERROR)
				{
				}
				else
				{
					// 未定位到城市信息
				}
				//				String msg = rootObject.optString("msg", "");
			}
		}
	}

	// 处理"USA"简称
	private static String toFullName(String name)
	{
		String fullName = name;
		if ("USA".equals(fullName))
		{
			fullName = "United States";
		}
		return fullName;
	}

	/**
	 * 从协义2.0采用定位请求的方法，不区分国内外用户
	 * @param context
	 * @param curLocation
	 * @param languageSetting 程序语言设置,包含区域，如en_us
	 * @return
	 */
	public static List<CityBean> getMyLocation(Context context, Location curLocation,
			Result result, String languageSetting)
	{
		List<CityBean> curCities = null;

		Request request = null;
		request = new Request(LocationConstants.STR_HTTP + LocationConstants.LOCATION_SERVER_HOST
				+ LocationConstants.STR_API_GPS);

		request.addHeader("latlng", curLocation.getLatitude() + "," + curLocation.getLongitude());
		request.addHeader("lang", languageSetting);
		request.addHeader("sys", android.os.Build.VERSION.RELEASE);
		request.addHeader("ps", LocationConstants.LOCATION_PROTOCOL_VERSION);

		// 先用经纬从google那里获取城市相关息，再与我们的服务器定位城市
		Map<String, String> map = googleCityGeo(context, curLocation);
		String countryName = null;
		String areaName = null;
		String areaNameAlias = null;
		String localityName = null;
		String sublocalityName = null;
		if (map != null && map.size() > 0)
		{
			countryName = map.get("CountryName");
			countryName = toFullName(countryName);
			areaName = map.get("AreaName");
			areaNameAlias = map.get("AreaName_Alias");
			localityName = map.get("LocalityName");
			sublocalityName = map.get("SublocalityName");
		}
		request.addHeader("subcity", null == sublocalityName ? "" : sublocalityName);
		request.addHeader("city", null == localityName ? "" : localityName);
		request.addHeader("state", null == areaName ? "" : areaName);
		request.addHeader("stateAlias", null == areaNameAlias ? "" : areaNameAlias);
		request.addHeader("country", null == countryName ? "" : countryName);

		String requestURL = null;
		try
		{
			requestURL = request.composeCompleteURL();
		}
		catch (UnsupportedEncodingException e1)
		{
			e1.printStackTrace();
		}

		if (requestURL != null)
		{
			result.setStatus(HttpRequestStatus.REQUEST_HTTP_FAILED);
			HttpExecutor httpExecutor = HttpExecutorContext.getHttpExecutor();
			if (httpExecutor.checkNetwork(result, context))
			{
				result.setRequestStartTime(SystemClock.elapsedRealtime());
				InputStream inputStream = httpExecutor.doRefresh(requestURL, request, result);
				if (inputStream != null)
				{
					String cityJson = null;
					try
					{
						cityJson = HttpUtil.readInputStream(inputStream, HTTP.UTF_8);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					if (cityJson != null)
					{
						curCities = new ArrayList<CityBean>();
						parseCityInfoJSON(curCities, cityJson, true, result);
					}
					try
					{
						inputStream.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					finally
					{
						inputStream = null;
					}
				}
				httpExecutor.release();
			}
		}
		return curCities;
	}

	/**
	 * <br>功能简述: 调用Google Geo API将经纬度转为地名
	 * @param context
	 * @param location
	 * @return
	 * @throws JSONException 
	 * @throws Exception
	 */
	private static Map<String, String> googleCityGeo(Context context, Location location)
	{
		Map<String, String> googleCitieInfos = new HashMap<String, String>();
		String longitude = String.valueOf(location.getLongitude());
		String latitude = String.valueOf(location.getLatitude());
		boolean isChinese = Util.isInternalUser(context);
		// 示例地址http://maps.google.com/maps/api/geocode/json?latlng=23.129163,113.264435&sensor=false&language=en
		String host = isChinese
				? "http://ditu.google.com/maps/api/geocode/json"
				: "http://maps.google.com/maps/api/geocode/json";
		//Google定位，减少超时的时间
		Request request = new Request(host, TIME_OUT, TIME_OUT);
		request.addHeader("latlng", latitude + "," + longitude);
		request.addHeader("sensor", "false");
		// 2.0协义要求上传的信息是是英语
		request.addHeader("language", "en");
		//伪装成浏览器，防止部分服务器不给第三方应用程序访问
		request.setUseAgent(true);
		Result result = new Result();
		result.setStatus(HttpRequestStatus.REQUEST_HTTP_FAILED);
		String requestURL = null;
		try
		{
			requestURL = request.composeCompleteURL();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		if (requestURL != null)
		{
			HttpExecutor httpExecutor = HttpExecutorContext.getHttpExecutor();
			if (httpExecutor.checkNetwork(result, context))
			{
				InputStream inputStream = httpExecutor.doRefresh(requestURL, request, result);
				if (inputStream != null)
				{
					try
					{
						String jsonString = HttpUtil.readInputStream(inputStream, HTTP.UTF_8);
						JSONObject root = new JSONObject(jsonString);
						String status = root.getString("status");
						// 如果请求处理成功，才接着解释数据
						if ("OK".equalsIgnoreCase(status))
						{
							JSONArray results = root.getJSONArray("results");
							final int len = results.length();
							for (int i = 0; i < len; i++)
							{
								JSONObject resultPer = results.getJSONObject(i);
								JSONArray types = resultPer.getJSONArray("types");
								if (types.length() > 0)
								{
									if ("postal_code".equalsIgnoreCase(types.getString(0)))
									{
										JSONArray components = resultPer
												.getJSONArray("address_components");
										final int len2 = components.length();
										for (int j = 0; j < len2; j++)
										{
											JSONObject component = components.getJSONObject(j);
											JSONArray componentTypes = component
													.getJSONArray("types");
											if (componentTypes.length() > 0)
											{
												if ("country".equalsIgnoreCase(componentTypes
														.getString(0)))
												{
													// 国家
													googleCitieInfos.put("CountryName",
															component.getString("long_name"));
												}
												else if ("administrative_area_level_1"
														.equalsIgnoreCase(componentTypes
																.getString(0)))
												{
													// 直辖市、省、自治区、州
													googleCitieInfos.put("AreaName",
															component.getString("long_name"));
												}
												else if ("administrative_area_level_2"
														.equalsIgnoreCase(componentTypes
																.getString(0)))
												{
													// 直辖市、省、自治区、州的第二级分类
													googleCitieInfos.put("AreaName_Alias",
															component.getString("long_name"));
												}
												else if ("locality".equalsIgnoreCase(componentTypes
														.getString(0)))
												{
													// 市、区
													googleCitieInfos.put("LocalityName",
															component.getString("long_name"));
												}
												else if ("sublocality"
														.equalsIgnoreCase(componentTypes
																.getString(0)))
												{
													// 市、区第二级分类
													googleCitieInfos.put("SublocalityName",
															component.getString("long_name"));
												}
											}
										}
										break;
									}
								}
							}
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
				httpExecutor.release();
			}
		}

		return googleCitieInfos;
	}
}
