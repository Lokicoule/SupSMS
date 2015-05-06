/*
 * Author : Lokicoule
 */
package com.supsms.app.webservice.facade;

import android.net.Uri;
import com.google.gson.JsonArray;
import com.supsms.app.activity.HomeActivity;
import com.supsms.app.session.UserSession;

public interface RecoverySmsInterfaceWS {
	/*
	 * Entry point Task to do
	 * Analyse api
	 * call other different part of process
	 */
	boolean Main(HomeActivity activity, Uri uriBox, UserSession session);
	/*
	 * Set parameters to request
	 * the server will parse these params
	 */
	void PreparedPostRequest(String username, String password);
	/*
	 * Analyse response
	 */
	void parseResponse();
	/*
	 * get List thread_id of existant thread
	 * Parse sms json Array
	 * check if thread_id exist before inserting disapear sms
	 */
	void recreateSms(JsonArray smsArray);
}
