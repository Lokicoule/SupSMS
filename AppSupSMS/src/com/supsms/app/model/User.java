/*
 * Author : Lokicoule
 */
package com.supsms.app.model;

public class User {
	private int id;
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String phone;
	private String postalCode;
	private String address;
	private String email;
	private boolean api;
	private boolean isValidUser;
	
	public User() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getApi() {
		return api;
	}

	public void setApi(boolean api) {
		this.api = api;
	}

	public boolean isValidUser() {
		return isValidUser;
	}

	public void setValidUser(boolean isValidUser) {
		this.isValidUser = isValidUser;
	}
}
