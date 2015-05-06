/*
 * Author : Lokicoule
 */
package com.supsms.app.webservice.implementation;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

import android.content.ContentProviderOperation;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.supsms.app.activity.HomeActivity;
import com.supsms.app.session.UserSession;
import com.supsms.app.session.enumeration.SESSION;
import com.supsms.app.webservice.WSGlobal;
import com.supsms.app.webservice.facade.RecoveryContactInterfaceWS;
import com.supsms.app.webservice.utils.ContactUtils;

public class RecoveryContactWS implements RecoveryContactInterfaceWS{
	
	private static HttpPost post = new HttpPost();
	private JSONObject contactsJson = new JSONObject();
	private boolean success = false;
	private static HttpResponse response;
	private String URL_API;
	private ActionBarActivity activity;
	
	public RecoveryContactWS() {
	}

	@Override
	public boolean Main(HomeActivity activity, UserSession session)
	{
		try {
			this.activity = activity;
			HttpClient httpClient = new DefaultHttpClient();
			JSONArray contacts = ContactUtils.ListContacts(activity, session);
			HashMap<String, String> user = session.getUserDetails();
			contactsJson.put("contacts", contacts);
			System.out.println(contactsJson);
			URL_API = (session.getApi()) ? WSGlobal.SUPINFO_API_WS : WSGlobal.SUPSMS_API_WS;
			PreparedPostRequest(user.get(SESSION.KEY_NAME.toString()).toString(), user.get(SESSION.KEY_PASSWORD.toString()).toString());
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
	public void PreparedPostRequest(String username, String password)
	    {
	    	URI uri = null;
			try {
				uri = new URI(URL_API);
				System.out.println(uri);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			try {
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		    	params.add(new BasicNameValuePair("action","dobackupcontacts"));
				params.add(new BasicNameValuePair("login", username));
				params.add(new BasicNameValuePair("password", password));
				params.add(new BasicNameValuePair("contacts", contactsJson.toString()));
		    	post.setURI(uri);
				post.setEntity(new UrlEncodedFormEntity(params));
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	    	recreateContact(jsonObject.getAsJsonArray("contacts"));
	    	success = isSuccess.getAsBoolean();
	    }
    }
	
	@Override
	public void recreateContact(JsonArray jsonArray)
	{
		if (jsonArray == null)
			return;
		for (int i = 0; i < jsonArray.size(); i++)
		{
			ArrayList <ContentProviderOperation> operation = new ArrayList <ContentProviderOperation> ();
			JsonObject contact = jsonArray.get(i).getAsJsonObject();
			try {
				operation.add(ContentProviderOperation.newInsert(
						 ContactsContract.RawContacts.CONTENT_URI)
					     .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
					     .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
					     .build());
				operation = parseEmails(operation, contact.getAsJsonArray("emails"));
				operation = parsePhones(operation, contact.getAsJsonArray("phones"));
				operation = addDName(operation, contact.get("DNAME").getAsString());
				try {
				    activity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operation);
				 } catch (Exception e) {
				     e.printStackTrace();
				 }
			} catch (Exception e) {
				
			}
		}
	}
	
	@Override
	public ArrayList<ContentProviderOperation> addDName(ArrayList<ContentProviderOperation> operation, String DName)
	{
		if (DName != null)
		{
			operation.add(ContentProviderOperation.newInsert(
				ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
							.withValue(	
								ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
								DName).build());
		}
		return operation;
	}
	
	@Override
	public ArrayList<ContentProviderOperation> parseEmails(ArrayList<ContentProviderOperation> operation, JsonArray emails)
	{
		if (emails != null)
		{
			for (int j = 0; j < emails.size(); j++)
			{
				String email = emails.get(j).getAsJsonObject().get("EMAIL").getAsString();
				switch (j)
				{
				case 0:
					operation = addEmail(operation, ContactsContract.CommonDataKinds.Email.TYPE_HOME, email);
					break;
				case 1:
					operation = addEmail(operation, ContactsContract.CommonDataKinds.Email.TYPE_WORK, email);
					break;
				default:
					operation = addEmail(operation, ContactsContract.CommonDataKinds.Email.TYPE_OTHER, email);
					break;
				}
			}
		}
		return operation;
	}
	
	@Override
	public ArrayList<ContentProviderOperation> addEmail(ArrayList<ContentProviderOperation> operation, int type, String email)
	{
		operation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
		         .withValue(ContactsContract.CommonDataKinds.Email.TYPE, type)
		         .build());
		return operation;
	}
	
	@Override
	public ArrayList<ContentProviderOperation> parsePhones(ArrayList<ContentProviderOperation> operation, JsonArray phones)
	{
		if (phones != null)
		{
			for (int j = 0; j < phones.size(); j++)
			{
				String phone = phones.get(j).getAsJsonObject().get("PNUM").getAsString();
				switch (j)
				{
					case 0:
						operation = addPhone(operation, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, phone);
						break;
					case 1:
						operation = addPhone(operation, ContactsContract.CommonDataKinds.Phone.TYPE_HOME, phone);
						break;
					case 2:
						operation = addPhone(operation, ContactsContract.CommonDataKinds.Phone.TYPE_WORK, phone);
						break;
					default:
						operation = addPhone(operation, ContactsContract.CommonDataKinds.Phone.TYPE_OTHER, phone);
						break;
				}
			}
		}
		return operation;
	}
	
	@Override
	public ArrayList<ContentProviderOperation> addPhone(ArrayList<ContentProviderOperation> operation, int type, String phone)
	{
		operation.add(ContentProviderOperation.
			     newInsert(ContactsContract.Data.CONTENT_URI)
			         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
			         .withValue(ContactsContract.Data.MIMETYPE,
			     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
			         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
			         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, type).build());
		return operation;
	}
}
