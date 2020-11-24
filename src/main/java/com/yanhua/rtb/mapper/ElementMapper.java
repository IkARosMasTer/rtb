/**
 * @filename:ElementDao 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanhua.rtb.vo.ElementVo;
import org.apache.ibatis.annotations.Mapper;
import com.yanhua.rtb.entity.Element;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**   
 * @description 元素表数据访问层
 *
 * @version V1.0
 * @author Emiya
 * 
 */
@Mapper
public interface ElementMapper extends BaseMapper<Element> {


    List<ElementVo> getElementResult(@Param("columnId1") Integer columnId1);

    @Update("update `element` a set a.column_order = column_order-1 where a.par_column_id = #{parColumnId} and a.column_order > #{small} ")
    int delElementOrder(@Param("small")Integer small,@Param("parColumnId") Integer parColumnId);


    @Update("update `element` a set a.element_order = element_order-1 where a.column_id = #{templetId} and a.element_order > #{small} and a.element_order < #{big}")
    int decrElementOrder(@Param("small")Integer small,@Param("big") Integer big,@Param("templetId") Integer templetId);
    @Update("update `element` a set a.element_order = element_order+1 where a.column_id = #{templetId} and a.element_order > #{small} and a.element_order < #{big}")
    int incrElementOrder(@Param("small")Integer small,@Param("big") Integer big,@Param("templetId") Integer templetId);

//
//    @Update("update `r_column` a set a.column_order = column_order-1 where a.channel_id = #{channelId} and a.par_column_id = #{parColumnId} and a.column_order > #{small} and a.column_order < #{big}")
//    int decrColumnOrder(@Param("channelId") Integer channelId,@Param("small")Integer small,@Param("big") Integer big,@Param("parColumnId") Integer parColumnId);
//    @Update("update `r_column` a set a.column_order = column_order+1 where a.channel_id = #{channelId} and a.par_column_id = #{parColumnId} and a.column_order > #{small} and a.column_order < #{big}")
//    int incrColumnOrder(@Param("channelId") Integer channelId,@Param("small")Integer small,@Param("big") Integer big,@Param("parColumnId") Integer parColumnId);


    @Update("update element a set a.element_order = #{now} where a.column_id = #{templetId} and a.element_order = #{old}")
    int updateOrder(@Param("old") Integer old,@Param("now") Integer now,@Param("templetId") Integer templetId);

    @Update("update element a set a.element_order = #{now} where a.element_id = #{elementId}")
    int updateSelfOrder(@Param("now") Integer now,@Param("elementId") Integer elementId);


    @Select("select MAX(a.element_order) AS element_order FROM `element` a WHERE a.column_id = #{templetId} ")
    int getMaxElementOrder(@Param("templetId") Integer templetId);

}
