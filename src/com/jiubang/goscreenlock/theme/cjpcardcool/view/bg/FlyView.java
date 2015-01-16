package com.jiubang.goscreenlock.theme.cjpcardcool.view.bg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.FloatMath;
import android.view.View;

import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.LiveListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ViewUtils;


/**
 * 
 * @author chenjianping
 * 
 */
public class FlyView extends View implements LiveListener {

	public FlyView(Context context) {
		super(context);
		
		init(context);
	}

	private static final int [] RES_ID = {/*R.drawable.butterfly2, R.drawable.butterfly2_2, 
		R.drawable.butterfly1, R.drawable.butterfly1_2, 
		R.drawable.butterfly3, R.drawable.butterfly3_2, 
		R.drawable.butterfly4, R.drawable.butterfly4_2, */};
	
	private void init(Context context) {
		mButterflyBitmaps = new Bitmap[RES_ID.length];
		mButterflyX = new float[RES_ID.length / 2];
		mButterflyY = new float[RES_ID.length / 2];
		for (int i = 0; i < RES_ID.length; i++) {
			mButterflyBitmaps[i] = ViewUtils.getScaleBitmapWithIDByWidth(context, RES_ID[i]);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (Constant.sIsScreenOn) {
			drawCanvas(canvas);
			invalidate();
		} 
		super.onDraw(canvas);
	}

	private void drawCanvas(Canvas canvas) {
		for (int i = 0; i < mButterflyBitmaps.length / 2; i++) {
			drawButterfly(canvas, i);
		}
		
	}
	
	private Bitmap[] mButterflyBitmaps;
	private float[] mButterflyX, mButterflyY;
	private final static int[][] POS = {{ViewUtils.getPXByWidth(100), ViewUtils.getPXByHeight(600)},
		{ViewUtils.getPXByWidth(450), ViewUtils.getPXByHeight(450)},
		{ViewUtils.getPXByWidth(500), ViewUtils.getPXByHeight(800)},
		{ViewUtils.getPXByWidth(360), ViewUtils.getPXByHeight(1000)}};
	private final static int RADUIS = ViewUtils.getPXByWidth(80);
	private void drawButterfly(Canvas canvas, int i) {
		
		long timeX = (System.currentTimeMillis() / (20 + i * 2)) % 3600;
		timeX = fitTimeSin(timeX);
		mButterflyX[i] = POS[i][0] 
				+ RADUIS
				* FloatMath.sin((float) Math.toRadians(timeX));
		
		long timeY = System.currentTimeMillis() / (40 + i * 2) % 3600;
		timeY = fitTimeCos(timeY);
		mButterflyY[i] = POS[i][1]
				+ RADUIS
				* FloatMath.cos((float) Math.toRadians(timeY));
		
		canvas.drawBitmap(
				mButterflyBitmaps[i * 2 + (int) (System.currentTimeMillis() / 100 % 2)],
				mButterflyX[i], mButterflyY[i], null);
	}

	private long fitTimeSin(long time) {
		if (time > 90 && time < 450) {
			time = 90;
		} else if (time > 630 && time < 990) {
			time = 630;
		}
		return time;
	}

	private long fitTimeCos(long time) {
		if (time > 180 && time < 540) {
			time = 180;
		} else if (time > 720 && time < 1080) {
			time = 720;
		}
		return time;
	}
	
	/*
    protected void drawBg(Canvas canvas)  
    {  
    	if (mBackgroundBitmap == null) {
			mBackgroundBitmap = BitmapManager.getInstance(mContext).getBitmap(
					mContext.getResources(), R.drawable.bg,
					Constant.sRealWidth, Constant.sRealHeight);
		}
        //对bitmap按verts数组进行扭曲  
        //从第一个点(由第5个参数0控制)开始扭曲  
        canvas.drawBitmapMesh(mBackgroundBitmap, 20, 20, BackgroundView.verts, 0, null, 0, null);  
    }  
    

	private void addGUI(float x, float y) {
		if (System.currentTimeMillis() % 5 == 0) {
			DotUtil.addDot(x, y);
		}
	}

	private String mPreviewTipsString = null;
	public boolean mIsPreview = false;

	private void drawPreviewTips(Canvas canvas) {
		if (!mIsPreview) {
			return;
		}
		if (mPreviewTipsString == null) {
			mPreviewTipsString = mContext.getResources().getString(
					R.string.theme_preview_tips);
		}
		mTextPaint.setAlpha((int) (127 + 127 * FloatMath.sin((float) Math
				.toRadians(System.currentTimeMillis() / 5 % 360))));
		canvas.drawText(mPreviewTipsString, Constant.sRealWidth / 2,
				Constant.sRealHeight / 2, mTextPaint);
	}

	private void drawTrace(Canvas canvas) {
		DotUtil.draw(canvas);
	}

	public int mWeatherType = 0;

	private void drawWeather(Canvas canvas) {
		switch (WeatherConstants.WEATHER_TYPE_RAINY) {
		case WeatherConstants.WEATHER_TYPE_CLOUDY:
		case WeatherConstants.WEATHER_TYPE_FOG:
		case WeatherConstants.WEATHER_TYPE_OVERCAST:
			drawCloud(canvas);
			break;
		case WeatherConstants.WEATHER_TYPE_SUNNY:
			drawSunny(canvas);
			break;
		case WeatherConstants.WEATHER_TYPE_SNOWY:
			drawSnowy(canvas);
			break;
		case WeatherConstants.WEATHER_TYPE_RAINY:
		case WeatherConstants.WEATHER_TYPE_THUNDERSTORM:
			drawRainy(canvas);
			break;
		default:
			break;
		}
	}

	private float mRaindSize = 20 * Constant.sScaleX;
	private float mAngle = (float) Math.toRadians(20);
	private float mLen = 0;
	private int mRainCount = 50;
	private float mSin = FloatMath.sin(mAngle);
	private float mCos = FloatMath.cos(mAngle);
	private final LinearGradient mGradient = new LinearGradient(0, 0, 0,
			2 * mRaindSize, new int[] { 0x00FFFFFF,
					Color.argb(127, 255, 255, 255),
					Color.argb(200, 255, 255, 255) }, new float[] { 0, 0.5f,
					1.0f }, TileMode.CLAMP);
	private static final Matrix MATRIX = new Matrix();

	private void drawRainy(Canvas canvas) {
		int sx = 0, sy = 0, ex = 0, ey = 0;
		int alpha = 0;
		float k = 0;
		for (int i = 0; i < mRainCount; i++) {
			sx = mRandom.nextInt(Constant.sRealWidth);
			sy = mRandom.nextInt(Constant.sRealHeight);
			mLen = mRandom.nextInt((int) mRaindSize) + mRaindSize;
			k = (mLen + mRaindSize) / (3 * mRaindSize);
			k = k > 1 ? 1 : k;
			mPaint.setStrokeWidth(k);
			ex = (int) (sx - mSin * mLen);
			ey = (int) (sy + mCos * mLen);
			alpha = (int) (k * 180);
			MATRIX.setTranslate(sx, sy);
			mGradient.setLocalMatrix(MATRIX);
			mPaint.setShader(mGradient);
			canvas.drawLine(sx, sy, ex, ey, mPaint);
			mPaint.setShader(null);
		}
	}

	private List<Flake> mFlakes = new ArrayList<Flake>();
	private boolean misInitFlake = false;
	private static final int MAX_FLAKE = 40; // 绘制的雪花数量

	private void drawSnowy(Canvas canvas) {
		if (!misInitFlake) {
			for (int i = 0; i < MAX_FLAKE; i++) {
				Flake flake = new Flake(mRandom, mContext.getResources(),
						canvas);
				mFlakes.add(flake);
			}
			misInitFlake = true;
		}
		drawFlakes(canvas);
	}

	private void drawFlakes(Canvas canvas) {
		if (mFlakes == null || mRandom == null) {
			return;
		}
		for (Flake flake : mFlakes) {
			flake.updateFlake(mRandom, canvas);
			flake.drawFlake(canvas);
		}
	}

	private void drawSunny(Canvas canvas) {
		if (mSunshineBitmap == null) {
			mSunshineBitmap = BitmapManager.getInstance(mContext).getBitmap(
					mContext.getResources(),
					R.drawable.zonelimbo_weather_sunshine, Constant.sScaleX);
		}
		float angle = 10 * (0 + FloatMath
				.sin((float) Math.toRadians((float) (System.currentTimeMillis() / 20 % 3600) / 10)));
		setMatricY(angle);
		canvas.save();
		canvas.concat(mMatrix);
		canvas.drawBitmap(mSunshineBitmap, Constant.sRealWidth
				- mSunshineBitmap.getWidth(), 0, null);
		canvas.restore();
	}

	private void drawBackground(Canvas canvas) {
		if (mBackgroundBitmap == null) {
			mBackgroundBitmap = BitmapManager.getInstance(mContext).getBitmap(
					mContext.getResources(), R.drawable.bg,
					Constant.sRealWidth, Constant.sRealHeight);
		}
		canvas.drawBitmapMesh(
				mBackgroundBitmap,
				2,
				2,
				getMesh(new float[] {
						0f,
						0f,
						0.5f,
						0f,
						1f,
						0f,
						0f,
						0.5f,
						0.5f + 0.05f * FloatMath.sin((float) Math
								.toRadians((float) (System.currentTimeMillis() / 20 % 3600) / 2)),
						0.5f, 1f, 0.5f, 0f, 1f, 0.5f, 1f, 1f, 1f }), 0, null,
				0, mPaint);
	}

	private float[] getMesh(float[] verts) {
		if (verts != null) {
			for (int i = 0; i < verts.length; ++i) {
				if (i % 2 == 0) {
					verts[i] *= Constant.sRealWidth;
				} else {
					verts[i] *= Constant.sRealHeight;
				}
			}
		}
		return verts;
	}

	private void drawCloud(Canvas canvas) {
		if (mCloudBitmaps == null) {
			mCloudBitmaps = new Bitmap[] {
					BitmapManager.getInstance(mContext).getBitmap(
							mContext.getResources(),
							R.drawable.zonelimbo_weather_cloudy_0,
							Constant.sScaleX),
					BitmapManager.getInstance(mContext).getBitmap(
							mContext.getResources(),
							R.drawable.zonelimbo_weather_cloudy_1,
							Constant.sScaleX),
					BitmapManager.getInstance(mContext).getBitmap(
							mContext.getResources(),
							R.drawable.zonelimbo_weather_cloudy_2,
							Constant.sScaleX) };
			mCloudPoints = new Point[mCloudBitmaps.length];
			mCloundStartTimes = new long[mCloudBitmaps.length];
			for (int i = 0; i < mCloudPoints.length; ++i) {
				mCloudPoints[i] = new Point();
				initRandomPoint(i);
			}
		}
		for (int i = 0; i < mCloudBitmaps.length; ++i) {
			float scale = (float) (System.currentTimeMillis() - mCloundStartTimes[i])
					/ (ANIMATION_TIME * mCloudBitmaps[i].getWidth() / mCloudBitmaps[0]
							.getWidth());
			canvas.save();
			canvas.drawBitmap(mCloudBitmaps[i], mCloudPoints[i].x - scale
					* Constant.sRealWidth, mCloudPoints[i].y, mPaint);
			canvas.restore();
			if (scale >= 2) {
				getRandomPoint(i);
			}
		}
	}

	private void getRandomPoint(int i) {
		mCloudPoints[i].x = Constant.sRealWidth
				+ mRandom.nextInt(Constant.sRealWidth / 2);
		mCloudPoints[i].y = mRandom.nextInt(Constant.sRealHeight / 3);
		mCloundStartTimes[i] = System.currentTimeMillis();
	}

	private void initRandomPoint(int i) {
		mCloudPoints[i].x = Constant.sRealWidth
				+ mRandom.nextInt(Constant.sRealWidth);
		mCloudPoints[i].y = mRandom.nextInt(Constant.sRealHeight / 3);
		mCloundStartTimes[i] = System.currentTimeMillis();
	}

	private void drawGirl(Canvas canvas) {
		if (mGirlBitmaps == null) {
			mGirlBitmaps = new Bitmap[] {
					BitmapManager.getInstance(mContext).getBitmap(
							mContext.getResources(),
							R.drawable.zonelimbo_girl_0, Constant.sScaleX),
					BitmapManager.getInstance(mContext).getBitmap(
							mContext.getResources(),
							R.drawable.zonelimbo_girl_1, Constant.sScaleX),
					BitmapManager.getInstance(mContext).getBitmap(
							mContext.getResources(),
							R.drawable.zonelimbo_girl_2, Constant.sScaleX),
					BitmapManager.getInstance(mContext).getBitmap(
							mContext.getResources(),
							R.drawable.zonelimbo_girl_3, Constant.sScaleX),
					BitmapManager.getInstance(mContext).getBitmap(
							mContext.getResources(),
							R.drawable.zonelimbo_girl_4, Constant.sScaleX),
					BitmapManager.getInstance(mContext).getBitmap(
							mContext.getResources(),
							R.drawable.zonelimbo_girl_5, Constant.sScaleX), };
		}
		int girlIndex = (int) (System.currentTimeMillis() / 200 % (2 * mGirlBitmaps.length));
		if (girlIndex == mGirlBitmaps.length) {
			girlIndex = 0;
		} else if (girlIndex == mGirlBitmaps.length + 1) {
			girlIndex = 1;
		} else if (girlIndex == mGirlBitmaps.length + 2) {
			girlIndex = 0;
		} else if (girlIndex == mGirlBitmaps.length + 3) {
			girlIndex = 1;
		}
		girlIndex = girlIndex > mGirlBitmaps.length - 1 ? mGirlBitmaps.length - 1
				: girlIndex;
		canvas.drawBitmap(mGirlBitmaps[girlIndex], Constant.sRealWidth * 0.25f,
				Constant.sRealHeight * 0.92f - mGirlBitmaps[0].getHeight(),
				null);
	}

	private void drawBoy(Canvas canvas) {
		if (mBoyBitmap == null) {
			mBoyBitmap = BitmapManager.getInstance(mContext).getBitmap(
					mContext.getResources(), R.drawable.zonelimbo_boy,
					Constant.sScaleX);
		}
		canvas.save();
		canvas.rotate(5 * FloatMath.sin((float) Math.toRadians(System
				.currentTimeMillis() / 23 % 360)), Constant.sRealWidth
				- mBoyBitmap.getWidth() * 0.4f, 0);
		canvas.drawBitmap(mBoyBitmap,
				Constant.sRealWidth - mBoyBitmap.getWidth(), 0, null);
		canvas.restore();
	}

	private void drawBird(Canvas canvas) {
		if (mBirdBitmap == null) {
			mBirdBitmap = BitmapManager.getInstance(mContext).getBitmap(
					mContext.getResources(), R.drawable.zonelimbo_bird,
					Constant.sScaleX);
		}
		canvas.save();
		canvas.rotate(20 * FloatMath.sin((float) Math.toRadians(System
				.currentTimeMillis() / 13 % 360)), Constant.sRealWidth / 2, 0);
		canvas.drawBitmap(mBirdBitmap,
				(Constant.sRealWidth - mBirdBitmap.getWidth()) / 2, 0, null);
		canvas.restore();
	}

	private Camera mCamera = new Camera();
	private Matrix mMatrix = new Matrix();

	private void setMatricY(float angle) {
		mMatrix.reset();
		mCamera.save();
		mCamera.translate(0, 0, 0);
		mCamera.rotateX(angle);
		mCamera.rotateY(angle);
		mCamera.getMatrix(mMatrix);
		mCamera.restore();

		mMatrix.preTranslate(-Constant.sRealWidth, 0);
		mMatrix.postTranslate(Constant.sRealWidth, 0);
	} */

	@Override
	public void onResume() {
		invalidate();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {

		for (int i = 0; i < mButterflyBitmaps.length; i++) {
			ViewUtils.recycleBitmap(mButterflyBitmaps[i]);
		}
		mButterflyBitmaps = null;
	}

}
