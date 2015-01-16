package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.R;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnResumeListener;
/**
 * 
 * @author chenjianping
 *
 */
public class SlideItemsToUnlock extends FrameLayout implements OnResumeListener, 
	ILocker.OnDateChangedListener {

	public SlideItemsToUnlock(Context context) {
		super(context);

		int padding = ViewUtils.getPXByHeight(16);
		FrameLayout.LayoutParams mTotalFLParams = new FrameLayout.LayoutParams(
				Constant.sRealWidth - padding * 2, ViewUtils.getPXByHeight(212), Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		mTotalFLParams.topMargin = ViewUtils.getPXByHeight(510);
		setLayoutParams(mTotalFLParams);

		addCameraView();
		addDateTimeView();
		setOnTouchListener(mOnTouchListener);
	}

	private void addCameraView() {
		FrameLayout ly = new FrameLayout(getContext());
		LayoutParams params = new LayoutParams(
				ViewUtils.getPXByWidth(336), LayoutParams.MATCH_PARENT, Gravity.LEFT);
		ly.setBackgroundResource(R.drawable.camera_bg);
		
		View icon = new View(getContext());
		icon.setBackgroundResource(R.drawable.camera_icon);
		int w = ViewUtils.getPXByHeight(100);
		FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams(
				w, w, Gravity.LEFT | Gravity.CENTER_VERTICAL);
		iconParams.topMargin = ViewUtils.getPXByHeight(20);
		iconParams.leftMargin = ViewUtils.getPXByWidth(30);
		ly.addView(icon, iconParams);
		
		addView(ly, params);
	}

	private DateTimeView mDateTimeView;
	private void addDateTimeView() {
		mDateTimeView = new DateTimeView(getContext());
		addView(mDateTimeView);
		
	}
	
	@Override
	public void onDateChanged() {
		if (mDateTimeView != null) {
			mDateTimeView.onDateChanged();
		}
	}
	
	
	// unlock
	private float mStartX, mDisX;
	private boolean mUnlockable;
	private FrameLayout.LayoutParams mThisParams = null ;
	private int mMargin, mBottomMargin;
	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mStartX = event.getRawX();
				mDisX = 0;
				mUnlockable = false;
				mThisParams = (FrameLayout.LayoutParams) getLayoutParams();
				mMargin = 0;
				break;
			case MotionEvent.ACTION_MOVE:
				mDisX = (int) (event.getRawX() - mStartX);
				/*if (mDisX > 0) {
					mDisX = 0;
				} else */ {
					if (Math.abs(mDisX) > ViewUtils.getPXByHeight(300)) {
						if (!mUnlockable && Constant.sIsquake) {
							Global.getvibrator(getContext());
						}
						mUnlockable = true;
					} else {
						mUnlockable = false;
					}
				}
				mThisParams.leftMargin = (int) (mMargin + mDisX);
				setLayoutParams(mThisParams);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:

				if (mUnlockable) {
					float disX = Constant.sRealWidth - Math.abs(mDisX);
					if (mDisX < 0) {
						disX = - disX;
					}
					TranslateAnimation t = new TranslateAnimation(0, disX, 0, 0);
					t.setDuration((int) (Math.abs(disX) / Constant.sYRate / 2));
					//t.setFillAfter(true);
					
					AlphaAnimation r2 = new AlphaAnimation(1, 0);
					r2.setDuration((int) (Math.abs(disX) / Constant.sYRate / 2));		
					//r2.setFillAfter(true);
					
					AnimationSet set = new AnimationSet(true);
					set.addAnimation(t);
					set.addAnimation(r2);
					set.setFillAfter(true);
					startAnimation(set);
					set.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							if (mDisX < 0) {
								Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
								Global.sendUnlockWithIntent(getContext(), null, null, null, intent);
							} else {
								LogUtils.log(null, "unlock camera");
								Global.sendUnlockWithIntent(getContext(), "camera", null, null);
							}
						}
					});
					
				} else if (mDisX != 0) {
					mThisParams.leftMargin = mMargin;
					setLayoutParams(mThisParams);
					TranslateAnimation t = new TranslateAnimation(mDisX, 0, 0, 0);
					t.setDuration((int) (Math.abs(mDisX) / Constant.sYRate));
					startAnimation(t);					
				}

				break;
			}
			return true;
		}
	};
	
	@Override
	public void onResume() {
		if (mThisParams != null) {
			mThisParams.leftMargin = mMargin;
			setLayoutParams(mThisParams);
		}
	}

}
