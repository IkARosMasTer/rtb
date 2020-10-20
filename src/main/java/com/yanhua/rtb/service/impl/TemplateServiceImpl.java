/**
 * @filename:TemplateServiceImpl 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2018 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.service.impl;

import com.yanhua.rtb.entity.Template;
import com.yanhua.rtb.mapper.TemplateMapper;
import com.yanhua.rtb.service.ITemplateService;
import com.yanhua.rtb.vo.TemplateVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**   
 * @Description 模板表服务实现
 *
 * @version V1.0
 * @author Emiya
 * 
 */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements ITemplateService  {

    @Override
    public TemplateVo getTemplateVo(Integer templateId) {
        TemplateVo templateVo = new TemplateVo();
        if (templateId!=null&&templateId>0){
            Template template = getById(templateId);
            BeanUtils.copyProperties(template,templateVo);
        }
        return templateVo;
    }
}