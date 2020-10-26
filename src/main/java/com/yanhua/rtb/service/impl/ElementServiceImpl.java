/**
 * @filename:ElementServiceImpl 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2018 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanhua.rtb.common.EngineException;
import com.yanhua.rtb.entity.*;
import com.yanhua.rtb.mapper.ElementMapper;
import com.yanhua.rtb.service.IContentSpxlDetailService;
import com.yanhua.rtb.service.IContentSpxlService;
import com.yanhua.rtb.service.IElementService;
import com.yanhua.rtb.service.ITemplateService;
import com.yanhua.rtb.vo.ContentSpxlDetailVo;
import com.yanhua.rtb.vo.ContentSpxlVo;
import com.yanhua.rtb.vo.ElementVo;
import com.yanhua.rtb.vo.TemplateVo;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.yanhua.rtb.common.ResultCodeEnum.*;

/**   
 * @description 元素表服务实现
 * @version V1.0
 * @author Emiya
 * 
 */
@Slf4j
@Service
public class ElementServiceImpl extends ServiceImpl<ElementMapper, Element> implements IElementService  {

    @Autowired
    private ITemplateService iTemplateService;
    @Autowired
    private IContentSpxlService iContentSpxlService;
    @Autowired
    private IContentSpxlDetailService iContentSpxlDetailService;
    @Autowired
    private ElementMapper elementMapper;

    @Override
    public int countElementNumByTempletId(Integer templetId) {
        return count(new QueryWrapper<Element>().lambda().eq(Element::getColumnId,templetId));
    }

    @Override
    public ElementVo saveElement(ElementVo elementVo) {
        Integer templetId = elementVo.getColumnId();
        //判断是否存在content_id
        String contentSpxlId = elementVo.getContentSpxlId();
        ContentSpxl contentSpxl = iContentSpxlService.getOne(new QueryWrapper<ContentSpxl>().lambda().eq(ContentSpxl::getContentId,contentSpxlId).last("LIMIT 1"));
        if (contentSpxl==null){
            //无对应的彩铃内容
            log.warn("saveElement=========>无对应的彩铃内容:模板id{}",templetId);
            throw new EngineException("模板"+templetId+"无对应的彩铃内容");
        }
        //todo:
//        elementVo.setElementName(contentSpxl.getContentName());
        elementVo.setElementOrder(countElementNumByTempletId(templetId)+1);
        Element element = new Element();
        BeanUtils.copyProperties(elementVo,element);
        Integer templateId = elementVo.getTemplateId();
        //获取模板样式
        if (templateId!=null&&templateId>0){
            Template template = iTemplateService.getById(templateId);
            TemplateVo templateVo = new TemplateVo();
            BeanUtils.copyProperties(template,templateVo);
            elementVo.setEleTemplateVo(templateVo);
        }
        //插入模板
        if (save(element)){
            elementVo.setElementId(element.getElementId());
        }else {
            //插入失败。未生成推荐位id
            log.error("新增模板==========>插入失败,未生成推荐位id:模板id{}",templateId);
            throw new EngineException("插入失败,未生推荐位板id:模板id"+templateId);
        }
        return elementVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String deleteElement(Integer elementId) {
        try{
            checkElement(elementId);
            int num = elementMapper.deleteById(elementId);
            return "删除推荐位"+num+"个成功！";
        }catch (Exception e) {
            log.error("deleteElement==========>删除推荐位{}失败:{}", elementId, e.getMessage());
            throw new EngineException("删除推荐位" + elementId + "失败:" + e.getMessage());
        }
    }

    @Override
    public void checkElement(Integer elementId) {
        if (elementId==null) {
            throw new EngineException(PARAM_NOT_COMPLETE);
        }
        if (elementId<0){
            throw new EngineException(PARAM_IS_INVALID);
        }
        if (getById(elementId)==null){
            throw new EngineException("该推荐位不存在!");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String deleteElementAll(Integer templetId) {
        //删除模板下的所有推荐位
        List<Element> elementList = list(new QueryWrapper<Element>().lambda().eq(Element::getColumnId,templetId));
        List<Integer> elementIds = elementList.stream().filter(Objects::nonNull).map(Element::getElementId).collect(Collectors.toList());
        int num = 0;
        if (elementIds.size() > 0){
            num =  elementMapper.deleteBatchIds(elementIds);
        }
        return "成功删除"+num+"条推荐位";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateElement(ElementVo elementVo) {
        Integer templetId = elementVo.getColumnId();
        Element element = new Element();

//        elementVo.setElementOrder(countElementNumByTempletId(templetId)+1);
        BeanUtils.copyProperties(elementVo,element);
        Integer templateId = elementVo.getTemplateId();
        //获取模板样式
        if (templateId!=null&&templateId>0){
            Template template = iTemplateService.getById(templateId);
            TemplateVo templateVo = new TemplateVo();
            BeanUtils.copyProperties(template,templateVo);
            elementVo.setEleTemplateVo(templateVo);
        }
        //更新模板
        if (updateById(element)){
            elementVo.setColumnId(element.getColumnId());
        }else {
            //更新失败
            log.error("更新推荐位==========>更新失败:模板id{}",templateId);
            throw new EngineException("更新失败:模板id"+templateId);
        }
        return "更新推荐位"+templateId+"成功!";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Integer> updateElementAll(List<ElementVo> elementVos, Integer templetId) {
        if (elementVos==null||elementVos.size()<1){
            log.warn("updateElementAll=========>参数为空");
            throw new EngineException(PARAM_IS_INVALID_OR_BLANK);
        }
        //先删除旧推荐位
        deleteElementAll(templetId);
        List<Element> elements = elementVos.stream().filter(Objects::nonNull).map(elementVo -> {
            elementVo.setColumnId(templetId);
            if (elementVo.getCreateTime()==null){
                elementVo.setCreateTime(new Date());
            }
            if (elementVo.getUpdateTime()==null){
                elementVo.setUpdateTime(new Date());
            }
            Element element = new Element();
            BeanUtils.copyProperties(elementVo,element);
            return element;
        }).collect(Collectors.toList());
        if (saveOrUpdateBatch(elements)){
            return elements.stream().filter(element -> element.getElementId()!=null).map(Element::getElementId).collect(Collectors.toList());
//            String ret = StringUtils.join(elements.stream().filter(element -> element.getElementId()!=null).map(Element::getElementId).collect(Collectors.toList()),",");
        }else {
            throw new EngineException("更新推荐位数组失败");
        }
    }

    @Override
    public Map<String, Object> saveOrUpdateElements(List<Element> elements) {
        StringBuilder stringBuilder = new StringBuilder();

        //处理元素（推荐位）数组
        if (elements!=null&&elements.size()>0){
            AtomicInteger j = new AtomicInteger();
            try{
                //处理推荐位元素的模板样式 map(Element::getEleTemplateVo).filter(Objects::nonNull)
                List<Template> eleTemplates = elements.stream().filter(ele -> ele!=null&&ele.getEleTemplateVo()!=null).map(element -> {
                    Template eleTemplate = new Template();
                    if (element.getEleTemplateVo().getCreateTime()==null){
                        element.getEleTemplateVo().setCreateTime(new Date());
                    }
                    element.getEleTemplateVo().setUpdateTime(new Date());
                    BeanUtils.copyProperties(element.getEleTemplateVo(),eleTemplate);
                    element.setEleTemplate(eleTemplate);
                    return eleTemplate;
                }).collect(Collectors.toList());
                boolean eleTemplateRes = iTemplateService.saveOrUpdateBatch(eleTemplates);
                stringBuilder.append(";推荐位的模板样式处理").append(eleTemplateRes);
                //处理推荐位（元素）
                elements.stream().filter(element -> element!=null&&StringUtils.isNotBlank(element.getContentSpxlId())).forEach(element -> {
                    if (element.getCreateTime()==null){
                        element.setCreateTime(new Date());
                    }
                    element.setUpdateTime(new Date());
                    element.setTemplateId(element.getEleTemplate().getTemplateId());
                    j.getAndIncrement();
                    element.setElementOrder(countElementNumByTempletId(element.getColumnId())+j.intValue());
                    if (StringUtils.isEmpty(element.getElementName())){
                        ContentSpxl contentSpxl = iContentSpxlService.getOne(new QueryWrapper<ContentSpxl>().lambda().eq(ContentSpxl::getContentId,element.getContentSpxlId()).last("LIMIT 1"));
                        if (contentSpxl!=null){
                            element.setElementName(contentSpxl.getContentName());
                        }
                    }
                });
                boolean elementRes = saveOrUpdateBatch(elements);
                stringBuilder.append(";推荐位处理").append(elementRes);
                Map<String,Object> map = new HashMap<>();
                map.put("ret",stringBuilder.toString());
                map.put("elements",elements);
                return map;
            }catch (Exception e){
                e.printStackTrace();
                throw new EngineException("批量推荐位新增/更新失败"+e.getMessage());
            }
        }
       return null;
    }


    @Override
    public ElementVo getElementAll(Integer elementId) {
        ElementVo elementVo = new ElementVo();
        Element element = getById(elementId);
        if (element!=null){
            BeanUtils.copyProperties(element,elementVo);
            String contentSpxlId = element.getContentSpxlId();
            ContentSpxl contentSpxl = iContentSpxlService.getOne(new QueryWrapper<ContentSpxl>().lambda().eq(ContentSpxl::getContentId,contentSpxlId).last("LIMIT 1"));
            if (contentSpxl!=null){
                ContentSpxlVo contentSpxlVo = new ContentSpxlVo();
                BeanUtils.copyProperties(contentSpxl,contentSpxlVo);
                List<ContentSpxlDetail> contentSpxlDetails = iContentSpxlDetailService.list(new QueryWrapper<ContentSpxlDetail>().lambda().eq(ContentSpxlDetail::getContentId,contentSpxlId));
                List<ContentSpxlDetailVo> contentSpxlDetailVos = contentSpxlDetails.stream().filter(Objects::nonNull).map(contentSpxlDetail -> {
                    ContentSpxlDetailVo contentSpxlDetailVo = new ContentSpxlDetailVo();
                    BeanUtils.copyProperties(contentSpxlDetail,contentSpxlDetailVo);
                    return contentSpxlDetailVo;
                }).collect(Collectors.toList());
                contentSpxlVo.setFileList(contentSpxlDetailVos);
                elementVo.setContentSpxlVo(contentSpxlVo);
            }
        }
        return elementVo;

    }

    @Override
    public List<ElementVo> getElementAlls(Integer templetId) {
        return elementMapper.getElementResult(templetId);
    }
}