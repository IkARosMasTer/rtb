/**
 * @filename:ITemplateService 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.service;

import com.yanhua.rtb.entity.Template;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhua.rtb.vo.TemplateVo;

import java.util.Map;

/**
 * @description 模板表服务层
 * @version  V1.0
 * @author  Emiya
 * 
 */
public interface ITemplateService extends IService<Template> {

    TemplateVo getTemplateVo(Integer templateId);


}