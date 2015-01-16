package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.os.Handler;
import android.view.Gravity;
import android.widget.FrameLayout;

/**
 * 
 * @author caizhiming
 *
 */
public class AnalogClockView extends FrameLayout implements ILocker.LiveListener {

	private RectF mRectF;

	private float mHourDegree;
	private float mMinuteDegree;
	private float mSecondDegree;
	private Receiver mReceiver;

	private Bitmap mHourBitmap;
	private Bitmap mMinuteBitmap;
	private Bitmap mSecondBitmap;
	//Bitmap mCenterBitmap;

	boolean mIsLock = false;;

	private PaintFlagsDrawFilter mPfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

	public AnalogClockView(Context context) {
		super(context);
		int w = ViewUtils.getPXByHeight(330);
		//setBackgroundResource(R.drawable.clock_center);
		FrameLayout.LayoutParams params = new LayoutParams(w, w, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		params.topMargin = ViewUtils.getPXByHeight(100);
		setLayoutParams(params);

		/*mHourBitmap = ViewUtils.getBitmapWidthId(context, R.drawable.clock_hour);
		mMinuteBitmap = ViewUtils.getBitmapWidthId(context, R.drawable.clock_minute);
		mSecondBitmap = ViewUtils.getBitmapWidthId(context, R.drawable.clock_second);*/
		//mCenterBitmap = ViewUtils.getBitmapWidthId(context, R.drawable.clock_center);

		mReceiver = new Receiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		context.registerReceiver(mReceiver, filter);
		
		mRectF = new RectF(0, 0, w, w);

		/*mHourRectF = new RectF((int) ((125 - 7) * Constant.sYRate), (int) ((125 - 56) * Constant.sYRate),
				(int) ((125 + 7) * Constant.sYRate), (int) (125 * Constant.sYRate));

		mMinuteRectF = new RectF((int) ((125 - 9) * Constant.sYRate), (int) ((140 - 94) * Constant.sYRate),
				(int) ((125 + 9) * Constant.sYRate), (int) (140 * Constant.sYRate));

		mSecondRectF = new RectF((int) ((125 - 7) * Constant.sYRate), (int) ((140 - 94) * Constant.sYRate),
				(int) ((125 + 7) * Constant.sYRate), (int) (140 * Constant.sYRate));

		mRingRectF = new RectF(0, 0, (int) (250 * Constant.sYRate), (int) (250 * Constant.sYRate));
		mCenterRectF = new RectF((int) ((125 - 30) * Constant.sYRate), (int) ((125 - 30) * Constant.sYRate),
				(int) ((125 + 30) * Constant.sYRate), (int) ((125 + 30) * Constant.sYRate));*/
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		canvas.setDrawFilter(mPfd);

		//时针
		canvas.save();
		canvas.rotate(mHourDegree % 360f, mRectF.centerX(), mRectF.centerY());
		canvas.drawBitmap(mHourBitmap, null, mRectF, paint);
		canvas.restore();

		//秒针
		canvas.save();
		canvas.rotate(mSecondDegree % 360f, mRectF.centerX(), mRectF.centerY());
		canvas.drawBitmap(mSecondBitmap, null, mRectF, paint);
		canvas.restore();

		//分针
		canvas.save();
		canvas.rotate(mMinuteDegree % 360f, mRectF.centerX(), mRectF.centerY());
		canvas.drawBitmap(mMinuteBitmap, null, mRectF, paint);
		canvas.restore();

		//canvas.drawBitmap(mCenterBitmap, null, mRectF, paint);

	}

	/**
	 * 
	 * @author caizhiming
	 *
	 */
	class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_TIME_TICK.equals(action)) {
				Calendar c = Calendar.getInstance();
				mSecond = c.get(Calendar.SECOND);
				mSecondDegree = mSecond * (360 / 60);
				updateTime();
			}
		}
	}

	Handler mHandler = new Handler();
	private int mSecond;
	Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			if (mIsLock) {
				mHandler.removeCallbacks(mRunnable);
			} else {
				Calendar c = Calendar.getInstance();
				mSecond = c.get(Calendar.SECOND);
				mSecondDegree = mSecond * (360 / 60);
				mHandler.postDelayed(mRunnable, 1000);
				invalidate();
			}
		}
	};

	private void updateTime() {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		mSecond = c.get(Calendar.SECOND);

		mHourDegree = hour * 30f + minute * 30f / 60f;
		mMinuteDegree = minute * 6f;
		mSecondDegree = mSecond * (360 / 60);

		mHandler.removeCallbacks(mRunnable);
		mHandler.postDelayed(mRunnable, 1000);

		invalidate();
	}

	@Override
	public void onStart() {

	}

	@Override
	public void onResume() {
		mIsLock = false;
		updateTime();
	}

	@Override
	public void onPause() {
		mIsLock = true;
	}

	@Override
	public void onDestroy() {
		ViewUtils.recycleBitmap(mHourBitmap);
		ViewUtils.recycleBitmap(mMinuteBitmap);
		ViewUtils.recycleBitmap(mSecondBitmap);

		if (mReceiver != null) {
			getContext().unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

}
