package com.jiubang.goscreenlock.theme.cjpcardcool.crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.R;

/**
 * <br>
 * 类描述: <br>
 * 功能详细描述:
 * 
 * @author huanglun
 * @date [2012-10-13]
 */
public class CropImageActivity extends MonitoredActivity {
	@SuppressWarnings("unused")
	private Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG;
	
	private Uri mSaveUri = null;
	private float mAspectX = 1;
	private boolean mCircleCrop = false;
	private final Handler mHandler = new Handler();

	private int mOutputX, mOutputY;
	private boolean mScale;
	boolean mWaitingToPick;
	boolean mSaving;
	private CropImageView mImageView;
	private ContentResolver mContentResolver;

	private Bitmap mBitmap;
	HighlightView mCrop = null;
	private boolean mHasApha = true;

	private Drawable mResizeDrawableWidth;
	private Drawable mResizeDrawableHeight;
	private Drawable mResizeDrawableDiagonal;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		mContentResolver = getContentResolver();

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		if (extras != null) {
			mBitmap = (Bitmap) extras.getParcelable("data");
			mSaveUri = (Uri) extras.getParcelable(MediaStore.EXTRA_OUTPUT);

			if (mSaveUri != null) {
				String outputFormatString = extras.getString("outputFormat");
				if (outputFormatString != null) {
					mOutputFormat = Bitmap.CompressFormat
							.valueOf(outputFormatString);
				}
			}

			mAspectX = extras.getFloat("aspectX");
			mOutputX = extras.getInt("outputX");
			mOutputY = extras.getInt("outputY");
			mScale = extras.getBoolean("scale", false);
			mCircleCrop = extras.getBoolean("circle");

			android.content.res.Resources resources = getResources();
			int id = extras.getInt("arrowHorizontal", -1);
			if (id != -1) {
				mResizeDrawableWidth = resources.getDrawable(id);
			}
			id = extras.getInt("arrowVertical", -1);
			if (id != -1) {
				mResizeDrawableHeight = resources.getDrawable(id);
			}
		}

		if (mBitmap == null) {
			Uri target = intent.getData();
			try {
				decodeBitmapStreamSafe(target);
			} catch (OutOfMemoryError e) {
				OutOfMemoryHandler.handle();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		try {
			Uri target = intent.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(target, proj, null, null,
					null);
			int actualIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String imgPath = cursor
					.getString(actualIndex);

			ExifInterface exifInterface = new ExifInterface(imgPath);
			int ori = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);

			if (cursor != null) {
				try {
					if (Integer.parseInt(Build.VERSION.SDK) < 14) {
						cursor.close();
					}
				} catch (Exception e) {
				}
			}

			float digree = 0;
			switch (ori) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				digree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				digree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				digree = 270;
				break;
			default:
				digree = 0;
				break;
			}
			Matrix m = new Matrix();
			m.postRotate(digree);
			mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
					mBitmap.getHeight(), m, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (mBitmap == null) {
			setResult(RESULT_CANCELED);
			finish();
			return;
		}

		setContentView(R.layout.cropimage);
		FrameLayout parent = (FrameLayout) findViewById(R.id.image);
		mImageView = new CropImageView(this);
		parent.addView(mImageView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		findViewById(R.id.discard).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						setResult(RESULT_CANCELED);
						finish();
					}
				});

		findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSaveClicked();
			}
		});

		makeDefault();
		mImageView.requestLayout();
		mCrop = mImageView.mHighlightViews.get(0);
		mCrop.setFocus(true);

		/*
		 * if (mBitmap.getWidth() > mBitmap.getHeight()) {
		 * mImageView.setImageBitmapResetBase90(mBitmap, 90, true); } else {
		 */
		mImageView.setImageBitmapResetBase(mBitmap, true);
		// }
	}

	private boolean decodeBitmapStreamSafe(Uri target) {
		InputStream is = null;

		boolean bool = true;
		int scale = 1;
		Options opt = new Options();
		while (bool) {
			try {
				is = mContentResolver.openInputStream(target);
				opt.inSampleSize = scale;
				Options opt2 = new Options();
				opt2.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(is, null, opt2);
				is.close();
				opt.inSampleSize = calculateInSampleSize(opt2,
						Global.sScreenWidth, Global.sScreenHeight);
				mBitmap = null;
				is = mContentResolver.openInputStream(target);
				mBitmap = BitmapFactory.decodeStream(is, null, opt);
				// mBitmap = rotate(mBitmap, 90);
				// Log.v(RootView.TAG, "width1 = " + mBitmap.getWidth()
				// +"   height1= "+ mBitmap.getHeight());
				mHasApha = mBitmap.hasAlpha();
				bool = false;

				return true;
			} catch (OutOfMemoryError e) {
				// 如果解碼大圖片，出现爆内存，则每次缩放一半
				OutOfMemoryHandler.handle();
				scale *= 2;
				if (scale > (1 << 10)) {
					// 防止异常死循环
					return false;
				}
			} catch (Throwable e) {
				// TODO: handle Throwable
				bool = false;

				return false;
			}
		}
		return false;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

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

	private void onSaveClicked() {
		if (mCrop == null) {
			return;
		}

		if (mSaving) {
			return;
		}
		mSaving = true;

		Bitmap croppedImage = null;
		Bitmap.Config bmpConfig = mHasApha ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.ARGB_8888;

		if (mOutputX != 0 && mOutputY != 0 && !mScale) {
			// Don't scale the image but instead fill it so it's the
			// required dimension
			try {
				croppedImage = Bitmap.createBitmap(mOutputX, mOutputY,
						bmpConfig);
			} catch (OutOfMemoryError e) {
				// 创建失败
				e.printStackTrace();
				OutOfMemoryHandler.handle();
				// Release bitmap memory as soon as possible
				mImageView.clear();
				mBitmap.recycle();
				mBitmap = null;

				setResult(RESULT_CANCELED);
				finish();
				return;
			}

			Canvas canvas = new Canvas(croppedImage);
			Rect srcRect = mCrop.getCropRect();
			Rect dstRect = new Rect(0, 0, mOutputX, mOutputY);

			int dx = (srcRect.width() - dstRect.width()) / 2;
			int dy = (srcRect.height() - dstRect.height()) / 2;

			// If the srcRect is too big, use the center part of it.
			srcRect.inset(Math.max(0, dx), Math.max(0, dy));

			// If the dstRect is too big, use the center part of it.
			dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

			// Draw the cropped bitmap in the center
			canvas.drawBitmap(mBitmap, srcRect, dstRect, null);

			// Release bitmap memory as soon as possible
			mImageView.clear();
			mBitmap.recycle();
			mBitmap = null;
		} else {
			Rect r = mCrop.getCropRect();

			try {
				croppedImage = Bitmap.createBitmap(mOutputX, mOutputY,
						bmpConfig);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				OutOfMemoryHandler.handle();

				mImageView.clear();
				mBitmap.recycle();
				mBitmap = null;

				setResult(RESULT_CANCELED);
				finish();
				return;
			}

			Canvas canvas = new Canvas(croppedImage);
			canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
					Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG)); // 在有缩放的情况下需要提高绘图质量
			Rect dstRect = new Rect(0, 0, mOutputX, mOutputY);
			canvas.drawBitmap(mBitmap, r, dstRect, null);

			mImageView.clear();
			mBitmap.recycle();
			mBitmap = null;
		}

		mImageView.setImageBitmapResetBase(croppedImage, true);
		mImageView.center(true, true);
		mImageView.mHighlightViews.clear();

		// Return the cropped image directly or save it to the specified URI.
		Bundle myExtras = getIntent().getExtras();
		if (myExtras != null
				&& (myExtras.getParcelable("data") != null || myExtras
						.getBoolean("return-data"))) {
			Bundle extras = new Bundle();
			extras.putParcelable("data", croppedImage);
			setResult(RESULT_OK, (new Intent()).setAction("inline-data")
					.putExtras(extras));
			finish();
		} else {
			final Bitmap b = croppedImage;
			if (b != null && !b.isRecycled()) {

			} else {

			}
			// saveBitmap2file(b, mSaveUri.getPath());
			startBackgroundJob(this, null,
					getResources().getString(R.string.savingImage),
					new Runnable() {
						@Override
						public void run() {
							// saveOutput(b);
							saveBitmap2file(b, mSaveUri.getPath());
						}
					}, mHandler);
		}
	}

	private void startBackgroundJob(MonitoredActivity activity, String title,
			String message, Runnable job, Handler handler) {
		// Make the progress dialog uncancelable, so that we can gurantee
		// the thread will be done before the activity getting destroyed.
		ProgressDialog dialog = ProgressDialog.show(activity, title, message,
				true, false);
		new Thread(new BackgroundJob(activity, job, dialog, handler)).start();
	}

	/**
	 * <br>
	 * 类描述: <br>
	 * 功能详细描述:
	 * 
	 * @author huanglun
	 * @date [2012-10-13]
	 */
	private class BackgroundJob extends MonitoredActivity.LifeCycleAdapter
			implements Runnable {

		private final MonitoredActivity mActivity;
		private final ProgressDialog mDialog;
		private final Runnable mJob;
		private final Handler mHandler;
		private final Runnable mCleanupRunner = new Runnable() {
			@Override
			public void run() {
				mActivity.removeLifeCycleListener(BackgroundJob.this);

				if (mDialog.getWindow() != null) {
					mDialog.dismiss();
				}
			}
		};

		public BackgroundJob(MonitoredActivity activity, Runnable job,
				ProgressDialog dialog, Handler handler) {
			mActivity = activity;
			mDialog = dialog;
			mJob = job;
			mActivity.addLifeCycleListener(this);
			mHandler = handler;
		}

		@Override
		public void run() {
			try {
				mJob.run();
			} finally {
				mHandler.post(mCleanupRunner);
			}
		}

		@Override
		public void onActivityDestroyed(MonitoredActivity activity) {
			// We get here only when the onDestroyed being called before
			// the mCleanupRunner. So, run it now and remove it from the queue
			mCleanupRunner.run();
			mHandler.removeCallbacks(mCleanupRunner);
		}

		@Override
		public void onActivityStopped(MonitoredActivity activity) {
			mDialog.hide();
		}

		@Override
		public void onActivityStarted(MonitoredActivity activity) {
			mDialog.show();
		}
	}

	public void deleteFile1(String file) {
		try {
			File f = new File(file);
			if (f.exists()) {
				f.delete();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private boolean saveBitmap2file(Bitmap bitmap, String filename) {
		deleteFile1(filename);
		File file = new File(filename);
		/*File file = new File(getFilesDir()
				+ filename.substring(filename.lastIndexOf("/")));*/
		FileOutputStream stream = null;
		boolean b = false;
		try {
			file.createNewFile();
			openFileOutput(file.getName(), Context.MODE_WORLD_READABLE
					+ Context.MODE_WORLD_WRITEABLE);
			stream = new FileOutputStream(file);
			b = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				stream.flush();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Bundle extras = new Bundle();
		// 将保存的路径再次返回给调用者
		setResult(RESULT_OK,
				new Intent(mSaveUri.getPath().toString()).putExtras(extras));
		final Bitmap b1 = bitmap;
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mImageView.clear();
				b1.recycle();
			}
		});

		finish();
		return b;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (null != mImageView) {
			mImageView.clear();
		}

		if (null != mBitmap && !mBitmap.isRecycled()) {
			mBitmap.recycle();
			mBitmap = null;
		}
	}

	private void makeDefault() {
		HighlightView hv = new HighlightView(mImageView);

		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();

		Rect imageRect = new Rect(0, 0, width, height);

		// make the default size about 4/5 of the width or height
		if (mCircleCrop) {
			width /= 2;
			height /= 2;
		}
		int cropWidth = width;
		int cropHeight = height;

		if (mAspectX != 0) {
			if (mAspectX * height > width) {
				cropHeight = (int) (cropWidth / mAspectX);
			} else {
				cropWidth = (int) (cropHeight * mAspectX);
			}
		}

		int x = (width - cropWidth) / 2;
		int y = (height - cropHeight) / 2;

		if (mCircleCrop) {
			x = width / 2;
			y = height / 2;
		}

		RectF cropRect = null;
		if (mCircleCrop) {
			cropRect = new RectF(x, y, x + cropWidth, y + cropWidth);
		} else {
			cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
		}
		hv.setup(null, imageRect, cropRect, mCircleCrop, mAspectX != 0);
		mImageView.add(hv);
		hv.setResizeDrawable(mResizeDrawableWidth, mResizeDrawableHeight,
				mResizeDrawableDiagonal);
	}
}
