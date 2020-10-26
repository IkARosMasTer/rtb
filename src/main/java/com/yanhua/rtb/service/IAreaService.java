/**
 * @filename:AreaService 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhua.rtb.entity.Area;

import java.util.List;

/**
 * @description 渠道服务层
 * @version V1.0
 * @author Emiya
 * 
 */
public interface IAreaService extends IService<Area> {

    List<Area> findAll();

    int removeById(Integer id);

    int disableById(Integer id);

//    @Override
//    boolean saveOrUpdate(Area entity);

    /**
     *
     * @description: 检查地区
     *      <p/>
     * @param areaId:
     * @return void
     */
    void checkArea(Integer areaId);
}