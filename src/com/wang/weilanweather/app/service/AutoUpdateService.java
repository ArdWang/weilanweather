/***
 * 创建服务
 * @author JingWang
 * @date 2016/4/4
 */
package com.wang.weilanweather.app.service;

import com.wang.weilanweather.app.util.HttpCallbackListener;
import com.wang.weilanweather.app.util.HttpUtil;
import com.wang.weilanweather.app.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

public class AutoUpdateService extends Service{
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateWeather();
			}
		}).start();
		AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
		int anHour=8*60*60*1000; //这里8小时的毫秒数
		long triggerAttime=SystemClock.elapsedRealtime()+anHour;
		Intent i=new Intent(this,AutoUpdateService.class);
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAttime, pi);
		return super.onStartCommand(intent, flags, startId);
	}
	/**
	 * 更新天气信息
	 */
	private void updateWeather(){
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode=prefs.getString("weather_code", "");
		String address="http://www.weather.com.cn/data/cityinfo/" +
				weatherCode + ".html";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				Utility.handleWeatherResponse(AutoUpdateService.this, response);
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		});
		
	}
}
