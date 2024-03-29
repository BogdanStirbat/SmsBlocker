package com.bogdans.smsblocker.service;

import java.lang.reflect.Method;

import com.bogdans.smsblocker.constants.IntentContstants;
import com.bogdans.smsblocker.constants.SharedPreferencesConstants;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneService extends Service{
	
	private boolean blockCalls = false;
	private boolean sendSms = false;
	private String smsMessage = "";
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Context context = getApplicationContext();
		checkSettings(context);
		String phoneNumber = intent.getExtras().getString(IntentContstants.PHONE_NUMBER);
		Log.d("SERVICE", "Phone arrived: " + phoneNumber);
		if (phoneNumber != null && blockCalls) {
			boolean blocked = killCall(context);
			if (blocked && sendSms) {
				sendSmsMessage(phoneNumber);
			}
		}
		stopSelf();
		return Service.START_NOT_STICKY;
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private void checkSettings(Context context) {
		SharedPreferences preferences = context
				.getSharedPreferences(
						SharedPreferencesConstants.PREF_FILE_NAME,
						Context.MODE_PRIVATE);
		blockCalls = preferences.getBoolean(SharedPreferencesConstants.ACTIVE, false);
		Log.d("SERVICE", "[checkSettings] blockCalls:" + blockCalls);
		sendSms = preferences.getBoolean(SharedPreferencesConstants.SEND_SMS_MESSAGE, false);
		smsMessage = preferences.getString(SharedPreferencesConstants.SMS_MESSAGE, "");
	}
	
	/**
	 * See http://stackoverflow.com/questions/23097944/can-i-hang-up-a-call-programmatically-in-android
	 * @param context the Application Context
	 * @return if successfully killed the incoming call
	 */
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
			Log.d("SERVICE", "[killCall] SUCCESSS");
		} catch (Exception e) {
			Log.d("SERVICE", "[killCall] FALSE");
			return false;
		}
		return true;
	}
	
	public boolean sendSmsMessage(String phoneNumber) {
		if (!phoneNumber.startsWith("07")) {
			return false;
		}
		if (smsMessage == null || smsMessage.trim().length() == 0) {
			return false;
		}
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
		Log.d("SERVICE", "[sendSmsMessage] message:" + smsMessage + " to " + phoneNumber);
		return true;
	}
}
