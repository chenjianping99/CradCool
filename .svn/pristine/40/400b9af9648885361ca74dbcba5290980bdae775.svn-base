package com.jiubang.goscreenlock.theme.cjpcardcool.view.battery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.CircleContainer;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.LiveListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnMonitorListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ViewUtils;

/**
 * 
 * @author chenjianping
 *
 */
public class BatteryView extends View implements LiveListener,
	OnMonitorListener
{
	private int mLevel = 0, mLevelAdd = 0;
	private boolean mScreenOn = false;
	private int mWidth, mHeight, mPaddingY, mPaddingX;
	
	public BatteryView(Context context)
	{
		super(context);
		
		mWidth =  ViewUtils.getPXByWidth(417);
		mHeight =  ViewUtils.getPXByWidth(101);
		mPaddingY =  ViewUtils.getPXByWidth(5);
		mPaddingX =  ViewUtils.getPXByWidth(46);
		int height =  ViewUtils.getPXByWidth(150);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mWidth + mPaddingX * 2,
				height, Gravity.TOP);
		params.topMargin = ViewUtils.getPXByHeight(68);
		params.leftMargin = ViewUtils.getPXByWidth(220) - mPaddingX;
		setLayoutParams(params);
		//setBackgroundColor(0x33ffffff);
	}

	private Bitmap mBattery, mMask, mPointIcon;
	private PorterDuffXfermode mPorterDuffXfermode;
	private Paint mPaint, mPaintText;
	//private RectF mRectF;
	@Override
	public void onStart()
	{
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG); // 抗锯齿
		mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
				
		mPaintText = new Paint();
		mPaintText.setTypeface(CircleContainer.sTypeface);
		mPaintText.setTextSize(ViewUtils.getPXByWidth(22));
		mPaintText.setAntiAlias(true);
	    mPaintText.setTextAlign(Paint.Align.CENTER);
	    mPaintText.setColor(Color.WHITE);		
	    
	    //mRectF = new RectF(mPaddingX, 0, mWidth + mPaddingX, mHeight);
		/*mBattery = ViewUtils.getScaleBitmapWithIDByWidth(getContext(), R.drawable.battery_bg);
		mMask = ViewUtils.getScaleBitmapWithIDByWidth(getContext(), R.drawable.battery_mask);
		mPointIcon = ViewUtils.getScaleBitmapWithIDByWidth(getContext(), R.drawable.battery_point);*/
	}
	
	@Override
	public void onResume()
	{
		mScreenOn = true;
		updateBatteryLevel();
	}
	@Override
	public void onPause()
	{
		mScreenOn = false;
	}
	
	@Override
	public void onDestroy()
	{
		ViewUtils.recycleBitmap(mBattery);
		ViewUtils.recycleBitmap(mMask);
		ViewUtils.recycleBitmap(mPointIcon);
	}

	private void updateBatteryLevel()
	{	
		//mLevel = 360 * Constant.sBatteryLevel / 100;
		mLevel = mWidth * (Constant.sBatteryLevel  - 100) / 100;
		LogUtils.log(null, "mLevel = " + mLevel + "Constant.sBatteryLevel =" + Constant.sBatteryLevel);
		invalidate();
	}
	
	private long mStartTime = 0;
	private final int mTextStartX = ViewUtils.getPXByWidth(95);
	private final int mTextEndX = ViewUtils.getPXByWidth(430);
	@Override
	protected void onDraw(Canvas canvas)
	{
		if (Constant.sBatteryState == 1) {
			if (mStartTime == 0) {
				mStartTime = System.currentTimeMillis();
			}
			float t = (System.currentTimeMillis() - mStartTime) / 5000f;
			if (t < 1) {
				mLevelAdd++;
				if (mLevel + mLevelAdd > 0) {
					 mLevelAdd = 0;
				}
			} else {
				mStartTime = System.currentTimeMillis();
			}
		} else {
			mLevelAdd = 0;
		}

		int tipX = mPaddingX + mWidth * Constant.sBatteryLevel / 100 - mPointIcon.getWidth() / 2;
		if (tipX > mPaddingX + mWidth - mPointIcon.getWidth()) {
			tipX = mPaddingX + mWidth - mPointIcon.getWidth();
		}
		canvas.drawBitmap(mPointIcon, tipX, mHeight + mPaddingY, null);
		//画电量
		//Constant.sBatteryLevel = 1;  //test
		int textCenterX = mPaddingX + mWidth * Constant.sBatteryLevel / 100;
		if (textCenterX < mTextStartX) {
			textCenterX = mTextStartX;
		} else if (textCenterX > mTextEndX) {
			textCenterX = mTextEndX;
		} 
		canvas.drawText("Battery " + Constant.sBatteryLevel + "%", textCenterX, 
				mHeight + mPointIcon.getHeight() + mPaddingY * 5.5f ,  mPaintText);
		
		int sc = canvas.saveLayer(mPaddingX, 0, mWidth + mPaddingX, mHeight, null,
				Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
						| Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
						| Canvas.CLIP_TO_LAYER_SAVE_FLAG);

		/*mRectF.left = mPaddingX;
		mRectF.right = mBattery.getWidth() + mPaddingX + mLevel + mLevelAdd;
		canvas.drawBitmap(mBattery, null, mRectF, mPaint);*/
		canvas.clipRect(0, 0, mWidth + (mPaddingX + mLevel + mLevelAdd), getHeight());
		canvas.drawBitmap(mMask, mPaddingX, 0, mPaint);
		mPaint.setXfermode(mPorterDuffXfermode);
		canvas.drawBitmap(mBattery, mPaddingX + mLevel + mLevelAdd, 0, mPaint);
		mPaint.setXfermode(null);
		canvas.restore();
		canvas.restoreToCount(sc);
		
		if (mScreenOn && Constant.sBatteryState == 1) { 
			invalidate();
		}
	}

	@Override
	public void onMonitor(Bundle bundle)
	{
		// TODO Auto-generated method stub
		String eventType = bundle.getString(Constant.TYPE);
		if (eventType.equals(Constant.BATTERYSTATE)) {
			// 更新电量状态,param参数,0:正常,1:充电中,2:电量低,3:已充满
			
			updateBatteryLevel();
		} else if (eventType.equals(Constant.BATTERYLEVEL)) {
			// 更新电量值
			updateBatteryLevel();
		}
	}

}
