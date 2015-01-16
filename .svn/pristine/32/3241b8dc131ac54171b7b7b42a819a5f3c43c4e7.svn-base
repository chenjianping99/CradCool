package com.jiubang.goscreenlock.theme.cjpcardcool.view.bg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ViewUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.LiveListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.bg.BgCircleCell.IDeleteCell;

/**
 * 动态背景
 * 
 * @author liangdaijian
 * 
 */
public class BgView extends View implements LiveListener, SensorEventListener,
		IDeleteCell {

	// 所有元素集合
	List<BgCircleCell> mBgCircleCells;
	Paint mPaint;
	//SensorManager mSensorManager;
	private float mLastX, mLastY;
	Random mRandom;
	// Bitmap mBitmapNormal;
	// Bitmap mBitmapCover;
	boolean mIsUserBg;

	public static final long T_APPEAR = 2000;
	private long mTimeBegin = 0;
	private static final int[] COLORS = { 0xb24fb5e9, 0x88f3ff37, 0xafe3c4ff,
			0x9fc1ffc0, 0xb24fb5e9 };
	private static final int COLOR = 0xff7cf15f;

	private static final int MSG_DISDISAPPEAR = 0x77;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try {
				if (msg.what == MSG_DISDISAPPEAR) {
					BgCircleCell cell = (BgCircleCell) msg.obj;
					cell.activeDisappear();

				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	};

	public BgView(Context context) {
		super(context);
		/*try {
			mSensorManager = (SensorManager) context
					.getSystemService(Context.SENSOR_SERVICE);
		} catch (Exception e) {
			// TODO: handle exception
		}*/
		mBgCircleCells = new ArrayList<BgCircleCell>();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL_AND_STROKE);
		mRandom = new Random();
		// this.setBackgroundResource(R.drawable.bg);
	}

	// 第一类园，开起来很大，半径范围是50-70，最多的时候有3个同时出现
	public static final int COUNT0 = 5;
	public static final int MINR0 = ViewUtils.getPXByHeight(10);
	public static final int MAXR0 = ViewUtils.getPXByHeight(15);
	// 第二类园
	public static final int COUNT1 = 25;
	public static final int MINR1 = ViewUtils.getPXByHeight(8);
	public static final int MAXR1 = ViewUtils.getPXByHeight(10);
	// 第三类园
	public static final int COUNT2 = 5;
	public static final int MINR2 = ViewUtils.getPXByHeight(5);
	public static final int MAXR2 = ViewUtils.getPXByHeight(10);

	/**
	 * 获取某类圆的个数
	 * 
	 * @param flag
	 *            圆的类别
	 * @return
	 */
	private int getCellCountByFlag(int flag) {
		int count = 0;
		if (mBgCircleCells != null) {
			for (int i = mBgCircleCells.size() - 1; i >= 0; i--) {
				if (mBgCircleCells.get(i).getmFlag() == flag) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 添加园
	 * 
	 * @param flag
	 *            类别
	 * @param isShow
	 *            是否马上展示，主要是初始化的时候，一开始就有一些园出现
	 */
	private void addCellByLevel(int flag, boolean isShow) {
		int ra = 0;
		int color = 0;
		int alpha = 0;
		if (isShow) {
			alpha = (255 >> 1) + Math.abs(mRandom.nextInt() % (255 >> 1));
		}
		int count = getCellCountByFlag(flag);
		// color = COLORS[mRandom.nextInt(COLORS.length)];
		color = COLOR;
		switch (flag) {
		case 0:
			if (count >= COUNT0) {
				return;
			}
			ra = Global.dip2px(MINR0
					+ Math.abs(mRandom.nextInt() % (MAXR0 - MINR0)));
			break;
		case 1:
			if (count >= COUNT1) {
				return;
			}
			ra = Global.dip2px(MINR1
					+ Math.abs(mRandom.nextInt() % (MAXR1 - MINR1)));
			break;
		case 2:
			if (count >= COUNT2) {
				return;
			}
			ra = Global.dip2px(MINR2
					+ Math.abs(mRandom.nextInt() % (MAXR2 - MINR2)));
			break;
		default:
			return;
		}
		int left = Math.abs(mRandom.nextInt()) % (Constant.sRealWidth - ra);
		int right = left + (ra << 1);
		int top = Math.abs(mRandom.nextInt()) % (Constant.sRealHeight - ra);
		int bottom = top + (ra << 1);
		BgCircleCell result = new BgCircleCell(new RectF(left, top, right,
				bottom), color, alpha, true, mRandom, this, flag);
		// 园创建好了，激活出现过程和随机游走过程
		result.activeAppear();
		result.activeRandom();
		Message mag = mHandler.obtainMessage();
		mag.what = MSG_DISDISAPPEAR;
		mag.obj = result;
		// 园创建好了，6-12秒内激活消失过程
		mHandler.sendMessageDelayed(mag,
				6000 + Math.abs(mRandom.nextInt()) % 6000);
		if (mBgCircleCells != null) {
			mBgCircleCells.add(result);
		}
	}

	Matrix mMatrix;

	@Override
	public void onStart() {
		// Global.LogI("onStart");
		/*
		 * mBitmapNormal = Global.loadBitmapByPath(getContext().getFilesDir() +
		 * CropImageActivity.TOPJPG, RootView.sRealWidth, RootView.sRealHeight);
		 * mIsUserBg = true; if (mBitmapNormal == null) { mIsUserBg = false;
		 * mBitmapNormal = Global.getCurrBgBitmap(getContext()); mBitmapNormal =
		 * BitmapManager.getInstance(getContext())
		 * .decodeSampledBitmapFromResource(getContext().getResources(),
		 * R.drawable.bg, RootView.sRealWidth, RootView.sRealHeight); }
		 * mBitmapCover =
		 * BitmapManager.getInstance(getContext()).decodeSampledBitmapFromResource
		 * ( getContext().getResources(), R.drawable.bg2, RootView.sRealWidth,
		 * RootView.sRealHeight);
		 */
		mMatrix = new Matrix();
		mTimeBegin = System.currentTimeMillis();
		int count = (COUNT0 + COUNT1 + COUNT2) / 3;
		for (int i = 0; i < count; i++) {
			addCellByLevel(Math.abs(mRandom.nextInt()) % 3, true);
		}
	}

	@Override
	public void onResume() {
		mCoverAlpha = 255;
		if (mBgCircleCells != null) {
			// 亮屏，接着上次灭屏的时间点，继续跑物理过程
			for (int i = mBgCircleCells.size() - 1; i >= 0; i--) {
				mBgCircleCells.get(i).resetTime();
				mBgCircleCells.get(i).setmIsVisiable(true);
			}
		}
		/*if (mSensorManager != null) {
			try {
				mSensorManager.registerListener(this, mSensorManager
						.getDefaultSensor(Sensor.TYPE_ORIENTATION),
						SensorManager.SENSOR_DELAY_GAME);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}*/
		mIsRef = true;
		invalidate();
	}

	// Bitmap mTemp;
	@Override
	public void onPause() {
		// Global.LogI("onPause");
		mIsRef = false;
		/*
		 * try { if (!mIsUserBg) { Bitmap result =
		 * Global.getCurrBgBitmap(getContext()); if (result != null) { if (mTemp
		 * != null) { if (!mTemp.isRecycled()) { mTemp.recycle(); } mTemp =
		 * null; } mBitmapNormal = result; mTemp = result; result = null; } } }
		 * catch (Exception e) { // TODO: handle exception }
		 */

		if (mBgCircleCells != null) {
			for (int i = mBgCircleCells.size() - 1; i >= 0; i--) {
				mBgCircleCells.get(i).setmIsVisiable(false);
			}
		}
		/*if (mSensorManager != null) {
			try {
				mSensorManager.unregisterListener(this);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}*/
	}

	@Override
	public void onDestroy() {
		// Global.LogI("onDestroy");
		// 设置上次取图片时间为-1，防止静态变量保存起来在一分钟内起来，没有去加载图片
		/*
		 * Global.s_TIME_LASTMINUTE = -1; if (mTemp != null) { if
		 * (!mTemp.isRecycled()) { mTemp.recycle(); } mTemp = null; }
		 * 
		 * if (mBitmapNormal != null) { if (!mBitmapNormal.isRecycled()) {
		 * mBitmapNormal.recycle(); } mBitmapNormal = null; } if (mBitmapCover
		 * != null) { if (!mBitmapCover.isRecycled()) { mBitmapCover.recycle();
		 * } mBitmapCover = null; }
		 */

		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
		if (mBgCircleCells != null) {
			mBgCircleCells.clear();
			mBgCircleCells = null;
		}
		mPaint = null;
	}

	/***
	 * 给定运动时间，减速的运动
	 * 
	 * @param startS
	 *            始点
	 * @param endS
	 *            终点
	 * @param totalTime
	 *            总的动画时间
	 * @param mTotalTime
	 *            实时时间
	 * @return 当前位置
	 */
	private float deccele(float startS, float endS, long totalTime,
			long currTime) {
		if (currTime < 0) {
			return startS;
		}
		final float accele = (endS - startS) * 2 / (totalTime * totalTime);
		final float maxV = accele * totalTime;
		float currS;
		if (currTime <= totalTime) {
			currS = startS + maxV * currTime - accele * currTime
					* (currTime >> 1);
		} else {
			currS = endS;
		}
		return currS;
	}

	int mCoverAlpha = 255;
	long mAlphaBegin = 0;
	int mFromAlpha, mToAlpha;
	int mT;

	public void setmCoverAlpha(int mCoverAlpha) {
		this.mCoverAlpha = mCoverAlpha;
		if (mCoverAlpha > 255) {
			mCoverAlpha = 255;
		} else if (mCoverAlpha < 0) {
			mCoverAlpha = 0;
		}
	}

	/*public void goAlpha(int toAlpha, int t) {
		mFromAlpha = mCoverAlpha;
		mToAlpha = toAlpha;
		mT = t;
		mAlphaBegin = System.currentTimeMillis();
		invalidate();
	}*/

	private boolean mIsRef;

	@Override
	protected void onDraw(Canvas canvas) {
		try {
			/*
			 * if (MainContentView.sMainParams != null) { //
			 * 背景设置裁剪区域大小，主要看主面板的属性 canvas.clipRect(0, 0, RootView.sRealWidth,
			 * RootView.sRealHeight + MainContentView.sMainParams.topMargin); }
			 */
			if (mAlphaBegin == 0) {

			} else {
				long t = System.currentTimeMillis() - mAlphaBegin;
				mCoverAlpha = (int) deccele(mFromAlpha, mToAlpha, mT, t);
				if (t >= mT) {
					mAlphaBegin = 0;
				}
			}
			if (mCoverAlpha > 255) {
				mCoverAlpha = 255;
			} else if (mCoverAlpha < 0) {
				mCoverAlpha = 0;
			}
			/*
			 * mPaint.setAlpha(255); mMatrix.setScale((float)
			 * RootView.sRealWidth / mBitmapCover.getWidth(), (float)
			 * RootView.sRealHeight / mBitmapCover.getHeight());
			 * canvas.drawBitmap(mBitmapCover, mMatrix, mPaint); if
			 * (mBitmapNormal != null && !mBitmapNormal.isRecycled()) {
			 * mPaint.setAlpha(mCoverAlpha); mMatrix.setScale((float)
			 * RootView.sRealWidth / mBitmapNormal.getWidth(), (float)
			 * RootView.sRealHeight / mBitmapNormal.getHeight());
			 * canvas.drawBitmap(mBitmapNormal, mMatrix, mPaint); }
			 */
			long t = System.currentTimeMillis() - mTimeBegin;
			if (t > T_APPEAR) {
				// 每隔两秒中，尝试添加圆
				mTimeBegin = System.currentTimeMillis();
				int count = 1 + (Math.abs(mRandom.nextInt()) % ((COUNT0
						+ COUNT1 + COUNT2) / 3));
				for (int i = 0; i < count; i++) {
					addCellByLevel(Math.abs(mRandom.nextInt()) % 3, false);
				}
				// 每隔两秒中，随机尝试让大园抖动
				for (int i = mBgCircleCells.size() - 1; i >= 0; i--) {
					if (mBgCircleCells.get(i).getmFlag() == 0) {
						if (mRandom.nextBoolean()
								&& mBgCircleCells.get(i).getmShakeTimeLast() == 0) {
							mBgCircleCells.get(i).activeShake();
						}
					}
				}
			}
			for (int i = mBgCircleCells.size() - 1; i >= 0; i--) {
				mBgCircleCells.get(i).draw(canvas, mPaint);
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		if (mIsRef) {
			// invalidate();
			for (BgCircleCell cell : mBgCircleCells) {
				invalidate((int) cell.getmRectF().left,
						(int) cell.getmRectF().top,
						(int) cell.getmRectF().right,
						(int) cell.getmRectF().bottom);
			}
		}
	}

	private static final float TYPE_GRAVITY_LENGTH_OK = 15.0f;
	private static final float TYPE_GRAVITY_LENGTH = Global.dip2px(0.5f);

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			// 获得x、y轴的数值
			float thisX = -event.values[2] - mLastX;
			float thisY = -event.values[1] - mLastY;

			// XXX 重力反转
			thisX = -thisX;
			thisY = -thisY;

			// x轴
			if (Math.abs(thisX) >= TYPE_GRAVITY_LENGTH_OK) {
				if (thisX > TYPE_GRAVITY_LENGTH) {
					thisX = TYPE_GRAVITY_LENGTH;
				} else if (thisX < -TYPE_GRAVITY_LENGTH) {
					thisX = -TYPE_GRAVITY_LENGTH;
				}
				// 激活所有的x轴的传感
				if (mBgCircleCells != null) {
					for (int i = mBgCircleCells.size() - 1; i >= 0; i--) {
						mBgCircleCells.get(i).activeSensorX(
								thisX * (mRandom.nextFloat() / 2 + 1) / 12);
					}
				}
				mLastX = -event.values[2];
				// mLastY = -event.values[1];
			}

			// y轴
			if (Math.abs(thisY) >= TYPE_GRAVITY_LENGTH_OK) {
				if (thisY > TYPE_GRAVITY_LENGTH) {
					thisY = TYPE_GRAVITY_LENGTH;
				} else if (thisY < -TYPE_GRAVITY_LENGTH) {
					thisY = -TYPE_GRAVITY_LENGTH;
				}
				// 激活所有的y轴的传感
				if (mBgCircleCells != null) {
					for (int i = mBgCircleCells.size() - 1; i >= 0; i--) {
						mBgCircleCells.get(i).activeSensorY(
								thisY * (mRandom.nextFloat() / 2 + 1) / 12);
					}
				}
				// mLastX = -event.values[2];
				mLastY = -event.values[1];
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(BgCircleCell cell) {
		// TODO Auto-generated method stub
		if (mBgCircleCells != null && mBgCircleCells.contains(cell)) {
			mBgCircleCells.remove(cell);
		}
	}

}
