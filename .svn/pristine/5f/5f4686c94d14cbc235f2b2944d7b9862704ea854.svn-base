package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.LiveListener;

/**
 * 
 * @author chenjianping
 * 
 */
public class TipView extends ImageView implements LiveListener {

	Bitmap mDefault;
	Bitmap mLight;
	Bitmap mMask;

	Paint mPaint = new Paint();
	PorterDuffXfermode mXfermode;

	public TipView(Context context) {
		super(context);
		int w = ViewUtils.getPXByWidth(30);
		int h = ViewUtils.getPXByWidth(47);

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w, h,
				Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		params.bottomMargin = ViewUtils.getPXByHeight(45);
		setLayoutParams(params);

		/*mDefault = ViewUtils.getScaleBitmapWithIDByWidth(context, R.drawable.moving_up_light_bg);
		mLight = ViewUtils.getScaleBitmapWithIDByWidth(context, R.drawable.moving_up_light);
		mMask = ViewUtils.getScaleBitmapWithIDByWidth(context, R.drawable.moving_up_light_display_area);*/
		mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
		
		/*
		 * mPaint.setColor(Color.WHITE); mPaint.setStyle(Style.FILL);
		 */
	}

	private long mStartTime = 0;
	private static final int TIME = 800;

	@Override
	protected void onDraw(Canvas canvas) {
		if (Constant.sIsScreenOn) {
			long time = System.currentTimeMillis() - mStartTime;
			float t = time * 1.0f / TIME;
			canvas.saveLayer(0, 0, mDefault.getWidth(), mDefault.getHeight(),
					null, Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
							| Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
							| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
							| Canvas.CLIP_TO_LAYER_SAVE_FLAG);
			if (t < 2) {
				canvas.save();
				canvas.drawBitmap(mDefault, 0, 0, null);
				canvas.translate(0, mDefault.getHeight() - mDefault.getHeight() * t);
				canvas.drawBitmap(mLight, 0, 0, mPaint);
				canvas.translate(0, - mDefault.getHeight() + mDefault.getHeight() * t) ;
				mPaint.setXfermode(mXfermode);
				canvas.drawBitmap(mMask, 0, 0, mPaint);
				mPaint.setXfermode(null);
				canvas.restore();
			} else {
				canvas.drawBitmap(mDefault, 0, 0, null);
				canvas.drawBitmap(mLight, 0, mDefault.getHeight(), mPaint);
				mPaint.setXfermode(mXfermode);
				canvas.drawBitmap(mMask, 0, 0, mPaint);
				mPaint.setXfermode(null);
				mStartTime = System.currentTimeMillis();
			}
			invalidate();
		} 
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onResume() {
		invalidate();
	}


	@Override
	public void onDestroy() {
		ViewUtils.recycleBitmap(mDefault);
		ViewUtils.recycleBitmap(mMask);
		ViewUtils.recycleBitmap(mLight);
	}

	@Override
	public void onPause() {
		
	}

}