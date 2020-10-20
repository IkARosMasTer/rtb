/**
 * @filename:RColumnDao 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhua.rtb.vo.ColumnVo;
import com.yanhua.rtb.vo.SpecialVo;
import org.apache.ibatis.annotations.Mapper;
import com.yanhua.rtb.entity.RColumn;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**   
 * @description 栏目表数据访问层
 *
 * @version V1.0
 * @author Emiya
 * 
 */
@Mapper
public interface RColumnMapper extends BaseMapper<RColumn> {

    //    @Select({"select \ta.*,\n" +
//            "\t\tb.column_id AS columnId1 , b.template_id AS templateId1,b.area_id AS areaId1, b.bg_photo AS bgPhoto1, b.column_name AS columnName1, b.par_column_id AS parColumnId1, b.level AS level1, b.column_order AS columnOrder1, b.subtitle AS subtitle1, b.background_img AS backgroundImg1, b.create_time AS createTime1, b.update_time AS updateTime1  from `column` a left join `column` b on b.par_column_id = a.column_id where a.area_id =#{areaId} and a.column_id = #{columnId} GROUP BY b.column_id"})
//            @ResultType(value = com.yanhua.rtb.Vo.ColumnVo.class)
//    @Results(id = "columnVo" ,value = {
//            @Result(column = "column_id",property = "columnId" ,id = true),
//            @Result(column = "template_id",property = "templateId" ),
//            @Result(column = "area_id",property = "areaId" ),
//            @Result(column = "bg_photo",property = "bgPhoto"),
//            @Result(column = "column_name",property = "columnName" ),
//            @Result(column = "par_column_id",property = "parColumnId" ),
//            @Result(column = "level",property = "level"),
//            @Result(column = "column_order",property = "columnOrder" ),
//            @Result(column = "subtitle",property = "subtitle"),
//            @Result(column = "background_img",property = "backgroundImg" ),
//            @Result(column = "create_time",property = "createTime" ),
//            @Result(column = "update_time",property = "updateTime" ),
//            @Result(column = "pagination",property = "pagination" ,),
////            @Result(property="specialList", javaType= List.class,column="column_id",
////                    many=@Many(select="com.yanhua.rtb.mapper.ColumnMapper.getByParColumnId2"))
//            @Result(property="specialList", javaType= List.class,column="{pagination.current = current,pagination.size = size,column_id}",
//                    many=@Many(select="com.yanhua.rtb.mapper.ColumnMapper.getByParColumnId2"))
//    })
//    @ResultMap({
//            @Result(column = ),
//            @Result( of)})
    List<ColumnVo> getByAreaIdAndColumnId(Page<ColumnVo> pagination, @Param("areaId") Integer areaId, @Param("columnId") Integer columnId);
//    IPage<ColumnVo> getByAreaIdAndColumnId(Page<Column> pagination,@Param(Constants.WRAPPER) Wrapper<ColumnVo> wrapper);
//);

    @Select("select * from `column` a where a.par_column_id = #{columnId}")
    IPage<SpecialVo> getByParColumnId(@Param("columnId")Integer columnId);

//    @Select("select * from `column` a where a.par_column_id = #{columnId}")
//    IPage<Column> getByParColumnId2(IPage<Column>() pagination, @Param("columnId") Integer columnId);

//    List<ElementVo> getElementResult(@Param("columnId1") Integer columnId1);
}
