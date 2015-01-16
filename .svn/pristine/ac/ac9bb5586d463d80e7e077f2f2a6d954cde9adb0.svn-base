package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.WindowManager;

/**
 * 
 * <br>功能详细描述:从SwitchWidget抽离调整可以使用的代码
 * 
 * @date  [2013-1-5]
 */
public class BrightnessSettingActivity extends Activity {

	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int brightness = getScreenBrightness(this);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = Float.valueOf(brightness / 255.0f);
		getWindow().setAttributes(lp);
		initHandler();
		mHandler.sendEmptyMessageDelayed(1, 10);
	}

	private void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				finish();
			}
		};
	}

	/**
	 * 获取现在屏幕亮度
	 */
	public int getScreenBrightness(Context context) {

		int nowBrightnessValue = 0;
		ContentResolver resolver = context.getContentResolver();
		try {
			nowBrightnessValue = android.provider.Settings.System.getInt(resolver,
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowBrightnessValue;
	}
}
