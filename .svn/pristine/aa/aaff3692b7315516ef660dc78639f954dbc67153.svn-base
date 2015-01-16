package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.ThemeSetProvider;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.unlock.UnlockViews;

/**
 * 
 * @author shenyaobin 锁屏父View
 * 
 */
public class CircleContainer extends FrameLayout implements
		ILocker.LiveListener {
	private final static byte METHOD_ONSTART = 1;
	private final static byte METHOD_ONRESUME = METHOD_ONSTART + 1;
	private final static byte METHOD_ONPAUSE = METHOD_ONRESUME + 1;
	private final static byte METHOD_ONDESTROY = METHOD_ONPAUSE + 1;
	private final static byte METHOD_ONWEATHERCHANGE = METHOD_ONDESTROY + 1;
	private final static byte METHOD_ONMONITOR = METHOD_ONWEATHERCHANGE + 1;
	private final static byte METHOD_ONDATECHANGED = METHOD_ONMONITOR + 1;

	private DateChangedReceiver mChangeReceiver;
	private Bundle mWeatherBundle;
	private Bundle mMonitorBundle;
	public final static int OFFSET = 10;
	public final static int COLOR_WHITE = 0xffffffff;
	public static Typeface sTypeface, sTypefaceCal, sTypefaceWea;

	public CircleContainer(Context context) {
		super(context);
		
		Constant.sBgIndex = ThemeSetProvider.getBackgroundIndex(getContext());
		mChangeReceiver = new DateChangedReceiver();
		if (sTypeface == null) {
			sTypeface = Typeface.createFromAsset(context.getAssets(),
					"fonts/date.otf");
		}
		/*if (sTypefaceCal == null) {
			sTypefaceCal = Typeface.createFromAsset(context.getAssets(),
					"fonts/cal.ttf");
		}
		if (sTypefaceWea == null) {
			sTypefaceWea = Typeface.createFromAsset(context.getAssets(),
					"fonts/wea.ttf");
			//sTypefaceWea = sTypefaceCal;
		}*/
		addChildsView(context);
	}

	private void addChildsView(Context context) {
		
		addView(new WeatherView(context));
		addView(new SlideItemsToUnlock(context));
		addView(new BodyViewPager(context));
		addView(new MyCalendarEventView(context, null));
		addView(new UnlockViews(context, null));
		addView(new MumView(context));
	}
	
	/*private WallpaperSelectorContentView mWallpaper;
	private void addWallView(Context context) {
		mWallpaper = new WallpaperSelectorContentView(context, this);
		mWallParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, Constant.sRealHeight,
				Gravity.TOP);
		mWallParams.topMargin = Constant.sRealHeight;
		addView(mWallpaper, mWallParams);

		addMenuBtn(context);
	}
	
	private ImageView mAddImage;
	private boolean mClickable = true;
	private void addMenuBtn(Context context) {
		mAddImage = new ImageView(context);
		mAddImage.setImageResource(R.drawable.add);
		mAddImage.setAlpha(92);
		FrameLayout.LayoutParams addParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.RIGHT);
		addParams.topMargin = ViewUtils.getPXByHeight(30);
		addParams.rightMargin = ViewUtils.getPXByWidth(20);
		addView(mAddImage, addParams);
		//mAddImage.setVisibility(INVISIBLE); //关闭互推页
		mAddImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mClickable) {
					return;
				} else {
					menueViewIn();
					mAddImage.setVisibility(INVISIBLE);
					mDateTimeView.setVisibility(INVISIBLE);
					if (Constant.sIsMum) {
						mMumView.clearAnimation();
						mMumView.setVisibility(INVISIBLE);
					}
					Global.getRootView().setUnlockTipVisiable(INVISIBLE);
				}
					if (!sAddClickable) {
						RotateAnimation rotateAnimation = new RotateAnimation(0, 225f,
								Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
						rotateAnimation.setDuration(400);
						rotateAnimation.setFillAfter(true);
						mAddImage.startAnimation(rotateAnimation);
						mAddImage.setAlpha(0);
						sAddClickable = true;
						menueViewIn();
					} else {
						RotateAnimation rotateAnimation = new RotateAnimation(225f, 0,
								Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
						rotateAnimation.setDuration(400);
						rotateAnimation.setFillAfter(true);
						mAddImage.startAnimation(rotateAnimation);
						mAddImage.setAlpha(92);
						sAddClickable = false;
						menueViewOut();
					}
			}
		});
	}
	
	private FrameLayout.LayoutParams mWallParams;
	private void menueViewIn() {
		mWallpaper.clearAnimation();
		mWallParams.topMargin = 0;
		mWallpaper.setLayoutParams(mWallParams);
		TranslateAnimation tAnimation = new TranslateAnimation(0, 0, Constant.sRealHeight, 0);
		tAnimation.setDuration(400);
		tAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				mClickable = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				//mWallpaper.setMenueVisible();
				mClickable = true;
			}
		});
		mWallpaper.startAnimation(tAnimation);
	}

	*//**
	 * 
	 * MenueView 消失动画
	 *//*
	public void menueViewOut() {
		mWallpaper.clearAnimation();
		mWallParams.topMargin = Constant.sRealHeight;
		mWallpaper.setLayoutParams(mWallParams);
		TranslateAnimation tAnimation = new TranslateAnimation(0, 0, - Constant.sRealHeight, 0);
		tAnimation.setDuration(400);
		//tAnimation.setFillAfter(true);
		tAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				//mWallpaper.setMenueInvisible();
				mClickable = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mClickable = true;
				startVisiableAnimation();
				if (Constant.sIsMum) {
					mMumView.setVisibility(VISIBLE);
					mMumView.onResume();
				}
				Global.getRootView().startVisiableAnimation();
			}
		});
		mWallpaper.startAnimation(tAnimation);
	}

	private void startVisiableAnimation() {
		AlphaAnimation r = new AlphaAnimation(0f, 1f);
		r.setInterpolator(new LinearInterpolator());
		r.setDuration(1000);
		r.setRepeatCount(Animation.INFINITE);
		r.setRepeatMode(Animation.RESTART);
		if (mDateTimeView != null) {
			mDateTimeView.setVisibility(VISIBLE);
			mDateTimeView.startAnimation(r);
		}
		if (mAddImage != null) {
			mAddImage.setVisibility(VISIBLE);
			mAddImage.startAnimation(r);
		}
	}*/
	
	/*private View mGear1, mGear2;
	private void addCoverBg(Context context) {
		int w = ViewUtils.getPXByHeight(104);
		mGear1 = new View(context);
		FrameLayout.LayoutParams thisParams = new FrameLayout.LayoutParams(
				w, w, Gravity.LEFT | Gravity.TOP);
		thisParams.topMargin =  - ViewUtils.getPXByHeight(55);
		mGear1.setBackgroundResource(R.drawable.bg_gear);
		addView(mGear1, thisParams);
		
		mGear2 = new View(context);
		FrameLayout.LayoutParams thisParams2 = new FrameLayout.LayoutParams(
				w, w, Gravity.RIGHT | Gravity.TOP);
		thisParams2.topMargin =  - ViewUtils.getPXByHeight(55);
		mGear2.setBackgroundResource(R.drawable.bg_gear);
		addView(mGear2, thisParams2);
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
	}*/
	
	/*private static final char[] WEEK_LATTER = {'S', 'M', 'T', 'W', 'T', 'F', 'S'};
	private LinearLayout mTotalFL;
	private void addWeekly(Context context) {
		mTotalFL = new LinearLayout(context);
		mTotalFL.setOrientation(LinearLayout.HORIZONTAL);
		mTotalFL.setGravity(Gravity.CENTER);
		FrameLayout.LayoutParams mTotalFLParams = new FrameLayout.LayoutParams(
				Constant.sRealWidth, ViewUtils.getPXByHeight(119), Gravity.BOTTOM);
		mTotalFL.setBackgroundResource(R.drawable.week_bg);
		addView(mTotalFL, mTotalFLParams);
		int w = ViewUtils.getPXByWidth(54);
		int magin = (Constant.sRealWidth - w * WEEK_LATTER.length) / (2 * WEEK_LATTER.length);
		for (int i = 0; i < WEEK_LATTER.length; i++) {
			TextView mWeek = new TextView(context);
			mWeek.setText("" + WEEK_LATTER[i]);
			mWeek.setTextColor(Color.BLACK);
			mWeek.setTypeface(CircleContainer.sTypefaceCal);
			mWeek.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(36));
			mWeek.setGravity(Gravity.CENTER);
			
			LinearLayout.LayoutParams mWeekLP = new LinearLayout.LayoutParams(
					w, w);
			mWeekLP.leftMargin = magin;
			mWeekLP.rightMargin = mWeekLP.leftMargin;
			mTotalFL.addView(mWeek, mWeekLP);
		}
		
		if (mTotalFL != null) {
			int index = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
			mTotalFL.getChildAt(index).setBackgroundResource(R.drawable.week_chosen);
		}
	}*/
	
	/*private TextView mUnlockTip;
	private void addUnlockTip() {
		mUnlockTip = new TextView(getContext());
		mUnlockTip.setTextColor(Constant.sTextColor);
		String str = "Slide to Unlock";
		mUnlockTip.setText(str);
		mUnlockTip.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				ViewUtils.getPXByWidth(40));
		mUnlockTip.setGravity(Gravity.CENTER);
		mUnlockTip.setTypeface(CircleContainer.sTypefaceI);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				ViewUtils.getPXByHeight(138), Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		params.topMargin = ViewUtils.getPXByHeight(Constant.S_DEFAULT_HEIGHT - 138);
		params.leftMargin = ViewUtils.getPXByWidth(54 / 2);
		//mUnlockTip.setBackgroundColor(0x44000000);
		addView(mUnlockTip, params);
	}*/

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		doMethodInSub(this, METHOD_ONSTART);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		doMethodInSub(this, METHOD_ONRESUME);
	}

	@Override
	public void onPause() {
		doMethodInSub(this, METHOD_ONPAUSE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getContext().unregisterReceiver(mChangeReceiver);
		doMethodInSub(this, METHOD_ONDESTROY);
		removeAllViews();
	}

	public void onMonitor(Bundle bundle) {
		// TODO Auto-generated method stub
		mMonitorBundle = bundle;
		doMethodInSub(this, METHOD_ONMONITOR);
	}

	public void updateWeather(Bundle weather) {
		mWeatherBundle = weather;
		doMethodInSub(this, METHOD_ONWEATHERCHANGE);
	}

	public void onDateChange() {
		doMethodInSub(this, METHOD_ONDATECHANGED);
	}

	/**
	 * 
	 * @author shenyaobin
	 * 
	 */
	class DateChangedReceiver extends BroadcastReceiver {
		public DateChangedReceiver() {
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
			intentFilter.addAction(Intent.ACTION_TIME_TICK);
			getContext().registerReceiver(this, intentFilter);
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			onDateChange();
		}
	}

	private void doMethodInSub(ViewGroup vg, int method) {
		for (int i = 0; i < vg.getChildCount(); i++) {
			View v = vg.getChildAt(i);
			try {
				switch (method) {
				case METHOD_ONSTART:
					if (v instanceof ILocker.LiveListener) {
						ILocker.LiveListener iv = (ILocker.LiveListener) v;
						iv.onStart();
					} else if (v instanceof ILocker.OnStartListener) {
						ILocker.OnStartListener iv = (ILocker.OnStartListener) v;
						iv.onStart();
					}
					break;
				case METHOD_ONRESUME:
					if (v instanceof ILocker.LiveListener) {
						ILocker.LiveListener iv = (ILocker.LiveListener) v;
						iv.onResume();
					} else if (v instanceof ILocker.OnResumeListener) {
						ILocker.OnResumeListener iv = (ILocker.OnResumeListener) v;
						iv.onResume();
					}
					break;
				case METHOD_ONPAUSE:
					if (v instanceof ILocker.LiveListener) {
						ILocker.LiveListener iv = (ILocker.LiveListener) v;
						iv.onPause();
					} else if (v instanceof ILocker.OnPauseListener) {
						ILocker.OnPauseListener iv = (ILocker.OnPauseListener) v;
						iv.onPause();
					}
					break;
				case METHOD_ONDESTROY:
					if (v instanceof ILocker.LiveListener) {
						ILocker.LiveListener iv = (ILocker.LiveListener) v;
						iv.onDestroy();
					} else if (v instanceof ILocker.OnDestroyListener) {
						ILocker.OnDestroyListener iv = (ILocker.OnDestroyListener) v;
						iv.onDestroy();
					}
					break;
				case METHOD_ONWEATHERCHANGE:
					if (v instanceof ILocker.OnWeatherChangeListener) {
						ILocker.OnWeatherChangeListener iv = (ILocker.OnWeatherChangeListener) v;
						if (mWeatherBundle != null) {
							iv.onWeatherChange(mWeatherBundle);
						}
					}
				case METHOD_ONMONITOR:
					if (v instanceof ILocker.OnMonitorListener) {
						ILocker.OnMonitorListener iv = (ILocker.OnMonitorListener) v;
						if (mMonitorBundle != null) {
							iv.onMonitor(mMonitorBundle);
						}
					}
				case METHOD_ONDATECHANGED:
					if (v instanceof ILocker.OnDateChangedListener) {
						ILocker.OnDateChangedListener iv = (ILocker.OnDateChangedListener) v;
						iv.onDateChanged();
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {

			}
			if (v instanceof ViewGroup) {
				doMethodInSub((ViewGroup) v, method);
			}
		}
	}
}
