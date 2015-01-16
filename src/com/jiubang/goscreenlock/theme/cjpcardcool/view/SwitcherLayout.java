package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.BroadcastBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.SwitcherService;
import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler.ISwitcherable;
import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler.SwitchConstants;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.LiveListener;

/**
 * 
 * 开关菜单
 * 
 * @author chenjianping
 * 
 */
public class SwitcherLayout extends FrameLayout implements OnClickListener, 
	LiveListener {

    private Context mContext;
    public boolean mIsShow = false;
    private BroadcastReceiver mStateChangeReceiver;
    private BroadcastReceiver mFailedChangeReceiver;

    private int mRingState;
    private int mRingIndex = 3;
    private int[] mSwitcherType = new int[] { 
    		ISwitcherable.SWITCH_TYPE_WIFI, 
            ISwitcherable.SWITCH_TYPE_GPRS, 
            ISwitcherable.SWITCH_TYPE_FLASHLIGHT, 
    		ISwitcherable.SWITCH_TYPE_RING, 
    		ISwitcherable.SWITCH_TYPE_BLUETOOTH,    		
    };

	private int[] mSwitcherResIdsOn = new int[] {
			/*R.drawable.switcher_wifi_on, 
			R.drawable.switcher_gprs_on, 
			R.drawable.switcher_flashlight_on, 
			R.drawable.switcher_ring_on, 
			R.drawable.switcher_bt_on,*/
		};

	private int[] mSwitcherResIdsOff = new int[] { 
			/*R.drawable.switcher_wifi_off, 
			R.drawable.switcher_gprs_off, 
			R.drawable.switcher_flashlight_off, 
			R.drawable.switcher_ring_off, 
			R.drawable.switcher_bt_off,*/
		};
	
	/*private String[] mSwitcherName = new String[] { 
			"AirPlane", "Gps", "Settings",
			"Volume", "BlueTooth",  "Camera",
			"WiFi", "Data", "Flashlight", 
    };*/
    
    private SwitcherItemLayout[] mSwitcherViews = new SwitcherItemLayout[mSwitcherType.length];
    private static final int LINES = 1; //一共1行
    private LinearLayout[] mSwitcherLys = new LinearLayout[LINES];
    private LinearLayout mMainLy;
    public SwitcherLayout(Context context) {
        super(context);
        this.mContext = context;
        LayoutParams mSwitcherParams = new FrameLayout.LayoutParams(
        		LayoutParams.MATCH_PARENT, ViewUtils.getPXByWidth(273 + 50), Gravity.BOTTOM);
        mSwitcherParams.bottomMargin = mMargin;
		setLayoutParams(mSwitcherParams);
		setOnTouchListener(mOnTouchListener);
		
		addCoverBg(context);
		
        mMainLy = new LinearLayout(context);
        mMainLy.setOrientation(LinearLayout.VERTICAL);
        mMainLy.setGravity(Gravity.CENTER);
        LayoutParams mParams = new FrameLayout.LayoutParams(
        		LayoutParams.MATCH_PARENT, ViewUtils.getPXByWidth(273), Gravity.BOTTOM);
		addView(mMainLy, mParams);
		mMainLy.setPadding(0, ViewUtils.getPXByWidth(110), 0, 0);
		//mMainLy.setBackgroundResource(R.drawable.switches_bg);
		
        /*mSwitcherLy = new LinearLayout(context);
        mSwitcherLy.setOrientation(LinearLayout.HORIZONTAL);
        mSwitcherLy.setGravity(Gravity.CENTER);
        LayoutParams params = new FrameLayout.LayoutParams(
        		LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        //mSwitcherLy.setBackgroundResource(R.drawable.switch_bg);
		addView(mSwitcherLy, params);*/
        
        addSwitcherView();
        
        startSwitcherService();
        registNomalReceiver();
        registerFailReceiver();
        
        postDelayed(new Runnable() {
            @Override
            public void run()
            {
                requestAllSwitch();
            }
        }, 200);
    }
    
    private View mGear1, mGear2;
	private void addCoverBg(Context context) {
		int w = ViewUtils.getPXByWidth(174);
		mGear1 = new View(context);
		FrameLayout.LayoutParams thisParams = new FrameLayout.LayoutParams(
				w, w, Gravity.LEFT | Gravity.TOP);
		thisParams.leftMargin = - ViewUtils.getPXByWidth(33);
		//mGear1.setBackgroundResource(R.drawable.switch_bg_gear);
		addView(mGear1, thisParams);
		
		mGear2 = new View(context);
		FrameLayout.LayoutParams thisParams2 = new FrameLayout.LayoutParams(
				w, w, Gravity.RIGHT | Gravity.TOP);
		thisParams2.rightMargin =  - ViewUtils.getPXByWidth(23);
		//mGear2.setBackgroundResource(R.drawable.switch_bg_gear);
		addView(mGear2, thisParams2);
	}

	private void startRotateAnim(final View v) {
		v.clearAnimation();
		float toDegrees = 359;
		/*if (v == mCircle2) {
			toDegrees = - toDegrees;
		}*/
		RotateAnimation r = new RotateAnimation(0, toDegrees,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		r.setInterpolator(new LinearInterpolator());
		r.setDuration(2000);
		r.setRepeatCount(Animation.INFINITE);
		r.setRepeatMode(Animation.RESTART);
		v.startAnimation(r);
	}

    /*private ImageView mClickIcon;
    private void addOnClickIcon() {
    	mClickIcon = new ImageView(getContext());
		//mClickIcon.setBackgroundResource(R.drawable.switcher_up);
		int mClickIconW = ViewUtils.getPXByHeight(103);
		
		LayoutParams mWeatherViewParams = new LayoutParams(
				mClickIconW, mClickIconH, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		addView(mClickIcon, mWeatherViewParams);
		mClickIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mSwitcherParams.bottomMargin == 0) {
					//mClickIcon.setBackgroundResource(R.drawable.switcher_up);
					handleUpDownAnim(true);
				} else {
					//mClickIcon.setBackgroundResource(R.drawable.switcher_down);
					handleUpDownAnim(false);
				}
			}
		});
    }
    
    private void handleUpDownAnim(boolean hide) {
    	Animation animation = null;
    	if (hide) {
    		mSwitcherParams.bottomMargin = mClickIconH - mSwitcherH;
        	setLayoutParams(mSwitcherParams);
    		animation = new TranslateAnimation(0, 0, mClickIconH - mSwitcherH, 0);
    	} else {
    		mSwitcherParams.bottomMargin = 0;
        	setLayoutParams(mSwitcherParams);
    		animation = new TranslateAnimation(0, 0, mSwitcherH - mClickIconH, 0);
    	}
    	animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(200);
        this.startAnimation(animation);
    }*/
	
	private boolean onTouchArea(MotionEvent event) {
		int x = ViewUtils.getPXByWidth(120);
		int y = ViewUtils.getPXByWidth(80);
		//LogUtils.log(null, "event.getX() = " + event.getX() + "event.getRawY =" + event.getY() + "x=" + x + "y=" + y);
		if (event.getX() > x && event.getX() < Constant.sRealWidth - x
				&& event.getY() < y) {
			return false;
		}
		return true;
	}
    
    private float mStartY, mDisY;
	//private boolean mUnlockable;
	private LayoutParams mThisParams = null;
	private int mMargin = - ViewUtils.getPXByHeight(150);
	private final int mMaxDisY = mMargin;
	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!onTouchArea(event)) {
					return false;
				}
				mStartY = event.getRawY();
				mDisY = 0;
				mThisParams = (LayoutParams) v.getLayoutParams();
				mMargin = mThisParams.bottomMargin;
				LogUtils.log(null, "ACTION_DOWN:mMargin = " + mMargin);
				break;
			case MotionEvent.ACTION_MOVE:
				mDisY = (int) (event.getRawY() - mStartY);
				if (mMargin < 0 && mDisY > 0) {
					mDisY = 0;
				} else if (mMargin == 0 && mDisY < 0) {
					mDisY = 0;
				} else {
					/*if (mViewPager != null) {
						mViewPager.requestDisallowInterceptTouchEvent(true);
					}*/
					if (mDisY < 0 && mDisY < mMaxDisY) {
						mDisY = mMaxDisY;
					} else if (mDisY > 0 && mDisY > - mMaxDisY) {
						mDisY = - mMaxDisY;
					}
				}
				mThisParams.bottomMargin = (int) (mMargin - mDisY);
				v.setLayoutParams(mThisParams);
				break;
				
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_OUTSIDE:
			case MotionEvent.ACTION_CANCEL:
				float animY = Math.abs(mMaxDisY) - Math.abs(mDisY);
				if (mMargin == 0) {
					//down
					mThisParams.bottomMargin = mMaxDisY;
					v.setLayoutParams(mThisParams);
					animY = - animY;
				} else {
					mThisParams.bottomMargin = 0;
					v.setLayoutParams(mThisParams);
				}
				LogUtils.log(null, "mMargin = " + mMargin + "animY =" + animY);
				TranslateAnimation t = new TranslateAnimation(0, 0, animY, 0);
				t.setDuration((int) (Math.abs(animY) / Constant.sYRate));
				t.setInterpolator(new DecelerateInterpolator());
				v.startAnimation(t);				
				break;
			}
			return true;
		}
	};
    
    /**
     * 注册返回广播
     */
    private void registNomalReceiver()
    {
        IntentFilter mFilter = new IntentFilter();
        
        mFilter.addAction(BroadcastBean.GPRS_CHANGE);
        //mFilter.addAction(BroadcastBean.GPS_CHANGE);
        mFilter.addAction(BroadcastBean.WIFI_CHANGE);
        mFilter.addAction(BroadcastBean.RINGER_CHANGE);
        mFilter.addAction(BroadcastBean.BLUE_TOOTH_CHANGE);
        mFilter.addAction(BroadcastBean.AIRPLANE_CHANGE);
        mStateChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                doStateChange(intent);
            }
        };
        mContext.registerReceiver(mStateChangeReceiver, mFilter);
    }

    /**
     * 注册失败打开开关广播
     */
    private void registerFailReceiver()
    {
        IntentFilter mFilter2 = new IntentFilter();
        mFilter2.addAction(BroadcastBean.FAILED_WIFI);
		mFilter2.addAction(BroadcastBean.FAILED_APN);
		mFilter2.addAction(BroadcastBean.FAILED_BLUTH);
		mFilter2.addAction(BroadcastBean.FAILED_RING);

        mFailedChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                doFailed(intent);
            }
        };
        mContext.registerReceiver(mFailedChangeReceiver, mFilter2);
    }

    /**
     * 处理状态改变的消息
     * 
     * @param bundle
     */
    private void doStateChange(Intent intent)
    {
        if (intent == null) {
            return;
        }
        Bundle bundle = intent.getExtras();
        String action = intent.getAction();
        if (bundle == null || action == null) {
            return;
        }
        int status1 = bundle.getInt(BroadcastBean.STATUS1);
        int status2 = bundle.getInt(BroadcastBean.STATUS2);
        int type = ISwitcherable.SWITCH_TYPE_EMPTY;
        try {
            if (action.equals(BroadcastBean.WIFI_CHANGE)) {
                type = ISwitcherable.SWITCH_TYPE_WIFI;
            } else if (action.equals(BroadcastBean.GPRS_CHANGE)) {
                type = ISwitcherable.SWITCH_TYPE_GPRS;
            } else if (action.equals(BroadcastBean.GPS_CHANGE)) {
                type = ISwitcherable.SWITCH_TYPE_GPS;
            } else if (action.equals(BroadcastBean.AIRPLANE_CHANGE)) {
                type = ISwitcherable.SWITCH_TYPE_AIRPLANE_MODE;
            } else if (action.equals(BroadcastBean.BLUE_TOOTH_CHANGE)) {
                type = ISwitcherable.SWITCH_TYPE_BLUETOOTH;
            } else if (action.equals(BroadcastBean.FLASH_LIGHT)) {
                type = ISwitcherable.SWITCH_TYPE_FLASHLIGHT;
            } else if (action.equals(BroadcastBean.RINGER_CHANGE)) {
                type = ISwitcherable.SWITCH_TYPE_RING;
            }
            updateSwitcherState(type, status1, status2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理改变状态失败的事件
     */
    private void doFailed(Intent intent)
    {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        try {
    		Intent mIntent = null;
			if (intent.getAction().equals(BroadcastBean.FAILED_WIFI)) {
				mIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
			} else if (intent.getAction().equals(BroadcastBean.FAILED_APN)) {
				mIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
			} else if (intent.getAction().equals(BroadcastBean.FAILED_BLUTH)) {
				mIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
			} else if (intent.getAction().equals(BroadcastBean.FAILED_RING)) {
				mIntent = new Intent(Settings.ACTION_SOUND_SETTINGS);
			}
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Global.sendUnlockWithIntent(getContext(), null, null, null, mIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //private static final float ALPHA_OFF = 0.5f;
    private void addSwitcherView()
    {
    	int i = 0;
    	//mMainLy.addView(getLine(), LayoutParams.MATCH_PARENT, 1);
    	//for (int i = 0; i < mSwitcherViews.length / LINES; ++i) {
        	for (int j = 0; j < mSwitcherViews.length / LINES; ++j) {
	            mSwitcherViews[i * LINES + j] = new SwitcherItemLayout(getContext(), null);
	            mSwitcherViews[i * LINES + j].setOnClickListener(this);
	            mSwitcherViews[i * LINES + j].setImageResource(mSwitcherResIdsOff[i * LINES + j]);
	            mSwitcherViews[i * LINES + j].setTag(j);
	            if (mSwitcherLys[i] == null) {
	            	mSwitcherLys[i] = new LinearLayout(mContext);
	            	mSwitcherLys[i] .setOrientation(LinearLayout.HORIZONTAL);
	            }
	            mSwitcherLys[i].addView(mSwitcherViews[i * LINES + j]);
        	}
        	mMainLy.addView(mSwitcherLys[i]);
        }
    	//mMainLy.addView(getLine(), LayoutParams.MATCH_PARENT, 1);
    //}
    
    /*private View getLine() {
    	View v = new View(mContext);
    	v.setBackgroundResource(R.drawable.line);
    	return v;
    }*/

    /*private void show()
    {
        mIsShow = true;
        this.setClickable(true);
        for (int i = 0; i < getChildCount(); ++i) {
        	 mSwitcherParams.leftMargin = 0;
             this.setLayoutParams(mSwitcherParams);
            Animation animation = new TranslateAnimation(-mSwitcherH, 0, 0, 0);
            animation.setFillAfter(true);
            animation.setInterpolator(new OvershootInterpolator());
            animation.setDuration(200);
            animation.setStartOffset(i * 50);
            View view = getChildAt(i);
            view.requestLayout();
            view.startAnimation(animation);
        }
    }

    private void hide()
    {
        mIsShow = false;
        this.setClickable(false);
        final int maxChildCount = getChildCount();
        for (int i = 0; i < maxChildCount; ++i) {
            Log.i("ddd", "i =" + i + " max = " + maxChildCount);
            Animation animation = new TranslateAnimation(0, - mSwitcherH, 0, 0);
            animation.setFillAfter(true);
            animation.setInterpolator(new OvershootInterpolator());
            animation.setDuration(200);
            animation.setStartOffset(i * 50);
            View view = getChildAt(i);
            view.requestLayout();
            view.startAnimation(animation);
            if (i == maxChildCount - 1) {
            	animation.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                    	mSwitcherParams.leftMargin = - mSwitcherH;
                        setLayoutParams(mSwitcherParams);
                        try {
                        } catch (Exception ex) {

                        }
                    }
                });
            }
        }
    }*/

    private void updateSwitcherState(int type, int state1, int state2)
    {
    	LogUtils.log(null, "state1 =" + state1 + "state2=" + state2 + "type = " + type);
        for (int i = 0; i < mSwitcherType.length; ++i) {
            if (type == mSwitcherType[i]) {
                if (type == ISwitcherable.SWITCH_TYPE_RING) {
                        //震动状态
                	switch (state1) {
					case AudioManager.RINGER_MODE_NORMAL:
						mSwitcherViews[i].setImageResource(mSwitcherResIdsOn[i]);
						mRingState = AudioManager.RINGER_MODE_NORMAL;
						break;
					case AudioManager.RINGER_MODE_SILENT:
						mSwitcherViews[i].setImageResource(mSwitcherResIdsOff[i]);
						mRingState = AudioManager.RINGER_MODE_SILENT;
						break;
					case AudioManager.RINGER_MODE_VIBRATE:
						//mSwitcherViews[i].setImageResource(R.drawable.switcher_vibrate);
						mRingState = AudioManager.RINGER_MODE_VIBRATE;
						break;
					default:
						mSwitcherViews[i].setImageResource(mSwitcherResIdsOn[i]);
						mSwitcherViews[i].mSwitcherStatus = true;
						break;
					}
                } else {
                    if (state1 == SwitchConstants.STATUS_ON) {
                    	mSwitcherViews[i].setImageResource(mSwitcherResIdsOn[i]);
                    	mSwitcherViews[i].mSwitcherStatus = true;
                    } else {
                    	mSwitcherViews[i].setImageResource(mSwitcherResIdsOff[i]);
                    	mSwitcherViews[i].mSwitcherStatus = false;
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        int index = getViewIndex(v);
        LogUtils.log(null, "Switcher: onClick  index =" + index);
        if (index < 0 || index >= mSwitcherType.length) {
            return;
        }
        /*if (index == 2) {
        	//Intent intent = new Intent("com.jiubang.goscreenlock.theme.cjpcardcool.GetPictureActivity");
        	Intent intent = new Intent(Settings.ACTION_SETTINGS);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//intent.putExtra(GetPictureActivity.INDEX, GetPictureActivity.RAINNY_INDEX);
			Global.sendUnlockWithIntent(getContext(), null, null, null, intent);
        } else if (index == 5) {
			Global.sendUnlockWithIntent(getContext(), "camera", null, null);
        } else */ if (index == mRingIndex) {
        	switch (mRingState) {
				case AudioManager.RINGER_MODE_NORMAL:
					//mSwitcherViews[index].setImageResource(R.drawable.switcher_vibrate);
					mRingState = AudioManager.RINGER_MODE_VIBRATE;
					break;
				case AudioManager.RINGER_MODE_SILENT:
					//mSwitcherViews[index].setImageResource(R.drawable.switcher_ring_on);
					mRingState = AudioManager.RINGER_MODE_NORMAL;
					break;
				case AudioManager.RINGER_MODE_VIBRATE:
					//mSwitcherViews[index].setImageResource(R.drawable.switcher_ring_off);
					mRingState = AudioManager.RINGER_MODE_SILENT;
					break;
        	}
       	 	switchSwitch(mSwitcherType[index]);
        } else  {
        	 mSwitcherViews[index].mSwitcherStatus = !mSwitcherViews[index].mSwitcherStatus;
        	 if (mSwitcherViews[index].mSwitcherStatus) {
        		 mSwitcherViews[index].setImageResource(mSwitcherResIdsOn[index]);
        	 } else {
        		 mSwitcherViews[index].setImageResource(mSwitcherResIdsOff[index]);
        	 }
             LogUtils.log(null, "Switcher: onClick   mSwitcherViews[index].mSwitcherStatus =" + mSwitcherViews[index].mSwitcherStatus);
        	 switchSwitch(mSwitcherType[index]); 
        }
    }

    private int getViewIndex(View v)
    {
        return  (Integer) v.getTag();
    	
    	/*int count = mMainLy.getChildCount();
        for (int i = 0; i < count; i++) {
        	 for (int j = 0; j < 3; j++) {
	            View view = mSwitcherLys[i].getChildAt(j);
	            if (view == v) {
	                return i * 3 + j;
	            }
        	 }
        }
        return -1;*/
    }


    /**
     * 启动开关服务
     */
    private void startSwitcherService()
    {
        Intent intent = new Intent();
        intent.setClass(getContext(), SwitcherService.class);
        getContext().startService(intent);
    }

    private void stopSwitcherService()
    {
        Intent intent = new Intent();
        intent.setClass(getContext(), SwitcherService.class);
        getContext().stopService(intent);
    }

    

    private void requestAllSwitch()
    {
        Intent intent = new Intent(BroadcastBean.SWITCH_NOTIFY_ACTION);
        Bundle b = new Bundle();
        b.putInt(BroadcastBean.SWITCH_STATE, 0);
        intent.putExtras(b);
        getContext().sendBroadcast(intent);
    }

    private void switchSwitch(int switcherType)
    {
        Intent intent = new Intent(BroadcastBean.SWITCH_NOTIFY_ACTION);
        Bundle b = new Bundle();
        b.putInt(BroadcastBean.SWITCH_STATE, 1);
        b.putInt(BroadcastBean.SWITCH_TYPE, switcherType);
        intent.putExtras(b);
        getContext().sendBroadcast(intent);
    }

	@Override
	public void onResume() {
		startRotateAnim(mGear1);
		startRotateAnim(mGear2);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	private final static int FLIGHT_INDEX = 2;
	@Override
	public void onPause() {
		if (mSwitcherViews[FLIGHT_INDEX].mSwitcherStatus == true) {
			mSwitcherViews[FLIGHT_INDEX].mSwitcherStatus = false;
			mSwitcherViews[FLIGHT_INDEX].setImageResource(mSwitcherResIdsOff[FLIGHT_INDEX]);
            LogUtils.log(null, "Switcher: mSwitcherViews[index].mSwitcherStatus =" + mSwitcherViews[FLIGHT_INDEX].mSwitcherStatus);
       	 	switchSwitch(mSwitcherType[FLIGHT_INDEX]); 
		}
		
		mGear1.clearAnimation();
		mGear2.clearAnimation();
	}
	
	@Override
	public void onDestroy() {
		stopSwitcherService();
		if (mStateChangeReceiver != null) {
			try {
				getContext().unregisterReceiver(mStateChangeReceiver);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (mFailedChangeReceiver != null) {
			try {
				getContext().unregisterReceiver(mFailedChangeReceiver);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		removeAllViews();
		if (mSwitcherViews != null) {
			for (int i = 0; i < mSwitcherViews.length; ++i) {
				mSwitcherViews[i] = null;
			}
			mSwitcherViews = null;
		}
	}

	
}
