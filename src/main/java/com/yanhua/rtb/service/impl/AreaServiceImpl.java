/**
 * @filename:AreaServiceImpl 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2018 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.service.impl;

import com.yanhua.rtb.common.EngineException;
import com.yanhua.rtb.entity.Area;
import com.yanhua.rtb.mapper.AreaMapper;
import com.yanhua.rtb.mapper.UserMapper;
import com.yanhua.rtb.service.IAreaService;
import com.yanhua.rtb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.yanhua.rtb.common.ResultCodeEnum.PARAM_IS_INVALID;
import static com.yanhua.rtb.common.ResultCodeEnum.PARAM_NOT_COMPLETE;

/**   
 * @description 渠道服务实现
 *
 * @version V1.0
 * @author Emiya
 * 
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements IAreaService {

    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IUserService iUserService;

//    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Area> findAll() {

        //        areas.forEach(area -> {
//            area.setAreaId(null);
//        });
//        saveOrUpdateBatch(areas);
//        List<User> users = userMapper.findAllUser();
//        users.forEach(user -> {
//            user.setId(null);
//        });
//        iUserService.saveOrUpdateBatch(users);
        return areaMapper.selectAll();
    }


    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public int removeById(Integer id) {

//        areaMapper.delete()
        return areaMapper.delByAreaId(id);
//        return remove(new QueryWrapper<Area>().lambda().eq(Area::getAreaId,id));
    }

    @Override
    public int disableById(Integer id) {
        return Optional.of(areaMapper.deleteById(id)).orElseThrow(EngineException::new);

//        return areaMapper.deleteById(id);
    }

    @Override
    public void checkArea(Integer channelId) {
        if (channelId==null) {
            throw new EngineException(PARAM_NOT_COMPLETE);
        }
        if (channelId<0){
            throw new EngineException(PARAM_IS_INVALID);
        }
        Area area = areaMapper.selectById(channelId);
        if (area==null){
            throw new EngineException("当前渠道为空");
        }
        if (area.getStatus()!=0){
            throw new EngineException("当前渠道不可用");
        }
    }
}