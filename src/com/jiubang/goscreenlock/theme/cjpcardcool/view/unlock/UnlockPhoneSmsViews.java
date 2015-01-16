package com.jiubang.goscreenlock.theme.cjpcardcool.view.unlock;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager.JazzyViewPager;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.CircleContainer;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ViewUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnDestroyListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnMonitorListener;

/**
 * 
 * @author chenjianping
 * 
 */
public class UnlockPhoneSmsViews extends FrameLayout implements OnDestroyListener, OnMonitorListener {
	
    private Context mContext;
    private JazzyViewPager mBodyViewPager;
    public UnlockPhoneSmsViews(Context context, JazzyViewPager v) {
        super(context);
        this.mContext = context;
        mBodyViewPager = v;
       addUnlockViews();
    }
    
    private FrameLayout mPhone, mSms;
    private TextView mPhoneText, mSmsText;
    private void addUnlockViews() {
    	int w = ViewUtils.getPXByHeight(680);
    	int wText = ViewUtils.getPXByHeight(175);
    	mPhone = new FrameLayout(mContext);
    	FrameLayout.LayoutParams mPhoneParams = new LayoutParams(w, w, Gravity.LEFT | Gravity.TOP);
    	mPhoneParams.topMargin = ViewUtils.getPXByHeight(1030);
    	mPhoneParams.leftMargin = ViewUtils.getPXByWidth(60);
    	//mPhone.setBackgroundResource(R.drawable.icon_phone);
    	addView(mPhone, mPhoneParams);
    	
    	mPhoneText = new TextView(mContext);
    	//mPhoneText.setBackgroundResource(R.drawable.num_bg);
    	mPhoneText.setTextColor(Color.WHITE);
    	mPhoneText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(20));
    	mPhoneText.setTypeface(CircleContainer.sTypeface);
    	mPhoneText.setGravity(Gravity.CENTER);
    	LayoutParams phonetextParams = new LayoutParams(wText, wText, Gravity.TOP | Gravity.RIGHT);
    	phonetextParams.topMargin = ViewUtils.getPXByHeight(5);
    	phonetextParams.rightMargin = ViewUtils.getPXByHeight(8);
    	mPhone.addView(mPhoneText, phonetextParams);
    	
    	mSms = new FrameLayout(mContext);
    	FrameLayout.LayoutParams mSmsParams = new LayoutParams(w, w, Gravity.LEFT | Gravity.TOP);
    	mSmsParams.topMargin = mPhoneParams.topMargin + phonetextParams.topMargin;
    	mSmsParams.leftMargin = ViewUtils.getPXByWidth(560);
    	//mSms.setBackgroundResource(R.drawable.icon_sms);
    	addView(mSms, mSmsParams);
    	
    	mSmsText = new TextView(mContext);
    	//mSmsText.setBackgroundResource(R.drawable.num_bg);
    	mSmsText.setTextColor(Color.WHITE);
    	mSmsText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(20));
    	mSmsText.setTypeface(CircleContainer.sTypeface);
    	mSmsText.setGravity(Gravity.CENTER);
    	LayoutParams smstextParams = new LayoutParams(wText, wText, Gravity.TOP | Gravity.RIGHT);
    	mSms.addView(mSmsText, smstextParams);
    	
    	updateNum(mSmsText);
    	updateNum(mPhoneText);
    	mPhone.setOnTouchListener(mOnTouchListener);
    	mSms.setOnTouchListener(mOnTouchListener);
	}

    private void updateNum(View v) {
        if (v == mSmsText) {
        	if (Constant.sSMS > 0) {
        		mSmsText.setText("" + Constant.sSMS);
        		mSmsText.setVisibility(VISIBLE);
        	} else {
        		mSmsText.setVisibility(INVISIBLE);
        	}
        } else if (v == mPhoneText) {
        	if (Constant.sCall > 0) {
        		mPhoneText.setText("" + Constant.sCall);
        		mPhoneText.setVisibility(VISIBLE);
        	} else {
        		mPhoneText.setVisibility(INVISIBLE);
        	}
        }
    }
    
    private void handlePressImage(View v, boolean press) {
    	/*if (v == mSms) {
    		if (press) {
    			mSms.setBackgroundResource(R.drawable.icon_sms_press);
    			mPhone.setVisibility(INVISIBLE);
    		} else {
    			mSms.setBackgroundResource(R.drawable.icon_sms);
    			mPhone.setVisibility(VISIBLE);
    		}
    	} else if (v == mPhone) {
    		if (press) {
    			mPhone.setBackgroundResource(R.drawable.icon_phone_press);
    			mSms.setVisibility(INVISIBLE);
    		} else {
    			mPhone.setBackgroundResource(R.drawable.icon_phone);
    			mSms.setVisibility(VISIBLE);
    		}
    	}*/
    }

    private float mStartX, mDisX;
	private boolean mUnlockable;
	private LayoutParams mThisParams = null;
	private View mOnTouchView = null;
	private int mMargin;
	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mStartX = event.getRawX();
				mDisX = 0;
				mUnlockable = false;
				mOnTouchView = v;
				mThisParams = (LayoutParams) v.getLayoutParams();
				mMargin = mThisParams.leftMargin;
				handlePressImage(v, true);
				break;
			case MotionEvent.ACTION_MOVE:
				mDisX = (int) (event.getRawX() - mStartX);
				if (mOnTouchView == mPhone && mDisX < 0) {
					mDisX = 0;
				} else if (mOnTouchView == mSms && mDisX > 0) {
					mDisX = 0;
				} else {
					if (mBodyViewPager != null) {
						//mBodyViewPager.getViewPager().requestDisallowInterceptTouchEvent(true);
					}
					if (Math.abs(mDisX) > ViewUtils.getPXByWidth(240)) {
						if (!mUnlockable && Constant.sIsquake) {
							Global.getvibrator(getContext());
						}
						mUnlockable = true;
					} else {
						mUnlockable = false;
					}
				}
				mThisParams.leftMargin = (int) (mMargin + mDisX);
				//LogUtils.log(null, "leftMargin = " + mThisParams.leftMargin);
				v.setLayoutParams(mThisParams);
				break;
				
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				handlePressImage(v, false);
				mThisParams.leftMargin = mMargin;
				LogUtils.log(null, "mDisX = " + mDisX);
				v.setLayoutParams(mThisParams);
				if (mUnlockable) {
					if (v == mSms) {
						Global.sendUnlockWithIntent(getContext(), "sms", null, null);
					} else {
						Global.sendUnlockWithIntent(getContext(), "phone", null, null);
					}
				} else if (Math.abs(mDisX) > 0) {
					/*float disX = Math.abs(mDisX);
					if (mDisX < 0) {
						disX = mDisX;
					}*/
					TranslateAnimation t = new TranslateAnimation(mDisX, 0, 0, 0);
					t.setDuration((int) (Math.abs(mDisX) / Constant.sXRate));
					t.setInterpolator(new BounceInterpolator());
					v.startAnimation(t);					
				} 
				
				if (mBodyViewPager != null) {
					//mBodyViewPager.getViewPager().requestDisallowInterceptTouchEvent(false);
				}
				break;
			}
			return true;
		}
	};
	
	@Override
	public void onDestroy() {
		mBodyViewPager = null;
	}
	
	@Override
	public void onMonitor(Bundle bundle) {
		String eventType = bundle.getString(Constant.TYPE);
		if (eventType.equals(Constant.CALL)) {
			updateNum(mPhoneText);
		} else if (eventType.equals(Constant.SMS)) {
			updateNum(mSmsText);
		} 
	}
	
}
