package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Build.VERSION;
import android.view.animation.Animation;
import android.view.animation.Transformation;


/**
 * 3D空间中绕Y轴旋转的动画
 * 
 * @author dengweiming
 * 
 */
public class Flip3DAnimation extends Animation {

	public final static int VERTICAL = 1;
	public final static int HORIZONTAL = 2;
	private int mOrientation = HORIZONTAL;

	boolean mIsPlaying;
	float mFromAngle;
	float mToAngle;
	float mTotalAngle;
	float mPivotX;
	float mPivotY;
	Camera mCamera;

	public Flip3DAnimation(float fromAngle, float toAngle) {
		mFromAngle = fromAngle;
		mToAngle = toAngle;
		mTotalAngle = mToAngle - mFromAngle;
		mCamera = new Camera();
		try {
			int sysVersion = Integer.parseInt(VERSION.SDK);
			if (sysVersion >= 19) {
				mCamera.setLocation(0, 0, ViewUtils.getPXByWidth(-50));
			} else {
				mCamera.setLocation(0, 0, ViewUtils.getPXByWidth(-20));
			}
		} catch (NoSuchMethodError e) {
			// TODO: handle exception
		}
	}

	public void setAngle(float fromAngle, float toAngle) {
		mFromAngle = fromAngle;
		mToAngle = toAngle;
		mTotalAngle = mToAngle - mFromAngle;
	}

	public void setOrientation(int orientation) {
		mOrientation = orientation;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final Matrix matrix = t.getMatrix();
		float angle = mFromAngle + mTotalAngle * interpolatedTime;
		mCamera.save();
		mCamera.translate(0, 0, 0);
		if (mOrientation == VERTICAL) {
			mCamera.rotateX(angle);
		} else {
			mCamera.rotateY(angle);
		}
		mCamera.getMatrix(matrix);
		mCamera.restore();
		matrix.postTranslate(mPivotX, mPivotY);
		matrix.preTranslate(-mPivotX, -mPivotY);
		applyAngle(angle);
	}

	protected void applyAngle(float angle) {

	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);

		mPivotX = resolveSize(RELATIVE_TO_SELF, 0.5f, width, parentWidth);
		mPivotY = resolveSize(RELATIVE_TO_SELF, 0.5f, height, parentHeight);

	}

	@Override
	public boolean willChangeBounds() {
		return false;
	}

}
