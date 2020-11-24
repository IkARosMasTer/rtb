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


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String deleteElement(Integer elementId) {
        try{
            checkElement(elementId);
            Element element = getById(elementId);
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
        Integer templateId = elementVo.getTemplateId();
        //获取模板样式
        if (templateId!=null&&templateId>0){
            Template template = iTemplateService.getById(templateId);
            TemplateVo templateVo = new TemplateVo();
            BeanUtils.copyProperties(template,templateVo);
            elementVo.setEleTemplateVo(templateVo);
        }

        if (elementVo.getElementOrder() == null || elementVo.getElementOrder() < 1) {
            //找出原栏目的排序号
            Map<String, Object> map = this.getMap(new QueryWrapper<Element>().lambda().select(Element::getElementOrder).eq(Element::getElementId, elementVo.getElementId()));
            elementVo.setElementOrder((Integer) map.get("element_order"));
        } else {
            //更新且有排序号,做维护
            Map<String, Object> map = this.getMap(new QueryWrapper<Element>().lambda().select(Element::getElementOrder).eq(Element::getElementId, elementVo.getElementId()));
            if (map == null || map.size() < 1) {
                //说明没有查出该栏目
                throw new EngineException("该推荐位" + elementVo.getElementId() + elementVo.getElementName()+"不存在");
            }
            //作比较
            Integer order = (Integer) map.get("element_order");
            //新的比旧的大
            if (elementVo.getElementOrder() > order) {
                //调整当前后面的排序-1  后移
                int bigOrder = elementVo.getElementOrder();
                int decrFlag = elementMapper.decrElementOrder(order, bigOrder+1, templetId);
                log.info("推荐位更新维护了" + decrFlag + "条已有推荐位");
                int upd = elementMapper.updateSelfOrder(bigOrder, elementVo.getElementId());
                log.info("推荐位更新维护自身" + upd + "条推荐位");
            } else if (elementVo.getElementOrder() < order) {
                //前移
                int smallOrder = elementVo.getElementOrder() < 1 ? 1 : elementVo.getElementOrder();
                elementVo.setElementOrder(smallOrder);
                int incrFlag = elementMapper.incrElementOrder(smallOrder - 1, order, templetId);
                log.info("推荐位更新维护了" + incrFlag + "条已有推荐位");
                int upd = elementMapper.updateSelfOrder(smallOrder, elementVo.getElementId());
                log.info("推荐位更新维护自身" + upd + "条推荐位");
            }
        }
        //维护更新时所在栏目下的所有模板的顺序
//        Map<String,Object> map = getMap(new QueryWrapper<Element>().lambda().select(Element::getElementOrder).eq(Element::getElementId,element.getElementId()));
//        Integer order = (Integer) map.get("element_order");
//        if (element.getElementOrder()==null||element.getElementOrder()<1){
//            //找出原栏目的排序号
//            element.setElementOrder(order);
//        }else if (element.getElementId()!=null&&element.getElementId()>0){
//            //更新且有排序号
//            //作比较
//            //新的比旧的大
//            if (element.getElementOrder()>order){
//                //调整当前后面的排序-1  后移
////                int decrFlag = rColumnMapper.decrColumnOrder(order,templetVo.getColumnOrder()+1,columnId);
//
//                Integer num = countElementNumByTempletId(templetId);
//                int bigOrder = element.getElementOrder()>num?num:element.getElementOrder();
//                int decrFlag = elementMapper.updateOrder(bigOrder,order,templetId);
//                int upd = elementMapper.updateSelfOrder(bigOrder,element.getElementId());
//            }else if (element.getElementOrder()<order){
//                //前移
////                int incrFlag = rColumnMapper.incrColumnOrder(templetVo.getColumnOrder()-1,order,columnId);
//
//                int smallOrder = element.getElementOrder()<1?1:element.getElementOrder();
//                int incrFlag = elementMapper.updateOrder(smallOrder,order,templetId);
//                int upd = elementMapper.updateSelfOrder(smallOrder,element.getElementId());
//            }
//        }else {
//            throw new EngineException("模板id参数错误");
//        }


        //更新模板
        //复制然后插入
        BeanUtils.copyProperties(elementVo,element);
        if (updateById(element)){
            elementVo.setColumnId(element.getColumnId());
        }else {
            //更新失败
            log.error("更新推荐位==========>更新失败:模板id{}",templateId);
            throw new EngineException("更新失败:模板id"+templateId);
        }
        return "更新推荐位"+templetId+"成功!";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Integer> updateElementAll(List<ElementVo> elementVos, Integer templetId) {
        if (elementVos==null||elementVos.size()<1){
            log.warn("updateElementAll=========>参数为空");
            throw new EngineException(PARAM_IS_INVALID_OR_BLANK);
        }
        //先删除旧推荐位
//        deleteElementAll(templetId);
        AtomicInteger i = new AtomicInteger();
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
            //维护推荐位顺序
            Map<String,Object> map = getMap(new QueryWrapper<Element>().lambda().select(Element::getElementOrder).eq(Element::getElementId,element.getElementId()));
            Integer order = (Integer) map.get("element_order");
            if (element.getElementOrder()==null||element.getElementOrder()<1){
                //如果是更新
                if (element.getElementId()!=null&&element.getElementId()>0){
                    //找出原栏目的排序号
                    element.setElementOrder(order);
                }else {
                    //如果是新增,排序号=总数+1
                    i.getAndIncrement();
                    element.setElementOrder(countElementNumByTempletId(templetId)+i.intValue());
                }
            }else if (element.getElementId()!=null&&element.getElementId()>0){
                //更新且有排序号
                //作比较
                //新的比旧的大
                if (element.getElementOrder()>order){
                    //直接将当前与库中占有的模板进行替换
                    Integer num = countElementNumByTempletId(templetId);
                    int bigOrder = element.getElementOrder()>num?num:element.getElementOrder();
                    int ee = elementMapper.updateOrder(bigOrder,order,templetId);
                    int ff = elementMapper.updateSelfOrder(bigOrder,element.getElementId());
                    //调整当前后面的排序-1  后移
//                            templetVo.setColumnOrder(bigOrder);
//                            int decrFlag = rColumnMapper.decrColumnOrder(order,bigOrder+1,columnId);
                }else if (element.getElementOrder()<order){
                    int smallOrder = element.getElementOrder()<1?1:element.getElementOrder();
                    int ee = elementMapper.updateOrder(smallOrder,order,templetId);
                    int ff = elementMapper.updateSelfOrder(smallOrder,element.getElementId());
                    //前移
//                            int incrFlag = rColumnMapper.incrColumnOrder(smallOrder-1,order,columnId);
                }
            }else {
                throw new EngineException("模板id参数错误");
            }

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
                    //维护更新时所在栏目下的所有模板的顺序
                    Map<String,Object> map = getMap(new QueryWrapper<Element>().lambda().select(Element::getElementOrder).eq(Element::getColumnId,element.getColumnId()));
                    Integer order = (Integer) map.get("element_order");
                    if (element.getElementOrder()==null||element.getElementOrder()<1){
                        //如果是更新
                        if (element.getElementId()!=null&&element.getElementId()>0){
                            //找出原栏目的排序号
                            element.setElementOrder(order);
                        }else {
                            //如果是新增,排序号=总数+1
                            j.getAndIncrement();
                            element.setElementOrder(countElementNumByTempletId(element.getColumnId())+j.intValue());
                        }
                    }else if (element.getElementId()!=null&&element.getElementId()>0){
                        //更新且有排序号
                        //作比较
                        //新的比旧的大
                        if (element.getElementOrder()>order){
                            //调整当前后面的排序-1  后移
                            Integer num = countElementNumByTempletId(element.getColumnId());
                            int bigOrder = element.getElementOrder()>num?num:element.getElementOrder();
                            int ee = elementMapper.updateOrder(bigOrder,order,element.getColumnId());
                            int ff = elementMapper.updateSelfOrder(bigOrder,element.getElementId());
                            //调整当前后面的排序-1  后移
//                            templetVo.setColumnOrder(bigOrder);
//                            int decrFlag = rColumnMapper.decrColumnOrder(order,bigOrder+1,columnId);
                        }else if (element.getElementOrder()<order){
                            int smallOrder = element.getElementOrder()<1?1:element.getElementOrder();
                            int ee = elementMapper.updateOrder(smallOrder,order,element.getColumnId());
                            int ff = elementMapper.updateSelfOrder(smallOrder,element.getElementId());
//                            int incrFlag = elementMapper.incrElementOrder(element.getElementOrder()-1,order,element.getColumnId());
                        }
                    }else {
                        throw new EngineException("模板id参数错误");
                    }
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


    @Override
    public Integer getMaxElementOrder(Integer templetId) {
        return elementMapper.getMaxElementOrder(templetId);
    }
}