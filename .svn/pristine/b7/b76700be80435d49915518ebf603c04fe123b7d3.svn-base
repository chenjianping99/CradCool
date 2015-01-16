package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jiubang.goscreenlock.theme.cjpcardcool.R;
import com.jiubang.goscreenlock.theme.cjpcardcool.calendar.CalendarEvent;
import com.jiubang.goscreenlock.theme.cjpcardcool.calendar.CalendarJumper;
import com.jiubang.goscreenlock.theme.cjpcardcool.calendar.CalendarQueryHandler;
import com.jiubang.goscreenlock.theme.cjpcardcool.calendar.OnQueryDataListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;

/**
 * 
 * @author chenjianping
 *
 */
public class MyCalendarEventView extends FrameLayout implements OnQueryDataListener, OnClickListener
{
	CalendarQueryHandler	mCalendarQueryHandler;
	TextView				mActionView, mTimeView;
	TextView 				mAddNotesTip;
	//View						mAddIcon;
	
	private ListView mListView;
	private ArrayList<CalendarEvent> mAgendaBeanList = new ArrayList<CalendarEvent>();
	private MyAdapter mMyAdapter;
	private MyCalendar mMyCalendar;
	public MyCalendarEventView(Context context, MyCalendar v)
	{
		super(context);
		
		int padding = ViewUtils.getPXByHeight(16);
		int mWidth = Constant.sRealWidth - padding * 2;
		int mHeight = ViewUtils.getPXByHeight(152);
		LayoutParams mThisParams = new FrameLayout.LayoutParams(
				mWidth, mHeight, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		mThisParams.topMargin = ViewUtils.getPXByHeight(510 + (212 + 16) * 2);
		setLayoutParams(mThisParams);
		setBackgroundColor(0x33000000);
		
		addIndexView(context);
		
		mMyCalendar = v;
		mAddNotesTip = new TextView(context);
		mAddNotesTip.setTextColor(Color.WHITE);
		mAddNotesTip.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				ViewUtils.getPXByWidth(30));
		Drawable locationIcon = getResources().getDrawable(R.drawable.add_notes);
		int h = ViewUtils.getPXByHeight(55);
		locationIcon.setBounds(0, 0, h, h);
		mAddNotesTip.setCompoundDrawables(null, locationIcon, null, null);
		mAddNotesTip.setCompoundDrawablePadding(5);
		mAddNotesTip.setGravity(Gravity.CENTER);
		LayoutParams number = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		addView(mAddNotesTip, number);
		mAddNotesTip.setText("Add Notes");
		mAddNotesTip.setTypeface(CircleContainer.sTypeface);
		mAddNotesTip.setOnClickListener(this);
		
		/*int margins = ViewUtils.getPXByWidth(4);
		int w = Math.round(ViewUtils.getDimensionByWidth(context, R.drawable.add_notes).width);
		mAddIcon = new View(context);
		mAddIcon.setBackgroundResource(R.drawable.add_notes);
		LayoutParams mAddIconParam = new LayoutParams(w, w,
				Gravity.RIGHT | Gravity.TOP);
		//mAddIconParam.topMargin = margins;
		mAddIconParam.rightMargin = margins;
		addView(mAddIcon, mAddIconParam);
		mAddIcon.setVisibility(INVISIBLE);
		mAddIcon.setOnClickListener(this);*/
		
		int margins = ViewUtils.getPXByHeight(10);
		mListView = new ListView(context);
		LayoutParams pager = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
		pager.setMargins(0, 0, 0, margins);
		addView(mListView, pager);
		mMyAdapter = new MyAdapter();
		mListView.setAdapter(mMyAdapter);
		mListView.setFadingEdgeLength(0);
		mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mListView.setVerticalFadingEdgeEnabled(false);
		mListView.setVerticalScrollBarEnabled(false);
		//mListView.setDivider(getResources().getDrawable(R.drawable.cal_line));
		mListView.setFooterDividersEnabled(false);
		mListView.setDividerHeight(0);
		
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				/*switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					isIdle = true;
					break;
				default:
					isIdle = false;
					break;
				}*/
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if (mIndexView != null) {
					int curIndex = firstVisibleItem + 1;
					if (firstVisibleItem > 0 && visibleItemCount > 1) {
						curIndex++;
					}
					mIndexView.setText(curIndex + "/" + totalItemCount);
				}
			}
		});
						
		mCalendarQueryHandler = new CalendarQueryHandler(context.getContentResolver(), this);
		mCalendarQueryHandler.startQuery();
	}
	
	private TextView mIndexView;
	private void addIndexView(Context context) {
		mIndexView = new TextView(context);
		mIndexView.setTypeface(CircleContainer.sTypeface);
		mIndexView.setTextColor(Color.WHITE);
		mIndexView.setBackgroundColor(0x33000000);
		int padding = ViewUtils.getPXByWidth(5);
		mIndexView.setPadding(padding, 0, padding, 0);
		mIndexView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				ViewUtils.getPXByWidth(20));
		LayoutParams number = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.RIGHT);
		addView(mIndexView, number);
		mIndexView.setVisibility(INVISIBLE);
	}
	
	@Override
	public void onQueryFinish(boolean isAsync, Object newestEvent, Object events)
	{
		mAgendaBeanList.clear();
		mAgendaBeanList.addAll((Collection<? extends CalendarEvent>) events);
		mMyAdapter.notifyDataSetChanged();
		LogUtils.log(null, "onQueryFinish");
		if (mMyCalendar != null) {
			mMyCalendar.onQueryFinish();
		}
	}
	
	@Override
	public void onClick(View v)
	{
		if (Constant.sIsquake)
		{
			Global.getvibrator(getContext());
		}
		Global.sendUnlockWithIntent(getContext(), null, null, null, CalendarJumper
				.getJumper().getMainIntent(getContext()));

	}

	/**
	 * 
	 * @author chenjianping
	 * 
	 */
	class MyAdapter extends BaseAdapter {

		SimpleDateFormat	mDateFormat;
		private int margins = ViewUtils.getPXByWidth(25);
		@Override
		public int getCount() {
			int count = mAgendaBeanList.size();
			if (count > 0) {
				mAddNotesTip.setVisibility(GONE);
				mListView.setVisibility(VISIBLE);
				//mAddIcon.setVisibility(VISIBLE);
				mIndexView.setVisibility(VISIBLE);
			} else {
				mListView.setVisibility(GONE);
				mIndexView.setVisibility(GONE);
				//mAddIcon.setVisibility(GONE);
				mAddNotesTip.setVisibility(VISIBLE);
			}
			return count;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mAgendaBeanList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Context c = getContext();
			LinearLayout itemView = null;
			if (convertView == null) {
				itemView = new LinearLayout(c);
				itemView.setOrientation(LinearLayout.VERTICAL);
				itemView.setGravity(Gravity.CENTER_VERTICAL);
				AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 
						LayoutParams.WRAP_CONTENT);
				itemView.setLayoutParams(params);
				itemView.setPadding(margins, margins / 2, margins, margins / 2);

				TextView time = new TextView(c);
				time.setTag("time");
				time.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						ViewUtils.getPXByWidth(28));
				time.setTypeface(CircleContainer.sTypeface/*, Typeface.BOLD*/);
				/*Drawable locationIcon = getResources().getDrawable(R.drawable.icon_cal);
				int w = ViewUtils.getPXByHeight(42);
				int h = ViewUtils.getPXByHeight(46);
				locationIcon.setBounds(0, 0, w, h);
				time.setCompoundDrawables(locationIcon, null, null, null);
				time.setCompoundDrawablePadding(ViewUtils.getPXByWidth(10));*/
				time.setGravity(Gravity.CENTER);
				
				LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				time.setGravity(Gravity.LEFT | Gravity.TOP);
				//timeParams.leftMargin = margins;
				itemView.addView(time, timeParams);

				TextView title = new TextView(c);
				title.setTag("title");
				title.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						ViewUtils.getPXByWidth(30));
				title.setTypeface(CircleContainer.sTypeface/*, Typeface.BOLD*/);
				LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				title.setGravity(Gravity.LEFT | Gravity.TOP);
				//titleParams.leftMargin = ViewUtils.getPXByHeight(37) + timeParams.leftMargin + ViewUtils.getPXByWidth(10);
				itemView.addView(title, titleParams);
			} else {
				itemView = (LinearLayout) convertView;
			}
			
			if (mDateFormat == null) {
				mDateFormat = new SimpleDateFormat("MM/dd  HH:mm");
			}

			CalendarEvent calendar = mAgendaBeanList.get(position);
			if (calendar != null) {
				TextView time = (TextView) itemView.findViewWithTag("time");
				time.setTextColor(Color.WHITE);
				String timeText = mDateFormat.format(new Date(calendar.mStartTime));
				time.setText(timeText);
				
				TextView title = (TextView) itemView.findViewWithTag("title");
				title.setTextColor(Color.WHITE);
				//LogUtils.log(null, "calendar.mTitle=" + calendar.mTitle);
				title.setText(calendar.mTitle);
			}
			return itemView;
		}
	}
	
	//获取日历事件列表
	public ArrayList<CalendarEvent> getCalendarEventList() {
		return mAgendaBeanList;
	}
}
