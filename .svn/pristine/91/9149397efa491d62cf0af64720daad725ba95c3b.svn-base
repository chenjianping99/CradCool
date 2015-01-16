package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager.JazzyViewPager;
import com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager.JazzyViewPager.TransitionEffect;
import com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager.ViewPageAdapter;
import com.jiubang.goscreenlock.theme.cjpcardcool.music.MusicControlCenter;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;

/**
 * 主题滑动界面
 * 
 * @author chenjianping
 * 
 */
public class ViewPagerMusic extends FrameLayout implements ILocker.LiveListener {
	private JazzyViewPager mViewPager;
	private ViewPageAdapter mPageAdapter;
	private List<View> mListViews;
	private Context mContext;
	public ViewPagerMusic(Context context) {
		super(context);

		FrameLayout.LayoutParams thisParams = new FrameLayout.LayoutParams(
				ViewUtils.getPXByWidth(680), ViewUtils.getPXByHeight(208), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		thisParams.bottomMargin = ViewUtils.getPXByHeight(175);
		this.setLayoutParams(thisParams);
				
		mContext = context;
		initViewPager();
	}

	private PlayLinearView mPlayLinearView;
	private MusicEffectView mMusicEffectView;
	private void initViewPager() {
		mViewPager = new JazzyViewPager(mContext);
		mViewPager.setTransitionEffect(TransitionEffect.CubeOut);
		mViewPager.setPageMargin(-8);
		mViewPager.setHorizontalFadingEdgeEnabled(false);
		mViewPager.setFadeEnabled(true);

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
		
		mPlayLinearView = new PlayLinearView(mContext, mViewPager);
		mListViews.add(mPlayLinearView);
		mMusicEffectView = new MusicEffectView(mContext);
		mListViews.add(mMusicEffectView);
		
		if (MusicControlCenter.isMusicPlaying(getContext())) {
			LogUtils.log(null, "isPlaying");
			mViewPager.setCurrentItem(1);
		}

		mPageAdapter = new ViewPageAdapter(mListViews);
		mPageAdapter.setViewPager(mViewPager);	
		mViewPager.setAdapter(mPageAdapter);
	}

	@Override
	public void onPause() {
		if (mPlayLinearView != null) {
			mPlayLinearView.onPause();
		}
	}

	@Override
	public void onDestroy() {

		if (mPlayLinearView != null) {
			mPlayLinearView.onDestroy();
		}
		if (mMusicEffectView != null) {
			mMusicEffectView.onDestroy();
		}
		
		removeAllViews();
		mContext = null;
		mListViews.clear();
		mListViews = null;
		mViewPager = null;
		mPageAdapter = null;
	}

	@Override
	public void onStart() {
	}
	
	@Override
	public void onResume() {
		if (mPlayLinearView != null) {
			mPlayLinearView.onResume();
		}
		if (mMusicEffectView != null) {
			mMusicEffectView.onResume();
		}
	}

}
