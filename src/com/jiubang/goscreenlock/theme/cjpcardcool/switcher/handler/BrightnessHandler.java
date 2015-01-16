package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;

/**
 * <br>类描述:屏幕亮度开关器
 * <br>注意:从SwitchWidget抽离调整可以使用的代码
 * 
 * @author guoyiqing
 * @date  [2013-1-5]
 */
class BrightnessHandler implements ISwitcherable {

	private final static int LIGHT_AUTO = -1;
	private final static int LIGHT_LOW = 25;
	private final static int LIGHT_MIDDLE = 150;
	private final static int LIGHT_HIGHT = 255;
	private static final String TAG = "BrightnessHandler";
	private Context mContext;
	private int[] mBrightnessNum = { LIGHT_AUTO, LIGHT_LOW, LIGHT_MIDDLE, LIGHT_HIGHT };
	private int mIndex = 0;
	private Uri mUri = android.provider.Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
	private Uri mUriAuto;
	private BrightnessObserver mBrightnessObserver;

	public BrightnessHandler(Context context) {
		mContext = context;
		if (Build.VERSION.SDK_INT >= 8) {
			mUriAuto = android.provider.Settings.System
					.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
		} else {
			mUriAuto = android.provider.Settings.System.getUriFor("screen_brightness_mode");
		}

		boolean isAutoBrightness = isAutoBrightness(mContext.getContentResolver());
		if (isAutoBrightness) {
			mIndex = 0;
		} else {
			int i = getScreenBrightness(mContext);
			if (i >= LIGHT_HIGHT) {
				mIndex = 3;
			} else if (i >= LIGHT_MIDDLE) {
				mIndex = 2;
			} else {
				mIndex = 1;
			}
		}
		Handler handler = new Handler();
		mBrightnessObserver = new BrightnessObserver(handler);
		context.getContentResolver().registerContentObserver(mUri, false, mBrightnessObserver);
		context.getContentResolver().registerContentObserver(mUriAuto, false, mBrightnessObserver);
	}

	/**
	 * 是否系统自动调节亮度
	 * 
	 * @param aContext
	 * @return
	 */
	public boolean isAutoBrightness(ContentResolver aContentResolver) {
		boolean automicBrightness = false;
		try {
			if (Build.VERSION.SDK_INT >= 8) {
				automicBrightness = Settings.System.getInt(aContentResolver,
						Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
			} else {
				automicBrightness = Settings.System.getInt(aContentResolver,
						"screen_brightness_mode") == 1;
			}

		} catch (SettingNotFoundException e) {
//			LogUtils.log(TAG, e);
		}
		return automicBrightness;
	}

	/**
	 * 停止系统自动调节亮度
	 */
	public void stopAutoBrightness(Context context) {
		if (Build.VERSION.SDK_INT >= 8) {
			Settings.System.putInt(context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE,
					Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		} else {
			Settings.System.putInt(context.getContentResolver(), "screen_brightness_mode", 0);
		}
		context.getContentResolver().notifyChange(mUriAuto, null);
	}

	/**
	 * 启动系统自动调节亮度
	 */
	public void startAutoBrightness(Context context) {
		if (Build.VERSION.SDK_INT >= 8) {
			Settings.System.putInt(context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE,
					Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
		} else {
			Settings.System.putInt(context.getContentResolver(), "screen_brightness_mode", 1);
		}
		context.getContentResolver().notifyChange(mUriAuto, null);
	}

	/**
	 * 调节系统亮度
	 */
	public void saveBrightness(ContentResolver resolver, int brightness) {
		android.provider.Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS,
				brightness);
		resolver.notifyChange(mUri, null);
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
//			LogUtils.log(TAG, e);
		}
		return nowBrightnessValue;
	}

	/**
	 * 设置屏幕亮度
	 */
	public void setBrightness(Context context, int brightness) {
		Intent intent = new Intent(context, BrightnessSettingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 
	 * <br>类描述:
	 * 
	 * @date  [2013-1-5]
	 */
	class BrightnessObserver extends ContentObserver {

		public BrightnessObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			boolean isAutoBrightness = isAutoBrightness(mContext.getContentResolver());
			if (isAutoBrightness) {
				mIndex = 0;
			} else {
				int i = getScreenBrightness(mContext);
				if (i >= 255) {
					mIndex = 3;
				} else if (i >= 125) {
					mIndex = 2;
				} else {
					mIndex = 1;
				}
			}
			broadCastState();
		}
	}

	@Override
	public void switchState() {
		boolean isAutoBrightness = isAutoBrightness(mContext.getContentResolver());
		if (isAutoBrightness) {
			stopAutoBrightness(mContext);
			saveBrightness(mContext.getContentResolver(), mBrightnessNum[1]);
			setBrightness(mContext, mBrightnessNum[1]);
		} else {
			int i = getScreenBrightness(mContext);
			if (i >= LIGHT_HIGHT) {
				startAutoBrightness(mContext);
			} else if (i >= LIGHT_MIDDLE) {
				saveBrightness(mContext.getContentResolver(), mBrightnessNum[3]);
				setBrightness(mContext, mBrightnessNum[3]);
			} else {
				saveBrightness(mContext.getContentResolver(), mBrightnessNum[2]);
				setBrightness(mContext, mBrightnessNum[2]);
			}
		}
	}

	@Override
	public void broadCastState() {
		Intent intent = new Intent(BroadcastBean.BRIGHTNESS_CHANGE);
		if (mIndex == 0) {
			//			intent.setFlags(SwitchConstants.LIGHT_AUTO);
			intent.putExtra(BroadcastBean.STATUS1, SwitchConstants.LIGHT_AUTO);
		} else if (mIndex == 1) {
			//			intent.setFlags(SwitchConstants.LIGHT_LOW);
			intent.putExtra(BroadcastBean.STATUS1, SwitchConstants.LIGHT_LOW);
		} else if (mIndex == 2) {
			//			intent.setFlags(SwitchConstants.LIGHT_MIDDLE);
			intent.putExtra(BroadcastBean.STATUS1, SwitchConstants.LIGHT_MIDDLE);
		} else if (mIndex == 3) {
			//			intent.setFlags(SwitchConstants.LIGHT_HIGHT);
			intent.putExtra(BroadcastBean.STATUS1, SwitchConstants.LIGHT_HIGHT);
		}
		mContext.sendBroadcast(intent);
	}

	@Override
	public void cleanUp() {
		if (mBrightnessObserver != null) {
			mContext.getContentResolver().unregisterContentObserver(mBrightnessObserver);
			mBrightnessObserver = null;
		}
	}

	@Override
	public int getSwitchType() {
		return SWITCH_TYPE_BRIGHTNESS;
	}
}
