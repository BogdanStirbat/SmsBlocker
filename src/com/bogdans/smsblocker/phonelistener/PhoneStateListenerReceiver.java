package com.bogdans.smsblocker.phonelistener;

import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

/**
 *   BroadcastReceivers that listens to incomming phone calls.
 *   see http://stackoverflow.com/questions/23097944/can-i-hang-up-a-call-programmatically-in-android
 *
 */
public class PhoneStateListenerReceiver extends BroadcastReceiver {
	
	private boolean blockCalls = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
			String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
			if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
				if (blockCalls) {
					killCall(context);
				}
			}
		}
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

}
