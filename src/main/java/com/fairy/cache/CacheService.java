package com.fairy.cache;

/**
 * 
 * <p>Description: 缓存业务接口 </p>  
 * @author yhh  
 * @date 2020年1月6日
 */
public interface CacheService {
    
    /**
     * 更新用户token
     * @param username
     * @return
     */
    public String updateToken(String username);
    
    /**
     * 判断token是否存在
     * @param token
     * @return
     */
    public boolean checkToken(String username);
    
    /**
     * 主动删除token
     * @param username
     * @return
     */
    public void deleteToken(String username);

    /**
     * 验证token
     * @param username
     * @param token
     * @return
     */
    public boolean verifyToken(String username, String token);
    
}
