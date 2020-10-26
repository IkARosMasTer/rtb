/**
 * @filename:RColumnServiceImpl 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2018 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    public List<ColumnVo> getAllColumnVoList(Integer areaId, Integer columnId, Integer startPage, Integer pageSize ) {
        Page<ColumnVo> page = new Page<>(startPage, pageSize);
        return columnMapper.getByAreaIdAndColumnId(page,areaId,columnId);
//        return columnMapper.getByAreaIdAndColumnId(page,new QueryWrapper<ColumnVo>().lambda().eq(ColumnVo::getColumnId,columnId).eq(ColumnVo::getAreaId,areaId).groupBy(ColumnVo::getColumnId)).getRecords();
    }

    @Override
    public List<ColumnVo> getColumnVoList(Integer areaId) {
        List<RColumn> columns = columnMapper.selectList(new QueryWrapper<RColumn>().lambda().eq(RColumn::getAreaId, areaId).eq(RColumn::getLevel,1));
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
            columnVo.setColumnOrder(countNumByParColumnId(-1)+1);
            BeanUtils.copyProperties(columnVo,rColumn);
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
    public int countNumByParColumnId(Integer parColumnId) {
        return count(new QueryWrapper<RColumn>().lambda().eq(RColumn::getParColumnId,parColumnId));
    }

    @Override
    public void checkColumnAndArea(Integer areaId, Integer columnId) {
        iAreaService.checkArea(areaId);
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
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String deleteColumn(Integer columnId) {
        StringBuilder stringBuilder = new StringBuilder("操作结果:");

        LambdaQueryWrapper<RColumn> lambdaQueryWrapper = new QueryWrapper<RColumn>().lambda().eq(RColumn::getParColumnId,columnId);
        //先删除栏目
        if (removeById(columnId)){
            stringBuilder.append("删除栏目成功;");
            List<Integer> templetIds = listObjs(lambdaQueryWrapper, templetId -> (Integer) templetId);
            //删除模板
            int temNum = columnMapper.delete(lambdaQueryWrapper);
            stringBuilder.append("删除模板").append(temNum).append("条成功;");
            //删除推荐位
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
}