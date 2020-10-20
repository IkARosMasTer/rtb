/**
 * @filename:TemplateController 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yanhua.rtb.common.EngineException;
import com.yanhua.rtb.common.ResponseResult;
import com.yanhua.rtb.entity.Template;
import com.yanhua.rtb.service.ITemplateService;
import com.yanhua.rtb.vo.ColumnVo;
import com.yanhua.rtb.vo.TemplateVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static com.yanhua.rtb.common.ResultCodeEnum.PARAM_IS_INVALID;
import static com.yanhua.rtb.common.ResultCodeEnum.PARAM_IS_INVALID_OR_BLANK;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： 模板表API接口层</P>
 * @version V1.0
 * @author Emiya
 * @create 2020年10月01日
 *
 */
@Slf4j
@Api(tags = "模板样式控制器" )
@RestController
@ResponseResult
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private ITemplateService iTemplateService;

    @ApiOperation(value = "新增/更新模板样式", httpMethod = "POST", notes = "生成模板id,返回模板样式视图")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public TemplateVo saveOrUpdateTemplateVo(@RequestBody @Valid TemplateVo templateVo) {
        //todo:RequestBodyAdvice 来统一做创建时间或更新时间判断
        if (templateVo.getCreateTime()==null){
            templateVo.setCreateTime(new Date());
        }
        if (templateVo.getUpdateTime()==null){
            templateVo.setUpdateTime(new Date());
        }
        Template template = new Template();
        BeanUtils.copyProperties(templateVo,template);
        if (iTemplateService.saveOrUpdate(template,new UpdateWrapper<Template>().lambda().eq(Template::getTemplateId,template.getTemplateId()))){
            templateVo.setTemplateId(template.getTemplateId());
            return templateVo;
        }else {
            log.error("新增/更新模板样式=========>失败");
            throw new EngineException("新增/更新模板样式失败");
        }
    }

    @ApiOperation(value = "查询模板样式", httpMethod = "GET", notes = "根据模板id,返回模板样式视图")
    @RequestMapping(value = "/{templateId}", method = RequestMethod.GET)
    @ResponseBody
    public TemplateVo selectTemplateVo(@PathVariable Integer templateId) {
        return iTemplateService.getTemplateVo(templateId);
    }

    @ApiOperation(value = "删除模板样式", httpMethod = "DELETE", notes = "根据模板id,删除模板样式")
    @RequestMapping(value = "/{templateId}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteTemplate(@PathVariable Integer templateId) {
        return iTemplateService.removeById(templateId);
    }

    @ApiOperation(value = "查询所有模板样式", httpMethod = "GET", notes = "默认返回所有模板样式视图")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Template> selectTemplate() {
        return iTemplateService.list();
    }



}