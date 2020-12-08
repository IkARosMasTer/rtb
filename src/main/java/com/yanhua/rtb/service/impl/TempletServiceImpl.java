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
import com.yanhua.rtb.entity.ContentSpxl;
import com.yanhua.rtb.entity.Element;
import com.yanhua.rtb.entity.RColumn;
import com.yanhua.rtb.entity.Template;
import com.yanhua.rtb.mapper.ElementMapper;
import com.yanhua.rtb.mapper.RColumnMapper;
import com.yanhua.rtb.service.*;
import com.yanhua.rtb.vo.TemplateVo;
import com.yanhua.rtb.vo.TempletVo;
import jdk.nashorn.internal.ir.LiteralNode;
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
    @Autowired
    private IContentSpxlService iContentSpxlService;

    @Override
    public TempletVo saveTemplet(TempletVo templetVo) {
        Integer areaId = templetVo.getChannelId();
        Integer columnId = templetVo.getParColumnId();
        RColumn rColumn = new RColumn();
        //todo:目前写死是二级
        templetVo.setLevel("2");
        //维护该模板所在栏目的模板顺序
        Integer num = rColumnMapper.getMaxColumnOrder(areaId,columnId);
        num = num==null?0:num;
        if (templetVo.getColumnOrder() == null || templetVo.getColumnOrder() < 1 ) {
            //如果是新增且没有带序号或者带了序号并且大于现有总数,排序号=最大序号数+1
            templetVo.setColumnOrder(num + 1);
        } else if (templetVo.getColumnOrder() < num) {
            //如果带了序号并且小于现有总数，则比她大的需要后移一位
            int incrFlag = rColumnMapper.incrColumnOrder(areaId,templetVo.getColumnOrder() - 1, num+1, -1);
            log.info("模板新增需要维护了" + incrFlag + "条已有模板");
        }
        //如果带了序号并且大于现有最大序号数，则不用处理
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
            log.error("新增模板==========>插入失败,未生成模板id:渠道id{},栏目id{}",areaId,columnId);
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
            RColumn templet = rColumnMapper.selectById(templetId);
            String delNum = iElementService.deleteElementAll(templetId);
            int temDelNum = rColumnMapper.deleteById(templetId);
//            if (temDelNum>0){
//                //维护模板的顺序
//                int delOrder = rColumnMapper.delColumnOrder(templet.getChannelId(),templet.getColumnOrder(),templet.getParColumnId());
//                log.info("删除模板:"+templet+"需要维护"+delOrder+"条模板");
//            }
            return "删除模板"+temDelNum+"个以及其下"+delNum+"个推荐位";
        }catch (Exception e){
            log.error("deleteTemplet==========>删除模板{}失败:{}",templetId,e.getMessage());
            throw new EngineException("删除模板"+templetId+"失败:"+e.getMessage());
        }
    }

    @Override
    public TempletVo checkTemplet(Integer templetId) {
        if (templetId==null) {
            throw new EngineException(PARAM_NOT_COMPLETE);
        }
        if (templetId<0){
            throw new EngineException(PARAM_IS_INVALID);
        }
        RColumn rColumn = irColumnService.getById(templetId);
        TempletVo templetVo = new TempletVo();
        BeanUtils.copyProperties(rColumn,templetVo);
        if (rColumn==null||!"2".equals(rColumn.getLevel())){
            throw new EngineException("该模板不存在!");
        }
        return templetVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateTemplet(TempletVo templetVo) {
        Integer columnId = templetVo.getParColumnId();
        RColumn rColumn = new RColumn();
        //todo:目前写死是二级
        templetVo.setLevel("2");
        //维护更新时所在栏目下的所有模板的顺序
        Map<String,Object> map = irColumnService.getMap(new QueryWrapper<RColumn>().lambda().select(RColumn::getColumnOrder).eq(RColumn::getColumnId,templetVo.getColumnId()).eq(RColumn::getChannelId,templetVo.getChannelId()));
        Integer order = (Integer) map.get("column_order");
        if (templetVo.getColumnId()!=null&&templetVo.getColumnId()>0) {
            if (templetVo.getColumnOrder()==null||templetVo.getColumnOrder()<1){
                //找出原栏目的排序号
                templetVo.setColumnOrder(order);
            }else {
                //更新且有排序号
                //作比较
                //新的比旧的大
                if (templetVo.getColumnOrder()>order){
                    //调整当前后面的排序-1  后移
//                    Integer num = irColumnService.countNumByParColumnId(columnId,templetVo.getChannelId());
//                    int bigOrder = templetVo.getColumnOrder()>num?num:templetVo.getColumnOrder();
                    int bigOrder = templetVo.getColumnOrder();
                    int decrFlag = rColumnMapper.decrColumnOrder(templetVo.getChannelId(),order,bigOrder+1,columnId);
                    log.info("模板更新维护了" + decrFlag + "条已有模板");
                    int upd = rColumnMapper.updateSelfOrder(bigOrder,templetVo.getColumnId());
                    log.info("模板更新维护自身" + upd + "条模板");
                }else if (templetVo.getColumnOrder()<order){
                    //前移
//                int incrFlag = rColumnMapper.incrColumnOrder(templetVo.getColumnOrder()-1,order,columnId);
                    int smallOrder = templetVo.getColumnOrder()<1?1:templetVo.getColumnOrder();
                    int incrFlag = rColumnMapper.incrColumnOrder(templetVo.getChannelId(),smallOrder-1,order,columnId);
                    log.info("模板更新维护了" + incrFlag + "条已有模板");
                    int upd = rColumnMapper.updateSelfOrder(smallOrder,templetVo.getColumnId());
                    log.info("模板更新维护自身" + upd + "条模板");
                }
            }
        }else {
            throw new EngineException("模板id参数错误");
        }
//        templetVo.setColumnOrder(irColumnService.countNumByParColumnId(columnId,templetVo.getChannelId())+1);
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
            log.error("更新模板==========>更新失败:渠道id{},栏目id{}",templetVo.getChannelId(),columnId);
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
                    //维护更新时所在栏目下的所有模板的顺序
                    Map<String,Object> map = irColumnService.getMap(new QueryWrapper<RColumn>().lambda().select(RColumn::getColumnOrder).eq(RColumn::getColumnId,templetVo.getColumnId()));
                    Integer order = (Integer) map.get("column_order");
                    if (templetVo.getColumnOrder()==null||templetVo.getColumnOrder()<1){
                        //如果是更新
                        if (templetVo.getColumnId()!=null&&templetVo.getColumnId()>0){
                            //找出原栏目的排序号
                            templetVo.setColumnOrder(order);
                        }else {
                            //如果是新增,排序号=总数+1
                            i.getAndIncrement();
                            templetVo.setColumnOrder(irColumnService.countNumByParColumnId(columnId,templetVo.getChannelId())+i.intValue());
                        }
                    }else if (templetVo.getColumnId()!=null&&templetVo.getColumnId()>0){
                        //更新且有排序号
                        //作比较
                        //新的比旧的大
                        if (templetVo.getColumnOrder()>order){
                            //直接将当前与库中占有的模板进行替换
                            Integer num = irColumnService.countNumByParColumnId(columnId,templetVo.getChannelId());
                            int bigOrder = templetVo.getColumnOrder()>num?num:templetVo.getColumnOrder();
                            int ee = rColumnMapper.updateOrder(bigOrder,order,columnId);
                            int ff = rColumnMapper.updateSelfOrder(bigOrder,templetVo.getColumnId());
                              //调整当前后面的排序-1  后移
//                            templetVo.setColumnOrder(bigOrder);
//                            int decrFlag = rColumnMapper.decrColumnOrder(order,bigOrder+1,columnId);
                        }else if (templetVo.getColumnOrder()<order){
                            int smallOrder = templetVo.getColumnOrder()<1?1:templetVo.getColumnOrder();
                            int ee = rColumnMapper.updateOrder(smallOrder,order,columnId);
                            int ff = rColumnMapper.updateSelfOrder(smallOrder,templetVo.getColumnId());
                            //前移
//                            int incrFlag = rColumnMapper.incrColumnOrder(smallOrder-1,order,columnId);
                        }
                    }else {
                        throw new EngineException("模板id参数错误");
                    }
                    //是否只要查一次count，在单线程环境下，并发环境需要分布式order数目或者分布式事务
//                    templetVo.setColumnOrder(irColumnService.countNumByParColumnId(columnId,templetVo.getChannelId()) + i.intValue());
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

    @Override
    public List<Element> getElementCopy(Integer templetId) {
        List<Element> elements = elementMapper.selectList(new QueryWrapper<Element>().lambda().eq(Element::getColumnId,templetId).orderByAsc(Element::getElementOrder));
        elements.forEach(element -> {
            String contentId = element.getContentSpxlId();
            //判断是否存在content_id
            ContentSpxl contentSpxl = iContentSpxlService.getOne(new QueryWrapper<ContentSpxl>().lambda().eq(ContentSpxl::getContentId,element.getContentSpxlId()).eq(ContentSpxl::getStatus,0).last("LIMIT 1"));
            if (contentSpxl==null){
                //无对应的彩铃内容
                log.warn("getElementCopy=========>无对应的彩铃内容或已过期:模板id{},彩铃内容id{}",templetId,contentId);
                elements.remove(element);
            }
            element.setElementId(null);
            element.setColumnId(null);
        });
        return elements;
    }
}