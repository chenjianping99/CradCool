package com.jiubang.goscreenlock.theme.cjpcardcool.switcher.handler;
//CHECKSTYLE OFF
//import java.util.List;

//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.provider.Settings;
//import android.provider.Telephony;

/**
 * 用于获取、改变MTK移动数据状态的类。
 * @author huchao
 *
 */
public class MtkGprs {
//	public static final String GPRS_CHANGE_ACTION = "android.intent.action.DATA_DEFAULT_SIM";
//	public static final String GPRS_SIMID_KEY = "simid";
//	private static final String GPRS_CONNECTION_SETTING = "gprs_connection_sim_setting";
//
//	/**
//	 * 判断当前手机是否基于MTK芯片的
//	 * @return 当前手机是否基于MTK芯片
//	 */
//    public static boolean isMtkWare() {
//    	if (Build.VERSION.SDK_INT <= 7) {
//    		// MTK判断方法不支持Android2.1以下的手机
//    		return false;
//    	}
//    	
//    	String hardware = Build.HARDWARE;
//    	hardware.trim();
//    	hardware.toLowerCase();
//    	
//    	if (hardware.length() <= "mt".length()) {
//    		return false;
//    	}
//    	
//    	if (hardware.startsWith("mt")) {
//    		return true;
//    	}
//    	return false;
//    }
//    
//    /**
//     * 获取系统最多支持的SIM卡数目
//     * @param context  View运行的Context
//     * @return  当前系统支持的SIM卡数目
//     */
//    public static int getSystemSupportSimCount(Context context) {
//    	return Telephony.SIMInfo.getAllSIMCount(context);
//    }
//    
//    /**
//     * 获取当前插入的SIM卡数目
//     * @param context  View运行的Context
//     * @return  当前插入的SIM卡数目
//     */
//    public static int getInsertedSimCount(Context context) {
//    	return Telephony.SIMInfo.getInsertedSIMCount(context);
//    }
//    
//    /**
//     * 获取当前插入的SIM卡详细列表信息
//     * @param context  View运行的Context
//     * @return  当前插入的SIM卡详细列表信息，如果当前没有插入任何SIM卡，则null
//     */
//    public static List<Telephony.SIMInfo> getInsertedSimList(Context context) {
//    	List<Telephony.SIMInfo> simList = Telephony.SIMInfo.getInsertedSIMList(context);
//    	if (simList == null || simList.size() == 0) {
//    		return null;
//    	}
//    	return simList;
//    }
//    
//    /**
//     * 获取当前移动数据处于打开状态的SIM卡ID
//     * @param context  View运行的Context
//     * @return  当前移动数据处于打开状态的SIM卡ID。如果当前没有任何SIM卡打开了移动数据，则返回0
//     */
//    public static int getDataConnectedSimId(Context context) {
//    	return (int) Settings.System.getLong(context.getContentResolver(), GPRS_CONNECTION_SETTING, 0);
//    }
//    
//    /**
//     * 获取当前移动数据处于打开状态的SIM卡槽号(从0开始计数)
//     * @param context  View运行的Context
//     * @return  当前移动数据处于打开状态的SIM卡槽号。
//     *           如果当前没有任何SIM卡打开了移动数据，则返回-1(从0开始计数)
//     */
//    public static int getDataConnectedSimSlot(Context context) {
//    	int simId = getDataConnectedSimId(context);
//    	List<Telephony.SIMInfo> simList = getInsertedSimList(context);
//    	for (Telephony.SIMInfo sim : simList) {
//    		if (sim.mSimId == simId) {
//    			return sim.mSlot;
//    		}
//    	}
//    	return -1;
//    }
//    
//    /**
//     * 从SIM卡槽号获取SIM卡ID号
//     * @param context  View运行的Context
//     * @param slot     SIM卡槽号(从0开始计数)
//     * @return  SIM卡ID号
//     */
//    public static int getSimIdFromSimSlot(Context context, int slot) {
//    	List<Telephony.SIMInfo> simList = getInsertedSimList(context);
//    	for (Telephony.SIMInfo sim : simList) {
//	    	if (sim.mSlot == slot) {
//	    		return (int) sim.mSimId;
//	    	}
//    	}
//    	return 0;
//    }
//    
//    /**
//     * 从SIM卡ID号获取SIM卡槽号
//     * @param context  View运行的Context
//     * @param simId    SIM卡ID号
//     * @return   SIM卡槽号(从0开始计数)
//     */
//    public static int getSimSlotFromSimId(Context context, int simId) {
//    	List<Telephony.SIMInfo> simList = getInsertedSimList(context);
//    	for (Telephony.SIMInfo sim : simList) {
//    		if (sim.mSimId == simId) {
//    			return sim.mSlot;
//    		}
//    	}
//    	return -1;
//    }
//    
//    /**
//     * 获取最大SIM卡槽号(从0开始计数)
//     * @param context  View运行的Context
//     * @return  最大SIM卡槽号
//     */
//    public static int getMaxSimSlotIndex(Context context) {
//    	int index = -1;
//    	List<Telephony.SIMInfo> simList = getInsertedSimList(context);
//    	for (Telephony.SIMInfo sim : simList) {
//    		if (index < sim.mSlot) {
//    			index = sim.mSlot;
//    		}
//    	}
//    	return index;
//    }
//    
//    /**
//     * 获得指定SIM卡信息
//     * @param context  View运行的Context
//     * @param simId  指定SIM卡ID
//     * @return  指定SIM卡信息，如果获取失败，则返回null
//     */
//    public static Telephony.SIMInfo getSimInfoById(Context context, int simId) {
//    	return Telephony.SIMInfo.getSIMInfoById(context, (long) simId);
//    }
//    
//    /**
//     * 打开指定SIM卡移动网络数据(异步打开)。
//     * 打开一张SIM卡移动数据，则默认会关闭所有其他SIM卡移动数据。
//     * 如果同时打开多张SIM卡移动网络数据，结果相当于关闭移动网络数据。
//     * @param context  View运行的Context
//     * @param simId  指定SIM卡ID
//     * @return  打开是否成功
//     */
//    public static boolean openMobileDataConnection(Context context, int simId) {
//    	List<Telephony.SIMInfo> simList = Telephony.SIMInfo.getInsertedSIMList(context);
//    	if (simList == null || simList.size() == 0) {
//    		return false;
//    	}
//    	
//    	boolean isExisted = false;
//    	for (Telephony.SIMInfo sim : simList) {
//    		if (sim.mSimId == simId) {
//    			isExisted = true;
//    			break;
//    		}
//    	}
//    	if (!isExisted) {
//    		return false;
//    	}
//    	
//    	Intent intent = new Intent(GPRS_CHANGE_ACTION);
//    	intent.putExtra(GPRS_SIMID_KEY, (long) simId);
//    	context.sendBroadcast(intent);
//    	return true;
//    }
//    
//    /**
//     * 关闭所有SIM卡移动数据(异步关闭)
//     * @param context  View运行的Context
//     * @return  关闭所有SIM卡移动数据
//     */
//    public static boolean closeMobileDataConnection(Context context) {
//    	Intent intent = new Intent(GPRS_CHANGE_ACTION);
//		intent.putExtra(GPRS_SIMID_KEY, 0L);
//		context.sendBroadcast(intent);
//		return true;
//    }
}
