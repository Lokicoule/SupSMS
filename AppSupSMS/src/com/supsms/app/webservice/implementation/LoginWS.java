/*
 * Author : Lokicoule
 */
package com.supsms.app.webservice.implementation;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;

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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.supsms.app.activity.LoginActivity;
import com.supsms.app.model.User;
import com.supsms.app.webservice.WSGlobal;
import com.supsms.app.webservice.facade.LoginInterfaceWS;
import com.supsms.app.webservice.utils.UserUtils;

public class LoginWS implements LoginInterfaceWS {
	
	private static HttpPost post = new HttpPost();
	private static boolean success = false;
	private static HttpResponse response;
	URI uri;
	User user;
	
	public LoginWS ()
	{
		
	}
	
	@Override
    public User Main(String username, String password, LoginActivity activity)
    {   
		try {
			uri = (activity.getApi()) ? new URI(WSGlobal.SUPINFO_API_WS) : new URI(WSGlobal.SUPSMS_API_WS);
			HttpClient httpClient = new DefaultHttpClient();
    		PreparedPostRequest(username, password);
    		response = httpClient.execute(post);
    		parseResponse();
    	} catch (Exception e) {
    		activity.setAllRight(false);
            e.printStackTrace();
        }
    	return user;
    }
    
    @Override
    public void parseResponse()
    {
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
	    	System.out.println(jsonObject);
	    	if (success)
	    		user = UserUtils.createUser(jsonObject.get("user"));
	    	user.setValidUser(success);
	    	System.out.println(success);
	    }
    }   
    
    @Override
    public void PreparedPostRequest(String username, String password)
    {
    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("action","login"));
		params.add(new BasicNameValuePair("login", username));
		params.add(new BasicNameValuePair("password", password));
    	post.setURI(uri);
		try {
			post.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
}
