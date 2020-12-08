/**
 * @filename:RColumnServiceImpl 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2018 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhua.rtb.mapper.ElementMapper;
import com.yanhua.rtb.service.*;
import com.yanhua.rtb.vo.*;
import com.yanhua.rtb.common.EngineException;
import com.yanhua.rtb.entity.Element;
import com.yanhua.rtb.entity.RColumn;
import com.yanhua.rtb.entity.Template;
import com.yanhua.rtb.mapper.AreaMapper;
import com.yanhua.rtb.mapper.RColumnMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.yanhua.rtb.common.ResultCodeEnum.PARAM_IS_INVALID;
import static com.yanhua.rtb.common.ResultCodeEnum.PARAM_NOT_COMPLETE;

/**   
 * @description 栏目表服务实现
 *
 * @version V1.0
 * @author Emiya
 * 
 */
@Service
@Slf4j
public class RColumnServiceImpl extends ServiceImpl<RColumnMapper, RColumn> implements IRColumnService  {

    @Autowired
    private RColumnMapper columnMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private ElementMapper elementMapper;
    @Autowired
    private ITemplateService iTemplateService;
    @Autowired
    private IElementService iElementService;
    @Autowired
    private IAreaService iAreaService;
    @Autowired
    private ITempletService iTempletService;

    @Override
    public List<ColumnVo> getAllColumnVoList(Integer channelId, Integer columnId, Integer startPage, Integer pageSize ) {
        Page<ColumnVo> page = new Page<>(startPage, pageSize);
        return columnMapper.getByAreaIdAndColumnId(page,channelId,columnId);
//        return columnMapper.getByAreaIdAndColumnId(page,new QueryWrapper<ColumnVo>().lambda().eq(ColumnVo::getColumnId,columnId).eq(ColumnVo::getChannelId,areaId).groupBy(ColumnVo::getColumnId)).getRecords();
    }

    @Override
    public List<ColumnVo> getColumnVoList(Integer channelId) {
        List<RColumn> columns = columnMapper.selectList(new QueryWrapper<RColumn>().lambda().eq(RColumn::getChannelId, channelId).eq(RColumn::getLevel,"1").orderByDesc(RColumn::getColumnOrder).orderByDesc(RColumn::getUpdateTime));
        return columns.stream().filter(Objects::nonNull).map(rColumn -> {
            ColumnVo columnVo = new ColumnVo();
            BeanUtils.copyProperties(rColumn, columnVo);
            return columnVo;
        }).collect(Collectors.toList());
    }


    @SuppressWarnings("unchecked")
    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public String saveOrUpdateColumnVo(ColumnVo columnVo) {
        StringBuilder stringBuilder = new StringBuilder().append("处理结果:");
        RColumn rColumn = new RColumn();
        Integer templateId =null;
        Integer rColumnId;
        try{
            if (columnVo.getColTemplateVo()!=null){
                //处理栏目模板样式
                TemplateVo templateVo = columnVo.getColTemplateVo();
                if (templateVo.getCreateTime()==null){
                    templateVo.setCreateTime(new Date());
                }
                templateVo.setUpdateTime(new Date());
                Template template = new Template();
                BeanUtils.copyProperties(templateVo,template);
                boolean templateRes = iTemplateService.saveOrUpdate(template);
                stringBuilder.append(";栏目模板样式处理").append(templateRes);
                log.info("yyyyyyyyyyyyyyyyyyyyyyy=id:"+template.getTemplateId());
                templateId = template.getTemplateId();
            }
            if (templateId==null||templateId<0){
                throw new EngineException("栏目模板插入失败");
            }
            //处理栏目
            if (columnVo.getCreateTime()==null){
                columnVo.setCreateTime(new Date());
            }
            columnVo.setUpdateTime(new Date());
            columnVo.setTemplateId(templateId);
            //TODO：目前栏目写死只有一级
            columnVo.setLevel("1");
            columnVo.setParColumnId(-1);

            BeanUtils.copyProperties(columnVo,rColumn);
            if (rColumn.getColumnOrder()==null||rColumn.getColumnOrder()<0){
                //如果是更新
                if (rColumn.getColumnId()!=null&&rColumn.getColumnId()>0){
                    //找出原栏目的排序号
                    Map<String,Object> map = getMap(new QueryWrapper<RColumn>().lambda().select(RColumn::getColumnOrder).eq(RColumn::getColumnId,rColumn.getColumnId()));
                    rColumn.setColumnOrder((Integer) map.get("column_order"));
                }else {
                    //如果是新增,排序号=总数+1
                    rColumn.setColumnOrder(countNumByParColumnId(-1,rColumn.getChannelId())+1);
                }
            }else if (rColumn.getColumnId()!=null&&rColumn.getColumnId()>0){
                //更新且有排序号
                Map<String,Object> map = getMap(new QueryWrapper<RColumn>().lambda().select(RColumn::getColumnOrder).eq(RColumn::getColumnId,rColumn.getColumnId()));
                //作比较
                Integer order = (Integer) map.get("column_order");
                //新的比旧的大
                if (rColumn.getColumnOrder()>order){
                    //调整当前后面的排序-1
//                    int ff = columnMapper.decrColumnOrder(order,columnVo.getColumnOrder()+1,-1);
                    Integer num = countNumByParColumnId(-1,rColumn.getChannelId());
                    int bigOrder = rColumn.getColumnOrder()>num?num:rColumn.getColumnOrder();
                    int decrFlag = columnMapper.updateOrder(bigOrder,order,-1);
                    int upd = columnMapper.updateSelfOrder(bigOrder,rColumn.getColumnId());

//                    if (ff){
//                        update()
//                    }
//                    update(columnVo,new UpdateWrapper<RColumn>().lambda().set(RColumn::getColumnOrder,columnVo.getColumnOrder()).gt())
                }else if (rColumn.getColumnOrder()<order){
                    int smallOrder = rColumn.getColumnOrder()<1?1:rColumn.getColumnOrder();
                    int incrFlag = columnMapper.updateOrder(smallOrder,order,-1);
                    int upd = columnMapper.updateSelfOrder(smallOrder,rColumn.getColumnId());
//                    int gg = columnMapper.incrColumnOrder(columnVo.getColumnOrder()-1,order,-1);
                }
            }
            boolean columnRes = saveOrUpdate(rColumn);
            stringBuilder.append(";栏目处理").append(columnRes);
            log.info("xxxxxxxxxxxxxxxxxxxxxxxxx=id:"+rColumn.getColumnId());
            rColumnId = rColumn.getColumnId();
            if (rColumnId==null||rColumnId<0){
                throw new EngineException("栏目插入失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new EngineException("新增/更新栏目失败"+e.getMessage());
        }
        //处理模板的模板样式
        List<TempletVo> templetVos = columnVo.getTempletVoList();
        if (templetVos!=null&&templetVos.size()>0){
            Map<String,Object> map = iTempletService.saveOrUpdateTemplets(templetVos,rColumnId);
            if (map!=null){
                stringBuilder.append(map.get("ret"));
                List<RColumn> templets = (List<RColumn>) map.get("templets");
                //有模板数组，才能有推荐位数组
                //处理元素（推荐位）数组
                List<ElementVo> elementVos = templets.stream().filter(s -> s.getElementVoList()!=null&&s.getElementVoList().size()>0).peek(templet ->{
                    Integer templetId = templet.getColumnId();
                    //赋予推荐位的模板样式id
                    templet.getElementVoList().forEach(elementVo -> elementVo.setColumnId(templetId));
                }).flatMap(
                        templet -> templet.getElementVoList().stream()).collect(Collectors.toList());
                List<Element> elements = elementVos.stream().filter(Objects::nonNull).map(elementVo -> {
                    Element element = new Element();
                    if (elementVo.getCreateTime()==null){
                        elementVo.setCreateTime(new Date());
                    }
                    BeanUtils.copyProperties(elementVo,element);
                    element.setEleTemplateVo(elementVo.getEleTemplateVo());
                    return element;
                }).collect(Collectors.toList());
                Map<String,Object> eleMap = iElementService.saveOrUpdateElements(elements);
                if (eleMap!=null){
                    stringBuilder.append(eleMap.get("ret"));
                }
            }
            //处理推荐位的元素数组(另一种方法)
//            specialVos.stream().filter(Objects::nonNull).forEach(specialVo -> {
//                //推荐位
//                RColumn rColumn = new RColumn();
//                BeanUtils.copyProperties(specialVo,rColumn);
//                saveOrUpdate(rColumn);
//                //推荐位模板
//                Template speTemplate  = new Template();
//                if (specialVo.getSpeTemplateVo()!=null){
//                    BeanUtils.copyProperties(specialVo.getSpeTemplateVo(),speTemplate);
//                    iTemplateService.saveOrUpdate(speTemplate);
//                }
//                //元素
//                List<ElementVo> elementVos = specialVo.getElementList();
//                if (elementVos!=null&&elementVos.size()>0){
//                    List<Element> elements = elementVos.stream().filter(Objects::nonNull).map(elementVo -> {
//                        Element element = new Element();
//                        BeanUtils.copyProperties(elementVo,element);
//                        return element;
//                    }).collect(Collectors.toList());
//                    boolean elementRes = iElementService.saveOrUpdateBatch(elements);
//                    stringBuilder.append(";推荐位元素处理").append(elementRes);
//                }
//                //元素模板
//            });
        }
        return stringBuilder.toString();
    }

    @Override
    public int countNumByParColumnId(Integer parColumnId , Integer channelId) {
        LambdaQueryWrapper<RColumn> queryWrapper = new QueryWrapper<RColumn>().lambda().eq(RColumn::getParColumnId,parColumnId);
        if (channelId!=null&&channelId>0){
            queryWrapper.eq(RColumn::getChannelId,channelId);
        }
        return count(queryWrapper);
    }

    @Override
    public boolean checkColumnAndArea(Integer channelId, Integer columnId) {
        iAreaService.checkArea(channelId);
        this.checkColumn(columnId);
        return true;
    }
    @Override
    public RColumn checkColumn(Integer columnId) {
        if (columnId==null) {
            throw new EngineException(PARAM_NOT_COMPLETE);
        }
        if (columnId<0){
            throw new EngineException(PARAM_IS_INVALID);
        }
        RColumn rColumn = columnMapper.selectById(columnId);
        if (rColumn==null||!"1".equals(rColumn.getLevel())){
            throw new EngineException("当前地区下无该栏目");
        }
        return rColumn;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String deleteColumn(Integer columnId) {
        StringBuilder stringBuilder = new StringBuilder("操作结果:");
        LambdaQueryWrapper<RColumn> lambdaQueryWrapper = new QueryWrapper<RColumn>().lambda().eq(RColumn::getParColumnId,columnId);
        //先删除栏目
        RColumn rColumn = getById(columnId);
        if (removeById(columnId)){
            //维护栏目顺序
//            int delNum = columnMapper.delColumnOrder(rColumn.getChannelId(),rColumn.getColumnOrder(),rColumn.getParColumnId());
//            log.info("栏目："+columnId+"删除需维护"+delNum+"条栏目");
            stringBuilder.append("删除栏目成功;");
            List<Integer> templetIds = listObjs(lambdaQueryWrapper, templetId -> (Integer) templetId);
            //删除模板
            //因为是删除该栏目下所有模板所以不需要维护顺序
            int temNum = columnMapper.delete(lambdaQueryWrapper);
            stringBuilder.append("删除模板").append(temNum).append("条成功;");
            //删除推荐位
            //推荐位也是全部删除不需要维护
            int eleNum = 0;
            if (templetIds!=null&&templetIds.size()>0){
                eleNum = elementMapper.delete(new QueryWrapper<Element>().lambda().in(Element::getColumnId,templetIds));
            }
            stringBuilder.append("删除推荐位").append(eleNum).append("条成功;");

        }else {
            stringBuilder.append("删除栏目失败");
        }
        return stringBuilder.toString();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ColumnVo updateOrSaveColumn(ColumnVo columnVo) {
        //TODO：目前栏目写死只有一级
        columnVo.setLevel("1");
        columnVo.setParColumnId(-1);
        //进行栏目顺序的维护
        //TODO：希望的是如果在后面更新的时候报错回滚，则维护工作也回滚到排序前的顺序
        this.maintainOrder(columnVo);
        //获取模板样式
        columnVo.setColTemplateVo(iTemplateService.getTemplateVo(columnVo.getTemplateId()));
        RColumn rColumn = new RColumn();
        BeanUtils.copyProperties(columnVo,rColumn);
        //更新或新增
        if (this.saveOrUpdate(rColumn)){
            columnVo.setColumnId(rColumn.getColumnId());
            return columnVo;
        }else {
            log.error("更新/新增某个栏目基础信息==========>失败,channelId{}",columnVo.getChannelId());
            throw new EngineException("更新/新增栏目基础信息失败");
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ColumnVo maintainOrder(ColumnVo columnVo) {
        //如果是更新
        if (columnVo.getColumnId() != null && columnVo.getColumnId() > 0) {
            if (columnVo.getColumnOrder() == null || columnVo.getColumnOrder() < 1) {
                //找出原栏目的排序号
                Map<String, Object> map = this.getMap(new QueryWrapper<RColumn>().lambda().select(RColumn::getColumnOrder).eq(RColumn::getColumnId, columnVo.getColumnId()));
                columnVo.setColumnOrder((Integer) map.get("column_order"));
            } else {
                //更新且有排序号,做维护
                Map<String, Object> map = this.getMap(new QueryWrapper<RColumn>().lambda().select(RColumn::getColumnOrder).eq(RColumn::getColumnId, columnVo.getColumnId()));
                if (map == null || map.size() < 1) {
                    //说明没有查出该栏目
                    throw new EngineException("该栏目" + columnVo.getColumnId() + "不存在");
                }
                //作比较
                Integer order = (Integer) map.get("column_order");
                //新的比旧的大
                if (columnVo.getColumnOrder() > order) {
                    //调整当前后面的排序-1  后移
//                    Integer num = this.countNumByParColumnId(-1, columnVo.getChannelId());
//                    int bigOrder = columnVo.getColumnOrder() > num ? num+1 : columnVo.getColumnOrder()+1;
                    int bigOrder = columnVo.getColumnOrder();
//                    columnVo.setColumnOrder(bigOrder);
                    int decrFlag = columnMapper.decrColumnOrder(columnVo.getChannelId(),order, bigOrder+1, -1);
                    log.info("栏目更新维护了" + decrFlag + "条已有栏目");
//                int decrFlag = columnMapper.updateOrder(bigOrder,order,-1);
                    int upd = columnMapper.updateSelfOrder(bigOrder, columnVo.getColumnId());
                    log.info("栏目更新维护自身" + upd + "条栏目");
                } else if (columnVo.getColumnOrder() < order) {
                    //前移
                    int smallOrder = columnVo.getColumnOrder() < 1 ? 1 : columnVo.getColumnOrder();
                    columnVo.setColumnOrder(smallOrder);
                    int incrFlag = columnMapper.incrColumnOrder(columnVo.getChannelId(),smallOrder - 1, order, -1);
                    log.info("栏目更新维护了" + incrFlag + "条已有栏目");
//                int incrFlag = columnMapper.updateOrder(smallOrder,order,-1);
                    int upd = columnMapper.updateSelfOrder(smallOrder, columnVo.getColumnId());
                    log.info("栏目更新维护自身" + upd + "条栏目");
                }
            }
        //如果是新增
        } else {
//            int num = this.countNumByParColumnId(-1, columnVo.getChannelId());
            Integer num = columnMapper.getMaxColumnOrder(columnVo.getChannelId(),-1);
            num = num==null?0:num;
            if (columnVo.getColumnOrder() == null || columnVo.getColumnOrder() < 1 ) {
                //如果是新增且没有带序号或者带了序号并且大于现有总数,排序号=总数+1
                columnVo.setColumnOrder(num + 1);
            } else if (columnVo.getColumnOrder() < num) {
                //如果带了序号并且小于现有最大序号数，则
                int incrFlag = columnMapper.incrColumnOrder(columnVo.getChannelId(),columnVo.getColumnOrder() - 1, num+1, -1);
                log.info("栏目新增需要维护了" + incrFlag + "条已有栏目");
//                int upd = columnMapper.updateSelfOrder(columnVo.getColumnOrder(), columnVo.getColumnId());
//                log.info("维护自身" + upd + "条栏目");
            }
            //如果带了序号并且大于现有最大序号数，不用做处理
        }
        return columnVo;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String maintainColumns(List<ColumnVo> columnVos,Integer channelId) {


        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ColumnVo> importColumnList(List<RColumn> rColumns,Integer channelId) {
        return rColumns.stream().map(rColumn -> {
            rColumn.setChannelId(channelId);
            try{
                if (this.save(rColumn)){
                    Integer columnId = rColumn.getColumnId();
                    log.info("importColumnList============>遍历复制渠道栏目成功,channelId:{},columnId:{}",channelId,rColumn.getColumnId());
                    List<RColumn> templets = rColumn.getTempletList();
                    List<TempletVo> templetVos = templets.stream().filter(Objects::nonNull).map(templet ->{
                        templet.setChannelId(channelId);
                        templet.setParColumnId(columnId);
                        try{
                            if (this.save(templet)){
                                //复制模板成功
                                Integer templetId = templet.getColumnId();
                                log.info("importColumnList============>遍历复制渠道模板成功,channelId:{},columnId:{},templetId:{}",channelId,columnId,templetId);
                                List<Element> elements = templet.getElementList();
                                List<ElementVo> elementVos = elements.stream().filter(Objects::nonNull).map(element -> {
                                    element.setColumnId(templetId);
                                    try{
                                        if (iElementService.save(element)){
                                            Integer elementId = element.getElementId();
                                            log.info("importColumnList============>遍历复制渠道推荐位成功,channelId:{},columnId:{},templetId:{},elementId:{}",channelId,columnId,templetId,elementId);
                                            ElementVo elementVo = new ElementVo();
                                            BeanUtils.copyProperties(element,elementVo);
                                            return elementVo;
                                        }
                                    }catch (Exception e){
                                        log.error("importColumnList============>遍历复制渠道推荐位错误,channelId:{},columnId:{},templetId:{},{}",channelId,columnId,templetId,e.getMessage());
                                    }
                                    return null;
                                }).collect(Collectors.toList());
                                TempletVo templetVo = new TempletVo();
                                BeanUtils.copyProperties(templet,templetVo);
                                templetVo.setElementVoList(elementVos);
                                return templetVo;
                            }
                        }catch (Exception e){
                            log.error("importColumnList============>遍历复制渠道模板错误,channelId:{},columnId:{},templetId:{},{}",channelId,columnId,templet.getColumnId(),e.getMessage());
                        }
                        return null;
                    }).collect(Collectors.toList());
                    ColumnVo columnVo = new ColumnVo();
                    BeanUtils.copyProperties(rColumn,columnVo);
                    columnVo.setTempletVoList(templetVos);
                    return columnVo;
                }
            }catch (Exception e){
                log.error("importColumnList============>遍历复制渠道栏目错误,channelId:{},columnId:{},{}",channelId,rColumn.getColumnId(),e.getMessage());
            }
            return null;
        }).collect(Collectors.toList());
    }


}