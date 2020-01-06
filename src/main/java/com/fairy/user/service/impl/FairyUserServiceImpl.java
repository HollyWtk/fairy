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
import com.fairy.common.MngSvrEncrypter;
import com.fairy.configure.FairyProperties;
import com.fairy.user.entity.FairyUser;
import com.fairy.user.mapper.FairyUserMapper;
import com.fairy.user.service.IFairyUserService;
import com.fairy.utils.data.MngResponse;
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
    MngSvrEncrypter ecrypter;
    
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
        // DES加密
        password = ecrypter.encrypt(password);
        if (user != null && password.equals(user.getFldPassword())) {
            flag = true;
        }
        return flag;
    }

    @Override
    public FairyUser queryUserByNameOrEmail(String args) {
        FairyUser user = getOne(new QueryWrapper<FairyUser>().lambda()
                .eq(FairyUser::getFldAccount, args).or().eq(FairyUser::getFldEmail, args));
        return user;
    }

    @Override
    public void increaseWrongTimes(String args) {
        fairyUserMapper.increaseWrongTimes(args);
        
    }
    @Override
    public boolean updateUserByNameOrEmail(FairyUser user) {
        boolean result = this.update(user, new UpdateWrapper<FairyUser>().lambda()
                .eq(FairyUser::getFldAccount,user.getFldAccount()).or().eq(FairyUser::getFldEmail, user.getFldEmail()));
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
