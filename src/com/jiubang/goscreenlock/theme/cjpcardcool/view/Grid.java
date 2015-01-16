package com.jiubang.goscreenlock.theme.cjpcardcool.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiubang.goscreenlock.theme.cjpcardcool.R;

/**
 * 
 * @author June KwoK
 * 
 */
public class Grid extends GridView {

	private List<ResolveInfo> mAppItemList = new ArrayList<ResolveInfo>();
	private int mViewWidth, mViewHeight;
	private int mCurrentPosition = -1;

	public Grid(Context context, int w, int h, List<ResolveInfo> mList) {
		super(context);
		setNumColumns(3);
		setSelector(android.R.color.transparent);
		mAppItemList = mList;
		mViewWidth = w;
		mViewHeight = h;
		setAdapter(new AppAdapter());
	}

	public Grid(Context context) {
		super(context);
		setNumColumns(3);
		setSelector(android.R.color.transparent);
		setAdapter(new AppAdapter());
	}

	public boolean isEmpty() {
		return mAppItemList == null || mAppItemList.size() == 0;
	}

	public boolean isOnlyOne() {
		return mAppItemList != null && mAppItemList.size() == 1;
	}

	public String getDefaultPackage() {
		mCurrentPosition = 0;
		return getCurrentSelect();
	}

	public String getCurrentSelect() {
		if (mCurrentPosition >= 0 && mCurrentPosition < mAppItemList.size()) {
			ResolveInfo item = mAppItemList.get(mCurrentPosition);
			return item.activityInfo.packageName;
		}
		return null;
	}

	/**
	 * 
	 * @author June Kwok
	 * 
	 */
	private class AppAdapter extends BaseAdapter implements OnClickListener {

		@Override
		public int getCount() {
			return mAppItemList.size();
		}

		@Override
		public Object getItem(int position) {
			return mAppItemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ResolveInfo item = (ResolveInfo) getItem(position);
			LinearLayout itemView = null;
			if (convertView == null) {
				itemView = new LinearLayout(getContext());
				int w = (int) (mViewWidth * 0.23f);
				int h = (int) (mViewWidth * 0.38f);
				itemView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, h));
				itemView.setOrientation(LinearLayout.VERTICAL);
				FrameLayout frameLayout = new FrameLayout(getContext());
				itemView.addView(frameLayout);
				LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (mViewWidth * 0.3f));
				frameLayout.setLayoutParams(layout);
				FrameLayout.LayoutParams iconBGLayout = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
				int margins = ViewUtils.getPXByWidth(8);
				iconBGLayout.setMargins(margins, margins, margins, margins);
				ImageView iconBG = new ImageView(getContext());
				iconBG.setLayoutParams(iconBGLayout);
				iconBG.setScaleType(ImageView.ScaleType.FIT_CENTER);
				iconBG.setBackgroundResource(R.drawable.chiceapp_icon_up);
				frameLayout.addView(iconBG);

				FrameLayout.LayoutParams iconViewLayout = new FrameLayout.LayoutParams(w, w, Gravity.CENTER);
				margins = ViewUtils.getPXByWidth(30);
				iconViewLayout.setMargins(margins, margins, margins, margins);
				View iconView = new View(getContext());
				iconView.setLayoutParams(iconViewLayout);
				iconView.setTag("icon");
				frameLayout.addView(iconView);

				ImageView selectedView = new ImageView(getContext());
				selectedView.setBackgroundResource(R.drawable.chiceapp_folder_select);
				FrameLayout.LayoutParams chockbox = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.RIGHT);
				margins = ViewUtils.getPXByWidth(8);
				chockbox.setMargins(margins, margins, margins, margins);
				selectedView.setLayoutParams(chockbox);
				selectedView.setTag("selector");
				selectedView.setVisibility(View.INVISIBLE);
				frameLayout.addView(selectedView);

				TextView appName = new TextView(getContext());
				LayoutParams name = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				appName.setLayoutParams(name);
				appName.setTag("name");
				appName.setGravity(Gravity.CENTER);
				appName.setTextColor(Color.WHITE);
				appName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				appName.setSingleLine(true);
				appName.setGravity(Gravity.CENTER_HORIZONTAL);
				appName.setEllipsize(TruncateAt.valueOf("END"));
				itemView.addView(appName);

			} else {
				itemView = (LinearLayout) convertView;
			}

			PackageManager pManager = getContext().getPackageManager();
			Drawable d = item.loadIcon(pManager);
			View v = itemView.findViewWithTag("icon");
			v.setOnClickListener(this);
			v.setId(position);
			v.setBackgroundDrawable(d);
			//			CharSequence lable = item.loadLabel(pManager);
			//			if (lable != null) {
			//				TextView tv = (TextView) itemView.findViewWithTag("name");
			//				tv.setText(lable);
			//			}

			View select = itemView.findViewWithTag("selector");
			select.setVisibility(mCurrentPosition == position ? View.VISIBLE : View.INVISIBLE);
			itemView.setTag(position);
			return itemView;
		}

		@Override
		public void onClick(View v) {
			mCurrentPosition = v.getId();
			notifyDataSetChanged();
		}
	}

	/**
	 * @return the mViewWidth
	 */
	public final int getViewWidth() {
		return mViewWidth;
	}

	/**
	 * @return the mViewHeight
	 */
	public final int getViewHeight() {
		return mViewHeight;
	}

}
