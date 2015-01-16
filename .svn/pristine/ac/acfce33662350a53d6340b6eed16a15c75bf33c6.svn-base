package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager.JazzyViewPager;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnDestroyListener;
/**
 * 
 * @author chenjianping
 *
 */
public class TopSidebarView extends FrameLayout implements OnClickListener, OnDestroyListener {

	private JazzyViewPager mViewPager;
	private int mCurrentItem;
	public TopSidebarView(Context context, JazzyViewPager pager) {
		super(context);

		mViewPager = pager;
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewUtils.getPXByWidth(200), 
				LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		params.bottomMargin = ViewUtils.getPXByHeight(120);
		setLayoutParams(params);
		//setBackgroundColor(0x44ffffff);
		initTop3Icon(context);
	}
	
	private LinearLayout mTopLinearLayout;
	private LayoutParams mTopLinearLayoutParams;
	private int mIconWidth = ViewUtils.getPXByWidth(15);
	private int mBlack = 0xff000000;
	private TextView mApp, mHome/*, mOther*/;
	private void initTop3Icon(Context context) {
		mTopLinearLayout = new LinearLayout(context);
		mTopLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		mTopLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		mTopLinearLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.WRAP_CONTENT, Gravity.TOP);
		addView(mTopLinearLayout, mTopLinearLayoutParams);
		
		mApp = new TextView(context);
		mApp.setTextColor(mBlack);
		mApp.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByWidth(22));
		mApp.setTypeface(CircleContainer.sTypeface);
		mApp.setGravity(Gravity.CENTER);
		//mApp.setText("Apps");
        /*Drawable icon = getResources().getDrawable(R.drawable.icon_chosen_off);
		icon.setBounds(0, 0, mIconWidth, mIconWidth / 10);
		mApp.setCompoundDrawables(null, icon, null, null);
		mApp.setCompoundDrawablePadding(ViewUtils.getPXByWidth(5));*/
		
		mTopLinearLayout.addView(mApp);
		mTopLinearLayout.addView(addLine());
		
		mHome = new TextView(context);
		mHome.setTextColor(mBlack);
		mHome.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByWidth(22));
		mHome.setTypeface(CircleContainer.sTypeface);
		mHome.setGravity(Gravity.CENTER);
		//mHome.setText("Home");
        /*Drawable iconHome = getResources().getDrawable(R.drawable.icon_chosen_off);
        iconHome.setBounds(0, 0, mIconWidth, mIconWidth / 10);
        mHome.setCompoundDrawables(null, iconHome, null, null);
        mHome.setCompoundDrawablePadding(ViewUtils.getPXByWidth(5));*/
        mTopLinearLayout.addView(mHome);
		//mTopLinearLayout.addView(addLine());
		
		/*mOther = new TextView(context);
		mOther.setTextColor(mBlack);
		mOther.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByWidth(22));
		mOther.setTypeface(CircleContainer.sTypeface);
		mOther.setGravity(Gravity.CENTER);
		//mOther.setText("Other");
        Drawable iconOther = getResources().getDrawable(R.drawable.icon_chosen_off);
        iconOther.setBounds(0, 0, mIconWidth, mIconWidth / 10);
        mOther.setCompoundDrawables(null, iconOther, null, null);
        mOther.setCompoundDrawablePadding(ViewUtils.getPXByWidth(5));
        mTopLinearLayout.addView(mOther);*/
        
        mApp.setOnClickListener(this);
        mHome.setOnClickListener(this);
        //mOther.setOnClickListener(this);
        //updateIcon(mApp, R.drawable.icon_chosen_on, Color.WHITE);
		//updateIcon(mHome, R.drawable.icon_chosen_off, mBlack);
	}
	
	private View addLine() {
		View v = new View(getContext());
		//v.setBackgroundColor(mBlack);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mIconWidth * 2, mIconWidth);
		/*params.leftMargin = ViewUtils.getPXByWidth(10);
		params.rightMargin = params.leftMargin;*/
		v.setLayoutParams(params);
		v.setVisibility(INVISIBLE);
		return v;
	}
	
	private void updateIcon(TextView v, int resId, int color) {
		if (v != null) {
			v.setTextColor(color);
			Drawable iconHome = getResources().getDrawable(resId);
	        iconHome.setBounds(0, 0, mIconWidth, mIconWidth);
	        v.setCompoundDrawables(null, iconHome, null, null);
	        v.setCompoundDrawablePadding(ViewUtils.getPXByWidth(5));
		}
	}
	
	public void handlePageSelected(int index) {
		mCurrentItem = index;
		onClick(null);
	}
	
	@Override
	public void onClick(View v) {
		if (mViewPager != null) {
			if (v == mApp || v == null && mCurrentItem == 0) {
				//updateIcon(mApp, R.drawable.icon_chosen_on, Color.WHITE);
				//updateIcon(mHome, R.drawable.icon_chosen_off, mBlack);
				//updateIcon(mOther, R.drawable.icon_chosen_off, mBlack);
				if (v != null) {
					mViewPager.setCurrentItem(0);
				}
			} else if (v == mHome  || v == null && mCurrentItem == 1) {
				//updateIcon(mApp, R.drawable.icon_chosen_off, mBlack);
				//updateIcon(mHome, R.drawable.icon_chosen_on, Color.WHITE);
				//updateIcon(mOther, R.drawable.icon_chosen_off, mBlack);
				if (v != null) {
					mViewPager.setCurrentItem(1);
				}
			} /*else if (v == mOther  || v == null && mCurrentItem == 2) {
				updateIcon(mApp, R.drawable.icon_chosen_off, mBlack);
				updateIcon(mHome, R.drawable.icon_chosen_off, mBlack);
				updateIcon(mOther, R.drawable.icon_chosen_on, Color.WHITE);
				if (v != null) {
					mViewPager.setCurrentItem(2);
				}
			}*/
		}
	}

	@Override
	public void onDestroy() {
		removeAllViews();
		mViewPager = null;
	}	
}
