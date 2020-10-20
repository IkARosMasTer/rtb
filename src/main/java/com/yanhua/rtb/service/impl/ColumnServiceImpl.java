///**
// * @filename:ColumnServiceImpl 2020年10月01日
// * @project rtb  V1.0
// * Copyright(c) 2018 Emiya Co. Ltd.
// * All right reserved.
// */
//package com.yanhua.rtb.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.Wrapper;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.yanhua.rtb.Vo.ColumnVo;
//import com.yanhua.rtb.Vo.SpecialVo;
//import com.yanhua.rtb.entity.Column;
//import com.yanhua.rtb.mapper.ColumnMapper;
//import com.yanhua.rtb.service.IColumnService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//
//import java.util.List;
//
///**
// * @description 栏目表服务实现
// *
// * @version V1.0
// * @author Emiya
// *
// */
//@Service
//public class ColumnServiceImpl extends ServiceImpl<ColumnMapper, Column> implements IColumnService  {
//
//    @Autowired
//    private ColumnMapper columnMapper;
//    @Override
//    public List<ColumnVo> getAllColumnVoList(Integer areaId, Integer columnId,Integer startPage,Integer pageSize ) {
//        Page<ColumnVo> page = new Page<>(startPage, pageSize);
//        return columnMapper.getByAreaIdAndColumnId(page,areaId,columnId);
////        return columnMapper.getByAreaIdAndColumnId(page,new QueryWrapper<ColumnVo>().lambda().eq(ColumnVo::getColumnId,columnId).eq(ColumnVo::getAreaId,areaId).groupBy(ColumnVo::getColumnId)).getRecords();
//    }
//
//    @Override
//    public List<Column> getColumnVoList(Integer areaId) {
//        return columnMapper.selectList(new QueryWrapper<Column>().lambda().eq(Column::getAreaId,areaId));
//    }
//
//}