package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

/**
 * 
 * <br>
 * 类描述:{@link ISwitcherable}的工厂,创建及销毁
 * 
 * @author guoyiqing
 * @date [2013-1-8]
 */
public class SwitchHandlerFactory {

	private static final String TAG = "SwitchHandlerFactory";

	private SparseArray<ISwitcherable> mCacheMap = new SparseArray<ISwitcherable>();

	private SparseArray<ISwitcherable> mSwitcherableMap = new SparseArray<ISwitcherable>();

	private static SwitchHandlerFactory sFactory;

	private SwitchHandlerFactory() {
	}

	public static synchronized SwitchHandlerFactory getFactory() {
		if (sFactory == null) {
			sFactory = new SwitchHandlerFactory();
		}
		return sFactory;
	}

	public ISwitcherable getSwicher(Context context, int switch_type) {
		return getSwicher(context, switch_type, false);
	}

	public ISwitcherable getSwicher(Context context, int switch_type,
			boolean isPersistent) {
		ISwitcherable switcherable = null;
		if (isPersistent) {
			switcherable = mSwitcherableMap.get(switch_type);
		} else {
			switcherable = mCacheMap.get(switch_type);
		}
		if (context == null) {
			if (switcherable == null) {
				switcherable = new EmptySwitchHandler();
				mCacheMap.put(switcherable.getSwitchType(), switcherable);
			}
			return switcherable;
		}
		context = context.getApplicationContext();
		if (switcherable != null) {
			return switcherable;
		}
		switch (switch_type) {
		case ISwitcherable.SWITCH_TYPE_WIFI:
			switcherable = new WifiHandler(context);
			Log.i("SwitchHandlerFactory", "new WifiHandler");
			break;
		case ISwitcherable.SWITCH_TYPE_WIFI_AP:
			switcherable = new WifiAPHandler(context);
			break;
		case ISwitcherable.SWITCH_TYPE_SCREEN_TIMEOUT:
			switcherable = new ToggleScreenTimeoutHandler(context);
			break;
		case ISwitcherable.SWITCH_TYPE_SDMOUNT:
			// switcherable = new SDMountHandler(context);
			break;
		case ISwitcherable.SWITCH_TYPE_SDMASS:
			// switcherable = new SDMassHandler(context);
			break;
		case ISwitcherable.SWITCH_TYPE_RING:
			switcherable = new RingerHandler(context);
			Log.i("SwitchHandlerFactory", "new RingerHandler");
			break;
		case ISwitcherable.SWITCH_TYPE_VIBRATE:
			switcherable = new VibrateHandler(context);
			Log.i("SwitchHandlerFactory", "new VibrateHandler");
			break;
		case ISwitcherable.SWITCH_TYPE_REBOOT:
			// switcherable = new RebootHandler(context);
			break;
		// case ISwitcherable.SWITCH_TYPE_MTK_GPRS :
		// switcherable = new MtkGprsHandler(context);
		// break;
		case ISwitcherable.SWITCH_TYPE_LOCK_SCREEN:
			// switcherable = new LockScreenHandler(context);
			break;
		case ISwitcherable.SWITCH_TYPE_HAPTICFB:
			switcherable = new HapticFeedbackHandler(context);
			break;
		case ISwitcherable.SWITCH_TYPE_GPS:
			switcherable = new GpsHandler(context);
			Log.i("SwitchHandlerFactory", "new GpsHandler");
			break;
		case ISwitcherable.SWITCH_TYPE_NORMAL_GPRS:
			switcherable = new NormalGprsHandler(context);
			break;
		case ISwitcherable.SWITCH_TYPE_FLASHLIGHT:
			// switcherable = new FlashlightHandler(context);

			break;
		case ISwitcherable.SWITCH_TYPE_BRIGHTNESS:
			switcherable = new BrightnessHandler(context);
			Log.i("SwitchHandlerFactory", "new BrightnessHandler");
			break;
		case ISwitcherable.SWITCH_TYPE_BLUETOOTH:
			switcherable = new BlueToothHandler(context);
			Log.i("SwitchHandlerFactory", "new BlueToothHandler");
			break;
		case ISwitcherable.SWITCH_TYPE_BATTERY:
			switcherable = new BatteryHandler(context);
			Log.i("SwitchHandlerFactory", "new BatteryHandler");
			break;
		case ISwitcherable.SWITCH_TYPE_AUTO_SYNC:
			switcherable = new AutosyncHandler(context);
			Log.i("SwitchHandlerFactory", "new AutosyncHandler");
			break;
		case ISwitcherable.SWITCH_TYPE_AUTO_ROTATE:
			switcherable = new AutoRotateHandler(context);
			break;
		case ISwitcherable.SWITCH_TYPE_AIRPLANE_MODE:
			switcherable = new AirplaneModeHandler(context);
			Log.i("SwitchHandlerFactory", "new AirplaneModeHandler");
			break;
		case ISwitcherable.SWITCH_TYPE_GPRS:
			switcherable = new GprsHandler(context, isPersistent);
			Log.i("SwitchHandlerFactory", "new GprsHandler");
			break;
		default:
			switcherable = new EmptySwitchHandler();
			break;
		}
		if (switcherable != null) {
			if (isPersistent) {
				mSwitcherableMap
						.put(switcherable.getSwitchType(), switcherable);
			} else {
				mCacheMap.put(switcherable.getSwitchType(), switcherable);
			}
		}
		return switcherable;
	}

	public void destoryHandler(ISwitcherable iSwitcherable) {
		if (iSwitcherable != null) {
			destroyHandler(iSwitcherable.getSwitchType());
		}
	}

	public void destroyHandler(int handlerType) {
		try {
			ISwitcherable switcherable = mCacheMap.get(handlerType);
			if (switcherable != null) {
				switcherable.cleanUp();
			}
			mCacheMap.remove(handlerType);
		} catch (Exception e) { // 可能已判断,再反注册未知的错误
								// LogUtils.log(TAG, e);
		}
	}

	public void destroyAll() {
		try {
			if (mCacheMap != null) {
				int size = mCacheMap.size();
				for (int i = 0; i < size; i++) {
					mCacheMap.valueAt(i).cleanUp();
				}
				mCacheMap.clear();
			}
		} catch (Exception e) {
			// LogUtils.log(TAG, e);
		}
	}

	public void destroyPersistentHandler() {
		try {
			if (mSwitcherableMap != null) {
				int size = mSwitcherableMap.size();
				for (int i = 0; i < size; i++) {
					mSwitcherableMap.valueAt(i).cleanUp();
				}
				mSwitcherableMap.clear();
			}
		} catch (Exception e) {
			// LogUtils.log(TAG, e);
		}
	}
}
