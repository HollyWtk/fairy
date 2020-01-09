package com.fairy.user.service.impl;

import java.security.interfaces.RSAPrivateKey;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fairy.configure.FairyProperties;
import com.fairy.user.entity.FairyUser;
import com.fairy.user.mapper.FairyUserMapper;
import com.fairy.user.service.IFairyUserService;
import com.fairy.utils.data.MngResponse;
import com.fairy.utils.encrypt.Md5Util;
import com.fairy.utils.encrypt.RSAUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhh
 * @since 2019-12-19
 */
@Service
public class FairyUserServiceImpl extends ServiceImpl<FairyUserMapper, FairyUser> implements IFairyUserService,UserDetailsService {

    @Autowired
    FairyProperties fairyProperties;
    
    @Resource
    private FairyUserMapper fairyUserMapper;
    
    @Override
    public boolean checkUserPwd(String username, String password) {
        boolean flag = false;
        FairyUser user = this.queryUserByNameOrEmail(username);
        // BASE64解密
        byte[] bt = RSAUtils.decryptBase64(password);
        RSAPrivateKey privateKey = RSAUtils.loadPrivateKey(fairyProperties.getRsaPrivateKey());
        // RSA解密
        password = new String(RSAUtils.decrypt(privateKey, bt));
        // MD5加密
        password = Md5Util.md5(password, 16);
        if (user != null && password.equals(user.getPassword())) {
            flag = true;
        }
        return flag;
    }

    @Override
    public FairyUser queryUserByNameOrEmail(String args) {
        FairyUser user = getOne(new QueryWrapper<FairyUser>().lambda()
                .eq(FairyUser::getAccount, args).or().eq(FairyUser::getEmail, args));
        return user;
    }

    @Override
    public void increaseWrongTimes(String args) {
        fairyUserMapper.increaseWrongTimes(args);
        
    }
    @Override
    public boolean updateUserByNameOrEmail(FairyUser user) {
        boolean result = this.update(user, new UpdateWrapper<FairyUser>().lambda()
                .eq(FairyUser::getAccount,user.getAccount()).or().eq(FairyUser::getEmail, user.getEmail()));
        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        FairyUser user = this.queryUserByNameOrEmail(username);
        if (user == null) {
            MngResponse mngResponse = new MngResponse();
            mngResponse.code = 102;
            mngResponse.message = "账户或密码错误";
            throw new DisabledException(JSONObject.toJSONString(mngResponse));
        }
        return new FairyUser(user.getUsername(), user.getPassword(), user.getAuthorities());
    }


}
