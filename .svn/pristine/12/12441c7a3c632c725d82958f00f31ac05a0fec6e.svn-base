package com.jiubang.goscreenlock.theme.cjpcardcool.smstool;

import java.io.Serializable;

/**
 * 未读信息
 * 
 * @author zhangjie
 * 
 */
public class SmsInfoBean extends BaseInfo implements Serializable {
	private static final long	serialVersionUID	= 4402L;
	
	private String mSmsId;
	private String mSmsThreadId;
	private String mAddress;
	private String mSmsBody;
	private String mSmsDateStr;
	private long mSmsDateLong;
	private String mSmsType;
	private String mSmsPerson;
	private String mSmsRead;
	private String mAvatar; // 联系人头像  Uri没有序列号，如果需要头像，调用Uri.parse()还原

	@Override
	public String toString() {
		String str = "mSmsId :" + mSmsId + ", mSmsThreadId:" + mSmsThreadId
				+ ", mAddress:" + mAddress + ", mSmsBody:" + mSmsBody
				+ ", mSmsDate" + mSmsDateStr + ", mSmsType:" + mSmsType
				+ ", mSmsPerson:" + mSmsPerson + ", mSmsRead:" + mSmsRead;
		return str;
	}
	
	public SmsInfoBean() {
		
	}

	public SmsInfoBean(String mSmsId, String mSmsThreadId, String mAddress,
			String mSmsBody, String mSmsDate, String mSmsType,
			String mSmsPerson, String mSmsRead, String avatar) {
		this.mSmsId = mSmsId;
		this.mSmsThreadId = mSmsThreadId;
		this.mAddress = mAddress;
		this.mSmsBody = mSmsBody;
		this.mSmsDateStr = mSmsDate;
		this.mSmsType = mSmsType;
		this.mSmsPerson = mSmsPerson;
		this.mSmsRead = mSmsRead;
		mAvatar = avatar;
	}

	public long getmSmsDateLong() {
		return mSmsDateLong;
	}

	public void setmSmsDateLong(long mSmsDateLong) {
		this.mSmsDateLong = mSmsDateLong;
	}

	public String getmSmsId() {
		return mSmsId;
	}

	public void setmSmsId(String mSmsId) {
		this.mSmsId = mSmsId;
	}

	public String getmSmsThreadId() {
		return mSmsThreadId;
	}

	public void setmSmsThreadId(String mSmsThreadId) {
		this.mSmsThreadId = mSmsThreadId;
	}

	public String getmAddress() {
		return mAddress;
	}

	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public String getmSmsBody() {
		return mSmsBody;
	}

	public void setmSmsBody(String mSmsBody) {
		this.mSmsBody = mSmsBody;
	}

	public String getmSmsDate() {
		return mSmsDateStr;
	}

	public void setmSmsDate(String mSmsDate) {
		this.mSmsDateStr = mSmsDate;
	}

	public String getmSmsType() {
		return mSmsType;
	}

	public void setmSmsType(String mSmsType) {
		this.mSmsType = mSmsType;
	}

	public String getmSmsPerson() {
		return mSmsPerson;
	}

	public void setmSmsPerson(String mSmsPerson) {
		this.mSmsPerson = mSmsPerson;
	}

	public String getmSmsRead() {
		return mSmsRead;
	}

	public void setmSmsRead(String mSmsRead) {
		this.mSmsRead = mSmsRead;
	}

	public String getmAvatar() {
		return mAvatar;
	}

	public void setmAvatar(String avatar) {
		this.mAvatar = avatar;
	}
	
	/**
	 * 还原
	 * 
	 * @param data
	 */
	/*public void setSmsInfoBeanByByteArray(byte data[])
	{
		ArrayList<SmsInfoBean> list2 = null;
		
		if (null != data)
		{
			
			InputStream is = new ByteArrayInputStream(data);
			ObjectInputStream oi = null;
			try
			{
				oi = new ObjectInputStream(is);
				list2 = (ArrayList<SmsInfoBean>) oi.readObject();
				is.close();
				oi.close();
			}
			catch (StreamCorruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if (null != is)
					{
						is.close();
					}
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try
				{
					if (null != oi)
					{
						oi.close();
					}
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		if (null != list2)
		{
			setSmsInfoBeanList(list2);
		}
	}
	
	private ArrayList<SmsInfoBean> mSmsInfoBeanList;
	private void setSmsInfoBeanList(ArrayList<SmsInfoBean> list)
	{
		mSmsInfoBeanList = list;
	}
	// 天气预报
	public ArrayList<SmsInfoBean> getSmsInfoBeanList()
	{
		return mSmsInfoBeanList;
	}
	
	*//**
	 * 字节化数据
	 *//*
	public byte[] setSmsInfoBeanByList()
	{
		ArrayList<SmsInfoBean> list = mSmsInfoBeanList;
		if (null == list)
		{
			return null;
		}
		
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream os = null;
		byte[] weaterpre = null;
		try
		{
			
			os = new ObjectOutputStream(bo);
			os.writeObject(list);
			os.flush();
			weaterpre = bo.toByteArray();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (null != os)
			{
				try
				{
					os.close();
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != bo)
			{
				try
				{
					bo.close();
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return weaterpre;
	}*/

}
