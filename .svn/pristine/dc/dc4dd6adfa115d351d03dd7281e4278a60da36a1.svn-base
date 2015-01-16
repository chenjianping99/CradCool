package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;

/**
 * 
 * <br>类描述:蓝牙开关器
 * <br>注意:从SwitchWidget抽离调整可以使用的代码
 * 
 * @author guoyiqing
 * @date  [2013-1-5]
 */
class BlueToothHandler implements ISwitcherable
{

	private BluetoothAdapter	mAdapter;
	private BlueToothReceiver	mReciver;
	private Context				mContext;
	private final static int	ON			= 1;
	private final static int	OFF			= 0;
	private final static int	DISABLE		= -1;
	private final static int	AIR_DISABLE	= -2;
	private static final String	TAG			= "BlueToothHandler";

	public BlueToothHandler(Context context)
	{
		mContext = context;
		Log.i("BlueToothHandler", "context is " + (mContext == null ? "null" : "no null"));
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		IntentFilter fileFilter = new IntentFilter();
		fileFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		fileFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		mReciver = new BlueToothReceiver();
		context.registerReceiver(mReciver, fileFilter);
	}

	/**
	 * 是否打开了蓝牙
	 * 
	 * @return
	 */
	public int isOn()
	{
		if (mAdapter == null)
		{
			return DISABLE;
		}
		else if (Build.VERSION.SDK_INT < 11
				&& Settings.System.getInt(mContext.getContentResolver(),
						Settings.System.AIRPLANE_MODE_ON, 0) == 1)
		{
			return AIR_DISABLE;
		}
		else
		{
			int state = mAdapter.getState();
			if (state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_TURNING_ON)
			{
				return ON;
			}
			else
			{
				return OFF;
			}
		}
	}

	/**
	 * 发送广播到Widget前台
	 */
	public void sendBroacast()
	{
		Intent intents = new Intent(BroadcastBean.BLUE_TOOTH_CHANGE);
		int state = DISABLE;
		boolean isAirPlane = Settings.System.getInt(mContext.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) == 1 && Build.VERSION.SDK_INT < 11;
		if (isAirPlane)
		{
			state = AIR_DISABLE;
		}
		else if (mAdapter != null)
		{
			state = mAdapter.getState();
		}
		switch (state)
		{
			case BluetoothAdapter.STATE_ON :
				intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_ON);
				mContext.sendBroadcast(intents);
				break;
			case BluetoothAdapter.STATE_OFF :
				intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_OFF);
				mContext.sendBroadcast(intents);
				break;
			case DISABLE :
			case AIR_DISABLE :
				intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.BLUETOOTH_FALSE);
				mContext.sendBroadcast(intents);
				break;
			default :
				break;
		}
	}

	/**
	 * 
	 * <br>类描述:
	 * 
	 * @date  [2013-1-5]
	 */
	class BlueToothReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			broadCastState();
		}
	}

	@Override
	public void switchState()
	{
		if (mAdapter == null)
		{
			return;
		}
		
		Intent i = new Intent(BroadcastBean.CLICKABLE_BLUTH);
		mContext.sendBroadcast(i);
		
		int state = isOn();
		if (state == ON)
		{
			try
			{
				mAdapter.disable();
			}
			catch (SecurityException e)
			{
				//				LogUtils.log(TAG, e);
				/*Intent toastIntent = new Intent(GoWidgetConstant.ACTION_GO_WIDGET_TOAST);
				toastIntent.putExtra(GoWidgetConstant.EXTRA_TOAST_STRING, mContext.getResources()
						.getString(R.string.bluetooth_internal_error));
				mContext.sendBroadcast(toastIntent);*/
				try
				{
					Intent intent = new Intent(BroadcastBean.FAILED_BLUTH);
					mContext.sendBroadcast(intent);
					//					Intent intent = new Intent(Intent.ACTION_MAIN);
					//					intent.setClassName("com.android.settings",
					//							"com.android.settings.bluetooth.BluetoothSettings");
					//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					//					mContext.startActivity(intent);
				}
				catch (ActivityNotFoundException ex)
				{
					//					LogUtils.log(TAG, e);
				}
			}
		}
		else if (state == OFF)
		{
			mAdapter.enable();
		}
		else if (state == AIR_DISABLE)
		{
			sendBroacast();
			// 提示因为飞行模式，不能控制蓝牙
			/*Intent intent = new Intent(mContext, ToastActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("stringId", R.string.bluetooth_airplane_on_tips);
			mContext.startActivity(intent);*/
		}
		else
		{
			sendBroacast();
			/*Intent intent = new Intent(mContext, ToastActivity.class);
			intent.putExtra("stringId", R.string.bluetooth_no_adapter);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);*/
		}
	}

	@Override
	public void broadCastState()
	{
		Intent intents = new Intent(BroadcastBean.BLUE_TOOTH_CHANGE);
		int state = DISABLE;
		boolean isAirPlane = Settings.System.getInt(mContext.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) == 1 && Build.VERSION.SDK_INT < 11;
		if (isAirPlane)
		{
			state = AIR_DISABLE;
		}
		else if (mAdapter != null)
		{
			state = mAdapter.getState();
		}
		switch (state)
		{
			case BluetoothAdapter.STATE_ON :
				intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_ON);
				mContext.sendBroadcast(intents);
				break;
			case BluetoothAdapter.STATE_OFF :
				intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.STATUS_OFF);
				mContext.sendBroadcast(intents);
				break;
			case DISABLE :
			case AIR_DISABLE :
				intents.putExtra(BroadcastBean.STATUS1, SwitchConstants.BLUETOOTH_FALSE);
				mContext.sendBroadcast(intents);
				break;
			default :
				break;
		}
	}

	@Override
	public void cleanUp()
	{
		if (mReciver != null)
		{
			mContext.unregisterReceiver(mReciver);
			mReciver = null;
		}
	}

	@Override
	public int getSwitchType()
	{
		return SWITCH_TYPE_BLUETOOTH;
	}
}
