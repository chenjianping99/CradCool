package com.jiubang.goscreenlock.theme.cjpcardcool.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * 图片缓存管理
 * 
 * @author zhangjie
 */
public class BitmapManager {
	private static final String TAG = "BitmapManager";
	private static BitmapManager sInstance = null;
	private HashMap<String, SoftReference<Bitmap>> mBitmapCache;
	private HashMap<String, String> mBitmapToNameMap;
	protected int mImageWidth = 0;
	protected int mImageHeight = 0;

	public synchronized static BitmapManager getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new BitmapManager(context);
		}
		return sInstance;
	}

	/**
	 * 释放
	 */
	private synchronized static void releaseSelfInstance() {
		sInstance = null;
	}

	private BitmapManager(Context context) {
		mBitmapCache = new HashMap<String, SoftReference<Bitmap>>(50);
		mBitmapToNameMap = new HashMap<String, String>(50);
	}

	/**
	 * 设置目标图片的最大宽高
	 * 
	 * @param width
	 *            宽的最大值
	 * @param height
	 *            高的最大值
	 */
	public void setImageSize(int width, int height) {
		mImageWidth = width;
		mImageHeight = height;
	}

	/**
	 * 设置目标图片的最大宽高
	 * 
	 * @param size
	 *            宽和高的最大值
	 */
	public void setImageSize(int size) {
		setImageSize(size, size);
	}
	
	/**
	 * 根据文件路径获取Bitmap
	 * 
	 * @param path
	 * @return
	 */
	public Bitmap getBitmapByFilePathNoCache(String path) {
		Bitmap tmpBitmap = null;
		try {
			tmpBitmap = decodeSampledBitmapFromFile(path, mImageWidth,
					mImageHeight);
		} catch (OutOfMemoryError e) {
			Log.e(TAG, "OutOfMemoryError, path:" + path);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return tmpBitmap;
	}

	/**
	 * 根据文件路径获取Bitmap
	 * 
	 * @param path
	 * @return
	 */
	public Bitmap getBitmapByFilePath(String path) {
		return getBitmapByFilePath(path, mImageWidth, mImageHeight);
	}
	
	/**
	 * 根据文件路径获取Bitmap
	 * 
	 * @param path
	 * @return
	 */
	public Bitmap getBitmapByFilePath(String path, int reqWidth, int reqHeight) {
		Bitmap tmpBitmap = null;
		if (path == null) {
			return null;
		}
		tmpBitmap = getBitmapFormCache(path);
		if (null != tmpBitmap) {
			return tmpBitmap;
		}
		try {
			tmpBitmap = decodeSampledBitmapFromFile(path, reqWidth,
					reqHeight);
		} catch (OutOfMemoryError e) {
			Log.e(TAG, "OutOfMemoryError, path:" + path);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return tmpBitmap;
	}
	
	/**
	 * 根据资源ID获取Bitmap
	 * @param resId
	 * @return
	 */
	public Bitmap getBitmapByResid(Resources res, int resId)
	{
		if (res == null) {
			return null;
		}
		return getBitmapByResid(res, resId, mImageWidth, mImageHeight);
	}
	
	/**
	 * 根据资源ID获取Bitmap
	 * @param resId
	 * @return
	 */
	public Bitmap getBitmapByResid(Resources res, int resId, int reqWidth, int reqHeight)
	{
		if (res == null) {
			return null;
		}
		Bitmap tmpBitmap = null;
		tmpBitmap = getBitmapFormCache(String.valueOf(resId));
		if (null != tmpBitmap) {
			return tmpBitmap;
		}
		try {
			tmpBitmap = decodeSampledBitmapFromResource(res, resId, reqWidth, reqHeight);
		} catch (OutOfMemoryError e) {
			Log.e(TAG, "OutOfMemoryError, resId:" + resId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmpBitmap;
	}
	
	public static Bitmap getBitmap(Resources res, int resId, int width) {
		return getBitmap(res, resId, width, width);
	}
	public static Bitmap getBitmap(Resources res, int resId, int width, int height) {
		return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, resId), width, height, true);
	}

	/**
	 * 从内存取
	 * 
	 * @param name
	 * @return
	 */
	public Bitmap getBitmapFormCache(String name) {
		if (null == name) {
			return null;
		}
		Bitmap bitmap = null;
		SoftReference<Bitmap> bitmapReference = mBitmapCache.get(name);
		if (bitmapReference != null) {
			bitmap = bitmapReference.get();
			if (bitmap != null) {
				return bitmap;
			}
		}
		return null;
	}

	/**
	 * Decode and sample down a bitmap from resources to the requested width and
	 * height.
	 * 
	 * @param res
	 *            The resources object containing the image data
	 * @param resId
	 *            The resource id of the image data
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return A bitmap sampled down from the original with the same aspect
	 *         ratio and dimensions that are equal to or greater than the
	 *         requested width and height
	 */
	public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
			int reqWidth, int reqHeight) {
		if (res == null) {
			return null;
		}
		// First decode with inJustDecodeBounds=true to check dimensions
		final Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);
		if (bitmap != null) {
			saveBitmapToCache(String.valueOf(resId), bitmap);
		}
		return bitmap;
	}

	/**
	 * Decode and sample down a bitmap from a file to the requested width and
	 * height.
	 * 
	 * @param filename
	 *            The full path of the file to decode
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return A bitmap sampled down from the original with the same aspect
	 *         ratio and dimensions that are equal to or greater than the
	 *         requested width and height
	 * @throws java.io.FileNotFoundException
	 */
	public Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth,
			int reqHeight) throws FileNotFoundException {

		if (filename == null || filename.equals("") || filename == "") {
			return null;
		}
		// First decode with inJustDecodeBounds=true to check dimensions
		final Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		FileInputStream stream = null;
		Bitmap bitmap = null;
		try {
			stream = new FileInputStream(filename);
			bitmap = BitmapFactory.decodeStream(stream, null, options);
			// Bitmap bitmap = BitmapFactory.decodeFile(filename, options);
			if (bitmap != null) {
				saveBitmapToCache(filename, bitmap);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	/**
	 * Calculate an inSampleSize for use in a
	 * {@link android.graphics.BitmapFactory.Options} object when decoding
	 * bitmaps using the decode* methods from
	 * {@link android.graphics.BitmapFactory}. This implementation calculates
	 * the closest inSampleSize that will result in the final decoded bitmap
	 * having a width and height equal to or larger than the requested width and
	 * height. This implementation does not ensure a power of 2 is returned for
	 * inSampleSize which can be faster when decoding but results in a larger
	 * bitmap which isn't as useful for caching purposes.
	 * 
	 * @param options
	 *            An options object with out* params already populated (run
	 *            through a decode* method with inJustDecodeBounds==true
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return The value to be used for inSampleSize
	 */
	public static int calculateInSampleSize(Options options, int reqWidth,
			int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (reqHeight <= 0 || reqWidth <= 0) {
			return inSampleSize;
		}
		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee a final image
			// with both dimensions larger than or equal to the requested height
			// and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down
			// further
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}
	/**
	 * 从raw目录取bitmap
	 * @param context
	 * @param resName
	 * @param packageName
	 * @return
	 */
	public Bitmap decodeNormalBitmap(Context context, String resName, String packageName) {
		if (context == null) {
			return null;
		} else {
			return decodeNormalBitmap(context, packageName, resName, mImageWidth, mImageHeight);
		}
	}

	/**
	 * <br>
	 *  从raw目录取bitmap
	 * 
	 * @param context
	 * @param packageName
	 * @param resName
	 * @return
	 */
	public Bitmap decodeNormalBitmap(Context context, String packageName,
			String resName, int reqWidth, int reqHeight) {
		if (resName == null || context == null || packageName == null) {
			return null;

		}
		Bitmap bitmap = getBitmapFormCache(resName);
		if (bitmap != null) {
			return bitmap;
		}
		InputStream is = null;
		try {
			Resources res = context.getResources();
			int resId = res.getIdentifier(resName, "raw", packageName);
			is = res.openRawResource(resId); 
			final Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, options);
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeStream(is, null, options);
			if (bitmap != null) {
				saveBitmapToCache(resName, bitmap);
			}
		} catch (NotFoundException e) {
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}
	/**
	 * 保存图片至缓存
	 * @param name
	 * @param bitmap
	 */
	public void saveBitmapToCache(String name, Bitmap bitmap)
	{
		if (mBitmapCache != null) {
			mBitmapCache.put(name, new SoftReference<Bitmap>(bitmap));
		}
		if (mBitmapToNameMap != null) {
			mBitmapToNameMap.put(bitmap.toString(), name);
		}
	}
	
	public boolean isContain(String name)
	{
		if (mBitmapCache != null) {
			return mBitmapCache.containsKey(name);
		}
		return false;
	}

	/**
	 * <br>
	 * 功能简述:销毁所有bitmap
	 */
	public void recyleAllBitmap() {
		releaseSelfInstance();
		if (mBitmapCache != null) {
			for (SoftReference<Bitmap> reference : mBitmapCache.values()) {
				if (reference != null) {
					Bitmap bitmap = reference.get();
					if (bitmap != null && !bitmap.isRecycled()) {
						bitmap.recycle();
						bitmap = null;
					}
				}
			}
			mBitmapCache.clear();
		}
		if (mBitmapToNameMap != null) {
			mBitmapToNameMap.clear();
		}
	}

	/**
	 * <br>
	 * 功能简述:销毁单个Bitmap的地方 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param bitmap
	 */
	public void recyleBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			String drawableName = mBitmapToNameMap.get(bitmap.toString());
			if (drawableName != null) {
				mBitmapCache.remove(drawableName);
				mBitmapToNameMap.remove(bitmap.toString());
				if (!bitmap.isRecycled()) {
					bitmap.recycle();
					bitmap = null;
				}
			}
		}
	}
	
	/**
	 * 获取圆角位图的方法
	 * @param bitmap 需要转化成圆角的位图
	 * @param pixels 圆角的度数，数值越大，圆角越大
	 * @return 处理后的圆角位图
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
	
	public static Bitmap getGaussina(Context context, Bitmap sentBitmap, int radius) {
		Bitmap bitmap = null;
		try {
			/*if (VERSION.SDK_INT >= 17) {
				if (radius > 25) {
					radius = 25;
				}
				bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

				RenderScript rs = RenderScript.create(context);
				Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
						Allocation.USAGE_SCRIPT);
				Allocation output = Allocation.createTyped(rs, input.getType());
				ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
				script.setRadius(radius  e.g. 3.f );
				script.setInput(input);
				script.forEach(output);
				output.copyTo(bitmap);
				if (rs != null) {
					rs.finish();
					rs.destroy();
					rs = null;
				}
				if (input != null) {
					input.destroy();
					input = null;
				}
				if (output != null) {
					output.destroy();
					output = null;
				}
				if (script != null) {
					script.destroy();
					script = null;
				}
				return bitmap;
			}*/
			bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
			
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
			pix = null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return bitmap;
	}
}
