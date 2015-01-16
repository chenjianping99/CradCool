package com.jiubang.goscreenlock.theme.cjpcardcool.calendar;

/**
 * 
 * <br>类描述:查询数据监听
 * 
 * @author  guoyiqing
 * @date  [2013-1-5]
 */
public interface OnQueryDataListener
{

	public void onQueryFinish(boolean isAsync, Object newestEvent, Object events);

}
