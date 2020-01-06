package com.fairy.cache;


import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.fairy.utils.commons.MngContext;

public interface CacheService {
	
	 String updateToken(String username);

	 boolean verifyToken(String username, String token);

	 boolean loginStatus(String username);

	 MngContext getSessionContext(String username);
	 
	 void setMngContextAttribute(String username,String key,Serializable value);
	 
	 Object getMngContextAttribute(String username,String key);
	 
	 void clearToken(String username);

    public String getUserAuthedResources(String username);
}
