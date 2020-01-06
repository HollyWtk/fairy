package com.fairy.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fairy.user.entity.FairyUser;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhh
 * @since 2019-12-19
 */
@Mapper
public interface FairyUserMapper extends BaseMapper<FairyUser> {
    
    public void increaseWrongTimes(String username);

}
