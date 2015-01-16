package com.jiubang.goscreenlock.theme.cjpcardcool.smstool;

import java.io.Serializable;
/**
 * 
 * @author zhangjie
 *
 */
public class CallInfoBean extends BaseInfo implements Serializable {
	private static final long	serialVersionUID	= 4403L;
	
	private String mCallId;
	private String mCallNumber;
	private String mCallDate;
	private long mCallDateLong;
	private int mCallType;
	private String mCallName;
	private String mAvatar; //Uri没有序列号，如果需要头像，调用Uri.parse()还原

	@Override
	public String toString() {
		String str = "mCallId :" + mCallId + ", mCallNumber:" + mCallNumber
				+ ", mCallDate:" + mCallDate + ", mCallType:" + mCallType
				+ ", mCallName" + mCallName;
		return str;
	}
	
	public CallInfoBean() {
		
	}

	public CallInfoBean(String mCallId, String mCallNumber, String mCallDate,
			int mCallType, String mCallName, String avatar) {
		this.mCallId = mCallId;
		this.mCallNumber = mCallNumber;
		this.mCallDate = mCallDate;
		this.mCallType = mCallType;
		this.mCallName = mCallName;
		this.mAvatar = avatar;
	}

	public long getmCallDateLong() {
		return mCallDateLong;
	}

	public void setmCallDateLong(long mCallDateLong) {
		this.mCallDateLong = mCallDateLong;
	}

	public String getmCallId() {
		return mCallId;
	}

	public void setmCallId(String mCallId) {
		this.mCallId = mCallId;
	}

	public String getmCallNumber() {
		return mCallNumber;
	}

	public void setmCallNumber(String mCallNumber) {
		this.mCallNumber = mCallNumber;
	}

	public String getmCallDate() {
		return mCallDate;
	}

	public void setmCallDate(String mCallDate) {
		this.mCallDate = mCallDate;
	}

	public int getmCallType() {
		return mCallType;
	}

	public void setmCallType(int mCallType) {
		this.mCallType = mCallType;
	}

	public String getmCallName() {
		return mCallName;
	}

	public void setmCallName(String mCallName) {
		this.mCallName = mCallName;
	}

	public String getmAvatarUri() {
		return mAvatar;
	}

	public void setmAvatarUri(String avatar) {
		this.mAvatar = avatar;
	}
	
	//序列化开始
	/**
	 * 还原
	 * 
	 * @param data
	 */
	/*public void setCallInfoBeanByByteArray(byte data[])
	{
		ArrayList<CallInfoBean> list2 = null;
		
		if (null != data)
		{
			
			InputStream is = new ByteArrayInputStream(data);
			ObjectInputStream oi = null;
			try
			{
				oi = new ObjectInputStream(is);
				list2 = (ArrayList<CallInfoBean>) oi.readObject();
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
			setCallInfoBeanList(list2);
		}
	}
	
	private ArrayList<CallInfoBean> mCallInfoBeanList;
	private void setCallInfoBeanList(ArrayList<CallInfoBean> list)
	{
		mCallInfoBeanList = list;
	}
	// 天气预报
	public ArrayList<CallInfoBean> getCallInfoBeanList()
	{
		return mCallInfoBeanList;
	}
	
	*//**
	 * 字节化数据
	 *//*
	public byte[] setCallInfoBeanByList()
	{
		ArrayList<CallInfoBean> list = mCallInfoBeanList;
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
