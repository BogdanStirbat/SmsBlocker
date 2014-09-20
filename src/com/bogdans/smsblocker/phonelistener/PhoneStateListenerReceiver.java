package com.bogdans.smsblocker.phonelistener;

import java.lang.reflect.Method;

import com.bogdans.smsblocker.constants.SharedPreferencesConstants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

/**
 *   BroadcastReceivers that listens to incomming phone calls.
 *   see http://stackoverflow.com/questions/23097944/can-i-hang-up-a-call-programmatically-in-android
 *
 */
public class PhoneStateListenerReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
			String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
			if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			}
		}
	}
	

}
