package com.jiubang.goscreenlock.theme.cjpcardcool.view.laceview;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;

/**
 * 
 * <br>
 * 类描述:随机出现的事物，包括自动出现，吹气出现和摇晃出现 <br>
 * 功能详细描述:
 * 
 * @author guoyiqing
 * @date [2012-10-23]
 */
public class AutoRandom extends BaseElement implements IMovable {

	private static final float SPEED_FACTOR = 1.0f;
	private static final int TYPE_FLOWER = 0;
	private static final int TYPE_WATER = 1;
	private Random mRandom = new Random();
	private int mSpeedAlpha;
	private int mSpeedX;
	private int mSpeedY;
	private int mRotateX;
	private int mRotateY;
	private int mTumbleSpdX;
	private int mTumbleSpdY;
	private int mScreenWidth;
	private int mScreenHeight;
	private Matrix mMatrix;
	private int mColor;
	private CellDrawable mCellDrawable;

	public int getmColor() {
		return mColor;
	}

	public void setmColor(int mColor) {
		this.mColor = mColor;
	}

	public AutoRandom(Resources res, Bitmap[] typeFlowerBitmaps,
			Bitmap[] typeWaterBitmaps, int x, int y, boolean allowDrag,
			int type, int viewWidth, int viewHeight, int color) {
		mColor = color;
		mMatrix = new Matrix();
		mAlive = true;
		mScreenHeight = viewHeight;
		mScreenWidth = viewWidth;
		mAllowDrag = allowDrag;
		mX = x;
		mY = y;
		mScale = getRadomLargeScale();
		mSpeedAlpha = -1;
		mSpeedX = (int) ((-3 + mRandom.nextInt(6)) * SPEED_FACTOR);
		mSpeedY = (int) ((mRandom.nextInt(4) + 3) * SPEED_FACTOR);
		if (type == TYPE_FLOWER) {
			if (typeFlowerBitmaps != null) {
				mBitmap = typeFlowerBitmaps[mRandom
						.nextInt(typeFlowerBitmaps.length)];
			}
			/*mRotateX = mRandom.nextInt(45);
			mRotateY = mRandom.nextInt(45);*/
			mRotateX = 0;
			mRotateY = 0;
			mTumbleSpdX = (int) ((mRandom.nextInt(8) + 3));
			mTumbleSpdY = (int) ((mRandom.nextInt(8) + 3));
		}
		if (type == TYPE_WATER) {
			mBitmap = typeWaterBitmaps[mRandom.nextInt(typeWaterBitmaps.length)];
			mRotateX = 0;
			mRotateY = 0;
			mTumbleSpdX = 0;
			mTumbleSpdY = 0;
			mSpeedX = 0;
		}
		mCellDrawable = new CellDrawable(res, mBitmap, null);
	}

	@Override
	public void doDraw(Camera camera, Matrix matrix, Canvas canvas, Paint paint) {
		if (mAlive && Constant.sIsScreenOn) {
			if (mBitmap != null) {
				int oldAlpha = paint.getAlpha();
				try {
					mMatrix.set(matrix);
					canvas.save();
					canvas.translate(mX, mY);
					canvas.scale(mScale, mScale);
					camera.save();
					camera.rotateX(mRotateX);
					camera.rotateY(mRotateY);
					camera.getMatrix(mMatrix);
					canvas.concat(mMatrix);
					camera.restore();
					paint.setAlpha(mAlpha);
					/* canvas.drawBitmap(mBitmap, 0, 0, paint); */
					mCellDrawable.draw(canvas, mColor, 0, 0, 1.0f, paint, 0,
							false); // don,t used color
				} finally {
					paint.setAlpha(oldAlpha);
					canvas.restore();
					mMatrix.reset();
				}
			}
		}
	}

	private float getRadomLargeScale() {
		return 0.6f + (mRandom.nextInt(10) + 0.01f) / 10;
	}

	@Override
	public void moving() {
		if (mAlive) {
			mX += mSpeedX;
			mY += mSpeedY;
			/*mRotateX += mTumbleSpdX;
			mRotateY += mTumbleSpdY;
			mRotateX %= 360;
			mRotateY %= 360;*/ //不旋转
			mAlpha += mSpeedAlpha;
			if (mY >= mScreenHeight /* || mAlpha <= 0 */ || mX >= mScreenWidth
					|| mX <= 0 /* || mY <= 0 */) {
				mAlive = false;
			}
		}
	}

	@Override
	public boolean isTaped(float x, float y) {
		return false;
	}

	@Override
	public void cleanUp() {
		super.cleanUp();
	}
}
