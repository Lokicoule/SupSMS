/*
 * Author : Lokicoule
 */
package com.supsms.app.webservice.utils;

import com.google.gson.JsonElement;
import com.supsms.app.model.User;

public class UserUtils {

	public UserUtils() {
		// TODO Auto-generated constructor stub
	}
   
	public static User createUser(JsonElement jsonUser)
	{
		User user = new User();
		try {
			user.setId(jsonUser.getAsJsonObject().get("id").getAsInt());
			user.setUsername(jsonUser.getAsJsonObject().get("username").getAsString());
			user.setPassword(jsonUser.getAsJsonObject().get("password").getAsString());
			user.setPhone(jsonUser.getAsJsonObject().get("phone").getAsString());
			user.setLastname(jsonUser.getAsJsonObject().get("lastname").getAsString());
			user.setFirstname(jsonUser.getAsJsonObject().get("firstname").getAsString());
			user.setEmail(jsonUser.getAsJsonObject().get("email").getAsString());
			user.setAddress(jsonUser.getAsJsonObject().get("address").getAsString());
			user.setPostalCode(jsonUser.getAsJsonObject().get("postalCode").getAsString());
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
		return user;
	}
}
