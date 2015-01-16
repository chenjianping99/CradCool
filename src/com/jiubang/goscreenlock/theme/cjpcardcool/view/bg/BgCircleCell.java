package com.jiubang.goscreenlock.theme.cjpcardcool.view.bg;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;

/**
 * 动态壁纸的每个圆的对象
 * @author liangdaijian
 *
 */
public class BgCircleCell
{
	private RectF		mRectF;
	private int			mColor;
	private int			mAlpha;
	private boolean		mIsVisiable;
	private Shader		mShader;

	private float		mWidthInit;
	private float		mHeightInit;

	private Random		mRandom;
	private IDeleteCell	mIDeleteCell;
	private int			mFlag;

	public BgCircleCell(RectF mRectF, int mColor, int mAlpha, boolean mIsVisiable, Random mRandom,
			IDeleteCell mIDeleteCell, int mFlag)
	{
		super();
		this.mRectF = mRectF;
		this.mColor = mColor;
		this.mAlpha = mAlpha;
		this.mIsVisiable = mIsVisiable;
		this.mRandom = mRandom;
		this.mIDeleteCell = mIDeleteCell;
		this.mFlag = mFlag;

		if (this.mRectF != null)
		{
			mWidthInit = this.mRectF.width();
			mHeightInit = this.mRectF.height();
		}
	}

	// 所有物理运动的时间拉到当前，继续上次的物理运动过程
	public void resetTime()
	{
		if (mAppearTimeLast != 0)
		{
			mAppearTimeLast = System.currentTimeMillis();
		}
		if (mRandomTimeLast != 0)
		{
			mRandomTimeLast = System.currentTimeMillis();
		}
		if (mShakeTimeLast != 0)
		{
			mShakeTimeLast = System.currentTimeMillis();
		}
		if (mSensorTimeLast != 0)
		{
			mSensorTimeLast = System.currentTimeMillis();
		}
		if (mSensorTimeLastY != 0)
		{
			mSensorTimeLastY = System.currentTimeMillis();
		}
		if (mDisappearTimeLast != 0)
		{
			mDisappearTimeLast = System.currentTimeMillis();
		}
	}

	// 每个元素的刷新
	public void draw(Canvas canvas, Paint paint)
	{
		try
		{
			if (mIsVisiable)
			{
				// 出现
				doAppear();
				doChangeAlpha();
				// 随机移动
				doRandom();
				// 上下抖动
				//doShake();
				// 重力感应
				//doSensor();
				// 消失
				doDisappear();
			}
			paint.setColor(mColor);
			paint.setAlpha(mAlphaEffect);
			if (mFlag == 0)
			{
				mShader = new RadialGradient(mRectF.centerX(), mRectF.centerY(), Math.min(
						mRectF.width() / 2, mRectF.height() / 2), new int[] { mColor, mColor & 0x88ffffff,
					mColor & 0x4fffffff, mColor & 0x00ffffff }, new float[] {0.0f, 0.5f, 0.75f, 1.0f }, TileMode.CLAMP);
			}
			else if (mFlag == 1)
			{
				mShader = new RadialGradient(mRectF.centerX(), mRectF.centerY(), Math.max(
						mRectF.width() / 2, mRectF.height() / 2), new int[] { mColor, mColor & 0x88ffffff,
					mColor & 0x4fffffff, mColor & 0x00ffffff }, new float[] {0.0f, 0.5f, 0.75f, 1.0f }, TileMode.CLAMP);
			}
			else if (mFlag == 2)
			{
				mShader = new RadialGradient(mRectF.centerX(), mRectF.centerY(), Math.max(
						mRectF.width() / 2, mRectF.height() / 2), new int[] { mColor, mColor & 0xbfffffff,
					mColor & 0x4fffffff, mColor & 0x00ffffff }, new float[] { 0.0f, 0.15f, 0.3f, 1.0f }, TileMode.CLAMP);
			}

			paint.setShader(mShader);
			canvas.save();
			canvas.scale(mScale, mScale, mRectF.centerX(), mRectF.centerY());
			canvas.drawOval(mRectF, paint);
			canvas.restore();
			/*if (mFlag == 0)
			{
				// 修复大园，上下抖动
				canvas.save();
				canvas.scale(1, mRectF.height() / mRectF.width(), mRectF.centerX(),
						mRectF.centerY());
				canvas.drawOval(mRectF, paint);
				canvas.restore();
			}
			else
			{
				canvas.drawOval(mRectF, paint);
			}*/
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	private long	mShakeTimeLast	= 0;
	private float	mShakeVInit		= 0.05f;
	private float	mShakeV			= mShakeVInit;
	private float	mShakeA			= 0.00025f;
	private boolean	mIsBig;
	// 大园的抖动
	private void doShake()
	{
		if (mRectF != null && mShakeTimeLast != 0)
		{
			long tp = System.currentTimeMillis() - mShakeTimeLast;
			mShakeTimeLast = System.currentTimeMillis();
			if (mIsBig)
			{
				// 减速到最大
				mShakeV = mShakeV - mShakeA * tp;
				mRectF.top = mRectF.top - mShakeV * tp;
				mRectF.bottom = mRectF.bottom + mShakeV * tp;
				if (mShakeV <= 0)
				{
					mIsBig = false;
					mShakeV = mShakeVInit;
				}
			}
			else
			{
				mShakeV = mShakeV - mShakeA * tp;
				mRectF.top = mRectF.top + mShakeV * tp;
				mRectF.bottom = mRectF.bottom - mShakeV * tp;
				if (mShakeV <= 0)
				{
					mRectF.bottom = mRectF.top + mHeightInit;
					mIsBig = true;
					mShakeV = mShakeVInit;
					mShakeTimeLast = 0;
					if (++mShakeI < mShakeCount)
					{
						mShakeTimeLast = System.currentTimeMillis();
					}
				}
			}
		}
	}

	private int	mShakeCount;
	private int	mShakeI;
	// 激活大园的抖动
	public void activeShake()
	{
		mShakeCount = 5 + mRandom.nextInt(5);
		mShakeI = 0;
		mShakeTimeLast = System.currentTimeMillis();
		mShakeV = mShakeVInit;
		mIsBig = true;
	}

	/**
	 * 修正位置，圆可以运动到屏幕外面刚好不可见，返回碰壁的类型
	 * @return 0 是左边碰壁，1 是上边碰壁，2是右边，3是下边
	 */
	private int fixLocationAvoidOut()
	{
		int result = -1;
		if (mRectF != null)
		{
			if (mRectF.left <= -mRectF.width())
			{
				mRectF.offset(-mRectF.width() - mRectF.left, 0);
				result = 0;
			}
			else if (mRectF.left >= Constant.sRealWidth)
			{
				mRectF.offset(Constant.sRealWidth - mRectF.left, 0);
				result = 2;
			}

			if (mRectF.top <= -mRectF.height())
			{
				mRectF.offset(0, -mRectF.height() - mRectF.top);
				result = 1;
			}
			else if (mRectF.top >= Constant.sRealHeight)
			{
				mRectF.offset(0, Constant.sRealHeight - mRectF.top);
				result = 3;
			}
		}
		return result;
	}

	// 计算传感器产生的效果
	private void doSensor()
	{
		boolean isok = false;
		if (mRectF != null && mSensorTimeLast != 0)
		{
			long tp = System.currentTimeMillis() - mSensorTimeLast;
			mSensorTimeLast = System.currentTimeMillis();
			mSensorV = mSensorV + mSensorA * tp;
			if (mSensorA * mSensorV >= 0)
			{
				mSensorTimeLast = 0;
				mSensorV = 0;
			}
			mRectF.offset(mSensorV * tp, 0);
			isok = true;
		}
		if (mRectF != null && mSensorTimeLastY != 0)
		{
			long tp = System.currentTimeMillis() - mSensorTimeLastY;
			mSensorTimeLastY = System.currentTimeMillis();
			mSensorVY = mSensorVY + mSensorAY * tp;
			if (mSensorAY * mSensorVY >= 0)
			{
				mSensorTimeLastY = 0;
				mSensorVY = 0;
			}
			mRectF.offset(0, mSensorVY * tp);
			isok = true;
		}
		if (isok)
		{
			fixLocationAvoidOut();
		}
	}

	private long	mSensorTimeLast		= 0;

	//	private float	mSensorVInit		= 0f;
	private float	mSensorV			= 0;
	private float	mSensorA			= Global.dip2px(4.5f) / 100000f;

	private long	mSensorTimeLastY	= 0;
	//	private float	mSensorVInitY		= 0f;
	private float	mSensorVY			= 0;
	private float	mSensorAY			= Global.dip2px(4.5f) / 100000f;

	public void activeSensorX(float x)
	{
		//		mSensorVInit = x;
		if (x > 0)
		{
			mSensorA = -Math.abs(mSensorA);
		}
		else
		{
			mSensorA = Math.abs(mSensorA);
		}
		mSensorV = x;
		mSensorTimeLast = System.currentTimeMillis();
	}
	public void activeSensorY(float y)
	{
		//		mSensorVInitY = y;
		if (y > 0)
		{
			mSensorAY = -Math.abs(mSensorA);
		}
		else
		{
			mSensorAY = Math.abs(mSensorA);
		}
		mSensorVY = y;
		mSensorTimeLastY = System.currentTimeMillis();
	}

	private long	mRandomTimeLast	= 0;
	private float	mRandomVx, mRandomVy;
	public void activeRandom()
	{
		if (mRandom != null)
		{
			mRandomTimeLast = System.currentTimeMillis();
			mRandomVx = (float) (mRandom.nextInt() % 15) / 200 * Constant.sXRate;
			mRandomVy = (float) (mRandom.nextInt() % 15) / 200 * Constant.sXRate;
			//LogUtils.log(null, "mRandomVx = " + mRandomVx + "mRandomVy=" + mRandomVy);
		}
	}

	private void doRandom()
	{
		if (mRectF != null && mRandomTimeLast != 0)
		{
			long t = System.currentTimeMillis() - mRandomTimeLast;
			mRandomTimeLast = System.currentTimeMillis();
			if (mSensorTimeLast + mSensorTimeLastY > 0)
			{
				// 让重力感应跑，不要随机自己跑
				return;
			}
			mRectF.offset(mRandomVx * t, mRandomVy * t);
			int flat = fixLocationAvoidOut();
			switch (flat)
			{
				case 0 :
					if (mRandomVx <= 0)
					{
						activeRandom();
					}
					break;
				case 1 :
					if (mRandomVy <= 0)
					{
						activeRandom();
					}
					break;
				case 2 :
					if (mRandomVx >= 0)
					{
						activeRandom();
					}
					break;
				case 3 :
					if (mRandomVy >= 0)
					{
						activeRandom();
					}
					break;
			}
		}
	}
	
	//改变alpha值
	private long mStartTime = 0;
	private int mAlphaEffect;
	private boolean mAddAlpha = true;
	private float mScale;
	private int mDuration;
	private void doChangeAlpha() {
		if (mStartTime == 0) {
			mStartTime = System.currentTimeMillis();
			mAddAlpha = true;
			mDuration = mRandom.nextInt(500) + 1500;
		}
		float t = (System.currentTimeMillis() - mStartTime) * 1f / mDuration;
		if (t < 1) {
			if (mAddAlpha) {
				mAlphaEffect = (int) (255 * t);
				mScale = (float) (0.2 + 0.8 * t);
			} else {
				mAlphaEffect = (int) (255 * (1 - t));
				mScale = 1 -  0.8f * t;
			}
		} else {
			if (mAddAlpha) {
				mStartTime = System.currentTimeMillis();
				mAddAlpha = false;
			} else {
				mStartTime = 0;
			}
			//LogUtils.log(null, "t = 1; mAlphaEffect =" + mAlphaEffect);
		}
	}

	private long	mAppearTimeLast	= 0;
	private float	mAppearV		= 0.04f;
	private float	mAlphaStand		= 0;
	private void doAppear()
	{
		if (mAppearTimeLast != 0)
		{
			long t = System.currentTimeMillis() - mAppearTimeLast;
			mAppearTimeLast = System.currentTimeMillis();
			mAlphaStand = mAlphaStand + mAppearV * t;
			mAlpha = (int) (mAlphaStand + 0.5f);
			if (mAlpha < 0)
			{
				mAlpha = 0;
			}
			if (mAlpha >= 255)
			{
				mAlpha = 255;
				mAlphaStand = mAlpha;
				mAppearTimeLast = 0;
			}
		}
	}
	public void activeAppear()
	{
		mDisappearTimeLast = 0;
		mAppearTimeLast = System.currentTimeMillis();
		mAlphaStand = mAlpha;
	}

	private long	mDisappearTimeLast	= 0;
	private float	mDisappearV			= 0.03f;
	private float	mDisalphaStand		= 0;
	private void doDisappear()
	{
		if (mDisappearTimeLast != 0)
		{
			long t = System.currentTimeMillis() - mDisappearTimeLast;
			mDisappearTimeLast = System.currentTimeMillis();
			mDisalphaStand = mDisalphaStand - mDisappearV * t;
			mAlpha = (int) (mDisalphaStand + 0.5f);
			if (mAlpha > 255)
			{
				mAlpha = 255;
			}
			if (mAlpha <= 0)
			{
				mAlpha = 0;
				mDisalphaStand = mAlpha;
				mDisappearTimeLast = 0;
				if (mIDeleteCell != null)
				{
					mIDeleteCell.delete(this);
				}
			}
		}
	}
	public void activeDisappear()
	{
		mAppearTimeLast = 0;
		mDisappearTimeLast = System.currentTimeMillis();
		mDisalphaStand = mAlpha;
	}

	public RectF getmRectF()
	{
		return mRectF;
	}
	public void setmRectF(RectF mRectF)
	{
		this.mRectF = mRectF;
		if (this.mRectF != null)
		{
			mWidthInit = this.mRectF.width();
			mHeightInit = this.mRectF.height();
		}
	}
	public int getmColor()
	{
		return mColor;
	}
	public void setmColor(int mColor)
	{
		this.mColor = mColor;
	}
	public int getmAlpha()
	{
		return mAlpha;
	}
	public void setmAlpha(int mAlpha)
	{
		this.mAlpha = mAlpha;
	}
	public boolean ismIsVisiable()
	{
		return mIsVisiable;
	}
	public void setmIsVisiable(boolean mIsVisiable)
	{
		this.mIsVisiable = mIsVisiable;
	}
	public long getmShakeTimeLast()
	{
		return mShakeTimeLast;
	}
	public long getmRandomTimeLast()
	{
		return mRandomTimeLast;
	}
	public long getmAppearTimeLast()
	{
		return mAppearTimeLast;
	}
	public long getmDisappearTimeLast()
	{
		return mDisappearTimeLast;
	}
	public int getmFlag()
	{
		return mFlag;
	}

	/**
	 * 
	 * @author liangdaijian
	 *
	 */
	interface IDeleteCell
	{
		public void delete(BgCircleCell cell);
	}
}
