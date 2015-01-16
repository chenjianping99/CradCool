package com.jiubang.goscreenlock.theme.cjpcardcool.guide;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.jiubang.goscreenlock.theme.cjpcardcool.ComponentUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.Constants;
import com.jiubang.goscreenlock.theme.cjpcardcool.GOScreenLockInstallDialog;
import com.jiubang.goscreenlock.theme.cjpcardcool.PreviewTheme;
import com.jiubang.goscreenlock.theme.cjpcardcool.R;

/**
 * 主题滑动界面
 * 
 * @author zhangfanghua
 *
 */
public class ViewPageActivity extends Activity implements OnTouchListener, OnPageChangeListener {

	// implements android.view.View.OnClickListener
	private ViewPager mViewPager;
	private ViewPageAdapter mPageAdapter;
	private List<View> mListViews;
	private Button mDownloadBtn;
	private Button mDownloadWithPreviewBtn;
	private Button mExpercienceBtn;

	private ImageView[] mImageViews;
	// 点的个数
	private int mViewCount;
	// 总共的preview的页面数量
	private int mViewCountVirtual;
	// 当前点点的位置
	private int mCurSel;
	private String[] mPreImgNameList;

	private Boolean mTouchMove = false;
	private final int mChangeUi = 0x004;
	private int mCount = 0;
	private ArrayList<Bitmap> mPreviewBitmapList = null;

	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case mChangeUi :
					// change preview
					changPreview();
					break;
				default :
					break;
			}
		}
	};

	/**
	 * 自动滑动
	 */
	private void changPreview() {
		if (0 <= mCount && mCount <= mViewCountVirtual) {

			setCurView(mCount);
			setCurPoint(mCount % mViewCount);
			mCount++;
			if (mCount == mViewCountVirtual || mCount == 0) {
				// mCount = 0;
				mTouchMove = true;
			}
		}
	}

	/**
	 * 
	 * <br>
	 * 类描述: <br>
	 * 功能详细描述:
	 * 
	 * @author linhang
	 * @date [2012-10-13]
	 */
	class RefreshRunner implements Runnable {
		public void run() {

			while (!mTouchMove && !Thread.currentThread().isInterrupted()) {
				try {

					Message message = new Message();
					message.what = mChangeUi;
					myHandler.sendMessage(message);
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}

	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_main);
		
		initViewPage();
		init();
		new Thread(new RefreshRunner()).start();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	/**
	 * 用于引导页面初始化，在arrays_packagefiles.xml中配置preview
	 */
	private void initViewPage() {
		mListViews = new ArrayList<View>();
		LayoutInflater mLayoutInflater = getLayoutInflater();
		
		mPreviewBitmapList = ComponentUtils.getPreviewBitmaps(getApplicationContext());
		
		if (mPreviewBitmapList == null) {
			// 不显示点点
			mViewCount = 0;
			mViewCountVirtual = 1;
		}
		else
		{
			mViewCountVirtual = mPreviewBitmapList.size();
			mViewCount = mPreviewBitmapList.size();
		}

		int index = 0;

		if (mViewCountVirtual <= 1)
		{
			View view = mLayoutInflater.inflate(R.layout.guide_viewitem, null);
			ImageView imageview = (ImageView) view.findViewById(R.id.preview);
			imageview.setBackgroundResource(R.drawable.bg);
			imageview.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					GOScreenLockInstallDialog.disPatchDownLoad(ViewPageActivity.this);
				}
			});
			mListViews.add(view);
		}
		else
		{
			for (int i = 0; i < mViewCountVirtual; i++) {
				View view = mLayoutInflater.inflate(R.layout.guide_viewitem, null);
				ImageView imageview = (ImageView) view.findViewById(R.id.preview);
				imageview.setImageBitmap(mPreviewBitmapList.get(index));
				imageview.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						GOScreenLockInstallDialog.disPatchDownLoad(ViewPageActivity.this);
					}
				});
				mListViews.add(view);

				index++;
				if (index >= mPreviewBitmapList.size()) {
					index = 0;
				}
			}
		}
		
		mPageAdapter = new ViewPageAdapter(mListViews);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(mPageAdapter);
		mViewPager.setOnTouchListener(this);
		mViewPager.setOnPageChangeListener(this);

		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			// mScroller.setmDuration(mMyDuration);
			Scroller scroller = new FixedSpeedScroller(mViewPager.getContext(),
					new AccelerateInterpolator());
			field.set(mViewPager, scroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将图片名称转换成ResourceId
	 * @param ss
	 * @param res
	 * @return
	 */
	private ArrayList<Integer> names2Ids(String[] ss, Resources res) {

		ArrayList<Integer> list = null;
		if (ss.length > 0) {
			list = new ArrayList<Integer>();
			for (int i = 0; i < ss.length; i++) {
				int id = res.getIdentifier(ss[i], // 资源名称的字符串数组
						"drawable", // 资源类型
						getPackageName()); // R类所在的包名
				list.add(id);
			}
		}

		return list;
	}
	
	/**
	 * 获取预览图片的resourceId
	 * @param resourceId
	 */
	private void getPreview(int resourceId)
	{
		mPreImgNameList = getResources().getStringArray(resourceId);
	}

	/**
	 * 动态生成点的ImageView
	 * @return
	 */
	private ImageView getImageView()
	{
		ImageView point = new ImageView(getApplicationContext());
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_VERTICAL;
		point.setClickable(true);
		point.setPadding(5, 5, 5, 5);
		point.setImageResource(R.drawable.guide_round);
		point.setLayoutParams(params);
		return point;
	}
	
	/**
	 * 界面元素初始化
	 */
	private void init() {
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.theme_Bg);
		BitmapDrawable drawable = (BitmapDrawable) layout.getBackground();
		drawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);

		// 点点
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llayout);
		mImageViews = new ImageView[mViewCount];

		for (int i = 0; i < mViewCount; i++) {
			mImageViews[i] = getImageView();
			linearLayout.addView(mImageViews[i]);
			mImageViews[i].setEnabled(true);
			mImageViews[i].setTag(i);
		}

		mCurSel = 0;
		mImageViews[mCurSel].setEnabled(false);

		mDownloadWithPreviewBtn = (Button) layout.findViewById(R.id.download_with_preview);
		mDownloadBtn = (Button) layout.findViewById(R.id.download);
		mExpercienceBtn = (Button) layout.findViewById(R.id.experience_theme);

		boolean isNeedDisplay = isNeedDisplayExpercienceView();
		Log.d("zfh", "isNeedDisplay:  " + isNeedDisplay);
		if (isNeedDisplay)
		{
			mDownloadBtn.setVisibility(View.GONE);
			mDownloadWithPreviewBtn.setVisibility(View.VISIBLE);
			mExpercienceBtn.setVisibility(View.VISIBLE);
			
		}
		else
		{
			mDownloadWithPreviewBtn.setVisibility(View.GONE);
			mDownloadBtn.setVisibility(View.VISIBLE);
			mExpercienceBtn.setVisibility(View.GONE);
		}
		
		mDownloadWithPreviewBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO
				GOScreenLockInstallDialog.disPatchDownLoad(ViewPageActivity.this);
				finish();
			}
		});
		
		mDownloadBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO
				GOScreenLockInstallDialog.disPatchDownLoad(ViewPageActivity.this);
				finish();
			}
		});
		
		mExpercienceBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ViewPageActivity.this, PreviewTheme.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

	private boolean isNeedDisplayExpercienceView()
	{
		int isAppId = getResources().getIdentifier("isapp", "string", getPackageName());
		String isApp = null;
		if (isAppId != 0)
		{
			isApp = getResources().getString(isAppId);
			if (isApp == null || !isApp.equals("1"))
			{
				return false;
			}
			int isDisplayId = getResources().getIdentifier("ispreview_theme", "string", getPackageName());
			if (isDisplayId != 0)
			{
				String isDisplay = getResources().getString(isDisplayId);
				if (isDisplay != null && isDisplay.equals("1"))
				{
					return true;
				}
				return false;
			}
		}
		
		return false;
	}
	
	/**
	 * 设置当前界面
	 * @param pos
	 */
	private void setCurView(int pos) {
		if (pos < 0 || pos >= mViewCountVirtual) {
			return;
		}
		mViewPager.setCurrentItem(pos);
	}

	/**
	 * 当前点点的位置
	 * @param index
	 */
	private void setCurPoint(int index) {
		if (index < 0 || index > mViewCountVirtual - 1 || mCurSel == index) {
			return;
		}
		mImageViews[mCurSel].setEnabled(true);
		mImageViews[index % mViewCount].setEnabled(false);
		mCurSel = index % mViewCount;

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setCurPoint(arg0 % mViewCount);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (ComponentUtils.isNeedBoardcast(getApplicationContext()))
		{
			ComponentUtils.scheduleNextCheck(getApplicationContext(),
					Constants.TWENTY_MINUTES,
					Constants.ACTION_LAUNCHER_AD_FIRST);
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		final int action = event.getAction();
		if (MotionEvent.ACTION_MOVE == action) {
			mTouchMove = true;
		}
		return false;
	}
}
