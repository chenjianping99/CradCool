package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.DisplayMetrics;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;

/**
 * 
 * @author zhangjie
 * 
 */
public class Constant {

	/*-------------------------------------设计师的切图分辨率－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
	public static final int S_DEFAULT_WIDTH = 720;
	public static final int S_DEFAULT_HEIGHT = 1280;

	/*-----------------------------------基础信息的key---------------------------------------------*/
	public static final String TYPE = "type"; // onMonitor传递的bundle的key，暂时只有四种类型，未读短信，未接电话，电池状态改变，电池电量改变
	public static final String PARAM = "param"; // 不同type获取的值的key
	public static final String ISDISPLAYDATE = "isdisplaydate"; // 是否显示日期true为显示，false为不显示
	public static final String DATEFORMAT = "dateformat"; // 日期格式
	public static final String ISLOCKSOUND = "islocksound"; // 锁屏声音
	public static final String ISUNLOCKSOUND = "isunlocksound"; // 解锁声音
	public static final String ISQUAKE = "isquake"; // 解锁震动
	public static final String THEME_NAME = "name"; // 主题名称
	public static final String ISTIME24 = "istime24"; // 是否24小时，1为24小时制，0为12小时制
	public static final String CALL = "call"; // 未接来电的数量
	public static final String SMS = "sms"; // 未读短信是数量
	public static final String BATTERYSTATE = "batterystate"; // 电池状态0:正常,1:充电中,2:电量低,3:已充满
	public static final String BATTERYLEVEL = "batterylevel"; // 0~100
	public static final String LOCKBG = "lockbg"; // 是否更换壁纸
	public static final String ISFULLSCREEN = "isfullscreen"; // 是否全屏
	
	public static final String ACTION_GOLOCKER_UNLOCK = "com.jiubang.goscreenlock.unlock";
	
	/*-----------------------------------基础信息---------------------------------------------*/
	/*-------------------------------------天气---------------------------------------------*/
	public static final String WEATHER = "weather";
	public static final String ISWEATHEROPENED = "isweatherserviceopened"; // 天气服务是否启动
	public static final String CITYNAME = "cityname"; // 城市名称
	public static final String WEATHERTYPE = "weathertype"; // 天气类型，支持8种
	public static final String CURRTEMPERATURE = "currtemperature"; // 当前温度
	public static final String HIGHTEMPERATURE = "hightemperature"; // 最高温
	public static final String LOWTEMPERATURE = "lowtemperature"; // 最低温

	public static final String WEATHER_ISSUCCED = "issucced";
	public static final String WEATHER_MSG = "msg";
	public static final String WEATHER_CITYNAME = "cityname";
	public static final String WEATHER_TYPE = "type";
	public static final String WEATHER_CURR = "curr";
	public static final String WEATHER_HIGH = "high";
	public static final String WEATHER_LOW = "low";
	public static final String WEATHER_UNIT = "unit"; // 单位
	public static final String WEATHER_PREVIEW = "preivew";
	/*-------------------------------------天气---------------------------------------------*/
	/*------------------------------------集中各个provider，统一修改名称-----------------------------------------*/
	public static final String THEME_PACKAGE_SHORT_NAME = "cjpcardcool";
	public static final String THEME_PACKAGE_NAME = "com.jiubang.goscreenlock.theme.cjpcardcool";
	public static final String WEATHERBROADCASTFILTER = THEME_PACKAGE_NAME
			+ ".weatherdfilter";
	public static final String WEATHER_AUTHORITY = THEME_PACKAGE_NAME
			+ ".weather.util.provider";
	public static final String WEATHEREFLUSH = THEME_PACKAGE_NAME
			+ ".weatherreflush";
	public static final String WEATHER_FLUSH_FORCE = "weather_isforce_reflush";
	public static final String WEATHER_MY_PROVIDER_AUTHORITY = THEME_PACKAGE_NAME
			+ ".util.myprovider";
	/*------------------------------------集中各个provider，统一修改名称-----------------------------------------*/

	public static final int TIME_SIZE = 60;
	public static final int TIME_AP_SIZE = 20;
	public static final int TIME_DATE_SIZE = 16;
	public static final int WEATHER_CURR_SIZE = 40;
	public static final int WEATHER_TYPE_SIZE = 16;
	public static final int WEATHER_HIGH_LOW_SIZE = 16;
	public static final int LOCKER_MESSAGES = 30;
	public static final int LOCKER_MESSAGES_OTHER = 14;

	public static int sRealWidth = 720;
	public static int sRealHeight = 1280;
	public static int sStatusBarHeight = 0;
	public static boolean sIsDisplayDate = true; // 是否显示日期true为显示，false为不显示
	public static String sDateFormat = "yyyy-MM-dd"; // 日期格式
	public static boolean sIslocksound = false; // 是否启用锁屏音
	public static boolean sIsunlocksound = true; // 是否启用解锁音
	public static boolean sIsquake = true; // 是否振动
	public static int sIsTime24 = 1; // 是否24小时，1为24小时制，0为12小时制
	public static int sCall = 0; // 未接电话
	public static int sSMS = 0; // 未读短信
	public static boolean sIsfullscreen = false; // 是否全屏
	public static int sBatteryState = 0;
	public static int sBatteryLevel = 50;
	
	public static boolean sIsScreenOn = false;
	
	public static int sBgIndex = 0; 
	public static int sBlackColor = 0xff161616;
	public static int[] sColor = {0xffe63e21, 0xfffe7708, 0xfff7b533,
		0xfff01d54, 0xfff04582, 0xfff7aa7b,
		0xff3244d0, 0xff1382ff, 0xff3dc4f6};
	
	public static int[] sBatteryBgColor = {0xfffdefd5, 0xfffdebe5, 0xffd8f1fd};

	public static int getStatusBarHeight(Context context) {
		if (sStatusBarHeight > 0) {
			return sStatusBarHeight;
		}
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sStatusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return sStatusBarHeight;
	}

	public static float sXRate = 1;
	public static float sYRate = 1;
	public static int sTextColor = 0xfffff6ea;
	public static boolean sIstranslucentSysBar = false;
	protected static boolean sIsMum = false;
	public static void initMetrics(Context c) {
		DisplayMetrics mDm = c.getResources().getDisplayMetrics();
		Global.sScreenWidth = mDm.widthPixels;
		Global.sScreenHeight = mDm.heightPixels;
		sRealWidth = Global.sScreenWidth;
		if (sIsfullscreen) {
			sRealHeight = Global.sScreenHeight;
		} else {
			sRealHeight = Global.sScreenHeight - getStatusBarHeight(c);
		}
//		if (Build.VERSION.SDK_INT >= 19) {
//			DrawUtils.resetDensity(c);
//			sRealHeight = Global.sScreenHeight - getStatusBarHeight(c);
//		}
		
		sXRate = (float) sRealWidth / (float) Constant.S_DEFAULT_WIDTH;
		sYRate = (float) sRealHeight / (float) Constant.S_DEFAULT_HEIGHT;
		LogUtils.log("ddd", "sYRate =" + sYRate + "Constant.sRealHeight =" + Constant.sRealHeight);
	}

}
