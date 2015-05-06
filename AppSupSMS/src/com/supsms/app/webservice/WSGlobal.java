/*
 * Author : Lokicoule
 */
package com.supsms.app.webservice;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;

@SuppressLint("InlinedApi")
public class WSGlobal {
	public static final String SUPINFO_API_WS = "http://91.121.105.200/API/";
	public static final String SUPSMS_API_WS= "http://5.135.182.173:8082/SupSMS/API/";
	public static final Uri SMS_INBOX_CONTENT_URI = Uri.parse("content://sms/inbox");
	public static final Uri SMS_SENT_CONTENT_URI = Uri.parse("content://sms/sent");
	public static final String SMS_ID = Telephony.Sms._ID;
	public static final	String SMS_BODY = Telephony.Sms.BODY;
	public static final	String SMS_ADDRESS = Telephony.Sms.ADDRESS;
	public static final String SMS_THREAD_ID = Telephony.Sms.THREAD_ID;
	public static final	String SMS_DATE = Telephony.Sms.DATE;
	public static final String SMS_DATE_SENT = Telephony.Sms.DATE_SENT;
	public static final String SORT_ORDER = " _id ASC";
	public static final int MESSAGE_TYPE_INBOX  = 1;
	public static final int MESSAGE_TYPE_SENT   = 2;
	public static final Uri CONTACT_CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
	public static final String CONTACT_ID = ContactsContract.Contacts._ID;
	public static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
	public static final String CONTACT_LASTNAME = ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME;
	public static final String CONTACT_FIRSTNAME = ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME;
	public static final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
	public static final Uri PHONE_CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	public static final String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
	public static final String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
	public static final Uri EMAIL_CONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
	public static final String EMAIL_CONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
	public static final String EMAIL_DATA = ContactsContract.CommonDataKinds.Email.DATA;
}
