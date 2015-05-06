/*
 * Author : Lokicoule
 */
package com.supsms.app.model;

import java.util.List;

/*
 * Not used
 */
public class Contact {
	private long id;
	private String dname;
	private List<String> emails;
	private List<String> phones;
	
	public Contact() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
	
	public void addEmail(String email)
	{
		getEmails().add(email);
	}

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}
	
	public void addPhone(String phone) {
		getPhones().add(phone);
	}
}
