package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager.JazzyViewPager;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.LiveListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnDateChangedListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnMonitorListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnWeatherChangeListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.unlock.UnlockViews;
/**
 * 
 * @author chenjianping
 *
 */
public class BodyHomeView extends FrameLayout implements LiveListener, OnDateChangedListener, 
	OnMonitorListener, OnWeatherChangeListener {
	private Context mContext;
	private JazzyViewPager mBodyViewPager;
	private FrameLayout mBody;
	public BodyHomeView(Context context, JazzyViewPager v) {
		super(context);
		mContext = context;
		//setBackgroundDrawable(ViewUtils.getDrawable(mContext, R.drawable.bg));
		mBody = new FrameLayout(mContext);
		/*int w = ViewUtils.getPXByWidth(662);
		int h = ViewUtils.getPXByHeight(953);
		FrameLayout.LayoutParams thisParams = new FrameLayout.LayoutParams(
				w, h, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		thisParams.topMargin = ViewUtils.getPXByHeight(160);
		addView(mBody, thisParams);
		mBody.setBackgroundResource(R.drawable.home_bg);*/
		addView(mBody);
		mBodyViewPager = v;
		addDateTimeView();
		addUnlockViews();
		addWeatherView();
		addMumView();
	}
	
	private UnlockViews mUnlockViews;
	private void addUnlockViews() {
		mUnlockViews = new UnlockViews(mContext, mBodyViewPager);
		mBody.addView(mUnlockViews, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
	}

	private WeatherView mWeatherView;
	private void addWeatherView() {
		mWeatherView = new WeatherView(mContext);
		mBody.addView(mWeatherView);
	}

	private DateTimeView mDateTimeView;
	private void addDateTimeView() {
		mDateTimeView = new DateTimeView(mContext);
		mBody.addView(mDateTimeView);
	}
	
	private MumView mMumView;
	private void addMumView() {
		mMumView = new MumView(mContext);
		mBody.addView(mMumView);
	}
		

	@Override
	public void onStart() {

	}
	@Override
	public void onResume() {
		if (mUnlockViews != null) {
			mUnlockViews.onResume();
		}
		if (mMumView != null) {
			mMumView.onResume();
		}
	}
	
	@Override
	public void onPause() {
		if (mMumView != null) {
			mMumView.onPause();
		}
	}
	@Override
	public void onDestroy() {
		if (mUnlockViews != null) {
			mUnlockViews.onDestroy();
		}
		if (mMumView != null) {
			mMumView.onDestroy();
		}
		
		mBodyViewPager = null;
		removeAllViews();
		mBody = null;
		mContext = null;
	}
	@Override
	public void onMonitor(Bundle bundle) {

		if (mUnlockViews != null) {
			mUnlockViews.onMonitor(bundle);
		}
		if (mMumView != null) {
			mMumView.onMonitor(bundle);
		}
	}
	
	@Override
	public void onDateChanged() {
		if (mDateTimeView != null) {
			mDateTimeView.onDateChanged();
		}
	}

	@Override
	public void onWeatherChange(Bundle weather) {
		if (mWeatherView != null) {
			mWeatherView.onWeatherChange(weather);
		}
	}

}
