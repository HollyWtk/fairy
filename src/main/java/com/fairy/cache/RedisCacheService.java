package com.fairy.cache;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fairy.user.entity.FairyUser;
import com.fairy.user.service.IFairyUserService;
import com.fairy.utils.commons.DateTimeUtils;
import com.fairy.utils.commons.RedisUtil;
import com.fairy.utils.encrypt.Md5Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisCacheService implements CacheService {

    @Autowired
    private RedisUtil redisUtil;
    
    @Autowired
    private IFairyUserService fairyUserService;
    
    @Override
    public String updateToken(String username) {
        String token = Md5Util.md5(UUID.randomUUID().toString().replace("-", "") + DateTimeUtils.getStringDate() + username, 16);
        //put token into redis, expiretime 3000s key: token_username
        redisUtil.setString("token_" + username, token, 3000);
        log.info("create user token :{}",token);
        //put userinfo into redis,expiretime 3000s key:info_username
        FairyUser user = fairyUserService.queryUserByNameOrEmail(username);
        redisUtil.setString("info_" + username,user,3000);
        return token;
    }


    @Override
    public boolean checkToken(String username) {
        return redisUtil.hasKey("token_" + username);
    }


    @Override
    public void deleteToken(String username) {
        redisUtil.del("token_" + username,"info_" + username);
        log.info("delete token/userInfo,user:{}",username);
    }


    @Override
    public boolean verifyToken(String username, String token) {
        boolean flag = false;
        if(checkToken(username)) {
            String exsitToken = (String) redisUtil.getString("token_" + username);
            if(StringUtils.isNotBlank(exsitToken) && exsitToken.equals(token)) {
                flag = true;
            }
        }
        return flag;
    }
    
}
