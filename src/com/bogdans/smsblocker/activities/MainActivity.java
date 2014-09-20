package com.bogdans.smsblocker.activities;

import com.bogdans.smsblocker.R;
import com.bogdans.smsblocker.R.id;
import com.bogdans.smsblocker.R.layout;
import com.bogdans.smsblocker.R.menu;
import com.bogdans.smsblocker.R.string;
import com.bogdans.smsblocker.constants.SharedPreferencesConstants;
import com.bogdans.smsblocker.service.PhoneService;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	
	private boolean isActive = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		isActive = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadState();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		persistState();
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	public void changeBlockerState(View view) {
		if (!isActive) {
			isActive = true;
			showActiveOnUI();
			startBrackgroundService();
			Toast.makeText(getApplicationContext(), "SmsBlocker has started",
					Toast.LENGTH_SHORT).show();
		} else {
			isActive = false;
			showInactiveOnUI();
			stopBackgroundService();
			Toast.makeText(getApplicationContext(), "SmsBlocker has stopped",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	public void launchSmsEditActivity(View view) {
		Intent startSmsActivityIntent = new Intent(MainActivity.this, SmsEditorActivity.class);
		MainActivity.this.startActivity(startSmsActivityIntent);
	}
	
	private void showActiveOnUI() {
		Button startButton = getStartButton();
		startButton.setText(R.string.stop_sms_blocker);
		TextView stateText = getStateTextView();
		stateText.setText(R.string.sms_blocker_state_started);
	}
	
	private void showInactiveOnUI() {
		Button startButton = getStartButton();
		startButton.setText(R.string.start_sms_blocker);
		TextView stateText = getStateTextView();
		stateText.setText(R.string.sms_blocker_state_stopped);
	}
	
	private void startBrackgroundService() {
		Context context = getApplicationContext();
		Intent startServiceIntent = new Intent(context, PhoneService.class);
		context.startService(startServiceIntent);
	}
	
	private void stopBackgroundService() {
		Context context = getApplicationContext();
		Intent stopServiceIntent = new Intent(context, PhoneService.class);
		context.stopService(stopServiceIntent);
	}
	
	private void loadState() {
		loadAndSetIsActive();
		adjustStateToIsActive();
		loadSendSms();
	}
	
	private void loadAndSetIsActive() {
		SharedPreferences settings = getSharedPreferences(SharedPreferencesConstants.PREF_FILE_NAME, 0);
		isActive = settings.getBoolean(SharedPreferencesConstants.ACTIVE, false);
	}
	
	private void adjustStateToIsActive() {
		if (isActive) {
			showActiveOnUI();
		} else {
			showInactiveOnUI();
		}
	}
	
	private void loadSendSms() {
		SharedPreferences settings = getSharedPreferences(SharedPreferencesConstants.PREF_FILE_NAME, 0);
		boolean sendSms = settings.getBoolean(SharedPreferencesConstants.SEND_SMS_MESSAGE, false);
		CheckBox sendSmsCheckBox = getSendSmsCheckBox();
		sendSmsCheckBox.setChecked(sendSms);
	}
	
	
	private void persistState() {
		persistActive();
		persistSendSms();
	}
	
	private void persistActive() {
		SharedPreferences settings = getSharedPreferences(SharedPreferencesConstants.PREF_FILE_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(SharedPreferencesConstants.ACTIVE, isActive);
		editor.commit();
	}
	
	private void persistSendSms() {
		SharedPreferences settings = getSharedPreferences(SharedPreferencesConstants.PREF_FILE_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		CheckBox sendSmsCheckBox = getSendSmsCheckBox();
		boolean sendSms = sendSmsCheckBox.isChecked();
		editor.putBoolean(SharedPreferencesConstants.SEND_SMS_MESSAGE, sendSms);
		editor.commit();
	}
	
	private Button getStartButton() {
		Button startButton = (Button) findViewById(R.id.button1);
		return startButton;
	}
	
	private TextView getStateTextView() {
		TextView textView = (TextView) findViewById(R.id.textView1);
		return textView;
	}
	
	private CheckBox getSendSmsCheckBox() {
		CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
		return checkBox;
	}
}
