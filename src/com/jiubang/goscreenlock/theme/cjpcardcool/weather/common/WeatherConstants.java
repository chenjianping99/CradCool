package com.jiubang.goscreenlock.theme.cjpcardcool.weather.common;

/**
 * 天气信息的常量
 * @author lishen
 *
 */
public class WeatherConstants
{

	/** 天气协议版本号。<br>
	 * 注意：
	 * <li>天气协议从1.3版本开始，服务器下发的天气信息都是经过压缩的。客户端必须解压
	 * <li>天气协议2.0，中英文协议合并
	 */
	public static final String	WEATHER_PROTOCOL_VERSION	= "2.0";

	/** 天气服务器地址 */
	public static final String	WEATHER_SERVER_HOST			= "goweatherex.3g.cn";

	/** 获取天气信息的链接 */
	public static final String	GET_WEATHER_URL				= "/goweatherex/weather/getWeather";

	/**12小时制*/
	public static final String	HOUR_FORMAT_12				= "12";
	/**24小时制*/
	public static final String	HOUR_FORMAT_24				= "24";

	/**
	 * 天气类型
	 */
	/** 未知天气类型 */
	public static final int		WEATHER_TYPE_UNKNOWN		= 0x01;
	/** 晴 */
	public static final int		WEATHER_TYPE_SUNNY			= 0x02;
	/** 多云 */
	public static final int		WEATHER_TYPE_CLOUDY			= 0x03;
	/** 阴天 */
	public static final int		WEATHER_TYPE_OVERCAST		= 0x04;
	/** 雪 */
	public static final int		WEATHER_TYPE_SNOWY			= 0x05;
	/** 大雾 */
	public static final int		WEATHER_TYPE_FOG			= 0x06;
	/** 下雨 */
	public static final int		WEATHER_TYPE_RAINY			= 0x07;
	/** 雷雨 */
	public static final int		WEATHER_TYPE_THUNDERSTORM	= 0x08;

}
