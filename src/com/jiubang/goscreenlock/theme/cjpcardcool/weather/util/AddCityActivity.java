package com.jiubang.goscreenlock.theme.cjpcardcool.weather.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.jiubang.goscreenlock.theme.cjpcardcool.util.Global;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.CityBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.bean.SearchCitiesResultBean;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.LocationConstants;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.common.Util;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.location.ReqLocation;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.location.ReqLocationListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.search.SearchCity;
import com.jiubang.goscreenlock.theme.cjpcardcool.weather.search.SearchCityListener;
import com.jiubang.goscreenlock.theme.cjpcardcool.R;

/**
 * 
 * <br>类描述: 英文下添加城市的界面
 * <br>功能详细描述:
 * 
 * @date  [2012-9-5]
 */
public class AddCityActivity extends Activity
		implements
			OnClickListener,
			OnItemClickListener,
			OnEditorActionListener
{
	View	mV;

	private static final int		REC_SEARCH_ERROR				= 0X01;
	private static final int		REC_DATA_READY					= 0X02;
	private static final int		REC_NO_CITY						= 0X03;
	private static final int		REC_NO_NETWORK					= 0x04;
	/**查询将要添加的城市是否已经存在**/
	private static final int		TOKEN_QUERY_ADD_CITY			= 0x05;

	/** 定位完成后返回的各种状态 */
	public static final int			LOCATION_CHANGED				= 1;								// 获取当前位置的经纬度成功
	public static final int			LOCATION_TIME_OUT				= 2;								// 获取经纬度超时
	public static final int			LOCATION_NULL					= 3;								// 无法获取当前城市
	public static final int			LOCATION_CITY					= 4;								// 获取当前位置所处的城市成功
	public static final int			LOCATION_PROVIDER_UNABLE		= 5;								// 没有开启定位功能
	public static final int			LOCATION_NETWORK_ERROR			= 6;								// 基站定位网络不可用
	public static final int			LOCATION_DUPLICATED				= 7;								// 我的位置重复
	public static final int			LOCATION_AUTO_LOCATION_CLOSED	= 8;								// 定位完成时，自动定位选项已关闭
	public static final int			LOCATION_EMPTY					= 9;								// 获取到城市ID为空的我的位置

	/** 自动定位按钮*/
	private View					mCurCity;
	private ImageView				mAddCurCityTarget;
	private EditText				mSearchInput;
	/**
	 * search_input监听软件件盘的回车点击时
	 * 启动搜索，由于部分输入法会出现两次
	 * 点击状况，用这个计数防止二次响应
	 */
	private int						mCountImeActionUnspecified		= 0;
	/** 搜索城市按钮*/
	private ImageView				mSearchBtn;
	private ListView				mListView;
	private ListAdapter				mListAdapter;
	/** 搜索结果*/
	private SearchCitiesResultBean	mSearchResult;
	/** ListView显示的标签*/
	private ArrayList<String>		mListLable;
	/** 当前是否在显示搜索结果*/
	private boolean					mIsSearchResultShowing;
	private LayoutInflater			mInflater;
	private ProgressDialog			mLoadingDialog;

	private TextView				mNocityLabelV;
	private ImageView				mDevideV;
	private TextView				mTipLabelV, mTip1V, mTip2V, mTip3V;
	private TextView				mTips;
	//    private String mCurSearchText;
	private PopupWindow				mLoadingLayer;
	private ProgressBar				mProgress;
	//	private LocationHandler mLocationHandler;
	private ReqLocation				mReqLocation;
	private boolean					mLocating;
	private LocationResultHandler	mLococationHandler;
	private LocationQueryHandler	mLocationQueryHandler;
	private BroadcastReceiver		mReceiver;
	/** 添加城市界面是否已销毁 */
	private boolean					mIsDestroy						= false;

	/** 搜索城市的异步Task*/
	private SearchCity				mSearchCity						= null;
	/** 是否正在执行数据库的查询和添加城市操作。true，是；false，否 */
	private boolean					mDBOperating					= false;

	/*******************************************************搜索城市**************************************************************/
	/** 搜索城市业务类 */
	private SearchCityListener		mSearchCityListener				= new SearchCityListener()
																	{

																		@Override
																		public void onSearchComplete(
																				SearchCitiesResultBean searchResultBean,
																				int searchType)
																		{

																			// 搜索成功，有结果
																			if (!searchResultBean
																					.getCities()
																					.isEmpty())
																			{
																				Message msg = new Message();
																				msg.what = REC_DATA_READY;
																				msg.obj = searchResultBean;
																				mHandler.sendMessage(msg);
																			}
																			// 搜索成功，无结果(理论上不会出现，但为防止服务器出错，加入这个判断)
																			else
																			{
																				mHandler.sendEmptyMessage(REC_NO_CITY);
																			}

																		}

																		@Override
																		public void onSearchNoNetWorkConnection()
																		{
																			mHandler.sendEmptyMessage(REC_NO_NETWORK);
																		}

																		@Override
																		public void onSearchFailed()
																		{
																			mHandler.sendEmptyMessage(REC_SEARCH_ERROR);
																		}

																		@Override
																		public void onSearchNoResult()
																		{
																			mHandler.sendEmptyMessage(REC_NO_CITY);
																		}
																	};

	SearchResultHandler				mHandler						= new SearchResultHandler(this);

	/***/
	static class SearchResultHandler extends Handler
	{
		WeakReference<AddCityActivity>	mOutClass;

		SearchResultHandler(AddCityActivity outClass)
		{
			mOutClass = new WeakReference<AddCityActivity>(outClass);
		}

		@Override
		public void handleMessage(Message msg)
		{

			AddCityActivity outClass = mOutClass.get();

			if (outClass == null)
			{
				return;
			}

			// 收到结果，不管是什么，都先清空之前的结果
			if (!outClass.mIsDestroy && outClass.mLoadingDialog != null)
			{
				outClass.mLoadingDialog.dismiss();
			}

			switch (msg.what)
			{
			// 搜索成功，没有结果
				case REC_NO_CITY :
					outClass.showHideSearchTip(true);
					break;

				// 无网络
				case REC_NO_NETWORK :
					Toast toast = Toast.makeText(outClass, R.string.addcity_search_no_network,
							Toast.LENGTH_SHORT);
					toast.show();
					outClass.showHideSearchTip(false);
					break;
				// 网络错误    
				case REC_SEARCH_ERROR :

					Toast toast2 = Toast.makeText(outClass, R.string.addcity_search_server_error,
							Toast.LENGTH_SHORT);
					toast2.show();
					outClass.showHideSearchTip(false);
					break;

				// 搜索成功，有结果
				case REC_DATA_READY :
					outClass.mIsSearchResultShowing = true;
					outClass.mSearchResult = null;
					outClass.mListLable.clear();
					outClass.mSearchResult = (SearchCitiesResultBean) msg.obj;
					// 取出数据
					for (CityBean city : outClass.mSearchResult.getCities())
					{
						outClass.mListLable.add(city.getLabel());
					}
					outClass.showHideSearchTip(false);
					outClass.mListAdapter.notifyDataSetChanged();
					if (!outClass.mSearchResult.isPrePageExisted())
					{
						outClass.mListView.setSelection(0);
					}
					break;
				default :
					break;
			}

			super.handleMessage(msg);
		}
	};

	private void firstNetworkSearch(String keyword)
	{
		if (mSearchCity != null)
		{
			mSearchCity.cancel();
		}
		mSearchCity = new SearchCity(AddCityActivity.this, SearchCity.SEARCH_LS,
				mSearchCityListener, keyword, Util.getCurLanguage(this));
		mSearchCity.execute();
	}

	private void moreNetworkSearch(String more_url)
	{
		if (mSearchCity != null)
		{
			mSearchCity.cancel();
		}
		mSearchCity = new SearchCity(AddCityActivity.this, SearchCity.SEARCH_MORE,
				mSearchCityListener, mSearchResult);
		mSearchCity.execute();
	}

	/*******************************************************定位城市**************************************************************/
	static class LocationResultHandler extends Handler
	{
		WeakReference<AddCityActivity>	mOutClass;

		public LocationResultHandler(AddCityActivity outClass)
		{
			mOutClass = new WeakReference<AddCityActivity>(outClass);
		}

		public void handleMessage(Message msg)
		{
			final AddCityActivity outClass = mOutClass.get();
			if (outClass != null)
			{
				outClass.locationEnd();
				switch (msg.what)
				{
					case LOCATION_TIME_OUT :
						Toast toast = Toast.makeText(outClass, R.string.locate_timeout,
								Toast.LENGTH_SHORT);
						toast.show();
						break;
					case LOCATION_PROVIDER_UNABLE :
						outClass.mLocating = false;
						outClass.showAlertDialog(null,
								outClass.getString(R.string.addcity_gps_result_no));
						break;
					case LOCATION_NULL :
						outClass.showAlertDialog(null,
								outClass.getString(R.string.addcity_gps_server_no));
						break;
					case LOCATION_CITY :
						final CityBean curCity = (CityBean) msg.obj;
						if (!outClass.mIsDestroy)
						{
							StringBuilder label = new StringBuilder();
							if (!TextUtils.isEmpty(curCity.getCityName()))
							{
								label.append(curCity.getCityName());
							}
							if (!TextUtils.isEmpty(curCity.getStateName()))
							{
								label.append(",").append(curCity.getStateName());
							}
							if (!TextUtils.isEmpty(curCity.getCountryName()))
							{
								label.append("(").append(curCity.getCountryName()).append(")");
							}
							new GoWeatherEXDialog.Builder(outClass)
									.setTitle(R.string.addcity_title_curcity_label)
									.setTips(
											String.format(outClass
													.getString(R.string.addcity_gps_result_ok),
													label.toString()))
									.setPositiveButton(R.string.addcity_gps_add_button,
											new DialogInterface.OnClickListener()
											{
												@Override
												public void onClick(DialogInterface dialog,
														int which)
												{
													if (curCity != null)
													{
														outClass.selectCity(curCity);
													}
												}
											})
									.setNegativeButton(android.R.string.cancel,
											new DialogInterface.OnClickListener()
											{
												@Override
												public void onClick(DialogInterface dialog,
														int which)
												{
												}
											}).show();
						}
						break;
				}
				return;
			}
		}
	};

	public void startLocation()
	{
		this.mLocating = true;
		doStartLocation(0, LocationConstants.WAY_CELL_LOCATION, 15);
	}

	private void doStartLocation(final int prevLocationWay, final int curLocationWay, int countTime)
	{
		mReqLocation.startLocation(prevLocationWay, curLocationWay, countTime,
				new ReqLocationListener()
				{

					@Override
					public void onLocationTimeout(int locationWay)
					{
						if (locationWay == LocationConstants.WAY_GPS_LOCATION)
						{
							// 按照定位顺序：基站定位->网络定位->GPS定位，最后一种定位方式为GPS
							mLococationHandler.sendEmptyMessage(LOCATION_TIME_OUT);
						}
						else
						{
							startNextLocationWay(prevLocationWay, curLocationWay, LOCATION_TIME_OUT);
						}
					}

					@Override
					public void onLocationNull()
					{
						// sendBroadcast(Constants.LOCATION_NULL);
						mLococationHandler.sendEmptyMessage(LOCATION_NULL);
					}

					@Override
					public void onLocationWayChanged(int switchType)
					{
						// sendSwitchLocationWayBroadcast(switchType);
					}

					@Override
					public void onLocationSuccess(CityBean curCity, Location curLocation)
					{
						Message message = Message.obtain();
						message.obj = curCity;
						message.what = LOCATION_CITY;
						mLococationHandler.sendMessage(message);
					}

					@Override
					public void onLocationFailed(int status)
					{
						startNextLocationWay(prevLocationWay, curLocationWay, status);
					}

					@Override
					public void onLocationLatLngFectched(Location curLocation)
					{
						mReqLocation.fetchAddressInfo(curLocation);
					}

				});
	}

	private void startNextLocationWay(int prevLocationWay, int curLocationWay, int status)
	{
		switch (curLocationWay)
		{
			case LocationConstants.WAY_CELL_LOCATION :
				if (status == LocationConstants.LOCATION_SIMCARD_NOT_READY
						|| status == LocationConstants.LOCATION_NETWORK_ERROR)
				{
					// 基站定位不可用
					doStartLocation(prevLocationWay, LocationConstants.WAY_NETWORK_LOCATION, 15);
				}
				else
				{
					// 基站定位失败
					doStartLocation(curLocationWay, LocationConstants.WAY_NETWORK_LOCATION, 15);
				}
				break;
			case LocationConstants.WAY_NETWORK_LOCATION :
				if (status == LocationConstants.LOCATION_NETWORK_UNABLED
						|| status == LocationConstants.LOCATION_UNSUPPORTED_BY_SYSYTEM)
				{
					// 网络定位不可用
					doStartLocation(prevLocationWay, LocationConstants.WAY_GPS_LOCATION, 50);
				}
				else
				{
					// 网络定位失败
					doStartLocation(curLocationWay, LocationConstants.WAY_GPS_LOCATION, 50);
				}
				break;
			case LocationConstants.WAY_GPS_LOCATION :
				// 按照定位顺序：基站定位->网络定位->GPS定位，最后一种定位方式为GPS
				// sendBroadcast(Constants.LOCATION_PROVIDER_UNABLE);
				mLococationHandler.sendEmptyMessage(LOCATION_PROVIDER_UNABLE);
				break;
			default :
				;
		}
	}

	private void locationEnd()
	{
		mProgress.setVisibility(View.GONE);
		mAddCurCityTarget.setVisibility(View.VISIBLE);
		mCurCity.setClickable(true);
	}

	/*******************************************************Activity**************************************************************/
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// 设置View
		mInflater = getLayoutInflater();
		setContentView(R.layout.weather_act_add_city_layout);
		mCurCity = findViewById(R.id.add_city_title_curcity);
		mCurCity.setOnClickListener(this);
		mAddCurCityTarget = (ImageView) mCurCity.findViewById(R.id.add_city_target);
		mProgress = (ProgressBar) mCurCity.findViewById(R.id.add_city_curcity_progress);
		mSearchInput = (EditText) findViewById(R.id.add_city_search_input);
		mSearchInput.setOnEditorActionListener(this); // 软盘键入监听
		mSearchBtn = (ImageView) findViewById(R.id.add_city_search_submit);
		mSearchBtn.setOnClickListener(this);

		mNocityLabelV = (TextView) findViewById(R.id.add_city_empty_label);
		mDevideV = (ImageView) findViewById(R.id.add_city_line_devide);
		mTipLabelV = (TextView) findViewById(R.id.add_city_search_tip_label);
		mTip1V = (TextView) findViewById(R.id.add_city_search_tip_1);
		mTip2V = (TextView) findViewById(R.id.add_city_search_tip_2);
		mTip3V = (TextView) findViewById(R.id.add_city_search_tip_3);

		mTips = (TextView) findViewById(R.id.add_city_search_tips);
		mTips.setOnClickListener(this);

		// 初始化列表内容
		mSearchResult = null;
		mListLable = new ArrayList<String>();
		String[] lable = new String[] { getString(R.string.pop_location),
				getString(R.string.browse_by_loction) };
		for (int i = 0; i < lable.length; i++)
		{
			mListLable.add(lable[i]);
		}
		mListView = (ListView) findViewById(R.id.add_city_list);
		mListAdapter = new ListAdapter();
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(this);
		mIsSearchResultShowing = false;

		// 准备工具
		mReqLocation = new ReqLocation(this, Util.getCurLanguage(this));
		mLocating = false;
		mLococationHandler = new LocationResultHandler(this);
		mLocationQueryHandler = new LocationQueryHandler(getContentResolver());
		
		mV = findViewById(R.id.weather_guanggao);
		mV.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				boolean result = Global.gotoMarket(AddCityActivity.this, Global.SURISTRING);
				if (!result)
				{
					Global.gotoBrowser(AddCityActivity.this, Global.SPATHSTRING);
				}
			}
		});
	}

	/**
	 * 
	 * 类描述:ListAdapter
	 * 功能详细描述:
	 * 
	 * @author  chenyuning
	 * @date  [2012-11-2]
	 */
	private class ListAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			int count = mListLable.size();

			// 3.1版在点击的时候就清空列表，但不清空结果（防止空指针），因此要判断列表为零
			if (mSearchResult != null && mListLable.size() != 0)
			{
				if (mSearchResult.isMutliPage())
				{
					count++;
				}
				if (mSearchResult.getPrePage() != null)
				{
					count++;
				}
			}
			return count;
		}

		@Override
		public Object getItem(int position)
		{
			if (mListLable != null)
			{
				return mListLable.get(position);
			}
			else
			{
				return null;
			}
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View item;
			ViewHolder holder;
			if (convertView == null)
			{
				item = mInflater.inflate(R.layout.weather_addcity_list_item_layout, parent, false);
				holder = new ViewHolder();
				holder.mTextView = (TextView) item.findViewById(R.id.addcity_list_item_title);
				holder.mImageView = (ImageView) item.findViewById(R.id.addcity_list_item_more);
				item.setTag(holder);
			}
			else
			{
				item = convertView;
				holder = (ViewHolder) item.getTag();
			}

			holder.mImageView.setVisibility(View.GONE);

			// 先判断是否需要显示上一页
			// 若需要显示，则将第一项设为上一页，其他项的position-1（可以不需要改动原有代码）
			if (mSearchResult != null && mSearchResult.getPrePage() != null)
			{
				if (position == 0)
				{
					holder.mTextView.setText(R.string.previous_page);
					holder.mTextView.setGravity(Gravity.CENTER);
					return item;
				}
				else
				{
					position--;
				}
			}

			// 不管是不是有下一页，前面的Label都可以直接取
			if (position >= 0 && position < mListLable.size())
			{
				holder.mTextView.setText(mListLable.get(position));
				holder.mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			}

			// 如果有下一页，就显示下一页（只要position比标签数大，就有下一页）
			else if (position == mListLable.size() && mListLable.size() != 0)
			{
				holder.mTextView.setText(R.string.next_page);
				holder.mTextView.setGravity(Gravity.CENTER);
			}
			return item;
		}
	}

	/**
	 * 
	 * 类描述:
	 * 功能详细描述:
	 * 
	 * @author  chenyuning
	 * @date  [2012-11-5]
	 */
	class ViewHolder
	{
		TextView	mTextView;
		ImageView	mImageView;
	}

	/**
	 * 选择一个城市
	 * @param city
	 */
	private void selectCity(CityBean city)
	{
		WeatherSettingUtil.setCity(this, city);
		finish();
		//		if (!mDBOperating) {
		//			mDBOperating = true;
		//			// TODO：执行插入操作之前先检查要插入的城市在数据库中是否存在
		//			/*
		//			mLocationQueryHandler.startQuery(TOKEN_QUERY_ADD_CITY, city,
		//					WeatherContentProvider.TABLE_CITYNOW_URI,
		//					new String[] { CityNowTable.CITY_ID }, CityNowTable.CITY_ID + " in " + "('"
		//							+ city.getCityId() + "') and " + CityNowTable.MY_LOCATION + " = '"
		//							+ Constants.FLAG_CITY_ADDED + "'", null, null);
		//			*/
		//		}
	}

	@Override
	public void onClick(View v)
	{
		if (v.equals(mCurCity))
		{
			if (mAddCurCityTarget.getVisibility() == View.VISIBLE)
			{
				mCurCity.setClickable(false);
				mAddCurCityTarget.setVisibility(View.GONE);
				mProgress.setVisibility(View.VISIBLE);
				startLocation();
			}
		}
		else if (v.equals(mSearchBtn))
		{
			final String inputContent = mSearchInput.getText().toString();
			if (inputContent == null || inputContent.length() == 0)
			{
				Toast toast = Toast.makeText(AddCityActivity.this,
						R.string.addcity_search_input_default, Toast.LENGTH_SHORT);
				toast.show();
				return;
			}
			firstNetworkSearch(inputContent);
			deactivateInputBox();
			mLoadingDialog = createLoadingDialog(this,
					this.getResources().getString(R.string.addcity_search_dialog_title), this
							.getResources().getString(R.string.addcity_serach_dialog_content), null);

			// 这个操作要在ondismiss里处理，因为对话框会拦截Acticity的KeyDown事件。
			mLoadingDialog.setOnDismissListener(new OnDismissListener()
			{

				@Override
				public void onDismiss(DialogInterface dialog)
				{
					if (mSearchCity != null)
					{
						mSearchCity.cancel();
						mSearchCity = null;
					}
				}
			});
			if (!mIsDestroy)
			{
				mLoadingDialog.show();
			}
			if (mIsSearchResultShowing)
			{
				mListLable.clear();
				mListAdapter.notifyDataSetChanged();
			}
			showHideSearchTip(false);
			//            mCurSearchText = inputContent;
		}
		else if (v.equals(mTips))
		{

			View view = mInflater.inflate(R.layout.weather_add_city_tip_layout, null);
			new AlertDialog.Builder(AddCityActivity.this).setIcon(R.drawable.icon)
					.setTitle(R.string.search_tip_title).setView(view)
					.setPositiveButton(R.string.button_ok, null).create().show();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> l, View v, int position, long id)
	{
		// 当前显示的不是搜索结果，直接判断转跳
		if (!mIsSearchResultShowing)
		{
			if (position == 0)
			{
				gotoPopularScreen();
			}
			else if (position == 1)
			{
				gotoBrowseScreen();
			}
		}
		// 当前显示的是搜索结果，判断是添加城市还是下一页
		else if (mSearchResult != null)
		{
			// 先判断是否需要显示上一页，处理类似getiew
			if (mSearchResult != null && mSearchResult.getPrePage() != null)
			{
				// 第0项是"联网搜索"，所以这里上一页是第一项
				if (position == 0)
				{
					Message msg = new Message();
					msg.what = REC_DATA_READY;
					msg.obj = mSearchResult.getPrePage();
					mHandler.sendMessage(msg);
					return;
				}
				else
				{
					position--;
				}
			}

			// position在mListLable范围内，则是添加城市，否则是点了下一页
			if (position == mListLable.size())
			{
				//                    Log.d("CYN", mSearchResult.getMoreUrl());
				moreNetworkSearch(mSearchResult.getMoreUrl());
				if (mLoadingDialog != null)
				{
					mLoadingDialog.setTitle(this.getResources().getString(
							R.string.addcity_search_dialog_title));
					mLoadingDialog.setMessage(this.getResources().getString(
							R.string.addcity_serach_dialog_content));
					mLoadingDialog.show();
				}
			}
			else
			{
				selectCity(mSearchResult.getCities().get(position));
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		boolean bHandled = false;
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{

			showHideSearchTip(false);

			// 如果是显示搜索结果，就返回
			if (mIsSearchResultShowing)
			{
				// 恢复列表为初始内容
				mSearchResult = null;
				mListLable = new ArrayList<String>();
				String[] lable = new String[] { getString(R.string.pop_location),
						getString(R.string.browse_by_loction) };
				for (int i = 0; i < lable.length; i++)
				{
					mListLable.add(lable[i]);
				}
				mIsSearchResultShowing = false;
				mListAdapter.notifyDataSetChanged();
				bHandled = true;
			}
		}
		// 界面返回
		if (!bHandled)
		{
			return super.onKeyDown(keyCode, event);
		}
		else
		{
			return true;
		}
	}

	private void deactivateInputBox()
	{
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mSearchInput.getWindowToken(), 0);
	}

	private void showHideSearchTip(boolean bShow)
	{
		if (bShow)
		{
			mNocityLabelV.setVisibility(View.VISIBLE);
			mDevideV.setVisibility(View.VISIBLE);
			mTipLabelV.setVisibility(View.VISIBLE);
			mTip1V.setVisibility(View.VISIBLE);
			mTip2V.setVisibility(View.VISIBLE);
			mTip3V.setVisibility(View.VISIBLE);
		}
		else
		{
			mNocityLabelV.setVisibility(View.INVISIBLE);
			mDevideV.setVisibility(View.INVISIBLE);
			mTipLabelV.setVisibility(View.INVISIBLE);
			mTip1V.setVisibility(View.INVISIBLE);
			mTip2V.setVisibility(View.INVISIBLE);
			mTip3V.setVisibility(View.INVISIBLE);
		}
	}

	protected void onDestroy()
	{
		mIsDestroy = true;
		if (mLoadingLayer != null)
		{
			mLoadingLayer.dismiss();
			mLoadingLayer = null;
		}
		if (mReqLocation != null)
		{
			mReqLocation.cancel();
		}
		if (mSearchCity != null)
		{
			mSearchCity.cancel();
		}
		if (mReceiver != null)
		{
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
		super.onDestroy();
	}

	public ProgressDialog createLoadingDialog(Context context, String title, String msg,
			OnDismissListener listener)
	{
		ProgressDialog loadingDialog = new ProgressDialog(context);
		loadingDialog.setTitle(title);
		loadingDialog.setMessage(msg);
		loadingDialog.setOnDismissListener(listener);
		return loadingDialog;
	}

	private void gotoPopularScreen()
	{
		Intent intent = new Intent();
		intent.setClass(this, PopularcityActivity.class);
		startActivityForResult(intent, 0);
	}

	private void gotoBrowseScreen()
	{
		Intent intent = new Intent();
		intent.setClass(this, BrowseCityActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null)
		{
			//            Log.d("CYN", "onActivityResult");
			finish();
		}
	}
	/**
	 * 
	 * <br>类描述:
	 * <br>功能详细描述:
	 * 
	 * @date  [2012-9-5]
	 */
	class LocationQueryHandler extends AsyncQueryHandler
	{

		public LocationQueryHandler(ContentResolver cr)
		{
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor)
		{
			super.onQueryComplete(token, cookie, cursor);
			switch (token)
			{
				case TOKEN_QUERY_ADD_CITY :
					CityBean city = (CityBean) cookie;
					// 添加的城市是否已经存在
					boolean isExist = false;
					if (cursor != null)
					{
						try
						{
							if (cursor.getCount() > 0)
							{
								Toast toast = Toast.makeText(AddCityActivity.this,
										R.string.cityexists, Toast.LENGTH_SHORT);
								toast.show();
								mDBOperating = false;
								isExist = true;
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
							// 数据库操作异常，终止本次添加城市，更新数据库操作
							mDBOperating = false;
						}
						finally
						{
							cursor.close();
						}
						if (mDBOperating && !isExist)
						{
							// 数据库操作正常，并且添加的城市不存在数据库中
							// TODO：这里将所得到的城市插入到数据库中，插入完成后会调用onInsertComplete方法
							/*
							ContentValues values = new ContentValues();
							values.put(CityNowTable.CITY_ID, city.getCityId());
							values.put(CityNowTable.OLD_CITY_ID, city.getCityId());
							values.put(CityNowTable.CITY_NAME, city.getCityName());
							values.put(CityNowTable.STATE, city.getStateName());
							values.put(CityNowTable.COUNTRY, city.getCountryName());
							values.put(CityNowTable.TZ_OFFSET, city.getTimeZone());
							values.put(CityNowTable.MY_LOCATION, Constants.FLAG_CITY_ADDED);
							values.put(CityNowTable.WIND_DIRECTION, Constants.UNKNOWN_VALUE_STRING);
							values.put(CityNowTable.WIND_STRENGTH, Constants.UNKNOWN_VALUE_STRING);
							values.put(CityNowTable.WIND_TYPE, Constants.UNKNOWN_WIND_TYPE);
							values.put(CityNowTable.SUNRISE, Constants.UNKNOWN_VALUE_STRING);
							values.put(CityNowTable.SUNSET, Constants.UNKNOWN_VALUE_STRING);
							values.put(CityNowTable.TYPE, Constants.UNKNOWN_WEATHER_TYPE); // 未知天气类型
							values.put(CityNowTable.NOW_DESP, Constants.UNKNOWN_VALUE_STRING);
							values.put(CityNowTable.SEQUENCE, mLargestSeqNum);
							values.put(CityNowTable.POP, Constants.WEATHER_MIN_VALUE);
							values.put(CityNowTable.CITY_TYPE, PlaceInfo.WORLDWIDE);
							values.put(CityNowTable.FEELSLIKE_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.BAROMETER_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.DEWPOINT_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.HIGH_TEMP_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.LOW_TEMP_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.WIND_STRENGTH_VALUE,
									Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.NOW_TEMP_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.TIMESTAMP, Constants.UNKNOWN_VALUE_INT);
							values.put(CityNowTable.UVINDEX_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.HUMIDITY_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.VISIBILITY_VALUE, Constants.UNKNOWN_VALUE_FLOAT);
							values.put(CityNowTable.UPDATE_TIME, Constants.UNKNOWN_VALUE_INT);
							mLocationQueryHandler.startInsert(0, city,
									WeatherContentProvider.TABLE_CITYNOW_URI, values);
							*/
						}
					}
					else
					{
						mDBOperating = false;
					}
					break;

				default :
					break;
			}

		}

		@Override
		protected void onInsertComplete(int token, Object cookie, Uri uri)
		{
			super.onInsertComplete(token, cookie, uri);
			mDBOperating = false;
			CityBean city = (CityBean) cookie;
			Toast.makeText(
					AddCityActivity.this,
					AddCityActivity.this.getString(R.string.add_city_successfully,
							city.getCityName()), Toast.LENGTH_SHORT).show();
			// TODO：到这里插入城市操作完成，可以处理一些插入完成后的操作，例如请求天气
			AddCityActivity.this.finish();
		}

	}

	private void showAlertDialog(final String title, final String message)
	{
		if (!mIsDestroy)
		{
			new GoWeatherEXDialog.Builder(this).setTitle(title).setTips(message)
					.setPositiveButton(R.string.button_ok, null).show();
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
	{
		// 按下确定软键(回车键的另一种显示方式，见EditText的android:imeOptions="actionDone"属性)，开始搜索
		if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
		{
			if (v.equals(mSearchInput))
			{
				if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
				{
					mCountImeActionUnspecified++;
					if (mCountImeActionUnspecified == Integer.MAX_VALUE)
					{
						mCountImeActionUnspecified = 0;
					}
				}
				// 防止IME_ACTION_UNSPECIFIED的二次响应
				if (mCountImeActionUnspecified % 2 == 0)
				{
					return true;
				}
				final String inputContent = mSearchInput.getText().toString();
				if (inputContent == null || inputContent.length() == 0)
				{
					Toast toast = Toast.makeText(AddCityActivity.this,
							R.string.addcity_search_input_default, Toast.LENGTH_SHORT);
					toast.show();
					return true;
				}
				mLoadingDialog = createLoadingDialog(this,
						this.getResources().getString(R.string.addcity_search_dialog_title), this
								.getResources().getString(R.string.addcity_serach_dialog_content),
						null);
				if (!mIsDestroy)
				{
					mLoadingDialog.show();
				}
				showHideSearchTip(false);
				deactivateInputBox();
				//                mCurSearchText = inputContent;
				firstNetworkSearch(inputContent);
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (mReceiver != null)
		{
			unregisterReceiver(mReceiver);
		}
		if (mV != null && Global.isAppExist(this, Global.SWEATHERPACK))
		{
			mV.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		IntentFilter counterActionFilter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
		mReceiver = new LacleChangedBroadcastReceiver();
		registerReceiver(mReceiver, counterActionFilter);
	}

	/**
	 * 
	 * <br>类描述:
	 * <br>功能详细描述:
	 * 
	 * @date  [2012-9-5]
	 */
	class LacleChangedBroadcastReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			finish();
		}

	}

}
