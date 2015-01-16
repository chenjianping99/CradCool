package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiubang.goscreenlock.theme.cjpcardcool.R;
import com.jiubang.goscreenlock.theme.cjpcardcool.ThemeSetProvider;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.LiveListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnWeatherChangeListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.WeatherConstants;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.util.UnitTransformUtil;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.util.WeatherDataBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.util.WeatherSettingUtil;

/**
 * 
 * @author chenjianping
 * 
 */
public class WeatherView extends FrameLayout implements
		OnWeatherChangeListener, LiveListener {
	
	private int mWidth;
	private boolean mIsToday = true;
	
	public WeatherView(Context context) {
		super(context);
		int padding = ViewUtils.getPXByHeight(16);
		mWidth = Constant.sRealWidth - padding * 2;
		FrameLayout.LayoutParams thisParams = new FrameLayout.LayoutParams(
				mWidth, ViewUtils.getPXByHeight(478), Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		thisParams.topMargin =  padding;
		setBackgroundResource(R.drawable.wea_bg_sunny);
		setLayoutParams(thisParams);
		addTodayWeather(context);
		addNextDayWeather(context);
		reflushWeatherInfo(true);
	}
	
	/*public WeatherView(Context context, boolean isToday) {
		super(context);
		mIsToday = isToday;
		mWidth = ViewUtils.getPXByWidth(680);
		FrameLayout.LayoutParams thisParams = new FrameLayout.LayoutParams(
				mWidth, Constant.sRealHeight / 2, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		//thisParams.topMargin =  ViewUtils.getPXByHeight(565);
		//setBackgroundColor(0x55000000);
		setLayoutParams(thisParams);
		if (mIsToday) {
			addTodayWeather(context);
		} else {
			addNextDayWeather(context);
			reflushWeatherInfo(true);
		}
	}*/

	/*private FrameLayout mTodayLp;
	private void addTodayBg(Context context) {	
		mTodayLp = new FrameLayout(context);
		FrameLayout.LayoutParams thisParams = new FrameLayout.LayoutParams(
				mWidth, ViewUtils.getPXByHeight(259 - 100), Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		thisParams.topMargin =  ViewUtils.getPXByHeight(550);
		mTodayLp.setBackgroundResource(R.drawable.wea_today_bg);
		addView(mTodayLp, thisParams);
	}
	
	private void addCoverBg(Context context) {
		mBg = new View(context);
		FrameLayout.LayoutParams thisParams = new FrameLayout.LayoutParams(
				mWidth, mHight, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		addView(mBg, thisParams);
		thisParams.topMargin =  ViewUtils.getPXByHeight(6);
		mBg.setBackgroundResource(R.drawable.wea_bg_sunny);
		
		View v = new View(getContext());
		v.setBackgroundResource(R.drawable.wea_bg);
		addView(v, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}*/
	
	

	private View mWeatherView/*, mUpdateIcon*/;
	private TextView mTempText, mTempRangeText, mTypeText;
	private void addTodayWeather(Context context) 
	{				
		mWeatherView = new View(context);
		//mWeatherView.setBackgroundResource(R.drawable.wea_sunny);
		int mWeatherViewW = ViewUtils.getPXByWidth(100);
		LayoutParams mWeatherViewParams = new LayoutParams(
				mWeatherViewW, mWeatherViewW, Gravity.TOP | Gravity.LEFT);
		mWeatherViewParams.leftMargin = ViewUtils.getPXByWidth(20);
		mWeatherViewParams.topMargin = ViewUtils.getPXByHeight(25);	
		addView(mWeatherView, mWeatherViewParams);
		
		//天气类型
		mTypeText = new TextView(context);
		mTypeText.setTextColor(Color.WHITE);
		//mTypeText.setGravity(Gravity.CENTER);
		//mTypeText.setBackgroundColor(0x33ffffff);
		mTypeText.setTypeface(CircleContainer.sTypeface);
		mTypeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(40));
		//mTypeText.setText("Cloudy");
		//mTypeText.setMaxWidth(mWeatherViewW);
		LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.LEFT);
		param.leftMargin = ViewUtils.getPXByWidth(200);
		addView(mTypeText, param);
		
		addCity(context);
		//温度
		mTempText = new TextView(context);
		mTempText.setTextColor(Color.WHITE);
		mTempText.setTypeface(CircleContainer.sTypeface);
		//mTempText.setShadowLayer(3, 2, 0, 0xff000000);
		mTempText.setGravity(Gravity.CENTER);
		mTempText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(100));
		//mTempText.setText("75°F");
		//mTempText.setBackgroundColor(0x22ffffff);
		LayoutParams mTempTextParam = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.LEFT);
		mTempTextParam.topMargin = ViewUtils.getPXByHeight(20);
		mTempTextParam.leftMargin = ViewUtils.getPXByWidth(150);
		addView(mTempText, mTempTextParam);

		//温度范围
		mTempRangeText = new TextView(context);
		mTempRangeText.setTextColor(Color.WHITE);
		mTempRangeText.setTypeface(CircleContainer.sTypeface);
		mTempRangeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(40));
		//mTempRangeText.setText("217°/317°");
		//mTempRangeText.setGravity(Gravity.LEFT);
		//mTempRangeText.setBackgroundColor(0x33ffffff);
		LayoutParams tempRangeParam = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, Gravity.LEFT | Gravity.CENTER_VERTICAL);
		tempRangeParam.leftMargin = ViewUtils.getPXByWidth(30);
		addView(mTempRangeText, tempRangeParam);
	}
	
	private String mCityName = "";
	private MarqueeTextView mCityText;
	private void addCity(Context context) {
		mCityText = new MarqueeTextView(context);
		mCityText.setTextColor(Color.WHITE);
		mCityText.setTypeface(CircleContainer.sTypeface);
		mCityText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(40));
		mCityText.setGravity(Gravity.LEFT);
		//mCityText.setBackgroundColor(0x22000000);
		int w = ViewUtils.getPXByWidth(630);
		mCityText.setMaxWidth(w);
		LayoutParams mCityParam = new LayoutParams(w,
				LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		mCityParam.topMargin = ViewUtils.getPXByHeight(150);
		addView(mCityText, mCityParam);
		
		/*View mPlaceIcon = new View(context);
		mPlaceIcon.setBackgroundResource(R.drawable.big_weather_place_icon);
		int mPlaceIconW = ViewUtils.getPXByWidth(32);
		LayoutParams mIconParams = new LayoutParams(
				mPlaceIconW, mPlaceIconW, Gravity.RIGHT | Gravity.TOP);
		mIconParams.topMargin = ViewUtils.getPXByHeight(35);
		mIconParams.rightMargin = ViewUtils.getPXByWidth(50);
		addView(mPlaceIcon, mIconParams);*/
	
		/*mUpdateTime = new TextView(context);
		mUpdateTime.setTextColor(0xaaffffff);
		mUpdateTime.setTypeface(CircleContainer.sTypeface);
		mUpdateTime.setShadowLayer(3, 2, 0, 0xff000000);
		mUpdateTime.setGravity(Gravity.LEFT);
		//mUpdateTime.setBackgroundColor(0x33ffffff);
		mUpdateTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(34));
		//mUpdateTime.setText("12:30");
		LayoutParams mUpdateTimeParam = new LayoutParams((int) (mWidth / 2.2f),
				LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.TOP);
		mUpdateTimeParam.topMargin = mCityParam.topMargin;
		mUpdateTimeParam.rightMargin = ViewUtils.getPXByHeight(10);
		addView(mUpdateTime, mUpdateTimeParam);*/
		
		/*mUpdateIcon = new View(context);
		mUpdateIcon.setBackgroundResource(R.drawable.weather_shuaxin);
		int mUpdateIconParamsW = ViewUtils.getPXByWidth(36);
		int mUpdateIconParamsH = ViewUtils.getPXByWidth(40);
		LayoutParams mUpdateIconParams = new LayoutParams(
				mUpdateIconParamsW, mUpdateIconParamsH, Gravity.RIGHT | Gravity.BOTTOM);
		mUpdateIconParams.bottomMargin = ViewUtils.getPXByHeight(270);
		mUpdateIconParams.rightMargin = ViewUtils.getPXByWidth(45);
		addView(mUpdateIcon, mUpdateIconParams);
		mUpdateIcon.setOnClickListener(mOnClickListener);*/
	}
	
    /*private View.OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			reflushWeatherInfo(true);
			starRotateAnim(mUpdateIcon);
			mUpdateIcon.setOnClickListener(null);
		}
	};
	
	private void starRotateAnim(View v) {
		RotateAnimation r = new RotateAnimation(0, 359,
		Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		r.setInterpolator(new LinearInterpolator());
		r.setDuration(1000);
		r.setRepeatCount(-1);
		v.startAnimation(r);
	}*/
	
	/**
	 * 发送刷新天气的请求
	 */
	private void reflushWeatherInfo(boolean mIsForce) {
		LogUtils.log(null, "WeatherView: reflushWeatherInfo mIsForce =" + mIsForce);
		Intent intent = new Intent(Constant.WEATHEREFLUSH);
		Bundle b = new Bundle();
		b.putBoolean(Constant.WEATHER_FLUSH_FORCE, mIsForce);
		intent.putExtras(b);
		getContext().sendBroadcast(intent);
	}
	
	
	@Override
	public void onWeatherChange(Bundle weather) {
		LogUtils.log(null, "WeatherView: onWeatherChange mIsToday =" + mIsToday);
		updataWeather(weather);
		
		byte[] byteArray = weather.getByteArray(Constant.WEATHER_PREVIEW);
		if (null != byteArray) {
			WeatherDataBean bean = new WeatherDataBean();
			bean.setWeatherPreByByteArray(byteArray);
			ArrayList<WeatherDataBean> list = bean.getWeaterThemePreList();
			//下一天是第二条数据
			int i = 1;
			for (; i < list.size(); i++) {
				if (i == mWeatherArray.length + 1) {
					LogUtils.log(null, "break: i =" + i);
					break;
				}
				WeatherDataBean bean1 = list.get(i);
				mWeatherArray[i - 1].updataWeather(bean1);
			}
			LogUtils.log(null, "i =" + i);
			for (; i < mWeatherArray.length; i++) {
				LogUtils.log(null, "i =" + i);
				mWeatherArray[i].setVisibility(GONE);
			}
		}
	}
	
	public static final int	S_MAX_TEMPERTURE = 10000;
	public void updataWeather(Bundle weather)
	{
		String unit = weather.getString(Constant.WEATHER_UNIT);
		String temperature = Math
				.round(weather.getFloat(Constant.WEATHER_CURR)) + unit/* + "°"*/;
		mTempText.setText(temperature);
		Spannable span = new SpannableString(mTempText.getText());
		span.setSpan(new RelativeSizeSpan(0.7f), mTempText.length() - 2, mTempText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mTempText.setText(span);
		
		mCityName = weather.getString(Constant.WEATHER_CITYNAME);
		mCityText.setText(mCityName);
		String temperatureRange = Math.round(weather.getFloat(Constant.WEATHER_LOW)) 
				+ "°/" + Math.round(weather.getFloat(Constant.WEATHER_HIGH)) /*+ unit */ + "°";
		mTempRangeText.setText(temperatureRange);
		setWeatherType(weather.getInt(Constant.WEATHER_TYPE));
	}

	private void setWeatherType(int type) {
		switch (type) {
		default:
		case WeatherConstants.WEATHER_TYPE_UNKNOWN:// 未知天气类型
			break;
		case WeatherConstants.WEATHER_TYPE_SUNNY:// 晴
			mWeatherView.setBackgroundResource(R.drawable.wea_sunny);
			mTypeText.setText(R.string.weather_sunny);
			break;
		case WeatherConstants.WEATHER_TYPE_CLOUDY:// 多云
			mWeatherView.setBackgroundResource(R.drawable.wea_cloudy);
			mTypeText.setText(R.string.weather_cloudy);
			break;
		case WeatherConstants.WEATHER_TYPE_OVERCAST:// 阴天
			mWeatherView.setBackgroundResource(R.drawable.wea_overcast);
			mTypeText.setText(R.string.weather_overcast);
			break;
		case WeatherConstants.WEATHER_TYPE_FOG:// 大雾
			mWeatherView.setBackgroundResource(R.drawable.wea_foggy);
			mTypeText.setText(R.string.weather_fog);
			break;
		case WeatherConstants.WEATHER_TYPE_RAINY:// 下雨
			mWeatherView.setBackgroundResource(R.drawable.wea_rainy);
			mTypeText.setText(R.string.weather_rainy);
			break;
		case WeatherConstants.WEATHER_TYPE_THUNDERSTORM:// 雷雨
			mWeatherView.setBackgroundResource(R.drawable.wea_thunderstorm);
			mTypeText.setText(R.string.weather_thunderstorm);
			break;
		case WeatherConstants.WEATHER_TYPE_SNOWY:// 雪
			mWeatherView.setBackgroundResource(R.drawable.wea_snowy);
			mTypeText.setText(R.string.weather_snowy);
			break;
		}
		LogUtils.log(null, "setBackgroundIndex: index = " + (type - 2));
		ThemeSetProvider.setBackgroundIndex(getContext(), type - 2);
		setBackgroundResource(RES_BG[type - 2]);
	}
	private static final int[] RES_BG = {R.drawable.wea_bg_sunny, R.drawable.wea_bg_cloudy,
		R.drawable.wea_bg_cloudy, R.drawable.wea_bg_snowy, R.drawable.wea_bg_foggy, 
		R.drawable.wea_bg_rainy, R.drawable.wea_bg_rainy};

	@Override
	public void onStart() {

	}

	@Override
	public void onResume() {
		LogUtils.log(null, "Weather: onResume");
		if (mCityText != null) {
			mCityText.setText(mCityName);
			mCityText.onResume();
		}
	}
	@Override
	public void onPause() {
		/*if (mUpdateIcon != null) {
			mUpdateIcon.clearAnimation();
			mUpdateIcon.setOnClickListener(mOnClickListener);
		}*/
		if (mCityText != null) {
			mCityText.onPause();
		}
	}
	
	@Override
	public void onDestroy() {
		/*ViewUtils.recycleBitmap(mGaosiBitmap);
		ViewUtils.recycleBitmap(mOriginalBitmap);*/
	}
	
	//未来5天
	private Weather[] mWeatherArray = new Weather[5] ;
	private void addNextDayWeather(Context context)
	{
		/*View v1 = new View(context);
		//v1.setBackgroundColor(0xa0ffffff);
		FrameLayout.LayoutParams lineParams = new FrameLayout.LayoutParams(
				ViewUtils.getPXByWidth(400), 1, Gravity.RIGHT | Gravity.TOP);
		lineParams.rightMargin = ViewUtils.getPXByWidth(40);
		lineParams.topMargin = ViewUtils.getPXByHeight(90);
		addView(v1, lineParams);*/
		
		LinearLayout mTotalFL = new LinearLayout(context);
		mTotalFL.setOrientation(LinearLayout.HORIZONTAL);
		mTotalFL.setGravity(Gravity.CENTER);
		FrameLayout.LayoutParams mTotalFLParams = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, ViewUtils.getPXByHeight(150), Gravity.BOTTOM);
		mTotalFLParams.topMargin = ViewUtils.getPXByHeight(30);
		mTotalFL.setBackgroundColor(0x33000000);
		//mTotalFL.setBackgroundResource(R.drawable.wea_nextday_bg);
		addView(mTotalFL, mTotalFLParams);

		for (int i = 0; i < mWeatherArray.length; i++) {
			mWeatherArray[i] = new Weather(context);
			mTotalFL.addView(mWeatherArray[i]);
		}
	}

	
	/**
	 * @author chenjianping
	 */
	class Weather extends LinearLayout
	{
		private View mWeatherView;
		private TextView mWeek, mTempRange;
		public Weather(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			setOrientation(LinearLayout.VERTICAL);
			setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
					/*ViewUtils.getPXByHeight(54)*/LayoutParams.MATCH_PARENT, 1);
			setLayoutParams(params);
			//setBackgroundColor(0x66000000);
						
			mWeek = new TextView(context);
			//mWeek.setText("SUN");
			mWeek.setTextColor(Color.WHITE);
			mWeek.setTypeface(CircleContainer.sTypefaceCal);		
			mWeek.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByWidth(26));
			mWeek.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams mWeekLP = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			addView(mWeek, mWeekLP);
			
			mWeatherView = new View(context);
			//mWeatherView.setBackgroundResource(R.drawable.wea_sunny);
			int w = ViewUtils.getPXByHeight(53);
			LinearLayout.LayoutParams mWeatherViewLP = new LinearLayout.LayoutParams(w, w);
			mWeatherViewLP.topMargin = ViewUtils.getPXByHeight(5);
			mWeatherViewLP.bottomMargin = mWeatherViewLP.topMargin;
			addView(mWeatherView, mWeatherViewLP);
					
			mTempRange = new TextView(context);
			mTempRange.setTextColor(Color.WHITE);
			mTempRange.setGravity(Gravity.CENTER);
			mTempRange.setTypeface(CircleContainer.sTypeface);
			//mTempRange.setText("25/36°");
			mTempRange.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByWidth(26));
			//mTempRange.setBackgroundColor(Color.BLACK);
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			//param.leftMargin = ViewUtils.getPXByWidth(10);
			addView(mTempRange, param);
		}
		
		public void updataWeather(WeatherDataBean weatherData) {
			LogUtils.log(null, "WeatherView: nextDays: updataWeather weatherData =" + weatherData);
			WeatherDataBean mWeatherDataBean = weatherData;
			setWeatherType(mWeatherDataBean.getmWeatherType());

			String unit = "°F";
			float curr = mWeatherDataBean.getmWeatherCurrT();
			float high = mWeatherDataBean.getmWeatherHighT();
			float low = mWeatherDataBean.getmWeatherLowT();

			int mTemperateScale = WeatherSettingUtil
					.getTemperateScale(getContext());
			if (mTemperateScale == 2
					|| (mTemperateScale == 0 && !Locale.getDefault()
							.getLanguage().equalsIgnoreCase("en"))) {
				curr = UnitTransformUtil.getTempValueInCelsius(curr, 1);
				high = UnitTransformUtil.getTempValueInCelsius(high, 1);
				low = UnitTransformUtil.getTempValueInCelsius(low, 1);
				unit = "°C";
			}

			// if (mWeatherDataBean.getmCityName() == null
			// || mWeatherDataBean.getmCityName().trim().length() <= 0)
			// {
			// mCityName.setText("--");
			// }
			// else
			// {
			// mCityName.setText(" " + mWeatherDataBean.getmCityName());
			// }
			//
			
			unit = "°";
			/*if (curr >= S_MAX_TEMPERTURE) {
				mTempRange.setText("");
			} else if ((high + low) / 2 < S_MAX_TEMPERTURE) {
				mTempRange.setText("" + (int) ((high + low) / 2 + 0.5f) + unit);
			} else {
				mTempRange.setText("" + (int) (curr + 0.5f) + unit);
			}*/
			 
			// Log.d("ddd", "curr =" + curr + "high =" + high + "low=" + low);
			if (mWeatherDataBean.getmWeatherLowT() >= S_MAX_TEMPERTURE
					|| mWeatherDataBean.getmWeatherHighT() >= S_MAX_TEMPERTURE) {
				mTempRange.setText("");
			} else {
				mTempRange.setText((int) (low + 0.5f) + "-"
						+ (int) (high + 0.5f) /*+ unit */ + "°");
			}

			String week = mWeatherDataBean.getWeekDate();
			mWeek.setText(week.toUpperCase());
			//mWeek.setBackgroundResource(R.drawable.wea_week_bg);
		}

		private void setWeatherType(int type) {
			switch (type) {
			default:
			case WeatherConstants.WEATHER_TYPE_UNKNOWN:// 未知天气类型
				break;
			case WeatherConstants.WEATHER_TYPE_SUNNY:// 晴
				mWeatherView.setBackgroundResource(R.drawable.wea_sunny);
				break;
			case WeatherConstants.WEATHER_TYPE_CLOUDY:// 多云
				mWeatherView.setBackgroundResource(R.drawable.wea_cloudy);
				break;
			case WeatherConstants.WEATHER_TYPE_OVERCAST:// 阴天
				mWeatherView.setBackgroundResource(R.drawable.wea_overcast);
				break;
			case WeatherConstants.WEATHER_TYPE_FOG:// 大雾
				mWeatherView.setBackgroundResource(R.drawable.wea_foggy);
				break;
			case WeatherConstants.WEATHER_TYPE_RAINY:// 下雨
				mWeatherView.setBackgroundResource(R.drawable.wea_rainy);
				break;
			case WeatherConstants.WEATHER_TYPE_THUNDERSTORM:// 雷雨
				mWeatherView.setBackgroundResource(R.drawable.wea_thunderstorm);
				break;
			case WeatherConstants.WEATHER_TYPE_SNOWY:// 雪
				mWeatherView.setBackgroundResource(R.drawable.wea_snowy);
				break;
			}
		}
		
		/*private String week2WeekLy(String week) {
			String weekly = "Sunday";
			if (week.toLowerCase().contains("mon")) {
				weekly = "Monday";
			} else if (week.toLowerCase().contains("tue")) {
				weekly = "Tuesday";
			} else if (week.toLowerCase().contains("wed")) {
				weekly = "Wednesday";
			} else if (week.toLowerCase().contains("thu")) {
				weekly = "Thursday";
			} else if (week.toLowerCase().contains("fri")) {
				weekly = "Friday";
			} else if (week.toLowerCase().contains("sat")) {
				weekly = "Saturday";
			} else if (week.toLowerCase().contains("sun")) {
				weekly = "Sunday";
			}
			//return weekly; 
			return weekly.substring(0, 3).toUpperCase();
		}*/
	}
		
	/*private boolean mIsDoGaosi = false;
	private void setBg(int index) {
		mOriginalBitmap = Global.getMyBgBitmap(getContext(), GetPictureActivity.RAINNY_INDEX);
		if (mOriginalBitmap != null) {
			mIsCustomBg = true;
		} else {
			mIsCustomBg = false;
		}
		LogUtils.log(null, "mIsCustomBg = " + mIsCustomBg);
		if (!mIsCustomBg) {
			if (mBg != null) {
				//mBg.setBackgroundResource(RES_ID[index]);
			}
			if (mCircleContainer != null) {
				//mCircleContainer.setBackgroundResource(RES_GAOSI_ID[index]);
			}
		} else {
			if (!mIsDoGaosi) {
				if (mBg != null) {
					mBg.setBackgroundDrawable(new BitmapDrawable(mOriginalBitmap));
				}
				LogUtils.log(null, "time1 = " + System.currentTimeMillis());
				mHandler.sendEmptyMessage(index);
				mIsDoGaosi = true;
			}
		}
	}
	
	private boolean mIsCustomBg = false;
	private Bitmap mGaosiBitmap, mOriginalBitmap;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bitmap b = Bitmap.createScaledBitmap(mOriginalBitmap, Constant.sRealWidth / 8, Constant.sRealHeight / 8, true);
			mGaosiBitmap = BitmapManager.getGaussina(getContext(), b, 5);
			LogUtils.log(null, "msg = " + msg.what);
			if (mCircleContainer != null) {
				mCircleContainer.setBackgroundDrawable(new BitmapDrawable(mGaosiBitmap));
			}
			LogUtils.log(null, "time3 = " + System.currentTimeMillis());
		};
	};*/
	
}
