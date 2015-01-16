package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.bean;


/**
 * 开关类型
 * @author zhangjie
 *
 */
public class SliderSwitch extends BaseApp {
	private String mSwitchName; // 开关名称
	private int mSwitchId; // 开关类型
	private int mSwitchStatus; //开关状态
	private int mSwitchRemark; //开关状态2，主要是用于电池电量
	
	public SliderSwitch() {
		super();
		mBaseType = SwipeTypes.SWITCH_TYPE;
	}

	public SliderSwitch(SwitchItem item) {
		super();
		this.mSwitchName = item.mSwitchName;
		this.mSwitchId = item.mSwitchType;
		mPosition = item.mPosition;
		mBaseType = SwipeTypes.SWITCH_TYPE;
	}

	public int getmSwitchStatus() {
		return mSwitchStatus;
	}

	public void setmSwitchStatus(int mSwitchStatus) {
		this.mSwitchStatus = mSwitchStatus;
	}

	public int getmSwitchRemark() {
		return mSwitchRemark;
	}

	public void setmSwitchRemark(int mSwitchRemark) {
		this.mSwitchRemark = mSwitchRemark;
	}

	public int getmSwitchId() {
		return mSwitchId;
	}

	public void setmSwitchId(int mSwitchType) {
		this.mSwitchId = mSwitchType;
	}

	public String getmSwitchName() {
		return mSwitchName;
	}

	public void setmSwitchName(String mSwitchName) {
		this.mSwitchName = mSwitchName;
	}

	@Override
	protected boolean compare(BaseApp baseApp) {
		if (baseApp == null) {
			return false;
		}
		if (baseApp instanceof SliderSwitch) {
			SliderSwitch sliderSwitch = (SliderSwitch) baseApp;
			if (compareStr(sliderSwitch.getmSwitchName(), mSwitchName)) {
				return true;
			}
		}
		return false;
	}
}
