package com.jiubang.goscreenlock.theme.cjpcardcool.switcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler.ISwitcherable;
import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler.SwitchConstants;

/**
 * Switch View
 * @author zhongwenqi
 *
 */
public class SwitchView extends ImageView implements OnClickListener {

	private boolean mIsOn;
	private int mSwitchType = ISwitcherable.SWITCH_TYPE_EMPTY;
	private static final String SYSTEM_SETTING_PKG = "com.android.settings";

	private BroadcastReceiver mStateChangeReceiver;
	private BroadcastReceiver mFailedChangeReceiver;
	private BroadcastReceiver mClickableReceiver;
	private Intent mIntent;

	private Paint mPaint = new Paint();
	private String mText = null;

	public SwitchView(Context context, int switchType) {
		super(context);
		// TODO Auto-generated constructor stub
		mSwitchType = switchType;
		switch (switchType) {
		case ISwitcherable.SWITCH_TYPE_WIFI:
			mText = "Wifi";
			break;
		case ISwitcherable.SWITCH_TYPE_WIFI_AP:
			mText = "WifiAP";
			break;
		case ISwitcherable.SWITCH_TYPE_SCREEN_TIMEOUT:
			mText = "TimeOut";
			break;
		case ISwitcherable.SWITCH_TYPE_SDMOUNT:
			mText = "SDMount";
			break;
		case ISwitcherable.SWITCH_TYPE_SDMASS:
			mText = "SDMass";
			break;
		case ISwitcherable.SWITCH_TYPE_RING:
			mText = "Ring";
			break;
		case ISwitcherable.SWITCH_TYPE_VIBRATE:
			mText = "Vibrate";
			break;
		case ISwitcherable.SWITCH_TYPE_REBOOT:
			mText = "Reboot";
			break;
		case ISwitcherable.SWITCH_TYPE_MTK_GPRS:
			mText = "GPRS_MTK";
			break;
		case ISwitcherable.SWITCH_TYPE_LOCK_SCREEN:
			mText = "LockeScreen";
			break;
		case ISwitcherable.SWITCH_TYPE_HAPTICFB:
			mText = "Hapticfb";
			break;
		case ISwitcherable.SWITCH_TYPE_GPS:
			mText = "GPS";
			break;
		case ISwitcherable.SWITCH_TYPE_NORMAL_GPRS:
			mText = "GPRS_Normal";
			break;
		case ISwitcherable.SWITCH_TYPE_FLASHLIGHT:
			mText = "FlashLight";
			break;
		case ISwitcherable.SWITCH_TYPE_BRIGHTNESS:
			mText = "Brightness";
			break;
		case ISwitcherable.SWITCH_TYPE_BATTERY:
			mText = "Battery";
			break;
		case ISwitcherable.SWITCH_TYPE_BLUETOOTH:
			mText = "Bluetooth";
			break;
		case ISwitcherable.SWITCH_TYPE_AUTO_SYNC:
			mText = "AutoSync";
			break;
		case ISwitcherable.SWITCH_TYPE_AUTO_ROTATE:
			mText = "AutoRotate";
			break;
		case ISwitcherable.SWITCH_TYPE_AIRPLANE_MODE:
			mText = "AirPlane";
			break;
		case ISwitcherable.SWITCH_TYPE_GPRS:
			mText = "GPRS";
			break;
		default:
			mText = "Empty";
			break;
		}
		setOnClickListener(this);
		registerReceiver();

		postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					requestSwitch(BroadcastBean.REQUEST_FIRST);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}, 200);

		mPaint.setTextSize(30);
		mPaint.setColor(Color.BLACK);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawText(mText, 0, getHeight(), mPaint);
	}

	private String[] mTimeOut = new String[] { "15s_TO", "30s_TO", "1m_TO",
			"5m_TO", "10m_TO", "never" };
	private String[] mBrightness = new String[] { "AUTO_L", "LOW_L",
			"MIDDLE_L", "HIGHT_L" };

	private void registerReceiver() {
		// 注册返回广播
		if (BroadcastBean.getChange(mSwitchType) != null) {
			IntentFilter mFilter = new IntentFilter();
			// int length = BroadcastBean.BROADCASTS.length;
			/*
			 * String action = null; for (int i = 0; i < length; ++i) { action =
			 * BroadcastBean.BROADCASTS[i]; mFilter.addAction(action); }
			 */
			mFilter.addAction(BroadcastBean.getChange(mSwitchType));
			mStateChangeReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					Bundle b = intent.getExtras();
					if (b != null) {
						try {
							int stste = b.getInt(BroadcastBean.STATUS1);
							if (intent.getAction().equals(
									BroadcastBean.getChange(mSwitchType))) {
								if (mSwitchType == ISwitcherable.SWITCH_TYPE_SCREEN_TIMEOUT) {
									mText = mTimeOut[stste];
									SwitchView.this.invalidate();
								} else if (mSwitchType == ISwitcherable.SWITCH_TYPE_BRIGHTNESS) {
									mText = mBrightness[stste];
									SwitchView.this.invalidate();
								} else if (mSwitchType == ISwitcherable.SWITCH_TYPE_BATTERY) {
									mText = stste + "%";
									if (b.getInt(BroadcastBean.STATUS2) == 2) {
										mText = mText + "charging";
									}
									SwitchView.this.invalidate();
								} else {
									if (SwitchConstants.STATUS_ON == stste) {
										SwitchView.this
												.setBackgroundColor(Color.WHITE);
										mIsOn = true;
									} else {
										SwitchView.this
												.setBackgroundColor(0x7fffffff & Color.WHITE);
										mIsOn = false;
									}
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				}
			};
			getContext().registerReceiver(mStateChangeReceiver, mFilter);
		}
		// 注册失败打开开关广播
		if (BroadcastBean.getFailed(mSwitchType) != null) {
			IntentFilter mFilter2 = new IntentFilter();
			/*
			 * mFilter2.addAction(BroadcastBean.FAILED_WIFI);
			 * mFilter2.addAction(BroadcastBean.FAILED_APN);
			 * mFilter2.addAction(BroadcastBean.FAILED_BLUTH);
			 * mFilter2.addAction(BroadcastBean.FAILED_RING);
			 */
			mFilter2.addAction(BroadcastBean.getFailed(mSwitchType));

			mFailedChangeReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					try {
						if (intent.getAction()
								.equals(BroadcastBean.FAILED_WIFI)) {
							if (!Util.getAppName(getContext(),
									SYSTEM_SETTING_PKG,
									"com.android.settings.wifi.WifiSettings")
									.equalsIgnoreCase("none")) {
								mIntent.setClassName(SYSTEM_SETTING_PKG,
										"com.android.settings.wifi.WifiSettings");
							} else {
								mIntent.setPackage(SYSTEM_SETTING_PKG);
							}
							mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						} else if (intent.getAction().equals(
								BroadcastBean.FAILED_APN)) {
							mIntent.setPackage(SYSTEM_SETTING_PKG);
							mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						} else if (intent.getAction().equals(
								BroadcastBean.FAILED_BLUTH)) {
							if (!Util
									.getAppName(getContext(),
											SYSTEM_SETTING_PKG,
											"com.android.settings.bluetooth.BluetoothSettings")
									.equalsIgnoreCase("none")) {
								mIntent.setClassName(SYSTEM_SETTING_PKG,
										"com.android.settings.bluetooth.BluetoothSettings");
							} else {
								mIntent.setPackage(SYSTEM_SETTING_PKG);
							}
						} else if (intent.getAction().equals(
								BroadcastBean.FAILED_RING)) {
							mIntent.setPackage(SYSTEM_SETTING_PKG);
							mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						}

						if (true) {
							Util.getvibrator(getContext());
						}
						Util.sendUnlockWithIntent(getContext(), null, null,
								null, mIntent);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			};
			getContext().registerReceiver(mFailedChangeReceiver, mFilter2);
		}
		if (BroadcastBean.getClickable(mSwitchType) != null) {
			IntentFilter mFilter3 = new IntentFilter();
			/*
			 * mFilter3.addAction(BroadcastBean.CLICKABLE_WIFI);
			 * mFilter3.addAction(BroadcastBean.CLICKABLE_APN);
			 * mFilter3.addAction(BroadcastBean.CLICKABLE_BLUTH);
			 * mFilter3.addAction(BroadcastBean.CLICKABLE_RING);
			 */
			mFilter3.addAction(BroadcastBean.getClickable(mSwitchType));
			mClickableReceiver = new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					// TODO Auto-generated method stub
					if (intent.getAction().equals(
							BroadcastBean.getClickable(mSwitchType))) {
						SwitchView.this.setClickable(true);
					}
				}

			};
			getContext().registerReceiver(mClickableReceiver, mFilter3);
		}
	}

	public void onDestroy() {
		if (mStateChangeReceiver != null) {
			try {
				getContext().unregisterReceiver(mStateChangeReceiver);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		if (mFailedChangeReceiver != null) {
			try {
				getContext().unregisterReceiver(mFailedChangeReceiver);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		if (mClickableReceiver != null) {
			try {
				getContext().unregisterReceiver(mClickableReceiver);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	private void requestSwitch(int code) {
		Intent intent = new Intent(BroadcastBean.SWITCH_NOTIFY_ACTION);
		Bundle b = new Bundle();
		b.putInt(BroadcastBean.SWITCH_STATE, code);
		b.putInt(BroadcastBean.SWITCH_TYPE, mSwitchType);
		intent.putExtras(b);
		Log.i("switchView", "sendBroadcast" + code);
		getContext().sendBroadcast(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mSwitchType == ISwitcherable.SWITCH_TYPE_BATTERY) {
			/*BatteryCircs bc = new BatteryCircs();
			Util.sendUnlockWithIntent(getContext(), null, bc.getPackageName(),
					bc.getClassName(), null);*/
		} else {
			if (BroadcastBean.getClickable(mSwitchType) != null) {
				setClickable(false);
			}
			requestSwitch(BroadcastBean.REQUEST_OTHER);
			mIsOn = !mIsOn;
			setBackgroundColor((mIsOn ? 0xffffffff : 0x7fffffff) & Color.WHITE);
		}
	}

}
