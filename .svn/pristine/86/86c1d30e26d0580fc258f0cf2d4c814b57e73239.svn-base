package com.jiubang.goscreenlock.theme.cjpcardcool.view.unlock;

import java.util.Random;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager.JazzyViewPager;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Flip3DAnimation;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ViewUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.LiveListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.laceview.LaceView;
import com.jiubang.goscreenlock.theme.cjpcardcool.R;

/**
 * 
 * @author chenjianping
 * 
 */
public class UnlockHomeView extends FrameLayout implements LiveListener {
	
    private Context mContext;
    //private JazzyViewPager mViewPager;
    public UnlockHomeView(Context context, JazzyViewPager v) {
        super(context);
        this.mContext = context;
        //mViewPager = v;
        addReady();
        addUnlockView();
        addmLace();
    }
    
    private LaceView mLace;
	private void addmLace() {
		mLace = new LaceView(mContext, null);
		addView(mLace);
	}

    private ImageView mReadyView;
    private AnimationDrawable mReadyD;
    private void addReady() {
    	mReadyView = new ImageView(mContext);
    	mReadyD = (AnimationDrawable) getResources().getDrawable(R.anim.craft_ready);
    	//mReadyView.setBackgroundResource(R.drawable.ready_0);
    	LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 
    			ViewUtils.getPXByHeight(268), Gravity.CENTER);
    	params.topMargin = ViewUtils.getPXByHeight(208);
    	addView(mReadyView, params);
    }
    
    private FrameLayout mCraftLy;
    private ImageView mUnlock, mLeftFire, mRightFire;
    private void addUnlockView() {
    	int w = ViewUtils.getPXByHeight(533);
    	mCraftLy = new FrameLayout(mContext);
    	LayoutParams params = new LayoutParams(w, 
    			ViewUtils.getPXByHeight(610), Gravity.CENTER);
    	params.topMargin = ViewUtils.getPXByHeight(280);
    	addView(mCraftLy, params);
    	
    	int wFire = ViewUtils.getPXByHeight(68);
    	int  hFire = ViewUtils.getPXByHeight(165);
    	mLeftFire = new ImageView(mContext);
    	//mLeftFire.setBackgroundResource(R.drawable.craft_fire);
    	LayoutParams paramsL = new LayoutParams(wFire, 
    			hFire, Gravity.BOTTOM | Gravity.LEFT);
    	paramsL.leftMargin = ViewUtils.getPXByHeight(40);
    	mCraftLy.addView(mLeftFire, paramsL);
    	
    	mRightFire = new ImageView(mContext);
    	//mRightFire.setBackgroundResource(R.drawable.craft_fire);
    	LayoutParams paramsR = new LayoutParams(wFire, 
    			hFire, Gravity.BOTTOM | Gravity.RIGHT);
    	paramsR.rightMargin = ViewUtils.getPXByHeight(40);
    	mCraftLy.addView(mRightFire, paramsR);
    	
    	int  h = ViewUtils.getPXByHeight(481);
    	mUnlock = new ImageView(mContext);
    	//mUnlock.setBackgroundResource(R.drawable.craft_normal);
    	LayoutParams paramsUnlock = new LayoutParams(w, h, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
    	//paramsUnlock.topMargin = ViewUtils.getPXByHeight(5);
    	//params.leftMargin = ViewUtils.getPXByWidth(8);
    	mCraftLy.addView(mUnlock, paramsUnlock);
    	mUnlock.setOnTouchListener(mOnTouchListener);
    	
    	mRandom = new Random();
    	mRightFire.setVisibility(INVISIBLE);
    	mLeftFire.setVisibility(INVISIBLE);
	}
    
    private Random mRandom;
    private void starScaleAnim(int times) {
    	float toY;
    	if (times > 0) {
    		toY = mRandom.nextFloat() / 4 + 0.75f;
    	} else {
    		toY = mRandom.nextFloat() / 4 + 0.25f;
    	}
		final ScaleAnimation r = new ScaleAnimation(1, 1, toY, 1, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF);
		r.setInterpolator(new BounceInterpolator());
		r.setDuration((int) (toY * 500));
		//r.setFillAfter(true);
		/*r.setRepeatCount(1);
		r.setRepeatMode(Animation.REVERSE);*/
		
		if (mLeftFire != null && mRightFire != null) {
	    	mLeftFire.setVisibility(VISIBLE);
			mLeftFire.startAnimation(r);
			mRightFire.setVisibility(VISIBLE);
			mRightFire.startAnimation(r);
		}
		
		r.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				if (mIsOnTouched) {
					starScaleAnim(1);
				} else {
					mLeftFire.setVisibility(INVISIBLE);
					mRightFire.setVisibility(INVISIBLE);
				}
			}
		});
	}
	

    private float mStartY, mDisy;
	private boolean mHomeUnlockable, mIsOnTouched, mIsAniming;
	private LayoutParams mHomeThisParams = null;
	private View mHomeOnTouchView = null;
	private int mHomeMargin;
	private final int mMaxDisY = ViewUtils.getPXByHeight(200);
	
	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (mIsAniming) {
				return false;
			}
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mStartY = event.getRawY();
				mDisy = 0;
				mHomeUnlockable = false;
				mHomeOnTouchView = mCraftLy;
				mHomeThisParams = (LayoutParams) mCraftLy.getLayoutParams();
				mHomeMargin = mHomeThisParams.topMargin;
				//mUnlock.setBackgroundResource(R.drawable.craft_pressed);
				mIsOnTouched = true;
				starScaleAnim(0);
				mReadyView.setBackgroundDrawable(mReadyD);
				mReadyD.start();
				
				if (mLace != null) {
					mLace.setLaceStart(true);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				mDisy = (int) (event.getRawY() - mStartY);
				if (mDisy > 0) {
					mDisy = 0;
				} else {
					/*if (mViewPager != null) {
						mViewPager.requestDisallowInterceptTouchEvent(true);
					}*/
					if (Math.abs(mDisy) > mMaxDisY) {
						/*if (mDisy > mMaxDisY) {
							mDisy = mMaxDisY;
						}*/
						if (!mHomeUnlockable && Constant.sIsquake) {
							Global.getvibrator(getContext());
						}
						//
						mHomeUnlockable = true;
					} else {
						//mUnlock.setBackgroundResource(R.drawable.unlock_icon);
						mHomeUnlockable = false;
					}
				}
				mHomeThisParams.topMargin = (int) (mHomeMargin + mDisy);
				mHomeOnTouchView.setLayoutParams(mHomeThisParams);
				break;
				
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				
				TranslateAnimation t;
				float disY;
				if (mHomeUnlockable) {
					disY = Constant.sRealHeight + mDisy;
					LogUtils.log(null, "mUnlockable: disY = " + disY);
					t = new TranslateAnimation(0, 0, 0, - disY);
					t.setInterpolator(new AccelerateInterpolator());
					//t.setFillAfter(true);
					start3DUnlockAnimation();
				} else {
					disY = mDisy;
					//LogUtils.log(null, "not mUnlockable: disY = " + disY);
					t = new TranslateAnimation(0, 0, disY, 0);
					t.setInterpolator(new DecelerateInterpolator());
					//mUnlock.setBackgroundResource(R.drawable.craft_normal);
					mHomeThisParams.topMargin = mHomeMargin;
					mHomeOnTouchView.setLayoutParams(mHomeThisParams);
					mIsOnTouched = false;
					mReadyD.stop();
					//mReadyView.setBackgroundResource(R.drawable.ready_0);
				} 
				if (disY != 0) {
					t.setDuration((int) (Math.abs(disY) / Constant.sYRate / 2));
					mIsAniming = true;
					//t.setFillAfter(true);
					mHomeOnTouchView.startAnimation(t);	
					t.setAnimationListener(new AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							if (mHomeUnlockable) {
								mHomeOnTouchView.setVisibility(GONE);
								Global.sendUnlockWithIntent(getContext(), "home", null, null);
							}
							mIsAniming = false;
						}
					});
				}
				/*if (mViewPager != null) {
					mViewPager.requestDisallowInterceptTouchEvent(false);
				}*/
				break;
			}
			return true;
		}
	};
	
	@Override
	public void onDestroy() {
		//mViewPager = null;
		mLace.onDestroy();
		LogUtils.log(null, "UnlockHome: onDestroy");
	}

	@Override
	public void onResume() {
		if (mLace != null) {
			mLace.setVisibility(INVISIBLE);
		}
		LogUtils.log(null, "UnlockHome: onResume");
		if (mHomeOnTouchView != null && mHomeThisParams != null) {
			mHomeOnTouchView.clearAnimation();
			mHomeThisParams.topMargin = mHomeMargin;
			mHomeOnTouchView.setLayoutParams(mHomeThisParams);
			mHomeOnTouchView.setVisibility(VISIBLE);
		}
		/*if (Global.getRootView() != null) {
			Global.getRootView().clearAnimation();
		}*/
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		if (mReadyD != null) {
			mReadyD.stop();
		}
		mIsOnTouched = false;
		//mReadyView.setBackgroundResource(R.drawable.ready_0);
	}
	
	
	private Flip3DAnimation mVerticalAnimation;
	private void start3DUnlockAnimation() {
		if (mVerticalAnimation == null) {
			mVerticalAnimation = new Flip3DAnimation(0, 45);
		}
		mVerticalAnimation.setOrientation(Flip3DAnimation.VERTICAL);
		mVerticalAnimation.setDuration(500);
		mVerticalAnimation.setFillAfter(true);
		//mVerticalAnimation.setRepeatCount(Animation.INFINITE);
		//mVerticalAnimation.setRepeatMode(Animation.RESTART);
		mVerticalAnimation.setInterpolator(new LinearInterpolator());
		mVerticalAnimation.setAnimationListener(null);
		if (Global.getRootView() != null) {
			Global.getRootView().startAnimation(mVerticalAnimation);
		}
	}
}
