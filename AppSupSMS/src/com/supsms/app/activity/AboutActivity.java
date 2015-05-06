/*
 * Author : Lokicoule
 */
package com.supsms.app.activity;

import com.supsms.app.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends ActionBarActivity {
	private Button api, save, recovery, notYet, goBack;
	private TextView api1TV, api2TV, saveTV, recoveryTV, notYetTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initActivity();
	}
	
	/*
	 * Call init methods
	 */
	private void initActivity()
	{
		initButton();
		initTextView();
	}
	
	/*
	 * Buttons get values
	 * Call method which give listener to buttons
	 */
	private void initButton()
	{
		api = (Button) findViewById(R.id.btnApiAbout);
		save = (Button) findViewById(R.id.btnSaveAbout);
		recovery = (Button) findViewById(R.id.btnRecoveryAbout);
		notYet = (Button) findViewById(R.id.btnNotYetAbout);
		goBack = (Button) findViewById(R.id.btnGoBack);
		setBtnOnClickListner();
	}
	
	/*
	 * Textviews get value
	 */
	private void initTextView()
	{
		api1TV = (TextView) findViewById(R.id.textViewApi1);
		api2TV = (TextView) findViewById(R.id.textViewApi2);
		saveTV = (TextView) findViewById(R.id.textViewSaveDescription);
		recoveryTV = (TextView) findViewById(R.id.textViewRecoveryDescription);
		notYetTv = (TextView) findViewById(R.id.textViewNotYetDescription);
	}
	
	/*
	 * Listeners are define
	 */
	private void setBtnOnClickListner()
	{
		api.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveTV.setVisibility(View.GONE);
				recoveryTV.setVisibility(View.GONE);
				notYetTv.setVisibility(View.GONE);
				if (api1TV.getVisibility() == View.VISIBLE)
				{
					api1TV.setVisibility(View.GONE);
					api2TV.setVisibility(View.GONE);
				}
				else
				{
					api1TV.setVisibility(View.VISIBLE);
					api2TV.setVisibility(View.VISIBLE);
				}
			}
		});
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				recoveryTV.setVisibility(View.GONE);
				notYetTv.setVisibility(View.GONE);
				api1TV.setVisibility(View.GONE);
				api2TV.setVisibility(View.GONE);
				if (saveTV.getVisibility() == View.VISIBLE)
					saveTV.setVisibility(View.GONE);
				else
					saveTV.setVisibility(View.VISIBLE);
			}
		});
		recovery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveTV.setVisibility(View.GONE);
				notYetTv.setVisibility(View.GONE);
				api1TV.setVisibility(View.GONE);
				api2TV.setVisibility(View.GONE);
				if (recoveryTV.getVisibility() == View.VISIBLE)
					recoveryTV.setVisibility(View.GONE);
				else
					recoveryTV.setVisibility(View.VISIBLE);
			}
		});
		notYet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				recoveryTV.setVisibility(View.GONE);
				saveTV.setVisibility(View.GONE);
				api1TV.setVisibility(View.GONE);
				api2TV.setVisibility(View.GONE);
				if (notYetTv.getVisibility() == View.VISIBLE)
					notYetTv.setVisibility(View.GONE);
				else
					notYetTv.setVisibility(View.VISIBLE);
			}
		});
		goBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AboutActivity.this, LoginActivity.class));
			}
		});
	}
}
