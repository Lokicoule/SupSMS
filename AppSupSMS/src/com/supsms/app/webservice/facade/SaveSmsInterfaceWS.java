/*
 * Author : Lokicoule
 */
package com.supsms.app.webservice.facade;

import android.net.Uri;
import com.supsms.app.activity.HomeActivity;
import com.supsms.app.session.UserSession;

public interface SaveSmsInterfaceWS {
	/*
	 * Entry point Task to do
	 * Analyse api
	 * call other different part of process
	 */
	public boolean Main(HomeActivity activity, Uri box, UserSession session);
	/*
	 * Set parameters to request
	 * the server will parse these params
	 */
	void PreparedPostRequest(String username, String password);
	/*
	 * Analyse response
	 */
	void parseResponse();
}
