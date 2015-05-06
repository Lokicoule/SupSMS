/*
 * Author : Lokicoule
 */
package com.supsms.app.model;

/*
 * Not used
 */
public class Sms {
	private long id;
	private String body;
	private String address;
	private long thread_id;
	private String date;
	private String box;
	public Sms() {
		// TODO Auto-generated constructor stub
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getThread_id() {
		return thread_id;
	}
	public void setThread_id(long thread_id) {
		this.thread_id = thread_id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getBox() {
		return box;
	}
	public void setBox(String box) {
		this.box = box;
	}

}
