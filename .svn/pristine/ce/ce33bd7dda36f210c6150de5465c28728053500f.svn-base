package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.jiubang.goscreenlock.theme.cjpcardcool.R;
import com.jiubang.goscreenlock.theme.cjpcardcool.crop.CropImageActivity;

/**
 * 
 * @author chenjianping
 *
 */
public class GetPictureActivity extends Activity {
	public static final String[] BGNAME = {"/bg1.jpg", "/bg2.jpg", "/bg3.jpg", "/bg4.jpg", "/bg5.jpg", "/bg6.jpg"};
	public static final int RAINNY_INDEX = 0;
	public static final int CLOUDY_INDEX = 1;
	public static final int SUNNY_INDEX = 2;
	public static final int FOGY_INDEX = 3;
	public static final int SNOWY_INDEX = 4;
	public static final int MUSIC_INDEX = 5;
	public static final String INDEX = "index";
	private int mIndex = 0;
	public static int sCustomBgNum = 0;
	//private Uri mUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
		mIndex = getIntent().getIntExtra(INDEX, 0);

		backgroundSelect();
	}

	/**
	 * 背景更换
	 */
	private static final int REQUESTCODE_CUSTOMBG_BG = 1001;
	
	public static Uri[] sCUSTOMBG_URI_BGs = new Uri[BGNAME.length];
	private static int[] sREQUESTCODE_CUSTOMBG_CROP_BGs =  new int[BGNAME.length];
	private void initData() {
		for (int i = 0; i < sCUSTOMBG_URI_BGs.length; i++) {
			sCUSTOMBG_URI_BGs[i] = Uri.parse("file:///data/data/"
					+ Constant.THEME_PACKAGE_NAME + "/files"
					+ BGNAME[i]);
			sREQUESTCODE_CUSTOMBG_CROP_BGs[i] = 10000 + i;
		}
	}
	
	
	private int mScreenWidth = 720;
	private int mScreenHeight = 1280;

	public void backgroundSelect() {
		DisplayMetrics mDm = this.getResources().getDisplayMetrics();
		mScreenWidth = mDm.widthPixels;
		mScreenHeight = mDm.heightPixels;
		
		if (mIndex == MUSIC_INDEX) {
			float scale = mScreenWidth / 720f;
			mScreenWidth = (int) (521 * scale);
			mScreenHeight = (int) (578 * scale);
		}

		Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
		intent2.setType("image/*");
		intent2.putExtra(INDEX, mIndex);
		try {
			startActivityForResult(intent2, REQUESTCODE_CUSTOMBG_BG);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		//Log.d("cjp", "onActivityResult");
		if (requestCode == REQUESTCODE_CUSTOMBG_BG && resultCode == RESULT_OK) {
			Intent intent = getCropImageIntent(data, sCUSTOMBG_URI_BGs[mIndex],
					mScreenWidth, mScreenHeight);
			if (intent != null) {
				startActivityForResult(intent, sREQUESTCODE_CUSTOMBG_CROP_BGs[mIndex]);
				Log.d("cjp", "mHeadUri 84");
			}
		} 
		else {
			Log.d("cjp", " else finish 94...");
			gotoLockerScreen();
			finish();
		}
	}
	
	void gotoLockerScreen() {
		Intent intent = new Intent("com.jiubang.intent.action.LAUNCHER_LOCK");
		sendBroadcast(intent);
	}

	private Intent getCropImageIntent(Intent intent, Uri output, int w, int h) {
		if (intent == null) {
			return null;
		}
		Intent cropIntent = new Intent(this, CropImageActivity.class);
		float proportionWH = (float) mScreenWidth / mScreenHeight;
		// 获取数据
		Uri srcUri = intent.getData();
		cropIntent.setData(srcUri); // 源图片的Uri
		cropIntent.putExtra("output", output); // 编辑后图片输出路径的Uri
		cropIntent.putExtra("outputFormat",
				Bitmap.CompressFormat.JPEG.toString()); // 编辑后图片的保存格式，默认是75%质量的jpg
		cropIntent.putExtra("scale", true); // 支持缩放，默认为true
		cropIntent.putExtra("aspectX", proportionWH); // 裁剪框的纵横比
		cropIntent.putExtra("outputX", mScreenWidth); // 保存的图片的宽度
		cropIntent.putExtra("outputY", mScreenHeight); // 保存的图片的高度，最好满足裁剪框的纵横比
		cropIntent.putExtra(INDEX, mIndex);
		cropIntent.putExtra("arrowHorizontal", R.drawable.camera_crop_width); // 水平方向的箭头的资源id，非必需
		cropIntent.putExtra("arrowVertical", R.drawable.camera_crop_height); // 垂直方向的箭头的资源id，非必需
		return cropIntent;
	}
}
