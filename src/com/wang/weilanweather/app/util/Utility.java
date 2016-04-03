/***
 * Util格式处理服务器返回回来的数据
 * @author JingWang
 * @date 2016/4/2
 */
package com.wang.weilanweather.app.util;

import com.wang.weilanweather.app.dp.WeilanWeatherDB;
import com.wang.weilanweather.app.model.City;
import com.wang.weilanweather.app.model.County;
import com.wang.weilanweather.app.model.Province;

import android.R.bool;
import android.text.TextUtils;


public class Utility {
	/**
	 * 解析和处理服务器返回的省级数据
	 */
	public synchronized static boolean handleProvinceResponse(WeilanWeatherDB weilanWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces=response.split(",");
			if(allProvinces!=null&&allProvinces.length>0){
				for(String p:allProvinces){
					String[] array=p.split("\\|");
					Province province=new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//将解析出来的数据存储到Province表
					weilanWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * 解析和处理服务器返回的市级数据
	 */
	public  static boolean handleCitiesResponse(WeilanWeatherDB weilanWeatherDB,String response,
			int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities=response.split(",");
			if(allCities!=null&&allCities.length>0){
				for(String c:allCities){
					String[] array=c.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					//将解析出来的数据存储到City表
					weilanWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * 解析和处理服务器返回的县极数据
	 */
	public static boolean handleCountiesResponse(WeilanWeatherDB weilanWeatherDB,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties=response.split(",");
			if(allCounties!=null&&allCounties.length>0){
				for(String c:allCounties){
					String array[]=c.split("\\|");
					County county=new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					//将解析出来的数据存入County表中
					weilanWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}	
	
}
