package com.jiubang.goscreenlock.theme.cjpcardcool.weather.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.CommonConstants;

/**
 * 
 * <br>类描述: 风力、温度等数值单位转换的工具类
 * <br>功能详细描述:
 * 
 * @author  lishen
 * @date  [2012-11-3]
 */
public class UnitTransformUtil {

	/**
	 * <br>功能简述: 将华氏温度转化为摄氏温度
	 * <br>功能详细描述:
	 * <br>注意: 这里默认传入的温度值是有效的，只会按照传入的数值计算后返回
	 * @param degree 华氏温度值
	 * @return 摄氏温度值
	 */
	private static float getTempInCelsius(float fahrenheit) {
		return (fahrenheit - 32) * 5 / 9;
	}

	/**
	 * <br>功能简述: 将华氏温度转化为摄氏温度
	 * <br>功能详细描述:输入输出都为整形，结果进行四舍五入
	 */
	public static int getTempIntInCelsius(int fahrenheit) {
		return getIntWithRound((float) (fahrenheit / 1.8));
	}

	/**
	 * <br>功能简述: 将华氏温度转化为摄氏温度
	 * <br>功能详细描述:输入输出都为整形，结果进行四舍五入
	 */
	public static int getTempInFahrenheit(int celsius) {
		return getIntWithRound((float) (celsius * 1.8));
	}

	/**
	 * 功能简述: 将华氏温度转化为摄氏温度</br>
	 * 功能详细描述: 按照特定小数位数的构造摄氏单位下的字符串类型温度数值</br>
	 * 注意: 如果温度无效则返回“--”</br>
	 * @param fahrenheit 华氏温度值
	 * @param digits 小数位
	 * @return 华氏温度单位下的温度数值字符串（不带单位符号）
	 */
	public static String getTempStringInCelsius(float fahrenheit, int digits) {
		// 超出合理值域
		if (fahrenheit <= CommonConstants.UNKNOWN_VALUE_FLOAT) {
			return CommonConstants.UNKNOWN_VALUE_STRING;
		} else {
			NumberFormat formatter = DecimalFormat.getInstance();
			formatter.setMaximumFractionDigits(digits);
			return formatter.format(getTempInCelsius(fahrenheit));
		}
	}

	/**
	 * 功能简述: 将华氏温度转化为摄氏温度</br>
	 * 功能详细描述: 按照特定小数位数的构造摄氏单位下的float类型温度数值</br>
	 * @param fahrenheit 华氏温度值
	 * @param digits 小数位
	 * @return 摄氏温度单位下的温度数值
	 */
	public static float getTempValueInCelsius(float fahrenheit, int digits) {
		String celsius = getTempStringInCelsius(fahrenheit, digits);
		if (celsius.equals(CommonConstants.UNKNOWN_VALUE_STRING)) {
			return CommonConstants.UNKNOWN_VALUE_FLOAT;
		}
		float floatValue = CommonConstants.UNKNOWN_VALUE_FLOAT;
		try {
			floatValue = DecimalFormat.getInstance().parse(celsius).floatValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return floatValue;
	}

	/**
	 * 功能简述:浮点数进行四舍五入，返回整数
	 * 功能详细描述:
	 * 注意:
	 * @param f
	 * @return
	 */
	public static int getIntWithRound(float f) {
		return Math.round(f);
	}

	/**
	 * <br>功能简述:
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param f
	 * @return
	 */
	public static float getFloatWithADecimal(float f) {
		f = Math.round(f * 10);
		return f / 10;
	}
	/**
	 * 取得KPH单位下字符串类型的风力值，保留一位小数
	 * 
	 * @param windStrength 返回风力数值的字符串，不带单位
	 * @return
	 */
	public static String getWindInKPH(String windStrength) {
		if (windStrength == null) {
			return CommonConstants.UNKNOWN_VALUE_STRING;
		}
		try {
			float windFloat = Float.parseFloat(windStrength.split(" ")[0]);
			NumberFormat numFormatter = NumberFormat.getNumberInstance();
			numFormatter.setMaximumFractionDigits(1);
			numFormatter.setMinimumFractionDigits(1); // 最少保留一位小数
			windStrength = numFormatter.format(windFloat * 1.61);
			return windStrength;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CommonConstants.UNKNOWN_VALUE_STRING;
	}

	/**
	 * 功能简述: 取得KPH单位下的风力
	 * 功能详细描述: 1MPH = 1.61 KPH
	 * @param windStrength
	 * @return 如果转化单位失败，返回 -10000；
	 */
	public static float getWindInKPH(float windStrength, int digits) {
		windStrength *= 1.61;
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(digits);
		String kph = formatter.format(windStrength);
		float floatValue = CommonConstants.UNKNOWN_VALUE_FLOAT;
		try {
			floatValue = formatter.parse(kph).floatValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return floatValue;
	}

	/**
	 * 功能简述: 取得km/h单位下字符串类型的风力值，保留一位小数
	 * 功能详细描述: 1mph = 1.61km/h，与kph单位的换算相同，所以采用kph的换算公式 
	 * @param windStrength 返回风力数值的字符串，不带单位
	 * @return
	 */
	public static String getWindInKMH(String windStrength) {
		return getWindInKPH(windStrength);
	}

	/**
	 * 功能简述: 取得KMH单位下的风力
	 * 功能详细描述: 
	 * @param windStrength
	 * @return 如果转化单位失败，返回 -10000；
	 */
	public static float getWindInKMH(float windStrength, int digits) {
		return getWindInKPH(windStrength, digits);
	}

	/**
	 * 功能简述: 取得m/s单位下字符串类型的风力值，保留一位小数
	 * 功能详细描述: 1mph = 0.4464m/s
	 * @param windStrength 返回风力数值的字符串，不带单位
	 * @return
	 */
	public static String getWindInMS(String windStrength) {
		if (windStrength == null) {
			return CommonConstants.UNKNOWN_VALUE_STRING;
		}
		try {
			float windFloat = Float.parseFloat(windStrength.split(" ")[0]);
			NumberFormat numFormatter = NumberFormat.getNumberInstance();
			numFormatter.setMaximumFractionDigits(1);
			numFormatter.setMinimumFractionDigits(1); // 最少保留一位小数
			windStrength = numFormatter.format(windFloat * 0.4464);
			return windStrength;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CommonConstants.UNKNOWN_VALUE_STRING;
	}

	/**
	 * 功能简述: 取得m/s单位下的风力值
	 * 功能详细描述: 
	 * @param windStrength
	 * @param digits 小数位
	 * @return 如果转化单位失败，返回 -10000；
	 */
	public static float getWindInMS(float windStrength, int digits) {
		windStrength *= 0.4464f;
		NumberFormat formatter = DecimalFormat.getInstance();
		formatter.setMaximumFractionDigits(digits);
		String ms = formatter.format(windStrength);
		float floatValue = CommonConstants.UNKNOWN_VALUE_FLOAT;
		try {
			floatValue = formatter.parse(ms).floatValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return floatValue;
	}

	public static final float[] LEVEL = { 0.67f, 3.58f, 7.62f, 12.32f, 17.92f, 24.19f, 31.14f,
			38.53f, 46.59f, 54.88f, 63.84f, 73.03f, 82.89f, 92.97f, 103.49f, 114.25f, 125.67f };

	/**
	 * 功能简述: 将MPH转换为风力等级
	 * 功能详细描述:
	 * 注意:取2位小数判断。
	 * @param windStrength MPH单位的风速值
	 * @return 风力等级
	 */
	public static int getWindInLevel(double windStrength) {
		if (windStrength < 0) {
			return CommonConstants.UNKNOWN_VALUE_INT;
		} else if (windStrength < LEVEL[0]) {
			return 0;
		} else if (windStrength < LEVEL[1]) {
			return 1;
		} else if (windStrength < LEVEL[2]) {
			return 2;
		} else if (windStrength < LEVEL[3]) {
			return 3;
		} else if (windStrength < LEVEL[4]) {
			return 4;
		} else if (windStrength < LEVEL[5]) {
			return 5;
		} else if (windStrength < LEVEL[6]) {
			return 6;
		} else if (windStrength < LEVEL[7]) {
			return 7;
		} else if (windStrength < LEVEL[8]) {
			return 8;
		} else if (windStrength < LEVEL[9]) {
			return 9;
		} else if (windStrength < LEVEL[10]) {
			return 10;
		} else if (windStrength < LEVEL[11]) {
			return 11;
		} else if (windStrength < LEVEL[12]) {
			return 12;
		} else if (windStrength < LEVEL[13]) {
			return 13;
		} else if (windStrength < LEVEL[14]) {
			return 14;
		} else if (windStrength < LEVEL[15]) {
			return 15;
		} else if (windStrength < LEVEL[16]) {
			return 16;
		} else {
			return 17;
		}
	}

	/**
	 * 功能简述: 将MPH转换为风力等级
	 * 功能详细描述:
	 * 注意:取2位小数判断。
	 * @param windStrength MPH单位的风速值
	 * @return 风力等级
	 */
	public static String getWindInLevel(String windStrength) {
		if (windStrength == null) {
			return CommonConstants.UNKNOWN_VALUE_STRING;
		}
		try {
			float windFloat = Float.parseFloat(windStrength.split(" ")[0]);
			int windLevel = getWindInLevel(windFloat);
			windStrength = windLevel + "";
			return windStrength;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CommonConstants.UNKNOWN_VALUE_STRING;
	}

	private static final float[] WINDLEVELMAPPING = new float[] { 0.4f, 2.2f, 5.6f, 9.8f, 14.9f,
			20.1f, 27.5f, 33.5f, 41.8f, 49.2f, 59.3f, 67.1f, 73.2f };

	/**
	* 通过风力等级获取对应的MPH
	*/
	public static float getMPHFromLevel(int level) {
		return WINDLEVELMAPPING[level];
	}

	/**
	 * 取得KNOTS单位下字符串类型的风力值，保留一位小数
	 * 
	 * @param windStrength 返回风力数值的字符串，不带单位
	 * @return
	 */
	public static String getWindInKnots(String windStrength) {
		if (windStrength == null) {
			return CommonConstants.UNKNOWN_VALUE_STRING;
		}
		try {
			float windFloat = Float.parseFloat(windStrength.split(" ")[0]);
			NumberFormat numFormatter = NumberFormat.getNumberInstance();
			numFormatter.setMaximumFractionDigits(1);
			numFormatter.setMinimumFractionDigits(1); // 最少保留一位小数
			windStrength = numFormatter.format(windFloat * 0.8679);
			return windStrength;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CommonConstants.UNKNOWN_VALUE_STRING;
	}

	/**
	 * 功能简述: 取得KPH单位下的风力
	 * 功能详细描述: 1MPH = 0.8679 KPH
	 * @param windStrength
	 * @return 如果转化单位失败，返回 -10000；
	 */
	public static float getWindInKnots(float windStrength, int digits) {
		windStrength *= 0.8679f;
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(digits);
		String kph = formatter.format(windStrength);
		float floatValue = CommonConstants.UNKNOWN_VALUE_FLOAT;
		try {
			floatValue = formatter.parse(kph).floatValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return floatValue;
	}

	/**
	 * 功能简述: 将华氏温度转化为摄氏温度</br>
	 * 功能详细描述: 按照特定小数位数的构造摄氏单位下的字符串类型温度数值</br>
	 * 注意: 如果温度无效则返回“--”</br>
	 * @param degree 摄氏温度值
	 * @param digits 小数位
	 * @return 华氏温度单位下的温度数值字符串（不带单位符号）
	 */
	public static String getCDegree(float degree, int digits) {
		// 超出合理值域
		if (degree <= CommonConstants.UNKNOWN_VALUE_FLOAT) {
			return CommonConstants.UNKNOWN_VALUE_STRING;
		} else {
			NumberFormat formatter = DecimalFormat.getInstance();
			formatter.setMaximumFractionDigits(digits);
			return formatter.format(getCDegree(degree));
		}
	}


	/**
	 * <br>功能简述: 将华氏温度转化为摄氏温度
	 * <br>功能详细描述:
	 * <br>注意: 这里默认传入的温度值是有效的，只会按照传入的数值计算后返回
	 * @param degree 华氏温度值
	 * @return 摄氏温度值
	 */
	public static float getCDegree(float degree) {
		if (degree == CommonConstants.UNKNOWN_VALUE_FLOAT) {
			return CommonConstants.UNKNOWN_VALUE_FLOAT;
		}
		return (degree - 32) * 5f / 9f;
	}

	/**
	 * 功能简述: 将华氏温度转化为摄氏温度</br>
	 * 功能详细描述: 按照特定小数位数的构造摄氏单位下的字符串类型温度数值</br>
	 * 注意: 如果温度无效则返回“--”</br>
	 * @param degree 华氏温度值
	 * @param digits 小数位
	 * @return 华氏温度单位下的温度数值字符串（不带单位符号）
	 */
	public static String getFDegree(float degree, int digits) {
		// 超出合理值域
		if (degree <= CommonConstants.UNKNOWN_VALUE_FLOAT) {
			return CommonConstants.UNKNOWN_VALUE_STRING;
		} else {
			NumberFormat formatter = DecimalFormat.getInstance();
			formatter.setMaximumFractionDigits(digits);
			return formatter.format(getFDegree(degree));
		}
	}

	/**
	 * <br>功能简述: 将摄氏温度转化为华氏温度
	 * <br>功能详细描述:
	 * <br>注意: 这里默认传入的温度值是有效的，只会按照传入的数值计算后返回
	 * @param degree 摄氏温度值
	 * @return 华氏温度值
	 */
	public static float getFDegree(float degree) {
		return degree * 9f / 5f + 32;
	}


}
