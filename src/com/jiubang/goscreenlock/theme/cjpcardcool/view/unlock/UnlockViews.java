package com.jiubang.goscreenlock.theme.cjpcardcool.view.unlock;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiubang.goscreenlock.theme.cjpcardcool.R;
import com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager.JazzyViewPager;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.CircleContainer;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnDestroyListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnMonitorListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnResumeListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.RootView;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ViewUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.battery.BatteryCircleWave;

/**
 * 
 * @author chenjianping
 * 
 */
public class UnlockViews extends FrameLayout implements OnResumeListener, OnDestroyListener, 
OnMonitorListener {
	
    private Context mContext;
    //private JazzyViewPager mViewPager;
    private int mHeight = ViewUtils.getPXByHeight(146);
    public UnlockViews(Context context, JazzyViewPager v) {
        super(context);
        
        int padding = ViewUtils.getPXByHeight(16);
		int mWidth = Constant.sRealWidth - padding * 2;
		LayoutParams mThisParams = new FrameLayout.LayoutParams(
				mWidth, mHeight, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		mThisParams.topMargin = ViewUtils.getPXByHeight(510 + (212 + 16) * 2 + 152 + 16);
		setLayoutParams(mThisParams);
		setBackgroundColor(0x33000000);
		
        this.mContext = context;
        //mViewPager = v;
       addUnlockViews();
       addBattery();
       mRootView = Global.getRootView();
    }
    
    private FrameLayout mPhone, mSms, mLock;
    private ImageView mPhoneIcon, mSmsIcon, mLockIcon;
    private TextView mPhoneText, mSmsText;
    private void addUnlockViews() {
    	
    	//天气、电池背景
    	/*ImageView v1 = new ImageView(mContext);
    	//v1.setBackgroundResource(R.drawable.wea_today_bg);
    	FrameLayout.LayoutParams v1Params = new LayoutParams(
    			ViewUtils.getPXByWidth(245), ViewUtils.getPXByWidth(254), Gravity.CENTER);
    	v1Params.bottomMargin = ViewUtils.getPXByWidth(185);
    	v1Params.rightMargin = ViewUtils.getPXByWidth(198);
    	addView(v1, v1Params);
    	
    	ImageView v2 = new ImageView(mContext);
    	//v2.setBackgroundResource(R.drawable.icon_battery_bg);
    	FrameLayout.LayoutParams v2Params = new LayoutParams(
    			ViewUtils.getPXByWidth(223), ViewUtils.getPXByWidth(203), Gravity.CENTER);
    	v2Params.topMargin = ViewUtils.getPXByWidth(180);
    	v2Params.leftMargin = ViewUtils.getPXByWidth(194);
    	addView(v2, v2Params);*/
    	
    	
    	int w = ViewUtils.getPXByWidth(103);
    	int h = ViewUtils.getPXByWidth(103);
    	mPhone = new FrameLayout(mContext);
    	FrameLayout.LayoutParams mPhoneParams = new LayoutParams(w, h, Gravity.LEFT | Gravity.CENTER_VERTICAL);
    	mPhoneParams.leftMargin = ViewUtils.getPXByWidth(50);
    	//mPhone.setBackgroundResource(R.drawable.icon_phone_bg);
    	addView(mPhone, mPhoneParams);
    	
    	mPhoneIcon =  new ImageView(mContext);
    	LayoutParams mPhoneIconParams = new LayoutParams(w, w, Gravity.CENTER);
    	mPhoneIcon.setBackgroundResource(R.drawable.icon_phone);
    	mPhone.addView(mPhoneIcon, mPhoneIconParams);
    	int textW = ViewUtils.getPXByHeight(40);
    	mPhoneText = new TextView(mContext);
    	mPhoneText.setBackgroundResource(R.drawable.cal_note_circle);
    	mPhoneText.setTextColor(Color.WHITE);
    	mPhoneText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(30));
    	mPhoneText.setTypeface(CircleContainer.sTypefaceCal);
    	mPhoneText.setGravity(Gravity.CENTER);
    	LayoutParams phonetextParams = new LayoutParams(textW, 
    			textW, Gravity.TOP | Gravity.RIGHT);
    	phonetextParams.topMargin = ViewUtils.getPXByHeight(5);
    	mPhone.addView(mPhoneText, phonetextParams);
    	
    	//sms
    	mSms = new FrameLayout(mContext);
    	FrameLayout.LayoutParams mSmsParams = new LayoutParams(w, h, Gravity.LEFT | Gravity.CENTER_VERTICAL);
    	mSmsParams.leftMargin = Constant.sRealWidth - w - ViewUtils.getPXByWidth(32) - mPhoneParams.leftMargin ;
    	//mSms.setBackgroundResource(R.drawable.icon_sms_bg);
    	addView(mSms, mSmsParams);
    	
    	mSmsIcon =  new ImageView(mContext);
    	LayoutParams mSmsIconParams = new LayoutParams(w, w, Gravity.CENTER);
    	/*mSmsIconParams.topMargin = mPhoneIconParams.bottomMargin;
    	mSmsIconParams.leftMargin = mPhoneIconParams.leftMargin;*/
    	mSmsIcon.setBackgroundResource(R.drawable.icon_sms);
    	mSms.addView(mSmsIcon, mSmsIconParams);
    	
    	mSmsText = new TextView(mContext);
    	mSmsText.setBackgroundResource(R.drawable.cal_note_circle);
    	mSmsText.setTextColor(Color.WHITE);
    	mSmsText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(32));
    	mSmsText.setTypeface(CircleContainer.sTypefaceCal);
    	mSmsText.setGravity(Gravity.CENTER);
    	LayoutParams mSmsTextParams = new LayoutParams(textW, 
    			textW, Gravity.TOP | Gravity.RIGHT);
    	mSmsTextParams.topMargin = phonetextParams.topMargin;
    	mSms.addView(mSmsText, mSmsTextParams);
    	
    	//unlock
    	mLock = new FrameLayout(mContext);
    	FrameLayout.LayoutParams mLockParams = new LayoutParams(w, h, Gravity.CENTER);
    	addView(mLock, mLockParams);
    	
    	mLockIcon =  new ImageView(mContext);
    	LayoutParams mLockIconParams = new LayoutParams(w, w, Gravity.CENTER);
    	mLockIcon.setBackgroundResource(R.drawable.icon_home);
    	mLock.addView(mLockIcon, mLockIconParams);
    	
    	updateNum(mSmsText);
    	updateNum(mPhoneText);
    	
    	mSms.setOnTouchListener(mOnTouchListener);
    	mPhone.setOnTouchListener(mOnTouchListener);
    	mLock.setOnTouchListener(mHomeOnTouchListener);
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
    	if (v == mSms) {
    		if (press) {
    			mSms.setBackgroundResource(R.drawable.icon_sms_unlock);
    			mSmsText.setVisibility(INVISIBLE);
    			mLockIcon.setBackgroundResource(R.drawable.icon_other);
    		} else {
    			mSms.setBackgroundResource(R.drawable.icon_sms);
    			updateNum(mSmsText);
    			mLockIcon.setBackgroundResource(R.drawable.icon_home);
    		}
    	} else if (v == mPhone) {
    		if (press) {
    			mPhone.setBackgroundResource(R.drawable.icon_phone_unlock);
    			mPhoneText.setVisibility(INVISIBLE);
    			mLockIcon.setBackgroundResource(R.drawable.icon_other);
    		} else {
    			mPhone.setBackgroundResource(R.drawable.icon_phone);
    			mPhoneText.setVisibility(VISIBLE);
    			updateNum(mPhoneText);
    			mLockIcon.setBackgroundResource(R.drawable.icon_home);
    		}
    	}
    }

    /*
    private void addTouchView() {
    	int w = ViewUtils.getPXByWidth(98);
    	mTouchView = new ImageView(mContext);
    	FrameLayout.LayoutParams mPhoneParams = new LayoutParams(w, w, Gravity.CENTER);
    	//mTouchView.setBackgroundResource(R.drawable.icon_touch);
    	addView(mTouchView, mPhoneParams);
    	mTouchView.setOnTouchListener(mOnTouchListener);
    }*/
    
    
    /*private int mUnlockIndex = -1; //sms0, phone1, unlock2;
    private int getUnlockIndex(float disX, float disY) {
    	int index = -1;
    	int r = ViewUtils.getPXByWidth(343 - 60) / 2;
    	int r2 = ViewUtils.getPXByWidth(40);

    	//往左移动
    	if (disX > - r - r2 &&  disX < - r + r2) {
    		//往左上移动
    		if (disY > - r - r2 &&  disY < - r + r2) {
    			mThisParams.leftMargin = -r;
				mThisParams.topMargin = -r;
				mTouchView.setLayoutParams(mThisParams);
				if (!mUnlockable && Constant.sIsquake) {
					Global.getvibrator(getContext());
				}
				mUnlockable = true;
				index = 0;
    		} else if (disY > r - r2 &&  disY < r + r2) {
    			mThisParams.leftMargin = -r;
				mThisParams.topMargin = r;
				mTouchView.setLayoutParams(mThisParams);
				if (!mUnlockable && Constant.sIsquake) {
					Global.getvibrator(getContext());
				}
				mUnlockable = true;
				index = 1;
    		} else {
    			mUnlockable = false;
    		}
    	} else if (disX > r - r2 &&  disX < r + r2) {
    		//往右下移动
    		if (disY > r - r2 &&  disY < r + r2) {
    			mThisParams.leftMargin = r;
				mThisParams.topMargin = r;
				mTouchView.setLayoutParams(mThisParams);
				if (!mUnlockable && Constant.sIsquake) {
					Global.getvibrator(getContext());
				}
				mUnlockable = true;
				index = 2;
    		} else {
    			mUnlockable = false;
    		}
		} else {
			mUnlockable = false;
		}
    	handleUnlockImage(index);
    	return index;
    }
    
    private void handleUnlockImage(int index) {
    	switch (index) {
		case 0:
			mSmsText.setVisibility(INVISIBLE);
			//mTouchView.setBackgroundResource(R.drawable.icon_unlock_sms);
			break;
		case 1:
			mPhoneText.setVisibility(INVISIBLE);
			//mTouchView.setBackgroundResource(R.drawable.icon_unlock_phone);
			break;
		case 2:
			//mTouchView.setBackgroundResource(R.drawable.icon_unlock);
			break;
		default:
			mSmsText.setVisibility(VISIBLE);
			mPhoneText.setVisibility(VISIBLE);
			//mTouchView.setBackgroundResource(R.drawable.icon_touch);
			break;
		}
    }*/
    
    private float mStartX, mDisX;
	private boolean mUnlockable;
	private View mTouchView;
	private LayoutParams mThisParams = null;
	private int mMargin;
	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mStartX = event.getRawX();
				mDisX = 0;
				mTouchView = v;
				mUnlockable = false;
				mThisParams = (LayoutParams) v.getLayoutParams();
				mMargin = mThisParams.leftMargin;
				/*if (mViewPager != null) {
					mViewPager.requestDisallowInterceptTouchEvent(true);
				}*/
				handlePressImage(v, true);
				break;
			case MotionEvent.ACTION_MOVE:
				mDisX = event.getRawX() - mStartX;
				if (mTouchView == mPhone && mDisX < 0) {
					mDisX = 0;
				} else if (mTouchView == mSms && mDisX > 0) {
					mDisX = 0;
				} else {
					if (Math.abs(mDisX) > ViewUtils.getPXByWidth(220)) {
						int positionDisX = ViewUtils.getPXByWidth(243);
						if (mDisX > 0) {
							mDisX = positionDisX;
						} else {
							mDisX = - positionDisX;
						}
						if (!mUnlockable && Constant.sIsquake) {
							Global.getvibrator(getContext());
						}
						mLock.setVisibility(INVISIBLE);
						mUnlockable = true;
					} else {
						mUnlockable = false;
						mLock.setVisibility(VISIBLE);
					}
				}
				mThisParams.leftMargin = (int) (mMargin + mDisX);
				LogUtils.log(null, "mDisX = " + mDisX + "mMargin = " + mMargin);
				v.setLayoutParams(mThisParams);
				break;
				
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				/*if (mViewPager != null) {
					mViewPager.requestDisallowInterceptTouchEvent(false);
				}*/
				handlePressImage(v, false);
				if (mUnlockable) {
					if (v == mSms) {
						Global.sendUnlockWithIntent(getContext(), "sms", null, null);
					} else {
						Global.sendUnlockWithIntent(getContext(), "phone", null, null);
					}
				} else if (mDisX != 0) {
					mThisParams.leftMargin = mMargin;
					v.setLayoutParams(mThisParams);
					TranslateAnimation t = new TranslateAnimation(mDisX, 0, 0, 0);
					t.setDuration((int) (Math.abs(mDisX) / Constant.sXRate));
					t.setInterpolator(new BounceInterpolator());
					v.startAnimation(t);				
				} 
				
				break;
			}
			return true;
		}
	};
	
	
	//解锁到home
	private RootView mRootView;
	private float mStartY, mDisY;
	private boolean mHomeUnlockable;
	private FrameLayout.LayoutParams mHomeThisParams = null ;
	private int mHomeMargin, mHomeBottomMargin;
	private OnTouchListener mHomeOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mStartY = event.getRawY();
				mDisY = 0;
				mHomeUnlockable = false;
				mHomeThisParams = (FrameLayout.LayoutParams) mRootView.getLayoutParams();
				mHomeThisParams.gravity = Gravity.TOP;
				mHomeMargin = mHomeThisParams.topMargin;
				mHomeBottomMargin = mHomeThisParams.bottomMargin;
				
				hideAnimation(mPhone, 0);
				hideAnimation(mSms, 200);
				break;
			case MotionEvent.ACTION_MOVE:
				mDisY = (int) (event.getRawY() - mStartY);
				if (mDisY > 0) {
					mDisY = 0;
				} else {
					if (mDisY < - ViewUtils.getPXByHeight(300)) {
						//mDisX = ViewUtils.getPXByHeight(330);
						if (!mHomeUnlockable && Constant.sIsquake) {
							Global.getvibrator(getContext());
						}
						mHomeUnlockable = true;
					} else {
						mHomeUnlockable = false;
					}
				}
				mHomeThisParams.topMargin = (int) (mHomeMargin + mDisY);
				mHomeThisParams.bottomMargin = (int) (mHomeBottomMargin - mDisY);
				mRootView.setLayoutParams(mHomeThisParams);
				/*if (mMainViewPager != null) {
					float alpha = 1;
					if (Math.abs(mDisY) > 100) {
						alpha = 0;
					} else {
						alpha = 1 - Math.abs(mDisY) / 100;
					}
					mMainViewPager.setChildViewAlpha(alpha);
				}*/
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				/*if (mMainViewPager != null) {
					mMainViewPager.setChildViewAlpha(1);
				}*/
				upAnimation(mPhone, 200);
				upAnimation(mSms, 0);
				if (mHomeUnlockable) {
					mRootView.startAnimation(mRootView.getCustomAimation());
				} else if (mDisY != 0) {
					mHomeThisParams.topMargin = mHomeMargin;
					mHomeThisParams.bottomMargin = mHomeBottomMargin;
					mRootView.setLayoutParams(mHomeThisParams);
					
					TranslateAnimation t = new TranslateAnimation(0, 0, mDisY, 0);
					t.setDuration((int) (Math.abs(mDisY) / Constant.sYRate * 2));
					t.setInterpolator(new BounceInterpolator());
					mRootView.startAnimation(t);					
				} else {
				}
			
				break;
			}
			return true;
		}
	};
	
	@Override
	public void onDestroy() {
		//mViewPager = null;
		mRootView = null;
		if (mBatteryCircleWave != null) {
			mBatteryCircleWave.onDestroy();
		}
	}
	
	@Override
	public void onMonitor(Bundle bundle) {
		String eventType = bundle.getString(Constant.TYPE);
		if (eventType.equals(Constant.CALL)) {
			updateNum(mPhoneText);
		} else if (eventType.equals(Constant.SMS)) {
			updateNum(mSmsText);
		} 
		if (mBatteryCircleWave != null) {
			mBatteryCircleWave.onMonitor(bundle);
		}
	}

	//unlockBg
	/*private static final int[] RES_BG = {R.drawable.unlock_bg_sunny, R.drawable.unlock_bg_cloudy, 
		R.drawable.unlock_bg_overcast, R.drawable.unlock_bg_snowy, R.drawable.unlock_bg_foggy, 
		R.drawable.unlock_bg_raniy, R.drawable.unlock_bg_thunderstom};
	@Override
	public void onBGChange() {
		if (mLock != null) {
			int index = ThemeSetProvider.getBackgroundIndex(getContext());
			LogUtils.log(null, "mLock index = " + index);
			//mLock.setBackgroundResource(RES_BG[index]);
		}
	}*/

	@Override
	public void onResume() {
		if (mThisParams != null) {
			mThisParams.leftMargin = mMargin;
			mTouchView.setLayoutParams(mThisParams);
			mLock.setVisibility(VISIBLE);
		}
		if (mBatteryCircleWave != null) {
			mBatteryCircleWave.onResume();
		}
		if (mSms != null) {
			mSms.clearAnimation();
		}
		if (mPhone != null) {
			mPhone.clearAnimation();
		}
	}
	
	
	//add battery;
	private BatteryCircleWave mBatteryCircleWave;
	private void addBattery() {
		mBatteryCircleWave = new BatteryCircleWave(mContext);
		LayoutParams mLockIconParams = new LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT, Gravity.CENTER);
    	mLock.addView(mBatteryCircleWave, mLockIconParams);		
	}
	
	//动画
	private void hideAnimation(final View v, int offsetTime) {
		Animation animation = new TranslateAnimation(0, 0, 0, mHeight);
		animation.setDuration(500);
		animation.setInterpolator(new OvershootInterpolator());
		animation.setStartOffset(offsetTime);
		animation.setFillAfter(true);
		v.startAnimation(animation);
	}
	
	private void upAnimation(final View v, int offsetTime) {
		Animation animation = new TranslateAnimation(0, 0, mHeight, 0);
		animation.setDuration(500);
		animation.setInterpolator(new OvershootInterpolator());
		animation.setStartOffset(offsetTime);
		animation.setFillAfter(true);
		v.startAnimation(animation);
	}
	
}
