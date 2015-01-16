package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 
 * <br>功能详细描述:从SwitchWidget抽离调整可以使用的代码
 * 
 * @date  [2013-1-5]
 */
public class FlashTorchSurface extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder = null;
	private Camera mCamera = null;
	private boolean mIsWantStart = false; // 开启手电筒为异步操作，还没初始化完成时，外部调用startFlashTorch()将设置此Flag
	public static boolean sIsOn = false;

	public FlashTorchSurface(Context context) {
		super(context);
		initHolder();
	}

	public FlashTorchSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHolder();
	}

	public FlashTorchSurface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHolder();
	}

	private boolean mWhetherTested = false;
	private boolean mIsSupportLED = true;
	public boolean hasLedFlash(Context context) {
		/*String device = Build.DEVICE;
		// System.out.println(device);
		if (device.trim().toLowerCase().equals("m9")) {
			// 对于魅族M9，直接返回没有LED闪光灯
			return false;
		}
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);*/
		if (!mWhetherTested) {
			mWhetherTested = true;
		} else {
			return mIsSupportLED;
		}

		if (!context.getPackageManager().hasSystemFeature(
				"android.hardware.camera.flash")) {
			mIsSupportLED = false;
			return false;
		}
		try {
			initCamera();
			if (mCamera != null) {
				if (Build.MODEL.equals("GT-S5830i")) {
					releaseCamera();
					return true;
				}
				if (Build.MODEL.trim().toLowerCase().equals("m9")) {
					releaseCamera();
					mIsSupportLED = false;
					return false;
				}
				List<String> localList = mCamera.getParameters()
						.getSupportedFlashModes();
				releaseCamera();
				if (localList != null) {
					boolean bool = localList.contains("torch");
					if (bool) {
						return true;
					}
				}
			}
		} catch (Exception localException) {
			Log.d("Utils", "Failed to open camera: " + localException);
		}
		return false;
	}

	private void initCamera() {
		try {
			if (mCamera == null) {
				mCamera = Camera.open();
			}
		} catch (Exception e) {
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}
	
	public boolean startFlashTorch() {
		if (mHolder == null) {
			mIsWantStart = true;
			return true;
		}

		try {
			mCamera = Camera.open();
			mCamera.setPreviewDisplay(mHolder);
		} catch (Exception e) {
			if (mCamera != null) {
				mCamera.release();
				mCamera = null;
			}
			return false;
		}

		try {
			// 防止startPreview、setParameters抛出RuntimeException异常导致崩溃
			mCamera.startPreview();
			Camera.Parameters param = mCamera.getParameters();
			param.setFlashMode(Parameters.FLASH_MODE_TORCH);
			mCamera.setParameters(param);
		} catch (RuntimeException e) {
			e.printStackTrace();
			stopFlashTorch();
			return false;
		}
		sIsOn = true;
		return true;
	}

	public void stopFlashTorch() {
		mIsWantStart = false;
		if ((mHolder == null) || (mCamera == null)) {
			return;
		}
		try {
			// 防止startPreview、setParameters抛出RuntimeException异常导致崩溃
			Camera.Parameters param = mCamera.getParameters();
			param.setFlashMode(Parameters.FLASH_MODE_OFF);
			mCamera.setParameters(param);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		} catch (RuntimeException e) {
			e.printStackTrace();
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
		sIsOn = false;
	}

	private void initHolder() {
		// 注册surfaceCreated、surfaceChanged、surfaceDestroyed回调方法
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // 在某些机型上，这一项必须设置
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// 通知 SurfaceView 被创建起来
		mHolder = holder;

		if (mIsWantStart) {
			mIsWantStart = false;
			startFlashTorch();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}
}
