package com.jiubang.goscreenlock.theme.cjpcardcool.weather.location.celllocation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

/**
 * 
 * 类描述: 获取基站信息
 * 
 * @author  liuwenqin
 * @date  [2012-9-4]
 */
public class CellIDInfoManager
{

	public ArrayList<CellIDInfo> getCellIDInfo(Context context)
	{
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		ArrayList<CellIDInfo> cellID = new ArrayList<CellIDInfo>();
		android.telephony.CellLocation cellLocation = manager.getCellLocation();
		if (null == cellLocation)
		{
			return null;
		}
		else
		{
			final int mccMncLen = 5;
			final int mccLen = 3;
			if (cellLocation instanceof GsmCellLocation)
			{
				GsmCellLocation gsm = (GsmCellLocation) cellLocation;
				int lac = gsm.getLac();
				String mccMnc = manager.getNetworkOperator();
				CellIDInfo currentCell = new CellIDInfo();
				if (mccMnc != null && mccMnc.length() >= mccMncLen)
				{
					currentCell.mMobileCountryCode = parseMccMnc(mccMnc.substring(0, mccLen));
					currentCell.mMobileNetworkCode = parseMccMnc(mccMnc
							.substring(mccLen, mccMncLen));
				}
				else
				{
					currentCell.mMobileCountryCode = 0;
					currentCell.mMobileNetworkCode = 0;
				}
				currentCell.mCellId = gsm.getCid();
				currentCell.mLocationAreaCode = lac;
				currentCell.mRadioType = "gsm";
				cellID.add(currentCell);

				List<NeighboringCellInfo> list = manager.getNeighboringCellInfo();
				if (list != null)
				{
					int size = list.size();
					for (int i = 0; i < size; i++)
					{
						CellIDInfo info = new CellIDInfo();
						info.mCellId = list.get(i).getCid();
						info.mLocationAreaCode = lac;
						cellID.add(info);
					}
				}
				return cellID;
			}
			else if (cellLocation instanceof CdmaCellLocation)
			{
				/**
				 * CDMA TelephonyManager.NETWORK_TYPE_EVDO_A 中国电信3G
				 * TelephonyManager.NETWORK_TYPE_CDMA 中国电信2G
				 * TelephonyManager.NETWORK_TYPE_1xRTT
				 */
				CdmaCellLocation cdma = (CdmaCellLocation) cellLocation;
				// int sid = cdma.getSystemId(); //系统标识
				int bid = cdma.getBaseStationId(); // 基站小区号
				int nid = cdma.getNetworkId(); // 网络标识
				int sid = cdma.getSystemId();
				CellIDInfo info = new CellIDInfo();
				info.mCellId = bid;
				info.mLocationAreaCode = nid;
				String mccMnc = manager.getNetworkOperator();
				info.mMobileNetworkCode = sid;

				if (mccMnc != null && mccMnc.length() >= mccMncLen)
				{
					info.mMobileCountryCode = parseMccMnc(mccMnc.substring(0, mccLen));
					//					info.mobileNetworkCode = parseMccMnc(mccMnc.substring(3, 5));
				}
				else
				{
					info.mMobileCountryCode = 0;
					//					info.mobileNetworkCode = 0;
				}
				info.mRadioType = "cdma";
				cellID.add(info);
				return cellID;
			}
			return null;
		}
	}

	private int parseMccMnc(String codeString)
	{
		int code = 0;
		try
		{
			code = Integer.parseInt(codeString);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return code;
	}
}