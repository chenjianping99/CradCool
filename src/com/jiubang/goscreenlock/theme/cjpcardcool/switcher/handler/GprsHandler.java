package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;

import android.content.Context;

/**
 * 
 * <br>类描述:统一了{@link NormalGprsHandler}和 {@link MtkGprsHandler}
 * 
 * @author  guoyiqing
 * @date  [2013-1-8]
 */
public class GprsHandler implements ISwitcherable {

	private ISwitcherable mSwitcherable;
	
	public GprsHandler(Context context, boolean isPersistent) {
//		if (MtkGprs.isMtkWare()) {
//			mSwitcherable = SwitchHandlerFactory.getFactory()
//					.getSwicher(context, ISwitcherable.SWITCH_TYPE_MTK_GPRS);
//		} else {
			mSwitcherable = SwitchHandlerFactory.getFactory()
					.getSwicher(context, ISwitcherable.SWITCH_TYPE_NORMAL_GPRS, isPersistent);
//		}
	}
	
	@Override
	public void switchState() {
		if (mSwitcherable != null) {
			mSwitcherable.switchState();
		}
	}

	@Override
	public void broadCastState() {
		if (mSwitcherable != null) {
			mSwitcherable.broadCastState();
		}
	}

	@Override
	public void cleanUp() {
		if (mSwitcherable != null) {
			mSwitcherable.cleanUp();
		}
	}

	@Override
	public int getSwitchType() {
		return ISwitcherable.SWITCH_TYPE_GPRS;
	}

}
