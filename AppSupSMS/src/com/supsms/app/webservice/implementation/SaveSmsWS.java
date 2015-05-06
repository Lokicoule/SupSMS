/*
 * Author : Lokicoule
 */
package com.supsms.app.webservice.implementation;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.supsms.app.activity.HomeActivity;
import com.supsms.app.session.UserSession;
import com.supsms.app.session.enumeration.SESSION;
import com.supsms.app.webservice.WSGlobal;
import com.supsms.app.webservice.facade.SaveSmsInterfaceWS;
import com.supsms.app.webservice.utils.SmsUtils;

public class SaveSmsWS implements SaveSmsInterfaceWS {
	private static HttpPost post = new HttpPost();
	private JSONObject smsJson = new JSONObject();
	private static boolean success = false;
	private static HttpResponse response;
	private String URL_API;
	private boolean isSent;
	public SaveSmsWS() {
	}
	
	@Override
	public boolean Main(HomeActivity activity, Uri box, UserSession session)
	{
		try {
			HttpClient httpClient = new DefaultHttpClient();
			JSONArray sms = SmsUtils.ListSMS(activity, box);
			smsJson.put("SMS", sms);
			URL_API = (session.getApi()) ? WSGlobal.SUPINFO_API_WS : WSGlobal.SUPSMS_API_WS;
			HashMap<String, String> user = session.getUserDetails();
			isSent = (box.equals(WSGlobal.SMS_INBOX_CONTENT_URI)) ? false : true;
			PreparedPostRequest(user.get(SESSION.KEY_NAME.toString()), user.get(SESSION.KEY_PASSWORD.toString()));
			response = httpClient.execute(post);
			parseResponse();
			activity.setAllRight(true);
		} catch (Exception e){
			activity.setAllRight(false);
			e.printStackTrace();
		}
		return success;		
	}	

	@Override
	public void PreparedPostRequest(String username, String password) {
		URI uri = null;
		try {
			uri = new URI(URL_API);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("action","backupsms"));
			params.add(new BasicNameValuePair("login", username));
			params.add(new BasicNameValuePair("password", password));
			if (isSent)
				params.add(new BasicNameValuePair("box", "sent"));
			else
				params.add(new BasicNameValuePair("box", "inbox"));
			params.add(new BasicNameValuePair("sms", smsJson.toString()));
	    	post.setURI(uri);
			post.setEntity(new UrlEncodedFormEntity(params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void parseResponse() {
    	HttpEntity entity = response.getEntity();
		Object content = null;
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		StatusLine statusLine = response.getStatusLine();
	    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
	    {
	    	Gson gson = new Gson();
	    	JsonObject jsonObject = gson.fromJson( content.toString(), JsonObject.class);
	    	JsonElement isSuccess = jsonObject.get("success");
	    	success = isSuccess.getAsBoolean();
	    }		
	}

}
