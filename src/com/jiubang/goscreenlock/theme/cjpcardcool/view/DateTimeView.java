package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jiubang.goscreenlock.theme.cjpcardcool.R;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnDateChangedListener;

/**
 * 
 * @author chenjianping
 * 
 */
public class DateTimeView extends FrameLayout implements OnDateChangedListener
 	/*OnResumeListener, OnPauseListener */ {
	
	private TextView mTimeText, mAlarmText/*mTimeText2, mDateText, mBattery*/;
	/*public final static int[] TIME_COLOR = {0xfff3c554, 0xff24acd6, 0xffe47fab};
	public final static int DATE_COLOR = 0xff161616;*/
	public final static int TIME_COLOR = 0xffffffff;
	
	public DateTimeView(Context context) {
		super(context);

		int width = ViewUtils.getPXByWidth(336);
		int height = ViewUtils.getPXByHeight(212);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,
				height, Gravity.RIGHT | Gravity.TOP);
		//params.topMargin = ViewUtils.getPXByHeight(50);
		setLayoutParams(params);
		setBackgroundColor(0x33000000);
		
		//addImageTimes(context);
		addTimeDateAndAlls(context);
		//addBatteryView(context);
		
		updateTime();
		updateAlarm();
		//updateBatteryLevel();
	}
	
	/*private LinearLayout mTimeLy; 
	private static final int[] RES_TIME_NUM = {R.drawable.num_0, R.drawable.num_1, R.drawable.num_2,
		R.drawable.num_3, R.drawable.num_4, R.drawable.num_5,
		R.drawable.num_6, R.drawable.num_7, R.drawable.num_8, R.drawable.num_9};
	private void addImageTimes(Context context) {
		mTimeLy = new LinearLayout(context);
		mTimeLy.setOrientation(LinearLayout.HORIZONTAL);
		mTimeLy.setGravity(Gravity.CENTER);
		FrameLayout.LayoutParams mTotalFLParams = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, ViewUtils.getPXByHeight(74), Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		//mTotalFLParams.topMargin = ViewUtils.getPXByHeight(30);
		//mTimeLy.setBackgroundColor(0x22000000);
		addView(mTimeLy, mTotalFLParams);

		for (int i = 0; i < 5; i++) {
			ImageView v = new ImageView(context);
			LinearLayout.LayoutParams params;
			if (i == 2) {
				//冒号
				params = new LinearLayout.LayoutParams(
						ViewUtils.getPXByHeight(20), LayoutParams.MATCH_PARENT);
				v.setBackgroundResource(R.drawable.num_point_);
			} else {
				params = new LinearLayout.LayoutParams(
						ViewUtils.getPXByHeight(46), LayoutParams.MATCH_PARENT);
			}
			mTimeLy.addView(v, params);
		}
	}*/
	
	private void addTimeDateAndAlls(Context context) {
		mTimeText = new TextView(context);
		mTimeText.setTextColor(TIME_COLOR);
		mTimeText.setTypeface(CircleContainer.sTypeface);
		mTimeText.setGravity(Gravity.CENTER);
		mTimeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(100));
		//int wTime = ViewUtils.getPXByWidth(300);
		LayoutParams mTimeTextLP = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		mTimeTextLP.topMargin = ViewUtils.getPXByHeight(15);
		addView(mTimeText, mTimeTextLP);
		
		/*mTimeText2 = new TextView(context);
		mTimeText2.setTextColor(TIME_COLOR);
		mTimeText2.setGravity(Gravity.CENTER);
		mTimeText2.setTypeface(CircleContainer.sTypeface);
		mTimeText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(217));
		LayoutParams mTimeTextLP2 = new LayoutParams(
				wTime, LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.RIGHT);
		mTimeTextLP2.topMargin = ViewUtils.getPXByHeight(240);
		mTimeTextLP2.rightMargin = mTimeTextLP.rightMargin;
		addView(mTimeText2, mTimeTextLP2);*/
				
		/*mDateText = new TextView(context);
		mDateText.setTextColor(TIME_COLOR);
		//mDateText.setShadowLayer(3, 2, 0, 0xff000000);
		mDateText.setTypeface(CircleContainer.sTypeface, Typeface.BOLD);
		//int w = ViewUtils.getPXByWidth(325);
		//mDateText.setBackgroundColor(0x44000000);
		//mDateText.setMaxWidth(w);
		//mDateText.setGravity(Gravity.CENTER);
		mDateText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByWidth(42));
		LayoutParams mDateTextLP = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		//mDateTextLP.leftMargin = ViewUtils.getPXByWidth(50);
		mDateTextLP.topMargin = ViewUtils.getPXByHeight(200);
		addView(mDateText, mDateTextLP);*/
		
		mAlarmText = new TextView(context);
		mAlarmText.setTextColor(Color.WHITE);
		mAlarmText.setTypeface(CircleContainer.sTypeface);
		//mAlarmText.setMaxWidth(w);
		//mAlarmText.setGravity(Gravity.CENTER);
		mAlarmText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(30));
		Drawable alarmIcon = getResources().getDrawable(R.drawable.icon_alarm);
		int w = ViewUtils.getPXByHeight(37);
		alarmIcon.setBounds(0, 0, w, w);
		mAlarmText.setCompoundDrawables(null, null, alarmIcon, null);
		mAlarmText.setCompoundDrawablePadding(ViewUtils.getPXByWidth(15));
		mAlarmText.setGravity(Gravity.CENTER_VERTICAL);
		LayoutParams mAlarmTextLP = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 
				Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		mAlarmTextLP.bottomMargin = ViewUtils.getPXByHeight(25);
		addView(mAlarmText, mAlarmTextLP);
				
		/*mBattery = new TextView(context);
		mBattery.setTextColor(Color.WHITE);
		mBattery.setTypeface(CircleContainer.sTypefaceWea, Typeface.BOLD);
		mBattery.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(40));
		Drawable batteryIcon = getResources().getDrawable(R.drawable.icon_battery);
		int batteryIconW = ViewUtils.getPXByHeight(46);
		int batteryIconH = ViewUtils.getPXByHeight(19);
		batteryIcon.setBounds(0, 0, batteryIconW, batteryIconH);
		mBattery.setCompoundDrawables(null, batteryIcon, null, null);
		mBattery.setCompoundDrawablePadding(ViewUtils.getPXByHeight(5));
		LayoutParams mBatteryLP = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		mBatteryLP.topMargin = ViewUtils.getPXByWidth(240);
		mBatteryLP.rightMargin = ViewUtils.getPXByWidth(85);
		addView(mBattery, mBatteryLP);*/
	}

	@Override
	public void onDateChanged() {
		//LogUtils.log(null, "TimeView onDateChanged");
		updateTime();
	}

	private void updateTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("EEEE", Locale.US);

		String timeFormat = "HH:mm";
		if (Constant.sIsTime24 != 1) {
			timeFormat = "h:mma";
			/*format.applyPattern("a");
			String apm = format.format(date);
			try {
				apm = apm.toUpperCase();
			} catch (Exception e) {
				// TODO: handle exception
			}*/
			//mAPMText.setText(apm);
			format.applyPattern(timeFormat);
			String time = format.format(date);
			Spannable timeSpan = new SpannableString(time);        
			timeSpan.setSpan(new RelativeSizeSpan(0.4f), time.length() - 2, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // set size
			mTimeText.setText(timeSpan);
		} else {
			format.applyPattern(timeFormat);
			String time = format.format(date);
			mTimeText.setText(time);
		}
		/*format.applyPattern(timeFormat);
		String time = format.format(date);
		mTimeText.setText(time);*/
		/*int timeIndex = time.indexOf(':');
		mTimeText.setText(time.substring(0, timeIndex));
		mTimeText2.setText(time.substring(timeIndex + 1));*/

		/*String formatDate = "MMM.dd EEEE";
		if (Constant.sDateFormat != null && !Constant.sDateFormat.equals("")
				&& !Constant.sDateFormat.equals("Default")
				&& !Constant.sDateFormat.equals("default")) {
			formatDate = Constant.sDateFormat +  " EEEE";
		} else {
			format.applyPattern(formatDate);
			String text = format.format(date);
			Spannable wordtoSpan = new SpannableString(text);        
		    wordtoSpan.setSpan(new ForegroundColorSpan(Constant.sColor[Constant.sBgIndex * 3 + 1]), 
		    		text.length() - 4, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		    mDateText.setText(wordtoSpan);
		}
		
		format.applyPattern(formatDate);
		mDateText.setText(format.format(date).toUpperCase());*/
		
		/*SimpleDateFormat weekformat = new SimpleDateFormat("EEEE", Locale.US);
		mWeekText.setText("" + weekformat.format(date));
		//weekParseImage(weekformat.format(date));
		if (mWeekText.getWidth() +  2 * mAlarmIcon.getWidth() > mDateText.getWidth()) {
			mAlarmIconLP.rightMargin = mDateText.getWidth() - (mWeekText.getWidth() +  2 * mAlarmIcon.getWidth());
			mAlarmIcon.setLayoutParams(mAlarmIconLP);
		}*/
		
	}
	
	/**
	 * 更新闹钟
	 */
	private void updateAlarm() {
		String alarmTemp = Settings.System.getString(getContext()
				.getContentResolver(), Settings.System.NEXT_ALARM_FORMATTED);

		if (alarmTemp == null || alarmTemp.length() <= 0) {
			mAlarmText.setVisibility(GONE);
		} else {
			mAlarmText.setText(alarmTemp.toUpperCase());
			mAlarmText.setVisibility(VISIBLE);
		}
	}
	
	/*private ImageView mBattery;
	private Bitmap mBatteryLevel, mPointIcon;
	private Paint mPaintText;
	private void addBatteryView(Context context) {
		mBatteryLevel = ViewUtils.getScaleBitmapWithIDByWidth(getContext(), R.drawable.battery_level);
		mPointIcon = ViewUtils.getScaleBitmapWithIDByWidth(getContext(), R.drawable.battery_level_spot);
		mPaintText = new Paint();
		mPaintText.setAntiAlias(true);
		mPoint = new Point();
		
		mBattery = new ImageView(context) {
			private long mStartTime = 0;
			private int mLevelAdd = 0;
			@Override
			protected void onDraw(Canvas canvas) {
				super.onDraw(canvas);
				if (Constant.sBatteryState == 1) {
					if (mStartTime == 0) {
						mStartTime = System.currentTimeMillis();
					}
					float t = (System.currentTimeMillis() - mStartTime) / (4000 * (100 - Constant.sBatteryLevel) / 100f);
					//LogUtils.log(null, "t =" + t);
					if (t < 1) {
						mLevelAdd = (int) ((100 - Constant.sBatteryLevel) * t);
						if (Constant.sBatteryLevel + mLevelAdd > 100) {
							 mLevelAdd = 0;
						}
					} else {
						mStartTime = System.currentTimeMillis();
					}
				} else {
					mLevelAdd = 0;
				}
				canvas.save();
				int right = (Constant.sBatteryLevel + mLevelAdd) * getWidth() / 100;
				canvas.clipRect(0, 0, right, getHeight());
				canvas.drawBitmap(mBatteryLevel, 0, 0, mPaintText);
				caculatePointPosition(right);
				canvas.drawBitmap(mPointIcon, mPoint.x, mPoint.y, mPaintText);
				canvas.restore();
				
				if (Constant.sIsScreenOn && Constant.sBatteryState == 1) {
					invalidate();
				}
			}
		};
		
		LayoutParams params = new LayoutParams(mBatteryW, 
				mBatteryH, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		mBattery.setBackgroundResource(R.drawable.battery_bg);
		addView(mBattery, params);
	}
	
	private Point mPoint;
	private int mLeftMax = ViewUtils.getPXByWidth(57);
	private int mRightMin = ViewUtils.getPXByWidth(262);
	private int mBatteryW = ViewUtils.getPXByWidth(310);
	private int mBatteryH = ViewUtils.getPXByWidth(99);
	private int mPointH = ViewUtils.getPXByWidth(12);
	private float mK1 = 1.8f;
	private int mPadding = ViewUtils.getPXByWidth(2);
	private void caculatePointPosition(int right) {
		if (right < mLeftMax) {
			//y = y0 + k(x-x0);
			mPoint.y = - mPadding + (int) (mBatteryH - (mK1 * right)); //点斜式
		} else if (right > mLeftMax && right < mRightMin) {
			mPoint.y = - mPadding; //点斜式
		} else {
			mPoint.y = - mPadding + (int) (mK1 * (right - mRightMin)); //点斜式
		}
		mPoint.x = (int) (right - mPointH / 1.5); //点斜式
	}
	
	@Override
	public void onMonitor(Bundle bundle)
	{
		LogUtils.log(null, "TimeView onMonitor");
		String eventType = bundle.getString(Constant.TYPE);
		if (eventType.equals(Constant.BATTERYSTATE)) {
			// 更新电量状态,param参数,0:正常,1:充电中,2:电量低,3:已充满
			updateBatteryLevel();
		} else if (eventType.equals(Constant.BATTERYLEVEL)) {
			// 更新电量值
			updateBatteryLevel();
		}
	}
	
	private void updateBatteryLevel() {
		LogUtils.log(null, "TimeView updateBatteryLevel");
		/*if (mBattery != null) {
			mBattery.setText(Constant.sBatteryLevel + "%");
		}
		if (mBattery != null) {
			mBattery.invalidate();
		}
	}

	private void weekParseImage(String date) {
		int leftmagin = (int) (60 * ViewUtils.getScaleByWidth());
		int i = 0;
		if (date.toLowerCase().contains("mon")) {
			mWeek.setImageResource(R.drawable.week1);
			i = 0;
		} else if (date.toLowerCase().contains("tue")) {
			mWeek.setImageResource(R.drawable.week2);
			i = 1;
		} else if (date.toLowerCase().contains("wed")) {
			mWeek.setImageResource(R.drawable.week3);
			i = 2;
		} else if (date.toLowerCase().contains("thu")) {
			mWeek.setImageResource(R.drawable.week4);
			i = 3;
		} else if (date.toLowerCase().contains("fri")) {
			mWeek.setImageResource(R.drawable.week5);
			i = 4;
		} else if (date.toLowerCase().contains("sat")) {
			mWeek.setImageResource(R.drawable.week6);
			i = 5;
		} else if (date.toLowerCase().contains("sun")) {
			mWeek.setImageResource(R.drawable.week7);
			i = 6;
		}
		
		//i = 6;
		mWeekLP.leftMargin = leftmagin / 2 + leftmagin * i;
		mWeek.setLayoutParams(mWeekLP);
	}
	
	private void updateTimeImages(String time) {
		LogUtils.log(null, "time = " + time);
		for (int i = 0; i < mTimeLy.getChildCount(); i++) {
			if (i == 2) {
				//冒号
				continue;
			}
			int index = time.charAt(i) - '0';
			LogUtils.log(null, "index = " + index + "i = " + i);
			mTimeLy.getChildAt(i).setBackgroundResource(RES_TIME_NUM[index]);
		}
	}
	*/

}
