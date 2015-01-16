package com.jiubang.goscreenlock.theme.cjpcardcool.switcher;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler.FlashTorchSurface;
import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler.ISwitcherable;
import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler.SwitchHandlerFactory;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;

/**
 * 
 * @author shenyaobin
 * 
 */
public class SwitcherService extends Service {
	ISwitcherable mGprsISwitcherable;
	ISwitcherable mWifiISwitcherable;
	ISwitcherable mGpsISwitcherable;
	ISwitcherable mLightISwithcerable;
	ISwitcherable mBluetoothISwitcherable;
	ISwitcherable mAirModeISwitcherable;
	ISwitcherable mRingISwitcherable;
	BroadcastReceiver mReceiver;

	public static boolean sIsFlashlightOpen = false;
	FlashTorchSurface mFlashTorchSurface = null;
	private WindowManager mWindowManager;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 拿到所有的开关控制
		mGprsISwitcherable = SwitchHandlerFactory.getFactory().getSwicher(this,
				ISwitcherable.SWITCH_TYPE_GPRS);
		mWifiISwitcherable = SwitchHandlerFactory.getFactory().getSwicher(this,
				ISwitcherable.SWITCH_TYPE_WIFI);
		mGpsISwitcherable = SwitchHandlerFactory.getFactory().getSwicher(
				this, ISwitcherable.SWITCH_TYPE_GPS);
		mLightISwithcerable = SwitchHandlerFactory.getFactory().getSwicher(this,
				ISwitcherable.SWITCH_TYPE_FLASHLIGHT);
		mBluetoothISwitcherable = SwitchHandlerFactory.getFactory().getSwicher(
				this, ISwitcherable.SWITCH_TYPE_BLUETOOTH);
		mAirModeISwitcherable = SwitchHandlerFactory.getFactory().getSwicher(
				this, ISwitcherable.SWITCH_TYPE_AIRPLANE_MODE);
		mRingISwitcherable = SwitchHandlerFactory.getFactory().getSwicher(
				this, ISwitcherable.SWITCH_TYPE_RING);

		mWindowManager = (WindowManager) getApplicationContext()
				.getSystemService(WINDOW_SERVICE); // 获取WindowManager
		// 添加手电筒所需要的SurfaceView
		mFlashTorchSurface = new FlashTorchSurface(this);
		// 由于会在SurfaceView中进行预览，所以将背景色设置为黑色，使用户看不到
		mFlashTorchSurface.setBackgroundColor(Color.BLACK);
		WindowManager.LayoutParams mCallViewParams = new WindowManager.LayoutParams(); // 设置LayoutParams(全局变量）相关参数
		mCallViewParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		mCallViewParams.format = PixelFormat.RGBA_8888; // 设置透明背景
		mCallViewParams.width = 1;
		mCallViewParams.height = 1;
		mCallViewParams.gravity = Gravity.RIGHT | Gravity.TOP;
		mCallViewParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE; // 让当前View失去焦点
		mWindowManager.addView(mFlashTorchSurface, mCallViewParams);

		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(BroadcastBean.SWITCH_NOTIFY_ACTION);
		mFilter.addAction(Constant.ACTION_GOLOCKER_UNLOCK);
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (Constant.ACTION_GOLOCKER_UNLOCK.equals(intent.getAction())) {
					stopSelf();
				} else {
					Bundle b = intent.getExtras();
					if (b != null) {
						try {
							int code = b.getInt(BroadcastBean.SWITCH_STATE);
							switch (code) {
							case 0:
								if (mGprsISwitcherable != null) {
									mGprsISwitcherable.broadCastState();
								}
								if (mWifiISwitcherable != null) {
									mWifiISwitcherable.broadCastState();
								}
								if (mLightISwithcerable != null) {
									mLightISwithcerable.broadCastState();
								}
								if (mGpsISwitcherable != null) {
									mGpsISwitcherable.broadCastState();
								}
								if (mBluetoothISwitcherable != null) {
									mBluetoothISwitcherable.broadCastState();
								}
								if (mAirModeISwitcherable != null) {
									mAirModeISwitcherable.broadCastState();
								}
								if (mRingISwitcherable != null) {
									mRingISwitcherable.broadCastState();
								}
								break;
	
							case 1:
								int switcherType = b
										.getInt(BroadcastBean.SWITCH_TYPE);
								switch (switcherType) {
								case ISwitcherable.SWITCH_TYPE_GPRS:
									if (mGprsISwitcherable != null) {
										mGprsISwitcherable.switchState();
									}
									break;
								case ISwitcherable.SWITCH_TYPE_GPS:
									if (mGpsISwitcherable != null) {
										mGpsISwitcherable.switchState();
									}
									break;
								case ISwitcherable.SWITCH_TYPE_WIFI:
									if (mWifiISwitcherable != null) {
										mWifiISwitcherable.switchState();
									}
									break;
								case ISwitcherable.SWITCH_TYPE_BLUETOOTH:
									if (mBluetoothISwitcherable != null) {
										mBluetoothISwitcherable.switchState();
									}
									break;
								case ISwitcherable.SWITCH_TYPE_AIRPLANE_MODE:
									if (mAirModeISwitcherable != null) {
										mAirModeISwitcherable.switchState();
									}
									break;
								case ISwitcherable.SWITCH_TYPE_RING:
									if (mRingISwitcherable != null) {
										mRingISwitcherable.switchState();
									}
									break;
								case ISwitcherable.SWITCH_TYPE_FLASHLIGHT:
									if (!sIsFlashlightOpen) {
										sIsFlashlightOpen = mFlashTorchSurface
												.startFlashTorch();
									} else {
										sIsFlashlightOpen = false;
										mFlashTorchSurface.stopFlashTorch();
									}
								default:
									break;
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				}
			}
		};
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mReceiver);
		sIsFlashlightOpen = false;
		try {
			if (mGprsISwitcherable != null) {
				mGprsISwitcherable.cleanUp();
			}
			if (mWifiISwitcherable != null) {
				mWifiISwitcherable.cleanUp();
			}
			if (mGpsISwitcherable != null) {
				mGpsISwitcherable.cleanUp();
			}
			if (mLightISwithcerable != null) {
				mLightISwithcerable.cleanUp();
			}
			if (mBluetoothISwitcherable != null) {
				mBluetoothISwitcherable.cleanUp();
			}
			if (mAirModeISwitcherable != null) {
				mAirModeISwitcherable.cleanUp();
			}
			if (mRingISwitcherable != null) {
				mRingISwitcherable.cleanUp();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			mFlashTorchSurface.stopFlashTorch();
			mWindowManager.removeView(mFlashTorchSurface);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

}
