package com.jiubang.goscreenlock.theme.cjpcardcool.view.laceview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;

/**
 * 
 * @author liangdaijian
 *
 */
public class CellDrawable extends BitmapDrawable
{
	public CellDrawable()
	{
		super();
	}

	public CellDrawable(Resources res, Bitmap bitmap, String mWords)
	{
		super(res, bitmap);
		this.mWords = mWords;
	}

	@Override
	public void draw(Canvas canvas)
	{
		if (getBitmap() != null && !getBitmap().isRecycled())
		{
			super.draw(canvas);
		}
	}

	private String	mWords	= null;
	public void draw(Canvas canvas, int color, int centerX, int centerY, float scale,
			Paint wordPaint, int rotate, boolean canColor)
	{
		try
		{
			canvas.save();
			canvas.rotate(rotate, centerX, centerY);
			setBounds(centerX - (getIntrinsicWidth() >> 1), centerY - (getIntrinsicHeight() >> 1),
					centerX + (getIntrinsicWidth() >> 1), centerY + (getIntrinsicHeight() >> 1));
			if (canColor)
			{
				setColorFilter(color, PorterDuff.Mode.SRC_IN);
			}
			canvas.save();
			canvas.scale(scale, scale, centerX, centerY);
			draw(canvas);
			if (mWords != null)
			{
				canvas.drawText(mWords, centerX, centerY - (getIntrinsicHeight() >> 1), wordPaint);
			}
			canvas.restore();
			canvas.restore();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}
}
