/**
 * @filename:AreaDao 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanhua.rtb.entity.Area;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**   
 * @description 地区数据访问层
 *
 * @version V1.0
 * @author Emiya
 * 
 */
@Mapper
public interface AreaMapper extends BaseMapper<Area> {
    List<Area> selectAll();

//    @Update("UPDATE ")
//    boolean updateStatus(Integer status);
    @Delete("DELETE FROM area WHERE channel_id = #{id}")
    int delByAreaId(Integer id);

//    updateById()
}
