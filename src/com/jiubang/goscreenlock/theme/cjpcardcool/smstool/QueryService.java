package com.jiubang.goscreenlock.theme.cjpcardcool.smstool;

import java.io.Serializable;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;

import com.jiubang.goscreenlock.theme.cjpcardcool.smstool.UnreadHelper.QueryCallBack;
import com.jiubang.goscreenlock.theme.cjpcardcool.util.LogUtils;
import com.jiubang.goscreenlock.theme.cjpcardcool.view.Constant;

/**
 * 
 * @author chenjianping
 * 
 */
public class QueryService extends Service implements QueryCallBack {
	private Context mContext;
	private UnreadHelper mHelper;

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtils.log(null, "onCreate");
		mContext = this;
		mHelper = new UnreadHelper(mContext, this);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		getContentResolver().registerContentObserver(Uri.parse(UnreadHelper.SMS_URI_ALL),
                true, mSMSObserver);
		getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI,
                true, mCallObserver);
		mHelper.startQueryPhone();
		mHelper.startQuerySms();
		super.onStart(intent, startId);
	}

	private SMSObserver mSMSObserver = new SMSObserver(new Handler());
	private CallObserver mCallObserver = new CallObserver(new Handler());
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	public static final String FILTER = Constant.THEME_PACKAGE_NAME
			+ ".query";
	public static final String COPA_MESSAGE = "result";
	public static final String SMS = "sms";
	public static final String DATA = "data";
	public static final String CALL = "call";
	
	private void sendResult(String message) {
		Intent intent = new Intent(FILTER);
		intent.putExtra(COPA_MESSAGE, message);
		if (message.equals(CALL)) {
			intent.putExtra(DATA, (Serializable) mHelper.getAllPhoneBean());
		} else if (message.equals(SMS)) {
			intent.putExtra(DATA, (Serializable)  mHelper.getAllSmsBean());
		}
		/*if (message.equals(CALL) && mHelper.setCallInfoBeanByList() != null) {
			intent.putExtra(DATA, mHelper.setCallInfoBeanByList());
		} else if (message.equals(SMS) && mHelper.setSmsInfoBeanByList() != null) {
			intent.putExtra(DATA, mHelper.setSmsInfoBeanByList());
		}*/
		LogUtils.log(null, "service sendBroadcast: message=" + message);
		mContext.sendBroadcast(intent);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		LogUtils.log(null, "Query Service: onDestroy");
		mHelper.clean();
		this.getContentResolver().unregisterContentObserver(mSMSObserver); 
		this.getContentResolver().unregisterContentObserver(mCallObserver);  
		super.onDestroy();
		//android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void smsDone() {
		// TODO Auto-generated method stub
		LogUtils.log(null, "service :smsDone = " + mHelper.getAllSmsBean());
		if (!mHelper.getAllSmsBean().isEmpty()) {
			sendResult(SMS);
		}
	}

	@Override
	public void phoneDone() {
		// TODO Auto-generated method stub
		LogUtils.log(null, "service :phoneDone");
		if (!mHelper.getAllPhoneBean().isEmpty()) {
			sendResult(CALL);
		}
	}
	
	/*	public void startQuerySms() {
		mHelper.startQuerySms();
	}
	
	public void startQueryPhone() {
		mHelper.startQueryPhone();
	}

private QueryBinder mBinder = new QueryBinder();

	*//**
	 * @author chenjianping
	 *//*
	public class QueryBinder extends Binder{
		*//**
		 * 获取当前Service的实例
		 * @return
		 *//*
		public QueryService getService(){
			return QueryService.this;
		}
	}*/
	
	/**
	 * 
	 * @author chenjianping
	 * 
	 */
	public class SMSObserver extends ContentObserver {

		public SMSObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			mHelper.startQuerySms();
			super.onChange(selfChange);
		}
	}
	
	/**
	 * 
	 * @author chenjianping
	 * 
	 */
	public class CallObserver extends ContentObserver {

		public CallObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			mHelper.startQueryPhone();
			super.onChange(selfChange);
		}
	}

}
