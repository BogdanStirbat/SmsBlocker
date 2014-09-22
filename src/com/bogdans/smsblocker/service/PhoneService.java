package com.bogdans.smsblocker.service;

import java.lang.reflect.Method;

import com.bogdans.smsblocker.constants.IntentContstants;
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
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Context context = getApplicationContext();
		checkSettings(context);
		String phoneNumber = intent.getExtras().getString(IntentContstants.PHONE_NUMBER);
		Log.d("SERVICE", "Phone arrived: " + phoneNumber);
		if (phoneNumber != null && blockCalls) {
			Log.d("SERVICE", "Start KILL call");
			killCall(context);
		}
		return Service.START_NOT_STICKY;
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
		Log.d("SERVICE", "[checkSettings] blockCalls:" + blockCalls);
	}
	
	public boolean killCall(Context context) {
		try {
			Log.d("SERVICE", "[killCall] 1");
			TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			Log.d("SERVICE", "[killCall] 2");
			Class classTelephony = Class.forName(telephonyManager.getClass().getName());
			Log.d("SERVICE", "[killCall] 3");
			Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");
			Log.d("SERVICE", "[killCall] 4");
			
			methodGetITelephony.setAccessible(true);
			Log.d("SERVICE", "[killCall] 5");
			Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);
			Log.d("SERVICE", "[killCall] 6");
			Class telephonyInterfaceClass =  
                    Class.forName(telephonyInterface.getClass().getName());
			Log.d("SERVICE", "[killCall] 7");
			Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
			Log.d("SERVICE", "[killCall] 8");
			
			Log.d("SERVICE", "[killCall] 9");
			methodEndCall.invoke(telephonyInterface);
			Log.d("SERVICE", "[killCall] 10");
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
