package com.jiubang.goscreenlock.theme.cjpcardcool.weather.tianqi;

import java.util.List;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.City;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.Result;

/**
 * 
 * 类描述: 网络连接回调接口
 * 
 * @author  liuwenqin
 * @date  [2012-9-4]
 */
public interface IHttpConnListener
{
	public void onNetworkUnavailable(List<Result> results);

	public void onErrorGeneral(List<Result> results);

	/**
	 * <br>功能简述: 刷新成功的回调
	 * @param city_refresh 刷洗天气的城市
	 * @param results 刷新天气过程返回的结果列表
	 */
	public void onSuccess(City city_refresh, List<Result> results);

	/**
	 * <br>功能简述：时间戳一致，服务器没有新数据下发的回调
	 * @param results
	 */
	public void onNoNewData(List<Result> results);
}