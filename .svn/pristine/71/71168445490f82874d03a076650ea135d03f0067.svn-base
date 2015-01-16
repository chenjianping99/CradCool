package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jiubang.goscreenlock.theme.cjpcardcool.R;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.LiveListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnMonitorListener;

/**
 * 
 * @author shenyaobin 菊花
 */
public class MumView extends ImageView implements OnMonitorListener,
		LiveListener, OnClickListener {

	private int mState = 2;
	private int mHeight = ViewUtils.getPXByWidth(119);
	public MumView(Context context) {
		super(context);
		setOnClickListener(this);
		
		int w = ViewUtils.getPXByWidth(79);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w, mHeight,
				Gravity.TOP | Gravity.RIGHT);
		params.topMargin = ViewUtils.getPXByHeight(30);
		params.rightMargin = ViewUtils.getPXByWidth(50);
		setLayoutParams(params);
		setBackgroundResource(R.drawable.mum_bg);
		//setImageResource(R.drawable.mum);
		
		mSrc = ViewUtils.getScaleBitmapWithIDByWidth(context, R.drawable.mum);
	}

	@Override
	public void onResume() {
		//Constant.sIsMum = true; //test
		if (Constant.sIsMum) {
			setVisibility(View.VISIBLE);
			startAnimation(getCustomAimation());
			//startState1Animation();
		} else {
			setVisibility(View.GONE);
		}
	}

	
	@Override
	public void onClick(View v) {
		if (mState == 1) {
			startState2Animation();
		} else {
			Global.sendUnlockWithIntent(getContext(), "themepreview", null,
					null, null);
		}
	}
	
	/*private Flip3DAnimation mVerticalAnimation;
	private void startState1Animation() {
		mState = 1;
		if (mVerticalAnimation == null) {
			mVerticalAnimation = new Flip3DAnimation(-359, 0);
		}
		mVerticalAnimation.setOrientation(Flip3DAnimation.HORIZONTAL);
		mVerticalAnimation.setDuration(1500);
		mVerticalAnimation.setRepeatCount(Animation.INFINITE);
		mVerticalAnimation.setRepeatMode(Animation.RESTART);
		mVerticalAnimation.setInterpolator(new LinearInterpolator());
		mVerticalAnimation.setAnimationListener(null);
		startAnimation(mVerticalAnimation);
	}*/
	
	private void startState1Animation() {
		mState = 1;
		setVisibility(View.VISIBLE);
		clearAnimation();
		AlphaAnimation r = new AlphaAnimation(0.3f, 1f);
		r.setInterpolator(new LinearInterpolator());
		r.setDuration(1500);
		r.setRepeatCount(Animation.INFINITE);
		r.setRepeatMode(Animation.REVERSE);
		
		/*ScaleAnimation r2 = new ScaleAnimation(0.5f, 1, 0.5f, 1, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		r2.setDuration(1500);
		r2.setRepeatCount(Animation.INFINITE);
		r2.setRepeatMode(Animation.REVERSE);
		
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(r2);
		set.addAnimation(r);*/
		startAnimation(r);
	}

	
	private void startState2Animation() {
		mState = 2;
		setVisibility(View.VISIBLE);
		clearAnimation();
		
		RotateAnimation r = new RotateAnimation(0, 359,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		/*ScaleAnimation r = new ScaleAnimation(0.5f, 1, 0.5f, 1, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);*/
		//AlphaAnimation r = new AlphaAnimation(0.1f, 1f);
		r.setInterpolator(new LinearInterpolator());
		r.setDuration(1500);
		r.setRepeatCount(Animation.INFINITE);
		r.setRepeatMode(Animation.RESTART);
		startAnimation(r);
	}

	/*AnimationDrawable mFrameAnim = null;
	private void startState2Animation() {
		mState = 2;
		setVisibility(View.VISIBLE);
		if (mFrameAnim == null) {
			mFrameAnim = new AnimationDrawable();
			// 为AnimationDrawable添加动画帧
			mFrameAnim.addFrame(getResources().getDrawable(R.drawable.mum1), 250);
			mFrameAnim.addFrame(getResources().getDrawable(R.drawable.mum2), 250);
			mFrameAnim.addFrame(getResources().getDrawable(R.drawable.mum3), 250);
			mFrameAnim.addFrame(getResources().getDrawable(R.drawable.mum4), 250);
			setBackgroundDrawable(mFrameAnim);
			mFrameAnim.setOneShot(false);
		}
		post(new Runnable() {
	        public void run() {
	        	mFrameAnim.start();
	        }
	    });
		//mFrameAnim.start();
		RotateAnimation r = new RotateAnimation(0, 359,	Animation.RELATIVE_TO_SELF, 
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		r.setInterpolator(new LinearInterpolator());
		r.setDuration(3000);
		r.setRepeatCount(Animation.INFINITE);
		r.setRepeatMode(Animation.RESTART);
		startAnimation(r);
	}*/

	@Override
	public void onMonitor(Bundle bundle) {
		// TODO Auto-generated method stub
		String eventType = bundle.getString("type");
		int param = bundle.getInt("param");
		if (eventType.equals("themepreview")) {
			if (param != 0) {
				Constant.sIsMum = true;
			} else {
				Constant.sIsMum = false;
			}
			onResume();
		}
	}

	@Override
	public void onPause() {
		clearAnimation();
	}

	@Override
	public void onStart() {

	}

	@Override
	public void onDestroy() {
	}

	
	private Bitmap mSrc;
	private int mTop = 0, mBottom = mHeight;
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		canvas.clipRect(0, mTop, getWidth(), mBottom);
		canvas.drawBitmap(mSrc, 0, 0, null);
		canvas.restore();
		super.onDraw(canvas);
	}
	
	private int mCount;
	private Animation getCustomAimation() {
		Animation a = new AlphaAnimation(1, 0.99f) {
			
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if (interpolatedTime < 1) {
					if (mCount % 2 == 0) {
						mTop = (int) (mHeight * (1 - interpolatedTime));
						mBottom = mHeight;
					} else {
						mBottom = (int) (mHeight * (1 - interpolatedTime));
						mTop = 0;
					}
					invalidate();
				} else {			
					mCount++;
					if (mCount == 100) {
						mCount = 0;
					}
				}
				
				super.applyTransformation(interpolatedTime, t);
			}
		};
		a.setInterpolator(new LinearInterpolator());
		a.setDuration(1000);
		a.setRepeatCount(Animation.INFINITE);
		a.setRepeatMode(Animation.RESTART);
		
		return a;
	}

}
