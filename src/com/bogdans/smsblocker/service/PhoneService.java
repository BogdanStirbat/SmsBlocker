package com.bogdans.smsblocker.service;

import java.lang.reflect.Method;

import com.bogdans.smsblocker.constants.SharedPreferencesConstants;
import com.bogdans.smsblocker.phonelistener.PhoneStateListenerReceiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class PhoneService extends Service{
	
	private boolean blockCalls = false;
	
	private PhoneStateListenerReceiver phoneReceiver = null;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (phoneReceiver == null) {
			phoneReceiver = new PhoneStateListenerReceiver();
		}
		IntentFilter startReceiver = createIntentFilterForPhoneBroadcastReceiver();
		registerReceiver(phoneReceiver, startReceiver);
		Log.d("SERVICE", "START");
		return Service.START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy() {
		if (phoneReceiver != null) {
			unregisterReceiver(phoneReceiver);
			phoneReceiver = null;
		}
		Log.d("SERVICE", "STOP");
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void checkSettings(Context context) {
		SharedPreferences preferences = context
				.getSharedPreferences(
						SharedPreferencesConstants.PREF_FILE_NAME,
						Context.MODE_PRIVATE);
		blockCalls = preferences.getBoolean(SharedPreferencesConstants.ACTIVE, false);
	}
	
	public boolean killCall(Context context) {
		try {
			TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			Class classTelephony = Class.forName(telephonyManager.getClass().getName());
			Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");
			
			methodGetITelephony.setAccessible(true);
			Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);
			Class telephonyInterfaceClass =  
                    Class.forName(telephonyInterface.getClass().getName());
			Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
			
			methodEndCall.invoke(telephonyInterface);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private IntentFilter createIntentFilterForPhoneBroadcastReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PHONE_STATE");
		return filter;
	}
}
