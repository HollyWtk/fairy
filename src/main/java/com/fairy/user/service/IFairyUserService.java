package com.fairy.user.service;

import com.fairy.user.entity.FairyUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhh
 * @since 2019-12-19
 */
public interface IFairyUserService extends IService<FairyUser> {

    /**
     *  判断账户/密码是否正确
     * @param username
     * @param password
     * @return
     *         true --> 正确, false -->账户/密码错误
     */
    public boolean checkUserPwd(String username,String password);
    
    /**
     * 根据用户名或者邮箱查询用户
     * @param args
     *          用户名  | 邮箱
     * @return
     */
    FairyUser queryUserByNameOrEmail(String args);
    
    /**
     * 增加用户登录错误次数
     * @param args
     *          用户名  | 邮箱
     */
    public void increaseWrongTimes(String args);
    
    /**
     * 根据用户名或者邮箱更新用户
     * @return
     */
    boolean updateUserByNameOrEmail(FairyUser user);

}
