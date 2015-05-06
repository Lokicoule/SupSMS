/*
 * Author : Lokicoule
 */
package com.supsms.app.activity.enumeration;

public enum RESPONSE {
	SAVE_CONTACT("SAVE CONTACT"),
	RECOVERY_CONTACT("RECOVERY CONTACT"),
	SAVE_SMS_INBOX("SAVE INBOX SMS"),
	SAVE_SMS_SENT("SAVE SENT SMS"),
	RECOVERY_INBOX_SMS("RECOVERY INBOX SMS"),
	RECOVERY_SENT_SMS("RECOVERY SENT SMS");
	
	private final String resp;
	
	private RESPONSE (final String resp)
	{
		this.resp = resp;
	}
	
	@Override
	public String toString()
	{
		return resp;
	}
}
