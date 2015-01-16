package com.jiubang.goscreenlock.theme.cjpcardcool.switcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.bean.SwipeTypes;
import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler.ISwitcherable;
import com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler.SwitchConstants;

/**
 * 
 * @author zhangjie
 * 
 */
public class IconImage extends ImageView {
	private Bitmap mIconBitmap;
	private Bitmap mMaskBitmap;
	private Bitmap mMarkBitmap;
	private boolean mIsAddShow;
	private Paint mPaint;
	private int mBaseType;
	
	private int			mSwitchType;	//开关种类
	private int			mStatus;		//开关状态
	private int			mStatus2;		//用于电池
	private Drawable[]	mDrawables;
	private float		mScale	= 1.0f;
	private Paint		mPaintc;
	private float		mTextTop;
	private Rect		mRect;

	public IconImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public IconImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public IconImage(Context context) {
		super(context);
		init(context);
	}
	
	public void setType(int type) {
		mBaseType = type;
	}

	public void setBitmap(Bitmap mask, Paint paint) {
		mMaskBitmap = mask;
		mPaint = paint;
		invalidate();
	}

	public void setBitmap(Bitmap icon) {
		mIconBitmap = icon;
		setImageDrawable(null);
		invalidate();
	}
	
	public void setMark(Bitmap bitmap) {
		mMarkBitmap = bitmap;
		invalidate();
	}
	
	public void setShow(boolean show) {
		mIsAddShow = show;
		invalidate();
	}

	private void init(Context context) {
		mPaintc = new Paint();
		mPaintc.setTextSize(9 * DrawUtils.sDensity);
		mPaintc.setColor(Color.argb(255, 0xf0, 0xfb, 0xff));
		mPaintc.setTextAlign(Align.LEFT);
		mTextTop = DrawUtils.dip2px(12);
		mRect = new Rect();
	}
	
	/**
	 * 设置状态
	 * @param status
	 */
	public void setStatus(int type, int status1, int status2) {
		mSwitchType = type;
		mStatus = status1;
		mStatus2 = status2;

		switch (mSwitchType) {
			case ISwitcherable.SWITCH_TYPE_AIRPLANE_MODE :
			case ISwitcherable.SWITCH_TYPE_AUTO_SYNC :
			case ISwitcherable.SWITCH_TYPE_BLUETOOTH :
			case ISwitcherable.SWITCH_TYPE_GPRS :
			case ISwitcherable.SWITCH_TYPE_WIFI :
			case ISwitcherable.SWITCH_TYPE_GPS :
			case ISwitcherable.SWITCH_TYPE_VIBRATE :
			case ISwitcherable.SWITCH_TYPE_FLASHLIGHT :
			case ISwitcherable.SWITCH_TYPE_RING :
				if (SwitchConstants.STATUS_ON == status1) {
					//setImageResource(BroadcastBean.getIcon(mSwitchType));
				}
				else if (SwitchConstants.STATUS_OFF == status1) {
					//setImageResource(BroadcastBean.getIconOff(mSwitchType));
				}
				break;
			case ISwitcherable.SWITCH_TYPE_BATTERY :
				setImageDrawable(null);
				break;
			case ISwitcherable.SWITCH_TYPE_BRIGHTNESS :
				//setImageResource(BroadcastBean.getBrightNessIcon(status1));
				break;
			default :
				break;
		}
		invalidate();
	}

	public void setChargeBitmap(Drawable[] bitmaps) {
		mDrawables = bitmaps;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mBaseType == SwipeTypes.SWITCH_TYPE) {
			if (mSwitchType == ISwitcherable.SWITCH_TYPE_BATTERY) {
				try {
					Drawable drawable = mDrawables[0];
					int left = getWidth() / 2 - drawable.getIntrinsicWidth() / 2;
					int top = getHeight() / 2 - drawable.getIntrinsicHeight() / 2;
					int outWidth = drawable.getIntrinsicWidth();
					int outHeight = drawable.getIntrinsicHeight();
					int ww = DrawUtils.dip2px(3);
					if (drawable != null) {
						canvas.save();
						canvas.translate(left, top);
						drawable.setBounds(0, 0, outWidth, outHeight);
						drawable.draw(canvas);
						canvas.restore();
					}
					if (mStatus <= 10) {
						drawable = mDrawables[3];
					}
					else if (mStatus <= 50) {
						drawable = mDrawables[2];
					}
					else {
						drawable = mDrawables[1];
					}
					if (drawable != null) {
						canvas.save();
						float y = top + (int) ((outHeight - 3 * ww) * ((100 - mStatus) / 100f)) + ww;
						int heigh = (int) ((outHeight - 3 * ww) * (mStatus / 100f) + 3 * ww) - ww;
						canvas.translate(left, y);
						drawable.setBounds(0, 0, outWidth, heigh);
						drawable.draw(canvas);
						canvas.restore();
					}
					if (mPaintc != null) {
						mPaintc.getTextBounds("" + mStatus, 0, ("" + mStatus).length(), mRect);
						canvas.save();
						canvas.translate(getWidth() / 2 - mRect.width() / 2,
								top + mTextTop + mRect.height() / 2);
						canvas.drawText("" + mStatus, 0, 0, mPaintc);
						canvas.restore();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				super.onDraw(canvas);
			}
		} else {
			try {
				super.onDraw(canvas);
				int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
						Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
						| Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
						| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
						| Canvas.CLIP_TO_LAYER_SAVE_FLAG);
				if (mMaskBitmap != null && mPaint != null && !mMaskBitmap.isRecycled()) {
					canvas.save();
					canvas.scale(getWidth() * 1.0f / mMaskBitmap.getWidth(),
							getHeight() * 1.0f / mMaskBitmap.getHeight());
					canvas.drawBitmap(mMaskBitmap, 0, 0, mPaint);
					canvas.restore();
				}
				if (mIconBitmap != null && !mIconBitmap.isRecycled()) {
					canvas.save();
					canvas.scale(getWidth() * 1.0f / mIconBitmap.getWidth(),
							getHeight() * 1.0f / mIconBitmap.getHeight());
					canvas.drawBitmap(mIconBitmap, 0, 0, null);
					canvas.restore();
				}
				if (mIsAddShow && mMarkBitmap != null && !mMarkBitmap.isRecycled()) {
					canvas.save();
					canvas.scale(getWidth() * 1.0f / mMarkBitmap.getWidth(),
							getHeight() * 1.0f / mMarkBitmap.getHeight());
					canvas.drawBitmap(mMarkBitmap, 0, 0, null);
					canvas.restore();
				}
				canvas.restoreToCount(sc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
