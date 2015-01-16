/**
 * 
 */
package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jiubang.goscreenlock.theme.cjpcardcool.music.MusicControlCenter;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnDestroyListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnResumeListener;

/**
 * @author chenjianping
 * 
 */

class MusicEffectView extends FrameLayout implements OnResumeListener,
	OnDestroyListener  {

	public MusicEffectView(Context context) {
		super(context);
		
		//setBackgroundResource(R.drawable.music_play_bg);
		addEffectView(context);
	}
	
	private ImageView mEffect;
	private static final int sDX = ViewUtils.getPXByWidth(29);
	private static final int sDY = ViewUtils.getPXByHeight(14);
	private static int sWidth = ViewUtils.getPXByWidth(530);
	private static int sHeight = ViewUtils.getPXByHeight(120);
	private static final int COUNT = sWidth / sDX;
	private RectF /*mCoverRectF, */ mEffectorRectF;
	private Bitmap /*mCover, */mEffector;
	private Random mRandom;
	private long mStartTime = 0;
	private int[] mTop = new int[COUNT];
	private void addEffectView(Context context) {
		//mEffector = ViewUtils.getScaleBitmapWithIDByHeight(context, R.drawable.music_wave);
		mEffectorRectF = new RectF(0, 0, ViewUtils.getPXByWidth(29), ViewUtils.getPXByHeight(14));
		//mCover = ViewUtils.getScaleBitmapWithIDByHeight(context, R.drawable.music_effect_cover);
		//mCoverRectF = new RectF(0, 0, mWidth, mHeight);
		mRandom = new Random();
		
		mEffect = new ImageView(context) {
			@Override
			protected void onDraw(Canvas canvas) 
			{	
				if (/*true || */MusicControlCenter.isMusicPlaying(getContext()) && Constant.sIsScreenOn) {
					canvas.save();
					//初始化
					if (mStartTime == 0 || System.currentTimeMillis() - mStartTime > 200f) {
						for (int i = 0; i < COUNT; i++) {
							mTop[i] = mRandom.nextInt(sHeight);
						}
						mStartTime = System.currentTimeMillis();
					}
					
					for (int i = 0; i < COUNT; i++) {
						int countY = mTop[i] / sDY;
						if (countY < 2) {
							canvas.translate(0, sHeight - sDY);
							canvas.drawBitmap(mEffector, null, mEffectorRectF, null);
							canvas.translate(0,  - sHeight + sDY);
						} else {
							for (int j = 0; j < countY; j++) {
								canvas.translate(0, sHeight - sDY * (j + 1));
								canvas.drawBitmap(mEffector, null, mEffectorRectF, null);
								canvas.translate(0,  - sHeight + sDY * (j + 1));
							}
						}
						canvas.translate(sDX, 0);
					}
					/*canvas.translate(- mDX * COUNT, 0);
					canvas.drawBitmap(mCover, null, mCoverRectF, null);*/
					canvas.restore();
				}
		
				if (Constant.sIsScreenOn) {
					invalidate();
				}

			}
		};
		
		LayoutParams mParams = new LayoutParams(sWidth, 
				sHeight, Gravity.CENTER_VERTICAL | Gravity.LEFT);
		mParams.leftMargin = ViewUtils.getPXByWidth(20);
		addView(mEffect, mParams);
		
		ImageView icon = new ImageView(context);
		//icon.setBackgroundResource(R.drawable.music_icon);
		int w = ViewUtils.getPXByHeight(121);
		LayoutParams params = new LayoutParams(w, 
				w, Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		params.rightMargin = ViewUtils.getPXByWidth(20);
		addView(icon, params);
	}

	@Override
	public void onDestroy() {
		//ViewUtils.recycleBitmap(mCover);
		ViewUtils.recycleBitmap(mEffector);
	}

	@Override
	public void onResume() {
		mEffect.invalidate();
	}
}

/*public class MusicEffectView extends FrameLayout implements OnResumeListener, OnDestroyListener {
	public MusicEffectView(Context context) {
		super(context);
		setBackgroundResource(R.drawable.music_play_bg);
		addEffectView(context);
	}

	private ImageView mEffect;
	private Bitmap mBitmap;
	private void addEffectView(Context context) {
		final Random mRandom = new Random();
		mBitmap = ViewUtils.getScaleBitmapWithIDByWidth(context, R.drawable.music_play_effect);
		mEffect = new ImageView(context) {
			private long mStartTime = 0;
			private int mTop = 0;
			@Override
			protected void onDraw(Canvas canvas) {
				if (true || MusicControlCenter.isMusicPlaying(getContext()) && Constant.sIsScreenOn) {
				 	canvas.save();
					canvas.clipRect(0, mTop, getWidth(), getHeight());
					canvas.drawBitmap(mBitmap, 0, 0, null);
					canvas.restore();
				} else {
					super.onDraw(canvas);
				}
				if (Constant.sIsScreenOn) {
					if (mStartTime == 0 || System.currentTimeMillis() - mStartTime > 50f) {
						mStartTime = System.currentTimeMillis();
						mTop = mRandom.nextInt(getHeight());
						LogUtils.log(null, "top = " + mTop);
					}
					invalidate();
				}
			}
		};
		
		mEffect.setImageResource(R.drawable.music_play_effect);
		LayoutParams mParams = new LayoutParams(ViewUtils.getPXByWidth(506), 
				ViewUtils.getPXByHeight(146), Gravity.CENTER_VERTICAL | Gravity.LEFT);
		mParams.leftMargin = ViewUtils.getPXByWidth(20);
		addView(mEffect, mParams);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
	}

	@Override
	public void onResume() {
		if (mEffect != null) {
			mEffect.invalidate();
		}
	}

	@Override
	public void onDestroy() {
		ViewUtils.recycleBitmap(mBitmap);
	}

}*/
