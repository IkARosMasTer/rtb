/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: TempletServiceImpl
 * Author: Emiya
 * Date: 2020/10/15 10:37
 * Description: 模板（二级栏目）服务实现类
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yanhua.rtb.common.EngineException;
import com.yanhua.rtb.entity.Element;
import com.yanhua.rtb.entity.RColumn;
import com.yanhua.rtb.entity.Template;
import com.yanhua.rtb.mapper.ElementMapper;
import com.yanhua.rtb.mapper.RColumnMapper;
import com.yanhua.rtb.service.IElementService;
import com.yanhua.rtb.service.IRColumnService;
import com.yanhua.rtb.service.ITemplateService;
import com.yanhua.rtb.service.ITempletService;
import com.yanhua.rtb.vo.TemplateVo;
import com.yanhua.rtb.vo.TempletVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.appender.db.ColumnMapping;
import org.apache.poi.ss.formula.functions.Column;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.yanhua.rtb.common.ResultCodeEnum.PARAM_IS_INVALID;
import static com.yanhua.rtb.common.ResultCodeEnum.PARAM_NOT_COMPLETE;

/**
 * 〈功能简述〉<br>
 * 〈模板（二级栏目）服务实现类〉
 *  <p>
 * @author Emiya
 * @create 2020/10/15 10:37
 * @version 1.0.0
 */
@Slf4j
@Service
public class TempletServiceImpl implements ITempletService {

    @Autowired
    private IRColumnService irColumnService;
    @Autowired
    private ITemplateService iTemplateService;
    @Autowired
    private IElementService iElementService;
    @Autowired
    private ElementMapper elementMapper;
    @Autowired
    private RColumnMapper rColumnMapper;

    @Override
    public TempletVo saveTemplet(TempletVo templetVo) {
        Integer areaId = templetVo.getAreaId();
        Integer columnId = templetVo.getParColumnId();
        RColumn rColumn = new RColumn();
        //todo:目前写死是二级
        templetVo.setLevel("2");
        templetVo.setColumnOrder(irColumnService.countNumByParColumnId(columnId)+1);
        BeanUtils.copyProperties(templetVo,rColumn);
        Integer templateId = templetVo.getTemplateId();
        //获取模板样式
        if (templateId!=null&&templateId>0){
            Template template = iTemplateService.getById(templateId);
            TemplateVo templateVo = new TemplateVo();
            BeanUtils.copyProperties(template,templateVo);
            templetVo.setTemTemplateVo(templateVo);
        }
        //插入模板
        if (irColumnService.save(rColumn)){
            templetVo.setColumnId(rColumn.getColumnId());
        }else {
            //插入失败。未生成模板id
            log.error("新增模板==========>插入失败,未生成模板id:地区id{},栏目id{}",areaId,columnId);
            throw new EngineException("插入失败,未生成模板id:栏目id"+columnId);
        }
        return templetVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String deleteTemplet(Integer templetId) {
        try{
            //检查
            checkTemplet(templetId);
            //删除模板下的所有推荐位
            String delNum = iElementService.deleteElementAll(templetId);
            int temDelNum = rColumnMapper.deleteById(templetId);
            return "删除模板"+temDelNum+"个,"+delNum;
        }catch (Exception e){
            log.error("deleteTemplet==========>删除模板{}失败:{}",templetId,e.getMessage());
            throw new EngineException("删除模板"+templetId+"失败:"+e.getMessage());
        }
    }

    @Override
    public void checkTemplet(Integer templetId) {
        if (templetId==null) {
            throw new EngineException(PARAM_NOT_COMPLETE);
        }
        if (templetId<0){
            throw new EngineException(PARAM_IS_INVALID);
        }
        RColumn rColumn = irColumnService.getById(templetId);
        if (rColumn==null||!"2".equals(rColumn.getLevel())){
            throw new EngineException("该模板不存在!");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateTemplet(TempletVo templetVo) {
        Integer columnId = templetVo.getParColumnId();
        RColumn rColumn = new RColumn();
        //todo:目前写死是二级
        templetVo.setLevel("2");
        templetVo.setColumnOrder(irColumnService.countNumByParColumnId(columnId)+1);
        BeanUtils.copyProperties(templetVo,rColumn);
        Integer templateId = templetVo.getTemplateId();
        //获取模板样式
        if (templateId!=null&&templateId>0){
            Template template = iTemplateService.getById(templateId);
            TemplateVo templateVo = new TemplateVo();
            BeanUtils.copyProperties(template,templateVo);
            templetVo.setTemTemplateVo(templateVo);
        }
        //更新模板
        if (irColumnService.updateById(rColumn)){
            templetVo.setColumnId(rColumn.getColumnId());
        }else {
            //更新失败
            log.error("更新模板==========>更新失败:地区id{},栏目id{}",templetVo.getAreaId(),columnId);
            throw new EngineException("更新失败:栏目id"+columnId);
        }
        return "更新模板"+templateId+"成功!";
    }

    @Override
    public Map<String,Object> saveOrUpdateTemplets(List<TempletVo> templetVos, Integer columnId) {
        StringBuilder stringBuilder = new StringBuilder();
        if (templetVos!=null&&templetVos.size()>0) {
            AtomicInteger i = new AtomicInteger();
            //处理模板的模板样式
            try{
                List<Template> templates = templetVos.stream().filter(s -> s != null && s.getTemTemplateVo() != null).map(templetVo -> {
                    Template template = new Template();
                    if (templetVo.getTemTemplateVo().getCreateTime() == null) {
                        templetVo.getTemTemplateVo().setCreateTime(new Date());
                    }
                    templetVo.getTemTemplateVo().setUpdateTime(new Date());
                    BeanUtils.copyProperties(templetVo.getTemTemplateVo(), template);
                    templetVo.setTemTemplate(template);
                    return template;
                }).collect(Collectors.toList());
                boolean temTemplateRes = iTemplateService.saveOrUpdateBatch(templates);
                stringBuilder.append(";模板的模板样式处理").append(temTemplateRes);
                //处理模板数组
                List<RColumn> templets = templetVos.stream().map(templetVo -> {
                    i.getAndIncrement();
                    //是否只要查一次count，在单线程环境下，并发环境需要分布式order数目或者分布式事务
                    templetVo.setColumnOrder(irColumnService.countNumByParColumnId(columnId) + i.intValue());
                    RColumn templet = new RColumn();
                    //TODO：目前模板写死只有二级
                    templetVo.setLevel("2");
                    templetVo.setParColumnId(columnId);
                    if (templetVo.getCreateTime() == null) {
                        templetVo.setCreateTime(new Date());
                    }
                    templetVo.setUpdateTime(new Date());
                    BeanUtils.copyProperties(templetVo, templet);
                    templet.setElementVoList(templetVo.getElementVoList());
                    //把模板id赋予
                    templet.setTemplateId(templetVo.getTemTemplate() == null ? null : templetVo.getTemTemplate().getTemplateId());
                    return templet;
                }).collect(Collectors.toList());
                //方法默认执行1000条
                boolean specialRes = irColumnService.saveOrUpdateBatch(templets);
                stringBuilder.append(";模板处理").append(specialRes);
                Map<String,Object> map = new HashMap<>();
                map.put("ret",stringBuilder.toString());
                map.put("templets",templets);
                return map;
            }catch (Exception e){
                e.printStackTrace();
                throw new EngineException("批量模板新增/更新失败"+e.getMessage());
            }
        }
        return null;
    }


}