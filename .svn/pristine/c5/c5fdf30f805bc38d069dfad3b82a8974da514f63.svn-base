package com.jiubang.goscreenlock.theme.cjpcardcool;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * @author huyong
 * 
 */
public class HttpMachine 
{

	public static final int NETTYPE_MOBILE = 0;
	public static final int NETTYPE_UNICOM = 1;
	public static final int NETTYPE_TELECOM = 2;
	
	public static final String CTWAP = "ctwap";
	public static final String CMWAP = "cmwap";
	public static final String WAP_3G = "3gwap";
	public static final String UNIWAP = "uniwap";
	
	public static final int		CONNENCTION_CMNET	= 0;		// 网络类型是cmnet
	public static final int		CONNENCTION_CMWAP	= 1;		// 网络类型是cmwap
	public static final int		CONNENCTION_WIFI	= 2;		// 网络类型是wifi
	public static final int		CONNENCTION_CTWAP	= 3;		// 网络类型是ctwap,电信wap
	public static final int		CONNENCTION_NO_NET	= -1;		// 无法连接到网络	
	public static Uri sPREFERREDAPNURI = Uri
			.parse("content://telephony/carriers/preferapn");

	public static boolean isCWWAPConnect(Context context) 
	{
		boolean result = false;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) 
		{
			if (Proxy.getDefaultHost() != null
					|| Proxy.getHost(context) != null) 
			{
				result = true;
			}
		}

		return result;
	}

	public static int getNetWorkType(Context context) 
	{
		int netType = -1;

		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String simOperator = manager.getSimOperator();
		if (simOperator != null) {
			if (simOperator.startsWith("46000")
					|| simOperator.startsWith("46002")) 
			{
				netType = NETTYPE_MOBILE;
			} else if (simOperator.startsWith("46001")) 
			{
				netType = NETTYPE_UNICOM;
			} else if (simOperator.startsWith("46003")) 
			{
				netType = NETTYPE_TELECOM;
			}
		}
		return netType;
	}
	
	/**
	 * 添加电信wap网络的判断
	 * @param context
	 * @return
	 */
	public static int getChinaNetWorkType(Context context)
	{
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info == null
				|| (info.getState() != NetworkInfo.State.CONNECTING && info.getState() != NetworkInfo.State.CONNECTED))
		{
			return CONNENCTION_NO_NET;
		}
		if (info.getType() == ConnectivityManager.TYPE_WIFI)
		{
			return CONNENCTION_WIFI;
		}
		else if (info.getType() == ConnectivityManager.TYPE_MOBILE)
		{
			if (Proxy.getDefaultHost() != null || Proxy.getHost(context) != null)
			{
				final Cursor c = context.getContentResolver().query(
						sPREFERREDAPNURI, null, null, null, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					final String user = c.getString(c.getColumnIndex("user"));
					if (!TextUtils.isEmpty(user)) {
						if (user.startsWith(CTWAP)) {
							return CONNENCTION_CTWAP;
						}
					}
				}
				c.close();
				
				String netMode = info.getExtraInfo();
				if (netMode != null) {
					// 通过apn名称判断是否是联通和移动wap
					netMode = netMode.toLowerCase();
					if (netMode.equals(CMWAP) || netMode.equals(WAP_3G)
							|| netMode.equals(UNIWAP)) {
						return CONNENCTION_CMWAP;
					}
				}
			}
			return CONNENCTION_CMNET;
		}
		return -1;
		
	}

	/**
	 * @author huyong
	 * @param context
	 * @return
	 */
	public static String getProxyHost(Context context) 
	{
		return Proxy.getHost(context);
	}

	public static int getProxyPort(Context context) 
	{
		return Proxy.getPort(context);
	}

}
