package com.jiubang.goscreenlock.theme.cjpcardcool.weather.http;

/**
 * 保存刷新的各种状态码
 * @author lishen
 *
 */
public class HttpRequestStatus
{

	// 本地单个城市刷新状态码
	// =================== 这部分必须与服务器返回码一致 ========================
	/** 服务器错误 */
	public static final int	REQUEST_SERVER_ERROR			= -1;
	/** 服务器找不到该城市 */
	public static final int	REQUEST_NO_DATA					= 0;
	/** 刷新成功（统计项） */
	public static final int	REQUEST_SUCCESS					= 1;
	/** 没有更新数据 */
	public static final int	REQUEST_DATA_LATEST				= 2;
	// =====================================================================
	/** 无网络 */
	public static final int	REQUEST_NETWORK_UNAVAILABLE		= 3;
	/** 请求URL不合法 */
	public static final int	REQUEST_INVALID_URL				= 4;
	/** Http协议不合法 */
	public static final int	REQUEST_PROTOCOL_EXCEPTION		= 5;
	/** 连接超时（统计项） */
	public static final int	REQUEST_TIMEOUT					= 6;
	/** 获取数据错误（统计项） */
	public static final int	REQUEST_IO_EXCEPTION			= 7;
	/** 执行网络请求失败（统计项）（http返回码不是200的情况） */
	public static final int	REQUEST_HTTP_FAILED				= 8;
	/** 数据解压出错 */
	public static final int	REQUEST_ZIP_ERROR				= 9;
	/** 下发数据不符合json格式（统计项） */
	public static final int	REQUEST_INVALID_JSON_FORMAT		= 10;
	/** 其他出错
	 * <li> 获取到的城市未来几天的天气预报天数少于1天，视为失败
	 * <li> 暂不区分具体问题的时候，
	 */
	public static final int	REQUEST_FAILED					= 11;

	// 本地一次刷新任务状态码
	/** 正在刷新中 */
	public static final int	ALL_UPDATE_ING					= 0;
	/** 刷新成功 */
	public static final int	ALL_UPDATE_SUCCESS				= 1;
	/** 全部城市都没有新数据 */
	public static final int	ALL_UPDATE_NO_NEWDATA			= 2;
	/** 无网络 */
	public static final int	ALL_UPDATE_NETWORK_UNAVAILABLE	= 3;
	/** 刷新失败 */
	public static final int	ALL_UPDATE_FAILED				= 4;
}
