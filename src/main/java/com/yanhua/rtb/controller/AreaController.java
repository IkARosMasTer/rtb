/**
 * @filename:AreaController 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.controller;

import com.yanhua.rtb.common.ResponseResult;
import com.yanhua.rtb.entity.Area;
import com.yanhua.rtb.mapper.AreaMapper;
import com.yanhua.rtb.service.IAreaService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： 地区API接口层</P>
 * @version V1.0
 * @author Emiya
 * @create 2020年10月01日
 *
 */
@Api(tags = "地区控制器")
@RestController
@ResponseResult
@RequestMapping("/area")
public class AreaController {

    @Autowired
    private IAreaService areaService;

    @ApiOperation(value = "查询某个地区信息",httpMethod = "GET",notes = "默认返回禁用外的数据")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ApiImplicitParam(paramType="path", name = "id", value = "地区id", required = true, dataType = "Long")
    public Area findAreaById(@PathVariable Integer id){
        return areaService.getById(id);
    }

    @ApiOperation(value = "查询所有地区信息",httpMethod = "GET",notes = "默认返回所有禁用外的数据")
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public List<Area> findAllArea(){
        return areaService.findAll();
    }

    @ApiOperation(value = "禁用某个地区信息",httpMethod = "PUT",notes = "默認禁用不刪除")
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public int disAreaById(@PathVariable Integer id){
        return areaService.disableById(id);
    }

    @ApiOperation(value = "刪除某个地区信息",httpMethod = "GET",notes = "徹底刪除元數據")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.GET)
    public int delArea(@PathVariable Integer id){
        return areaService.removeById(id);
    }

    @ApiOperation(value = "更新/新增某个地区信息",httpMethod = "POST",notes = "更新/新增元數據")
    @RequestMapping(value = "",method = RequestMethod.POST)
    public boolean findAllArea(@RequestBody  @Valid Area area){
        //todo:RequestBodyAdvice 来统一做创建时间或更新时间判断
        if (area.getCreateTime()==null){
            area.setCreateTime(new Date());
        }
        if (area.getUpdateTime()==null){
            area.setUpdateTime(new Date());
        }
        return areaService.saveOrUpdate(area);
    }



}