/***
 * Util��ʽ������������ػ���������
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
	 * �����ʹ�����������ص�ʡ������
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
					//���������������ݴ洢��Province��
					weilanWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * �����ʹ�����������ص��м�����
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
					//���������������ݴ洢��City��
					weilanWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * �����ʹ�����������ص��ؼ�����
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
					//���������������ݴ���County����
					weilanWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}	
	
}
