package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;


/**
 * 
 * <br>类描述:空的开关器
 * <br>注意:没有实现
 * 
 * @author  guoyiqing
 * @date  [2013-1-8]
 */
public class EmptySwitchHandler implements ISwitcherable {

	private static final String TAG = "EmptySwitchHandler";
	
	@Override
	public void switchState() {
//		LogUtils.log(TAG, new Exception("should not action"));
	}

	@Override
	public void broadCastState() {
//		LogUtils.log(TAG, new Exception("should not action"));
	}

	@Override
	public void cleanUp() {
//		LogUtils.log(TAG, new Exception("should not action"));
	}

	@Override
	public int getSwitchType() {
//		LogUtils.log(TAG, new Exception("should not action"));
		return SWITCH_TYPE_EMPTY;
	}

}
