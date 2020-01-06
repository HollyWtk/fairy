/*package com.fairy.serurity;


import java.io.Serializable;
import java.security.interfaces.RSAPrivateKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.alibaba.fastjson.JSONObject;
import com.cmiot.mng.autoconfigure.LionProperties;
import com.cmiot.mng.core.data.User;
import com.cmiot.mng.core.mapper.UserMapper;
import com.cmiot.mng.core.service.MngSvrEncrypter;
import com.cmiot.mng.utils.data.MngResponse;
import com.cmiot.mng.utils.encrypt.RSAUtils;


*//**
 * 从数据库中获取用户数据
 *//*
public class UserDetailsServiceImpl implements UserDetailsService, Serializable {

	*//**
	 * 
	 *//*
	private static final long serialVersionUID = 8965556313708080404L;

	@Autowired
    private UserMapper userMapper;
	
    @Autowired
    MngSvrEncrypter ecrypter;
    
    @Autowired
    LionProperties lionProperties;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.queryUserByAccountNoOrEmail(username);
        if (user == null) {
            MngResponse mngResponse = new MngResponse();
            mngResponse.code = 102;
            mngResponse.message = "账户或密码错误";
            throw new DisabledException(JSONObject.toJSONString(mngResponse));
		}
        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
	}

    @Override
    public User queryUserByAccountNoOrEmail(String accountNo) {
        return userMapper.queryUserByAccountNoOrEmail(accountNo);
    }

    @Override
    public void updateWrongTimes(String username) {
        userMapper.updateWrongTimes(username);
    }

    @Override
    public Integer updateUserByAccountOrEmail(User user) {
        return userMapper.updateUserByAccountOrEmail(user);
    }

    @Override
    public boolean checkUserPwd(String username, String password) {
        boolean flag = false;
        User user = userMapper.queryUserByAccountNoOrEmail(username);
        // BASE64解密
        byte[] bt = RSAUtils.decryptBase64(password);
        RSAPrivateKey privateKey = RSAUtils.loadPrivateKey(lionProperties.getRsaPrivateKey());
        // RSA解密
        password = new String(RSAUtils.decrypt(privateKey, bt));
        // DES加密
        password = ecrypter.encrypt(password);
        if (user != null && password.equals(user.getPassword())) {
            flag = true;
        }
        return flag;
    }


}
*/