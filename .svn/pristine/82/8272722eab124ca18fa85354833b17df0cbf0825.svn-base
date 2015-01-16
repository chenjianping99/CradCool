/**
 * 
 */
package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnDestroyListener;

/**
 * @author chenjianping
 *
 */
public class MyCalendar extends FrameLayout implements OnClickListener, OnDestroyListener {
	
	private int mWidth, mHeight;
	FrameLayout.LayoutParams mThisParams;
	
	private boolean mIsCaleEvent;
	public MyCalendar(Context context, boolean b) {
		super(context);
		
		mIsCaleEvent = b;

		mWidth = ViewUtils.getPXByWidth(460);
		mHeight = ViewUtils.getPXByHeight(180);
		mThisParams = new FrameLayout.LayoutParams(
				mWidth, mHeight, Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		mThisParams.rightMargin = ViewUtils.getPXByWidth(16);
		setLayoutParams(mThisParams);
		init();
	}

	/*private View mBg;
	private void addCoverBg(Context context) {
		mBg = new View(context);
		FrameLayout.LayoutParams thisParams = new FrameLayout.LayoutParams(
				mWidth, mHeight, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		addView(mBg, thisParams);
		thisParams.topMargin =  ViewUtils.getPXByHeight(6);
		//mBg.setBackgroundDrawable(ViewUtils.getDrawable(context, R.drawable.cal_bg));
		mBg.setBackgroundResource(R.drawable.cal_bg);
		
		View v = new View(getContext());
		v.setBackgroundResource(R.drawable.wea_bg);
		addView(v, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}*/

	//private ImageView mPres, mNext;
	private MyCalendarView mCalendar;
	private void init() {
		if (!mIsCaleEvent) {
			// 日历
			ScrollView mScorll = new ScrollView(getContext());
			//mScorll.setBackgroundColor(0x33000000);
			mScorll.setFadingEdgeLength(0);
			mScorll.setVerticalFadingEdgeEnabled(false);
			mScorll.setVerticalScrollBarEnabled(false);
			mScorll.setFadingEdgeLength(0);
			addView(mScorll, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			
			mCalendar = new MyCalendarView(getContext());
			mScorll.addView(mCalendar);
		}
		
		// 事件
		addEventView();

		
		/*int w = ViewUtils.getPXByHeight(36);
		int h = ViewUtils.getPXByHeight(36);
		mPres = new ImageView(getContext());
		//mPres.setImageResource(R.drawable.cal_pre);
		//mPres.setBackgroundColor(0x33000000);
		LayoutParams mPresLP = new LayoutParams(w * 3, h * 2, Gravity.LEFT | Gravity.TOP);
		mPresLP.leftMargin = - ViewUtils.getPXByWidth(10);
		mPresLP.topMargin = ViewUtils.getPXByHeight(15);
		mPres.setPadding(w * 1, h / 2, w * 1, h / 2);

		mNext = new ImageView(getContext());
		//mNext.setImageResource(R.drawable.cal_next);
		//mNext.setBackgroundColor(0x33000000);
		LayoutParams mNextLP = new LayoutParams(w * 3, h * 2,  Gravity.RIGHT | Gravity.TOP);
		mNextLP.topMargin = mPresLP.topMargin;
		mNextLP.rightMargin = mPresLP.leftMargin;
		mNext.setPadding(w * 1, h / 2, w * 1, h / 2);

		addView(mPres, mPresLP);
		addView(mNext, mNextLP);

		mPres.setOnClickListener(this);
		mNext.setOnClickListener(this);*/
	}
	
	private MyCalendarEventView mEventView;
	private LayoutParams mEventLP;
	private void addEventView() {
		mEventView = new MyCalendarEventView(getContext(), this);
		mEventLP = new LayoutParams(mWidth, 
				ViewUtils.getPXByHeight(260), 
				Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		mEventLP.bottomMargin = ViewUtils.getPXByHeight(110);
		//mEventView.setBackgroundColor(0x550000ff);

		if (mIsCaleEvent) {
			addView(mEventView, mEventLP);
		} 
	}
	
	public void setViewHeight(int subH) {
		/*if (mEventLP.height != ViewUtils.getPXByHeight(260) - subH) {
			mEventLP.height = ViewUtils.getPXByHeight(260) - subH;
			mEventView.setLayoutParams(mEventLP);
		}*/
		/*if (mThisParams.height != mHeight + subH) {
			mThisParams.height = mHeight + subH;
			setLayoutParams(mThisParams);
		}*/
	}

	@Override
	public void onClick(View v) {
		/*if (v == mPres) {
			if (mCalendar != null) {
				mCalendar.presMon();
			}
		} else if (v == mNext) {
			if (mCalendar != null) {
				mCalendar.nextMon();
			}
		}*/
	}

	public void onQueryFinish() {
		LogUtils.log(null, "101:onQueryFinish");
		if (mEventView != null && mCalendar != null) {
			mCalendar.setEventDayList(mEventView.getCalendarEventList());
		}
	}

	@Override
	public void onDestroy() {
		if (mCalendar != null) {
			mCalendar.onDestroy();
		}
	}
}
