package com.jiubang.goscreenlock.theme.cjpcardcool.switcher;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
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
	private final static int NO_COLOR = 0x00000001;
	private final static int TRANSPARENT_COLOR = 0x00000000;

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
	
	public Drawable getDrawable(Resources res, int resId) {
		if (res == null) {
			return null;
		}
		Bitmap bitmap = getBitmapByResid(res, resId);
		if (bitmap == null) {
			return null;
		}
		if (null == bitmap.getNinePatchChunk()) {
			return res.getDrawable(resId);
		} else {
			Rect padding = new Rect();
			readPaddingFromChunk(bitmap.getNinePatchChunk(), padding);
			NinePatchDrawable d = new NinePatchDrawable(res,
					bitmap, bitmap.getNinePatchChunk(), padding, null);
			return d;
		}
	}
	
	public static void readPaddingFromChunk(byte[] chunk, Rect paddingRect) {
		paddingRect.left = getInt(chunk, 12);
		paddingRect.right = getInt(chunk, 16);
		paddingRect.top = getInt(chunk, 20);
		paddingRect.bottom = getInt(chunk, 24);
	}
 
	public static byte[] readChunk(Bitmap yuantuBmp) throws IOException {
		final int mBMW = yuantuBmp.getWidth();
		final int mBMH = yuantuBmp.getHeight();
 
		int xPointCount = 0;
		int yPointCount = 0;
 
		int xBlockCount = 0;
		int yBlockCount = 0;
 
		ByteArrayOutputStream ooo = new ByteArrayOutputStream();
		for (int i = 0; i < 32; i++) {
			ooo.write(0);
		}
 
		{ // x
			int[] pixelsTop = new int[mBMW - 2];
			yuantuBmp.getPixels(pixelsTop, 0, mBMW, 1, 0, mBMW - 2, 1);
			boolean topFirstPixelIsBlack = pixelsTop[0] == Color.BLACK;
			boolean topLastPixelIsBlack = pixelsTop[pixelsTop.length - 1] == Color.BLACK;
			int tmpLastColor = Color.TRANSPARENT;
			for (int i = 0, len = pixelsTop.length; i < len; i++) {
				if (tmpLastColor != pixelsTop[i]) {
					xPointCount++;
					writeInt(ooo, i);
					tmpLastColor = pixelsTop[i];
				}
			}
			if (topLastPixelIsBlack) {
				xPointCount++;
				writeInt(ooo, pixelsTop.length);
			}
			xBlockCount = xPointCount + 1;
			if (topFirstPixelIsBlack) {
				xBlockCount--;
			}
			if (topLastPixelIsBlack) {
				xBlockCount--;
			}
		}
 
		{ // y
			int[] pixelsLeft = new int[mBMH - 2];
			yuantuBmp.getPixels(pixelsLeft, 0, 1, 0, 1, 1, mBMH - 2);
			boolean firstPixelIsBlack = pixelsLeft[0] == Color.BLACK;
			boolean lastPixelIsBlack = pixelsLeft[pixelsLeft.length - 1] == Color.BLACK;
			int tmpLastColor = Color.TRANSPARENT;
			for (int i = 0, len = pixelsLeft.length; i < len; i++) {
				if (tmpLastColor != pixelsLeft[i]) {
					yPointCount++;
					writeInt(ooo, i);
					tmpLastColor = pixelsLeft[i];
				}
			}
			if (lastPixelIsBlack) {
				yPointCount++;
				writeInt(ooo, pixelsLeft.length);
			}
			yBlockCount = yPointCount + 1;
			if (firstPixelIsBlack) {
				yBlockCount--;
			}
			if (lastPixelIsBlack) {
				yBlockCount--;
			}
		}
 
		{ // color
			for (int i = 0; i < xBlockCount * yBlockCount; i++) {
				writeInt(ooo, NO_COLOR);
			}
		}
 
		byte[] data = ooo.toByteArray();
		data[0] = 1;
		data[1] = (byte) xPointCount;
		data[2] = (byte) yPointCount;
		data[3] = (byte) (xBlockCount * yBlockCount);
		dealPaddingInfo(yuantuBmp, data);
		return data;
	}
 
	private static void dealPaddingInfo(Bitmap bm, byte[] data) {
		{ // padding left & padding right
			int[] bottomPixels = new int[bm.getWidth() - 2];
			bm.getPixels(bottomPixels, 0, bottomPixels.length, 1,
					bm.getHeight() - 1, bottomPixels.length, 1);
			for (int i = 0; i < bottomPixels.length; i++) {
				if (Color.BLACK == bottomPixels[i]) { // padding left
					writeInt(data, 12, i);
					break;
				}
			}
			for (int i = bottomPixels.length - 1; i >= 0; i--) {
				if (Color.BLACK == bottomPixels[i]) { // padding right
					writeInt(data, 16, bottomPixels.length - i - 2);
					break;
				}
			}
		}
		{ // padding top & padding bottom
			int[] rightPixels = new int[bm.getHeight() - 2];
			bm.getPixels(rightPixels, 0, 1, bm.getWidth() - 1, 0, 1,
					rightPixels.length);
			for (int i = 0; i < rightPixels.length; i++) {
				if (Color.BLACK == rightPixels[i]) { // padding top
					writeInt(data, 20, i);
					break;
				}
			}
			for (int i = rightPixels.length - 1; i >= 0; i--) {
				if (Color.BLACK == rightPixels[i]) { // padding bottom
					writeInt(data, 24, rightPixels.length - i - 2);
					break;
				}
			}
		}
	}
 
	private static void writeInt(OutputStream out, int v) throws IOException {
		out.write((v >> 0) & 0xFF);
		out.write((v >> 8) & 0xFF);
		out.write((v >> 16) & 0xFF);
		out.write((v >> 24) & 0xFF);
	}
 
	private static void writeInt(byte[] b, int offset, int v) {
		b[offset + 0] = (byte) (v >> 0);
		b[offset + 1] = (byte) (v >> 8);
		b[offset + 2] = (byte) (v >> 16);
		b[offset + 3] = (byte) (v >> 24);
	}
 
	private static int getInt(byte[] bs, int from) {
		int b1 = bs[from + 0];
		int b2 = bs[from + 1];
		int b3 = bs[from + 2];
		int b4 = bs[from + 3];
		int i = b1 | (b2 << 8)  | (b3 << 16) | b4 << 24;
		return i;
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
}
