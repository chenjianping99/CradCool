package com.jiubang.goscreenlock.theme.cjpcardcool.view.bg;

import java.io.File;

import android.net.Uri;
import android.os.Environment;

import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;

/**
 * @author zhangkai 锁屏壁纸类型 0默认背景/1桌面背景/2自定义背景 mCustombg_uri 自定义背景存放的uri地址
 */
public class LockBgType {
	public static final int BG_DEFAULT = 0;
	public static final int BG_LAUNCHER = 1;
	public static final int BG_CUSTOM = 2;
	public static final int BG_GOLOCK = 3;
	public static final int BG_CUSTOM_RANDOM = 4;

	public static final String URL_BASE = getSDPath() + File.separator + "DCIM"
			+ File.separator + "GoLocker" + File.separator
			+ Constant.THEME_PACKAGE_SHORT_NAME + File.separator;
	public static final Uri CUSTOMBG_URI = Uri.parse("file://" + URL_BASE
			+ "custombg.png");
	public static final Uri GOLOCKERBG_URI = Uri.parse("file://" + URL_BASE
			+ "golockbg.png");

	public static final String CUSTOMBG_PATH = URL_BASE + "custombg.png";

	public static final String CUSTOM_RANDOM_BG_URI = "file://" + URL_BASE
			+ "custom_random/bg_in_list/";
	public static final String CUSTOM_RANDOM_BG_DIR = URL_BASE
			+ "custom_random/bg_in_list/";

	public static final String CUSTOM_GAUSS_BG_PATH = URL_BASE
			+ "custom_gause_bg.png";
	public static final Uri CUSTOM_GAUSS_BG_URI = Uri.parse("file://"
			+ CUSTOM_GAUSS_BG_PATH);

	public static final String GOLOCKER_GAUSS_BG_PATH = URL_BASE
			+ "golock_gause_bg.png";
	public static final Uri GOLOCKER_GAUSS_BG_URI = Uri.parse("file://"
			+ GOLOCKER_GAUSS_BG_PATH);

	public static final String GOLOCKER_DEFAULT_BG_PATH = URL_BASE
			+ "golock_default_bg517.png";
	public static final Uri GOLOCKER_DEFAULT_BG_URI = Uri.parse("file://"
			+ GOLOCKER_DEFAULT_BG_PATH);

	public static String getSDPath() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			return Environment.getExternalStorageDirectory().toString(); // 获取跟目录
		} else {
			return null;
		}
	}
}
