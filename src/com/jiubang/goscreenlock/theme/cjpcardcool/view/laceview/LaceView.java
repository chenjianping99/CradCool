package com.jiubang.goscreenlock.theme.cjpcardcool.view.laceview;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;

import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ViewUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.LiveListener;

/**
 * 
 * @author liangdaijian
 * 
 */
public class LaceView extends View implements LiveListener {
	private Bitmap[] mRandomBitmaps;
	private List<AutoRandom> mRandomMap;
	private List<AutoRandom> mInvisibleRandomsMap;
	private Camera mCamera = new Camera();
	private Matrix mMatrix = new Matrix();
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG
			| Paint.FILTER_BITMAP_FLAG);
	private PorterDuffXfermode mPorterDuffXfermode = new PorterDuffXfermode(
			PorterDuff.Mode.SRC);
	private static final int MAX_AUTO_RANDOM_COUNT = 20;
	private Random mRandom = new Random();
	private int[] mColors;
	
	private static final int[] RES_ID = {/*R.drawable.light_spot*/};

	public LaceView(Context context, int[] mColors) {
		super(context);
		this.mColors = mColors;

		mRandomMap = new ArrayList<AutoRandom>();
		mInvisibleRandomsMap = new ArrayList<AutoRandom>();
		mPaint.setStyle(Style.FILL);
		mPaint.setXfermode(mPorterDuffXfermode);

		mRandomBitmaps = new Bitmap[RES_ID.length];
		for (int i = 0; i < RES_ID.length; i++) {
			mRandomBitmaps[i] = ViewUtils.getScaleBitmapWithIDByHeight(context, RES_ID[i]);
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {

	}

	private void doSahua() {
		List<AutoRandom> randoms = new ArrayList<AutoRandom>();
		for (int i = 0; i < MAX_AUTO_RANDOM_COUNT; i++) {
			int color = 0xffff0000;
			if (mColors == null) {
				color = mRandom.nextBoolean() ? 0xffff0000 : 0xff00ff00;
			} else {
				color = mColors[mRandom.nextInt(mColors.length)];
			}
			randoms.add(/*new AutoRandom(getResources(), mRandomBitmaps, null,
					mRandom.nextInt(Constant.sRealWidth), -mRandom
							.nextInt(Global.dip2px(5)), false, 0,
					Constant.sRealWidth, Constant.sRealHeight, color)*/
					new AutoRandom(getResources(), mRandomBitmaps, null, 
							mRandom.nextInt(Constant.sRealWidth), 
							ViewUtils.getPXByHeight(980)  + mRandom.nextInt(200),
							false, 0,
							Constant.sRealWidth, Constant.sRealHeight + ViewUtils.getPXByHeight(120), color));

		}
		mRandomMap.addAll(randoms);
		invalidate();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		try {
			for (AutoRandom random : mRandomMap) {
				mRandomMap.remove(random);
				random.cleanUp();
				random = null;
			}

			for (AutoRandom random : mInvisibleRandomsMap) {
				mInvisibleRandomsMap.remove(random);
				random.cleanUp();
				random = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		mRandomMap.clear();
		mInvisibleRandomsMap.clear();

		if (mRandomBitmaps != null) {
			for (Bitmap b : mRandomBitmaps) {
				if (b != null && !b.isRecycled()) {
					b.recycle();
				}
			}
			mRandomBitmaps = null;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mIsStart) {
			try {
				for (AutoRandom auto : mRandomMap) {
					mPaint.setColor(auto.getmColor());
					auto.doDraw(mCamera, mMatrix, canvas, mPaint);
					if (!auto.mAlive) {
						mInvisibleRandomsMap.add(auto);
					}
					auto.moving();
				}
	
				/*int count = 0;
				for (AutoRandom auto : mRandomMap) {
					if (auto.mY > (Constant.sRealHeight )) {
						count++;
					}
				}
				if (count >= mRandomMap.size()) {
					doSahua();
				}*/

			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (Constant.sIsScreenOn) {
				invalidate();
			}
		}
	}
	
	private boolean mIsStart = false;
	public void setLaceStart(boolean b) {
		mIsStart = b;
		for (AutoRandom random : mInvisibleRandomsMap)
		{
			mRandomMap.remove(random);
			random.cleanUp();
			random = null;
		}
		mInvisibleRandomsMap.clear();
		doSahua();
		setVisibility(VISIBLE);
		invalidate();
	}
	
}
