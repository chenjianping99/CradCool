package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.LiveListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnMonitorListener;

/**
 * 
 * @author 菊花
 */
public class MumLy extends FrameLayout implements OnMonitorListener,
		LiveListener, OnClickListener {

	private boolean mHasMun = false;
	private int mState = 1;

	public MumLy(Context context) {
		super(context);
		setOnClickListener(this);
		
		int w = ViewUtils.getPXByHeight(92);
		int h = ViewUtils.getPXByHeight(100);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w, w,
				Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		params.topMargin = ViewUtils.getPXByHeight(50);
		//params.rightMargin = ViewUtils.getPXByWidth(20);
		setLayoutParams(params);
		//setBackgroundResource(R.drawable.mum_bg);
		addViews(context);
	}
	
	private View mCircle, mCircle2, mPoint;
	private void addViews(Context context) {
		mCircle = new View(context);
		//mCircle.setBackgroundResource(R.drawable.mum_1);
		LayoutParams mBgParams = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.BOTTOM);
		addView(mCircle, mBgParams);

		mCircle2 = new View(context);
		//mCircle2.setBackgroundResource(R.drawable.mum_2);
		LayoutParams mLeafParams = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		addView(mCircle2, mLeafParams);
		
		mPoint = new View(context);
		//mPoint.setBackgroundResource(R.drawable.mum_3);
		int w = ViewUtils.getPXByHeight((int) (92 * 0.8f));
		LayoutParams mFlowerParams = new FrameLayout.LayoutParams(
				w, w, Gravity.CENTER);
		addView(mPoint, mFlowerParams);
		//mPoint.setVisibility(INVISIBLE);
		
		View uper  = new View(context);
		//uper.setBackgroundResource(R.drawable.mum_uper);
		LayoutParams uperParams = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		addView(uper, uperParams);
	}

	@Override
	public void onResume() {
		//mHasMun = true; //test
		if (mHasMun) {
			setVisibility(View.VISIBLE);
			startState1Animation();
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
	
	/*private AnimationDrawable mFrameAnim = null;
	private Flip3DAnimation mVerticalAnimation;
	private void startState1Animation() {
		mState = 1;
		mPoint.clearAnimation();
		mPoint.setVisibility(INVISIBLE);
		
		if (mFrameAnim == null) {
			mFrameAnim = new AnimationDrawable();
			// 为AnimationDrawable添加动画帧
			mFrameAnim.addFrame(getResources().getDrawable(R.drawable.mum_circle_1), 330);
			mFrameAnim.addFrame(getResources().getDrawable(R.drawable.mum_circle_2), 330);
			mFrameAnim.addFrame(getResources().getDrawable(R.drawable.mum_circle_3), 330);
			mFrameAnim.setOneShot(false);
		}
		mCircle.setBackgroundDrawable(mFrameAnim);
		mCircle.setVisibility(VISIBLE);
		post(new Runnable() {
	        public void run() {
	        	if (mFrameAnim != null) {
	        		mFrameAnim.start();
	        	}
	        }
	    });
		
		if (mVerticalAnimation == null) {
			mVerticalAnimation = new Flip3DAnimation(0, 359);
		}
		mVerticalAnimation.setOrientation(Flip3DAnimation.HORIZONTAL);
		mVerticalAnimation.setDuration(1500);
		mVerticalAnimation.setRepeatCount(Animation.INFINITE);
		mVerticalAnimation.setRepeatMode(Animation.RESTART);
		mVerticalAnimation.setInterpolator(new LinearInterpolator());
		mVerticalAnimation.setAnimationListener(null);
		mCircle2.startAnimation(mVerticalAnimation);
	}*/

	private void startState1Animation() {
		startRotateAnim(mCircle);
		startRotateAnim(mCircle2);
	}
	
	private void startRotateAnim(final View v) {
		v.clearAnimation();
		float toDegrees = 359;
		if (v == mCircle2) {
			toDegrees = - toDegrees;
		}
		RotateAnimation r = new RotateAnimation(0, toDegrees,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		r.setInterpolator(new LinearInterpolator());
		r.setDuration(2000);
		r.setRepeatCount(Animation.INFINITE);
		r.setRepeatMode(Animation.RESTART);
		v.startAnimation(r);
	}
	
	private void startScaleAnim(final View v) {
		v.clearAnimation();
		ScaleAnimation r;
		if (v == mPoint) {
			r = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
					0.5f);
		} else {
			r = new ScaleAnimation(1f, 0.8f, 1f, 0.8f,
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
					0.5f);
		}
		 
		r.setInterpolator(new LinearInterpolator());
		r.setDuration(1000);
		r.setRepeatCount(Animation.INFINITE);
		r.setRepeatMode(Animation.REVERSE);
		v.startAnimation(r);
	}
	
	private void startState2Animation() {
		mState = 2;
		startScaleAnim(mCircle);
		startScaleAnim(mCircle2);
		startScaleAnim(mPoint);
		
		/*if (mCircle != null) {
			mCircle.clearAnimation();
			//mCircle.setVisibility(INVISIBLE);
		}
		if (mCircle2 != null) {
			mCircle2.clearAnimation();
			//mCircle2.setVisibility(INVISIBLE);
		}
		RotateAnimation r = new RotateAnimation(0, 359,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
//		AlphaAnimation r = new AlphaAnimation(0.1f, 1);
		r.setInterpolator(new LinearInterpolator());
		r.setDuration(2000);
		r.setRepeatCount(Animation.INFINITE);
		r.setRepeatMode(Animation.RESTART);
		v.startAnimation(r);*/
	}
	
	@Override
	public void onMonitor(Bundle bundle) {
		String eventType = bundle.getString("type");
		int param = bundle.getInt("param");

		LogUtils.log(null, eventType + ";" + param);
		if (eventType.equals("themepreview")) {
			if (param != 0) {
				mHasMun = true;
			} else {
				mHasMun = false;
			}
			onResume();
		}
	}

	@Override
	public void onPause() {
	
		if (mCircle != null) {
			mCircle.clearAnimation();
		}
		if (mCircle2 != null) {
			mCircle2.clearAnimation();
		}
		if (mPoint != null) {
			mPoint.clearAnimation();
		}
	}

	@Override
	public void onStart() {
	
	}

	@Override
	public void onDestroy() {

	}

}
