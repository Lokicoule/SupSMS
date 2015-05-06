/*
 * Author : Lokicoule
 */
package com.supsms.app.session.enumeration;

public enum SESSION {
	PREFERENCE_NAME("SupSMSPref"),
	KEY_NAME("userName"),
	KEY_PASSWORD("userPassword"),
	KEY_API("API");
	private final String resp;
	
	private SESSION (final String resp)
	{
		this.resp = resp;
	}
	
	@Override
	public String toString()
	{
		return resp;
	}
}
