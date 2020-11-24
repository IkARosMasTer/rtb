/**
 * @filename:AreaServiceImpl 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2018 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanhua.rtb.common.EngineException;
import com.yanhua.rtb.common.GlobalConstant;
import com.yanhua.rtb.entity.Area;
import com.yanhua.rtb.entity.Element;
import com.yanhua.rtb.entity.RColumn;
import com.yanhua.rtb.entity.Template;
import com.yanhua.rtb.mapper.AreaMapper;
import com.yanhua.rtb.mapper.RColumnMapper;
import com.yanhua.rtb.mapper.UserMapper;
import com.yanhua.rtb.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.yanhua.rtb.common.ResultCodeEnum.PARAM_IS_INVALID;
import static com.yanhua.rtb.common.ResultCodeEnum.PARAM_NOT_COMPLETE;

/**   
 * @description 渠道服务实现
 *
 * @version V1.0
 * @author Emiya
 * 
 */
@Slf4j
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements IAreaService {

    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IRColumnService irColumnService;
    @Autowired
    private RColumnMapper rColumnMapper;
    @Autowired
    private ITemplateService iTemplateService;
    @Autowired
    private ITempletService iTempletService;


    @Resource
    @Qualifier("redisTemplateCustomize")
    private RedisTemplate<Object,Object> template;
//    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Area> findAll() {
//        Thread.sleep(3000);
//        template.opsForValue().set(GlobalConstant.CTCC_TOKEN_KEY,"df4fcdfad9154d02b7c1bd3a6a59e727",3600, TimeUnit.SECONDS);

//        String token = (String) template.opsForValue().get(GlobalConstant.CTCC_TOKEN_KEY);
//        System.out.println("bbbbbbbbbbbbbb===="+token);


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
    public Area checkArea(Integer channelId) {
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
        return area;
    }

    @Override
    public List<RColumn> copyAreaAll(Area area) {
        Integer channelId = area.getChannelId();
        log.info("渠道复制===================>源channelId:"+channelId);
        List<RColumn> rColumns = rColumnMapper.selectList(new QueryWrapper<RColumn>().lambda().eq(RColumn::getChannelId,channelId).eq(RColumn::getLevel,"1").orderByAsc(RColumn::getColumnOrder));
        //遍历栏目
        rColumns.stream().filter(Objects::nonNull).forEach(rColumn -> {
            Integer columnId = rColumn.getColumnId();
            Integer templateId = rColumn.getTemplateId();
//            Template template = iTemplateService.getById(templateId);
//            if (template!=null){
//                template.setTemplateId(null);
//            }
//            rColumn.setSpeTemplate(template);
            List<RColumn> templets = rColumnMapper.selectList(new QueryWrapper<RColumn>().lambda().eq(RColumn::getParColumnId,columnId).eq(RColumn::getChannelId,channelId).eq(RColumn::getLevel,"2").orderByAsc(RColumn::getColumnOrder));
            templets.forEach(templet ->{
//                Integer temTemplateId = templet.getTemplateId();
//                Template temTemplate = iTemplateService.getById(temTemplateId);
                Integer templetId = templet.getColumnId();
                List<Element> elements = iTempletService.getElementCopy(templetId);
                templet.setElementList(elements);
                templet.setColumnId(null);
                templet.setChannelId(null);
                templet.setParColumnId(null);
            });
            rColumn.setTempletList(templets);
            rColumn.setChannelId(null);
            rColumn.setColumnId(null);
        });
        return rColumns;
//        irColumnService.importColumnList(rColumns);
    }
}