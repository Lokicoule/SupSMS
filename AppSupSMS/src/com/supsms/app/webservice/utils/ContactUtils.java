/*
 * Author : Lokicoule
 */
package com.supsms.app.webservice.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;

import com.supsms.app.session.UserSession;
import com.supsms.app.webservice.WSGlobal;

public class ContactUtils {

	public ContactUtils() {
	}
	
	public static JSONArray ListContacts(ActionBarActivity activity, UserSession session)
	{
		JSONArray contacts = new JSONArray();
		
		final ContentResolver contentResolver = activity.getContentResolver();
		final Cursor cursor_contacts = contentResolver.query(WSGlobal.CONTACT_CONTENT_URI, null,null, null, null);	
		
		if (cursor_contacts.getCount() > 0)
		{
			while (cursor_contacts.moveToNext()) 
			{
				JSONObject tmp = new JSONObject();
				final String contact_id = cursor_contacts.getString(cursor_contacts.getColumnIndex( WSGlobal.CONTACT_ID ));
				try {
					tmp.put("_ID", contact_id);
					if (Integer.parseInt(cursor_contacts.getString(cursor_contacts.getColumnIndex( WSGlobal.HAS_PHONE_NUMBER ))) > 0) 
					{
						List<Cursor> cursors = getCursors(contentResolver, contact_id);
						cursors.add(cursor_contacts);
						tmp = putDNameEmailsAndPhones(tmp, cursors, session);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				contacts.put(tmp);
			}
			cursor_contacts.close();
		}
		//System.out.println(contacts);
		return contacts;
	}
	
	private static JSONObject putDNameEmailsAndPhones(final JSONObject tmp, List<Cursor> cursors, UserSession session)
	{
		try {
			final JSONArray emails = new JSONArray();
			final JSONArray phones = new JSONArray();
			final Cursor phoneCursor = cursors.get(0);
			final Cursor emailCursor = cursors.get(1);
			final Cursor cursor_contacts = cursors.get(2);
			ExecutorService pool = Executors.newCachedThreadPool();
			Callable<Void> putDNameTask = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					tmp.put("DNAME", cursor_contacts.getString(cursor_contacts.getColumnIndex( WSGlobal.DISPLAY_NAME )));
					return null;
				}
			};
			if (session.getApi())
			{
				Callable<Void> putSinglePNum = new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						try {
							phoneCursor.moveToFirst();
							tmp.put("PNUM", phoneCursor.getString(phoneCursor.getColumnIndex(WSGlobal.NUMBER)));
						} catch (Exception e) {
							e.printStackTrace();
						}
						phoneCursor.close();
						return null;
					}
				};
				Callable<Void> putSingleEmail = new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						try {
							emailCursor.moveToFirst();
							tmp.put("EMAIL", emailCursor.getString(emailCursor.getColumnIndex(WSGlobal.EMAIL_DATA)));
						} catch (Exception e) {
							e.printStackTrace();
						}
						emailCursor.close();
						return null;
					}
				};
				pool.submit(putSingleEmail);
				pool.submit(putSinglePNum);
				pool.submit(putDNameTask);
				pool.shutdown();
				try {
					pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else 
			{
				Callable<Void> putEmails = new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						while (emailCursor.moveToNext())
						{
							JSONObject tmpEmail = new JSONObject();
							tmpEmail.put("EMAIL", emailCursor.getString(emailCursor.getColumnIndex(WSGlobal.EMAIL_DATA)));
							emails.put(tmpEmail);
						}
						emailCursor.close();
						tmp.put("emails", emails);
						return null;
					}
				};
				Callable<Void> putPhones = new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						while (phoneCursor.moveToNext())
						{
							JSONObject tmpPhone = new JSONObject();
							tmpPhone.put("PNUM", phoneCursor.getString(phoneCursor.getColumnIndex(WSGlobal.NUMBER)));
							phones.put(tmpPhone);
						}
						phoneCursor.close();
						tmp.put("phones", phones);
						return null;
					}
				};
				pool.submit(putEmails);
				pool.submit(putPhones);pool.submit(putDNameTask);
				pool.shutdown();
				try {
					pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}
	
	private static List<Cursor> getCursors(final ContentResolver contentResolver, final String contact_id)
	{
		List<Cursor> cursors = new ArrayList<Cursor>();
		try {
			ExecutorService pool = Executors.newCachedThreadPool();
			Callable<Cursor> getPhoneCursorTask = new Callable<Cursor>() {
	
				@Override
				public Cursor call() throws Exception {
					return contentResolver.query(WSGlobal.PHONE_CONTENT_URI, null, WSGlobal.PHONE_CONTACT_ID + " = ?", new String[] { contact_id }, null);
				}
			};
			Callable<Cursor> getEmailCursorTask = new Callable<Cursor>() {
	
				@Override
				public Cursor call() throws Exception {
					return contentResolver.query(WSGlobal.EMAIL_CONTENT_URI,	null, WSGlobal.EMAIL_CONTACT_ID+ " = ?", new String[] { contact_id }, null);
				}
			};
			Future<Cursor> phoneCursorFuture = pool.submit(getPhoneCursorTask);
			Future<Cursor> emailCursorFuture = pool.submit(getEmailCursorTask);
			pool.shutdown();
			try {
				cursors.add(phoneCursorFuture.get());
				cursors.add(emailCursorFuture.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursors;
	}

}
