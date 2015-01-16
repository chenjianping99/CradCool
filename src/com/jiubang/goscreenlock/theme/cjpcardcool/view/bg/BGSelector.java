package com.jiubang.goscreenlock.theme.cjpcardcool.view.bg;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.ThemeSetChangeListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.ThemeSetProvider;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ViewUtils;

/**
 * 
 * @author chenjianping
 * 
 */
public class BGSelector extends LinearLayout {

	private static final int[] RESID = {/*R.drawable.bg_icon1, R.drawable.bg_icon2,
			R.drawable.bg_icon3, R.drawable.bg_icon4*/};
	
	public BGSelector(Context context) {
		super(context);
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER);
		addSelector();
	}
	
	private LinearLayout mSelectorExLy;
	private void addSelector() {
		mSelectorExLy = new LinearLayout(getContext());
		mSelectorExLy.setOrientation(LinearLayout.HORIZONTAL);
		mSelectorExLy.setGravity(Gravity.CENTER);
		for (int i = 0; i < RESID.length; i++) {
			SelectorEx selector = new SelectorEx(getContext(), i);
			selector.setId(i);
			LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1);
			mSelectorExLy.addView(selector, param1);
		}

		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(mSelectorExLy, param);
		//mSelectorExLy.setBackgroundColor(0x33000000);
	}
	
	
	/*private View mRight;
	private void addRightViews() {
		final Paint mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(ViewUtils.getPXByHeight(4));
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(Color.GRAY);
		int w = ViewUtils.getPXByHeight(60);
		final int h = ViewUtils.getPXByHeight(50);
		final float[] pts = {ViewUtils.getPXByHeight(25), 0, ViewUtils.getPXByHeight(25), ViewUtils.getPXByHeight(50)};
		final Point a = new Point(ViewUtils.getPXByHeight(40), ViewUtils.getPXByHeight(15));
		final Point b = new Point(ViewUtils.getPXByHeight(60), ViewUtils.getPXByHeight(15));
		final Point c = new Point(ViewUtils.getPXByHeight(50), h - ViewUtils.getPXByHeight(15));
	   
		mRight = new View(getContext()) {
			@Override
			protected void onDraw(Canvas canvas) {
				canvas.drawLines(pts, mPaint);
				 
				Path path = new Path();
			    path.moveTo(a.x, a.y);
			    path.lineTo(b.x, b.y);
			    path.lineTo(c.x, c.y);
			    path.lineTo(a.x, a.y);
			    path.close();
			    canvas.drawPath(path, mPaint);
			}
		};
		
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				w, h);
		param.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
		addView(mRight, param);
		mRight.setOnClickListener(this);
	}*/
	

	/**
	 * 
	 * @author chenjianping
	 * 
	 */
	class SelectorEx extends FrameLayout implements OnClickListener, ThemeSetChangeListener {
		private ImageView  mIcon, mSelected;
		//private TextView mSelectorText;
		private boolean mIsSelected = false;
		private int mIndex = 0;
		//private final String[] mNAME = {"Juicy", "Bloom", "Wave"};
		public SelectorEx(Context context, int index) {
			super(context);
			ThemeSetProvider.addThemeSetChangeListener(this);
			mIndex = index;
			mIsSelected = ThemeSetProvider.getBackgroundIndex(context) == index ? true : false;
			initViews(context);
		}

		private void initViews(Context context) {
			mIcon = new ImageView(context);
			mIcon.setBackgroundResource(RESID[mIndex]);
			int mIconW = ViewUtils.getPXByHeight(106);
			LayoutParams mIconParam = new LayoutParams(mIconW, 
					mIconW, Gravity.CENTER/*_VERTICAL | Gravity.LEFT*/);
			addView(mIcon, mIconParam);
			setOnClickListener(this);
			
			mSelected = new ImageView(context);
			//mSelected.setBackgroundResource(R.drawable.bg_chose_icon);
			int mSelectedW = ViewUtils.getPXByHeight(106);
			int mSelectedH = mSelectedW;
			LayoutParams mSelectedParam = new LayoutParams(mSelectedW, 
					mSelectedH, Gravity.CENTER/*_VERTICAL | Gravity.LEFT*/);
			//mSelectedParam.leftMargin = (mIconW - mSelectedW) / 2;
			addView(mSelected, mSelectedParam);
			if (!mIsSelected) {
				mSelected.setVisibility(GONE);
			}
			
			/*mSelectorText = new TextView(context);
			mSelectorText.setTextColor(Color.GRAY);
			mSelectorText.setText(mNAME[mIndex]);
			mSelectorText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(35));
			mSelectorText.setTypeface(CircleContainer.sTypeface);
			Drawable d = getResources().getDrawable(RESID[mIndex + 3]);
			int dw = ViewUtils.getPXByHeight(40);
			int dh = ViewUtils.getPXByHeight(8);
			d.setBounds(0, 0, dw, dh);
			mSelectorText.setCompoundDrawables(null, null, null, d);
			LayoutParams mSelectorTextParam = new LayoutParams(LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.LEFT);
			mSelectorTextParam.leftMargin = ViewUtils.getPXByHeight(70);
			addView(mSelectorText, mSelectorTextParam);*/
		}

		@Override
		public void onClick(View v) {
			Constant.sBgIndex = mIndex;
			ThemeSetProvider.setBackgroundIndex(getContext(), mIndex);
			mSelected.setVisibility(VISIBLE);
		}
		
		private void updateSelectState() {
			mIsSelected = ThemeSetProvider.getBackgroundIndex(getContext()) == mIndex ? true : false;
			if (!mIsSelected) {
				mSelected.setVisibility(GONE);
			}
		}

		@Override
		public void onBGChange() {
			updateSelectState();
		}
		
	}
}
