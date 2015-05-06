/*
 * Author : Lokicoule
 */
package com.supsms.app.session;

import java.util.HashMap;

import com.supsms.app.activity.LoginActivity;
import com.supsms.app.model.User;
import com.supsms.app.session.enumeration.SESSION;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("CommitPrefEdits")
public class UserSession {
	SharedPreferences pref;
	Editor editor;
	Context context;
	int PRIVATE_MODE = 0;

	public UserSession(Context context) 
	{
		this.context = context;
		pref = context.getSharedPreferences(SESSION.PREFERENCE_NAME.toString(), PRIVATE_MODE);
		editor = pref.edit();
	}

	/*
	 * Create user session
	 * API SUPINFO is true, API SUPSMS is false
	 */
	public void createUserSession(User user) 
	{
		editor.putString(SESSION.KEY_NAME.toString(), user.getUsername());
		editor.putString(SESSION.KEY_PASSWORD.toString(), user.getPassword());
		editor.putBoolean(SESSION.KEY_API.toString(), user.getApi());
		editor.commit();
	}

	/*
	 * getUserDetails return hashmap with data saved when session was created
	 */
	public HashMap<String, String> getUserDetails() 
	{
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(SESSION.KEY_NAME.toString(), pref.getString(SESSION.KEY_NAME.toString(), null));
		user.put(SESSION.KEY_PASSWORD.toString(), pref.getString(SESSION.KEY_PASSWORD.toString(), null));
		return user;
	}

	/*
	 * Clear data save in sharedpreference and start login activity
	 * Need to relogin next time
	 */
	public void logoutUser() 
	{
		editor.clear();
		editor.commit();
		Intent i = new Intent(context, LoginActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
	
	public boolean getApi() 
	{
		return pref.getBoolean(SESSION.KEY_API.toString(), false);
	}
}