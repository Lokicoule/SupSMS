/*
 * Author : Lokicoule
 */
package com.supsms.app.activity;

import com.supsms.app.R;
import com.supsms.app.activity.utils.ScreenSize;
import com.supsms.app.model.User;
import com.supsms.app.session.UserSession;
import com.supsms.app.session.enumeration.SESSION;
import com.supsms.app.webservice.WSFactory;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LoginActivity extends ActionBarActivity {
	Button loginBtn;
	ToggleButton apiChoice;
	EditText usernameInput, passwordInput;
	String username, password;
	TextView status;
	ProgressBar state;
	private int height;
	boolean isValidUser, apiSelector;
	private static boolean isAllRight = true;
	UserSession session;
	SharedPreferences sharedpreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		session = new UserSession(getApplicationContext());
		initActivity();
	}
	
	@Override
	protected void onResume() {
		/*
		 * check if sharedpreferences already exist
		 * if yes start home activity
		 */
		sharedpreferences = getSharedPreferences(SESSION.PREFERENCE_NAME.toString(), Context.MODE_PRIVATE);
		if (sharedpreferences.contains(SESSION.KEY_NAME.toString()))
		{
			if(sharedpreferences.contains(SESSION.KEY_PASSWORD.toString()))
				startActivity(new Intent(this, HomeActivity.class));
		}
		super.onResume();
	}
	
	private void initActivity()
	{
		ScreenSize c_size = new ScreenSize(this);
		height = c_size.getScreenHeight();
		status = (TextView) findViewById(R.id.resultLoginTV);
		state = (ProgressBar) findViewById(R.id.progressBarLogin);
		initToggle();
		initBtn();
		initEditText();
	}
	
	private void initEditText()
	{
		usernameInput = (EditText) findViewById(R.id.usernameET);
		usernameInput.setHeight(height * 10 / 100);
		passwordInput = (EditText) findViewById(R.id.passwordET);
		passwordInput.setHeight(height * 10 / 100);
	}
	
	private void initBtn()
	{
		loginBtn = (Button) findViewById(R.id.loginBtn);
		loginBtn.setHeight(height * 7 / 100);
		loginBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (usernameInput.getText().length() != 0 && usernameInput.getText().toString() != "") {
                    if(passwordInput.getText().length() != 0 && passwordInput.getText().toString() != ""){
                        username = usernameInput.getText().toString();
                        password = passwordInput.getText().toString();
                        status.setText("");
                        AsyncCallWS task = new AsyncCallWS();
                        task.execute();
                    }
                    else{
                        status.setText("Please enter Password");
                    }
                } else {
                    status.setText("Please enter Username");
                }
            }
        });
	}
	
	private void initToggle()
	{
		apiChoice = (ToggleButton) findViewById(R.id.apiChoice);
		apiChoice.setChecked(true);
		apiChoice.setHeight(height * 8 / 100);
		apiChoice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				apiSelector = apiChoice.isChecked();				
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
			Intent intObj = new Intent(LoginActivity.this, AboutActivity.class);
            startActivity(intObj);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void setAllRight(boolean isAllRight) {
		LoginActivity.isAllRight = isAllRight;
	}

	private class AsyncCallWS extends AsyncTask<Object, Object, Object> {
		User user;
		@Override
		protected Object doInBackground(Object... params) {
			user = WSFactory.getLoginWS().Main(username, password, LoginActivity.this);
			if (user != null)
				isValidUser = (user.isValidUser()) ? true : false;
			return null;
		}
 
        @Override
        protected void onPostExecute(Object result) {
        	state.setVisibility(View.INVISIBLE);
            if(isAllRight)
            {
                if(isValidUser)
                {
                	user.setApi(apiChoice.isChecked());
                	session.createUserSession(user);
                	startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
                else
                    status.setText("Login Failed, try again");
            }
            else
            	status.setText("Error occured in invoking webservice");
            setAllRight(true);
        }
 
        @Override
        protected void onPreExecute() {
        	state.setVisibility(View.VISIBLE);
        }
 
        @Override
        protected void onProgressUpdate(Object... values) {
        }
    }

	public boolean getApi()
    {
    	return apiSelector;
    }
}
