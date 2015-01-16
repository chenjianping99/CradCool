package com.jiubang.goscreenlock.theme.cjpcardcool.view.unlock;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.ThemeSetProvider;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Util;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.CircleContainer;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.CustomTextView;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Selector;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ViewPagerSwitcher;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ViewUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnDestroyListener;

/**
 * 
 * @author chenjianping
 * 
 */
public class UnlockItemView extends LinearLayout implements OnDestroyListener {
    public final static int PHONE_ICON = 0;
    public final static int SMS_ICON = 1;
    public final static int CAMERA_ICON = 2;
    public final static int PHOTO_ICON = 3;
    public final static int SETTING_ICON = 4;
    public final static int BROWSER_ICON = 5;
    public final static int [] RESICON = {/*R.drawable.icon_phone, R.drawable.icon_sms, 
    	R.drawable.icon_camera, R.drawable.icon_photo, R.drawable.icon_setting, R.drawable.icon_browser*/};
    public final static String [] ICON_NAME = {"Phone", "Message", "Camera", 
    	"Gallery", "Settings", "Browser", };
    
    private int mIndex;
    private CustomTextView mNameText;
    private Context mContext;
    private ViewPagerSwitcher mBodyViewPager;
    public UnlockItemView(Context context, int index, ViewPagerSwitcher v) {
        super(context);
        this.mContext = context;
        mBodyViewPager = v;
        mIndex = index;
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        addNameText();
        mNameText.setOnTouchListener(mOnTouchListener);
        if (index == PHOTO_ICON) {
        	setParentView(v);
        }
    }
    private final int mWidth = ViewUtils.getPXByHeight(80);
    private void addNameText() {
        LayoutParams mParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewUtils.getPXByHeight(120));
        mParams.gravity = Gravity.LEFT;
        mParams.leftMargin = ViewUtils.getPXByWidth(20);
        mNameText = new CustomTextView(mContext);
        mNameText.setTextColor(Color.WHITE);
        mNameText.setTextSize(36);
        mNameText.setSingleLine(true);
        //mNameText.setHorizontallyScrolling(false); 
        //mNameText.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        mNameText.setTypeface(CircleContainer.sTypeface);
        mNameText.setGravity(Gravity.CENTER_VERTICAL);
       
        LogUtils.log(null, "mIndex = " + mIndex);
        if (mIndex < 0) {
        	mNameText.setText("> Slide to Unlock");
        	//mParams.leftMargin = mWidth + ViewUtils.getPXByWidth(40 + 20);
        	mNameText.setPadding(mWidth + ViewUtils.getPXByWidth(40), 0, 0, 0);
        } else {
        	mNameText.setText(ICON_NAME[mIndex]);
	        Drawable icon = getResources().getDrawable(RESICON[mIndex]);
			icon.setBounds(0, 0, mWidth, mWidth);
			mNameText.setCompoundDrawables(icon, null, null, null);
			mNameText.setCompoundDrawablePadding(ViewUtils.getPXByWidth(40));
        }
        addView(mNameText, mParams);
    }

    private String mDetail = "";
    private int mNumber = 0;
    public void setNumber(int number) {
    	mNameText.setSingleLine(false);
    	mNumber = number;
    	if (mIndex == PHONE_ICON) {
    		if (number == 0) {
    			mDetail = "no missed calls";
    		} else if (number > 1) {
    			mDetail = "you have " + number + " missed calls";
    		} else {
    			mDetail = "you have " + number + " missed call";
    		}
    	} else if (mIndex == SMS_ICON) {
    		if (number == 0) {
    			mDetail = "no unread messages";
    		} else if (number > 1) {
    			mDetail = "you have " + number + " unread messages";
    		} else {
    			mDetail = "you have " + number + " unread message";
    		}
    	}
    	
    	SpannableString ss =  new SpannableString(ICON_NAME[mIndex] + "\n" + mDetail);
    	 ss.setSpan(new RelativeSizeSpan(0.75f), ICON_NAME[mIndex].length(), ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // set size
    	 ss.setSpan(new ForegroundColorSpan(0xffc8c8c8), ICON_NAME[mIndex].length(), ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // set color
    	 mNameText.setText(ss);
    }
    
    private float mStartX, mDisX;
	private boolean mUnlockable;
	private LinearLayout.LayoutParams mThisParams = null;
	private int mMargin;
	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (mIndex >= 0 && mIndex <= 1) {
					mNameText.setSingleLine(true);
					mNameText.setText(ICON_NAME[mIndex]);
				}
				mStartX = event.getRawX();
				mDisX = 0;
				mUnlockable = false;
				mThisParams = (LinearLayout.LayoutParams) v.getLayoutParams();
				mMargin = mThisParams.leftMargin;
				LogUtils.log(null, "mMargin = " + mMargin);
				break;
			case MotionEvent.ACTION_MOVE:
				mDisX = (int) (event.getRawX() - mStartX);
				if (mDisX < 0) {
					mDisX = 0;
				} else {
					if (mBodyViewPager != null) {
						mBodyViewPager.getViewPager().requestDisallowInterceptTouchEvent(true);
					}
					if (mDisX > ViewUtils.getPXByWidth(220)) {
						//mDisX = ViewUtils.getPXByHeight(330);
						if (!mUnlockable && Constant.sIsquake) {
							Global.getvibrator(getContext());
						}
						mUnlockable = true;
					} else {
						mUnlockable = false;
					}
				}
				mThisParams.leftMargin = (int) (mMargin + mDisX);
				//LogUtils.log(null, "leftMargin = " + mThisParams.leftMargin);
				mNameText.setLayoutParams(mThisParams);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				if (mIndex >= 0 && mIndex <= 1) {
					mNameText.setSingleLine(false);
					setNumber(mNumber);
				}
				mThisParams.leftMargin = mMargin;
				LogUtils.log(null, "mDisX = " + mDisX);
				mNameText.setLayoutParams(mThisParams);
				if (mUnlockable) {
					switch (mIndex) {
					case BROWSER_ICON:
						Global.sendUnlockWithIntent(getContext(), "browser", null, null);
						break;
					case SETTING_ICON:
						Intent intent = new Intent(Settings.ACTION_SETTINGS);
						Global.sendUnlockWithIntent(getContext(), null, null, null, intent);
						break;
					case PHOTO_ICON:
						/*Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
						intent2.setType("image/*");
						Global.sendUnlockWithIntent(getContext(), null, null, null, intent2);*/
						goToPhoto(mContext);
						break;
					case CAMERA_ICON:
						Global.sendUnlockWithIntent(getContext(), "camera", null, null);
						break;
					case SMS_ICON:
						Global.sendUnlockWithIntent(getContext(), "sms", null, null);
						break;
					case PHONE_ICON:
						Global.sendUnlockWithIntent(getContext(), "phone", null, null);
						break;
					default:
						Global.sendUnlockWithIntent(getContext(), "home", null, null);
						break;
					}
				} else if (mDisX > 0) {
					TranslateAnimation t = new TranslateAnimation(mDisX, 0, 0, 0);
					t.setDuration((int) (100 * mDisX / ViewUtils.getPXByWidth(200)) + 300);
					t.setInterpolator(new BounceInterpolator());
					mNameText.startAnimation(t);					
				} else {
				}
				if (mBodyViewPager != null) {
					mBodyViewPager.getViewPager().requestDisallowInterceptTouchEvent(false);
				}
				break;
			}
			//mNameText.setGravity(Gravity.CENTER_VERTICAL);
			return true;
		}
	};
	@Override
	public void onDestroy() {
		mBodyViewPager = null;
		release();
	}
	
	//跳图库
	private static void goToPhoto(Context context) {
		String pkgName = ThemeSetProvider.getGallery(context);
		if (pkgName != null && !pkgName.equals("")) {
			Intent intent = Util.getIntentByPackageName(context, pkgName);
			if (Util.isIntentAvailable(context, intent)) {
				Global.sendUnlockWithIntent(context, null, null, null, intent);
			} else {
				selectGallery(context);
			}
		} else {
			selectGallery(context);
		}
	}
	
	public static ViewGroup sParentView;
	/*
	 * 一定要调用release方法！要不然下次就显示不出来了！
	 */
	public static final void setParentView(ViewGroup view) {
		if (sParentView == null) {
			sParentView = view;
		}
	}

	public static final void release() {
		if (sParentView != null) {
			if (sGallerySelector != null) {
				sParentView.removeView(sGallerySelector);
				sGallerySelector = null;
			}
			sParentView = null;
		}
	}

	private static Selector sGallerySelector;
	private static void selectGallery(Context context) {
		if (sParentView != null) {
			if (sGallerySelector == null) {
				sGallerySelector = new Selector(context, sParentView, Selector.GALLERY);
				if (!sGallerySelector.unlock()) {
					sParentView.addView(sGallerySelector);
				}
			}
			sGallerySelector.setVisibility(View.VISIBLE);
		}
	}	

}
