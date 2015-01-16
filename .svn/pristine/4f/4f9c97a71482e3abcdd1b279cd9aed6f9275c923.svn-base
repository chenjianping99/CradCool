package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 
 * <br>功能详细描述:从SwitchWidget抽离调整可以使用的代码
 * 
 * @date  [2013-1-5]
 */
public class ToastActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null) {
			int extra = intent.getIntExtra("stringId", -1);
			Toast.makeText(this, extra,
					Toast.LENGTH_SHORT).show();
		}
		finish();
	}
}
