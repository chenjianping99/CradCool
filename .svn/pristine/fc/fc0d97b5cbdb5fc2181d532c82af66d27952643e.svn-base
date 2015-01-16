package com.jiubang.goscreenlock.theme.cjpcardcool.weather.location.celllocation;

import android.os.Parcel;
import android.os.Parcelable;

/** 封装wifi的信息 */
public class WifiInfo implements Parcelable
{

	String	mac;

	public WifiInfo()
	{
	}

	public void setMac(String mac)
	{
		this.mac = mac;
	}

	public String getMac()
	{
		return mac;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	public static final Parcelable.Creator<WifiInfo>	CREATOR	= new Parcelable.Creator<WifiInfo>()
																{
																	public WifiInfo createFromParcel(
																			Parcel in)
																	{
																		return new WifiInfo(in);
																	}

																	public WifiInfo[] newArray(
																			int size)
																	{
																		return new WifiInfo[size];
																	}
																};

	private WifiInfo(Parcel in)
	{
		mac = in.readString();
	}

	@Override
	public void writeToParcel(Parcel out, int flags)
	{
		out.writeString(mac);
	}
}