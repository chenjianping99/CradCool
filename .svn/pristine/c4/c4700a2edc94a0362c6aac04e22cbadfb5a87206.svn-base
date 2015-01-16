package com.jiubang.goscreenlock.theme.cjpcardcool;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;

/**
 * 
 * <br>
 * 类描述: <br>
 * 功能详细描述:
 * 
 * @author jiezhang
 * @date [2012-10-23]
 */
public class ThemeSetProvider extends ContentProvider {

	private static List<ThemeSetChangeListener> sListeners = new ArrayList<ThemeSetChangeListener>();

	public static void addThemeSetChangeListener(ThemeSetChangeListener l) {
		sListeners.add(l);
	}

	public static void clearThemeSetChangeListener() {
		sListeners.clear();
	}

	private static final int THEMECODE = 0;
	public static final String AUTHORITY = "com.jiubang.goscreenlock.theme.cjpcardcool.extdata";
	private static final UriMatcher MURIMATCHER = new UriMatcher(
			UriMatcher.NO_MATCH);
	public static final Uri CONTENT_URI = Uri
			.parse("content://com.jiubang.goscreenlock.theme.cjpcardcool.extdata");
	public static final String FOLDER_PATH = "f_folderpath";
	public static final String TEMPRATURE_TYPE = "f_currpicpath";
	public static final String SHOW_WEATHER = "show_weather";
	public static final String FIRST_IN = "first_in";
	public static final int FIRST_IN_INDEX = 3;

	public static final String STYLE = "style";
	public static final int STYLE_INDEX = 4;
	
	public static final String GALLERY = "gallery";
	public static final int GALLERY_INDEX = 5;

	static {
		MURIMATCHER.addURI(AUTHORITY, "t_pic", THEMECODE);
	}
	
	public static final String BG_STR = "bg_index";
	public static final int BG_STR_INDEX = 6;
	public static final String BG_PATH = "bg_path";
	public static final int BG_PATH_INDEX = 7;

	private static final String COLUMNS[] = { FOLDER_PATH, TEMPRATURE_TYPE,
			SHOW_WEATHER, FIRST_IN, STYLE, GALLERY, BG_STR, BG_PATH};

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SharedPreferences sharedPreferences = getContext()
				.getApplicationContext()
				.getSharedPreferences(
						"com.jiubang.goscreenlock.theme.cjpcardcool.extdata.themedate",
						Context.MODE_PRIVATE);
		return combinCursor(new Object[] {
				sharedPreferences.getString(FOLDER_PATH, ""),
				sharedPreferences.getString(TEMPRATURE_TYPE, "0"),
				sharedPreferences.getString(SHOW_WEATHER, "true"),
				sharedPreferences.getString(FIRST_IN, "0"),
				sharedPreferences.getString(STYLE, "1"), 
				sharedPreferences.getString(GALLERY, ""),
				sharedPreferences.getString(BG_STR, ""),
				sharedPreferences.getString(BG_PATH, "")});
	}

	/**
	 * 组装cursor
	 * 
	 * @param cursor
	 * @return
	 */
	private MatrixCursor combinCursor(Object[] themeName) {
		MatrixCursor ret = null;

		ret = new MatrixCursor(COLUMNS);
		ret.addRow(themeName);

		return ret;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// Log.i("ldj","写入点击标记" + (String) values.get("f_folderpath"));

		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = getContext()
				.getApplicationContext()
				.getSharedPreferences(
						"com.jiubang.goscreenlock.theme.cjpcardcool.extdata.themedate",
						Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		String path = (String) values.get(FOLDER_PATH);
		if (path != null && !path.equals("") && path != "") {
			editor.putString(FOLDER_PATH, path);
		}

		String type = (String) values.get(TEMPRATURE_TYPE);
		if (type != null && !type.equals("") && type != "") {
			editor.putString(TEMPRATURE_TYPE, type);
		}

		String show = (String) values.get(SHOW_WEATHER);
		if (show != null && !show.equals("") && show != "") {
			editor.putString(SHOW_WEATHER, show);
		}

		String first = (String) values.get(FIRST_IN);
		if (first != null && !first.equals("") && first != "") {
			editor.putString(FIRST_IN, first);
		}

		String bg = (String) values.get(STYLE);
		if (bg != null && !bg.equals("") && bg != "") {
			editor.putString(STYLE, bg);
		}
		
		String bottom = (String) values.get(GALLERY);
		if (bottom != null && !bottom.equals("") && bottom != "") {
			editor.putString(GALLERY, bottom);
		}

		String bgStr = (String) values.get(BG_STR);
		if (bgStr != null) {
			editor.putString(BG_STR, bgStr);
		}

		String bgPath = (String) values.get(BG_PATH);
		if (bgPath != null) {
			editor.putString(BG_PATH, bgPath);
		}
		
		editor.commit();
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
		// TODO Auto-generated method stub
		return 0;
	}

	public static void setFirstIn(Context c, int count) {
		ContentValues value = new ContentValues();
		value.put(ThemeSetProvider.FIRST_IN, "" + count);
		c.getContentResolver().insert(ThemeSetProvider.CONTENT_URI, value);
	}

	public static boolean isFirstIn(Context c) {
		Cursor cursor = c.getContentResolver().query(
				ThemeSetProvider.CONTENT_URI, null, null, null, null);
		if (null != cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				int first = Integer.parseInt(cursor
						.getString(ThemeSetProvider.FIRST_IN_INDEX));
				cursor.close();
				if (first < 2) {
					setFirstIn(c, first + 1);
					return true;
				}
			}
		}

		return false;
	}

	public static void setBackgroundIndex(Context c, int index) {
		int current = getBackgroundIndex(c);
		//Log.d(Constant.TAG, "current: " + current);
		if (current == index) {
			return;
		}
		ContentValues value = new ContentValues();
		value.put(ThemeSetProvider.STYLE, "" + index);
		c.getContentResolver().insert(ThemeSetProvider.CONTENT_URI, value);

		//Log.d(Constant.TAG, "index: " + index);
		for (ThemeSetChangeListener l : sListeners) {
			l.onBGChange();
		}
	}

	public static int getBackgroundIndex(Context c) {
		Cursor cursor = c.getContentResolver().query(
				ThemeSetProvider.CONTENT_URI, null, null, null, null);
		if (null != cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				int index = Integer.parseInt(cursor
						.getString(ThemeSetProvider.STYLE_INDEX));
				cursor.close();
				return index;
			}
		}
		return 0;
	}
	
	public static void setGallery(Context c, String name) {
		ContentValues value = new ContentValues();
		value.put(ThemeSetProvider.GALLERY, name);
		c.getContentResolver().insert(ThemeSetProvider.CONTENT_URI, value);
	}

	public static String getGallery(Context c) {
		Cursor cursor = c.getContentResolver().query(
				ThemeSetProvider.CONTENT_URI, null, null, null, null);
		if (null != cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				String str = cursor
						.getString(ThemeSetProvider.GALLERY_INDEX);
				cursor.close();
				return str;
			}
		}
		return null;
	}
	
	public static String sBgIndexs = "", sBgPath = "";
	public static void getSetting(Context context) {
		Cursor cursor = context.getContentResolver().query(ThemeSetProvider.CONTENT_URI, null, null, null,
				null);
		if (null != cursor) {
			try {
				if (cursor.moveToFirst()) {
					sBgIndexs = cursor.getString(cursor.getColumnIndex(ThemeSetProvider.COLUMNS[BG_STR_INDEX]));
					sBgPath = cursor.getString(cursor.getColumnIndex(ThemeSetProvider.COLUMNS[BG_PATH_INDEX]));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
				cursor = null;
			}
		}
	}

	public static void saveSetting(Context context) {
		LogUtils.log(null, "saveSetting: ThemeSetProvider.sBgIndexs =" + ThemeSetProvider.sBgIndexs);
		ContentValues content = new ContentValues();
		content.put(ThemeSetProvider.COLUMNS[BG_STR_INDEX], sBgIndexs);
		content.put(ThemeSetProvider.COLUMNS[BG_PATH_INDEX], sBgPath);
		context.getContentResolver().insert(ThemeSetProvider.CONTENT_URI, content);
		
		for (ThemeSetChangeListener l : sListeners) {
			l.onBGChange();
		}
	}
	
}
