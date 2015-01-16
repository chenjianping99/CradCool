package com.jiubang.goscreenlock.theme.cjpcardcool.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

/**
 * <br>
 * 类描述: <br>
 * 功能详细描述:
 * 
 * @author jiezhang
 * @date [2012-9-11]
 */
public class MyBitmapDrawable extends BitmapDrawable
{
	public MyBitmapDrawable()
	{
		super();
	}

	public MyBitmapDrawable(Resources res, Bitmap bitmap)
	{
		super(res, bitmap);
	}

	@Override
	public void draw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		if (getBitmap() != null && !getBitmap().isRecycled())
		{
			super.draw(canvas);
		}
	}
}
