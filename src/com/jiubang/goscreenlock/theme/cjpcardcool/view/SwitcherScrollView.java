package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;

/**
 * 开关选择背景横向view
 * 
 * @author iamzl
 * 
 */
public class SwitcherScrollView extends HorizontalScrollView implements OnClickListener {
    private ImageView[] mSwitcherViews = new ImageView[5];
    private int[] mBackgroundIconResIds = new int[] { //
            };

    private int[] mBackgroundResIds = new int[] { //
            };
    private LinearLayout mContainer;
    private Context mContext;
    private int mWidth;

    public SwitcherScrollView(Context context) {
        super(context);
        this.mContext = context;
        init();
        initContainer();
        addItemView();
    }

    private void init() {
        if (mWidth == 0) {
            mWidth = ViewUtils.getPXByWidth(90);
        }
        setHorizontalScrollBarEnabled(false);
        setFadingEdgeLength(0);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        LayoutParams mParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mParams.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER;
        this.setLayoutParams(mParams);
    }

    private void initContainer() {
        mContainer = new LinearLayout(mContext);
        ScrollView.LayoutParams mScrollParams = new ScrollView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        addView(mContainer, mScrollParams);

    }

    private void addItemView() {
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(mWidth, mWidth);
        mParams.leftMargin = mWidth / 3;
        mParams.gravity = Gravity.CENTER_VERTICAL;
        for (int i = 0; i < GetPictureActivity.sCustomBgNum; ++i) {
            mSwitcherViews[i] = new ImageView(mContext);
            mSwitcherViews[i].setOnClickListener(this);
            mSwitcherViews[i].setImageResource(mBackgroundIconResIds[i]);
            mSwitcherViews[i].setVisibility(View.INVISIBLE);
            mSwitcherViews[i].setTag(mBackgroundResIds[i]);
            mContainer.addView(mSwitcherViews[i], mParams);
        }
        show(-1);
    }

    private void show(int index) {
        final int loop = ++index;
        if (index > mContainer.getChildCount() - 1) {
            return;
        }
        Animation animation = null;
        animation = new TranslateAnimation(-(mWidth + mWidth / 3), 0, 0, 0);
        animation.setFillAfter(true);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(150);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                show(loop);
            }
        });
        View view = mContainer.getChildAt(index);
        view.setVisibility(View.VISIBLE);
        view.requestLayout();
        view.startAnimation(animation);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onClick(View v) {
        //mPargent.setBackgroundByWhere((Integer) v.getTag());
    }
    
    
	/*private void checkCustomBg() {
		if (GetPictureActivity.sCustomBgNum > 0) {
			ViewUtils.recycleBitmap(mBitmap);
			int index = new Random().nextInt(GetPictureActivity.sCustomBgNum);
			mBitmap = Global.getMyBgBitmap(getContext(), index);
			if (mBitmap != null) {
				//mMusicBg.setBackgroundDrawable(new BitmapDrawable(mBitmap));
			} else {
				//mMusicBg.setBackgroundResource(R.drawable.music_bg);
			}
		}
	}*/
	private Bitmap[] mBitmap;
	private void initCustomBgs() {
		if (GetPictureActivity.sCustomBgNum > 0) {
			mBitmap = new Bitmap[GetPictureActivity.sCustomBgNum];
			for (int i = 0; i < GetPictureActivity.sCustomBgNum; i++) {
				mBitmap[0] = Global.getMyBgBitmap(getContext(), i);
			}
		}
	}
}
