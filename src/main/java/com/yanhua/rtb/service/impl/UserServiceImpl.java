package com.yanhua.rtb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhua.rtb.common.GlobalConstant;
import com.yanhua.rtb.entity.User;
import com.yanhua.rtb.mapper.UserMapper;
import com.yanhua.rtb.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Emiya
 * @since 2020-09-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Resource
    @Qualifier("redisTemplateCustomize")
    private RedisTemplate<Object,Object> template;
//    @Autowired
//    private StringRedisTemplate redisTemplate;


    @Transactional
    @Override
    public List<User> findAllUser() {
        return userMapper.findAllUser();
    }

    @Override
    public List<?> selectUserPage(String name, Integer pageSize, Integer startPage) {

        User user1 = (User) template.opsForHash().get("user", "user:" + name);
        List<User> userList = new ArrayList<User>();
        template.opsForValue().set(GlobalConstant.CTCC_TOKEN_KEY,"ggggffsf",3600, TimeUnit.SECONDS);
                    String token = (String) template.opsForValue().get(GlobalConstant.CTCC_TOKEN_KEY);

//        List<Object> values = template.opsForHash().values("user:" + name);
        if(user1 != null){
            userList.add(user1);
            System.out.println("未走数据库");
            return userList;
        }
        //like（“user_name”,name）
//        IPage<User> users = userMapper.selectPage(new Page<User>(startPage,pageSize),new QueryWrapper<User>().lambda().like(User::getUserName, name));
//        new QueryWrapper<User>().lambda().gt(User::getId, 1);
//        new QueryWrapper<User>().lambda().le(User::getId, 3);
//        new QueryWrapper<User>().lambda().or().le(User::getId, 3);
//        new QueryWrapper<User>().lambda().isNotNull(User::getId);
        List<User> users = userMapper.findAllUser();
        for (User user:users

        ) {
            template.opsForHash().put("user","user:"+name,user);
//            template.opsForValue().set("gg",user);
//            redisTemplate.opsForHash().put("user","user:"+name,JSONObject.toJSON(user).toString());
            System.out.println("存入缓存");
        }
        return users;
    }

    @Override
    public List<?> selectUserAll(Integer pageSize, Integer startPage) {
        List<Object> list = (List<Object>) template.opsForValue().get("user");
        if(list != null && list.size()>0){
            System.out.println("未走数据库");
            return list;
        }
        IPage<User> users = userMapper.selectPage(new Page<User>(startPage, pageSize), new QueryWrapper<User>());
        for (User user: users.getRecords()
        ) {
            template.opsForValue().set("user:"+user.getUserName(),user);
//            template.opsForValue().get();
            System.out.println("走数据库");
        }
        return users.getRecords();
    }

    /**
     * <p>
     * TableId 注解存在更新记录，否插入一条记录
     * </p>
     *
     * @param entity 实体对象
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertOrUpdate(User entity) {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
                Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
                if (StringUtils.checkValNull(idVal)) {
                    return save(entity);
                } else {
                    /*
                     * 更新成功直接返回，失败执行插入逻辑
                     */
                    template.opsForHash().put("user","user:"+entity.getUserName(),entity);
//                    template.opsForList().leftPush("user:"+user.getUsername(),user);
                    Object user = template.opsForHash().get("user", "user:" + entity.getUserName());
                    System.out.println("gggg"+user.toString());
                    User user1 = (User) user;

                    return updateById(entity) || save(entity);
                }
            } else {
                throw new MybatisPlusException("Error:  Can not execute. Could not find @TableId.");
            }
        }
        return false;
    }

}
