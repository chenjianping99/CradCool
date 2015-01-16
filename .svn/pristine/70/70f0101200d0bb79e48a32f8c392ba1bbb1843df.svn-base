package com.jiubang.goscreenlock.theme.cjpcardcool.smstool;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;

/**
 * 
 * @author zhangjie
 * 
 */
public class UnreadHelper extends Handler {
	private static final int EVENT_ARG_SMS = 1;
	private static final int EVENT_ARG_PHONE = 2;
	private static final int EVENT_ARG_MARK_SMS = 3;
	private static final int EVENT_ARG_MARK_PHONE = 4;
	
	public static final int INFO_TYPE_SMS = 10;
	public static final int INFO_TYPE_PHONE = 11;

	public static final String SMS_ID = "_id"; // 短信序号
	public static final String SMS_THREAD_ID = "thread_id"; // 对话的序号，如100，与同一个手机号互发的短信，其序号是相同的
	public static final String SMS_ADDRESS = "address"; // 发件人地址，即手机号，如+8613811810000
	public static final String SMS_BODY = "body"; // 短信内容
	public static final String SMS_DATE = "date"; // long型，如1256539465022，可以对日期显示格式进行设置
	public static final String SMS_TYPE = "type"; // 短信类型，1是接收到的，2是已发出
	public static final String SMS_PERSON = "person"; // 发件人，如果发件人在通讯录中则为具体姓名，陌生人为null
	public static final String SMS_READ = "read"; // 是否阅读0未读，1已读
	public static final String SMS_READ_1 = "1"; // 已读
	public static final String SMS_READ_0 = "0"; // 未读
	public static final String SMS_URI_ALL = "content://sms/"; // 所有短信
	public static final String SMS_URI_INBOX = "content://sms/inbox"; // 收件箱
	public static final String SMS_URI_SEND = "content://sms/sent"; // 发送箱
	public static final String SMS_URI_DRAFT = "content://sms/draft"; // 垃圾箱

	private static Looper sLooper = null;
	private Handler mWorkerThreadHandler;
	final WeakReference<ContentResolver> mResolver;
	private BitmapManager manager = null;
	private Context mContext = null;
	
	private int mPhone = 0; //在处理电话请求期间，有多少个新增请求
	private boolean mIsPhone = false; //是否正在处理查询电话的请求
	private int mSms = 0; //在处理电话请求期间，有多少个新增请求
	private boolean mIsSms = false; //是否正在处理查询电话的请求
	
//	private HashMap<String, SmsInfoBean> mUnreadSmsMap = new HashMap<String, SmsInfoBean>(); // id-bean所有信息
//	private HashMap<String, CallInfoBean> mUnreadCallMap = new HashMap<String, CallInfoBean>(); // id-bean所有信息
	
	private ArrayList<SmsInfoBean> mAllSmsInfoBeans = new ArrayList<SmsInfoBean>();
	private ArrayList<CallInfoBean> mAllPhoneBeans = new ArrayList<CallInfoBean>();

	// 组合电话号码-list<bean>（一个号码对应多条未读短信）
	private HashMap<String, ArrayList<SmsInfoBean>> mSmsNumberToBeanMap = new HashMap<String, ArrayList<SmsInfoBean>>();
	private ArrayList<String> mSmsNumberList = new ArrayList<String>(); // 短信：所有未读短信的号码，不重复

	private HashMap<String, ArrayList<CallInfoBean>> mCallNumberToBeanMap = new HashMap<String, ArrayList<CallInfoBean>>();
	private ArrayList<String> mCallNumberList = new ArrayList<String>(); // 电话：所有未读电话的号码，不重复
	
	private QueryCallBack mCallBack;

	public UnreadHelper(Context context, QueryCallBack callBack) {
		mResolver = new WeakReference<ContentResolver>(
				context.getContentResolver());
		synchronized (UnreadHelper.class) {
			if (sLooper == null) {
				HandlerThread thread = new HandlerThread("UnreadHelper");
				thread.start();
				sLooper = thread.getLooper();
			}
		}
		mWorkerThreadHandler = createHandler(sLooper);
		manager = BitmapManager.getInstance(context);
		mContext = context;
		mCallBack = callBack;
	}

	/**
	 *  清除短信内容
	 */
	private void cleanSms() {
		if (mAllSmsInfoBeans != null) {
			mAllSmsInfoBeans.clear();
		}
		if (mSmsNumberToBeanMap != null) {
			mSmsNumberToBeanMap.clear();
		}
		if (mSmsNumberList != null) {
			mSmsNumberList.clear();
		}
	}
	/**
	 * 清除电话内容
	 */
	private void cleanPhone() {
		if (mAllPhoneBeans != null) {
			mAllPhoneBeans.clear();
		}
		if (mCallNumberToBeanMap != null) {
			mCallNumberToBeanMap.clear();
		}
		if (mCallNumberList != null) {
			mCallNumberList.clear();
		}
	}

	/**
	 * 
	 * @author zhangjie
	 * 
	 */
	protected static final class WorkerArgs {
		public Uri uri;
		public Handler handler;
		public String[] projection;
		public String selection;
		public String[] selectionArgs;
		public String orderBy;
		public Object result;
		public Object cookie;
		public ContentValues values;
	}
	/**
	 * 
	 * @author zhangjie
	 *
	 */
	protected class WorkerHandler extends Handler {
		public WorkerHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			final ContentResolver resolver = mResolver.get();
			if (resolver == null) {
				return;
			}

			WorkerArgs args = (WorkerArgs) msg.obj;
			int token = msg.what;
			int event = msg.arg1;
			switch (event) {
			case EVENT_ARG_SMS:
				try {
					startQuerySms(resolver, args, token, msg);
				} catch (SQLiteException ex) {
					Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
				} catch (Exception e) {
					Log.e("ERROR: ", e.toString());
				}
				break;
			case EVENT_ARG_PHONE:
				try {
					startQueryPhone(resolver, args, token, msg);
				} catch (SQLiteException ex) {
					Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
				} catch (Exception e) {
					Log.e("ERROR: ", e.toString());
				}
				break;
			case EVENT_ARG_MARK_SMS:
				try {
					resolver.update(args.uri, args.values, args.selection,
                            args.selectionArgs);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case EVENT_ARG_MARK_PHONE:
				resolver.update(args.uri, args.values, args.selection,
                        args.selectionArgs);
				break;
			default:
				break;
			}
		}
	}
	
	private void sendComplteMsg(WorkerArgs args, int token, Message msg) {
		// passing the original token value back to the caller
		// on top of the event values in arg1.
		Message reply = args.handler.obtainMessage(token);
		reply.obj = args;
		reply.arg1 = msg.arg1;

		reply.sendToTarget();
	}

	protected Handler createHandler(Looper looper) {
		return new WorkerHandler(looper);
	}

	public void startQuerySms() {
		if (mIsSms) {
			mSms ++;
			return;
		}
		mIsSms = true;
		cleanSms();
		Message msg = mWorkerThreadHandler.obtainMessage(EVENT_ARG_SMS);
		msg.arg1 = EVENT_ARG_SMS;
		WorkerArgs args = new WorkerArgs();
		args.handler = this;
		msg.obj = args;

		mWorkerThreadHandler.sendMessage(msg);
	}
	
	/**
	 * 标记所有短信已读
	 * @param number
	 * @return
	 */
	public void markSmsRead() {
		ContentValues values = new ContentValues();
		values.put(SMS_READ, SMS_READ_1); // 修改短信为已读模式
		Message msg = mWorkerThreadHandler.obtainMessage(EVENT_ARG_MARK_SMS);
		msg.arg1 = EVENT_ARG_MARK_SMS;

		WorkerArgs args = new WorkerArgs();
        args.handler = this;
        args.uri = Uri.parse(SMS_URI_INBOX);
        args.values = values;
        args.selection = SMS_READ + "=?";
        args.selectionArgs = new String[] { "0" };
        msg.obj = args;

		mWorkerThreadHandler.sendMessage(msg);
		cleanSms();
	}
	/**
	 * 标记所有电话已读
	 */
	public void markPhoneRead() {
		ContentValues values = new ContentValues();
		values.put(android.provider.CallLog.Calls.TYPE,
				android.provider.CallLog.Calls.INCOMING_TYPE);
		Message msg = mWorkerThreadHandler.obtainMessage(EVENT_ARG_MARK_PHONE);
		msg.arg1 = EVENT_ARG_MARK_PHONE;

		WorkerArgs args = new WorkerArgs();
		args.handler = this;
		args.uri = CallLog.Calls.CONTENT_URI;
		args.values = values;
		args.selection = android.provider.CallLog.Calls.TYPE + "=?";
		args.selectionArgs = new String[] { String
				.valueOf(android.provider.CallLog.Calls.MISSED_TYPE) };
        msg.obj = args;

		mWorkerThreadHandler.sendMessage(msg);
		cleanPhone();
	}
	
	/**
	 * 根据电话标记这个号码的所有短信已读
	 * 
	 * @param number
	 */
	public int markSmsReadByNumber(String number) {
		int result = 0;
		try {
			ContentValues values = new ContentValues();
			values.put(SMS_READ, SMS_READ_1); // 修改短信为已读模式
			result = mContext.getContentResolver().update(
					UnreadProvider.UPDATE_SMS, values, SMS_ADDRESS + "=?",
					new String[] { number });
			if (result > 0 && mAllSmsInfoBeans != null) {
				for (Iterator<SmsInfoBean> iter = mAllSmsInfoBeans
						.iterator(); iter.hasNext();) {
					SmsInfoBean entry = iter.next();
					if (entry.getmAddress().equals(number)) {
						// mUnreadSmsMap.remove(val.getmSmsId());
						iter.remove(); // 预防java.util.ConcurrentModificationException异常
					}
				}
				if (mSmsNumberToBeanMap != null) {
					mSmsNumberToBeanMap.remove(number);
				}
				if (mSmsNumberList != null) {
					mSmsNumberList.remove(number);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据ID标记这一条短信为已读
	 * 
	 * @param id
	 */
	public int  markSmsReadById(String id) {
		int result = 0;
		try {
			ContentValues values = new ContentValues();
			values.put(SMS_READ, SMS_READ_1); // 修改短信为已读模式
			result = mContext.getContentResolver().update(
					UnreadProvider.UPDATE_SMS, values, SMS_ID + "=?",
					new String[] { id });
			if (result > 0) {
				SmsInfoBean infoBean = null;
				for (SmsInfoBean infoBean1 : mAllSmsInfoBeans) {
					if (infoBean1.getmSmsId().equals(id)) {
						infoBean = infoBean1;
						mAllSmsInfoBeans.remove(infoBean1);
						break;
					}
				}
				if (infoBean == null) {
					return result;
				}
				if (mSmsNumberToBeanMap != null && infoBean != null) {
					Iterator<Entry<String, ArrayList<SmsInfoBean>>> iter = mSmsNumberToBeanMap
							.entrySet().iterator();
					while (iter.hasNext()) {
						Entry<String, ArrayList<SmsInfoBean>> entry = iter
								.next();
						// String key = (String) entry.getKey();
						ArrayList<SmsInfoBean> val = (ArrayList<SmsInfoBean>) entry
								.getValue();
						for (int i = 0; i < val.size(); i++) {
							if (val.contains(infoBean)) {
								val.remove(infoBean);
							}
						}
					}
					if (!mSmsNumberToBeanMap.containsKey(infoBean
							.getmAddress())) {
						mSmsNumberList.remove(infoBean.getmAddress());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据电话标记这个电话的所有未接电话为已读
	 * 
	 * @param number
	 */
	public int markPhoneReadByNumber(String number) {
		int result = 0;
		try {
			ContentValues values = new ContentValues();
			values.put(android.provider.CallLog.Calls.TYPE,
					android.provider.CallLog.Calls.INCOMING_TYPE);
			result = mContext.getContentResolver().update(
					UnreadProvider.UPDATE_PHONE, values,
					CallLog.Calls.NUMBER + "=?", new String[] { number });
			if (result > 0 && mAllPhoneBeans != null) {
				for (Iterator<CallInfoBean> iter = mAllPhoneBeans.iterator(); iter.hasNext();) {
					CallInfoBean entry = iter.next();
					if (entry.getmCallNumber().equals(number)) {
						// mUnreadSmsMap.remove(val.getmSmsId());
						iter.remove(); // 预防java.util.ConcurrentModificationException异常
					}
				}
				if (mCallNumberToBeanMap != null) {
					mCallNumberToBeanMap.remove(number);
				}
				if (mCallNumberList != null) {
					mCallNumberList.remove(number);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据电话标记这个电话的所有未接电话为已读
	 * 
	 * @param number
	 */
	public int markPhoneReadById(String id) {
		int result = 0;
		try {
			ContentValues values = new ContentValues();
			values.put(android.provider.CallLog.Calls.TYPE,
					android.provider.CallLog.Calls.INCOMING_TYPE);
			result = mContext.getContentResolver().update(
					UnreadProvider.UPDATE_PHONE, values,
					CallLog.Calls._ID + "=?", new String[] { id });
			// 若置标志成功，则清除相关数据
			if (result > 0) {
				CallInfoBean infoBean = null;
				for (CallInfoBean infoBean1 : mAllPhoneBeans) {
					if (infoBean1.getmCallId().equals(id)) {
						infoBean = infoBean1;
						mAllPhoneBeans.remove(infoBean1);
						break;
					}
				}
				if (infoBean == null) {
					return result;
				}
				if (mCallNumberToBeanMap != null && infoBean != null) {
					Iterator<Entry<String, ArrayList<CallInfoBean>>> iter = mCallNumberToBeanMap
							.entrySet().iterator();
					while (iter.hasNext()) {
						Entry<String, ArrayList<CallInfoBean>> entry = iter
								.next();
						// String key = (String) entry.getKey();
						ArrayList<CallInfoBean> val = (ArrayList<CallInfoBean>) entry
								.getValue();
						for (int i = 0; i < val.size(); i++) {
							if (val.contains(infoBean)) {
								val.remove(infoBean);
							}
						}
					}
					if (!mCallNumberToBeanMap.containsKey(infoBean
							.getmCallNumber())) {
						mCallNumberList.remove(infoBean.getmCallNumber());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void startQueryPhone() {
		if (mIsPhone) {
			mPhone ++;
			return;
		}
		mIsPhone = true;
		cleanPhone();
		Message msg = mWorkerThreadHandler.obtainMessage(EVENT_ARG_PHONE);
		msg.arg1 = EVENT_ARG_PHONE;
		WorkerArgs args = new WorkerArgs();
		args.handler = this;
		msg.obj = args;

		mWorkerThreadHandler.sendMessage(msg);
	}

	@Override
	public void handleMessage(Message msg) {
		WorkerArgs args = (WorkerArgs) msg.obj;

		int token = msg.what;
		int event = msg.arg1;
		switch (event) {
		case EVENT_ARG_SMS:
			mIsSms = false;
			mSms --;
			if (mSms > 0) {
				startQuerySms();
			} else {
				if (mCallBack != null) {
					mCallBack.smsDone();
				}
			}
			break;
		case EVENT_ARG_PHONE :
			mIsPhone = false;
			mPhone --;
			if (mPhone > 0) {
				startQuerySms();
			} else {
				if (mCallBack != null) {
					mCallBack.phoneDone();
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 从数据库获取所有未读短信
	 */
	private void startQuerySms(ContentResolver resolver, WorkerArgs args, int token, Message msg) {
		Log.d("ddd", "Heper :startQuerySms");
		Cursor cur = null;
		try {
			Uri uri = Uri.parse(SMS_URI_INBOX);
			String[] projection = new String[] { SMS_ID, SMS_THREAD_ID,
					SMS_ADDRESS, SMS_BODY, SMS_DATE, SMS_TYPE, SMS_PERSON,
					SMS_READ };
			String selection = SMS_READ + "=?";
			String[] args1 = new String[] { SMS_READ_0 };
			cur = resolver.query(uri, projection, selection, args1, null); // 获取手机内部短信
			if (cur != null && cur.moveToFirst()) {
				int indexId = cur.getColumnIndex(SMS_ID);
				int indexThreadId = cur.getColumnIndex(SMS_THREAD_ID);
				int indexAddress = cur.getColumnIndex(SMS_ADDRESS);
				int indexBody = cur.getColumnIndex(SMS_BODY);
				int indexDate = cur.getColumnIndex(SMS_DATE);
				do {
					String smsId = cur.getString(indexId);
					String smsThreadId = cur.getString(indexThreadId);
					String smsAddress = cur.getString(indexAddress);
					String smsBody = cur.getString(indexBody);
					long smsDate = cur.getLong(indexDate);
					String smsPerson = getName(smsAddress, resolver);
					String avatar = getAvatar(smsAddress, resolver);
					SmsInfoBean info = new SmsInfoBean();
					info.setType(INFO_TYPE_SMS);
					info.setmSmsId(smsId);
					info.setmSmsThreadId(smsThreadId);
					info.setmSmsBody(smsBody);
					info.setmSmsPerson(smsPerson);
					info.setmSmsDateLong(smsDate);
					info.setmSmsDate(formatTimeStampString(mContext, smsDate, false));
					info.setmAddress(smsAddress);
					info.setmAvatar(avatar);
					addSmsUnread(info);
				} while (cur.moveToNext());
			}
			sendComplteMsg(args, token, msg);
		} catch (SQLiteException ex) {
			Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
		} catch (Exception e) {
			Log.e("ERROR: ", e.toString());
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
				cur = null;
			}
		}
	}
	
	private void startQueryMissPhone(ContentResolver resolver, WorkerArgs args, int token, Message msg) {
		final String[] projection = null;
		final String selection = CallLog.Calls.TYPE + "=?";
		final String[] selectionArgs = { String
				.valueOf(CallLog.Calls.MISSED_TYPE) };
		final String sortOrder = CallLog.Calls.DATE + " DESC";
				
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(
					CallLog.Calls.CONTENT_URI, projection, selection,
					selectionArgs, sortOrder);
			if (cursor != null && cursor.moveToFirst()) {
				int callId = cursor.getColumnIndex(CallLog.Calls._ID);
				int callNumber = cursor.getColumnIndex(CallLog.Calls.NUMBER);
				int callDate = cursor.getColumnIndex(CallLog.Calls.DATE);
				int callType = cursor.getColumnIndex(CallLog.Calls.TYPE);
				int callNew = cursor.getColumnIndex(CallLog.Calls.NEW);
				int callName = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
				do {
					String callLogID = cursor.getString(callId);
					String callLogNumber = cursor.getString(callNumber);
					long callLogDate = cursor.getLong(callDate);
					int callLogType = cursor.getInt(callType);
					int isCallNew = cursor.getInt(callNew);
					String callLogName = getName(callLogNumber, resolver);
					String avatar = getAvatar(callLogNumber, resolver);
					if (isCallNew > 0
							&& android.provider.CallLog.Calls.MISSED_TYPE == callLogType) {
						CallInfoBean info = new CallInfoBean();
						info.setType(INFO_TYPE_PHONE);
						info.setmCallId(callLogID);
						info.setmAvatarUri(avatar);
						info.setmCallDateLong(callLogDate);
						info.setmCallDate(formatTimeStampString(mContext, callLogDate, false));
						info.setmCallName(callLogName);
						info.setmCallNumber(callLogNumber);
						info.setmCallType(callLogType);
						addCallUnread(info);
					}
				} while (cursor.moveToNext());
				sendComplteMsg(args, token, msg);
			}
		} catch (SQLiteException ex) {
			Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
		} catch (Exception ex) {
			Log.e("ERROR: ", ex.toString());
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
	}
	
	private void startQueryPhone(ContentResolver resolver, WorkerArgs args, int token, Message msg) {
		final String[] projection = null;
		final String selection = CallLog.Calls.TYPE + "=?";
		final String[] selectionArgs = { String
				.valueOf(CallLog.Calls.MISSED_TYPE) };
		final String sortOrder = CallLog.Calls.DATE + " DESC";
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(
					CallLog.Calls.CONTENT_URI, projection, selection,
					selectionArgs, sortOrder);
			if (cursor != null && cursor.moveToFirst()) {
				int callId = cursor.getColumnIndex(CallLog.Calls._ID);
				int callNumber = cursor.getColumnIndex(CallLog.Calls.NUMBER);
				int callDate = cursor.getColumnIndex(CallLog.Calls.DATE);
				int callType = cursor.getColumnIndex(CallLog.Calls.TYPE);
				int callNew = cursor.getColumnIndex(CallLog.Calls.NEW);
				int callName = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
				do {
					String callLogID = cursor.getString(callId);
					String callLogNumber = cursor.getString(callNumber);
					long callLogDate = cursor.getLong(callDate);
					int callLogType = cursor.getInt(callType);
					int isCallNew = cursor.getInt(callNew);
					String callLogName = getName(callLogNumber, resolver);
					String avatar = getAvatar(callLogNumber, resolver);
					if (isCallNew > 0
							&& android.provider.CallLog.Calls.MISSED_TYPE == callLogType) {
						CallInfoBean info = new CallInfoBean();
						info.setType(INFO_TYPE_PHONE);
						info.setmCallId(callLogID);
						info.setmAvatarUri(avatar);
						info.setmCallDateLong(callLogDate);
						info.setmCallDate(formatTimeStampString(mContext, callLogDate, false));
						info.setmCallName(callLogName);
						info.setmCallNumber(callLogNumber);
						info.setmCallType(callLogType);
						addCallUnread(info);
						//Log.d("ddd", "addCallUnread");
					}
				} while (cursor.moveToNext());
				sendComplteMsg(args, token, msg);
			}
		} catch (SQLiteException ex) {
			Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
		} catch (Exception ex) {
			Log.e("ERROR: ", ex.toString());
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
	}
		
	/*private void startQueryPhone(ContentResolver resolver, WorkerArgs args, int token, Message msg) {

		Log.d("ddd", "startQueryPhone start ");
		final String[] projection = new String[] { CallLog.Calls._ID,
				CallLog.Calls.NUMBER,
				CallLog.Calls.DATE,
				CallLog.Calls.TYPE,
				CallLog.Calls.NEW,
				ContactsContract.Contacts._ID,
				"DISTINCT " + CallLog.Calls.NUMBER };
		final String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
                + "1" + "'";
		final String sortOrder = String.format("%s", CallLog.Calls.DATE + " DESC");
		final String where = " 1=1) group by( " + android.provider.CallLog.Calls.NUMBER;
		Cursor cursor = null;

		try {
			cursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, 
					null,
					null, 
                    null,
					sortOrder);
						
			if (cursor != null && cursor.moveToFirst()) {
				int callId = cursor.getColumnIndex(CallLog.Calls._ID);
				int callNumber = cursor.getColumnIndex(CallLog.Calls.NUMBER);
				int callDate = cursor.getColumnIndex(CallLog.Calls.DATE);
				int callType = cursor.getColumnIndex(CallLog.Calls.TYPE);
				int callNew = cursor.getColumnIndex(CallLog.Calls.NEW);
				//int callName = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
				int contactId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
				do {
					String callLogID = cursor.getString(callId);
					//Log.d("ddd", "callLogID =" + callLogID);
					//Log.d("ddd", "contactId =" + contactId);
					String callLogNumber = cursor.getString(callNumber);
					long callLogDate = cursor.getLong(callDate);
					int callLogType = cursor.getInt(callType);
					int isCallNew = cursor.getInt(callNew);
					String callLogName = getName(callLogNumber, resolver);
					Bitmap avatar = getAvatar(callLogNumber, resolver);
					//if (isCallNew > 0
							//&& android.provider.CallLog.Calls.MISSED_TYPE == callLogType) {
						CallInfoBean info = new CallInfoBean();
						info.setType(INFO_TYPE_PHONE);
						info.setmCallId(callLogID);
						info.setmAvatarBitmap(avatar);
						info.setmCallDateLong(callLogDate);
						info.setmCallDate(formatTimeStampString(mContext, callLogDate, false));
						info.setmCallName(callLogName);
						info.setmCallNumber(callLogNumber);
						info.setmCallType(callLogType);
						
						
						addCallUnread(info);
						if (mAllPhoneBeans.size() > 4) {
							break;
						}
					}
				} while (cursor.moveToNext());
				sendComplteMsg(args, token, msg);
			}
		} catch (SQLiteException ex) {
			Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
		} catch (Exception ex) {
			Log.e("ERROR: ", ex.toString());
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
	}*/
	
	public ArrayList<SmsInfoBean> getAllSmsBean() {
		return mAllSmsInfoBeans;
	}
	
	public ArrayList<CallInfoBean> getAllPhoneBean() {
		return mAllPhoneBeans;
	}

	/**
	 * 获取所有未读短信的号码
	 * 
	 * @return
	 */
	public ArrayList<String> getAllSms() {
		return mSmsNumberList;
	}

	/**
	 * 获取所有未接电话的号码
	 * 
	 * @return
	 */
	public ArrayList<String> getAllPhone() {
		return mCallNumberList;
	}

	/**
	 * 根据电话号码获取所有这个用户的未读短信
	 * 
	 * @param number
	 * @return
	 */
	public ArrayList<SmsInfoBean> getSmsByNumber(String number) {
		if (mSmsNumberToBeanMap == null) {
			return null;
		}
		return mSmsNumberToBeanMap.get(number);
	}

	/**
	 * 根据电话号码获取所有这个用户所有的未接电话
	 * 
	 * @param number
	 * @return
	 */
	public ArrayList<CallInfoBean> getCallByNumber(String number) {
		if (mCallNumberToBeanMap == null) {
			return null;
		}
		return mCallNumberToBeanMap.get(number);
	}


	/**
	 * 添加未读短信
	 * 
	 * @param infoBean
	 */
	private void addSmsUnread(SmsInfoBean infoBean) {
		if (mAllSmsInfoBeans == null) {
			return;
		}
		if (infoBean == null) {
			return;
		}
		for (SmsInfoBean smsInfoBean : mAllSmsInfoBeans) {
			if (smsInfoBean.getmSmsId().equals(infoBean.getmSmsId())) {
				return;
			}
		}
		mAllSmsInfoBeans.add(infoBean);
		String address = infoBean.getmAddress();
		// 第一次的时候没有List就初始化一个
		if (!mSmsNumberToBeanMap.containsKey(address)) {
			ArrayList<SmsInfoBean> arrayList = new ArrayList<SmsInfoBean>();
			arrayList.add(infoBean);
			mSmsNumberToBeanMap.put(address, arrayList);
		} else {
			mSmsNumberToBeanMap.get(address).add(infoBean);
		}

		// 添加号码
		boolean find = false;
		for (int j = 0; j < mSmsNumberList.size(); j++) {
			if (mSmsNumberList.get(j).equals(address)) {
				find = true;
				break;
			}
		}
		if (!find) {
			mSmsNumberList.add(address);
		}
	}

	/**
	 * 添加未读电话
	 * 
	 * @param infoBean
	 */
	private void addCallUnread(CallInfoBean infoBean) {
		if (infoBean == null) {
			return;
		}
		if (mAllPhoneBeans == null) {
			return;
		}
		for (CallInfoBean callInfoBean : mAllPhoneBeans) {
			if (callInfoBean.getmCallId().equals(infoBean.getmCallId())) 
			{
				return;
			}
		}
		
		mAllPhoneBeans.add(infoBean);
		String address = infoBean.getmCallNumber();
		// 第一次的时候没有List就初始化一个
		if (!mCallNumberToBeanMap.containsKey(address)) {
			ArrayList<CallInfoBean> arrayList = new ArrayList<CallInfoBean>();
			arrayList.add(infoBean);
			mCallNumberToBeanMap.put(address, arrayList);
		} else {
			mCallNumberToBeanMap.get(address).add(infoBean);
		}

		// 添加号码
		boolean find = false;
		for (int j = 0; j < mCallNumberList.size(); j++) {
			if (mCallNumberList.get(j).equals(address)) {
				find = true;
				break;
			}
		}
		if (!find) {
			mCallNumberList.add(address);
		}
	}

	private String getName(String number, ContentResolver resolver) {
		String name = "";
		number = PhoneNumberUtils.stripSeparators(number);
		if (number == null || "".equals(number)) {
			number = " ";
		}
		String selection = CALLER_ID_SELECTION.replace("+",
				PhoneNumberUtils.toCallerIDMinMatch(number));
		selection = selection.replace("??", "'" + number + "'");
		Cursor cursor = null;
		try {
			cursor = resolver.query(Data.CONTENT_URI, CALLER_ID_PROJECTION,
					selection, new String[] { number }, null);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					name = cursor.getString(CONTACT_NAME_COLUMN);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		if (name.equals("") || name == null) {
			return number;
		} else {
			return name;
		}
	}

	/*private Bitmap getAvatar(String number, ContentResolver resolver) {
		if (number == null || number.equals("")) {
			return null;
		}
		// 先看缓存有没有，若有，则直接拿缓存的
		Bitmap avatar = manager.getBitmapFormCache(number);
		Cursor cursorCantacts = null;
		if (avatar != null) {
			return avatar;
		}
		try {
			cursorCantacts = resolver.query(Uri.withAppendedPath(
					ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
					number), null, null, null, null);
			if (cursorCantacts.getCount() > 0) { // 若游标不为0则说明有头像,游标指向第一条记录
				cursorCantacts.moveToFirst();
				Long contactID = cursorCantacts
						.getLong(cursorCantacts
								.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));
				Uri uri = ContentUris.withAppendedId(
						ContactsContract.Contacts.CONTENT_URI, contactID);
				InputStream input = ContactsContract.Contacts
						.openContactPhotoInputStream(resolver, uri);
				avatar = BitmapFactory.decodeStream(input);
				if (!manager.isContain(number) && avatar != null) {
					manager.saveBitmapToCache(number, avatar);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursorCantacts != null && !cursorCantacts.isClosed()) {
				cursorCantacts.close();
				cursorCantacts = null;
			}
		}
		return avatar;
	}*/
	
	private String getAvatar(String number, ContentResolver resolver) {
		if (number == null || number.equals("")) {
			return null;
		}
		Cursor cursorCantacts = null;
		Uri uri = null;
		try {
			cursorCantacts = resolver.query(Uri.withAppendedPath(
					ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
					number), null, null, null, null);
			if (cursorCantacts.getCount() > 0) { // 若游标不为0则说明有头像,游标指向第一条记录
				cursorCantacts.moveToFirst();
				Long contactID = cursorCantacts
						.getLong(cursorCantacts
								.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));
				uri = ContentUris.withAppendedId(
						ContactsContract.Contacts.CONTENT_URI, contactID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursorCantacts != null && !cursorCantacts.isClosed()) {
				cursorCantacts.close();
				cursorCantacts = null;
			}
		}
		return uri == null ? "" : uri.toString();
	}
	

	private static final String CALLER_ID_SELECTION = getCallerIdSelection();
	private static final String[] CALLER_ID_PROJECTION = getCallerIdProjection();
	// add by zhouchaohong
	public static final String AUTHORITY = "com.android.contacts";
	/** A content:// style uri to the authority for the contacts provider */
	public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

	/**
	 * 
	 * @author zhangjie 模拟实现
	 *         android.provider.ContactsContract.CommonDataKinds.Data;
	 */
	public static class Data {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				AUTHORITY_URI, "data");
		public static final String MIMETYPE = "mimetype";
		public static final String RAW_CONTACT_ID = "raw_contact_id";
	}

	/**
	 * 模拟实现 import android.provider.ContactsContract.Phone;
	 * 
	 * @author zhangjie
	 * 
	 */
	public static class Phone {
		public static final String NUMBER = "data1";
		public static final String LABEL = "data3";
		public static final String DISPLAY_NAME = "display_name";
		public static final String CONTACT_ID = "contact_id";
		public static final String CONTACT_PRESENCE = "contact_presence";
		public static final String CONTACT_STATUS = "contact_status";

		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/phone_v2";

		public static final String CONTACT_LOOKUPKEY = "lookup";
	}

	// Android 1.6 只使用前面0-4字段
	// Android 2.2
	private static final int PHONE_NUMBER_COLUMN = 0;
	private static final int PHONE_LABEL_COLUMN = 1;
	private static final int CONTACT_NAME_COLUMN = 2;
	private static final int CONTACT_ID_COLUMN = 3;
	private static final int CONTACT_PRESENCE_COLUMN = 4;
	private static final int CONTACT_STATUS_COLUMN = 5;

	private static final int CONTACT_LOOKUPKEY_COLUMN = 6; // zhouchaohong
															// 用于Android
															// 2.0以上版本查找联系人

	private static String[] getCallerIdProjection() {
		String[] mCALLERIDPROJECTION = null;

		// Andriod 2.1 2.2
		if (Build.VERSION.SDK_INT >= 7) {
			mCALLERIDPROJECTION = new String[] { Phone.NUMBER, // 0
					Phone.LABEL, // 1
					Phone.DISPLAY_NAME, // 2
					Phone.CONTACT_ID, // 3
					Phone.CONTACT_PRESENCE, // 4
					Phone.CONTACT_STATUS, // 5

					Phone.CONTACT_LOOKUPKEY, // 6 //zhouchaohong 用于Android
												// 2.0以上版本查找联系人
			};
		}
		// Android 2.0.1 2.0

		if (Build.VERSION.SDK_INT == 6 || Build.VERSION.SDK_INT == 5) {
			mCALLERIDPROJECTION = new String[] { Phone.NUMBER, // 0
					Phone.LABEL, // 1
					Phone.DISPLAY_NAME, // 2
					Phone.CONTACT_ID, // 3
					Phone.CONTACT_PRESENCE, // 4
					Phone.CONTACT_STATUS, // 5
			};
		}

		return mCALLERIDPROJECTION;
	}

	private static String getCallerIdSelection() {

		String mCALLERIDSELECTION = null;

		// Android 3.0 4.0
		if (Build.VERSION.SDK_INT >= 11) {
			mCALLERIDSELECTION = "PHONE_NUMBERS_EQUAL(" + Phone.NUMBER
					+ ",?) AND " + Data.MIMETYPE + "='"
					+ Phone.CONTENT_ITEM_TYPE + "'" + " AND "
					+ Data.RAW_CONTACT_ID + " IN " + "(SELECT raw_contact_id "
					+ " FROM phone_lookup" + " WHERE min_match = '+')";
			return mCALLERIDSELECTION;
		}

		// Android 2.1 2.2
		if (Build.VERSION.SDK_INT >= 7) {

			mCALLERIDSELECTION = "PHONE_NUMBERS_EQUAL(" + Phone.NUMBER
					+ ",?) AND " + Data.MIMETYPE + "='"
					+ Phone.CONTENT_ITEM_TYPE + "'" + " AND "
					+ Data.RAW_CONTACT_ID + " IN " + "(SELECT raw_contact_id "
					+ " FROM phone_lookup"
					+ " WHERE normalized_number GLOB('+*')) ) "
					+ " UNION ALL SELECT " + Phone.NUMBER + "," + Phone.LABEL
					+ "," + Phone.DISPLAY_NAME + "," + Phone.CONTACT_ID + ","
					+ "1 as " + Phone.CONTACT_PRESENCE + "," + "'' as "
					+ Phone.CONTACT_STATUS + "," + Phone.CONTACT_LOOKUPKEY
					+ " " + "FROM view_data " + "WHERE "
					+ "PHONE_NUMBERS_EQUAL(" + Phone.NUMBER + ",??) AND "
					+ Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'"
					+ " AND " + Data.RAW_CONTACT_ID + " IN "
					+ "(SELECT raw_contact_id " + " FROM phone_lookup"
					+ " WHERE normalized_number GLOB('+*') ";
			return mCALLERIDSELECTION;
		}
		return mCALLERIDSELECTION;
	}
	
	public void clean() {
		manager = null;
		cleanSms();
		cleanPhone();
	}
	
	/**
	 * 转换时间格式
	 * 
	 * @param context
	 * @param when
	 * @param fullFormat
	 * @return
	 */
	private String formatTimeStampString(Context context, long when,
			boolean fullFormat) {
		if (context == null) {
			return String.valueOf(when);
		}
		Time then = new Time();
		then.set(when);
		Time now = new Time();
		now.setToNow();

		// Basic settings for formatDateTime() we want for all cases.
		int formatflags = DateUtils.FORMAT_NO_NOON_MIDNIGHT
				| DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_CAP_AMPM;

		// If the message is from a different year, show the date and year.
		if (then.year != now.year) {
			formatflags |= DateUtils.FORMAT_SHOW_YEAR
					| DateUtils.FORMAT_SHOW_DATE;
		} else if (then.yearDay != now.yearDay) {
			// If it is from a different day than today, show only the date.
			formatflags |= DateUtils.FORMAT_SHOW_DATE;
		} else {
			// Otherwise, if the message is from today, show the time.
			formatflags |= DateUtils.FORMAT_SHOW_TIME;
		}

		// If the caller has asked for full details, make sure to show the date
		// and time no matter what we've determined above (but still make
		// showing
		// the year only happen if it is a different year from today).
		if (fullFormat) {
			formatflags |= DateUtils.FORMAT_SHOW_DATE
					| DateUtils.FORMAT_SHOW_TIME;
		}

		return DateUtils.formatDateTime(context, when, formatflags);
	}
	
	/**
	 * 查询完成的回调接口
	 * @author zhangjie
	 *
	 */
	public interface QueryCallBack {
		public void smsDone();
		public void phoneDone();
	}
	
	
	
}
