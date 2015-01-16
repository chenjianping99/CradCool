package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.media.AudioManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jiubang.goscreenlock.theme.cjpcardcool.R;
import com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager.JazzyViewPager;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnDestroyListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnResumeListener;

/**
 * @author chenjianping
 */
public class MediaVolumeView extends FrameLayout implements 
	OnResumeListener, OnDestroyListener 
{
	private AudioManager mAudioManager;
	private int mMaxVolume, mCurrentVolume;
	
	private ImageView mVolumeView;
	private Paint mPaint = new Paint();
	private RectF mRectF1 = new RectF();
	//private int mPaddingVolumeHalf = ViewUtils.getPXByWidth(5); // Volume圆点的一半
	private int mNowX = 0;
	private float mVolumeX = 0;
	private JazzyViewPager mViewPager;
	public MediaVolumeView(Context context, JazzyViewPager v) {
		super(context);
		mViewPager = v;
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		initDatas();
		initVoiceSlide();
	}

	private int mVolumeBgHeight = ViewUtils.getPXByWidth(37); //背景高度==滑快高度
	private int mVolumeHeight = ViewUtils.getPXByHeight(10); //音量条高度
	private int mVolumeBgWidth, mVolumeWidth;
	private Bitmap mThumbBitmap/*, mThumbLineBg, mThumbLineVoice*/;
	private void initDatas() {
		mMaxVolume = mAudioManager .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mVolumeWidth = ViewUtils.getPXByWidth(325);  //音量条长度
		mVolumeBgWidth = mVolumeWidth + mVolumeBgHeight; //无背景
		mThumbBitmap = ViewUtils.getScaleBitmapWithIDByHeight(getContext(), R.drawable.music_thumb);
		/*mThumbLineBg = ViewUtils.getScaleBitmapWithIDByWidth(getContext(), R.drawable.music_sound_bg);
		mThumbLineVoice = ViewUtils.getScaleBitmapWithIDByWidth(getContext(), R.drawable.music_thumb_line);*/
		
		mPaint.setStyle(Style.FILL);
		mPaint.setAntiAlias(true);

		mRectF1.set(mVolumeBgHeight / 2, (mVolumeBgHeight  - mVolumeHeight) / 2, 
				mVolumeWidth + mVolumeBgHeight / 2, (mVolumeBgHeight + mVolumeHeight) / 2);
	}
	
	@Override
	public void onResume() {
		getVoice();
		mVolumeView.invalidate();
	}
	
	private void initVoiceSlide() {
		ImageView mVoiceLeftIcon = new ImageView(getContext());
		mVoiceLeftIcon.setImageResource(R.drawable.music_sound);
		LayoutParams voiceMinParams = new LayoutParams(ViewUtils.getPXByWidth(17), 
				ViewUtils.getPXByWidth(37), Gravity.CENTER_VERTICAL | Gravity.LEFT);
		addView(mVoiceLeftIcon, voiceMinParams);
		
		/*ImageView mVoiceRightIcon = new ImageView(getContext());
		//mVoiceRightIcon.setImageResource(R.drawable.music_sound_right);
		LayoutParams voiceMaxParams = new LayoutParams(ViewUtils.getPXByWidth(22), 
				ViewUtils.getPXByWidth(18), Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		addView(mVoiceRightIcon, voiceMaxParams);*/
		
		mVolumeView = new ImageView(getContext()) {
			private RectF mRectF2 = new RectF(mRectF1);
			@Override
			protected void onDraw(Canvas canvas) {
				super.onDraw(canvas);
				// 画音量条背景， 不动
				mPaint.setColor(0xff657291);
				canvas.drawRoundRect(mRectF1, mVolumeHeight / 2,
						mVolumeHeight / 2, mPaint);
				mPaint.setColor(DateTimeView.TIME_COLOR);
				mRectF2.right = mVolumeX + mVolumeBgHeight / 2;
				canvas.drawRoundRect(mRectF2, mVolumeHeight / 2,
						mVolumeHeight / 2, mPaint);
				//canvas.drawCircle(mVolumeX, getHeight() / 2, mVolumeBgHeight / 2, mPaint);
				canvas.drawBitmap(mThumbBitmap, mVolumeX,
						(getHeight() - mThumbBitmap.getHeight()) / 2 /*+ ViewUtils.getPXByHeight(4)*/, mPaint);
				/*canvas.drawBitmap(mThumbLineBg, null,
						mRectF1, null);
				mRectF2.right = mVolumeX + mThumbBitmap.getWidth() / 2;
				canvas.save();
				canvas.clipRect(mRectF2);
				canvas.drawBitmap(mThumbLineVoice, null,
						mRectF1, null);
				canvas.restore();
				canvas.drawBitmap(mThumbBitmap, mVolumeX,
						(getHeight() - mThumbBitmap.getHeight()) / 2, null);*/
				if (Constant.sIsScreenOn) {
					getVoice();
					invalidate();
				}
			}

			@Override
			public boolean onTouchEvent(MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					if (mViewPager != null) {
						mViewPager.requestDisallowInterceptTouchEvent(false);
					}
					break;
				default:
					if (mViewPager != null) {
						mViewPager.requestDisallowInterceptTouchEvent(true);
					}
					break;
				}
				mNowX = (int) event.getX();
				fixNowX();
				setVoice(mNowX * 1.0f / mVolumeWidth);
				return true;
			}

			private void fixNowX() {
				mNowX = mNowX <  mVolumeBgHeight / 2 ? 0 : mNowX;
				mNowX = mNowX > mVolumeWidth ? mVolumeWidth : mNowX;
			}
		};
		
		mVolumeView.setWillNotDraw(false);
		//mVolumeView.setBackgroundColor(0x33ffffff);
		LayoutParams volumeBgLP = new LayoutParams(mVolumeBgWidth,
				mVolumeBgHeight, Gravity.LEFT | Gravity.CENTER_VERTICAL);
		volumeBgLP.leftMargin = ViewUtils.getPXByWidth(20);
		addView(mVolumeView, volumeBgLP);
	}
	
	private void getVoice() {
		mCurrentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		/*int paddingL = ViewUtils.getPXByWidth(15);
		int paddingR = ViewUtils.getPXByWidth(10);*/
		
		mVolumeX = mVolumeWidth * mCurrentVolume / mMaxVolume;
		/*if (mVolumeX > mVolumeWidth - paddingR) {
			mVolumeX = mVolumeWidth - paddingR;
		}*/
		//LogUtils.log(null, "mVolumeX = " + mVolumeX);
	}

	private void setVoice(float rate) {
		LogUtils.log(null, "rate = " + rate);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (rate
				* mMaxVolume + 0.5f), 0);
	}

	@Override
	public void onDestroy()
	{
		ViewUtils.recycleBitmap(mThumbBitmap);
		/*ViewUtils.recycleBitmap(mThumbLineBg);
		ViewUtils.recycleBitmap(mThumbLineVoice);*/
	}

}

