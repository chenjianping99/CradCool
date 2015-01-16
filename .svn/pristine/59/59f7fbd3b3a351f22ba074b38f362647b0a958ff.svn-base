package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * 设置文本字体，字号
 * 
 * @author chenjianping
 * 
 */
public class CustomTextView extends TextView {

	public CustomTextView(Context context) {
		super(context);
	}

	private void init() {
		//setTextColor(Color.RED);
	}

	public void setTextSize(int size) {
		setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getPXByHeight(size));
		setEllipsize(TextUtils.TruncateAt.valueOf("END"));
		//setSingleLine(true);
	}

}
