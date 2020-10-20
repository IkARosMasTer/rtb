/**
 * @filename:TemplateDao 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.yanhua.rtb.entity.Template;

/**   
 * @description 模板表数据访问层
 *
 * @version V1.0
 * @author Emiya
 * 
 */
@Mapper
public interface TemplateMapper extends BaseMapper<Template> {
	
}
