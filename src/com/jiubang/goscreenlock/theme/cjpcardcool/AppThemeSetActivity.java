package com.jiubang.goscreenlock.theme.cjpcardcool;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LDrawable;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.util.AddChinaCityActivity;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.util.AddCityActivity;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.util.WeatherSettingUtil;

/**
 * 主题设置页
 * 
 */
public class AppThemeSetActivity extends Activity implements OnClickListener {
	public static final int RESULT_FROM_THEME_SET = 2000;
	public static final String THEME_CHANGED = "THEME_CHANGED";
	public static final String THEME_APPLY = "THEME_APPLY";
	// 0默认，1是华度 2是摄氏度
	private int mType;

	/****************************** 天气模块 ****************************************/
	boolean mIsAutoCity = true;
	TextView mCityMansel = null;
	String mName = null;
	ImageView mAutoBottom;
	ImageView mManBottom;
	View mAutoLayout;
	View mManLayout;
	View mCLayout;
	View mFLayout;
	ImageView mCBottom;
	ImageView mFBottom;
	/****************************** 天气模块 ****************************************/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme_set);
		
		ImageView backImageView = (ImageView) findViewById(R.id.go_lock_back_title_image);
		backImageView.setOnClickListener(this);
		backImageView.setBackgroundDrawable(new LDrawable());
		
		mAutoLayout = findViewById(R.id.auto_locate);
		mManLayout = findViewById(R.id.manually_select);
		mCLayout = findViewById(R.id.unit_c);
		mFLayout = findViewById(R.id.unit_f);
		
		
		((TextView) mAutoLayout.findViewById(R.id.preference_title)).setText(R.string.weather_city_auto);
		mAutoBottom = (ImageView) mAutoLayout.findViewById(R.id.radiobutton_img);
		
		mCityMansel = (TextView) mManLayout.findViewById(R.id.preference_title);
		mManBottom = (ImageView) mManLayout.findViewById(R.id.radiobutton_img);
		
		((TextView) mCLayout.findViewById(R.id.preference_title)).setText(R.string.theme_c);
		mCBottom = (ImageView) mCLayout.findViewById(R.id.radiobutton_img);
		
		((TextView) mFLayout.findViewById(R.id.preference_title)).setText(R.string.theme_f);
		mFBottom = (ImageView) mFLayout.findViewById(R.id.radiobutton_img);
		
		mFLayout.setOnClickListener(this);
		mCLayout.setOnClickListener(this);
		
		mAutoLayout.setOnClickListener(this);
		mManLayout.setOnClickListener(this);
		
		// 设置温度单位
		mType = WeatherSettingUtil.getTemperateScale(this);

		// 预设置城市类型
		mIsAutoCity = WeatherSettingUtil.getLocationWay(this) == 1;
	}

	/****************************** 天气模块 ****************************************/
	private void setCityType(boolean isAuto) {
		mIsAutoCity = isAuto;
		setChecked(mAutoBottom, mIsAutoCity);
		setChecked(mManBottom, !mIsAutoCity);
		WeatherSettingUtil.setLocationWay(this, mIsAutoCity ? 1 : 0);
		if (mCityMansel != null) {
			if (mName != null) {
				mCityMansel.setText(this.getResources().getString(
						R.string.weather_city_setting)
						+ " : " + mName);
			} else {
				mCityMansel.setText(this.getResources().getString(
						R.string.weather_city_setting));
			}
		}
	}

	private void setType() {
		if (mType == 1) {
			setChecked(mCBottom, false);
			setChecked(mFBottom, true);
			
		} else if (mType == 2) {
			setChecked(mCBottom, true);
			setChecked(mFBottom, false);
		} else {
			if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
				setChecked(mCBottom, false);
				setChecked(mFBottom, true);
			} else {
				setChecked(mCBottom, true);
				setChecked(mFBottom, false);
			}
		}
		WeatherSettingUtil.setTemperateScale(this, mType);
	}

	@Override
	protected void onResume() {
		if (!mIsAutoCity) {
			if (WeatherSettingUtil.getCity(this) == null) {
				mIsAutoCity = true;
				mName = null;
			} else {
				mName = WeatherSettingUtil.getCity(this).getCityName();
				if (mName == null) {
					mIsAutoCity = true;
				}
			}
		} else {
			if (WeatherSettingUtil.getCity(this) != null) {
				mName = WeatherSettingUtil.getCity(this).getCityName();
			}
		}
		setCityType(mIsAutoCity);
		
		setType();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		this.finish();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.unit_f:
			// 华度
			mType = 1;
			setType();
			break;
		case R.id.unit_c:
			// 摄氏度
			mType = 2;
			setType();
			break;
		case R.id.auto_locate:
			if (!mIsAutoCity) {
				WeatherSettingUtil.setIsManToAuto(this, 1);
			}
			setCityType(true);
			break;
		case R.id.manually_select:
			setCityType(false);
			// 跳转到选择城市
			if (!Global.isChinese(this)) {
				startActivity(new Intent(this, AddCityActivity.class));
			} else {
				startActivity(new Intent(this, AddChinaCityActivity.class));
			}
			break;
		case R.id.go_lock_back_title_image :
			finish();
			break;
		default:
			break;
		}
	}
	
	private void setChecked(ImageView view, boolean checked) {
		if (checked) {
			view.setImageResource(R.drawable.radio_select);
		} else {
			view.setImageResource(R.drawable.radio_unselect);
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		startFinishAnimation(this, AnimationStyle.RIGHT_OUT);
	}

	/**
	 * 在重写finish() 中执行super.finish()后调用
	 * @see Activity#finish()
	 * @param activity
	 * @param animationStyle
	 */
	public static void startFinishAnimation(Activity activity, AnimationStyle animationStyle) {
		if (activity != null) {
			activity.overridePendingTransition(getFinishEnterAnimation(animationStyle),
					getFinishExitAnimation(animationStyle));
		}
	}

	/**
	 * 动画类型
	 * @author zhongwenqi
	 *
	 */
	public enum AnimationStyle {
		RIGHT_IN, RIGHT_OUT
	}

	private static int getFinishEnterAnimation(AnimationStyle animationStyle) {
		int animationId = -1;
		switch (animationStyle) {
			case RIGHT_OUT :
				animationId = android.R.anim.fade_in;
				break;

			default :
				break;
		}
		return animationId;
	}

	private static int getFinishExitAnimation(AnimationStyle animationStyle) {
		int animationId = -1;
		switch (animationStyle) {
			case RIGHT_OUT :
				animationId = R.anim.store_activity_slide_out;
				break;

			default :
				break;
		}
		return animationId;
	}
	
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
}
