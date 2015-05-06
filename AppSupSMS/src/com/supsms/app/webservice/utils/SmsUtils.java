/*
 * Author : Lokicoule
 */
package com.supsms.app.webservice.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;

import com.supsms.app.webservice.WSGlobal;

public class SmsUtils {

	public SmsUtils() {
	}
	
	public static List<String> getThreadsId(ActionBarActivity activity, Uri box)
	{
		List<String> threadsId = new ArrayList<String>();
		ContentResolver contentResolver = activity.getContentResolver();
		Cursor cursor_sms = contentResolver.query(box, null, null, null, WSGlobal.SORT_ORDER);
		if (cursor_sms.getCount() > 0)
		{
			while (cursor_sms.moveToNext())
				threadsId.add(cursor_sms.getString(cursor_sms.getColumnIndex(WSGlobal.SMS_THREAD_ID)));
			cursor_sms.close();
		}
		return threadsId;
	}
	
	public static JSONArray ListSMS(ActionBarActivity activity, Uri box)
	{
		JSONArray sms = new JSONArray();
		
		ContentResolver contentResolver = activity.getContentResolver();
		Cursor cursor_sms = contentResolver.query(box, null, null, null, WSGlobal.SORT_ORDER);
		if (cursor_sms.getCount() > 0)
		{
			while (cursor_sms.moveToNext())
				sms.put(putSmsToJSONObject(cursor_sms, box));
			cursor_sms.close();
		}
		return sms;
	}
	
	private static JSONObject putSmsToJSONObject(final Cursor cursor_sms, final Uri box)
	{
		final JSONObject tmp = new JSONObject();
		try {
			ExecutorService pool = Executors.newCachedThreadPool();
			Callable<Void> putId = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					tmp.put("_id", cursor_sms.getString(cursor_sms.getColumnIndex(WSGlobal.SMS_ID)));
					return null;
				}
			};
			Callable<Void> putBody = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					tmp.put("body", Base64.encodeToString(cursor_sms.getString(cursor_sms.getColumnIndex(WSGlobal.SMS_BODY)).getBytes(), Base64.DEFAULT));
					return null;
				}
			};
			Callable<Void> putAddress = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					tmp.put("address", cursor_sms.getString(cursor_sms.getColumnIndex(WSGlobal.SMS_ADDRESS)));
					return null;
				}
			};
			Callable<Void> putThreadId = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					tmp.put("thread_id", cursor_sms.getString(cursor_sms.getColumnIndex(WSGlobal.SMS_THREAD_ID)));
					return null;
				}
			};
			Callable<Void> putDate = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					tmp.put("date", cursor_sms.getString(cursor_sms.getColumnIndex(WSGlobal.SMS_DATE)));
					return null;
				}
			};
			Callable<Void> putBox = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					if (box.equals(WSGlobal.SMS_INBOX_CONTENT_URI))
						tmp.put("box", "inbox");
					else
						tmp.put("box", "sent");
					return null;
				}
			};
			pool.submit(putId);
			pool.submit(putAddress);
			pool.submit(putBody);
			pool.submit(putBox);
			pool.submit(putThreadId);
			pool.submit(putDate);
			pool.shutdown();
			try {
				pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}

}
