package com.jiubang.goscreenlock.theme.cjpcardcool.weather.location.celllocation;

import java.util.ArrayList;

import android.content.Context;
import android.net.wifi.WifiManager;

/**获取当前连接的wifi的信息 */
public class WifiInfoManager
{

	public ArrayList<WifiInfo> getWifiInfo(Context context)
	{
		ArrayList<WifiInfo> wifi = new ArrayList<WifiInfo>();
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

		//		boolean result = wm.startScan();
		//		if(result){
		//			List<ScanResult> results = wm.getScanResults();
		//			if(results != null){
		//				for(int i = 0; i < results.size(); i++){
		//					WifiInfo info = new WifiInfo();
		//					info.mac = results.get(i).BSSID;
		//					wifi.add(info);
		//					ScanResult scanResult = results.get(i);
		//					Log.d("Test", "SSID = " + scanResult.SSID + ", level = " + scanResult.level + ", bssid = " + scanResult.BSSID);
		//				}
		//			} else {
		//				Log.d("Test", "scan result null >>>>>>>>>>>>>>>>>");
		//			}
		//		} else {
		//			WifiInfo info = new WifiInfo();
		//			android.net.wifi.WifiInfo wifiInfo = wm.getConnectionInfo();
		//			info.mac = wm.getConnectionInfo().getBSSID();
		//			Log.d("Test", "connected wifi rssi = " + wifiInfo.getRssi() + ", bssd = " + wifiInfo.getBSSID());
		//			wifi.add(info);
		//		}
		if (wm != null)
		{
			WifiInfo info = new WifiInfo();
			android.net.wifi.WifiInfo wifiInfo = wm.getConnectionInfo();
			if (wifiInfo != null)
			{
				info.mac = wifiInfo.getBSSID();
				wifi.add(info);
			}
		}
		return wifi;
	}
}