package com.jiubang.goscreenlock.theme.cjpcardcool.smstool;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;


/**
 * 
 * @author zhangjie
 * 
 */
public class UnreadProvider extends ContentProvider {
	private static final boolean DEBUG = false;
	public static final String SCHEMEN = "content://";
	public static final String AUTHORITY = "com.jiubang.goscreenlock.theme.cjpcardcool.unread.provider";
	private static final String UNREAD = "unread";
	private static final String TABLE_WEATHERINFO_AUTHORITY = SCHEMEN
			+ AUTHORITY + "/" + UNREAD;
	private static final Uri SMSURI = Uri.parse("content://sms/inbox"); // 短信的uri
	private static final int CODE_UPDATE_SMS = 100;
	private static final int CODE_UPDATE_PHONE = 101;

	private static final UriMatcher SURIMATCHER = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		SURIMATCHER.addURI(AUTHORITY, UNREAD + "/0", CODE_UPDATE_SMS); // 更新短信
		SURIMATCHER.addURI(AUTHORITY, UNREAD + "/1", CODE_UPDATE_PHONE); // 更新电话
	}

	public static final Uri UPDATE_SMS = Uri.parse(TABLE_WEATHERINFO_AUTHORITY
			+ "/0");
	public static final Uri UPDATE_PHONE = Uri
			.parse(TABLE_WEATHERINFO_AUTHORITY + "/1");

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		if (uri == null || values == null) {
			return 0;
		}
		if (DEBUG) {
			Log.v("Test", "update uri:" + uri);
		}
		int result = 0;
		switch (SURIMATCHER.match(uri)) {
		case CODE_UPDATE_SMS:
			result = getContext().getContentResolver().update(SMSURI, values,
					selection, selectionArgs);
			break;
		case CODE_UPDATE_PHONE:
			result = getContext().getContentResolver()
					.update(CallLog.Calls.CONTENT_URI, values, selection,
							selectionArgs);
			break;
		}
		if (DEBUG) {
			Log.v("Test", "result is:" + result);
		}
		return result;
	}
}
