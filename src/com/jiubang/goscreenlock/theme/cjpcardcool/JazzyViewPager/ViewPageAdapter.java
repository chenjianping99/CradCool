package com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * 
 * <br>
 * 类描述: <br>
 * 功能详细描述:
 * 
 * @author zhangfanghua
 * @date [2013-01-23]
 */
public class ViewPageAdapter extends PagerAdapter {

	private final static String TAG = "MyPagerAdapter";
	List<View> mViewList;
	JazzyViewPager mViewPager;
	public ViewPageAdapter(List<View> viewList) {
		mViewList = viewList;
	}
	
	public void setViewPager(JazzyViewPager pager) {
		mViewPager = pager;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mViewList != null) {
			return mViewList.size();
		}

		return 0;
	}

	@Override
	public Object instantiateItem(View view, int index) {
		// TODO Auto-generated method stub    
		((JazzyViewPager) view).addView(mViewList.get(index), 0);
		if (mViewPager != null) {
			mViewPager.setObjectForPosition(mViewList.get(index), index);
		}
		return mViewList.get(index);
	}

	@Override
	public void destroyItem(View view, int position, Object arg2) {

		((JazzyViewPager) view).removeView(mViewList.get(position));
	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return view == obj;

	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub

	}
}
