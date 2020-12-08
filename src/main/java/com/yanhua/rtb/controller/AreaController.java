/**
 * @filename:AreaController 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanhua.rtb.common.ResponseResult;
import com.yanhua.rtb.entity.Area;
import com.yanhua.rtb.entity.RColumn;
import com.yanhua.rtb.mapper.RColumnMapper;
import com.yanhua.rtb.service.IAreaService;
import com.yanhua.rtb.service.IRColumnService;
import com.yanhua.rtb.vo.ColumnVo;
import freemarker.template.utility.StringUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： 渠道API接口层</P>
 * @version V1.0
 * @author Emiya
 * @create 2020年10月01日
 *
 */
@Api(tags = "渠道控制器")
@RestController
@ResponseResult
@RequestMapping("/channel")
public class AreaController {

    @Autowired
    private IAreaService areaService;
    @Autowired
    private IRColumnService irColumnService;

    @ApiOperation(value = "查询某个渠道信息",httpMethod = "GET",notes = "默认返回禁用外的数据")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ApiImplicitParam(paramType="path", name = "id", value = "地区id", required = true, dataType = "Long")
    public Area findAreaById(@PathVariable Integer id){
        return areaService.getById(id);
    }
//    @ApiOperation(value = "查询某个渠道信息(根据渠道标识)",httpMethod = "GET",notes = "默认返回禁用外的数据")
//    @RequestMapping(value = "/channelCode",method = RequestMethod.GET)
//    @ApiImplicitParam(paramType="query", name = "channelCode", value = "渠道标识", required = true, dataType = "String")
//    public List<Area> findAreaByChannelCode(@RequestParam String channelCode){
//        return areaService.list(new QueryWrapper<Area>().lambda().eq(Area::getChannelCode,channelCode).eq(Area::getStatus,0));
//    }
    @ApiOperation(value = "查询某个渠道信息(根据运营商、渠道标识)",httpMethod = "GET",notes = "默认返回禁用外的数据")
    @RequestMapping(value = "",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "operator", value = "运营商", required = false, dataType = "Long"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "状态", required = false, dataType = "Long"),
            @ApiImplicitParam(paramType = "query", name = "channelCode", value = "渠道标识", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "company", value = "公司名称", required = false, dataType = "String")

    })
    public List<Area> findAreaByOperator(@RequestParam(required = false) Integer operator,@RequestParam(required = false) String channelCode
            ,@RequestParam(required = false) Integer status,@RequestParam(required = false) String company){
        LambdaQueryWrapper<Area> queryWrapper = new QueryWrapper<Area>().lambda();
        if (status!=null){
            queryWrapper.eq(Area::getStatus,status);
        }
        if (operator!=null&&operator>0){
            queryWrapper.eq(Area::getOperator,operator);
        }
        if (StringUtils.isNotEmpty(channelCode)){
            queryWrapper.eq(Area::getChannelCode,channelCode);
        }
        if (StringUtils.isNotEmpty(company)){
            queryWrapper.like(Area::getCompany,company);
        }
        return areaService.list(queryWrapper);
    }

    @ApiOperation(value = "查询所有渠道信息",httpMethod = "GET",notes = "默认返回所有禁用外的数据")
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public List<Area> findAllArea(){
        return areaService.findAll();
    }

    @ApiOperation(value = "禁用某个渠道信息",httpMethod = "PUT",notes = "默認禁用不刪除")
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public int disAreaById(@PathVariable Integer id){
        return areaService.disableById(id);
    }

    @ApiOperation(value = "刪除某个渠道信息",httpMethod = "GET",notes = "徹底刪除元數據")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.GET)
    public int delArea(@PathVariable Integer id){
        return areaService.removeById(id);
    }

    @ApiOperation(value = "更新/新增某个渠道信息",httpMethod = "POST",notes = "更新/新增元數據")
    @RequestMapping(value = "",method = RequestMethod.POST)
    public boolean findAllArea(@RequestBody  @Valid Area area){
        //todo:RequestBodyAdvice 来统一做创建时间或更新时间判断
        if (area.getCreateTime()==null){
            area.setCreateTime(new Date());
        }
        if (area.getUpdateTime()==null){
            area.setUpdateTime(new Date());
        }
        if (area.getStatus()==null){
            area.setStatus(0);
        }
        return areaService.saveOrUpdate(area);
    }
    @ApiOperation(value = "复制某个渠道信息",httpMethod = "POST",notes = "复制元數據")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "渠道id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType = "query", name = "channelCode", value = "渠道标识", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "company", value = "公司名称", required = true, dataType = "String")

    })
    @RequestMapping(value = "/copy/{id}",method = RequestMethod.POST)
    public String copyArea(@PathVariable Integer id,@RequestParam String channelCode,@RequestParam String company){
        //查出渠道
        Area area = areaService.checkArea(id);
        if (area.getCreateTime()==null){
            area.setCreateTime(new Date());
        }
        if (area.getUpdateTime()==null){
            area.setUpdateTime(new Date());
        }
        if (area.getStatus()==null){
            area.setStatus(0);
        }
        //查出渠道对应的栏目、模板、推荐位
        List<RColumn> rColumns = areaService.copyAreaAll(area);
        //重新复制插入
        area.setChannelId(null);
        area.setChannelCode(channelCode);
        area.setCompany(company);
        if (areaService.saveOrUpdate(area)){
            if (rColumns!=null&&rColumns.size()>0) {
                List<ColumnVo> columnVos = irColumnService.importColumnList(rColumns, area.getChannelId());
            }
            return "新渠道复制成功!";
        }else {
            return "渠道复制失败,请稍后重试!";
        }
    }



}