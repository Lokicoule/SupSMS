/*
 * Author : Lokicoule
 */
package com.supsms.app.webservice.implementation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.supsms.app.activity.HomeActivity;
import com.supsms.app.session.UserSession;
import com.supsms.app.session.enumeration.SESSION;
import com.supsms.app.webservice.WSGlobal;
import com.supsms.app.webservice.facade.RecoverySmsInterfaceWS;
import com.supsms.app.webservice.utils.SmsUtils;

public class RecoverySmsWS implements RecoverySmsInterfaceWS{
	private boolean success = false;
	private static HttpPost post = new HttpPost();
	private static HttpResponse response;
	private String URL_API, box;
	private JSONObject smsJson = new JSONObject();
	private ActionBarActivity activity;
	private Uri uriBox;
	public RecoverySmsWS() {
	}
	
	@Override
	public boolean Main(HomeActivity activity, Uri uriBox, UserSession session)
	{
		try {
			this.activity = activity;
			this.uriBox = uriBox;
			box = (uriBox.toString().contains("inbox")) ? "inbox" : "sent";
				
			HttpClient httpClient = new DefaultHttpClient();
			JSONArray sms = SmsUtils.ListSMS(activity, uriBox);
			smsJson.put("SMS", sms);
			URL_API = (session.getApi()) ? WSGlobal.SUPINFO_API_WS : WSGlobal.SUPSMS_API_WS;
			HashMap<String, String> user = session.getUserDetails();
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
	    	params.add(new BasicNameValuePair("action","dobackupsms"));
			params.add(new BasicNameValuePair("login", username));
			params.add(new BasicNameValuePair("password", password));
			params.add(new BasicNameValuePair("box", box));
			params.add(new BasicNameValuePair("sms", smsJson.toString()));
	    	post.setURI(uri);
			post.setEntity(new UrlEncodedFormEntity(params));
		} catch (Exception e) {
			System.out.println("preparedFail");
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
	    	recreateSms(jsonObject.getAsJsonArray("sms"));
	    	success = isSuccess.getAsBoolean();
	    }		
	}
	
	@Override
	public void recreateSms(JsonArray smsArray)
	{
		String body = null;
		if (smsArray == null)
			return;
		List<String> threadsId = SmsUtils.getThreadsId(activity, uriBox);
		if (threadsId == null)
			return;
		for (int i = 0; i < smsArray.size(); i++)
		{
			boolean isExist = false;
			ContentValues values = new ContentValues();
			JsonObject sms = smsArray.get(i).getAsJsonObject();
			String threadId = sms.get("thread_id").getAsString();
			for (int j = 0; j < threadsId.size(); j++)
				if (threadsId.get(j).equals(threadId))
					isExist = true;
			if (isExist == true)
			{
				values.put(WSGlobal.SMS_THREAD_ID, sms.get("thread_id").getAsString());
				byte[] byteBody = Base64.decode(sms.get("body").getAsString(), Base64.DEFAULT);
				try {
					body = new String(byteBody, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				values.put(WSGlobal.SMS_BODY, body);
				values.put(WSGlobal.SMS_ADDRESS, sms.get("address").getAsString());
				values.put("_id", sms.get("_id").getAsString());
				values.put(WSGlobal.SMS_DATE, sms.get("date").getAsString());
				activity.getContentResolver().insert(uriBox, values);
			}
		}
	}
}
