package com.bogdans.smsblocker;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView.BufferType;

public class SmsEditorActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms_editor);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		setPersistedMessageInEditText();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sms_editor, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_sms_editor,
					container, false);
			return rootView;
		}
	}
	
	
	public void saveTextAndReturnToPreviousActivity(View view) {
		persistSmsMessage(view);
		finish();
	}
	
	
	private void persistSmsMessage(View view) {
		SharedPreferences settings = getSharedPreferences(SharedPreferencesConstants.PREF_FILE_NAME, 0);
		String smsMessage = getSmsMessage();
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(SharedPreferencesConstants.SMS_MESSAGE, smsMessage);
		editor.commit();
	}
	
	private EditText getSmsEditorEditText() {
		EditText editText = (EditText) findViewById(R.id.smsEditText);
		return editText;
	}
	
	private String getSmsMessage() {
		String message = "";
		EditText smsEdit = getSmsEditorEditText();
		message = smsEdit.getText().toString();
		return message;
	}
	
	private String getPersistedSmsMessage() {
		String message = "";
		
		SharedPreferences settings = getSharedPreferences(SharedPreferencesConstants.PREF_FILE_NAME, 0);
		message = settings.getString(SharedPreferencesConstants.SMS_MESSAGE, "");
		
		return message;
	}
	
	private void setPersistedMessageInEditText() {
		String persistedSms = getPersistedSmsMessage();
		EditText smsEdit = getSmsEditorEditText();
		smsEdit.setText(persistedSms, BufferType.NORMAL);
	}
}
