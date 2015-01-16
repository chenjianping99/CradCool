package com.jiubang.goscreenlock.theme.cjpcardcool.util;

import android.util.Log;

/**
 * 
 * <br>
 * 类描述:log开关
 * 
 * @author jiangxuwen
 * @date [2013-4-17]
 */
public class LogUtils {

	private static final boolean DEBUG = true;
	private static final String DEFAULT_LOG_TAG = "ddd";

	public static void log(String tag, Exception e) {
		if (tag == null) {
			tag = DEFAULT_LOG_TAG;
		}
		if (DEBUG) {
			String msg = e.getMessage();
			if (msg == null) {
				e.printStackTrace();
			} else {
				Log.e(tag, msg);
			}
		}
	}

	public static void log(String tag, Error e) {
		if (tag == null) {
			tag = DEFAULT_LOG_TAG;
		}
		if (DEBUG) {
			String msg = e.getMessage();
			if (msg == null) {
				e.printStackTrace();
			} else {
				Log.e(tag, msg);
			}
		}
	}

	public static void log(String tag, String msg) {
		if (tag == null) {
			tag = DEFAULT_LOG_TAG;
		}
		if (DEBUG) {
			Log.d(tag, msg);
		}
	}

}
