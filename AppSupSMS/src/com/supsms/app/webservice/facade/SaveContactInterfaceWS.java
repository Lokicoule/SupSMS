/*
 * Author : Lokicoule
 */
package com.supsms.app.webservice.facade;

import com.supsms.app.activity.HomeActivity;
import com.supsms.app.session.UserSession;

public interface SaveContactInterfaceWS {
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
	 */
	public void parseResponse();
}
