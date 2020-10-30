/**
 * @filename:IRColumnService 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.service;

import com.yanhua.rtb.vo.ColumnVo;
import com.yanhua.rtb.entity.RColumn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @description 栏目表服务层
 * @version V1.0
 * @author Emiya
 * 
 */
public interface IRColumnService extends IService<RColumn> {
    /**
     *
     * @description: 获取栏目、专题、元素所有数据
     *      <p/>
     * @param channelId:
     * @param columnId:
     * @param startPage:
     * @param pageSize:
     */
    List<ColumnVo> getAllColumnVoList(Integer channelId, Integer columnId, Integer startPage, Integer pageSize);

    /**
     *
     * @description: 获取地区栏目
     *      <p/>
     * @param channelId:
     */
    List<ColumnVo> getColumnVoList(Integer channelId);


    /**
     *
     * @description: 更新/新增栏目整体
     *      <p/>
     * @param columnVo:
     * @return java.lang.String
     */
    String saveOrUpdateColumnVo(ColumnVo columnVo);

    /**
     *
     * @description: 根据父栏目id总计其下的模板/栏目数量
     *      <p/>
     * @param parColumnId:
     * @return int
     */
    int countNumByParColumnId(Integer parColumnId);

    /**
     *
     * @description: 检查区域栏目
     *      <p/>
     * @param channelId:
     * @param columnId:
     * @return void
     */
    void checkColumnAndArea(Integer channelId,Integer columnId);


    String deleteColumn(Integer columnId);

}