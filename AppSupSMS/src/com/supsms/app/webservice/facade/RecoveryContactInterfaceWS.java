/*
 * Author : Lokicoule
 */
package com.supsms.app.webservice.facade;

import java.util.ArrayList;

import android.content.ContentProviderOperation;

import com.google.gson.JsonArray;
import com.supsms.app.activity.HomeActivity;
import com.supsms.app.session.UserSession;

public interface RecoveryContactInterfaceWS {
	/*
	 * Entry point Task to do
	 * Analyse api
	 * call other different part of process
	 */
	public boolean Main(HomeActivity activity, UserSession session);
	
	/*
	 * Set parameters to request
	 * the server will parse these params
	 */
	public void PreparedPostRequest(String username, String password);
	
	/*
	 * Analyse response
	 * call recreateContact
	 */
	public void parseResponse();
	
	/*
	 * Initialize type of operation about ContentProviderOperation arraylist
	 * Parse Contacts JsonArray
	 * and call methods wich will add dname, phones and emails (one by one) 
	 */
	public void recreateContact(JsonArray jsonArray);
	
	/*
	 * add Display Name to ContentProviderOperation
	 */
	public ArrayList<ContentProviderOperation> addDName(ArrayList<ContentProviderOperation> operation, String DName);
	
	/*
	 * Parse Emails JsonArray
	 * And add emails one by one thanks to call addEmail each iteration
	 */
	public ArrayList<ContentProviderOperation> parseEmails(ArrayList<ContentProviderOperation> operation, JsonArray emails);
	
	/*
	 * Parse phones JsonArray
	 * Add phones one by one thanks to call addPhone each iteration
	 */
	public ArrayList<ContentProviderOperation> parsePhones(ArrayList<ContentProviderOperation> operation, JsonArray phones);
	
	/*
	 * Add Email by type to contentProviderOperation
	 */
	public ArrayList<ContentProviderOperation> addEmail(ArrayList<ContentProviderOperation> operation, int type, String email);
	
	/*
	 * Add Phone by type to contentProviderOperation
	 */
	public ArrayList<ContentProviderOperation> addPhone(ArrayList<ContentProviderOperation> operation, int type, String phone);
}
