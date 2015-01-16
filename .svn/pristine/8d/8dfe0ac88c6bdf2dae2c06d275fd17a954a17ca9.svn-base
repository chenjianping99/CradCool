package com.jiubang.goscreenlock.theme.cjpcardcool.smstool;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubang.goscreenlock.theme.cjpcardcool.smstool.UnreadHelper.QueryCallBack;
/**
 * 
 * @author zhangjie
 *	移植注意事项：
 */
public class UnreadActivity extends Activity implements QueryCallBack, OnItemClickListener {
	private UnreadHelper mHelper;
	private List<BaseInfo> mBaseInfos;
	private LayoutInflater mInflater;
	private MyAdapter myAdapter;
	private ListView mListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mHelper = new UnreadHelper(getApplicationContext(), this);
		mBaseInfos = new ArrayList<BaseInfo>();
		mInflater = LayoutInflater.from(getApplicationContext());
		
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LinearLayout layout = new LinearLayout(getApplicationContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		addContentView(layout, params);
		
		TextView textView = new TextView(getApplicationContext());
		textView.setText("注意事项：移植的时候请将此包的其他类全部拷贝，注意Provider需要在manifest注册,还有四个权限，authorities为了保证唯一性，" +
				"所以请将里面的themename改为各自的主题名称即可,别忘了同时修改UnreadProvider里面的哦。由于此工具已经实现了异步读取未读短信和电话，所以需要实现回调接口。在使用过程中" +
				"有任何问题，可以直接找本人：张杰。我将在后续版本即使修复及更新，谢谢！");
		layout.addView(textView);
		
		myAdapter = new MyAdapter(mBaseInfos);
		mListView = new ListView(getApplicationContext());
		layout.addView(mListView);
		mListView.setAdapter(myAdapter);
		mListView.setOnItemClickListener(this);
		//mHelper.startQuerySms();
		mHelper.startQueryPhone();
	}
	
	/**
	 * 
	 * @author zhangjie
	 *
	 */
	class MyAdapter extends ArrayAdapter<BaseInfo> {
		
		public MyAdapter(List<BaseInfo> mBaseInfos) {
			super(getApplicationContext(), android.R.layout.simple_list_item_2, mBaseInfos);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(android.R.layout.simple_list_item_2, null);
				holder.mTitleView = (TextView) convertView.findViewById(android.R.id.text1);
				holder.mContentView = (TextView) convertView.findViewById(android.R.id.text2);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			BaseInfo baseInfo = getItem(position);
			switch (baseInfo.getType()) {
			case UnreadHelper.INFO_TYPE_SMS:
				SmsInfoBean smsInfoBean = (SmsInfoBean) baseInfo;
				holder.mTitleView.setText("" + smsInfoBean.getmSmsPerson());
				holder.mContentView.setText("" + smsInfoBean.getmSmsBody());
				break;
			case UnreadHelper.INFO_TYPE_PHONE:
				CallInfoBean callInfoBean = (CallInfoBean) baseInfo;
				holder.mTitleView.setText("" + callInfoBean.getmCallName());
				holder.mContentView.setText("" + callInfoBean.getmCallNumber());
				break;
			default:
				break;
			}
			return convertView;
		}
	}
	
	/**
	 * 
	 * @author zhangjie
	 *
	 */
	class ViewHolder {
		TextView mTitleView;
		TextView mContentView;
	}
	
	@Override
	public void smsDone() {
		if (mHelper != null && mBaseInfos != null) {
			mBaseInfos.clear();
			mBaseInfos.addAll(mHelper.getAllSmsBean());
			mBaseInfos.addAll(mHelper.getAllPhoneBean());
		}
		if (myAdapter != null) {
			myAdapter.notifyDataSetChanged();
		}
		
	}
	@Override
	public void phoneDone() {
		if (mHelper != null && mBaseInfos != null) {
			mBaseInfos.clear();
			mBaseInfos.addAll(mHelper.getAllSmsBean());
			mBaseInfos.addAll(mHelper.getAllPhoneBean());
		}
		if (myAdapter != null) {
			myAdapter.notifyDataSetChanged();
		}
		Log.d("ddd", "phoneDone");
	}
	
	private void addButton() {
		Button mButton = new Button(getApplicationContext());
		
		mButton.setText("go to Contact");
		
		mButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Log.d("ddd", "onClick");
				mHelper.getAllPhoneBean();
				

			}
		});
	}
	
	private void intentContact(String phoneNumber) {
		int contactID = getContactIDFromNumber(phoneNumber, getApplicationContext());
		
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
		intent.setData(uri);
		startActivity(intent);
		finish();
	}
	
	public static int getContactIDFromNumber(String contactNumber, Context context)
	{
	    contactNumber = Uri.encode(contactNumber);
	    int phoneContactID = -1;
	    Cursor contactLookupCursor = context.getContentResolver().query(Uri.withAppendedPath(
	    		PhoneLookup.CONTENT_FILTER_URI,
	    		contactNumber),
	    		new String[] {PhoneLookup.DISPLAY_NAME, PhoneLookup._ID}, 
	    		null, null, null);
	        while (contactLookupCursor.moveToNext()) {
	            phoneContactID = contactLookupCursor.getInt(contactLookupCursor.getColumnIndexOrThrow(PhoneLookup._ID));
	            }
	        contactLookupCursor.close();

	    return phoneContactID;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		BaseInfo baseInfo = (BaseInfo) myAdapter.getItem(position);
		if (baseInfo == null) {
			return;
		}
		switch (baseInfo.getType()) {
		case UnreadHelper.INFO_TYPE_SMS:
			SmsInfoBean smsInfoBean = (SmsInfoBean) baseInfo;
			if (mHelper != null) {
				int result = mHelper.markSmsReadById(smsInfoBean.getmSmsId());
				String aaString = "";
				if (result > 0) {
					aaString = "标记短信成功！！";
				} else {
					aaString = "标记短信失败！！";
				}
				Toast.makeText(getApplicationContext(), aaString, Toast.LENGTH_SHORT).show();
			}
			smsDone();
			break;
		case UnreadHelper.INFO_TYPE_PHONE:
			CallInfoBean phoneInfoBean = (CallInfoBean) baseInfo;
			if (mHelper != null) {
//				int result = mHelper.markPhoneReadById(phoneInfoBean.getmCallId());
//				String aaString = "";
//				if (result > 0) {
//					aaString = "标记电话成功！！";
//				} else {
//					aaString = "标记电话失败！！";
//				}
				intentContact(phoneInfoBean.getmCallNumber());
				//Toast.makeText(getApplicationContext(), aaString, Toast.LENGTH_SHORT).show();
			}
			//phoneDone();
			break;
		default:
			break;
		}
	}
}
