/**
 * @filename:ContentSpxlDao 2020年10月13日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhua.rtb.vo.ContentSpxlVo;
import org.apache.ibatis.annotations.Mapper;
import com.yanhua.rtb.entity.ContentSpxl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**   
 * @description 联通入库表数据访问层
 *
 * @version V1.0
 * @author Emiya
 * 
 */
@Mapper
public interface ContentSpxlMapper extends BaseMapper<ContentSpxl> {


    List<ContentSpxlVo> pageVo(@Param("page") IPage<ContentSpxlVo> page,
                           @Param(Constants.WRAPPER) Wrapper<ContentSpxl> queryWrapper);


}
