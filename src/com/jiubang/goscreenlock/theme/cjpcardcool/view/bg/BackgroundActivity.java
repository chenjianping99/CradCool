package com.jiubang.goscreenlock.theme.cjpcardcool.view.bg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.jiubang.goscreenlock.theme.cjpcardcool.ThemeSetProvider;
import com.jiubang.goscreenlock.theme.cjpcardcool.crop.CropImageActivity;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;
import com.jiubang.goscreenlock.theme.cjpcardcool.R;

/**
 * 
 * @author shenyaobin
 * 
 */
public class BackgroundActivity extends Activity {
	private static final int REQUESTCODE_CUSTOMBG_BG = 1001;
	private static final int REQUESTCODE_CUSTOMBG_CROP_BG = 1002;
	// public static final Uri CUSTOMBG_URI_TOP = Uri.parse("file://"
	// + "/data/data/com.jiubang.goscreenlock.theme.urban/files");
	private int mImgId;
	private Uri mSaveUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		boolean sIsfullscreen = false;
		if (intent != null) {
			sIsfullscreen = intent.getBooleanExtra("sIsfullscreen", false);
			ThemeSetProvider.sBgIndexs = intent.getStringExtra("index");
			ThemeSetProvider.sBgPath = intent.getStringExtra("path");
		}

		LogUtils.log(null, "ThemeSetProvider.sBgIndexs = " + ThemeSetProvider.sBgIndexs);
		LogUtils.log(null, "ThemeSetProvider.sBgPath = " + ThemeSetProvider.sBgPath);

		mImgId = intent.getIntExtra("imgId", 0);
		mSaveUri = Uri.parse(LockBgType.CUSTOM_RANDOM_BG_URI + "bg_" + mImgId + ".jpg");
		DisplayMetrics mDm = this.getResources().getDisplayMetrics();
		Global.sScreenWidth = mDm.widthPixels;
		Global.sScreenHeight = mDm.heightPixels;
		Constant.sRealWidth = Global.sScreenWidth;
		if (sIsfullscreen) {
			Constant.sRealHeight = Global.sScreenHeight;
		} else {
			Constant.sRealHeight = Global.sScreenHeight - Constant.getStatusBarHeight(this);
		}

		Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
		intent2.setType("image/*");
		try {
			startActivityForResult(intent2, REQUESTCODE_CUSTOMBG_BG);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUESTCODE_CUSTOMBG_BG && resultCode == RESULT_OK) {

			Intent intent = getCropImageIntent(data, mSaveUri, Constant.sRealWidth, Constant.sRealHeight);
			//ThemeSetProvider.getSetting(getApplicationContext());
			if (intent != null) {

				String pathString = mSaveUri.getPath();

				if (ThemeSetProvider.sBgIndexs.equals("")) {
					ThemeSetProvider.sBgIndexs += mImgId + "";
					ThemeSetProvider.sBgPath += pathString + "";
				} else {
					ThemeSetProvider.sBgIndexs += "," + mImgId;
					ThemeSetProvider.sBgPath += "," + pathString;
				}

				LogUtils.log(null, "custom bg: ThemeSetProvider.sBgPath =" + ThemeSetProvider.sBgPath);
				ThemeSetProvider.saveSetting(getApplicationContext());
				
				startActivityForResult(intent, REQUESTCODE_CUSTOMBG_CROP_BG);
			}
		} /*else if (requestCode == REQUESTCODE_CUSTOMBG_CROP_BG && resultCode == RESULT_OK) {
			finish();
		} */ else {
			gotoLockerScreen();
			finish();
		}
	}

	private void gotoLockerScreen() {
		Intent intent = new Intent("com.jiubang.intent.action.LAUNCHER_LOCK");
		sendBroadcast(intent);
	}

	private Intent getCropImageIntent(Intent intent, Uri output, int w, int h) {
		if (intent == null) {
			return null;
		}

		Intent cropIntent = new Intent(this, CropImageActivity.class);
		int mImageWidth = Constant.sRealWidth;
		int mImageHeight = Global.sScreenHeight;
		//float proportionWH = (float) Constant.sRealWidth / Constant.sRealHeight;
		float proportionWH = mImageWidth * 1.0f / mImageHeight;
		// 获取数据
		Uri srcUri = intent.getData();
		cropIntent.setData(srcUri); // 源图片的Uri
		cropIntent.putExtra("output", output); // 编辑后图片输出路径的Uri
		cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); // 编辑后图片的保存格式，默认是75%质量的jpg
		cropIntent.putExtra("scale", true); // 支持缩放，默认为true
		cropIntent.putExtra("aspectX", proportionWH); // 裁剪框的纵横比
		cropIntent.putExtra("outputX", mImageWidth); // 保存的图片的宽度
		cropIntent.putExtra("outputY", mImageHeight); // 保存的图片的高度，最好满足裁剪框的纵横比

		cropIntent.putExtra("arrowHorizontal", R.drawable.camera_crop_width); // 水平方向的箭头的资源id，非必需
		cropIntent.putExtra("arrowVertical", R.drawable.camera_crop_height); // 垂直方向的箭头的资源id，非必需
		return cropIntent;
	}
}
