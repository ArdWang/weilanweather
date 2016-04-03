/***
 * ¹ã²¥½ÓÊÜÆ÷
 * @author JingWang
 * @date 2016/4/4
 */
package com.wang.weilanweather.app.receiver;

import com.wang.weilanweather.app.service.AutoUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoUpdateReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent i=new Intent(context,AutoUpdateService.class);
		context.startService(i);
	}
}
