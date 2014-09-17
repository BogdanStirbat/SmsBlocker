package com.bogdans.smsblocker;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	
	private boolean isStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		isStarted = false;
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
		if (!isStarted) {
			startBlockerState(view);
		} else {
			stopBlockerState(view);
		}
	}
	
	private void startBlockerState(View view) {
		isStarted = true;
		Toast.makeText(getApplicationContext(), "SmsBlocker has started",
				Toast.LENGTH_LONG).show();
		Button startButton = getStartButton();
		startButton.setText(R.string.stop_sms_blocker);
	}
	
	private void stopBlockerState(View view) {
		isStarted = false;
		Toast.makeText(getApplicationContext(), "SmsBlocker has stopped",
				Toast.LENGTH_LONG).show();
		Button startButton = getStartButton();
		startButton.setText(R.string.start_sms_blocker);
	}
	
	private Button getStartButton() {
		Button startButton = (Button) findViewById(R.id.button1);
		return startButton;
	}

}
