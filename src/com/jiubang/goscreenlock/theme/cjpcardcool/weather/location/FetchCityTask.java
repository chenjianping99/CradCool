package com.jiubang.goscreenlock.theme.cjpcardcool.weather.location;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.CityBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.http.Result;

/**
 * 
 * 类描述:将经纬度转为城市
 * 
 * @author  liuwenqin
 * @date  [2012-9-4]
 */
public class FetchCityTask extends AsyncTask<Object, Integer, CityBean>
{
	private Context				mContext;
	private ReqLocationListener	mReqLocationListener;
	private Location			mCurLocation;

	private Result				mResult;

	/**
	 * 程序语言设置
	 */
	private String				mLanguage;

	public FetchCityTask(Context context, ReqLocationListener reqLocationListener,
			Location curLocation, String language)
	{
		mContext = context;
		mReqLocationListener = reqLocationListener;
		mCurLocation = curLocation;
		mResult = new Result();
		mLanguage = language;
	}

	@Override
	protected CityBean doInBackground(Object... params)
	{
		Location curLocation = (Location) params[0];
		// ============== 这部分代码作随机定位测试用 ============== 
		//		Random random = new Random();
		//		curLocation.setLatitude(random.nextDouble() * 40 + 20);
		//		curLocation.setLongitude(random.nextDouble() * 30 + 90);
		// =====================================================
		List<CityBean> cities = WeatherLocation.getMyLocation(mContext, curLocation, mResult,
				mLanguage);

		if (cities != null && cities.size() > 0)
		{
			return cities.get(0);
		}
		return null;
	}

	@Override
	protected void onPostExecute(CityBean locationCity)
	{
		if (locationCity != null)
		{
			mReqLocationListener.onLocationSuccess(locationCity, mCurLocation);
		}
		else
		{
			mReqLocationListener.onLocationNull();
		}
	}

}
