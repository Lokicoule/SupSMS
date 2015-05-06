/*
 * Author : Lokicoule
 */
package com.supsms.app.activity;

import com.supsms.app.R;
import com.supsms.app.activity.enumeration.OPERATION;
import com.supsms.app.activity.enumeration.RESPONSE;
import com.supsms.app.activity.utils.ScreenSize;
import com.supsms.app.session.UserSession;
import com.supsms.app.webservice.WSFactory;
import com.supsms.app.webservice.WSGlobal;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class HomeActivity extends ActionBarActivity {
	private ToggleButton valToggle;
	private Button contactBtn, smsBtn, smsInbox, smsSent, saveContact, restaureContact, restaureSMS, saveSMS;
	TextView status;
	ProgressBar state;
	private boolean isAllRight = true;
	private boolean isSuccess = true;
	private int height;
	private static OPERATION operationToDo;
	private String msg;
	UserSession session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		session = new UserSession(getApplicationContext());
		initActivity();
	}
	
	/*
	 * isAllRight boolean is true if webservice not fail
	 * is false if error
	 */
	public void setAllRight(boolean isAllRight) {
		this.isAllRight = isAllRight;
	}
	
	/*
	 * get mobile screen height
	 * init composents values
	 */
	private void initActivity()
	{
		ScreenSize c_size = new ScreenSize(this);
		height = c_size.getScreenHeight();
		status = (TextView) findViewById(R.id.resultHomeTV);
		state = (ProgressBar) findViewById(R.id.progressBarHome);
		initToggle();
		initBtn();
	}
	
	/*
	 * define new valToggle height in %
	 * valToggle is true (supinfo api) when activity start
	 */
	private void initToggle()
	{
		valToggle = (ToggleButton) findViewById(R.id.OnOffToggle);
		valToggle.setChecked(true);
		valToggle.setHeight(height * 10 / 100);
		toggleLogout();
	}
	
	/*
	 * resize height buttons in %
	 * call method which initialize buttons listener
	 */
	private void initBtn()
	{
		contactBtn = (Button) findViewById(R.id.contactMenu);
		smsBtn = (Button) findViewById(R.id.smsMenu);
		smsInbox = (Button) findViewById(R.id.smsInbox);
		smsSent = (Button) findViewById(R.id.smsSent);
		restaureContact = (Button) findViewById(R.id.restaureContact);
		saveContact = (Button) findViewById(R.id.saveContact);
		saveSMS = (Button) findViewById(R.id.smsSave);
		restaureSMS = (Button) findViewById(R.id.smsRestaure);
		contactBtn.setHeight(height * 10 / 100);
		smsBtn.setHeight(height * 10 / 100);
		smsInbox.setHeight(height * 10 / 100);
		smsSent.setHeight(height * 10 / 100);
		saveContact.setHeight(height * 10 / 100);
		restaureContact.setHeight(height * 10 / 100);
		setOnClickButtonsListener();
	}
	
	/*
	 * Initialize listeners
	 */
	private void setOnClickButtonsListener()
	{
		contactBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (saveContact.getVisibility() == View.VISIBLE)
				{
					saveContact.setVisibility(View.GONE);
					restaureContact.setVisibility(View.GONE);
				}
				else
				{
					smsInbox.setVisibility(View.GONE);
					smsSent.setVisibility(View.GONE);
					restaureSMS.setVisibility(View.GONE);
					saveSMS.setVisibility(View.GONE);
					saveContact.setVisibility(View.VISIBLE);
					restaureContact.setVisibility(View.VISIBLE);
				}
			}
		});
		saveContact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				operationToDo = OPERATION.CONTACT_SAVE;
				AsyncCallWS task = new AsyncCallWS();
                task.execute();				
			}
		});
		smsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (smsInbox.getVisibility() == View.VISIBLE)
				{
					smsInbox.setVisibility(View.GONE);
					smsSent.setVisibility(View.GONE);
					restaureSMS.setVisibility(View.GONE);
					saveSMS.setVisibility(View.GONE);
				}
				else
				{
					saveContact.setVisibility(View.GONE);
					restaureContact.setVisibility(View.GONE);
					restaureSMS.setVisibility(View.GONE);
					saveSMS.setVisibility(View.GONE);
					smsInbox.setVisibility(View.VISIBLE);
					smsSent.setVisibility(View.VISIBLE);
				}
			}
		});
		smsInbox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (saveSMS.getVisibility() == View.VISIBLE)
				{
					restaureSMS.setVisibility(View.GONE);
					saveSMS.setVisibility(View.GONE);
					smsSent.setVisibility(View.VISIBLE);
				}
				else
				{
					smsSent.setVisibility(View.GONE);
					restaureSMS.setVisibility(View.VISIBLE);
					saveSMS.setVisibility(View.VISIBLE);
				}
			}
		});
		smsSent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (saveSMS.getVisibility() == View.VISIBLE)
				{
					restaureSMS.setVisibility(View.GONE);
					saveSMS.setVisibility(View.GONE);
					smsInbox.setVisibility(View.VISIBLE);
				}
				else 
				{
					smsInbox.setVisibility(View.GONE);
					restaureSMS.setVisibility(View.VISIBLE);
					saveSMS.setVisibility(View.VISIBLE);
				}
			}
		});
		saveSMS.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				operationToDo = (smsInbox.getVisibility() == View.VISIBLE) ? OPERATION.SMS_INBOX_SAVE : OPERATION.SMS_SENT_SAVE;
				AsyncCallWS task = new AsyncCallWS();
				task.execute();
			}
		});
		restaureSMS.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				operationToDo = (smsInbox.getVisibility() == View.VISIBLE) ? OPERATION.SMS_INBOX_RECOVERY : OPERATION.SMS_SENT_RECOVERY;
				AsyncCallWS task = new AsyncCallWS();
				task.execute();
			}
		});
		restaureContact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				operationToDo = OPERATION.CONTACT_RECOVERY;
				AsyncCallWS task = new AsyncCallWS();
                task.execute();	
			}
		});
	}
	
	/*
	 * Initialize listener
	 * call method in userSession in order to clean content in sharedpreference
	 */
	private void toggleLogout()
	{
		valToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isChecked)
					session.logoutUser();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intObj = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(intObj);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*
	 * AsyncCallWs : While treatment is running in doInBackground
	 * 				ProgressBar state is visible until the end of execution
	 * 				On postExecute is executed when doInBackground is closed.
	 */
	private class AsyncCallWS extends AsyncTask<Object, Object, Object> {
		
		@Override
		protected Object doInBackground(Object... params) 
		{
			/*
			 * check which button was clicked
			 */
			switch (operationToDo) 
			{
				case CONTACT_SAVE :
					isSuccess = WSFactory.getSaveContactWS().Main(HomeActivity.this, session);
					msg=RESPONSE.SAVE_CONTACT.toString();
					break;
				case CONTACT_RECOVERY :
					isSuccess = WSFactory.getRecoveryContactWS().Main(HomeActivity.this, session);
					msg = RESPONSE.RECOVERY_CONTACT.toString();
					break;
				case SMS_INBOX_SAVE :
					isSuccess = WSFactory.getSaveSmsWS().Main(HomeActivity.this, WSGlobal.SMS_INBOX_CONTENT_URI, session);
					msg = RESPONSE.SAVE_SMS_INBOX.toString();
					break;
				case SMS_SENT_SAVE:
					isSuccess = WSFactory.getSaveSmsWS().Main(HomeActivity.this, WSGlobal.SMS_SENT_CONTENT_URI, session);
					msg = RESPONSE.SAVE_SMS_SENT.toString();
					break;
				case SMS_INBOX_RECOVERY :
					isSuccess = WSFactory.getRecoverySmsWS().Main(HomeActivity.this, WSGlobal.SMS_INBOX_CONTENT_URI, session);
					msg = RESPONSE.RECOVERY_INBOX_SMS.toString();
					break;
				case SMS_SENT_RECOVERY:
					isSuccess = WSFactory.getRecoverySmsWS().Main(HomeActivity.this, WSGlobal.SMS_SENT_CONTENT_URI, session);
					msg = RESPONSE.RECOVERY_SENT_SMS.toString();
					break;
				default:
					break;
			}
			return null;
		}
 
        @Override
        protected void onPostExecute(Object result) {
        	/*
        	 * Print result to screen
        	 */
        	status.setVisibility(View.VISIBLE);
        	state.setVisibility(View.INVISIBLE);
        	operationToDo = OPERATION.INIT_OPERATION;
        	if (isAllRight)
        	{
        		if (isSuccess)
        		{
        			status.setText(msg + "\nWell Done");
        			status.setTextColor(Color.GREEN);
        		}
        		else
        		{
        			status.setTextColor(Color.RED);
        			status.setText(msg + "\nFail");
        		}
        	}
        	else
        	{
        		status.setTextColor(Color.RED);
        		status.setText("Webservice process fail");
        	}
        }
 
        @Override
        protected void onPreExecute() {
        	status.setVisibility(View.INVISIBLE);
        	state.setVisibility(View.VISIBLE);
        }
 
        @Override
        protected void onProgressUpdate(Object... values) {
        }
    }
}
