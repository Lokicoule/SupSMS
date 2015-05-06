/*
 * Author : Lokicoule
 */
package com.supsms.app.webservice;

import com.supsms.app.webservice.facade.LoginInterfaceWS;
import com.supsms.app.webservice.facade.RecoveryContactInterfaceWS;
import com.supsms.app.webservice.facade.RecoverySmsInterfaceWS;
import com.supsms.app.webservice.facade.SaveContactInterfaceWS;
import com.supsms.app.webservice.facade.SaveSmsInterfaceWS;
import com.supsms.app.webservice.implementation.LoginWS;
import com.supsms.app.webservice.implementation.RecoveryContactWS;
import com.supsms.app.webservice.implementation.RecoverySmsWS;
import com.supsms.app.webservice.implementation.SaveContactsWS;
import com.supsms.app.webservice.implementation.SaveSmsWS;

public class WSFactory {
	
	private static boolean ws = true;
	
	public WSFactory() {
	}
	
	public static LoginInterfaceWS getLoginWS()
	{
		return (ws) ? new LoginWS() : null;
	}
	
	public static SaveContactInterfaceWS getSaveContactWS()
	{
		return (ws) ? new SaveContactsWS() : null;
	}
	
	public static SaveSmsInterfaceWS getSaveSmsWS()
	{
		return (ws) ? new SaveSmsWS() : null;
	}
	
	public static RecoverySmsInterfaceWS getRecoverySmsWS()
	{
		return (ws) ? new RecoverySmsWS() : null;
	}
	
	public static RecoveryContactInterfaceWS getRecoveryContactWS()
	{
		return (ws) ? new RecoveryContactWS() : null;
	}
}
