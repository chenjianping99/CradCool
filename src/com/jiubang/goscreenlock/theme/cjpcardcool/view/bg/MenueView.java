package com.jiubang.goscreenlock.theme.cjpcardcool.view.bg;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ViewUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.LiveListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.R;

/**
 * @author xule
 * 
 */
public class MenueView extends FrameLayout implements OnClickListener, LiveListener {
	private boolean mChangeThemeFlag = false;
	//private ImageView mDeviderImage;
	public MenueView(Context context) {
		super(context);
		//setBackgroundColor(0xff60aba7);

		/*mDeviderImage = new ImageView(context);
		mDeviderImage.setBackgroundColor(Color.WHITE);
		FrameLayout.LayoutParams deviderParams = new FrameLayout.LayoutParams(VIEWW,
				1, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		deviderParams.topMargin = ViewUtils.getPXByHeight(280);
		mDeviderImage.setVisibility(View.GONE);
		addView(mDeviderImage, deviderParams);*/
		addHeader(context);
		addThemeView();
		addUnlockMenue();

		new Thread(new Runnable() {
			@Override
			public void run() {
				mIsGooglePlayExsit = isAppExsit(getContext(), GOOGLEPACKAGENAME);
			}
		}).start();
	}
	
	private ImageView mBackIcon, mResetIcon;
	private void addHeader(Context context) {
		FrameLayout ly = new FrameLayout(context);
		FrameLayout.LayoutParams themeParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				ViewUtils.getPXByHeight(200),
				Gravity.TOP);
		addView(ly, themeParams);
		ly.setBackgroundColor(0x44000000);
		
		mBackIcon = new ImageView(getContext());
		mBackIcon.setImageResource(R.drawable.add);
		FrameLayout.LayoutParams deviderParams = new FrameLayout.LayoutParams(ViewUtils.getPXByWidth(50),
				ViewUtils.getPXByWidth(50), Gravity.CENTER_VERTICAL | Gravity.LEFT);
		deviderParams.leftMargin = ViewUtils.getPXByWidth(50);
		ly.addView(mBackIcon, deviderParams);
		
		mResetIcon = new ImageView(getContext());
		mResetIcon.setImageResource(R.drawable.add);
		FrameLayout.LayoutParams mResetIconParams = new FrameLayout.LayoutParams(ViewUtils.getPXByWidth(50),
				ViewUtils.getPXByWidth(50), Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		mResetIconParams.rightMargin = ViewUtils.getPXByWidth(50);
		ly.addView(mResetIcon, mResetIconParams);
	}

	private LinearLayout mThemeView, mThemeView2;
	//private ImageView mBottomDivider;
	private void addThemeView() {
		mThemeView = new LinearLayout(getContext());
		mThemeView.setOrientation(LinearLayout.HORIZONTAL);
		mThemeView.setGravity(Gravity.CENTER);
		mThemeView.setVisibility(View.GONE);
		FrameLayout.LayoutParams themeParams = new FrameLayout.LayoutParams(ViewUtils.getPXByWidth(655), 
				ViewUtils.getPXByHeight(338),
				Gravity.CENTER);
		themeParams.bottomMargin = ViewUtils.getPXByHeight(150);
		addView(mThemeView, themeParams);
		
		mThemeView2 = new LinearLayout(getContext());
		mThemeView2.setOrientation(LinearLayout.HORIZONTAL);
		mThemeView2.setGravity(Gravity.CENTER);
		mThemeView2.setVisibility(View.GONE);
		FrameLayout.LayoutParams theme2Params = new FrameLayout.LayoutParams(ViewUtils.getPXByWidth(655), 
				ViewUtils.getPXByHeight(338),
				Gravity.CENTER);
		theme2Params.topMargin = ViewUtils.getPXByHeight(220);
		addView(mThemeView2, theme2Params);
		
		initThemeViews(getContext());
		
		/*mBottomDivider = new ImageView(getContext());
		mBottomDivider.setBackgroundColor(Color.WHITE);
		FrameLayout.LayoutParams deviderParams = new FrameLayout.LayoutParams(VIEWW,
				1, Gravity.CENTER);
		deviderParams.topMargin = ViewUtils.getPXByHeight(435);
		mBottomDivider.setVisibility(View.GONE);
		addView(mBottomDivider, deviderParams);*/
	}

	private static final int THEME_RES_ID[] = {/*R.drawable.bg1, R.drawable.bg2, 
		R.drawable.bg3*/};
	private ThemeShow mThemeShow[];

	private void initThemeViews(Context context) {
		mThemeShow = new ThemeShow[THEME_RES_ID.length];
		for (int i = 0; i < THEME_RES_ID.length; i++) {
			mThemeShow[i] = new ThemeShow(context, i);
			scaleThemeViews(mThemeShow[i]);
			mThemeShow[i].setOnClickListener(this);
		}
	}

	private void scaleThemeViews(ThemeShow v) {
		if (v != null) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewUtils.getPXByHeight(190), ViewUtils.getPXByHeight(338), 1);
			v.setLayoutParams(params);
			if (v.getmId() < 3) {
				mThemeView.addView(v);
			} else {
				mThemeView2.addView(v);
			}
		}
	}

	private FrameLayout mTopMenue;
	private void addUnlockMenue() {
		FrameLayout.LayoutParams topParams = new FrameLayout.LayoutParams(
				ViewUtils.getPXByWidth(680), ViewUtils.getPXByHeight(300),
				Gravity.CENTER);
		topParams.topMargin = ViewUtils.getPXByHeight(550);
		mTopMenue = new FrameLayout(getContext());
		mTopMenue.setVisibility(View.GONE);
		addView(mTopMenue, topParams);
		initViews(getContext());
		//mTopMenue.setBackgroundColor(0x33ffffff);
	}

	private TextView mThemeStore;
	private TextView mSettings;
	private void initViews(Context context) {
		mThemeStore = new TextView(context);
		mThemeStore.setTextColor(Color.WHITE);
		mThemeStore.setText("Theme Store");
		mThemeStore.setGravity(Gravity.CENTER);
		mThemeStore.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByWidth(28));
		//mThemeStore.setTypeface(DodolContainer.sMenueTypeface);
		//Drawable phoneDrawable = getResources().getDrawable(R.drawable.store);
		//phoneDrawable.setBounds(0, 0, ViewUtils.getPXByWidth(88), ViewUtils.getPXByWidth(88));
		//mThemeStore.setCompoundDrawablePadding(ViewUtils.getPXByHeight(10));
		//mThemeStore.setCompoundDrawables(null, phoneDrawable, null, null);
		mThemeStore.setOnClickListener(this);

		mSettings = new TextView(context);
		mSettings.setTextColor(Color.WHITE);
		mSettings.setText("Settings");
		mSettings.setGravity(Gravity.CENTER);
		mSettings.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByWidth(28));
		//mSettings.setTypeface(DodolContainer.sMenueTypeface);
		//Drawable settingsDrawable = getResources().getDrawable(R.drawable.setting);
		//settingsDrawable.setBounds(0, 0, ViewUtils.getPXByWidth(88), ViewUtils.getPXByWidth(88));
		//mSettings.setCompoundDrawablePadding(ViewUtils.getPXByHeight(10));
		//mSettings.setCompoundDrawables(null, settingsDrawable, null, null);
		mSettings.setOnClickListener(this);

		scaleViews(mThemeStore);
		scaleViews(mSettings);
	}

	private void scaleViews(TextView v) {
		if (v != null) {
			LayoutParams params = new LayoutParams(ViewUtils.getPXByWidth(190), LayoutParams.WRAP_CONTENT);
			v.setLayoutParams(params);
			if (mThemeStore == v) {
				params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
			} else {
				params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
			}
			mTopMenue.addView(v);
		}
	}

	public void setViewVisible() {
		mTopMenue.setVisibility(View.VISIBLE);
		mThemeView.setVisibility(View.VISIBLE);
		mThemeView2.setVisibility(View.VISIBLE);
	}

	private String mPackageName;

	@Override
	public void onClick(View v) {
		if (v == mThemeStore) {
			Global.sendUnlockWithIntent(getContext(), null, "com.jiubang.goscreenlock",
					"com.jiubang.golocker.diy.themescan.ThemeManageActivity", null);
		} else if (v == mSettings) {
			Intent i = new Intent(Settings.ACTION_SETTINGS);
			Global.sendUnlockWithIntent(getContext(), null, null, null, i);
		} else if (v instanceof ThemeShow) {
			int id = ((ThemeShow) v).getmId();
			LogUtils.log(null, "id = " + id);
			if (id < THEME_PKG_NAME.length - 1) {
				if (isAppExsit(getContext(), THEME_PKG_NAME[id])) {
					mChangeThemeFlag = true;
					Global.sendUnlockWithIntent(getContext(), "", null, null);
					mPackageName = THEME_PKG_NAME[id];
				} else {
					Global.sendUnlockWithIntent(getContext(), null, null, null,
							getMarketIntent(getContext(), THEME_PKG_NAME[id]));
				}
			}
		}
	}

	// 供父View调用
	public void setMenueVisible() {
		mTopMenue.setVisibility(View.VISIBLE);
		mThemeView.setVisibility(View.VISIBLE);
		mThemeView2.setVisibility(View.VISIBLE);

		for (int i = 0; i < mThemeView.getChildCount(); i++) {
			mThemeView.getChildAt(i).startAnimation(buildAnimation(300 + 70 * i));
		}
		for (int i = 0; i < mThemeView2.getChildCount(); i++) {
			mThemeView2.getChildAt(i).startAnimation(buildAnimation(450 + 70 * i));
		}
		for (int i = 0; i < mTopMenue.getChildCount(); i++) {
			mTopMenue.getChildAt(i).startAnimation(buildAnimation(600 + 70 * i));
		}
	}

	public void setMenueInvisible() {
		mTopMenue.setVisibility(View.GONE);
		mThemeView.setVisibility(View.GONE);
		mThemeView2.setVisibility(View.GONE);
	}

	private Animation buildAnimation(int offsetTime) {
		Animation animation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(500);
		animation.setInterpolator(new OvershootInterpolator());
		animation.setStartOffset(offsetTime);
		return animation;
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
		mChangeThemeFlag = false;
	}

	/**
	 * @author xule 四套主题互推展示
	 */
	private class ThemeShow extends FrameLayout {

		private ImageView mThemeImage;
		private FrameLayout mBgView;
		private ImageView mSelectImage;
		private int mId;
		public ThemeShow(Context context, int id) {
			super(context);
			mId = id;
			mThemeImage = new ImageView(context);
			mThemeImage.setBackgroundResource(THEME_RES_ID[id]);
			FrameLayout.LayoutParams imgParams = new FrameLayout.LayoutParams(ViewUtils.getPXByHeight(190),
					LayoutParams.MATCH_PARENT, Gravity.CENTER);
			addView(mThemeImage, imgParams);

			mBgView = new FrameLayout(context);
			mBgView.setBackgroundColor(0x80000000);
			mBgView.setVisibility(GONE);
			FrameLayout.LayoutParams bgParams = new FrameLayout.LayoutParams(ViewUtils.getPXByHeight(190),
					LayoutParams.MATCH_PARENT, Gravity.CENTER);
			addView(mBgView, bgParams);

			mSelectImage = new ImageView(context);
			//mSelectImage.setImageResource(R.drawable.download);
			FrameLayout.LayoutParams selectParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, Gravity.CENTER);
			//selectParams.bottomMargin = ViewUtils.getPXByWidth(5);
			//selectParams.rightMargin = ViewUtils.getPXByWidth(10);
			mBgView.addView(mSelectImage, selectParams);
			
			if (mId == THEME_RES_ID.length - 1) {
				mBgView.setVisibility(VISIBLE);
				//mSelectImage.setImageResource(R.drawable.applying);
			} else {
				if (!isAppExsit(context, THEME_PKG_NAME[mId])) {
					mBgView.setVisibility(VISIBLE);
				}
			}
		}
		
		private int getmId() {
			return mId;
		}
	}

	/**
	 * 跳转到Android Market
	 * 
	 * @param uriString
	 *            market的uri
	 * @return 成功打开返回true
	 */
	public Intent getMarketIntent(Context context, String packageName) {
		Intent intent = null;
		try {
			if (mIsGooglePlayExsit) {
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
				intent.setPackage(GOOGLEPACKAGENAME);
			} else {
				intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://play.google.com/store/apps/details?id=" + packageName));
			}
		} catch (Exception e) {
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="
					+ packageName));
		}

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	private boolean mIsGooglePlayExsit;
	private static final String GOOGLEPACKAGENAME = "com.android.vending";
	private static final String[] THEME_PKG_NAME = { "com.jiubang.goscreenlock.theme.chicken",
		"com.jiubang.goscreenlock.theme.hippo", "com.jiubang.goscreenlock.theme.animalpig",
			"com.jiubang.goscreenlock.theme.animalcow", "com.jiubang.goscreenlock.theme.galesaur", 
			Constant.THEME_PACKAGE_NAME};
	
	private static boolean isAppExsit(Context context, String packageName) {
		boolean ret = false;
		PackageManager packageManager = context.getPackageManager();
		try {
			List<PackageInfo> packages = packageManager
					.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
			for (PackageInfo packageInfo : packages) {
				if (packageInfo.packageName.equals(packageName)) {
					ret = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	private void useOtherTheme(String themePackageName) {
		try {
			if (themePackageName != null) {
				Intent it = new Intent("com.gau.go.launcherex_action_send_to_golock");
				it.putExtra("newtheme", themePackageName);
				getContext().sendBroadcast(it);
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onDestroy() {
		if (mPackageName != null && mChangeThemeFlag) {
			useOtherTheme(mPackageName);
		}
	}

	@Override
	public void onStart() {
	}
	
	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (CircleContainer.sAddClickable) {
			return true;
		} else {
			return super.onTouchEvent(event);
		}
	}*/

}
