/***
 * 创建选择Activity
 * @author JingWang
 * @date 2016/4/3
 */
package com.wang.weilanweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.wang.weilanweather.app.dp.WeilanWeatherDB;
import com.wang.weilanweather.app.model.City;
import com.wang.weilanweather.app.model.County;
import com.wang.weilanweather.app.model.Province;
import com.wang.weilanweather.app.util.HttpCallbackListener;
import com.wang.weilanweather.app.util.HttpUtil;
import com.wang.weilanweather.app.util.Utility;
import com.weilanweather.app.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity{
	private static final int LEVEL_PROVINCE=0;
	public static final int LEVEL_CITY=1;
	public static final int LEVEL_COUNTY=2;
	private ProgressDialog progressDialog;
	private ListView listView;
	private TextView titleText;
	private ArrayAdapter<String> adapter;  //适配器
	private WeilanWeatherDB weilanWeatherDB;
	private List<String> dataList=new ArrayList<String>();
	/**
	 * 省列表
	 */
	private List<Province> provinceList;
	/**
	 * 市列表
	 */
	private List<City> cityList;
	/**
	 * 县列表
	 */
	private List<County> countyList;
	/**
	 * 选中省份
	 */
	private Province selectedProvince;
	/**
	 * 选中城市
	 */
	private City selectedCity;
	/**
	 * 当前选中的级别
	 */
	private int currentLevel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView=(ListView)findViewById(R.id.list_view);
		titleText=(TextView)findViewById(R.id.title_text);
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		weilanWeatherDB=WeilanWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> v, View view, int index,
					long longset) {
				// TODO Auto-generated method stub
				if(currentLevel==LEVEL_PROVINCE){
					selectedProvince=provinceList.get(index);
					//需要写方法
					queryCities();
				}else if(currentLevel==LEVEL_CITY){
					selectedCity=cityList.get(index);
					//写入province
					queryCounties();
				}
			}
		});
		//加入省级数据
		queryProvinces();
	}
	/**
	 * 查询全国所有的省，优先数据库查询,如果没有查询到再去服务器上查询
	 */
	private void queryProvinces(){
		provinceList=weilanWeatherDB.loadProvinces();
		if(provinceList.size()>0){
			dataList.clear();
			for(Province province:provinceList){
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel=LEVEL_PROVINCE;
		}else{
			//写入传入服务器
			queryFromServer(null, "province");
		}
	}
	/**
	 * 查询选中省内所有的市,优先从数据库查询,如果没有查询到再去服务器上查询
	 */
	private void queryCities(){
		cityList=weilanWeatherDB.loadCities(selectedProvince.getId());
		if(cityList.size()>0){
			dataList.clear();
			for(City city:cityList){
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel=LEVEL_CITY;
		}else{
			//从服务器里面读取城市
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}
	/**
	 * 查询选中市所有的县,优先从数据库查询,如果没有查询到再去服务器上查询
	 */
	private void queryCounties(){
		countyList=weilanWeatherDB.loadCounties(selectedCity.getId());
		if(countyList.size()>0){
			dataList.clear();
			for(County county:countyList){
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel=LEVEL_COUNTY;
		}else{
			//选这县城
			queryFromServer(selectedCity.getCityCode(), "county");
		}
	}
	/**
	 * 根据传入的代号和类型从服务器上查询省市县数据
	 */
	private void queryFromServer(final String code,final String type){
		String address;
		if(!TextUtils.isEmpty(code)){
			address="http://www.weather.com.cn/data/list3/city" + code +
					".xml";
		}else{
			address="http://www.weather.com.cn/data/list3/city.xml";
		}
		//显示进度条
		
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result=false;
				if("province".equals(type)){
					result=Utility.handleProvinceResponse(weilanWeatherDB, response);
				}else if("city".equals(type)){
					result=Utility.handleCitiesResponse(weilanWeatherDB, response, selectedProvince.getId());
				}else if("county".equals(type)){
					result=Utility.handleCountiesResponse(weilanWeatherDB, response, selectedCity.getId());
				}
				if(result){
					//通过runOnUiThread()方法回到主线程处理逻辑
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}else if("city".equals(type)){
								queryCities();
							}else if("county".equals(type)){
								queryCounties();
							}
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				//通过runOnUiThread()方法回到主线程处理逻辑
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败！", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}
	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog(){
		if(progressDialog==null){
			progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	/**
	 * 关闭对话框
	 */
	private void closeProgressDialog(){
		if(progressDialog!=null){
			progressDialog.dismiss();
		}
	}
	/**
	 * 捕获Back按键,根据当前的级别来判断,此时应该返回市列表,省列表还是退出
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(currentLevel==LEVEL_COUNTY){
			queryCities();
		}else if(currentLevel==LEVEL_CITY){
			queryProvinces();
		}else{
			finish();
		}
	}
}
