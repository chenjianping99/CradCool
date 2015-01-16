package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;
//CHECKSTYLE OFF
//import android.provider.Telephony;

/**
 * <br>类描述:mtk的移动数据开关器
 * <br>注意:从SwitchWidget抽离调整可以使用的代码
 * 
 * @date  [2013-1-5]
 */
class MtkGprsHandler implements ISwitcherable {
//	private Context mContext = null;
//	private MtkMobileDataReceiver mReceiver = null;
//	private int mSimId;
//	
//	public MtkGprsHandler(Context context) {
//		mContext = context;
//
//		mReceiver = new MtkMobileDataReceiver();
//		IntentFilter filter = new IntentFilter(MtkGprs.GPRS_CHANGE_ACTION);
//		mContext.registerReceiver(mReceiver, filter);
//	}
//
//
//	/**
//	 * 
//	 * <br>类描述:mtk Gprs移动数据监听
//	 * 
//	 * @date  [2013-1-5]
//	 */
//	private class MtkMobileDataReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			if (action.equals(MtkGprs.GPRS_CHANGE_ACTION)) {
//				int simId = (int) intent.getLongExtra(MtkGprs.GPRS_SIMID_KEY, 0L);
//				mSimId = simId;
//				broadCastState();
//			}
//		}
//	}
//
	@Override
	public void switchState() {
//		List<Telephony.SIMInfo> simList = MtkGprs.getInsertedSimList(mContext);
//		if (simList == null) {
//			// 当前获取失败或没有插入SIM卡
//			MtkGprs.closeMobileDataConnection(mContext);
//			return;
//		}
//
//		int size = simList.size();
//		int simId = MtkGprs.getDataConnectedSimId(mContext);
//		if (simId == 0) {
//			// 打开第一个SIM卡的移动网络数据
//			MtkGprs.openMobileDataConnection(mContext, (int) simList.get(0).mSimId);
//			return;
//		}
//
//		if (simId == simList.get(size - 1).mSimId) {
//			// 关闭移动网络数据
//			MtkGprs.closeMobileDataConnection(mContext);
//			return;
//		}
//
//		for (int i = 0; i < size - 1; i++) {
//			Telephony.SIMInfo sim = simList.get(i);
//			if (simId == sim.mSimId) {
//				// 打开下一个移动网络数据
//				MtkGprs.openMobileDataConnection(mContext, (int) simList.get(i + 1).mSimId);
//				break;
//			}
//		}
	}

	@Override
	public void broadCastState() {
//		Intent intent = new Intent(BroadcastBean.GPRS_CHANGE);
//
//		if (mSimId == 0) {
//			// 没有打开移动数据，直接显示第一张图片
//			intent.putExtra(BroadcastBean.STATUS, 0);
//			mContext.sendBroadcast(intent);
//			return;
//		}
//
//		List<Telephony.SIMInfo> simList = MtkGprs.getInsertedSimList(mContext);
//		if (simList == null) {
//			// 获取失败，直接显示第一张图片
//			intent.putExtra(BroadcastBean.STATUS, 0);
//			mContext.sendBroadcast(intent);
//			return;
//		}
//
//		for (int i = 0; i < simList.size(); i++) {
//			Telephony.SIMInfo sim = simList.get(i);
//			if (mSimId == sim.mSimId) {
//				// 显示指定图片
//				intent.putExtra(BroadcastBean.STATUS, i + 1);
//				mContext.sendBroadcast(intent);
//				return;
//			}
//		}
	}

	@Override
	public void cleanUp() {
//		if (mReceiver != null) {
//			mContext.unregisterReceiver(mReceiver);
//			mReceiver = null;
//		}
	}

	@Override
	public int getSwitchType() {
		return SWITCH_TYPE_MTK_GPRS;
	}
}