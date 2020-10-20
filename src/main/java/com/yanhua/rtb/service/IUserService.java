package com.yanhua.rtb.service;

import com.yanhua.rtb.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Emiya
 * @since 2020-09-28
 */
public interface IUserService extends IService<User> {

    List<User> findAllUser();

    List<?> selectUserPage(String name, Integer pageSize, Integer startPage);
    List<?> selectUserAll(Integer pageSize, Integer startPage);
    boolean insertOrUpdate(User entity);

}
