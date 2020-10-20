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
	
}
