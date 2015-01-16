package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.viewHelper.ViewHelper;

/**
 * 
 * 开关菜单
 * 
 * @author chenjianping
 * 
 */
public class SwitcherItemLayout extends LinearLayout {

    private ImageView mImageView/*, mRight*/;
    private CustomTextView mTextView;

    private int mSwitcherWidth;
    private Context mContext;
    
    public boolean mSwitcherStatus = false;

    private String mTextName;
    public SwitcherItemLayout(Context context, String name) {
        super(context);
        this.mContext = context;
        
        mTextName = name;

        
        init();
        addChildView();
    }

    private void init() {
        if (mSwitcherWidth == 0) {
            mSwitcherWidth = ViewUtils.getPXByWidth(82);
        }
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        LayoutParams mParams = new LayoutParams(mSwitcherWidth, 
        		mSwitcherWidth, 1);
        //setBackgroundColor(0x33000000);
        this.setLayoutParams(mParams);
    }

    private void addChildView() {
        LinearLayout.LayoutParams mParams = new LayoutParams(mSwitcherWidth, mSwitcherWidth);
        mImageView = new ImageView(mContext);
        //mParams.bottomMargin = ViewUtils.getPXByHeight(15);
        //mImageView.setBackgroundResource(R.drawable.switch_bg);
        addView(mImageView, mParams);
        
        /*LinearLayout.LayoutParams mRParams = new LayoutParams(
        		ViewUtils.getPXByHeight(86), ViewUtils.getPXByHeight(40));
        mRParams.gravity = Gravity.CENTER_VERTICAL;
        mRParams.leftMargin = ViewUtils.getPXByWidth(560) - ViewUtils.getPXByHeight(86) - mSwitcherWidth;
        mRight = new ImageView(mContext);
        addView(mRight, mRParams);*/

        if (mTextName != null) {
	        mTextView = new CustomTextView(mContext);
	        mTextView.setTextColor(Color.WHITE);
	        mTextView.setGravity(Gravity.CENTER);
	        mTextView.setTypeface(CircleContainer.sTypefaceCal);
	        mTextView.setText(mTextName);
	        mTextView.setTextSize(28);
	        addView(mTextView);
        }
    }

    public void setImageResource(int resId, int rightRes) {
        if (mImageView != null) {
            mImageView.setImageResource(resId);
        }
        /*if (mRight != null) {
        	mRight.setImageResource(rightRes);
        }*/
    }
    
    public void setImageResource(int resId) {
        if (mImageView != null) {
            mImageView.setImageResource(resId);
            ViewHelper.setAlpha(mImageView, 1);
        }
        if (mTextView != null) {
            ViewHelper.setAlpha(mTextView, 1);
        }
    }
    
    public void setImageResource(int resId, float alpha) {
        if (mImageView != null) {
            mImageView.setImageResource(resId);
            ViewHelper.setAlpha(mImageView, alpha);
        }
        if (mTextView != null) {
            ViewHelper.setAlpha(mTextView, alpha);
        }
    }

    public void invisibleText() {
        /*if (null != mTextView) {
            mTextView.setVisibility(View.INVISIBLE);
        }*/
    }

    public void visibleText() {
        /*if (null != mTextView) {
            mTextView.setVisibility(View.VISIBLE);
        }*/
    }
}
