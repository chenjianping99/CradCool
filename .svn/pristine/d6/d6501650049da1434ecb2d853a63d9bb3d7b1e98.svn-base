package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.R;
import com.jiubang.goscreenlock.theme.cjpcardcool.calendar.CalendarEvent;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnDestroyListener;

/**
 * Calendar
 * 
 * @author zhongwenqi
 * 
 */
public class MyCalendarView extends LinearLayout implements OnDestroyListener {

	private CaleView mTableView;
	private String[] mWeekStrs = getResources().getStringArray(R.array.abbweek);
	private String[] mMonthStrs = getResources().getStringArray(R.array.month);

	private Calendar mCalendar;
	private Calendar mTodayCalendar;

	public MyCalendarView(Context context) {
		super(context);
		init();
	}

	private Paint mPaint;
	private void init() {
		if (mPaint == null) {
			mPaint = new Paint();
		}
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setTextSize(ViewUtils.getPXByHeight(28));
		mPaint.setTypeface(CircleContainer.sTypeface);
		mPaint.setAntiAlias(true);
		/*mRect = new Rect();
		mPaint.getTextBounds("0", 0, 1, mRect);*/
		
		mTodayCalendar = Calendar.getInstance();
		mTableView = new CaleView(getContext());
		LayoutParams mTableLP = new LayoutParams(LayoutParams.MATCH_PARENT, 
				ViewUtils.getPXByHeight(300));
		addView(mTableView, mTableLP);
		
	}
	
	private boolean mIs7Line = false;
	public void presMon() {
		mCalendar.add(Calendar.MONTH, -1);
		mTableView.invalidate();
		mIs7Line = false;
	}
	public void nextMon() {
		mCalendar.add(Calendar.MONTH, 1);
		mTableView.invalidate();
		mIs7Line = false;
	}
	
	@Override
	public void onDestroy() {
		if (mTableView != null) {
			mTableView.onDestroy();
		}
		
		mEventDayList.clear();
		mEventDayList = null;
	}
	
	/**
	 * 
	 * @author guojun
	 *
	 */
	private class CaleView extends ImageView implements OnDestroyListener {
		private int mPaddingX, mPaddingY;
		private final int mSIZE20 = ViewUtils.getPXByHeight(20);
		private Bitmap mTodayBitmap, mNotesDay;
		private int mWidth;
		public CaleView(Context context) {
			super(context);
			mCalendar = (Calendar) mTodayCalendar.clone();
			//mTodayBitmap = ViewUtils.getScaleBitmapWithIDByHeight(context, R.drawable.cal_today);
			//mNotesDay = ViewUtils.getScaleBitmapWithIDByHeight(context, R.drawable.cal_note_circle);
			mCalendar.set(Calendar.DAY_OF_MONTH, 1);
			mWidth = ViewUtils.getPXByWidth(460);
			mPaddingX = (mWidth - ViewUtils.getPXByWidth(10)) / 7;
			mPaddingY = ViewUtils.getPXByHeight(45);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			//super.onDraw(canvas);

			/*if (!mIs7Line) {
				mPaddingY = ViewUtils.getPXByHeight(50 + 5);
			}*/
			int year = mCalendar.get(Calendar.YEAR);
			int month = mCalendar.get(Calendar.MONTH);
			
			/*mPaint.setColor(0xff13abde);
			canvas.drawRect(0, 0, getWidth(), mSIZE100, mPaint);*/
			/*mPaint.setColor(CircleContainer.COLOR_WHITE);
			mPaint.setTextSize(ViewUtils.getPXByWidth(32));
			canvas.drawText(year  + "  " +  mMonthStrs[month].toUpperCase(), 
					getWidth() / 2, ViewUtils.getPXByHeight(65), mPaint);
			mPaint.setTextSize(mTextHeight);*/
			
			//week
			for (int i = 0; i < mWeekStrs.length; ++i) {
				if (i == 0 || i == mWeekStrs.length - 1) {
					mPaint.setAlpha(60);
				}
				mPaint.setColor(DateTimeView.TIME_COLOR);
				canvas.drawText(mWeekStrs[i].toUpperCase(), i * mPaddingX + mPaddingX / 2 + mSIZE20, 
						ViewUtils.getPXByHeight(25), mPaint);
				mPaint.setAlpha(255);
			}
			mPaint.setColor(CircleContainer.COLOR_WHITE);
			//canvas.drawBitmap(mLineBitmap, (getWidth() - mLineBitmap.getWidth()) / 2 , mSIZE20 * 9, null);
			
			// 主体 天
			int mY = 2;
			float deltaY = - mSIZE20 * 1.0f;
			while (mCalendar.get(Calendar.MONTH) == month) {
				int day = mCalendar.get(Calendar.DAY_OF_MONTH);
				int x = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
				if (x == -1) {
					x = 6;
				}
				if (x == 0 || x == 6) {
					mPaint.setAlpha(160);
				}
				if (filterEventDay(year, month, day)) {
					mPaint.setStyle(Paint.Style.FILL);
					mPaint.setColor(Color.RED);
					canvas.drawCircle(x * mPaddingX + mPaddingX / 2 + mSIZE20, 
							mY * mPaddingY + ViewUtils.getPXByHeight(-28),
							mPaddingY / 2, mPaint);
					
					mPaint.setColor(Color.WHITE);
					/*canvas.drawBitmap(mNotesDay, 
							x * mPaddingX + ViewUtils.getPXByWidth(30), 
							mY * mPaddingY - ViewUtils.getPXByHeight(47), mPaint);*/
				}
				
				if (mCalendar.equals(mTodayCalendar)) {
					mPaint.setStyle(Paint.Style.STROKE);
					mPaint.setStrokeWidth(ViewUtils.getPXByHeight(2));
					canvas.drawCircle(x * mPaddingX + mPaddingX / 2 + mSIZE20, 
							mY * mPaddingY + ViewUtils.getPXByHeight(-28),
							mPaddingY / 2, mPaint);
					/*canvas.drawBitmap(mTodayBitmap, 
							x * mPaddingX + ViewUtils.getPXByWidth(30), 
							mY * mPaddingY - ViewUtils.getPXByHeight(46), mPaint);*/
					mPaint.setColor(Color.WHITE);
					mPaint.setStyle(Paint.Style.FILL);
					canvas.drawText("" + day, x * mPaddingX + mPaddingX / 2 + mSIZE20, 
							mY * mPaddingY + deltaY, mPaint);
					mPaint.setColor(CircleContainer.COLOR_WHITE);
				} else {
					canvas.drawText("" + day, x * mPaddingX + mPaddingX / 2 + mSIZE20, 
							mY * mPaddingY + deltaY, 
							mPaint);
				}
				mPaint.setAlpha(255);
				
				//增加间距
				/*if (y >= 7) {
					if (mMyCalendar != null) {
						mMyCalendar.setViewHeight(mPaddingY);
					}
				} else {
					if (mMyCalendar != null) {
						mMyCalendar.setViewHeight(0);
					}
				}*/

				/*if (mY >= 7) {
					mPaddingY = ViewUtils.getPXByHeight(50);
					mIs7Line = true;
					invalidate();
				}*/
				
				if (x == 6) {
					++mY;
				}
				
				mCalendar.add(Calendar.DATE, 1);
			}
			mCalendar.set(year, month, 1);
		}

		@Override
		public void onDestroy() {
			ViewUtils.recycleBitmap(mTodayBitmap);
			ViewUtils.recycleBitmap(mNotesDay);
		}
	}
	
	private ArrayList<CalendarEvent> mEventDayList = new ArrayList<CalendarEvent>();
	public void setEventDayList(ArrayList<CalendarEvent> calendarEventList) {
		LogUtils.log(null, "setEventDayList");
		if (calendarEventList != null && mEventDayList != null) {
			mEventDayList.clear();
			mEventDayList.addAll(calendarEventList);
			mTableView.invalidate();
		}
	}
	
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("d", Locale.US);
	private boolean filterEventDay(int year, int month, int day) {
		//LogUtils.log(null, "year = " + year + "month = " + month + "day = " + day);
		boolean ret = false;
		for (CalendarEvent event : mEventDayList) {
			Date d = new Date(event.mStartTime);
			String dayOfMonth =  mDateFormat.format(d);
			//LogUtils.log(null, "d : year = " + d.getYear() + "month = " + d.getMonth() + "day = " + dayOfMonth);
			if (year - 1900 == d.getYear() && month == d.getMonth() && day == Integer.parseInt(dayOfMonth)) {
				ret = true;
			}
		}
		return ret;
	}

}
