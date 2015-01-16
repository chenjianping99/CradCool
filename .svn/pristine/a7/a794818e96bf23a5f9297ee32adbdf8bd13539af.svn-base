package com.jiubang.goscreenlock.theme.cjpcardcool.weather.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jiubang.goscreenlock.theme.cjpcardcool.R;
/***/
public class GoWeatherEXDialog extends Dialog {

	protected GoWeatherEXDialog(Context context, int theme) {
		super(context, theme);
	}

	protected GoWeatherEXDialog(Context context) {
		super(context);
	}

	private boolean mIsStoped = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!mIsStoped) {
			cancel();
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onStop() {
		mIsStoped = true;
		super.onStop();
	}

	/**
	 * 
	 * 类描述:
	 * 功能详细描述:
	 * 
	 * @author  chenyuning
	 * @date  [2012-11-29]
	 */
	public static class Builder {

		private int mWidth = LayoutParams.WRAP_CONTENT;
		private int mHeight = LayoutParams.WRAP_CONTENT;

		private Context mContext;
		private String mTitle;
		private String mMessage;
		private String mTips;
		private String mPositiveButtonText;
		private String mNeutralButtonText;
		private String mNegativeButtonText;
		private String mSingleButtonText;
		private View mContentView;
		/**用于获取宽高的Resources，不能用来获取其他*/
		private Resources mDisplayRes;

		private DialogInterface.OnClickListener mPositiveButtonClickListener;
		private DialogInterface.OnClickListener mNeutralButtonClickListener;
		private DialogInterface.OnClickListener mNegativeButtonClickListener;
		private DialogInterface.OnClickListener mSingleButtonClickListener;

		private CharSequence[] mItems;
		private OnMultiChoiceClickListener mOnCheckboxClickListener;
		private boolean[] mCheckedItems;
		private int mDialogStyle;
		private OnClickListener mOnClickListener;
		private int mCheckedItem;
		private int mMaxDisplayLines;
		private final static int MESSAGE_DIALOG = 0x01;
		private final static int SINGLE_CHOICE_DIALOG = 0x02;
		private final static int MULTI_CHOICE_DIALOG = 0x03;
		private final static int USER_DIALOG = 0x04;
		/**
		 * 若置为true，则限制对话框高度，内容区域变为可滚动显示；默认false
		 */
		private boolean mScrollable = false;

		public Builder(Context context) {
			this.mContext = context;
			mDisplayRes = context.getResources();
		}

		/**
		 * 设置是否可滚动显示
		 * @param scrollable
		 */
		public Builder setScrollable(boolean scrollable) {
			this.mScrollable = scrollable;
			return this;
		}

		/** 
		 * 设置正文（蓝色）
		 * @param message 
		 * @return
		 */
		public Builder setMessage(String message) {
			this.mMessage = message;
			this.mDialogStyle = MESSAGE_DIALOG;
			return this;
		}

		/** 
		 * 设置正文（蓝色）
		 * @param message 
		 * @return
		 */
		public Builder setMessage(int message) {
			setMessage((String) mContext.getText(message));
			return this;
		}

		/** 
		 * 设置标题
		 * @param message 
		 * @return
		 */
		public Builder setTitle(int title) {
			setTitle((String) mContext.getText(title));

			return this;
		}

		/** 
		 * 设置标题
		 * @param message 
		 * @return
		 */
		public Builder setTitle(String title) {
			this.mTitle = title;
			return this;
		}

		/** 
		 * 设置提示语（黑色）
		 * @param tips 
		 * @return
		 */
		public Builder setTips(int tips) {
			setTips((String) mContext.getText(tips));
			return this;
		}

		/** 
		 * 设置提示语（黑色）
		 * @param tips 
		 * @return
		 */
		public Builder setTips(String tips) {
			this.mTips = tips;
			return this;
		}

		/**
		 * 设置自定义View（会替换正文和列表，但提示语仍然会显示）
		 * @param v
		 * @return
		 */
		public Builder setContentView(View v, int width, int height) {
			this.mContentView = v;
			this.mDialogStyle = USER_DIALOG;
			this.mWidth = width;
			this.mHeight = height;
			return this;
		}

		/**
		 * 设置蓝色按钮（只有一个，不与白色按钮同时存在）
		 * @param singleButtonText
		 * @param listener
		 * @return
		 */
		public Builder setSingleButton(int singleButtonText,
				DialogInterface.OnClickListener listener) {
			return setSingleButton((String) mContext.getText(singleButtonText), listener);
		}

		/**
		 * 设置蓝色按钮（只有一个，不与白色按钮同时存在）
		 * @param singleButtonText
		 * @param listener
		 * @return
		 */
		public Builder setSingleButton(String singleButtonText,
				DialogInterface.OnClickListener listener) {
			this.mSingleButtonText = (String) singleButtonText;
			this.mSingleButtonClickListener = listener;
			return this;
		}

		/**
		 * 设置确定按钮（白色，不与蓝色按钮同时存在）
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			return setPositiveButton((String) mContext.getText(positiveButtonText), listener);
		}

		/**
		 * 设置确定按钮（白色，不与蓝色按钮同时存在）
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.mPositiveButtonText = positiveButtonText;
			this.mPositiveButtonClickListener = listener;
			return this;
		}

		/**
		 * 设置中间按钮（白色，不与蓝色按钮同时存在）
		 * @param neutralButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNeutralButton(int neutralButtonText,
				DialogInterface.OnClickListener listener) {
			return setNeutralButton((String) mContext.getText(neutralButtonText), listener);
		}

		/**
		 * 设置中间按钮（白色，不与蓝色按钮同时存在）
		 * @param neutralButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNeutralButton(String neutralButtonText,
				DialogInterface.OnClickListener listener) {
			this.mNeutralButtonText = neutralButtonText;
			this.mNeutralButtonClickListener = listener;
			return this;
		}

		/**
		 * 设置取消按钮（白色，不与蓝色按钮同时存在）
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			return setNegativeButton((String) mContext.getText(negativeButtonText), listener);
		}

		/**
		 * 设置取消按钮（白色，不与蓝色按钮同时存在）
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.mNegativeButtonText = negativeButtonText;
			this.mNegativeButtonClickListener = listener;
			return this;
		}

		/**
		 * 设置多选对话框
		 * @param itemsId 列表显示文本的资源数组
		 * @param checkedItems 被选中的项的数组
		 * @param listener
		 * @return
		 */
		public Builder setMultiChoiceItems(int itemsId, boolean[] checkedItems,
				final OnMultiChoiceClickListener listener, int maxLines) {
			return setMultiChoiceItems(mContext.getResources().getTextArray(itemsId), checkedItems,
					listener, maxLines);
		}

		/**
		 * 设置多选对话框
		 * @param itemsId 列表显示文本数组
		 * @param checkedItems 被选中的项的数组
		 * @param listener
		 * @return
		 */
		public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems,
				final OnMultiChoiceClickListener listener, int maxLines) {
			if (items != null && checkedItems != null && listener != null) {
				this.mItems = items;
				this.mOnCheckboxClickListener = listener;
				this.mCheckedItems = checkedItems;
				this.mDialogStyle = MULTI_CHOICE_DIALOG;
				this.mMaxDisplayLines = maxLines;
			}
			return this;
		}

		/**
		 * 设置单选对话框
		 * @param itemsId 列表显示文本的资源数组
		 * @param checkedItem 被选中的项
		 * @param listener
		 * @return
		 */
		public Builder setSingleChoiceItems(int itemsId, int checkedItem,
				final OnClickListener listener, int maxLines) {
			return setSingleChoiceItems(mContext.getResources().getTextArray(itemsId), checkedItem,
					listener, maxLines);
		}

		/**
		 * 设置单选对话框
		 * @param itemsId 列表显示文本数组
		 * @param checkedItem 被选中的项
		 * @param listener
		 * @return
		 */
		public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem,
				final OnClickListener listener, int maxLines) {
			if (items != null && listener != null) {
				this.mItems = items;
				this.mOnClickListener = listener;
				this.mCheckedItem = checkedItem;
				this.mDialogStyle = SINGLE_CHOICE_DIALOG;
				this.mMaxDisplayLines = maxLines;
			}
			return this;
		}

		/**
		 * 创建对话框
		 * @return
		 */
		public GoWeatherEXDialog create() {

			final GoWeatherEXDialog dialog = new GoWeatherEXDialog(mContext, R.style.dialog);

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = null;
			if (mScrollable) {
				layout = inflater.inflate(R.layout.weather_go_weather_ex_dialog_scrollable, null);
			} else {
				layout = inflater.inflate(R.layout.weather_go_weather_ex_dialog, null);
			}

			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));

			// 设置边距
			int screenWidth = mDisplayRes.getDisplayMetrics().widthPixels;
			int padding = (int) mDisplayRes.getDimension(R.dimen.dialog_padding);
			WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
			layoutParams.width = screenWidth - padding * 2;
			dialog.getWindow().setAttributes(layoutParams);

			// 设置显示内容
			TextView title = (TextView) layout.findViewById(R.id.dialog_title_text);
			LinearLayout titleLayout = (LinearLayout) layout.findViewById(R.id.dialog_title_layout);
			if (mTitle != null) {
				titleLayout.setVisibility(View.VISIBLE);
				title.setText(mTitle);
			} else {
				titleLayout.setVisibility(View.GONE);
			}

			LinearLayout contenter = (LinearLayout) layout.findViewById(R.id.dialog_container);
			TextView message = (TextView) layout.findViewById(R.id.dialog_message);
			ListView listView = (ListView) layout.findViewById(R.id.dialog_list);
			View divider = layout.findViewById(R.id.list_divider);

			switch (mDialogStyle) {
				case MESSAGE_DIALOG :
					divider.setVisibility(View.GONE);
					contenter.setVisibility(View.VISIBLE);
					message.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
					message.setText(mMessage);
					break;

				case SINGLE_CHOICE_DIALOG :
					divider.setVisibility(View.VISIBLE);
					contenter.setVisibility(View.VISIBLE);
					message.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);

					if (mItems.length > mMaxDisplayLines) {
						ViewGroup.LayoutParams params = listView.getLayoutParams();
						int listItemHeight = (int) mContext.getResources().getDimension(
								R.dimen.dialog_list_item_height);
						params.height = mMaxDisplayLines * listItemHeight;
						listView.setLayoutParams(params);
					}

					final SingleChoiceAdapter adapter = new SingleChoiceAdapter(dialog);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
							mCheckedItem = position;
							adapter.notifyDataSetChanged();
							mOnClickListener.onClick(dialog, mCheckedItem);
						}
					});
					break;

				case MULTI_CHOICE_DIALOG :
					divider.setVisibility(View.VISIBLE);
					contenter.setVisibility(View.VISIBLE);
					message.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);

					if (mItems.length > mMaxDisplayLines) {
						ViewGroup.LayoutParams params = listView.getLayoutParams();
						int listItemHeight = (int) mContext.getResources().getDimension(
								R.dimen.dialog_list_item_height);
						params.height = mMaxDisplayLines * listItemHeight;
						listView.setLayoutParams(params);
					}

					listView.setAdapter(new MultiChoiceAdapter(dialog));
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
							CheckBox cb = (CheckBox) view.findViewById(R.id.multi_item_checkbox);
							mCheckedItems[position] = !cb.isChecked();
							cb.setChecked(mCheckedItems[position]);
							mOnCheckboxClickListener.onClick(dialog, position,
									mCheckedItems[position]);
						}
					});
					break;

				case USER_DIALOG :
					divider.setVisibility(View.GONE);
					contenter.setVisibility(View.VISIBLE);
					contenter.removeAllViews();
					contenter.addView(mContentView, new LayoutParams(mWidth, mHeight));
					dialog.setContentView(layout);
					break;

				default :
					divider.setVisibility(View.GONE);
					contenter.setVisibility(View.GONE);
					break;
			}

			TextView tips = (TextView) layout.findViewById(R.id.dialog_tips);
			if (mTips != null) {
				tips.setVisibility(View.VISIBLE);
				tips.setText(mTips);
			} else {
				tips.setVisibility(View.GONE);
			}

			Button singleButton = (Button) layout.findViewById(R.id.dialog_single_button);
			Button positiveButton = (Button) layout.findViewById(R.id.dialog_positive_button);
			Button neutralButton = (Button) layout.findViewById(R.id.dialog_neutral_button);
			Button negativeButton = (Button) layout.findViewById(R.id.dialog_negative_button);
			// 设置Listener
			if (mSingleButtonText != null) {
				singleButton.setVisibility(View.VISIBLE);
				singleButton.setText(mSingleButtonText);
				singleButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (mSingleButtonClickListener != null) {
							mSingleButtonClickListener.onClick(dialog,
									DialogInterface.BUTTON_POSITIVE);
						}
						dialog.dismiss();
					}
				});

			} else {

				if (mPositiveButtonText != null) {
					positiveButton.setVisibility(View.VISIBLE);
					positiveButton.setText(mPositiveButtonText);
					positiveButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							if (mPositiveButtonClickListener != null) {
								mPositiveButtonClickListener.onClick(dialog,
										DialogInterface.BUTTON_POSITIVE);
							}
							dialog.dismiss();
						}
					});
				}

				if (mNeutralButtonText != null) {
					neutralButton.setVisibility(View.VISIBLE);
					neutralButton.setText(mNeutralButtonText);
					neutralButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							if (mNeutralButtonClickListener != null) {
								mNeutralButtonClickListener.onClick(dialog,
										DialogInterface.BUTTON_NEUTRAL);
							}
							dialog.dismiss();
						}
					});
				}

				if (mNegativeButtonText != null) {
					negativeButton.setVisibility(View.VISIBLE);
					negativeButton.setText(mNegativeButtonText);
					negativeButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							if (mNegativeButtonClickListener != null) {
								mNegativeButtonClickListener.onClick(dialog,
										DialogInterface.BUTTON_NEGATIVE);
							}
							dialog.dismiss();
						}
					});
				}
			}
			return dialog;
		}
		/**
		 * 显示对话框
		 */
		public void show() {
			create().show();
		}

		/***/
		class SingleChoiceAdapter extends BaseAdapter {

			private LayoutInflater mInflater;
			public SingleChoiceAdapter(GoWeatherEXDialog dialog) {
				mInflater = dialog.getLayoutInflater();
			}
			@Override
			public int getCount() {
				return mItems.length;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = (LinearLayout) mInflater.inflate(
							R.layout.weather_dialog_single_choice_item, null);
				}
				TextView tv = (TextView) convertView.findViewById(R.id.single_item_text);
				RadioButton rv = (RadioButton) convertView
						.findViewById(R.id.single_item_ratiobutton);

				tv.setText(mItems[position]);
				if (position == mCheckedItem) {
					rv.setChecked(true);
				} else {
					rv.setChecked(false);
				}
				return convertView;
			}
		}

		/***/
		class MultiChoiceAdapter extends BaseAdapter {
			private LayoutInflater mInflater;
			GoWeatherEXDialog mDialog;
			public MultiChoiceAdapter(GoWeatherEXDialog dialog) {
				mDialog = dialog;
				mInflater = dialog.getLayoutInflater();
			}
			@Override
			public int getCount() {
				return mItems.length;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = (LinearLayout) mInflater.inflate(
							R.layout.weather_dialog_multi_choice_item, null);
				}
				TextView tv = (TextView) convertView.findViewById(R.id.multi_item_text);
				CheckBox cb = (CheckBox) convertView.findViewById(R.id.multi_item_checkbox);

				tv.setText(mItems[position]);
				cb.setChecked(mCheckedItems[position]);
				return convertView;
			}
		}
	}
}