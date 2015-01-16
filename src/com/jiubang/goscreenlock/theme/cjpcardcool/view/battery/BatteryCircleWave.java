package com.jiubang.goscreenlock.theme.cjpcardcool.view.battery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.jiubang.goscreenlock.theme.cjpcardcool.R;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnDestroyListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnMonitorListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnResumeListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ViewUtils;
/**
 * 
 * @author chenjianping
 *
 */
public class BatteryCircleWave extends View implements OnResumeListener,
		OnDestroyListener, OnMonitorListener {
	private Bitmap mWave1, mWave2;
	private Rect mWave1Rect;
	private Paint mPaint;
	private int mWaveX = 0, mWaveY = 0;
	private Interpolator mInterpolator = new LinearInterpolator();
	private long mAnimationStartTime = 0;

	private int mWidth = ViewUtils.getPXByWidth(103);
	private RectF mBgRectF, mCircirRectF;
	private Bitmap mBg, mMask;

	PorterDuffXfermode mPorterDuffXfermode = new PorterDuffXfermode(
			PorterDuff.Mode.DST_IN);

	public BatteryCircleWave(Context context) {
		super(context);
		//mBg = ViewUtils.getBitmapWidthId(context, R.drawable.music_bg);
		mMask = ViewUtils.getScaleBitmapWithIDByWidth(context, R.drawable.battery_mask);
		mWave1 = ViewUtils.getScaleBitmapWithIDByWidth(context, R.drawable.battery_wave);
		mWave2 = ViewUtils.getScaleBitmapWithIDByWidth(context, R.drawable.battery_wave2);

		mWave1Rect = new Rect();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG); // 抗锯齿
		
		mPaint.setTextAlign(Align.CENTER);
		/*mPaint.setTextSize(ViewUtils.getPXByHeight(24));
		mPaint.setTypeface(CircleContainer.sTypeface);*/
		mPaint.setStyle(Style.STROKE);
		int strokeWidth = ViewUtils.getPXByWidth(5);
		mPaint.setStrokeWidth(strokeWidth);
		
		mBgRectF = new RectF();
		mBgRectF.set(0, 0, mWidth, mWidth);
		mCircirRectF = new RectF();
		mCircirRectF.set(strokeWidth, strokeWidth, mWidth - strokeWidth, mWidth - strokeWidth);
		
		mLevel = mWidth * Constant.sBatteryLevel / 100;
		mWaveY = mLevel;
		LogUtils.log(null, "mWaveY = " + mWaveY + "Constant.sBatteryLevel ="
				+ Constant.sBatteryLevel);
		mDuration = Constant.sBatteryLevel  * 40;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//canvas.drawBitmap(mBg, null, mBgRectF, mPaint);
		if (Constant.sBatteryState == 1) {
			int sc = canvas.saveLayer(mBgRectF, null, Canvas.MATRIX_SAVE_FLAG
					| Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
					| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
					| Canvas.CLIP_TO_LAYER_SAVE_FLAG);
	
			int waveWidth = mWave1.getWidth();
			int waveHeight = mWave1.getHeight();
			mWave1Rect.set(- waveWidth + mWaveX,  mWidth - mWaveY,
					0 + mWaveX, waveHeight + mWidth - mWaveY);
	
			if (Constant.sIsScreenOn && mWave1 != null && !mWave1.isRecycled()) {
				canvas.drawBitmap(mWave1, null, mWave1Rect, mPaint);
				canvas.drawBitmap(mWave2, null, mWave1Rect, mPaint);
				mWave1Rect.offset(waveWidth, 0);
				canvas.drawBitmap(mWave1, null, mWave1Rect, mPaint);
				canvas.drawBitmap(mWave2, null, mWave1Rect, mPaint);
				mWave1Rect.offset(-waveWidth, 0);
				//LogUtils.log(null, "waveWidth = " + waveWidth +"mWaveX =" + mWaveX);
			}
	
			mPaint.setXfermode(mPorterDuffXfermode);
			canvas.drawBitmap(mMask, null, mBgRectF, mPaint);
			mPaint.setXfermode(null);
			canvas.restoreToCount(sc);
			// =======================================================
			// 控制移动
			if (mAnimationStartTime == 0
					|| System.currentTimeMillis() - mAnimationStartTime > mDuration) {
				mAnimationStartTime = System.currentTimeMillis();
			}
			float t = (System.currentTimeMillis() - mAnimationStartTime) / mDuration * 1f;
			if (t < 1) {
				t = mInterpolator.getInterpolation(t);
				mWaveX = (int) (waveWidth * t);
				mWaveY = (int) ((mWidth - mLevel) * t) + mLevel;
			} else {
				mWaveX = 0;
				mWaveY = mLevel;
				mAnimationStartTime = System.currentTimeMillis();
			}
	
			if (Constant.sIsScreenOn) {
				invalidate();
			}
		} 
		//
		if (Constant.sBatteryLevel >= 50) {
			mPaint.setColor(Color.GREEN);
		} else if (Constant.sBatteryLevel >= 25) {
			mPaint.setColor(Color.BLUE);
		} else {
			mPaint.setColor(Color.RED);
		}
		//canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2 - ViewUtils.getPXByHeight(5), mPaint);
		int sweepAngle = 360 * Constant.sBatteryLevel / 100;
		canvas.drawArc(mCircirRectF, 90 - sweepAngle / 2, sweepAngle, false, mPaint);
	}

	@Override
	public void onDestroy() {
		ViewUtils.recycleBitmap(mBg);
		ViewUtils.recycleBitmap(mWave1);
		ViewUtils.recycleBitmap(mWave2);
	}

	@Override
	public void onResume() {
		invalidate();
	}

	@Override
	public void onMonitor(Bundle bundle) {
		String eventType = bundle.getString(Constant.TYPE);
		if (eventType.equals(Constant.BATTERYSTATE)) {
			// 更新电量状态,param参数,0:正常,1:充电中,2:电量低,3:已充满

			updateBatteryLevel();
		} else if (eventType.equals(Constant.BATTERYLEVEL)) {
			// 更新电量值
			updateBatteryLevel();
		}
	}

	private int mLevel = 0;
	private float mDuration = 6000f;
	private void updateBatteryLevel() {
		mLevel = mWidth * Constant.sBatteryLevel / 100;
		mDuration = Constant.sBatteryLevel  * 60;
		invalidate();
	}
}
