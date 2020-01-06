package com.fairy.cache;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.fairy.utils.commons.DateTimeUtils;
import com.fairy.utils.commons.MngContext;
import com.fairy.utils.encrypt.Md5Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SingletonCacheService implements CacheService, InitializingBean {

	@Autowired
	CacheManager cacheMgr;

	protected Cache tokenMap;

	protected Cache sessionMap;

	@Override
	public String updateToken(String username) {

		String token = Md5Util.md5(UUID.randomUUID().toString().replace("-", "") + DateTimeUtils.getStringDate() + username, 16);

		log.info("gen user {} token {}, tmp {}", username, token);
		
		tokenMap.put(username, token);

        sessionMap.put(username, new MngContext(username, getUserAuthedResources(username)));

		return token;
	}

	@Override
	public boolean verifyToken(String username, String token) {
		String sessionToken = tokenMap.get(username, String.class);

        if (sessionToken != null && sessionToken.equals(token)) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public MngContext getSessionContext(String username) {
		return sessionMap.get(username, MngContext.class);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		tokenMap = cacheMgr.getCache("token");
		sessionMap = cacheMgr.getCache("session");
	}

	@Override
	public void setMngContextAttribute(String username, String key, Serializable value) {
		MngContext mngContext = sessionMap.get(username, MngContext.class);
		mngContext.setAttribute(key, value);
		sessionMap.put(username, mngContext);
	}

	@Override
	public Object getMngContextAttribute(String username, String key) {
		return sessionMap.get(username, MngContext.class).getAttributes(key);
	}

	@Override
	public boolean loginStatus(String username) {
		return false;
	}

    @Override
    public void clearToken(String username) {
        tokenMap.evict(username);
    }

    @Override
    public String getUserAuthedResources(String username) {
        return null;
    }
}
