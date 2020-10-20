///**
// * @filename:IColumnService 2020年10月01日
// * @project rtb  V1.0
// * Copyright(c) 2020 Emiya Co. Ltd.
// * All right reserved.
// */
//package com.yanhua.rtb.service;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.yanhua.rtb.Vo.ColumnVo;
//import com.yanhua.rtb.entity.Column;
//import com.baomidou.mybatisplus.extension.service.IService;
//import org.apache.ibatis.annotations.Param;
//
//import java.util.List;
//
///**
// * @description 栏目表服务层
// * @version V1.0
// * @author Emiya
// *
// */
//public interface IColumnService extends IService<Column> {
//
//    /**
//     *
//     * @description: 获取栏目、专题、元素所有数据
//     *      <p/>
//     * @param areaId:
//     * @param columnId:
//     * @param startPage:
//     * @param pageSize:
//     */
//    List<ColumnVo> getAllColumnVoList(Integer areaId, Integer columnId,Integer startPage,Integer pageSize);
//
//    /**
//     *
//     * @description: 获取地区栏目
//     *      <p/>
//     * @param areaId:
//     */
//    List<Column> getColumnVoList(Integer areaId);
//
//}