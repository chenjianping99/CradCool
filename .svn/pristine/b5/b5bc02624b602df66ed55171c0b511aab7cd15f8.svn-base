package com.jiubang.goscreenlock.theme.cjpcardcool.switcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.WindowManager;

/**
 * 绘制工具类
 * @author luopeihuan
 *
 */
public class DrawUtils
{
	public static float sDensity = 1.0f;
	public static int sDensityDpi;
	public static int sWidthPixels;
	public static int sHeightPixels;
	public static float sFontDensity;
	public static int sTouchSlop = 15; // 点击的最大识别距离，超过即认为是移动

	public static int sStatusHeight; // 平板中底边的状态栏高度
	private static Class sClass = null;
	private static Method sMethodForWidth = null;
	private static Method sMethodForHeight = null;
	public static int sTopStatusHeight;

	// 在某些机子上存在不同的density值，所以增加两个虚拟值
	public static float sVirtualDensity = -1;
	public static float sVirtualDensityDpi = -1;

	
	/**
	 * dip/dp转像素
	 * 
	 * @param dipValue
	 *            dip或 dp大小
	 * @return 像素值
	 */
	public static int dip2px(float dipVlue) {
		return (int) (dipVlue * sDensity + 0.5f);
	}

	/**
	 * 像素转dip/dp
	 * 
	 * @param pxValue
	 *            像素大小
	 * @return dip值
	 */
	public static int px2dip(float pxValue) {
		final float scale = sDensity;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * sp 转 px
	 * 
	 * @param spValue
	 *            sp大小
	 * @return 像素值
	 */
	public static int sp2px(float spValue) {
		final float scale = sDensity;
		return (int) (scale * spValue);
	}

	/**
	 * px转sp
	 * 
	 * @param pxValue
	 *            像素大小
	 * @return sp值
	 */
	public static int px2sp(float pxValue) {
		final float scale = sDensity;
		return (int) (pxValue / scale);
	}

	public static void resetDensity(Context context) {
		if (context != null && null != context.getResources()) {
			DisplayMetrics metrics = context.getResources().getDisplayMetrics();
			sDensity = metrics.density;
			sFontDensity = metrics.scaledDensity;
			sWidthPixels = metrics.widthPixels;
			sHeightPixels = metrics.heightPixels;
			sDensityDpi = metrics.densityDpi;
			try {
				final ViewConfiguration configuration = ViewConfiguration.get(context);
				if (null != configuration) {
					sTouchSlop = configuration.getScaledTouchSlop();
				}
				getStatusBarHeight(context);
			} catch (Error e) {
				Log.i("DrawUtils", "resetDensity has error" + e.getMessage());
			}
		}
	}

	public static int getTabletScreenWidth(Context context) {
		int width = 0;
		if (context != null) {
			try {
				WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
				Display display = wm.getDefaultDisplay();
				if (sClass == null) {
					sClass = Class.forName("android.view.Display");
				}
				if (sMethodForWidth == null) {
					sMethodForWidth = sClass.getMethod("getRealWidth");
				}
				width = (Integer) sMethodForWidth.invoke(display);
			} catch (Exception e) {
			}
		}

		// Rect rect= new Rect();
		// ((Activity)
		// context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		// int statusbarHeight = height - rect.bottom;
		if (width == 0) {
			width = sWidthPixels;
		}

		return width;
	}

	public static int getTabletScreenHeight(Context context) {
		int height = 0;
		if (context != null) {
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			try {
				if (sClass == null) {
					sClass = Class.forName("android.view.Display");
				}
				if (sMethodForHeight == null) {
					sMethodForHeight = sClass.getMethod("getRealHeight");
				}
				height = (Integer) sMethodForHeight.invoke(display);
			} catch (Exception e) {
			}
		}

		// Rect rect= new Rect();
		// ((Activity)
		// context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		// int statusbarHeight = height - rect.bottom;
		if (height == 0) {
			height = sHeightPixels;
		}

		return height;
	}
	
	public static boolean isPad() {
		if (sDensity >= 1.5 || sDensity <= 0) {
			return false;
		}
		if (sWidthPixels < sHeightPixels) {
			if (sWidthPixels > 480 && sHeightPixels > 800) {
				return true;
			}
		} else {
			if (sWidthPixels > 800 && sHeightPixels > 480) {
				return true;
			}
		}
		return false;
	}
	
	public static int getStatusBarHeight(Context context)
	{
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0;
		int top = 0;
		try
		{
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			top = context.getResources().getDimensionPixelSize(x);
			sTopStatusHeight = top;
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		return top;
	}
	
	/**
	 * 
	 * @param mask 遮罩图
	 * @param mPhotoBitmap 源图
	 * @param overBitmap 罩子图
	 * @return
	 */
	public static Bitmap getBitmap(Bitmap mask, Bitmap mPhotoBitmap, Bitmap overBitmap)
	{
		if (mask == null) {
			return null;
		}
		Bitmap targetBitmap = null;
		Log.v("Test", "DrawUtil : getBitmap");
		try {
			float photoWidth = mask.getHeight();
			float photoHeight = mask.getHeight();
			Paint paint = new Paint();
			paint.setColor(Color.BLACK);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
			targetBitmap = Bitmap.createBitmap((int) photoWidth,
					(int) photoHeight, Config.ARGB_8888);
			Canvas canvas = new Canvas(targetBitmap);
			if (mPhotoBitmap != null) {
				canvas.save();
				canvas.scale(photoWidth / mPhotoBitmap.getWidth(), photoHeight
						/ mPhotoBitmap.getHeight());
				canvas.drawBitmap(mPhotoBitmap, 0, 0, null);
				canvas.restore();
			}
			canvas.save();
			canvas.scale(photoWidth / mask.getWidth(), photoHeight
					/ mask.getHeight());
			canvas.drawBitmap(mask, 0, 0, paint);
			canvas.restore();
			if (overBitmap != null) {
				canvas.save();
				canvas.scale(photoWidth / overBitmap.getWidth(), photoHeight
						/ overBitmap.getHeight());
				canvas.drawBitmap(overBitmap, 0, 0, null);
				canvas.restore();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return targetBitmap;
	}
	/**
	 * 
	 * @param mask 遮罩图
	 * @param mPhotoBitmap 源图
	 * @param overBitmap 罩子图
	 * @return
	 */
	public static Bitmap getClickBitmap(Bitmap mask, Bitmap mPhotoBitmap, Bitmap overBitmap)
	{
		if (mask == null) {
			return null;
		}
		Log.v("Test", "DrawUtil : getClickBitmap");
		Bitmap targetBitmap = null;
		try {
			float photoWidth = mask.getHeight();
			float photoHeight = mask.getHeight();
			Paint paint = new Paint();
			paint.setColor(Color.BLACK);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
			targetBitmap = Bitmap.createBitmap((int) photoWidth,
					(int) photoHeight, Config.ARGB_8888);
			Canvas canvas = new Canvas(targetBitmap);
			if (mPhotoBitmap != null) {
				canvas.save();
				canvas.scale(photoWidth / mPhotoBitmap.getWidth(), photoHeight
						/ mPhotoBitmap.getHeight());
				canvas.drawBitmap(mPhotoBitmap, 0, 0, null);
				canvas.restore();
			}
			canvas.save();
			canvas.scale(photoWidth / mask.getWidth(), photoHeight
					/ mask.getHeight());
			canvas.drawBitmap(mask, 0, 0, paint);
			canvas.restore();
			if (overBitmap != null) {
				canvas.save();
				canvas.scale(photoWidth / overBitmap.getWidth(), photoHeight
						/ overBitmap.getHeight());
				canvas.drawBitmap(overBitmap, 0, 0, null);
				canvas.restore();
			}
			canvas.save();
			canvas.scale(photoWidth / mask.getWidth(), photoHeight
					/ mask.getHeight());
			canvas.drawBitmap(mask, 0, 0, paint);
			canvas.restore();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return targetBitmap;
	}
}
