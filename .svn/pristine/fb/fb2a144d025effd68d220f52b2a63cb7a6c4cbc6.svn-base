package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiubang.goscreenlock.theme.cjpcardcool.ThemeSetProvider;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.ImageUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Util;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.LiveListener;

/**
 * 
 * @author June Kwok
 * 
 */
public class Selector extends FrameLayout implements LiveListener {

	public final static int MUSIC = 0;
	public final static int GALLERY = 1;
	private Content mContent;
	private LayoutParams mContentLP;
	private View mView;
	private int mSelectorType;
	public Selector(Context context, View view, int n) {
		super(context);
		mView = view;
		mSelectorType = n;
		init();

	}
	public Selector(Context context, View view) {
		super(context);
		mView = view;
		mSelectorType = MUSIC;
		init();
	}
	private void init() {
		this.setBackgroundColor(0x7f000000);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mContentLP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		mContent = new Content(getContext());
		this.addView(mContent, mContentLP);
		try {
			Bitmap cache = null;
			//			Bitmap cache = mView.getDrawingCache();
			//			if (cache != null) {
			DisplayMetrics dm = new DisplayMetrics();
			dm = getContext().getResources().getDisplayMetrics();
			int h = mContent.mTitleLP.height + mContent.mGridLP.height + mContent.mButtonLayoutLP.height + mContent.mCursorsLayoutLP.height;
			int top = Constant.sRealHeight / 2 - h / 2;
			cache = ImageUtils.cutBitmap(mView, dm.widthPixels / 2 - mContent.mGridLP.width / 2, top, 0, 0, mContent.mGridLP.width, h);
			Bitmap b = Bitmap.createScaledBitmap(cache, mContent.mGridLP.width / 8, mContent.mGridLP.width / 8, true);
			ViewUtils.recycleBitmap(cache);
			cache = ImageUtils.getGaussina(b, 5);
			//cache = ImageUtils.coverBitmap(cache);
			//cache = ImageUtils.getRoundedCornerBitmap(cache, 45 * Constant.sXRate / 8);
			mContent.setBackgroundDrawable(ImageUtils.bitmapToDrawable(cache));
			//			} else {
			//				mContent.setBackgroundColor(0xafffffff & Color.BLACK);
			//			}
		} catch (OutOfMemoryError e) {
			mContent.setBackgroundColor(0xafffffff & Color.BLACK);
		} catch (Exception e) {
			mContent.setBackgroundColor(0xafffffff & Color.BLACK);
		}
	}
	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if (visibility == VISIBLE) {
			this.bringToFront();
		}
	}
	/**
	 * 
	 * @author June Kwok
	 *
	 */
	private class Content extends LinearLayout implements OnClickListener {
		private static final int NUM_OF_ITEMS_IN_EACH_PAGE = 6;
		private int mConW, mConH, mSelecedPager;
		private LinearLayout mButtonLayout, mCursorsLayout;
		private TextView mTitle, mLineOne, mLineTwo;
		private TextView mAlways, mOnce;
		private List<Object> mListData;
		private List<View> mGridList;
		private List<CursorView> mCursors;
		private ViewPager mPager;
		private LayoutParams mButtonLayoutLP, mPagerLP, mTitleLP, mAlwaysLP, mOnceLP, mGridLP, mLineOneLP, mLineTwoLP, mCursorsLayoutLP;
		/**
		 * @param context
		 */
		public Content(Context context) {
			super(context);
			setOrientation(LinearLayout.VERTICAL);
			setGravity(Gravity.CENTER_HORIZONTAL);
			initData();
			initViewPager();
			init();
		}
		private void initData() {
			mListData = new ArrayList<Object>();
			List<ResolveInfo> mAppItemList = mSelectorType == MUSIC ? Util.getMusicActivities(getContext()) : Util.getPhotoActivities(getContext());
			int size = 0;
			if (mAppItemList != null && mAppItemList.size() > 0) {
				findAndLaunchActivity(mAppItemList, getContext());
				size = mAppItemList.size();
			}
			if (size > 6) {
				size = 6;
			}
			mConW = (int) (Constant.sRealWidth * 0.90f);
			mConH = (int) (0.38 * mConW * (1 + (size - 1) / 3));
			mConH = mConH > Constant.sRealWidth ? Constant.sRealWidth : mConH;
			mGridLP = new LayoutParams(mConW, mConH, Gravity.CENTER);
			mGridList = new ArrayList<View>();
			List<ResolveInfo> mItemList = new ArrayList<ResolveInfo>();
			size = mAppItemList.size();
			int m = size % NUM_OF_ITEMS_IN_EACH_PAGE == 0 ? size / NUM_OF_ITEMS_IN_EACH_PAGE : size / NUM_OF_ITEMS_IN_EACH_PAGE + 1;
			for (int n = 0; n < m; n++) {
				for (int i = 0; i < NUM_OF_ITEMS_IN_EACH_PAGE; i++) {
					if (n * NUM_OF_ITEMS_IN_EACH_PAGE + i < size) {
						mItemList.add(mAppItemList.get(n * NUM_OF_ITEMS_IN_EACH_PAGE + i));
					}
				}
				List<ResolveInfo> mTempList = new ArrayList<ResolveInfo>();
				mTempList.addAll(mItemList);
				mListData.add(mItemList);
				Grid mG = new Grid(getContext(), mGridLP.width, mGridLP.height, mTempList);
				mG.setLayoutParams(mGridLP);
				mGridList.add(mG);
				mItemList.clear();
			}
		}
		private void initViewPager() {
			mPagerLP = new LayoutParams(mGridLP.width, mGridLP.height, 1);
			mPager = new ViewPager(getContext());
			mPager.setAdapter(new GridPagerAdapter(mGridList));
			mPager.setOnPageChangeListener(new PageSelectedListener());

			mCursors = new ArrayList<Selector.CursorView>();
			mCursorsLayout = new LinearLayout(getContext());
			mCursorsLayout.setGravity(Gravity.CENTER);
			for (int i = 0; i < mGridList.size(); i++) {
				CursorView mCursor = new CursorView(getContext());
				mCursors.add(mCursor);
				mCursorsLayout.addView(mCursor);
				mCursor.setSelected(i == 0);
			}
			mCursorsLayoutLP = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (24 * Constant.sXRate));
		}
		private void init() {
			mAlwaysLP = new LayoutParams(mConW / 2 - (int) (11 * Constant.sXRate), LayoutParams.MATCH_PARENT, 1);
			mOnceLP = new LayoutParams(mConW / 2 - (int) (11 * Constant.sXRate), LayoutParams.MATCH_PARENT, 1);
			mButtonLayoutLP = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (110 * Constant.sXRate));
			mTitleLP = new LayoutParams(LayoutParams.WRAP_CONTENT, (int) (130 * Constant.sXRate), 1);

			mLineOneLP = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (2 * Constant.sXRate), 1);
			mLineTwoLP = new LayoutParams((int) (2 * Constant.sXRate), LayoutParams.MATCH_PARENT, 1);

			mLineOne = new TextView(getContext());
			mLineTwo = new TextView(getContext());
			mLineOne.setBackgroundColor(0x33FFFFFF & Color.WHITE);
			mLineTwo.setBackgroundColor(0x33FFFFFF & Color.WHITE);

			mTitle = new TextView(getContext());
			mTitle.setText("Choose the application");
			mTitle.setTextColor(Color.WHITE);
			mTitle.setGravity(Gravity.CENTER);
			mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (35 * Constant.sXRate));

			mButtonLayout = new LinearLayout(getContext());
			int padding = (int) (10 * Constant.sXRate);
			mButtonLayout.setPadding(padding, 0, padding, 0);

			mAlways = new TextView(getContext());
			mAlways.setTextColor(Color.WHITE);
			mAlways.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (35 * Constant.sXRate));
			mAlways.setText("Always");
			mAlways.setGravity(Gravity.CENTER);
			mAlways.setOnClickListener(this);

			mOnce = new TextView(getContext());
			mOnce.setTextColor(Color.WHITE);
			mOnce.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (35 * Constant.sXRate));
			mOnce.setText("Once");
			mOnce.setGravity(Gravity.CENTER);
			mOnce.setOnClickListener(this);

			mButtonLayout.addView(mAlways, mAlwaysLP);
			mButtonLayout.addView(mLineTwo, mLineTwoLP);
			mButtonLayout.addView(mOnce, mOnceLP);

			addView(mTitle, mTitleLP);
			addView(mPager, mPagerLP);
			addView(mCursorsLayout, mCursorsLayoutLP);
			addView(mLineOne, mLineOneLP);
			addView(mButtonLayout, mButtonLayoutLP);

		}
		@Override
		public void onClick(View v) {
			String pkg = ((Grid) mGridList.get(mSelecedPager)).getCurrentSelect();
			if (pkg != null && !pkg.equals("")) {
				if (v == mAlways) {
					if (mSelectorType == MUSIC) {
						//ThemeSettingProvider.setMusic(getContext(), pkg);
					} else {
						ThemeSetProvider.setGallery(getContext(), pkg);
					}

				}
			}
			unlock(pkg);
		}
		public void updateCursor(int n) {
			mSelecedPager = n;
			for (int i = 0; i < mCursors.size(); i++) {
				mCursors.get(i).setSelected(n == i);
			}
		}
	}
	/**
	 * 
	 * @author guojun
	 *
	 */
	private class CursorView extends View {
		private int mWidth, mHeight;
		private Paint mPaint;
		private boolean mIsSelected = false;
		/**
		 * @param context
		 */
		public CursorView(Context context) {
			super(context);
			init();
		}
		public void init() {
			mWidth = (int) (12 * Constant.sXRate);
			mHeight = (int) (12 * Constant.sXRate);
			setLayoutParams(new LayoutParams(2 * mWidth, 2 * mHeight));
			setPadding(mWidth / 2, mWidth / 2, mWidth / 2, mWidth / 2);
			mPaint = new Paint();
			mPaint.setAntiAlias(true);
		}
		@Override
		protected void onDraw(Canvas canvas) {
			canvas.saveLayer(0, 0, mWidth, mHeight, null, Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
					| Canvas.CLIP_TO_LAYER_SAVE_FLAG);
			if (mIsSelected) {
				mPaint.setColor(0xFFFFFFFF);
			} else {
				mPaint.setColor(0x7FFFFFFF);
			}
			canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, mPaint);
		}
		/**
		 * @return the mIsSelected
		 */
		public final boolean isSelected() {
			return mIsSelected;
		}
		/**
		 * @param mIsSelected the mIsSelected to set
		 */
		public final void setSelected(boolean mIsSelected) {
			this.mIsSelected = mIsSelected;
			invalidate();
		}
	}
	/**
	 * 
	 * @author guojun
	 *
	 */
	public class PageSelectedListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			mContent.updateCursor(arg0);
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}
	}
	/**
	 * 
	 * <br>
	 * 类描述: 标准组件的适配器
	 * 功能详细描述:
	 * 
	 * @author June Kwok
	 * @date [2014-03-21]
	 */
	public class GridPagerAdapter extends PagerAdapter {
		private List<View> mViewList;
		public GridPagerAdapter(List<View> viewList) {
			mViewList = viewList;
		}
		@Override
		public int getCount() {
			if (mViewList != null) {
				return mViewList.size();
			}
			return 0;
		}
		@Override
		public Object instantiateItem(View view, int index) {
			((ViewPager) view).addView(mViewList.get(index), 0);
			return mViewList.get(index);
		}
		@Override
		public void destroyItem(View view, int position, Object arg2) {
			((ViewPager) view).removeView(mViewList.get(position));
		}
		@Override
		public void finishUpdate(View arg0) {
		}
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}
		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}
		@Override
		/* (non-Javadoc)
		* @see android.app.Activity#onDestroy()
		*/
		public Parcelable saveState() {
			return null;
		}
		@Override
		public void startUpdate(View arg0) {
		}
	}
	private void findAndLaunchActivity(List<ResolveInfo> list, Context context) {
		try {
			for (int i = 0, count = list.size(); i < count; i++) {
				Object bject = list.get(i);
				if (bject instanceof ResolveInfo) {
					ResolveInfo info = (ResolveInfo) bject;
					if (info.activityInfo.packageName == null) {
						list.remove(info);
						continue;
					}
					Intent intent = Util.getIntentByPackageName(context, info.activityInfo.packageName);
					if (!Util.isIntentAvailable(context, intent)) {
						list.remove(info);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean unlock() {
		if (mContent.mListData.isEmpty()) {
			setVisibility(GONE);
			Global.sendUnlockWithIntent(getContext(), null, null, null, null);
		} else if (mContent.mListData.size() == 1) {
			setVisibility(GONE);
			//			unlock(mContent.mListData.get(0));
		} else {
			return false;
		}
		return false;
	}

	private void unlock(String pkg) {
		if (pkg != null && !pkg.equals("")) {
			Intent intent = Util.getIntentByPackageName(getContext(), pkg);
			if (Util.isIntentAvailable(getContext(), intent)) {
				Global.sendUnlockWithIntent(getContext(), null, null, null, intent);
			}
			setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		setVisibility(View.GONE);
		return true;
	}

	@Override
	public void onPause() {
		setVisibility(View.GONE);
	}

	@Override
	public void onDestroy() {
	}

	@Override
	public void onResume() {
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

}
