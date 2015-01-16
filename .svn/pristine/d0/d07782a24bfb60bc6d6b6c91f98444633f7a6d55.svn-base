package com.jiubang.goscreenlock.theme.cjpcardcool.util;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 桌面通用TextView
 * @author yangguanxiang
 *
 */
public class ItalicTextView extends TextView {

	public ItalicTextView(Context context) {
		super(context);
		setItalic();
	}

	public ItalicTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setItalic();
	}

	public ItalicTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setItalic();
	}


	/**
	 * 设置为斜体
	 */
	public void setItalic() {
		Typeface tf = Typeface.DEFAULT;
		Resources res = getResources();
		if (res != null) {
			Configuration config = res.getConfiguration();
			if (config != null) {
				Locale locale = config.locale;
				if (locale != null) {
					String language = locale.getLanguage();
					if ("zh".equals(language) || "ko".equals(language) || "ja".equals(language)) {
						tf = Typeface.MONOSPACE;
					}
				}
			}
		}
		setTypeface(tf, Typeface.ITALIC);
	}
}
