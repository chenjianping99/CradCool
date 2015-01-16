package com.jiubang.goscreenlock.theme.cjpcardcool;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.RootView;

/**
 * 测试界面
 * 
 * @author wanglingjun
 * 
 */
public class PreviewTheme extends Activity {
	private BroadcastReceiver mReceiver = null;
	private RootView mRootView = null;
	public static boolean sIsFull = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (Build.VERSION.SDK_INT >= 19) {
			sIsFull = true;
		}
		if (sIsFull) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		Global.hardwareAcceleratedByWindow(this);
		mRootView = new RootView(this);

		setContentView(mRootView);
		createReceive();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		onstart();
	}

	private void onstart() {
		Bundle bundle = new Bundle();
		bundle.putBoolean("isdisplaydate", true);
		bundle.putString("dateformat", "default");
		bundle.putBoolean("islocksound", false);
		bundle.putBoolean("isunlocksound", true);
		bundle.putBoolean("isquake", true);
		bundle.putInt("istime24", 0);
		bundle.putInt("call", 20);
		bundle.putInt("sms", 99);
		bundle.putInt("batterystate", 1);
		bundle.putInt("batterylevel", 50);
		bundle.putBoolean("isfullscreen", sIsFull);

		mRootView.onStart(bundle);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mRootView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mRootView.onResume();
	}

	@Override
	protected void onDestroy() {
		if (mRootView != null) {
			mRootView.onDestroy();
			mRootView = null;
		}
		this.unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		this.finish();

		super.onBackPressed();
	}

	private void createReceive() {
		// 创建广播接收器
		mReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				final String action = intent.getAction();
				if (action.equals("com.jiubang.goscreenlock.unlock")) {
					String strTheme = intent.getStringExtra("theme");
					if (strTheme != null) {
						finish();
					}
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.jiubang.goscreenlock.unlock");
		// 注册广播接收器
		this.registerReceiver(mReceiver, filter);
	}
}
