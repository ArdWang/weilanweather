package com.wang.weilanweather.app.util;
/***
 * ½Ó¿ÚÀà
 * @author JingWang
 * @date 2016/4/2
 */
public interface HttpCallbackListener {
	void onFinish(String response);
	void onError(Exception e);
}
