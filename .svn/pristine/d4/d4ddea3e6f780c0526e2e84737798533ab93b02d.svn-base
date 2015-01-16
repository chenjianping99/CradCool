package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager.JazzyViewPager;
import com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager.JazzyViewPager.TransitionEffect;
import com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager.ViewPageAdapter;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.unlock.UnlockHomeView;

/**
 * 主题滑动界面
 * 
 * @author chenjianping
 * 
 */
public class ViewPagerSwitcher extends FrameLayout implements ILocker.LiveListener {
	private JazzyViewPager mViewPager;
	private ViewPageAdapter mPageAdapter;
	private List<View> mListViews;
	private Context mContext;
	public ViewPagerSwitcher(Context context) {
		super(context);

		FrameLayout.LayoutParams thisParams = new FrameLayout.LayoutParams(
				ViewUtils.getPXByWidth(680), ViewUtils.getPXByHeight(175), Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		thisParams.topMargin = ViewUtils.getPXByHeight(1280 - 175);
		this.setLayoutParams(thisParams);
				
		mContext = context;
		initViewPager();
	}

	private SwitcherLayout mSwitcherLayout;
	private UnlockHomeView mUnlockHomeView;
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
				// TODO: handle exception
			}
		}
		
		FrameLayout.LayoutParams thisParams = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(mViewPager, thisParams);

		mListViews = new ArrayList<View>();
		mUnlockHomeView = new UnlockHomeView(mContext, mViewPager);
		mSwitcherLayout = new SwitcherLayout(mContext);
		mListViews.add(mUnlockHomeView);
		mListViews.add(mSwitcherLayout);

		mPageAdapter = new ViewPageAdapter(mListViews);
		mPageAdapter.setViewPager(mViewPager);		
		mViewPager.setAdapter(mPageAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				/*mCurrentPage = index % 3;
				ThemeSetProvider.setBackgroundIndex(mContext, mCurrentPage);
				if (mTopSidebarView != null) {
					mTopSidebarView.onClick(null);
				}
				
				int pageCount = mListViews.size();
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
	
	public JazzyViewPager getViewPager() {
		return mViewPager;
	}
	
	/*private TopSidebarView mTopSidebarView;
	private int mCurrentPage;
	public int getCurrentPage() {
		return mCurrentPage;
	}
	
	public void addPagerChangeListener(TopSidebarView leftSidebarView) {
		mTopSidebarView = leftSidebarView;
	}*/
	


	@Override
	public void onPause() {
		if (mSwitcherLayout != null) {
			mSwitcherLayout.onPause();
		}
	}

	@Override
	public void onDestroy() {
		if (mSwitcherLayout != null) {
			mSwitcherLayout.onDestroy();
		}
		if (mUnlockHomeView != null) {
			mUnlockHomeView.onDestroy();
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
		if (mSwitcherLayout != null) {
			mSwitcherLayout.onResume();
		}
	}

}
