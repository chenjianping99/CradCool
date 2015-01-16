package com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;

/**
 * @author chenjianping
 */
public class InfinitePagerAdapter extends PagerAdapter {
	private List<View> mViewList;
	private JazzyViewPager mViewPager;
	public InfinitePagerAdapter(List<View> viewList) {
		mViewList = viewList;
	}
	
	public void setViewPager(JazzyViewPager pager) {
		mViewPager = pager;
	}

    @Override
    public int getCount() {
        // warning: scrolling to very high values (1,000,000+) results in
        // strange drawing behaviour
		if (mViewPager != null) {
			return Integer.MAX_VALUE;
		} else if (mViewList != null) {
			return mViewList.size();
		}
		return 0;
    }

	@Override
	public Object instantiateItem(ViewGroup view, int index) {
		// TODO Auto-generated method stub    
		LogUtils.log(null, "instantiateItem = index" + index);
		if (mViewPager != null) {
			int virtualPosition = index % getRealCount();
			mViewPager.setObjectForPosition(mViewList.get(virtualPosition), index);
			try {
				((ViewPager) view).removeView(mViewList.get(virtualPosition));
			} catch (Exception e) {
				return mViewList.get(virtualPosition);
			}
			
			((ViewPager) view).addView(mViewList.get(virtualPosition), 0);
			return mViewList.get(virtualPosition);
		} else {
			((ViewPager) view).addView(mViewList.get(index), 0);
			return mViewList.get(index);
			
		}
	}

	@Override
	public void destroyItem(View view, int position, Object arg2) {
		LogUtils.log(null, "destroyItem: position =" + position);
		if (mViewPager != null) {
			int virtualPosition = position % getRealCount();
			((ViewPager) view).removeView(mViewList.get(virtualPosition));
		} else {
			((ViewPager) view).removeView(mViewList.get(position));
		}
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
	
	// add
	/**
     * @return the {@link #getCount()} result of the wrapped adapter
     */
    public int getRealCount() {
    	if (mViewList != null) {
			return mViewList.size();
		}
		return 0;
    }

}
