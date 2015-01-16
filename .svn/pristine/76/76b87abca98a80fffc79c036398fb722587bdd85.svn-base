package com.jiubang.goscreenlock.theme.cjpcardcool.switcher;

import android.content.Context;
import android.util.Log;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler.ISwitcherable;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.R;

/**
 * 各个开关状态改变广播标语
 * 
 * @author oulingmei
 */
public class BroadcastBean {
	public static final String SWITCH_NOTIFY_ACTION = Constant.THEME_PACKAGE_NAME
			+ ".switch";
	public static final String SWITCH_STATE = "switch_state";
	public static final String SWITCH_TYPE = "switch_type";

	public final static String STATUS1 = "STATUS1";
	public final static String STATUS2 = "STATUS2";
	
	public final static int 	REQUEST_FIRST = 0;
	public final static int 	REQUEST_OTHER = 1;

	public final static String WIFI_CHANGE = Constant.THEME_NAME + "_switch_wifi_change";
	public final static String BLUE_TOOTH_CHANGE = Constant.THEME_NAME + "_switch_blue_tooth_change";
	public final static String AIRPLANE_CHANGE = Constant.THEME_NAME + "_switch_airplane_change";
	public final static String AUTO_ROTATE_CHANGE = Constant.THEME_NAME + "_switch_auto_rotate_change";
	public final static String AUTO_SYNC_CHANGE = Constant.THEME_NAME + "_switch_auto_sync_change";
	public final static String BRIGHTNESS_CHANGE = Constant.THEME_NAME + "_switch_brightness_change";
	public final static String GPRS_CHANGE = Constant.THEME_NAME + "_switch_gprs_change";
	public final static String GPS_CHANGE = Constant.THEME_NAME + "_switch_gps_change";
	public final static String RINGER_CHANGE = Constant.THEME_NAME + "_switch_ringer_change";
	public final static String VIBRATE_CHANGE = Constant.THEME_NAME + "_switch_vibrate_change";
	public final static String TIMEOUT_CHANGE = Constant.THEME_NAME + "_switch_timeout_change";
	public final static String LOCK_SCREEN_CHANGE = Constant.THEME_NAME + "_switch_lock_screen_change";
	public final static String BATTERY_CHANGE = Constant.THEME_NAME + "_switch_battery_change";
	public final static String SD_MOUNT_CHANGE = Constant.THEME_NAME + "_switch_sd_mount_change"; // sd最大容量
	public final static String SD_VOLUME_MOUNT_CHANGE = Constant.THEME_NAME + "_switch_sd_volume_mount_change"; // sd挂载与否
	// public final static String CANCEL_LOCK_SCREEN = Constant.THEME_NAME + "_cancel_lock_screen";
	public final static String WIFI_AP_CHANGE = Constant.THEME_NAME + "_switch_wifi_ap_change";
	// 触动反馈，4g，手电筒
	public final static String HAPTIC_FEEDBACK_CHANGE = Constant.THEME_NAME + "_haptic_feedback_change";
	public final static String MOBILE_NET_4G = Constant.THEME_NAME + "_mobile_net_4g_change";
	public final static String FLASH_LIGHT = Constant.THEME_NAME + "_flash_light_change";

	public final static String REBOOT = Constant.THEME_NAME + "_switch_reboot_change";

	public final static String EXTRA_HAPTICFEEDBACK_STATE = "EXTRA_HAPTICFEEDBACK_STATE";
	/**
	 * 用于循环，顺序与开关的id号顺序相配
	 */
	public final static String[] BROADCASTS = { WIFI_CHANGE, GPRS_CHANGE,
			GPS_CHANGE, BLUE_TOOTH_CHANGE, AIRPLANE_CHANGE, AUTO_ROTATE_CHANGE,
			AUTO_SYNC_CHANGE, BRIGHTNESS_CHANGE, RINGER_CHANGE, VIBRATE_CHANGE,
			TIMEOUT_CHANGE, LOCK_SCREEN_CHANGE, BATTERY_CHANGE,
			SD_MOUNT_CHANGE, SD_VOLUME_MOUNT_CHANGE, WIFI_AP_CHANGE,
			HAPTIC_FEEDBACK_CHANGE, FLASH_LIGHT }; // , MOBILE_NET_4G,

	public final static String FAILED_WIFI = Constant.THEME_NAME + "_failed_wifi_change";
	public final static String FAILED_APN = Constant.THEME_NAME + "_failed_apn_change";
	public final static String FAILED_BLUTH = Constant.THEME_NAME + "_failed_bluth_change";
	public final static String FAILED_RING = Constant.THEME_NAME + "_failed_ring_change";

	public final static String CLICKABLE_WIFI = Constant.THEME_NAME + "_failed_wifi_clickable";
	public final static String CLICKABLE_APN = Constant.THEME_NAME + "_failed_apn_clickable";
	public final static String CLICKABLE_BLUTH = Constant.THEME_NAME + "_failed_bluth_clickable";
	public final static String CLICKABLE_RING = Constant.THEME_NAME + "_failed_ring_clickable";
	
	public final static String getName(Context context, int switchType) {
		switch (switchType) {
		case ISwitcherable.SWITCH_TYPE_AIRPLANE_MODE:
			return context.getResources().getString(R.string.switch_airplane_mode);
		case ISwitcherable.SWITCH_TYPE_AUTO_ROTATE:
			return context.getResources().getString(R.string.switch_auto_rotate);
		case ISwitcherable.SWITCH_TYPE_AUTO_SYNC:
			return context.getResources().getString(R.string.switch_auto_sync);
		case ISwitcherable.SWITCH_TYPE_BATTERY:
			return context.getResources().getString(R.string.switch_battery);
		case ISwitcherable.SWITCH_TYPE_BLUETOOTH:
			return context.getResources().getString(R.string.switch_bluetooth);
		case ISwitcherable.SWITCH_TYPE_BRIGHTNESS:
			return context.getResources().getString(R.string.switch_brightness);
		case ISwitcherable.SWITCH_TYPE_FLASHLIGHT:
			return context.getResources().getString(R.string.switch_flashlight);
		case ISwitcherable.SWITCH_TYPE_GPRS:
			return context.getResources().getString(R.string.switch_gprs);
		case ISwitcherable.SWITCH_TYPE_GPS:
			return context.getResources().getString(R.string.switch_gps);
		case ISwitcherable.SWITCH_TYPE_HAPTICFB:
			return context.getResources().getString(R.string.switch_hapticfb);
		case ISwitcherable.SWITCH_TYPE_RING:
			return context.getResources().getString(R.string.switch_sound);
		case ISwitcherable.SWITCH_TYPE_SCREEN_TIMEOUT:
			return context.getResources().getString(R.string.switch_screen_timeout);
		case ISwitcherable.SWITCH_TYPE_SDMASS:
			return null;
		case ISwitcherable.SWITCH_TYPE_SDMOUNT:
			return null;
		case ISwitcherable.SWITCH_TYPE_VIBRATE:
			return context.getResources().getString(R.string.switch_vibrate);
		case ISwitcherable.SWITCH_TYPE_WIFI:
			return context.getResources().getString(R.string.switch_wifi);
		case ISwitcherable.SWITCH_TYPE_WIFI_AP:
			return context.getResources().getString(R.string.switch_wifi_ap);
		default:
			Log.i("broadcastBean", "null is " + switchType);
			return null;
		}
	}
	
	public final static String getChange(int switchType) {
		switch (switchType) {
		case ISwitcherable.SWITCH_TYPE_AIRPLANE_MODE:
			return AIRPLANE_CHANGE;
		case ISwitcherable.SWITCH_TYPE_AUTO_ROTATE:
			return AUTO_ROTATE_CHANGE;
		case ISwitcherable.SWITCH_TYPE_AUTO_SYNC:
			return AUTO_SYNC_CHANGE;
		case ISwitcherable.SWITCH_TYPE_BATTERY:
			return BATTERY_CHANGE;
		case ISwitcherable.SWITCH_TYPE_BLUETOOTH:
			return BLUE_TOOTH_CHANGE;
		case ISwitcherable.SWITCH_TYPE_BRIGHTNESS:
			return BRIGHTNESS_CHANGE;
		case ISwitcherable.SWITCH_TYPE_GPRS:
			return GPRS_CHANGE;
		case ISwitcherable.SWITCH_TYPE_GPS:
			return GPS_CHANGE;
		case ISwitcherable.SWITCH_TYPE_HAPTICFB:
			return HAPTIC_FEEDBACK_CHANGE;
		case ISwitcherable.SWITCH_TYPE_RING:
			return RINGER_CHANGE;
		case ISwitcherable.SWITCH_TYPE_SCREEN_TIMEOUT:
			return TIMEOUT_CHANGE;
		case ISwitcherable.SWITCH_TYPE_SDMASS:
			return SD_VOLUME_MOUNT_CHANGE;
		case ISwitcherable.SWITCH_TYPE_SDMOUNT:
			return SD_MOUNT_CHANGE;
		case ISwitcherable.SWITCH_TYPE_VIBRATE:
			return VIBRATE_CHANGE;
		case ISwitcherable.SWITCH_TYPE_WIFI:
			return WIFI_CHANGE;
		case ISwitcherable.SWITCH_TYPE_WIFI_AP:
			return WIFI_AP_CHANGE;
		default:
			Log.i("broadcastBean", "null is " + switchType);
			return null;
		}
	}
	
	public final static String getFailed(int switchType) {
		switch (switchType) {
		case ISwitcherable.SWITCH_TYPE_WIFI:
			return FAILED_WIFI;
		case ISwitcherable.SWITCH_TYPE_GPRS:
			return CLICKABLE_APN;
		case ISwitcherable.SWITCH_TYPE_BLUETOOTH:
			return CLICKABLE_BLUTH;
		case ISwitcherable.SWITCH_TYPE_RING:
			return FAILED_RING;
		default:
			return null;
		}
	}
	
	public final static String getClickable(int switchType) {
		switch (switchType) {
		case ISwitcherable.SWITCH_TYPE_WIFI:
			return CLICKABLE_WIFI;
		case ISwitcherable.SWITCH_TYPE_GPRS:
			return CLICKABLE_APN;
		case ISwitcherable.SWITCH_TYPE_BLUETOOTH:
			return FAILED_BLUTH;
		case ISwitcherable.SWITCH_TYPE_RING:
			return CLICKABLE_RING;
		default:
			return null;
		}
	}
	
	/*public final static int getBrightNessIcon(int status) {
		switch (status) {
			case SwitchConstants.LIGHT_AUTO :
				return R.drawable.switch_brightness_auto;
			case SwitchConstants.LIGHT_HIGHT :
				return R.drawable.switch_brightness_high;
			case SwitchConstants.LIGHT_LOW :
				return R.drawable.switch_brightness_low;
			case SwitchConstants.LIGHT_MIDDLE :
				return R.drawable.switch_brightness_middle;
			default :
				return 0 ;
		}
	}*/
	
	/*public final static int getIcon(int switchType) {
		int res = getIconOn(switchType);
		if (res != 0) {
			return res;
		}
		return -1;
	}*/
	/*public final static int getIconOn(int switchType) {
		switch (switchType) {
		case ISwitcherable.SWITCH_TYPE_AIRPLANE_MODE:
			return R.drawable.switch_airplanemode_on;
		case ISwitcherable.SWITCH_TYPE_AUTO_ROTATE:
			return 0;
		case ISwitcherable.SWITCH_TYPE_AUTO_SYNC:
			return R.drawable.switch_sync_on;
		case ISwitcherable.SWITCH_TYPE_BATTERY:
			return 0;
		case ISwitcherable.SWITCH_TYPE_BLUETOOTH:
			return R.drawable.switch_bluetooth_on;
		case ISwitcherable.SWITCH_TYPE_BRIGHTNESS:
			Log.i("broadcastBean", "switch_brightness_auto");
			return R.drawable.switch_brightness_auto;
		case ISwitcherable.SWITCH_TYPE_FLASHLIGHT:
			return R.drawable.switch_flashlight_on;
		case ISwitcherable.SWITCH_TYPE_GPRS:
			return R.drawable.switch_gprs_on;
		case ISwitcherable.SWITCH_TYPE_GPS:
			return R.drawable.switch_gps_on;
		case ISwitcherable.SWITCH_TYPE_HAPTICFB:
			return 0;
		case ISwitcherable.SWITCH_TYPE_RING:
			return R.drawable.switch_sound_on;
		case ISwitcherable.SWITCH_TYPE_SCREEN_TIMEOUT:
			return 0;
		case ISwitcherable.SWITCH_TYPE_SDMASS:
			return 0;
		case ISwitcherable.SWITCH_TYPE_SDMOUNT:
			return 0;
		case ISwitcherable.SWITCH_TYPE_VIBRATE:
			return R.drawable.switch_vibrate;
		case ISwitcherable.SWITCH_TYPE_WIFI:
			return R.drawable.switch_wifi_on;
		case ISwitcherable.SWITCH_TYPE_WIFI_AP:
			return 0;
		default:
			Log.i("broadcastBean", "getIcon On is null" + switchType);
			return 0;
		}
		switch (switchType) {
		case ISwitcherable.SWITCH_TYPE_BLUETOOTH:
			return R.drawable.dialing_switcher_bluetooth_on;
		case ISwitcherable.SWITCH_TYPE_GPRS:
			return R.drawable.dialing_switcher_gprs_on;
		case ISwitcherable.SWITCH_TYPE_GPS:
			return R.drawable.dialing_switcher_gps_on;
		case ISwitcherable.SWITCH_TYPE_RING:
			return R.drawable.dialing_switcher_ring_on;
		case ISwitcherable.SWITCH_TYPE_WIFI:
			return R.drawable.dialing_switcher_wifi_on;
		default:
			return -1;
		}
	}*/
	
	/*public final static int getIconOff(int switchType) {
		switch (switchType) {
		case ISwitcherable.SWITCH_TYPE_AIRPLANE_MODE:
			return R.drawable.switch_airplanemode_off;
		case ISwitcherable.SWITCH_TYPE_AUTO_ROTATE:
			return 0;
		case ISwitcherable.SWITCH_TYPE_AUTO_SYNC:
			return R.drawable.switch_sync_off;
		case ISwitcherable.SWITCH_TYPE_BATTERY:
			return 0;
		case ISwitcherable.SWITCH_TYPE_BLUETOOTH:
			return R.drawable.switch_bluetooth_off;
		case ISwitcherable.SWITCH_TYPE_BRIGHTNESS:
			return 0;
		case ISwitcherable.SWITCH_TYPE_FLASHLIGHT:
			return R.drawable.switch_flashlight_off;
		case ISwitcherable.SWITCH_TYPE_GPRS:
			return R.drawable.switch_gprs_off;
		case ISwitcherable.SWITCH_TYPE_GPS:
			return R.drawable.switch_gps_off;
		case ISwitcherable.SWITCH_TYPE_HAPTICFB:
			return 0;
		case ISwitcherable.SWITCH_TYPE_RING:
			return R.drawable.switch_sound_off;
		case ISwitcherable.SWITCH_TYPE_SCREEN_TIMEOUT:
			return 0;
		case ISwitcherable.SWITCH_TYPE_SDMASS:
			return 0;
		case ISwitcherable.SWITCH_TYPE_SDMOUNT:
			return 0;
		case ISwitcherable.SWITCH_TYPE_VIBRATE:
			return R.drawable.switch_vibrate;
		case ISwitcherable.SWITCH_TYPE_WIFI:
			return R.drawable.switch_wifi_off;
		case ISwitcherable.SWITCH_TYPE_WIFI_AP:
			return 0;
		default:
			Log.i("broadcastBean", "getIcon off is null" + switchType);
			return 0;
		}
		switch (switchType) {
		case ISwitcherable.SWITCH_TYPE_BLUETOOTH:
			return R.drawable.dialing_switcher_bluetooth_off;
		case ISwitcherable.SWITCH_TYPE_GPRS:
			return R.drawable.dialing_switcher_gprs_off;
		case ISwitcherable.SWITCH_TYPE_GPS:
			return R.drawable.dialing_switcher_gps_off;
		case ISwitcherable.SWITCH_TYPE_RING:
			return R.drawable.dialing_switcher_ring_off;
		case ISwitcherable.SWITCH_TYPE_WIFI:
			return R.drawable.dialing_switcher_wifi_off;
		default:
			return -1;
		}
	}*/
}
