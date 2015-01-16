package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager.JazzyViewPager;
import com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager.ViewPageAdapter;
import com.jiubang.goscreenlock.theme.cjpcardcool.music.MusicControlCenter;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnDateChangedListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnMonitorListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnWeatherChangeListener;

/**
 * 主题滑动界面
 * 
 * @author chenjianping
 * 
 */
public class BodyViewPager extends FrameLayout implements ILocker.LiveListener, 
 OnWeatherChangeListener, OnDateChangedListener, OnMonitorListener {
	private JazzyViewPager mViewPager;
	private ViewPageAdapter mPageAdapter;
	private List<View> mListViews;
	private Context mContext;

	public BodyViewPager(Context context) {
		super(context);

		int padding = ViewUtils.getPXByHeight(16);
		FrameLayout.LayoutParams mTotalFLParams = new FrameLayout.LayoutParams(
				Constant.sRealWidth - padding * 2, ViewUtils.getPXByHeight(212), Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		mTotalFLParams.topMargin = ViewUtils.getPXByHeight(510 + 212 + 16);
		setLayoutParams(mTotalFLParams);
		setBackgroundColor(0x33000000);
		
		mContext = context;
		initViewPager();
		addTopSidebarView();
	}

	private PlayLinearView mPlayLinearView;
	private BodySecondView mBodySecondView;
	private void initViewPager() {
		mViewPager = new JazzyViewPager(mContext);
		//mViewPager.setTransitionEffect(TransitionEffect.CubeOut);
		//mViewPager.setPageMargin(10);
		mViewPager.setHorizontalFadingEdgeEnabled(false);

		if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
			try {
				mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
			} catch (Exception e) {
			}
		}
		
		FrameLayout.LayoutParams thisParams = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(mViewPager, thisParams);

		mListViews = new ArrayList<View>();

		mBodySecondView = new BodySecondView(mContext);
		mListViews.add(mBodySecondView);
		
		mPlayLinearView = new PlayLinearView(mContext, mViewPager);
		mListViews.add(mPlayLinearView);

		mPageAdapter = new ViewPageAdapter(mListViews);
		mPageAdapter.setViewPager(mViewPager);
		//mCurrentPage = ThemeSetProvider.getBackgroundIndex(getContext());
		
		mViewPager.setAdapter(mPageAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				//mCurrentPage = index;
				//ThemeSetProvider.setBackgroundIndex(mContext, mCurrentPage);
				if (mTopSidebarView != null) {
					mTopSidebarView.handlePageSelected(index);
				}
				
				/*int pageCount = mListViews.size();
	            if (index == 0){
	                mViewPager.setCurrentItem(pageCount - 1, true);
	            } else if (index == pageCount - 1){
	            	mViewPager.setCurrentItem(1, true);
	            }*/
			}

			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
	private TopSidebarView mTopSidebarView;
	private int mCurrentPage;
	public int getCurrentPage() {
		return mCurrentPage;
	}
	
	public void addPagerChangeListener(TopSidebarView leftSidebarView) {
		mTopSidebarView = leftSidebarView;
	}
	
	@Override
	public void onWeatherChange(Bundle weather) {
		
	}

	@Override
	public void onPause() {
		
		if (mPlayLinearView != null) {
			mPlayLinearView.onPause();
		}
		if (mBodySecondView != null) {
			mBodySecondView.onPause();
		}
	}

	@Override
	public void onDestroy() {
		if (mPlayLinearView != null) {
			mPlayLinearView.onDestroy();
		}
		if (mBodySecondView != null) {
			mBodySecondView.onDestroy();
		}
		
		removeAllViews();
		mContext = null;
		mListViews.clear();
		mListViews = null;
		mViewPager = null;
		mTopSidebarView = null;
		mPageAdapter = null;
	}

	public void onMonitor(Bundle bundle) {

	}

	@Override
	public void onStart() {
		if (mBodySecondView != null) {
			mBodySecondView.onStart();
		}
		
		if (MusicControlCenter.isMusicPlaying(getContext())) {
			mViewPager.setCurrentItem(1, true);
		}
	}
	
	@Override
	public void onResume() {
		
		if (mPlayLinearView != null) {
			mPlayLinearView.onResume();
		}
		if (mBodySecondView != null) {
			mBodySecondView.onResume();
		}
	}

	@Override
	public void onDateChanged() {
		if (mBodySecondView != null) {
			mBodySecondView.onDateChanged();
		}
	}
	
	//解锁开始
	/*
	private float mDelatY = 0, mTouchY, mDelatX, mTouchX = 0;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			onTouch(this, e);
			mTouchX = e.getX();
			mTouchY = e.getY();
			LogUtils.log(null, "mTouchY = " + mTouchY + "mCurrentPage =" + mCurrentPage);
			mDelatY = 0;
			mDelatX = 0;
			break;
		case MotionEvent.ACTION_MOVE:
			mDelatY = e.getY() - mTouchY;
			mDelatX = e.getX() - mTouchX;
			if (Math.abs(mDelatX) < Math.abs(mDelatY)) {
				if (mCurrentPage == 2 && 
						mTouchY > ViewUtils.getPXByHeight(600) && 
						mTouchY < ViewUtils.getPXByHeight(965)) {
					return false;
				} else {
					return true;
				}
			} 
			break;
		}
		//LogUtils.log(null, "mDelatX = " + mDelatX + "; mDelatY =" + mDelatY);

		return false;
	}

	//解锁
	private float mStartY, mDisY;
	private boolean mUnlockable;
	private FrameLayout.LayoutParams mThisParams = null;
	private int mMargin;
	private float mAlpha = 1;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mStartY = event.getRawY();
			mDisY = 0;
			mUnlockable = false;
			mThisParams = (LayoutParams) v.getLayoutParams();
			mMargin = mThisParams.topMargin;
			//LogUtils.log(null, "mThisParams = " + mThisParams);
			mAlpha = 1;
			break;
		case MotionEvent.ACTION_MOVE:
			mDisY = (int) (event.getRawY() - mStartY);
			//LogUtils.log(null, "mDisX = " + mDisX + "; mDisY =" + mDisY);
			
			if (mDisY > 0) {
				mDisY = 0;
			} else {
				if (mDisY < - Constant.sRealHeight / 5) {
					if (!mUnlockable && Constant.sIsquake) {
						Global.getvibrator(getContext());
					}
					mUnlockable = true;
				} else {
					mUnlockable = false;
				}
			}
			mThisParams.topMargin = (int) (mMargin + mDisY);
			//LogUtils.log(null, "mThisParams.rightMargin = " + mThisParams.rightMargin);
			if (mViewPager != null) {
				mAlpha = 1 - Math.abs(mDisY) /  Constant.sRealHeight / 3;
				ViewHelper.setAlpha(mViewPager, mAlpha);
			}
			setLayoutParams(mThisParams);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mThisParams.topMargin = mMargin;
			setLayoutParams(mThisParams);
			if (mUnlockable) {
				Global.sendUnlockWithIntent(getContext(), "home", null, null);
				if (mCurrentPage != 1) {
					ThemeSetProvider.setBackgroundIndex(mContext, 0);
				}
			} else {
				ViewHelper.setAlpha(mViewPager, 1);
				TranslateAnimation t = new TranslateAnimation(0, 0, mDisY, 0);
				t.setDuration((int) (100 * Math.abs(mDisY) / ViewUtils.getPXByHeight(200)) + 200);
				t.setInterpolator(new OvershootInterpolator());
				startAnimation(t);					
			} 
			break;
		}
		return true;
	}*/
	
	private void addTopSidebarView() {
		TopSidebarView leftsidebar = new TopSidebarView(mContext, mViewPager);
		this.addPagerChangeListener(leftsidebar);
		addView(leftsidebar);
	}
	
	/*private View mUnlockIcon;
	private void addUnlockIcon() {
		mUnlockIcon = new View(getContext());
		mUnlockIcon.setBackgroundResource(R.drawable.unlock_tip);
		FrameLayout.LayoutParams thisParams = new FrameLayout.LayoutParams(
				ViewUtils.getPXByHeight(270), ViewUtils.getPXByHeight(40), Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		thisParams.topMargin = Constant.sRealHeight - ViewUtils.getPXByHeight(60);
		addView(mUnlockIcon, thisParams);
	}*/

	
}
