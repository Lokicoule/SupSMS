/*
 * Author : Lokicoule
 */
package com.supsms.app.webservice.facade;

import com.supsms.app.activity.LoginActivity;
import com.supsms.app.model.User;

public interface LoginInterfaceWS {
	/*
	 * Entry point Task to do
	 * Analyse api
	 * call other different part of process
	 */
	User Main(String username, String password, LoginActivity activity);
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
