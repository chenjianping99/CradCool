package com.jiubang.goscreenlock.theme.cjpcardcool.view;
//CHECKSTYLE:OFF
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * 
 * @author shenyaobin 图片工具类
 * 
 */
public class ViewUtils {

	public static float sScaleH = 0;
	public static float sScaleW = 0;

	public static float getScaleByWidth() {
		//if (sScaleW == 0) {
			sScaleW = Constant.sRealWidth / 720f;
		//}
		return sScaleW;
	}

	public static float getScaleByHeight() {
		//if (sScaleH == 0) {
			sScaleH = Constant.sRealHeight / 1280f;
		//}
		return sScaleH;
	}

	public static int getPXByHeight(int value) {
		//if (sScaleH == 0) {
			sScaleH = Constant.sRealHeight / 1280f;
		//}
		return Math.round(value * sScaleH);
	}

	public static int getPXByWidth(int value) {
		//if (sScaleW == 0) {
			sScaleW = Constant.sRealWidth / 720f;
		//}
		//Log.d("ddd", "sScaleW =" + sScaleW + "Constant.sRealWidth =" + Constant.sRealWidth);
		return Math.round(value * sScaleW);
	}

	public static Dimension getDimension(Context context, int id) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		InputStream is = context.getResources().openRawResource(id);
		BitmapFactory.decodeStream(is, null, opt);
		float scale_w = (float) Constant.S_DEFAULT_WIDTH
				/ (float) Constant.sRealWidth;
		float scale_h = (float) Constant.S_DEFAULT_HEIGHT
				/ (float) Constant.sRealHeight;

		return new Dimension(opt.outWidth / scale_w, opt.outHeight / scale_h);
	}

	public static Dimension getDimensionByWidth(Context context, int id) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		InputStream is = context.getResources().openRawResource(id);
		BitmapFactory.decodeStream(is, null, opt);
		float scale = (float) Constant.S_DEFAULT_WIDTH
				/ (float) Constant.sRealWidth;
		return new Dimension(opt.outWidth / scale, opt.outHeight / scale);
	}

	public static Dimension getDimensionByHeight(Context context, int id) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		InputStream is = context.getResources().openRawResource(id);
		BitmapFactory.decodeStream(is, null, opt);
		float scale = (float) Constant.S_DEFAULT_HEIGHT
				/ (float) Constant.sRealHeight;
		return new Dimension(opt.outWidth / scale, opt.outHeight / scale);
	}

	/**
	 * 
	 * @author shenyaobin
	 * 
	 */
	static class Dimension {
		public int width;
		public int height;

		public Dimension(float w, float h) {
			width = (int) w;
			height = (int) h;
		}
	}

	public static Bitmap getBitmapWidthId(Context context, int id) {
		Bitmap bmp = null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(id);
		try {
			bmp = BitmapFactory.decodeStream(is, null, opt);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bmp;

	}

	public static Bitmap getBitmapByPath(Context context, String path) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		return BitmapFactory.decodeFile(path, opt);

	}

	public static Bitmap getScaleBitmapWithIDByHeight(Context context, int id) {
		Bitmap bmp = null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(id);
		try {
			bmp = BitmapFactory.decodeStream(is, null, opt);
			if ((int) Constant.sRealHeight == (int) Constant.S_DEFAULT_HEIGHT) {
				return bmp;
			}

			if (bmp != null) {
				int w = bmp.getWidth();
				int h = bmp.getHeight();

				float scale = (float) Constant.sRealHeight
						/ (float) Constant.S_DEFAULT_HEIGHT;
				Matrix matrix = new Matrix();
				matrix.postScale(scale, scale);
				Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix,
						true);
				if (newbm != bmp) {
					bmp.recycle();
					bmp = null;
				}
				return newbm;
			}
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Bitmap getScaleBitmapWithIDByWidth(Context context, int id) {
		Bitmap bmp = null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(id);
		try {
			bmp = BitmapFactory.decodeStream(is, null, opt);
			if ((int) Constant.sRealWidth == (int) Constant.S_DEFAULT_WIDTH) {
				return bmp;
			}
			if (bmp != null) {
				int w = bmp.getWidth();
				int h = bmp.getHeight();

				float scale = (float) Constant.sRealWidth
						/ (float) Constant.S_DEFAULT_WIDTH;
				Matrix matrix = new Matrix();
				matrix.postScale(scale, scale);
				Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix,
						true);
				if (newbm != bmp) {
					bmp.recycle();
					bmp = null;
				}
				return newbm;
			}
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Bitmap getScaleBitmapWithID(Context context, int id) {
		Bitmap bmp = null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(id);
		try {
			bmp = BitmapFactory.decodeStream(is, null, opt);
			if (bmp != null) {
				int w = bmp.getWidth();
				int h = bmp.getHeight();
				float scale_x = Constant.sRealWidth
						/ (float) Constant.S_DEFAULT_WIDTH;
				float scale_y = Constant.sRealHeight
						/ (float) Constant.S_DEFAULT_HEIGHT;
				Matrix matrix = new Matrix();
				matrix.postScale(scale_x, scale_y);
				Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix,
						true);
				if (newbm != bmp) {
					bmp.recycle();
					bmp = null;
				}
				return newbm;
			}
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/*public static Drawable getDrawable(Context context, int resId) {
		Drawable icon = null;
		try {
			Bitmap bmp = getScaleBitmapWithIDByHeight(context, resId);
			if (bmp != null) {
				icon = new MyBitmapDrawable(context.getResources(), bmp);
			}
		} catch (NotFoundException e) {
			// Log.d("RobotAnimationView--->>>", "getDrawable() " + strRes +
			// " NotFoundException");
		} catch (OutOfMemoryError e) {
			// Log.d("RobotAnimationView--->>>", "getDrawable() " + strRes +
			// " OutOfMemoryError");
		} catch (Exception e) {
			// Log.d("RobotAnimationView--->>>", "getDrawable()" + strRes +
			// " has Exception");
		}
		return icon;
	}
	
	public static Drawable getDrawable(Context context, String strRes) {
		Drawable icon = null;
		if (strRes == null) {
			return icon;
		}
		try {
			if (strRes.lastIndexOf(".9") > 0) {
				strRes = strRes.substring(0, strRes.lastIndexOf(".9"));
				int resId = getResId(context, strRes);
				icon = new BitmapDrawable(getScaleBitmapWithIDByHeight(context,
						resId));
			} else {
				int resId = getResId(context, strRes);
				Bitmap bmp = getScaleBitmapWithIDByHeight(context, resId);
				if (bmp != null) {
					icon = new MyBitmapDrawable(context.getResources(), bmp);
				}
			}
			return icon;
		} catch (NotFoundException e) {
			// Log.d("RobotAnimationView--->>>", "getDrawable() " + strRes +
			// " NotFoundException");
		} catch (OutOfMemoryError e) {
			// Log.d("RobotAnimationView--->>>", "getDrawable() " + strRes +
			// " OutOfMemoryError");
		} catch (Exception e) {
			// Log.d("RobotAnimationView--->>>", "getDrawable()" + strRes +
			// " has Exception");
		}
		return icon;
	}*/

	public static int getResId(Context context, String strRes) {
		int ret = -1;
		ret = context.getResources().getIdentifier(
				context.getPackageName() + ":drawable/" + strRes, null, null);
		return ret;
	}

	public static BitmapFactory.Options getOption(Context context, int id) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		InputStream is = context.getResources().openRawResource(id);
		BitmapFactory.decodeStream(is, null, opt);

		return opt;
	}

	public static Bitmap getGaussinaBlur(Bitmap sentBitmap, int radius) {
		try {
			return getGaussina(sentBitmap, radius);
		} catch (OutOfMemoryError oom) {
			// TODO: handle exception
			System.gc();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sentBitmap;

	}

	private static Bitmap getGaussina(Bitmap sentBitmap, int radius) {

		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		if (radius < 1) {
			return null;
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = i / divsum;
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = p & 0x0000ff;
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = p & 0x0000ff;

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
						| (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		bitmap.setPixels(pix, 0, w, 0, 0, w, h);
		return bitmap;
	}

	public static void recycleBitmap(Bitmap b) {
		if (b != null && !b.isRecycled()) {
			b.recycle();
			b = null;
		}
	}

}
