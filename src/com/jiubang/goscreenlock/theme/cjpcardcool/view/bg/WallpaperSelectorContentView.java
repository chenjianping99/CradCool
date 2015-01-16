package com.jiubang.goscreenlock.theme.cjpcardcool.view.bg;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubang.goscreenlock.theme.cjpcardcool.ThemeSetProvider;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.OutOfMemoryHandler;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.CircleContainer;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ViewUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.ILocker.OnDestroyListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.R;

/**
 * <br>
 * 类描述: 自定义随机背景activity <br>
 * 功能详细描述:
 * 
 * @author xuqian
 * @date [2012-9-10]
 */
public class WallpaperSelectorContentView extends FrameLayout implements OnClickListener, OnDestroyListener {
	/*private ImageView mPreviousImage;
	private ImageView mResetImage;*/

	private LinkedHashMap<Object, Boolean> mSelectImagesMap = null;
	private ArrayList<ImageItem> mImageItemList = null;
	private GridView mGridView = null;
	private ImageAdapter mImageaAdapter = null;
	private Handler mHandler = null;
	private HandlerThread mHandlerThread = null;
	// 进入界面时加载图片结束的标志
	private final static int FINISH_LOAD = 0;
	// 保存进SettingData的字符串数组中,字符串前三位为锁屏背景与自定义背景的区分标志
	private LayoutInflater mLayoutInflater;
	private FrameLayout mParentView;
	private boolean mIsNeedUpdate = false;
	private CircleContainer mCircleContainer;
	public WallpaperSelectorContentView(Context context, CircleContainer v) {
		super(context);
		mCircleContainer = v;
		
		addHeader(context);
		initData();
	}
	
	private ImageView mBackIcon, mResetIcon;
	private int mHeaderH = ViewUtils.getPXByHeight(150);
	private void addHeader(Context context) {
		FrameLayout ly = new FrameLayout(context);
		FrameLayout.LayoutParams themeParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				mHeaderH, Gravity.TOP);
		addView(ly, themeParams);
		ly.setBackgroundColor(0x44000000);
		
		TextView textName = new TextView(getContext());
		textName.setTextColor(Color.WHITE);
		//textName.setTypeface(CircleContainer.sTypeface);
		textName.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByWidth(48));
		LayoutParams mDateTextLP = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		textName.setText("Wallpaper");
		ly.addView(textName, mDateTextLP);
		
		mBackIcon = new ImageView(getContext());
		mBackIcon.setImageResource(R.drawable.pre);
		int w = ViewUtils.getPXByWidth(72);
		FrameLayout.LayoutParams deviderParams = new FrameLayout.LayoutParams(w, w, 
				Gravity.CENTER_VERTICAL | Gravity.LEFT);
		deviderParams.leftMargin = ViewUtils.getPXByWidth(20);
		ly.addView(mBackIcon, deviderParams);
		
		mResetIcon = new ImageView(getContext());
		mResetIcon.setImageResource(R.drawable.reset);
		FrameLayout.LayoutParams mResetIconParams = new FrameLayout.LayoutParams(w, w, 
				Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		mResetIconParams.rightMargin = deviderParams.leftMargin;
		ly.addView(mResetIcon, mResetIconParams);
		
		mBackIcon.setOnClickListener(this);
		mResetIcon.setOnClickListener(this);
	}

	/*private void addTopView(Context context) {
		TextView settings = new TextView(context);
		settings.setTextColor(0xa0ffffff);
		settings.setText("You can select one or more picture as wallpaper");
		settings.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByWidth(28));

		FrameLayout.LayoutParams settingsParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		settingsParams.topMargin = ViewUtils.getPXByHeight(160);
		addView(settings, settingsParams);

		mPreviousImage = new ImageView(context);
		mPreviousImage.setImageResource(R.drawable.icon);
		mPreviousImage.setOnClickListener(this);
		FrameLayout.LayoutParams previousParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.LEFT);
		previousParams.topMargin = ViewUtils.getPXByHeight(30);
		previousParams.leftMargin = ViewUtils.getPXByWidth(20);
		addView(mPreviousImage, previousParams);

		mResetImage = new ImageView(context);
		mResetImage.setImageResource(R.drawable.icon);
		mResetImage.setOnClickListener(this);
		FrameLayout.LayoutParams resetParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.RIGHT);
		resetParams.topMargin = ViewUtils.getPXByHeight(30);
		resetParams.rightMargin = ViewUtils.getPXByWidth(20);
		addView(mResetImage, resetParams);
	}*/

	/**
	 * 
	 * <br>
	 * 类描述: 图片信息类 <br>
	 * 功能详细描述:
	 * 
	 * @author xuqian
	 * @date [2012-9-10]
	 */
	private class ImageItem {
		int mImageResId;
		Resources mResource;
		// 是否自定义
		boolean mIsCustom;
		String mFilePath;
		// 是否已被选择
		boolean mIsSelected;
	}

	public void playGridViewChildsAnimation() {
		int offset = 0;
		for (int position = 0; position < mVisibleItemCount; position++) {
			int index = mFirstViisbleItem + position;
			View v = mGridView.findViewById(1001 + index);
			if (v != null) {
				Animation a = new AlphaAnimation(0, 1);
				a.setDuration(400);
				a.setStartOffset(offset * 150);
				v.startAnimation(a);
				offset++;
			}
		}
	}

	private void initData() {
		mLayoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 自定义图片保存时的文件名
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case FINISH_LOAD:
					mParentView = (FrameLayout) mLayoutInflater.inflate(R.layout.wallpaper_selector, null);
					FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
					params.topMargin = mHeaderH;
					addView(mParentView, params);
					init();
					break;
				default:
					break;
				}
			}
		};

		mHandlerThread = new HandlerThread("loading");
		mHandlerThread.start();

		Handler loadHandler = new Handler(mHandlerThread.getLooper());

		Runnable r = new Runnable() {
			@Override
			public void run() {
				// 加载主题背景图片
				loadDrawables();
				// 加载自定义图片
				try {
					loadCustomBg();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 判断图片是否已被选择
				checkIfSelected();
				if (mHandler != null) {
					mHandler.sendEmptyMessage(FINISH_LOAD);
				}
			}
		};

		loadHandler.post(r);
	}

	/**
	 * <br>
	 * 功能简述: 读取哪些主题被选择了,打勾 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	public void checkIfSelected() {
		ThemeSetProvider.getSetting(getContext());
		String[] strings = ThemeSetProvider.sBgIndexs.split(",");
		int[] ints = new int[strings.length];
		for (int i = 0; i < strings.length; ++i) {
			try {
				ints[i] = Integer.parseInt(strings[i]);
			} catch (NumberFormatException e) {
				ints[i] = -1;
			}
		}
		for (int i = 0; i < ints.length; ++i) {
			int index = ints[i];
			if (index >= 0 && index < mImageItemList.size()) {
				mImageItemList.get(index).mIsSelected = true;
			}
		}

		mSelectImagesMap = new LinkedHashMap<Object, Boolean>();
		LogUtils.log(null, "checkIfSelected: ThemeSetProvider.sBgIndexs =" + ThemeSetProvider.sBgIndexs);
		if (ThemeSetProvider.sBgIndexs.equals("")) {
			for (int i = 0; i < BG_RES.length; i++) {
				mSelectImagesMap.put(i, true);
				mImageItemList.get(i).mIsSelected = true;
			}
			// need modified
			ThemeSetProvider.sBgIndexs = "0,1,2";
			ThemeSetProvider.sBgPath = "null,null,null";
			ThemeSetProvider.saveSetting(getContext());
		}
	}

	private void init() {
		mGridView = (GridView) findViewById(R.id.gallery_custom_random); 

		mImageaAdapter = new ImageAdapter(getContext());
		mGridView.setAdapter(mImageaAdapter);
		mGridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {

				if (firstVisibleItem == mFirstViisbleItem && visibleItemCount == mVisibleItemCount) {
					return;
				}
				int offset = 0;
				for (int position = 0; position < visibleItemCount; position++) {
					int index = firstVisibleItem + position;
					if (index >= mFirstViisbleItem && index < mFirstViisbleItem + mVisibleItemCount) {
						continue;
					}
					View v = view.findViewById(1001 + index);
					Animation a = new AlphaAnimation(0, 1);
					a.setDuration(500);
					a.setStartOffset(offset * 100);
					v.startAnimation(a);
					offset++;
				}
				mFirstViisbleItem = firstVisibleItem;
				mVisibleItemCount = visibleItemCount;
			}
		});

	}

	private int mFirstViisbleItem = 0;
	private int mVisibleItemCount = 0;

	/**
	 * <br>
	 * 功能简述: 在文件夹中获得图片路径 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	private void loadCustomBg() {
		if (mImageItemList == null) {
			mImageItemList = new ArrayList<ImageItem>();
		}
		// File file = new File(BackgroundActivity.CUSTOMBG_URI_TOP.getPath());
		File file = new File(LockBgType.CUSTOM_RANDOM_BG_DIR);
		LogUtils.log(null, "file " + file.exists());
		if (!file.exists()) {
			file.mkdirs();
		} else {
			File[] files = file.listFiles();
			if (files.length > 0) {
				// 把文件传入FileSort类中
				LogUtils.log(null, "file " + file.exists());
				FileSort[] fileSorts = new FileSort[files.length];
				for (int i = 0; i < files.length; i++) {
					fileSorts[i] = new FileSort(files[i]);
				}
				// 排序
				Arrays.sort(fileSorts);
				// 排序完把文件放回files中
				files = new File[files.length];
				for (int i = 0; i < files.length; i++) {
					files[i] = fileSorts[i].getFile();
				}
				for (File f : files) {
					ImageItem item = new ImageItem();
					item.mIsCustom = true;
					item.mFilePath = f.getPath();
					mImageItemList.add(item);
				}
			}
		}
	}

	/**
	 * 
	 * <br>
	 * 类描述: gallery的adapter <br>
	 * 功能详细描述:
	 * 
	 * @author xuqian
	 * @date [2012-9-10]
	 */
	private class ImageAdapter extends BaseAdapter implements OnClickListener, OnDestroyListener {
		private Bitmap mTemp = null;
		private BitmapCache mBitmapCache = null;
		private AsyncImageLoader mAsyncImageLoader;

		ImageAdapter(Context context) {
			mBitmapCache = BitmapCache.getInstance();
			mAsyncImageLoader = new AsyncImageLoader();
		}

		@Override
		public int getCount() {
			return mImageItemList.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return mImageItemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(R.layout.defaulttheme_wallpaper_item, null);
			}
			convertView.setId(1001 + position);

			if (position == mImageItemList.size()) {
				convertView.findViewById(R.id.item_layout).setVisibility(View.GONE);
				convertView.findViewById(R.id.image_add_layout).setVisibility(View.VISIBLE);
				View add = convertView.findViewById(R.id.image_add);
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 自定义背景图跳转
						if (LockBgType.getSDPath() == null) {
							Toast.makeText(getContext(), "Can not open SdCard!", Toast.LENGTH_SHORT).show();
							return;
						} else {
							//updateSelectedInfo();
							Intent intent = new Intent(getContext(), BackgroundActivity.class);
							intent.putExtra("sIsfullscreen", Constant.sIsfullscreen);
							intent.putExtra("imgId", position);
							intent.putExtra("index", ThemeSetProvider.sBgIndexs);
							intent.putExtra("path", ThemeSetProvider.sBgPath);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							Global.sendUnlockWithIntent(getContext(), null, null, null, intent);
						}
					}
				});
				return convertView;
			} else {
				convertView.findViewById(R.id.item_layout).setVisibility(View.VISIBLE);
				convertView.findViewById(R.id.image_add_layout).setVisibility(View.GONE);
			}
			ImageItem imageItem = mImageItemList.get(position);
			ImageView contentImageView = (ImageView) convertView.findViewById(R.id.image_content);
			FrameLayout chooseImageView = (FrameLayout) convertView.findViewById(R.id.image_choose);
			ImageView delect = (ImageView) convertView.findViewById(R.id.image_delete);

			// 只对自定义图片出现删除按钮
			if (!imageItem.mIsCustom) {
				delect.setVisibility(View.GONE);
			} else {
				Boolean selectedObj = mSelectImagesMap.get(position);
				boolean selected = selectedObj != null ? selectedObj : false;
				if (imageItem.mIsSelected || selected) {
					delect.setVisibility(View.GONE);
				} else {
					delect.setVisibility(View.VISIBLE);
				}
			}

			// 判断是否选中该图片
			if (mSelectImagesMap.containsKey(position) && mSelectImagesMap.get(position)) {
				chooseImageView.setVisibility(View.VISIBLE);
			} else if (imageItem.mIsSelected) {
				imageItem.mIsSelected = false;
				mSelectImagesMap.put(position, true);
				LogUtils.log(null, "getView: true position = " + position);
				
				chooseImageView.setVisibility(View.VISIBLE);
			} else {
				mSelectImagesMap.put(position, false);
				chooseImageView.setVisibility(View.GONE);
				LogUtils.log(null, "getView: false position = " + position);
			}

			contentImageView.setTag(imageItem);
			Bitmap cachedImage = mAsyncImageLoader.loadDrawable(imageItem, new ImageCallback() {
				public void imageLoaded(Bitmap imageDrawable, ImageItem imageUrl) {
					ImageView imageViewByTag = (ImageView) mGridView.findViewWithTag(imageUrl);
					if (imageViewByTag != null) {
						imageViewByTag.setImageBitmap(imageDrawable);
					}
				}
			});
			contentImageView.setImageBitmap(cachedImage);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (mSelectImagesMap.containsKey(position)) {
						LogUtils.log(null, "Onclick: position = " + position);
						ImageItem imageItem = mImageItemList.get(position);
						ImageView delect = (ImageView) v.findViewById(R.id.image_delete);
						LogUtils.log(null, "Before Onclick: position Value = " + mSelectImagesMap.get(position));
						if (!mSelectImagesMap.get(position)) {
							mSelectImagesMap.put(position, true);
							mImageItemList.get(position).mIsSelected = true;
							View choose = v.findViewById(R.id.image_choose);
							choose.setVisibility(View.VISIBLE);
							delect.setVisibility(View.GONE);
						} else {
							mSelectImagesMap.put(position, false);
							mImageItemList.get(position).mIsSelected = false;
							View choose = v.findViewById(R.id.image_choose);
							choose.setVisibility(View.GONE);
							if (imageItem.mIsCustom) {
								delect.setVisibility(View.VISIBLE);
							}
						}
						LogUtils.log(null, "After Onclick: position Value = " + mSelectImagesMap.get(position));
						mIsNeedUpdate = true;
						//updateSelectedInfo();
					}
				}
			});

			delect.setOnClickListener(this);
			delect.setTag(position);
			return convertView;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.image_delete:
				try {
					deleteCustomImage((Integer) v.getTag());
					//updateSelectedInfo();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}

		@Override
		public void onDestroy() {
			if (mTemp != null && !mTemp.isRecycled()) {
				mTemp.recycle();
				mTemp = null;
			}
			
			mBitmapCache.clearCache();
		}

		/**
		 * 
		 * @author shenyaobin
		 * 
		 */
		private class AsyncImageLoader {

			private HashMap<ImageItem, SoftReference<Bitmap>> mImageCache;

			public AsyncImageLoader() {
				mImageCache = new HashMap<ImageItem, SoftReference<Bitmap>>();
			}

			public Bitmap loadDrawable(final ImageItem item, final ImageCallback imageCallback) {
				if (mImageCache.containsKey(item)) {
					SoftReference<Bitmap> softReference = mImageCache.get(item);
					Bitmap drawable = softReference.get();
					if (drawable != null) {
						return drawable;
					}
				}

				final Handler handler = new Handler() {
					public void handleMessage(Message message) {
						imageCallback.imageLoaded((Bitmap) message.obj, item);
					}
				};
				new Thread() {
					@Override
					public void run() {
						Bitmap bitmap = null;
						if (!item.mIsCustom) {
							/*
							 * bitmap = BitmapManager.getInstance(getContext()).
							 * getBitmapByResid(item.mResource,
							 * item.mImageResId, Constant.sRealWidth / 3,
							 * Constant.sRealHeight / 3);
							 */
							bitmap = decodeBitmapStreamSafe(item.mResource, item.mImageResId);
						} else {
							bitmap = decodeBitmapStreamSafe(item.mFilePath);
						}
						mImageCache.put(item, new SoftReference<Bitmap>(bitmap));
						Message message = handler.obtainMessage(0, bitmap);
						handler.sendMessage(message);
					}
				}.start();
				return null;
			}
		}
	}

	/**
	 * 
	 * @author shenyaobin
	 * 
	 */
	interface ImageCallback {
		public void imageLoaded(Bitmap imageDrawable, ImageItem item);

	}

	private void deleteCustomImage(int position) {
		ImageItem imageItem = mImageItemList.get(position);
		if (imageItem.mIsCustom) {
			File file = new File(imageItem.mFilePath);
			if (file.exists()) {
				if (file.delete()) {
					mImageItemList.remove(position);
					// 更新保存了选中数据的map
					int customSize = updateSelectImagesMap(position);
					// 更新缓存
					mImageaAdapter.mBitmapCache.updateCache(position, customSize);
					mImageaAdapter.notifyDataSetChanged();
					mGridView.setAdapter(mImageaAdapter);
					if (position > 0) {
						mGridView.setSelection(position - 1);
					}
				}
			}
		}
		mIsNeedUpdate = true;
	}

	public static final int SCALE = 4;

	public static Bitmap decodeBitmapStreamSafe(String pathName) {
		Bitmap bitmap = null;
		boolean bool = true;
		int scale = SCALE;
		Options opt = new Options();
		while (bool) {
			try {
				opt.inSampleSize = scale;
				bitmap = BitmapFactory.decodeFile(pathName, opt);
				bool = false;

				return bitmap;
			} catch (OutOfMemoryError e) {
				// 如果解碼大圖片，出现爆内存，则每次缩放一半
				OutOfMemoryHandler.handle();
				scale *= 2;
				final int maxTimes = 5;
				if (scale > (1 << maxTimes)) {
					// 防止异常死循环
					return bitmap;
				}
			} catch (Throwable e) {
				bool = false;
				return bitmap;
			}
		}
		return bitmap;
	}

	public static Bitmap decodeBitmapStreamSafe(Resources resources, int resId) {
		if (resources == null || resId < 0) {
			return null;
		}

		Bitmap bitmap = null;
		boolean bool = true;
		int scale = 1;
		Options opt = new Options();
		while (bool) {
			try {
				opt.inSampleSize = scale;
				bitmap = BitmapFactory.decodeResource(resources, resId, opt);
				bool = false;

				return bitmap;
			} catch (OutOfMemoryError e) {
				// 如果解碼大圖片，出现爆内存，则每次缩放一半
				OutOfMemoryHandler.handle();
				scale *= 2;
				final int maxTimes = 5;
				if (scale > (1 << maxTimes)) {
					// 防止异常死循环
					return bitmap;
				}
			} catch (Throwable e) {
				bool = false;
				return bitmap;
			}
		}
		return bitmap;
	}

	/**
	 * <br>
	 * 功能简述: 有自定义图片被删除时,更新选中map的信息 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param position
	 */
	private int updateSelectImagesMap(int position) {
		int customSize = 0;
		for (int i = position; i < mSelectImagesMap.size() - 1; i++) {
			mSelectImagesMap.put(i, mSelectImagesMap.get(i + 1));
			LogUtils.log(null, "updateSelectImagesMap: position = " + i + "Value = " + mSelectImagesMap.get(i + 1));
			customSize++;
		}
		mSelectImagesMap.remove(mSelectImagesMap.size() - 1);
		return customSize;
	}

	@Override
	public void onClick(View v) {
		/*if (v == mPreviousImage) {
			if (mCallBack != null) {
				mCallBack.wallpaperCallBack();
			}
		}*/
		if (v == mResetIcon && mClickable) {
			LogUtils.log(null, "mImageItemList.size() = " + mImageItemList.size());
			int size = mImageItemList.size();
			for (int i = BG_RES.length; i < size; i++) {
				LogUtils.log(null, "i =" + i);
				deleteCustomImage(mImageItemList.size() - 1);
			}
			RotateAnimation rotateAnimation = new RotateAnimation(0, -360f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation.setDuration(400);
			rotateAnimation.setFillAfter(true);
			rotateAnimation.setInterpolator(new DecelerateInterpolator());
			mResetIcon.startAnimation(rotateAnimation);
			rotateAnimation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					mClickable = false;
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					mClickable = true;
				}
			});
			
			//恢复默认壁纸
			{
				for (int i = 0; i < BG_RES.length; i++) {
					mSelectImagesMap.put(i, true);
					mImageItemList.get(i).mIsSelected = true;
				}
				mIsNeedUpdate = true;
				/*ThemeSetProvider.sBgIndexs = "0,1,2";
				ThemeSetProvider.sBgPath = "null,null,null";
				ThemeSetProvider.saveSetting(getContext());*/
			}
			
			//updateSelectedInfo();
			mImageaAdapter.notifyDataSetChanged();
		} else if (v == mBackIcon) {
			updateSelectedInfo();
			if (mCircleContainer != null) {
				//mCircleContainer.menueViewOut();
			}
		}
	}

	private boolean mClickable = true;
	
	private boolean isDefaultWallPaper() {
		ThemeSetProvider.getSetting(getContext());
		if (ThemeSetProvider.sBgIndexs.equalsIgnoreCase("0,1,2")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * <br>
	 * 功能简述: 保存选择的信息到settingdata中 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	private void updateSelectedInfo() {
		if (!mIsNeedUpdate || mSelectImagesMap == null || mImageItemList == null) {
			return;
		}

		String string = "";
		for (Object i : mSelectImagesMap.keySet()) {
			LogUtils.log(null, "updateSelectedInfo: mSelectImagesMap.get(i) =" + mSelectImagesMap.get(i));
			if (mSelectImagesMap.get(i)) {
				if (string != "") {
					string += ",";
				}
				string += i;
			}
		}
		LogUtils.log(null, "updateSelectedInfo: string =" + string);
		ThemeSetProvider.sBgIndexs = "";
		if (!string.equals("")) {
			String[] strings = string.split(",");
			int[] ints = new int[strings.length];
			for (int i = 0; i < strings.length; ++i) {
				try {
					ints[i] = Integer.parseInt(strings[i]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			for (int i = 0; i < ints.length; ++i) {
				if (ThemeSetProvider.sBgIndexs != "") {
					ThemeSetProvider.sBgIndexs += ",";
				}
				ThemeSetProvider.sBgIndexs += ints[i];
			}
		}

		ThemeSetProvider.sBgPath = "";
		for (Object i : mSelectImagesMap.keySet()) {
			if (mSelectImagesMap.get(i)) {
				if (ThemeSetProvider.sBgPath != "") {
					ThemeSetProvider.sBgPath += ",";
				}
				ThemeSetProvider.sBgPath += mImageItemList.get((Integer) i).mFilePath;
			}
		}
		ThemeSetProvider.saveSetting(getContext());
		LogUtils.log(null, "updateSelectedInfo: ThemeSetProvider.sBgPath =" 
		+ ThemeSetProvider.sBgPath + "; ThemeSetProvider.sBgIndexs = " + ThemeSetProvider.sBgIndexs);
		mIsNeedUpdate = false;
	}

	private static final int[] BG_RES = {/*R.drawable.bg1, R.drawable.bg2, 
		R.drawable.bg3*/};
	
	private void loadDrawables() {
		// 添加另一张默认背景
		for (int i = 0; i < BG_RES.length; i++) {
			addDrawables(getResources(), BG_RES[i], null);
		}
	}

	private void addDrawables(Resources resources, int bgId, String filePath) {
		if (resources == null) {
			return;
		}
		if (mImageItemList == null) {
			mImageItemList = new ArrayList<ImageItem>();
		}
		ImageItem imageItem = new ImageItem();
		if (filePath == null) {
			imageItem.mImageResId = bgId;
		} else {
			imageItem.mFilePath = filePath;
		}
		imageItem.mResource = resources;
		mImageItemList.add(imageItem);
	}


	@Override
	public void onDestroy() {
		mCircleContainer = null;
		if (mImageaAdapter != null) {
			mImageaAdapter.onDestroy();
		}
	}
		
}