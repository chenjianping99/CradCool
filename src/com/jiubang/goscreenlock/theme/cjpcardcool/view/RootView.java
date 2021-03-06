package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.R;
import com.jiubang.goscreenlock.theme.cjpcardcool.ThemeSetChangeListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.ThemeSetProvider;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.BitmapManager;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.blindsview.BlindsView;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.CityBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.UnitTransformUtil;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.util.DataBaseHandler;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.util.WeatherDataBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.util.WeatherService;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.util.WeatherSettingUtil;

/**
 * <br>
 * 类描述:跟View，锁屏主程序调用 <br>
 * 功能详细描述:
 * 
 * @author jiezhang
 */
public class RootView extends BlindsView implements ThemeSetChangeListener {
	private CircleContainer mUnlockView;
	private BroadcastReceiver mReceiver;
	int mLockBG = 0;
	
	public RootView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * GO锁屏反射创建时，会调用此函数创建
	 * 
	 * @param context
	 * @param attrs
	 */
	public RootView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * <br>
	 * 功能简述: 添加各种子View<br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param context
	 */
	private void init(Context context) {
		initReceiver();
		registerReceiver();
		ThemeSetProvider.addThemeSetChangeListener(this);
		Global.setRootView(this);
	}

	/**
	 * 更新背景,由主程序确定是否需要自定义背景
	 * 
	 * @param lockBG
	 */
	private void setBG() {
		if (mLockBG == 0) {
			//setBackgroundResource(R.drawable.bg1);
			onBGChange();
		} else {
			
		}
	}
	
	/**
	 * GO锁屏主程序通过反射调用此接口传递监听数据。(未接电话,未读短信，电量状态，电量大小)
	 * 
	 * @param bundle
	 */
	public void onMonitor(Bundle bundle) {
		/*-----------基本信息获取示例---------------*/
		String eventType = bundle.getString(Constant.TYPE);
		if (eventType.equals(Constant.CALL)) {
			// 更新未接电话
			int param = bundle.getInt(Constant.PARAM);
			Constant.sCall = param;
		} else if (eventType.equals(Constant.SMS)) {
			// 更新未读短信
			int param = bundle.getInt(Constant.PARAM);
			Constant.sSMS = param;
		} else if (eventType.equals(Constant.WEATHER)) {
			getWeatherFromLocker(bundle);
		} else if (eventType.equals(Constant.BATTERYSTATE)) {
			// 更新电量状态,param参数,0:正常,1:充电中,2:电量低,3:已充满
			int param = bundle.getInt(Constant.PARAM);
			Constant.sBatteryState = param;
		} else if (eventType.equals(Constant.BATTERYLEVEL)) {
			// 更新电量值
			int param = bundle.getInt(Constant.PARAM);
			Constant.sBatteryLevel = param;
		}
		/*-----------基本信息获取示例---------------*/
		if (mUnlockView != null) {
			mUnlockView.onMonitor(bundle);
		}
	}

	/**
	 * GO锁屏主程序通过反射调用此接口,周期同Activity
	 * 
	 * @param bundle
	 */
	public void onStart(Bundle bundle) {
		LogUtils.log(null, "RootView : onStart");
		if (bundle == null) {
			return;
		}
		/*-----------基本信息获取示例---------------*/
		Constant.sIsDisplayDate = bundle.getBoolean(Constant.ISDISPLAYDATE); // 是否显示日期true为显示，false为不显示
		Constant.sDateFormat = bundle.getString(Constant.DATEFORMAT); // 日期格式
		Constant.sIslocksound = bundle.getBoolean(Constant.ISLOCKSOUND); // 是否启用锁屏音
		Constant.sIsunlocksound = bundle.getBoolean(Constant.ISUNLOCKSOUND); // 是否启用解锁音
		Constant.sIsquake = bundle.getBoolean(Constant.ISQUAKE); // 是否振动
		Constant.sIsTime24 = bundle.getInt(Constant.ISTIME24); // 是否24小时，1为24小时制，0为12小时制
		Constant.sCall = bundle.getInt(Constant.CALL); // 未接电话
		Constant.sSMS = bundle.getInt(Constant.SMS); // 未读短信
		mLockBG = bundle.getInt(Constant.LOCKBG); // 是否更换壁纸
		Constant.sIsfullscreen = bundle.getBoolean(Constant.ISFULLSCREEN); // 是否全屏
		// 主包将传此参数来判断主包及系统是否支持透明化
		Constant.sIstranslucentSysBar = bundle.getBoolean("translucentSysBar");
		Constant.sBatteryState = bundle.getInt(Constant.BATTERYSTATE);
		Constant.sBatteryLevel = bundle.getInt(Constant.BATTERYLEVEL);
		LogUtils.log("ddd", "Constant.sIsfullscreen =" + Constant.sIsfullscreen 
				+ "sIstranslucentSysBar = " + Constant.sIstranslucentSysBar);
		Constant.initMetrics(getContext());
		/*-----------基本信息获取示例---------------*/
		/*-----------天气信息获取示例（PS：暂未实现）---------------*/
		boolean isWeatherServiceOpened = bundle
				.getBoolean(Constant.ISWEATHEROPENED); // 天气服务是否启动
		/*-----------天气信息获取示例（PS：暂未实现）---------------*/
		setBG();
		mUnlockView = new CircleContainer(getContext());
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(Constant.sRealWidth, 
				Constant.sRealHeight, Gravity.TOP | Gravity.LEFT);
		if (Constant.sIstranslucentSysBar && !Constant.sIsfullscreen) {
			params.topMargin = Constant.sStatusBarHeight;
		}
		addView(mUnlockView, params);
		mUnlockView.onStart();
		//addUnlockTip();
		
		/**************************** 天气模块 ******************************/
		// 判断天气服务
		if (!isWeatherServiceOpened) {
			// 自己去拿天气信息
			DataBaseHandler mDbHandler = new DataBaseHandler(getContext());
			WeatherDataBean mWeatherDataBean = mDbHandler.query();
			if (mWeatherDataBean != null) {
				boolean isSucced = true;
				String msg = "";
				String city = mWeatherDataBean.getmCityName();
				int type = mWeatherDataBean.getmWeatherType();
				float curr = mWeatherDataBean.getmWeatherCurrT();
				float high = mWeatherDataBean.getmWeatherHighT();
				float low = mWeatherDataBean.getmWeatherLowT();
				String unit = "°F";
				int mTemperateScale = WeatherSettingUtil
						.getTemperateScale(getContext());
				// 根据单位转换温度
				if (mTemperateScale == 2
						|| (mTemperateScale == 0 && !Locale.getDefault()
								.getLanguage().equalsIgnoreCase("en"))) {
					curr = UnitTransformUtil.getTempValueInCelsius(curr, 1);
					high = UnitTransformUtil.getTempValueInCelsius(high, 1);
					low = UnitTransformUtil.getTempValueInCelsius(low, 1);
					unit = "°C";
				}
				LogUtils.log(null, "curr =" + curr + "high = " + high
						+ " low = " + low + "unit : " + unit);
				Bundle bundle2 = new Bundle();
				bundle2.putBoolean(Constant.WEATHER_ISSUCCED, isSucced);
				bundle2.putString(Constant.WEATHER_MSG, msg);
				bundle2.putString(Constant.WEATHER_CITYNAME, city);
				bundle2.putInt(Constant.WEATHER_TYPE, type);
				bundle2.putFloat(Constant.WEATHER_CURR, curr);
				bundle2.putFloat(Constant.WEATHER_HIGH, high);
				bundle2.putFloat(Constant.WEATHER_LOW, low);
				bundle2.putString(Constant.WEATHER_UNIT, unit);

				byte[] bytes = mWeatherDataBean.getWeatherPreByteArray();

				if (null != bytes) {
					bundle2.putByteArray(Constant.WEATHER_PREVIEW, bytes);
				}

				boolean aIsAutoLocation = WeatherSettingUtil
						.getLocationWay(getContext()) == 1;
				boolean aIsManToAuto = WeatherSettingUtil
						.getIsManToAuto(getContext()) > 0;
				if (isSucced) {
					if (!aIsAutoLocation) {
						// 选择了城市，设置和数据一样的，则更新
						CityBean aCityBean = WeatherSettingUtil
								.getCity(getContext());
						if (aCityBean.getCityId().equalsIgnoreCase(
								mWeatherDataBean.getmCityId())) {
							updateWeatherInfo(bundle2);
						}
					} else {
						// 自动定位的
						if (aIsManToAuto) {
							// 刚从城市选择切换回来的，不更新
						} else {
							updateWeatherInfo(bundle2);
						}
					}
				}
			}
			// 启动天气服务
			Intent intent = new Intent();
			intent.setClass(getContext(), WeatherService.class);
			getContext().startService(intent);
		} else {
			getWeatherFromLocker(bundle);
		}
		/**************************** 天气模块 ******************************/
	}

	/**
	 * GO锁屏主程序通过反射调用此接口,周期同Activity
	 */
	public void onResume() {
		LogUtils.log(null, "RootView : onResume");
		Constant.sIsScreenOn = true;
		mUnlockView.onResume();
		
		clearAnimation();
		LayoutParams params = (LayoutParams) getLayoutParams();
		params.bottomMargin = 0;
		params.topMargin = 0;
		setLayoutParams(params);

		setVisibility(VISIBLE);
		invalidate();
	}

	/**
	 * GO锁屏主程序通过反射调用此接口,周期同Activity
	 */
	public void onPause() {
		LogUtils.log(null, "RootView : onPause");
		Constant.sIsScreenOn = false;
		// 千万不能删这个，这个是产品需求,每次灭屏，去尝试定位（当然肯定不会太频繁定位，定位有15分钟判断）。
		reflushWeatherInfo(false);
		if (mUnlockView != null) {
			mUnlockView.onPause();
		}
		
	}

	/**
	 * GO锁屏主程序通过反射调用此接口,周期同Activity
	 */
	public void onStop() {
		// int count = getChildCount();
		// mUnlockView.onStop();
		LogUtils.log(null, "RootView : onStop");
		Constant.sIsScreenOn = false;
	}

	/**
	 * GO锁屏主程序通过反射调用此接口,周期同Activity
	 */
	public void onDestroy() {
		LogUtils.log(null, "RootView : onDestroy");
		Constant.sIsScreenOn = false;
		cleanWeather();

		ThemeSetProvider.clearThemeSetChangeListener();
		if (mUnlockView != null) {
			mUnlockView.onDestroy();
			mUnlockView = null;
		}
		Global.setRootView(null);
		removeAllViews();
		//ViewUtils.recycleBitmap(sBgBitmap);
		BitmapManager.getInstance(getContext()).recyleAllBitmap();
	}

	/**************************** 天气模块 ******************************/
	private void initReceiver() {
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (Constant.WEATHERBROADCASTFILTER.equals(intent.getAction())) {
					boolean isSucced = intent.getExtras().getBoolean(
							Constant.WEATHER_ISSUCCED);
					String msg = intent.getExtras().getString(
							Constant.WEATHER_MSG);
					String city = intent.getExtras().getString(
							Constant.WEATHER_CITYNAME);
					int type = intent.getExtras().getInt(Constant.WEATHER_TYPE);
					float curr = intent.getExtras().getFloat(
							Constant.WEATHER_CURR);
					float high = intent.getExtras().getFloat(
							Constant.WEATHER_HIGH);
					float low = intent.getExtras().getFloat(
							Constant.WEATHER_LOW);
					String unit = "°F";
					//String unit = "°";
					int mTemperateScale = WeatherSettingUtil
							.getTemperateScale(getContext());
					// 根据单位转换温度
					if (mTemperateScale == 2
							|| (mTemperateScale == 0 && !Locale.getDefault()
									.getLanguage().equalsIgnoreCase("en"))) {
						curr = UnitTransformUtil.getTempValueInCelsius(curr, 1);
						high = UnitTransformUtil.getTempValueInCelsius(high, 1);
						low = UnitTransformUtil.getTempValueInCelsius(low, 1);
						unit = "°C";
					}
					LogUtils.log(null, "curr =" + curr + "high = " + high
							+ " low = " + low + "unit : " + unit);
					Bundle bundle = new Bundle();
					bundle.putBoolean(Constant.WEATHER_ISSUCCED, isSucced);
					bundle.putString(Constant.WEATHER_MSG, msg);
					bundle.putString(Constant.WEATHER_CITYNAME, city);
					bundle.putInt(Constant.WEATHER_TYPE, type);
					bundle.putFloat(Constant.WEATHER_CURR, curr);
					bundle.putFloat(Constant.WEATHER_HIGH, high);
					bundle.putFloat(Constant.WEATHER_LOW, low);
					bundle.putString(Constant.WEATHER_UNIT, unit);
					byte[] bytes = intent.getExtras().getByteArray(
							Constant.WEATHER_PREVIEW);
					if (null != bytes) {
						bundle.putByteArray(Constant.WEATHER_PREVIEW, bytes);
					}
					if (isSucced) {
						/*Log.d(Constant.TAG, "更新天气成功：\t城市：" + city + "\t天气类型："
								+ type + "\t温度：" + curr);*/
						updateWeatherInfo(bundle);
					}
				}
			}
		};
	}

	private void getWeatherFromLocker(Bundle bundle) {
		String cityName = bundle.getString(Constant.CITYNAME); // 城市名称
		int weatherType = bundle.getInt(Constant.WEATHERTYPE); // 天气类型，详见WeatherConstants
		float currTemperature = bundle.getFloat(Constant.CURRTEMPERATURE); // 当前温度
		float highTemperature = bundle.getFloat(Constant.HIGHTEMPERATURE); // 最高温
		float lowTemperature = bundle.getFloat(Constant.LOWTEMPERATURE); // 最低温
		String unit = "°F";
		//String unit = "°";
		int mTemperateScale = WeatherSettingUtil
				.getTemperateScale(getContext());
		// 根据单位转换温度
		if (mTemperateScale == 2
				|| (mTemperateScale == 0 && !Locale.getDefault().getLanguage()
						.equalsIgnoreCase("en"))) {
			currTemperature = UnitTransformUtil.getTempValueInCelsius(
					currTemperature, 1);
			highTemperature = UnitTransformUtil.getTempValueInCelsius(
					highTemperature, 1);
			lowTemperature = UnitTransformUtil.getTempValueInCelsius(
					lowTemperature, 1);
			unit = "°C";
		}
		Bundle bundle1 = new Bundle();
		bundle1.putString(Constant.WEATHER_CITYNAME, cityName);
		bundle1.putInt(Constant.WEATHER_TYPE, weatherType);
		bundle1.putFloat(Constant.WEATHER_CURR, currTemperature);
		bundle1.putFloat(Constant.WEATHER_HIGH, highTemperature);
		bundle1.putFloat(Constant.WEATHER_LOW, lowTemperature);
		bundle1.putString(Constant.WEATHER_UNIT, unit);
		updateWeatherInfo(bundle1);
	}

	/**
	 * 更新天气
	 * 
	 */
	private void updateWeatherInfo(Bundle bundle) {
		LogUtils.log(null, "mUnlockView.updateWeather: mUnlockView=" + mUnlockView);
		if (mUnlockView != null) {
			mUnlockView.updateWeather(bundle);
		}
	}

	/**
	 * 发送刷新天气的请求，可在广播接收里
	 * 
	 * @param mIsForce
	 *            是否强制刷新
	 */
	public void reflushWeatherInfo(boolean mIsForce) {
		Intent intent = new Intent(Constant.WEATHEREFLUSH);
		Bundle b = new Bundle();
		b.putBoolean(Constant.WEATHER_FLUSH_FORCE, mIsForce);
		intent.putExtras(b);
		getContext().sendBroadcast(intent);
	}

	/**
	 * 注销天气监听及服务
	 */
	private void cleanWeather() {
		unregisterReceiver();
		Intent intent = new Intent();
        intent.setClass(getContext(), WeatherService.class);
        try {
            getContext().stopService(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
	}

	/**
	 * 注册监听天气信息变化的广播
	 */
	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.WEATHERBROADCASTFILTER);
		getContext().registerReceiver(mReceiver, filter);
	}

	/**
	 * 反注册监听天气变化的广播
	 */
	private void unregisterReceiver() {
		getContext().unregisterReceiver(mReceiver);
	}
	/**************************** 天气模块 ******************************/

	/*
	private TextView mUnlockTip;
	private void addUnlockTip() {
		mUnlockTip = new TextView(getContext());
		mUnlockTip.setTextColor(DateTimeView.TIME_COLOR);
		mUnlockTip.setTypeface(CircleContainer.sTypeface/*, Typeface.BOLD);
		mUnlockTip.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByWidth(36));
		LayoutParams mDateTextLP = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		mDateTextLP.topMargin = (int) (Constant.sRealHeight * 0.92f);
		mUnlockTip.setText("Slide to Unlock");
		addView(mUnlockTip, mDateTextLP);
		addCameraIcon();
	}

	private boolean mIsTouchCamera = false;;
	private View mCameraIcon;
	private void addCameraIcon() {
		mCameraIcon = new View(getContext());
		mCameraIcon.setBackgroundResource(R.drawable.camera_icon);
		LayoutParams mDateTextLP = new LayoutParams(
				ViewUtils.getPXByHeight(65), ViewUtils.getPXByHeight(48), Gravity.TOP | Gravity.RIGHT);
		mDateTextLP.topMargin = (int) (Constant.sRealHeight * 0.92f);
		mDateTextLP.rightMargin = ViewUtils.getPXByWidth(45);
		addView(mCameraIcon, mDateTextLP);
		this.setOnTouchListener(mOnTouchListener);
	}
	
	private void handleTouchPosition(MotionEvent event) {
		int x = ViewUtils.getPXByWidth(520);
		int y = ViewUtils.getPXByHeight(1080);
		if (event.getX() > x && event.getY() > y) {
			mIsTouchCamera = true;
		} else {
			mIsTouchCamera = false;
		}
	}
	
	//子view调用
	public void setUnlockTipVisiable(int visiable) {
		if (mCameraIcon != null) {
			mCameraIcon.setVisibility(visiable);
		}
		if (mUnlockTip != null) {
			mUnlockTip.setVisibility(visiable);
		}
	}
	
	public void startVisiableAnimation() {
		AlphaAnimation r = new AlphaAnimation(0f, 1f);
		r.setInterpolator(new LinearInterpolator());
		r.setDuration(1000);
		r.setRepeatCount(Animation.INFINITE);
		r.setRepeatMode(Animation.RESTART);
		if (mCameraIcon != null) {
			mCameraIcon.setVisibility(VISIBLE);
			mCameraIcon.startAnimation(r);
		}
		if (mUnlockTip != null) {
			mUnlockTip.setVisibility(VISIBLE);
			mUnlockTip.startAnimation(r);
		}
	}
	
	// unlock
	private float mStartY, mDisY;
	private boolean mUnlockable;
	private FrameLayout.LayoutParams mThisParams = null ;
	private int mMargin, mBottomMargin;
	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mStartY = event.getRawY();
				mDisY = 0;
				mUnlockable = false;
				mThisParams = (FrameLayout.LayoutParams) Global.getRootView().getLayoutParams();
				mThisParams.gravity = Gravity.TOP;
				mMargin = mThisParams.topMargin;
				mBottomMargin = mThisParams.bottomMargin;
				handleTouchPosition(event);
				//mUnlockTip.setVisibility(INVISIBLE);
				LogUtils.log(null, "mMargin = " + mMargin + "mIsTouchCamera" + mIsTouchCamera);
				break;
			case MotionEvent.ACTION_MOVE:
				mDisY = (int) (event.getRawY() - mStartY);
				if (mDisY > 0) {
					mDisY = 0;
				} else {
					if (mDisY < - ViewUtils.getPXByHeight(400)) {
						if (!mUnlockable && Constant.sIsquake) {
							Global.getvibrator(getContext());
						}
						mUnlockable = true;
					} else {
						mUnlockable = false;
					}
				}
				mThisParams.topMargin = (int) (mMargin + mDisY);
				mThisParams.bottomMargin = (int) (mBottomMargin - mDisY);
				setLayoutParams(mThisParams);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:

				if (mUnlockable) {
					float disY = Constant.sRealHeight + mDisY;
					TranslateAnimation t = new TranslateAnimation(0, 0, 0, - disY);
					t.setDuration((int) (Math.abs(disY) / Constant.sYRate / 2));
					//t.setFillAfter(true);
					
					AlphaAnimation r2 = new AlphaAnimation(1, 0);
					r2.setDuration((int) (Math.abs(disY) / Constant.sYRate / 2));		
					//r2.setFillAfter(true);
					
					AnimationSet set = new AnimationSet(true);
					set.addAnimation(t);
					set.addAnimation(r2);
					set.setFillAfter(true);
					Global.getRootView().startAnimation(set);
					set.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							if (mIsTouchCamera) {
								LogUtils.log(null, "unlock camera");
								Global.sendUnlockWithIntent(getContext(), "camera", null, null);
							} else {
								Global.sendUnlockWithIntent(getContext(), "home", null, null);
							}
						}
					});
					
				} else if (mDisY != 0) {
					mThisParams.topMargin = mMargin;
					mThisParams.bottomMargin = mBottomMargin;
					Global.getRootView().setLayoutParams(mThisParams);
					TranslateAnimation t = new TranslateAnimation(0, 0, mDisY, 0);
					t.setDuration((int) (Math.abs(mDisY) / Constant.sYRate));
					t.setInterpolator(new BounceInterpolator());
					Global.getRootView().startAnimation(t);					
				}

				break;
			}
			return true;
		}
	};

	private static final int[] RES_BG = {R.drawable.bg1, R.drawable.bg2, 
		R.drawable.bg3};
	@Override
	public void onBGChange() {
		try {
			settingBackground();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	*//**
	 * 更新背景,由主程序确定是否需要自定义背景
	 *//*
	private static Bitmap sBgBitmap;
	private void settingBackground() {
		ThemeSetProvider.getSetting(getContext());
		LogUtils.log(null, " settingBackground: ThemeSetProvider.sBgIndexs = " + ThemeSetProvider.sBgIndexs);
		String[] strings = ThemeSetProvider.sBgIndexs.split(",");
		int[] ints = new int[strings.length];

		for (int i = 0; i < strings.length; ++i) {
			try {
				ints[i] = Integer.parseInt(strings[i]);
			} catch (NumberFormatException e) {
				ints[i] = -1;
			}
		}
		int customeBackgroundIndex = new Random().nextInt(strings.length);
		int index = ints[customeBackgroundIndex];

		if (index < 0) {
			setBackgroundResource(RES_BG[0]);
		} else if (index < RES_BG.length) {
			setBackgroundResource(RES_BG[index]);
		} else {
			String[] paths = ThemeSetProvider.sBgPath.split(",");
			if (customeBackgroundIndex < paths.length) {
				LogUtils.log(null, "settingBackground path = " + paths[customeBackgroundIndex]);
				try {
					Drawable drawable = Drawable.createFromPath(paths[customeBackgroundIndex]);
					if (drawable != null) {
						setBackgroundDrawable(drawable);
					ViewUtils.recycleBitmap(sBgBitmap);
					sBgBitmap = ViewUtils.getBitmapByPath(getContext(), paths[customeBackgroundIndex]);
					if (sBgBitmap != null) {
						setBackgroundDrawable(new BitmapDrawable(getResources(), sBgBitmap));
					} else {
						setBackgroundResource(RES_BG[0]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				LogUtils.log(null, "settingBackground last else");
				setBackgroundResource(RES_BG[0]);
			}
		}
		invalidate();
	}*/
	
	
	private static final int[] RES_BG = {R.drawable.bg_sunny, R.drawable.bg_cloudy,
		R.drawable.bg_cloudy, R.drawable.bg_snowy, R.drawable.bg_foggy, R.drawable.bg_rainy, R.drawable.bg_rainy};
	@Override
	public void onBGChange() {
		try {
			int index = ThemeSetProvider.getBackgroundIndex(getContext());
			setBackgroundResource(RES_BG[index]);
		} catch (OutOfMemoryError e) {
			setBackgroundResource(RES_BG[0]);
			e.printStackTrace();
		} catch (Exception e) {
			setBackgroundResource(RES_BG[0]);
			e.printStackTrace();
		}
		
	}

	
	@Override
	public Animation getCustomAimation() {
		Animation a = new AlphaAnimation(1, 0.1f) {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if (interpolatedTime < 1) {
					mIsInBlindMode = true;
					calculateBlindRotations(Constant.sRealWidth / 2, Constant.sRealHeight * (1 - interpolatedTime));
					invalidate();
				} else {
					mIsInBlindMode = false;
				}
				super.applyTransformation(interpolatedTime, t);
			}
		};
		a.setInterpolator(new LinearInterpolator());
		a.setDuration(1000);
		a.setFillAfter(true);
		a.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				LogUtils.log(null, "onAnimationEnd");
				Global.sendUnlockWithIntent(getContext(), null, null, null);
			}
		});
		return a;
	}
}
