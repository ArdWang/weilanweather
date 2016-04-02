package com.wang.weilanweather.app.dp;

import java.util.ArrayList;
import java.util.List;

import com.wang.weilanweather.app.model.City;
import com.wang.weilanweather.app.model.County;
import com.wang.weilanweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/***
 * 创建实体类
 * @author JingWang
 * @date 2016/4/2
 */
public class WeilanWeatherDB {
	/***
	 * 数据库名
	 */
	public static final String DB_NAME="weilan_weather";
	/***
	 * 数据库版本
	 */
	public static final int VERSION=1;
	private static WeilanWeatherDB weilanWeatherDB;
	private SQLiteDatabase db;
	/***
	 * 数据库构造方法私有法
	 */
	private WeilanWeatherDB(Context context){
		WeilanWheatherOpenHelper dbHelper=new WeilanWheatherOpenHelper(context, DB_NAME, null, VERSION);
		db=dbHelper.getWritableDatabase();
	}
	/***
	 * 获取WeilanWheatherDB的实列
	 */
	public synchronized static WeilanWeatherDB getInstance(Context context){
		if(weilanWeatherDB==null){
			weilanWeatherDB=new WeilanWeatherDB(context);
		}
		return weilanWeatherDB;
	}
	/**
	 * 将Province实列存储到数据库
	 */
	public void saveProvince(Province province){
		if(province!=null){
			ContentValues values=new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	/**
	 * 从数据库读取全国所有的省份信息
	 */
	public List<Province> loadProvinces(){
		List<Province> list=new ArrayList<Province>();
		Cursor cursor=db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province=new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	/**
	 * 将City实列存入数据库
	 */
	public void saveCity(City city){
		if(city!=null){
			ContentValues values=new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	/**
	 * 从数据库读取省下所有城市的信息
	 */
	public List<City> loadCities(int provinceId){
		List<City> list=new ArrayList<City>();
		Cursor cursor=db.query("City", null, "province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);
		if(cursor.moveToFirst()){
			do{
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	/**
	 * 将County存入到数据库
	 */
	public void saveCounty(County county){
		try{
			if(county!=null){
				ContentValues values=new ContentValues();
				values.put("county_name", county.getCountyName());
				values.put("county_code", county.getCountyCode());
				values.put("city_id", county.getCityId());
				db.insert("County", null, values);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			Log.e("TAG","error");
		}
	}
	/**
	 * 从数据库读取城市下所有的县的信息
	 */
	public List<County> loadCounties(int cityId){
		//try{
			List<County> list=new ArrayList<County>();
			Cursor cursor=db.query("County", null, "city_id=?", new String[]{String.valueOf(cityId)}, 
					null, null, null);
			if(cursor.moveToFirst()){
				do{
					County county=new County();
					county.setId(cursor.getInt(cursor.getColumnIndex("id")));
					county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
					county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
					county.setCityId(cityId);
					list.add(county);
				}while(cursor.moveToNext());
			}
			if(cursor!=null){
				cursor.close();
			}
			
		//}catch(SQLException ex){
			//ex.printStackTrace();
			//Log.e("TAG","error");
		//}
		return list;
	}
}
