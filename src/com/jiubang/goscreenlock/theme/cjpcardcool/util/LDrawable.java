package com.jiubang.goscreenlock.theme.cjpcardcool.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Android L button effect drawable
 * @author chenkai
 *
 */
public class LDrawable extends Drawable {
	private static final int DURATION_SHOW = 200;
	private static final int DURATION_HIDE = 400;
	private static final int MAX_ALPHA = 150;
	private static final float SCALE = 0.95f;
	private static final Interpolator INTERPOLATOR = new DecelerateInterpolator(2f);

	private float mScale = SCALE;
	private Paint mPaint;
	private boolean mPressing;
	private long mAnimStartTime;
	private int mAlpha;
	private float mRadius;

	public LDrawable() {
		this(Color.WHITE);
	}

	public LDrawable(int color) {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(color);
	}

	public LDrawable(int color, float scale) {
		this(color);
		mScale = scale;
	}

	@Override
	public void draw(Canvas canvas) {
		Rect rect = getBounds();
		boolean pressing = isPressing();
		int w = (int) ((rect.right - rect.left) * mScale);
		int h = (int) ((rect.bottom - rect.top) * mScale);
		float cx = (rect.right - rect.left) / 2f;
		float cy = (rect.bottom - rect.top) / 2f;
		int r = (w < h ? h : w) / 2;
		if (!mPressing && pressing) {
			mAnimStartTime = System.currentTimeMillis();
			mPressing = true;
			invalidateSelf();
		} else if (mPressing && pressing) {
			long offset = System.currentTimeMillis() - mAnimStartTime;
			if (offset <= DURATION_SHOW) {
				float t = INTERPOLATOR.getInterpolation(offset * 1f / DURATION_SHOW);
				mAlpha = (int) (MAX_ALPHA * t);
				mRadius = r * t;
				mPaint.setAlpha(mAlpha);
				canvas.drawCircle(cx, cy, mRadius, mPaint);
				invalidateSelf();
			} else {
				mAnimStartTime = 0;
				mAlpha = MAX_ALPHA;
				mRadius = r;
				mPaint.setAlpha(mAlpha);
				canvas.drawCircle(cx, cy, mRadius, mPaint);
			}
		} else if (mPressing) {
			mPressing = false;
			mAnimStartTime = System.currentTimeMillis();
			mPaint.setAlpha(mAlpha);
			canvas.drawCircle(cx, cy, mRadius, mPaint);
			invalidateSelf();
		} else {
			long offset = System.currentTimeMillis() - mAnimStartTime;
			if (offset <= DURATION_HIDE) {
				float t = INTERPOLATOR.getInterpolation((DURATION_HIDE - offset) * 1f / DURATION_HIDE);
				t = (DURATION_HIDE - offset) * 1f / DURATION_HIDE;
				mPaint.setAlpha((int) (mAlpha * t));
				canvas.drawCircle(cx, cy, r - (r - mRadius) * t, mPaint);
				invalidateSelf();
			} else {
				mRadius = 0;
				mAlpha = 0;
				mAnimStartTime = 0;
			}
		}
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	}

	@Override
	public int getOpacity() {
		return 0;
	}

	private boolean isPressing() {
		int[] states = getState();
		for (int i = 0; i < states.length; i++) {
			if (states[i] == android.R.attr.state_pressed) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isStateful() {
		return true;
	}

	@Override
	protected boolean onStateChange(int[] state) {
		invalidateSelf();
		return true;
	}
}
