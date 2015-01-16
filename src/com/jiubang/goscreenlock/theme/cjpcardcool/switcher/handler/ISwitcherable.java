package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;


/**
 * 
 * <br>类描述:可切换开关
 * 
 * @author  guoyiqing
 * @date  [2013-1-7]
 */
public interface ISwitcherable {

	public static final int SWITCH_TYPE_EMPTY = -1;
	public static final int SWITCH_TYPE_WIFI = 21;
	public static final int SWITCH_TYPE_WIFI_AP = 1;
	public static final int SWITCH_TYPE_SCREEN_TIMEOUT = 2;
	public static final int SWITCH_TYPE_SDMOUNT = 3;
	public static final int SWITCH_TYPE_SDMASS = 4;
	public static final int SWITCH_TYPE_RING = 5;
	public static final int SWITCH_TYPE_VIBRATE = 6;
	public static final int SWITCH_TYPE_REBOOT = 7;
	public static final int SWITCH_TYPE_MTK_GPRS = 8;
	public static final int SWITCH_TYPE_LOCK_SCREEN = 9;
	public static final int SWITCH_TYPE_HAPTICFB = 10;
	public static final int SWITCH_TYPE_GPS = 11;
	public static final int SWITCH_TYPE_NORMAL_GPRS = 12;
	public static final int SWITCH_TYPE_FLASHLIGHT = 13;
	public static final int SWITCH_TYPE_BRIGHTNESS = 14;
	public static final int SWITCH_TYPE_BLUETOOTH = 15;
	public static final int SWITCH_TYPE_BATTERY = 16;
	public static final int SWITCH_TYPE_AUTO_SYNC = 17;
	public static final int SWITCH_TYPE_AUTO_ROTATE = 18;
	public static final int SWITCH_TYPE_AIRPLANE_MODE = 19;
	public static final int SWITCH_TYPE_GPRS = 20; // 统一了SWITCH_TYPE_NORMAL_GPRS 和 SWITCH_TYPE_MTK_GPRS
	
	public static final int SWITHC_TYPE_FLASHLIGHT = 22;
	public static final int SWITCH_TYPE_SETTING = 23; //设置
	public static final int SWITCH_TYPE_BACKGROUND = 24; //设置
	
	public static final int SWITCH_TYPE_TEMP_UNIT = 25; //温标单位设置
	
	public void switchState();
	
	public void broadCastState();
	
	public void cleanUp();
	
	public int getSwitchType();
	
}
