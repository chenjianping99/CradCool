/**
 * 
 */
package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.R;
import com.jiubang.goscreenlock.theme.cjpcardcool.JazzyViewPager.JazzyViewPager;
import com.jiubang.goscreenlock.theme.cjpcardcool.music.MusicControlCenter;
import com.jiubang.goscreenlock.theme.cjpcardcool.music.MusicInfoBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.music.MusicInfoListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.LiveListener;

/**
 * @author chenjianping
 *
 */
public class PlayLinearView extends FrameLayout implements MusicInfoListener, 
OnClickListener, LiveListener
{
	private Handler mHandler;
	private boolean mIsPlaying = false, mIsGettingState;
	private MusicControlCenter mMusicCenter;
	private GetPlayingStateRunnable mGetstateRunnable;
	public PlayLinearView(Context context, JazzyViewPager v) {
		super(context);
		/*LayoutParams musicBgParams = new LayoutParams(mWidth, ViewUtils.getPXByHeight(100), 
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		musicBgParams.bottomMargin = ViewUtils.getPXByHeight(220);
		setLayoutParams(musicBgParams);*/
		//setBackgroundResource(R.drawable.music_bg);
		
		addPlayer(context);
		addMediaVolumeView(context, v);
		addCdViews(context);
		
	}
	
	/*private ImageView mMusicBg, mChangeIcon;
	private void addBg(Context context) {
		mMusicBg = new ImageView(context);
		//mMusicBg.setBackgroundResource(R.drawable.music_bg);
		LayoutParams musicBgParams = new LayoutParams(mWidth, ViewUtils.getPXByHeight(578), Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		musicBgParams.topMargin = 2 * WeatherView.sPadding;
		addView(mMusicBg, musicBgParams);
	}*/
	
	/*private MarqueeTextView mSongName;
	private ImageView mMusicIcon;
	private void addSongName(Context context) {
		mMusicIcon = new ImageView(getContext());
		//mMusicIcon.setImageResource(MUSIC_ICON[Constant.sBgIndex]);
		int w = ViewUtils.getPXByWidth(16);
		int h = ViewUtils.getPXByWidth(26);
		LayoutParams bgParams = new LayoutParams(w, h, Gravity.CENTER_VERTICAL | Gravity.LEFT);
		bgParams.leftMargin = ViewUtils.getPXByWidth(30);
		bgParams.bottomMargin = ViewUtils.getPXByHeight(100);
		addView(mMusicIcon, bgParams);
		
		mSongName = new MarqueeTextView(context);
		int width = ViewUtils.getPXByWidth(260);
		mSongName.setMaxWidth(width);
		//mSongName.setTextColor(DateTimeView.TIME_COLOR[Constant.sBgIndex]);
		//mSongName.setBackgroundColor(0x44ffffff);
		mSongName.setText("Song Name -- Player");
		mSongName.setTypeface(CircleContainer.sTypeface);
		mSongName.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByWidth(30));		
		LayoutParams paramSong = new LayoutParams(width, 
				LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.LEFT);
		paramSong.leftMargin = ViewUtils.getPXByWidth(60);
		paramSong.bottomMargin = bgParams.bottomMargin;
		addView(mSongName, paramSong);
		updateSongName();
	}*/
		
	private MediaVolumeView mMediaVolumeView;
	private void addMediaVolumeView(Context context, JazzyViewPager pager)
	{
		mMediaVolumeView = new MediaVolumeView(context, pager);
		LayoutParams param = new LayoutParams(ViewUtils.getPXByWidth(380),
				LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.RIGHT);
		param.bottomMargin = ViewUtils.getPXByHeight(15);
		param.rightMargin = ViewUtils.getPXByHeight(25);
		addView(mMediaVolumeView, param);
		//mMediaVolumeView.setBackgroundColor(0x33ffffff);
	}
	
	private View mCd, mNeddle;
	private void addCdViews(Context context) {
		View mBgCircle = new View(context);
		int wcircle = ViewUtils.getPXByHeight(202);
		LayoutParams mBgCircleParams = new LayoutParams(wcircle, wcircle,
				Gravity.CENTER_VERTICAL | Gravity.LEFT);
		mBgCircleParams.leftMargin = ViewUtils.getPXByHeight(20);
		mBgCircle.setBackgroundResource(R.drawable.music_cd_bg);
		addView(mBgCircle, mBgCircleParams);
		
		mCd = new View(context);
		//int wmCd = ViewUtils.getPXByHeight(346);
		LayoutParams mCdParams = new LayoutParams(wcircle, wcircle,
				Gravity.CENTER_VERTICAL | Gravity.LEFT);
		mCdParams.leftMargin = mBgCircleParams.leftMargin;
		mCd.setBackgroundResource(R.drawable.music_cd);
		addView(mCd, mCdParams);

		View mBgCover = new View(context);
		LayoutParams mBgCoverParams = new LayoutParams(wcircle, wcircle,
				Gravity.CENTER_VERTICAL | Gravity.LEFT);
		mBgCoverParams.leftMargin = mBgCircleParams.leftMargin;
		mBgCover.setBackgroundResource(R.drawable.music_cd_cover);
		addView(mBgCover, mBgCoverParams);
		
		mNeddle = new View(context);
		LayoutParams mNeddleParams = new LayoutParams(ViewUtils.getPXByHeight(87), 
				ViewUtils.getPXByHeight(155),
				Gravity.TOP | Gravity.LEFT);
		mNeddleParams.topMargin = ViewUtils.getPXByHeight(10);
		mNeddleParams.leftMargin = ViewUtils.getPXByHeight(170);
		mNeddle.setBackgroundResource(R.drawable.music_needle);
		addView(mNeddle, mNeddleParams);
	}

	private ImageView mPlay, mBack, mFont;
	private void addPlayer(Context context) {
		LinearLayout.LayoutParams mPlayLP, mBackLP, mFontLP;
		// 初始化时拿一下是否处于播放状态
		mHandler = new Handler();
		initMusicControCenter();
		mIsPlaying = getSystemPlayStatus();

		mBack = new ImageView(context);
		mBack.setImageResource(R.drawable.music_pre);
		int mBackW = ViewUtils.getPXByWidth(103);
		int mBackH = ViewUtils.getPXByWidth(103);
		mBackLP = new LinearLayout.LayoutParams(mBackW, mBackH);
		mBackLP.gravity = Gravity.CENTER_VERTICAL;
		
		mPlay = new ImageView(context);
		mPlay.setImageResource(mIsPlaying ? R.drawable.music_pause : R.drawable.music_play);
		int mPlayW = ViewUtils.getPXByWidth(103);
		int mPlayH = ViewUtils.getPXByWidth(103);
		mPlayLP = new LinearLayout.LayoutParams(mPlayW, mPlayH);
		mPlayLP.gravity = Gravity.CENTER_VERTICAL;
		mPlayLP.leftMargin = ViewUtils.getPXByWidth(50);
		mPlayLP.rightMargin = mPlayLP.leftMargin;
		
		mFont = new ImageView(context);
		mFont.setImageResource(R.drawable.music_next);
		mFontLP = new LinearLayout.LayoutParams(mBackW, mBackH);
		mFontLP.gravity = Gravity.CENTER_VERTICAL;
		
		LinearLayout linerLy = new LinearLayout(context);
		linerLy.setOrientation(LinearLayout.HORIZONTAL);
		linerLy.setGravity(Gravity.CENTER_HORIZONTAL);
		linerLy.addView(mBack, mBackLP);
		linerLy.addView(mPlay, mPlayLP);
		linerLy.addView(mFont, mFontLP);

		LayoutParams mLpParam = new LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.RIGHT);
		mLpParam.topMargin = ViewUtils.getPXByHeight(25);
		mLpParam.rightMargin = ViewUtils.getPXByHeight(20);

		addView(linerLy, mLpParam);
		mBack.setOnClickListener(this);
		mPlay.setOnClickListener(this);
		mFont.setOnClickListener(this);
	}
	
	private void initMusicControCenter() {
		mMusicCenter = new MusicControlCenter();
		mMusicCenter.resignReceiver(getContext());
		mMusicCenter.setMusicInfoUpdateListener(this);
	}
	
	private void unRegist() {
		if (mMusicCenter != null) {
			mMusicCenter.unregisterReceiver(getContext());
		}
	}

	/**
	 * 获取系统播放状态
	 * @return
	 */
	public boolean getSystemPlayStatus() {
		return MusicControlCenter.isMusicPlaying(getContext());
	}
	@Override
	public void notifyMusicInfoChanged(MusicInfoBean musicInfoBean) {
			//updateSongName();
		adjustPlayingState(0);
	}
	
	/**
	 * 播放/暂停功能
	 * @return
	 */
	public void play() {
		mIsPlaying = getSystemPlayStatus();
		if (mIsPlaying) {
			MusicControlCenter.pause(getContext());
			mPlay.setImageResource(R.drawable.music_play);
			/*stopNeddleRotate(mNeddle);
			mCd.clearAnimation();*/
		} else {
			MusicControlCenter.play(getContext());
			mPlay.setImageResource(R.drawable.music_pause);
			/*startNeddleRotate(mNeddle);*/
		}
		adjustPlayingState(2000);
	}

	/**
	 * 下一首
	 */
	public void next() {
		MusicControlCenter.next(getContext());
		adjustPlayingState(4000);
	}

	/**
	 * 前一首
	 * 
	 * @return
	 */
	public void previous() {
		MusicControlCenter.previous(getContext());
		adjustPlayingState(4000);
	}
	/**
	 * 调整音乐控件播放状态
	 * 
	 * @param delayTime
	 *            延时多少时间调整
	 */
	private void adjustPlayingState(int delayTime) {
		if (!mIsGettingState) {
			mIsGettingState = true;
			if (null == mGetstateRunnable) {
				mGetstateRunnable = new GetPlayingStateRunnable();
			}
			mHandler.postDelayed(mGetstateRunnable, delayTime);
		}
	}
	public void updatePlayInfo() {
		mIsPlaying = getSystemPlayStatus();
		mPlay.setImageResource(mIsPlaying ? R.drawable.music_pause : R.drawable.music_play);
		//updateSongName();
		if (mIsPlaying && !mIsAnimation) {
			startNeddleRotate(mNeddle);
		} else {
			if (mIsAnimation) {
				stopNeddleRotate(mNeddle);
				mCd.clearAnimation();
			}
		}
	}
	
	/*private void updateSongName() {
		try
		{
			if (mMusicCenter != null && mMusicCenter.getCurMusicInfo() != null &&
					mMusicCenter.getCurMusicInfo().getTrack() != null) 
			{
				String mSongNameStr = mMusicCenter.getCurMusicInfo().getTrack();
				if (mMusicCenter.getCurMusicInfo().getArtiSt() != null &&
						mMusicCenter.getCurMusicInfo().getArtiSt().length() > 0) {
					mSongNameStr = mSongNameStr + "-" + mMusicCenter.getCurMusicInfo().getArtiSt();
				}
				LogUtils.log(null, mSongNameStr);
				if (mSongName != null) {
					mSongName.setText(mSongNameStr);
				}
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}*/
	
	/**
	 * @author liangdaijian
	 */
	class GetPlayingStateRunnable implements Runnable {
		@Override
		public void run() {
			updatePlayInfo();
			mIsGettingState = false;
		}
	}
	@Override
	public void onClick(View v) {
		if (v == mBack) {
			previous();
		} else if (v == mPlay) {
			play();
		} else if (v == mFont) {
			next();
		} /*else if (v == mChangeIcon) {
			Intent intent = new Intent("com.jiubang.goscreenlock.theme.cjpcardcool.GetPictureActivity");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(GetPictureActivity.INDEX, GetPictureActivity.MUSIC_INDEX);
			Global.sendUnlockWithIntent(getContext(), null, null, null, intent);
			ThemeSetProvider.setBackgroundIndex(getContext(), 1);
		}*/
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onResume() {
		
		if (mMediaVolumeView != null) {
			mMediaVolumeView.onResume();
		}
		/*if (mSongName != null) {
			mSongName.onResume();
		}*/

		if (mIsPlaying) {
			startNeddleRotate(mNeddle);
		} else {
			mCd.clearAnimation();
			mNeddle.clearAnimation();
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		/*if (mSongName != null) {
			mSongName.onPause();
		}*/
		mCd.clearAnimation();
	}

	@Override
	public void onDestroy() {
		if (mMediaVolumeView != null) {
			mMediaVolumeView.onDestroy();
		}
		
		unRegist();
		removeAllViews();
	}
	
	/**
	 * @author chenjianping
	 *
	 */
	/*class CenterUnit extends View implements OnResumeListener,
		OnDestroyListener 
	{
		private Bitmap mWave1, mWave2;
		private RectF mWave1RectF, mWave2RectF;
		private Paint mPaint;
		private float mWaveX = 0, mWave2X = 0;
		private Interpolator mInterpolator = new LinearInterpolator();
		private long mAnimationStartTime = 0, mStartTime2 = 0;
		
		private int mWidth = ViewUtils.getPXByHeight(428);
		private RectF mBgRectF;
		private Bitmap mBg, mMask;
		String mSongName = "";
		
		PorterDuffXfermode mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
		public CenterUnit(Context context)
		{
			super(context);
			mBg = ViewUtils.getBitmapWidthId(context, R.drawable.music_bg);
			mMask = ViewUtils.getBitmapWidthId(context, R.drawable.music_mask);
			mWave1 = ViewUtils.getBitmapWidthId(context, R.drawable.music_line1);
			mWave2 = ViewUtils.getBitmapWidthId(context, R.drawable.music_line2);
			
			mBgRectF = new RectF();
			mWave1RectF = new RectF();
			mWave2RectF = new RectF();

			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG); // 抗锯齿
			mPaint.setColor(0xff00ffa2);
			mPaint.setTextAlign(Align.CENTER);
			mPaint.setTextSize(ViewUtils.getPXByHeight(24));
			mPaint.setTypeface(CircleContainer.sTypeface);
		}

		@Override
		protected void onDraw(Canvas canvas) 
		{	
			mBgRectF.set(0, 0, mWidth, mWidth);
			canvas.drawBitmap(mBg, null, mBgRectF, mPaint);

			int sc = canvas.saveLayer(mBgRectF, null, Canvas.MATRIX_SAVE_FLAG
					| Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
					| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
					| Canvas.CLIP_TO_LAYER_SAVE_FLAG);
			
			float wave2Width = mWave2.getWidth() * ViewUtils.sScaleH;
			float wave2Height = mWave2.getHeight() * ViewUtils.sScaleH;
			mWave2RectF.set(0 - wave2Width + mWave2X, (mWidth - wave2Height) / 2,
					0 + mWave2X, wave2Height + (mWidth - wave2Height) / 2);
			
			float waveWidth = mWave1.getWidth() * ViewUtils.sScaleH;
			float waveHeight = mWave1.getHeight() * ViewUtils.sScaleH;
			mWave1RectF.set(0 - waveWidth + mWaveX, (mWidth - waveHeight) / 2,
					0 + mWaveX, waveHeight + (mWidth - waveHeight) / 2);
			
			if (mIsPlaying && Constant.sIsScreenOn) {
				canvas.drawBitmap(mWave1, null, mWave1RectF, mPaint);
				mWave1RectF.offset(waveWidth, 0);
				canvas.drawBitmap(mWave1, null, mWave1RectF, mPaint);
				mWave1RectF.offset(-waveWidth, 0);
				
				canvas.drawBitmap(mWave2, null, mWave2RectF, mPaint);
				mWave2RectF.offset(wave2Width, 0);
				canvas.drawBitmap(mWave2, null, mWave2RectF, mPaint);
				mWave1RectF.offset(-wave2Width, 0);
			}
			
			if (mMusicCenter != null && mMusicCenter.getCurMusicInfo() != null) {
				//Log.d("ddd", "309 not null");
				mSongName = mMusicCenter.getCurMusicInfo().getTrack();
				//Log.d("ddd", "songName = " + songName);
				if (mSongName != null) {
					canvas.drawText(mSongName, mWidth / 2, mWidth * 0.75f, mPaint);
				}
			} else {
				canvas.drawText(mSongName, mWidth / 2, mWidth * 0.75f, mPaint);
			}
			mPaint.setXfermode(mPorterDuffXfermode);
			canvas.drawBitmap(mMask, null, mBgRectF, mPaint);
			//canvas.drawCircle(mWidth / 2, mWidth / 2, mBgRectF.width() / 2, mPaint);
			mPaint.setXfermode(null);
			canvas.restoreToCount(sc);
			// =======================================================
			//控制移动
			if (mAnimationStartTime == 0
					|| System.currentTimeMillis() - mAnimationStartTime > 4000f) {
				mAnimationStartTime = System.currentTimeMillis();
			}
			float t = (System.currentTimeMillis() - mAnimationStartTime) / 4000f;
			if (t < 1) {
				t = mInterpolator.getInterpolation(t);
				mWaveX = waveWidth * t;
			} else {
				mWaveX = 0;
				mAnimationStartTime = System.currentTimeMillis();
			}
			
			//第二条
			if (mStartTime2 == 0
					|| System.currentTimeMillis() - mStartTime2 > 3000f) {
				mStartTime2 = System.currentTimeMillis();
			}
			float t2 = (System.currentTimeMillis() - mStartTime2) / 3000f;
			if (t2 < 1) {
				t2 = mInterpolator.getInterpolation(t2);
				mWave2X = wave2Width * t2;
			} else {
				mWave2X = 0;
				mStartTime2 = System.currentTimeMillis();
			}

			if (Constant.sIsScreenOn) {
				invalidate();
			}
		}

		@Override
		public void onDestroy()
		{
			ViewUtils.recycleBitmap(mBg);
			ViewUtils.recycleBitmap(mWave1);
			ViewUtils.recycleBitmap(mWave2);
			unRegist();
		}

		@Override
		public void onResume()
		{
			invalidate();
		}
	}*/
	
	/*private Bitmap mBitmap;
	private void checkCustomBg() {
		ViewUtils.recycleBitmap(mBitmap);
		mBitmap = Global.getMyBgBitmap(getContext(), GetPictureActivity.MUSIC_INDEX);
		if (mBitmap != null) {
			mMusicBg.setBackgroundDrawable(new BitmapDrawable(mBitmap));
		} else {
			mMusicBg.setBackgroundResource(R.drawable.music_bg);
		}
	}*/
	
	private void startRotateAnimation(View v) {
		if (v != null) {
			RotateAnimation r = new RotateAnimation(0, 359,	Animation.RELATIVE_TO_SELF, 
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			r.setInterpolator(new LinearInterpolator());
			r.setDuration(3000);
			r.setRepeatCount(Animation.INFINITE);
			r.setRepeatMode(Animation.RESTART);
			v.startAnimation(r);
		}
	}
	
	private boolean mIsAnimation = true;
	private void startNeddleRotate(View v) {
		if (v != null) {
			RotateAnimation r = new RotateAnimation(0, 28,	Animation.RELATIVE_TO_SELF, 
					0.8f, Animation.RELATIVE_TO_SELF, 0.1f);
			r.setInterpolator(new LinearInterpolator());
			r.setDuration(1000);
			r.setFillAfter(true);
			/*r.setRepeatCount(Animation.INFINITE);
			r.setRepeatMode(Animation.RESTART);*/
			v.startAnimation(r);
			mIsAnimation = true;
			r.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					startRotateAnimation(mCd);
				}
			});
		}
	}
	
	private void stopNeddleRotate(View v) {
		if (v != null) {
			RotateAnimation r = new RotateAnimation(28, 0, Animation.RELATIVE_TO_SELF, 
					0.8f, Animation.RELATIVE_TO_SELF, 0.1f);
			r.setInterpolator(new LinearInterpolator());
			r.setDuration(1000);
			//r.setFillAfter(true);
			/*r.setRepeatCount(Animation.INFINITE);
			r.setRepeatMode(Animation.RESTART);*/
			v.startAnimation(r);
			mIsAnimation = false;
		}
	}
}
