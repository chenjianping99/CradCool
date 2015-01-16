package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.LiveListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnDateChangedListener;
/**
 * 
 * @author chenjianping
 *
 */
public class BodySecondView extends FrameLayout implements LiveListener, 
	OnDateChangedListener {
	private Context mContext;

	public BodySecondView(Context context) {
		super(context);
		mContext = context;
		
		
		addDateView();
		addCaleView();
	}
	
	private FrameLayout mDateFL;
	private TextView mDayText, mDateText, mWeek;
	private void addDateView() {
		mDateFL = new FrameLayout(mContext);
		LayoutParams mLP = new LayoutParams(
				ViewUtils.getPXByWidth(183), ViewUtils.getPXByHeight(180), Gravity.LEFT | Gravity.CENTER_VERTICAL);
		mLP.leftMargin = ViewUtils.getPXByWidth(16);
		mDateFL.setBackgroundColor(0x19000000);
		addView(mDateFL, mLP);
		
		mDayText = new TextView(mContext);
		mDayText.setTextColor(Color.WHITE);
		mDayText.setTypeface(CircleContainer.sTypeface);
		mDayText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByWidth(70));
		LayoutParams mDayTextLP = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.RIGHT);
		mDayTextLP.topMargin = ViewUtils.getPXByHeight(-5);
		mDayTextLP.rightMargin = ViewUtils.getPXByWidth(16);
		mDateFL.addView(mDayText, mDayTextLP);
		
		mDateText = new TextView(mContext);
		mDateText.setTextColor(Color.WHITE);
		mDateText.setTypeface(CircleContainer.sTypeface);
		mDateText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByWidth(30));
		LayoutParams mDateTextLP = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		mDateTextLP.rightMargin = mDayTextLP.rightMargin;
		mDateTextLP.topMargin = ViewUtils.getPXByHeight(20);
		mDateFL.addView(mDateText, mDateTextLP);
		
		mWeek = new TextView(mContext);
		mWeek.setTextColor(Color.WHITE);
		mWeek.setTypeface(CircleContainer.sTypeface);
		mWeek.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByWidth(30));
		LayoutParams mWeekLP = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.BOTTOM);
		mWeekLP.bottomMargin = ViewUtils.getPXByHeight(5);
		mWeekLP.rightMargin = mDayTextLP.rightMargin;
		mDateFL.addView(mWeek, mWeekLP);

		updateTime();
	}
	
	
	private MyCalendar mMyCalendar;
	private void addCaleView() {
		mMyCalendar = new MyCalendar(mContext, false);
		//mMyCalendar.setBackgroundColor(0x22000000);
		addView(mMyCalendar);
	}

	@Override
	public void onStart() {

	}
	@Override
	public void onResume() {

	}
	
	@Override
	public void onPause() {

	}
	@Override
	public void onDestroy() {

		if (mMyCalendar != null) {
			mMyCalendar.onDestroy();
		}
		
		removeAllViews();
		mDateFL = null;
		mContext = null;
	}
	
	@Override
	public void onDateChanged() {
		updateTime();
	}

	private void updateTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("EEEE", Locale.US);
		
		String formatDate = "MM/dd/yyyy";
		if (Constant.sDateFormat != null && !Constant.sDateFormat.equals("")
				&& !Constant.sDateFormat.equals("Default")
				&& !Constant.sDateFormat.equals("default")) {
			formatDate = Constant.sDateFormat;
		} else {
		}
		
		format.applyPattern(formatDate);
		mDateText.setText(format.format(date)/*.toUpperCase()*/);
		
		String formatWeek = "EEEE";
		format.applyPattern(formatWeek);
		mWeek.setText("" + format.format(date));
		
		String formatDay = "dd";
		format.applyPattern(formatDay);
		mDayText.setText("" + format.format(date));
	
	}

}
