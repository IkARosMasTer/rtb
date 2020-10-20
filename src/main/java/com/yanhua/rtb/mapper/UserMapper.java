package com.yanhua.rtb.mapper;

import com.yanhua.rtb.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Emiya
 * @since 2020-09-28
 */
public interface UserMapper extends BaseMapper<User> {

    public List<User> findAllUser();
}
