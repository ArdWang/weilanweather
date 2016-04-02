package com.wang.weilanweather.app.dp;
/***
 * 创建表语句
 * @author JingWang
 * @date 2016/4/2
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeilanWheatherOpenHelper extends SQLiteOpenHelper{
	/***
	 * Province表建表语句
	 */
	
	public static final String CREATE_PROVINCE="create table Province ("+"id integer primary key autoincrement,"
	 +"province_name text,"+"province_code text)";
	
	/***
	 * City表建表语句
	 */
	public static final String CREATE_CITY="create table City ("+"id integer primary key autoincrement,"+
	 "city_name text,"+"city_code text,"+"province_id integer)";
	/***
	 * County表建表语句
	 */
	public static final String CREATE_COUNTY="create table County ("+"id integer primary key autoincrement,"+
	 "county_name text,"+"county_code text,"+"city_id integer)";
	public WeilanWheatherOpenHelper(Context context,String name,CursorFactory factory,int version){
		super(context,name,factory,version);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE); //创建省表
		db.execSQL(CREATE_CITY);     //创建市表
		db.execSQL(CREATE_COUNTY);   //创建县表
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}
