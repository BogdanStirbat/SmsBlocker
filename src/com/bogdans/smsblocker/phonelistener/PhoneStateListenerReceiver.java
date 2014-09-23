package com.bogdans.smsblocker.phonelistener;

import com.bogdans.smsblocker.constants.IntentContstants;
import com.bogdans.smsblocker.service.PhoneService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

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
				Log.d("RECEIVER", "Phone arrived: " + incomingNumber);
				Intent startServiceIntent = new Intent(context, PhoneService.class);
				startServiceIntent.putExtra(IntentContstants.PHONE_NUMBER, incomingNumber);
				context.startService(startServiceIntent);
			}
		}
	}

}
