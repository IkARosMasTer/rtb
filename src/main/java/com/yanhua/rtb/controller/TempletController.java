/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: TempletController
 * Author: Emiya
 * Date: 2020/10/15 9:43
 * Description: 模板（二级栏目）控制器
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanhua.rtb.common.ResponseResult;
import com.yanhua.rtb.entity.RColumn;
import com.yanhua.rtb.service.IRColumnService;
import com.yanhua.rtb.service.ITempletService;
import com.yanhua.rtb.vo.TempletVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 〈功能简述〉<br>
 * 〈模板（二级栏目）控制器〉
 *  <p>
 * @author Emiya
 * @create 2020/10/15 9:43
 * @version 1.0.0
 */
@Slf4j
@Api(tags = "模板(二级栏目)控制器" )
@RestController
@ResponseResult
@RequestMapping("/templet")
public class TempletController {

    @Autowired
    private IRColumnService irColumnService;
    @Autowired
    private ITempletService iTempletService;

    @ApiOperation(value = "新增单个模板",httpMethod = "POST",notes = "生成模板id,返回模板视图")
    @RequestMapping(value = "/{areaId}/{columnId}",method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "areaId", value = "地区id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long")
    })
    @ResponseBody
    public TempletVo saveTempletVo(@PathVariable Integer areaId, @PathVariable Integer columnId, @RequestBody  @Valid TempletVo templetVo){
        irColumnService.checkColumnAndArea(areaId,columnId);
        templetVo.setParColumnId(columnId);
        templetVo.setAreaId(areaId);
        if (templetVo.getCreateTime()==null){
            templetVo.setCreateTime(new Date());
        }
        return iTempletService.saveTemplet(templetVo);
    }

    @ApiOperation(value = "删除单个模板",httpMethod = "DELETE",notes = "根据模板id,删除模板")
    @RequestMapping(value = "/{areaId}/{columnId}/{templetId}",method = RequestMethod.DELETE)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "areaId", value = "地区id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long")
    })
    @ResponseBody
    public String deleteTempletVo(@PathVariable Integer areaId, @PathVariable Integer columnId, @PathVariable Integer templetId){
        irColumnService.checkColumnAndArea(areaId,columnId);
        return iTempletService.deleteTemplet(templetId);
    }

    @ApiOperation(value = "更新单个模板",httpMethod = "PUT",notes = "根据模板id,更新模板")
    @RequestMapping(value = "/{areaId}/{columnId}/{templetId}",method = RequestMethod.PUT)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "areaId", value = "地区id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long")
    })
    @ResponseBody
    public String updateTempletVo(@PathVariable Integer areaId, @PathVariable Integer columnId, @PathVariable Integer templetId, @RequestBody @Valid TempletVo templetVo){
        irColumnService.checkColumnAndArea(areaId,columnId);
        templetVo.setAreaId(areaId);
        templetVo.setParColumnId(columnId);
        templetVo.setColumnId(templetId);
        if (templetVo.getUpdateTime()==null){
            templetVo.setUpdateTime(new Date());
        }
        return iTempletService.updateTemplet(templetVo);
    }
    @ApiOperation(value = "查询单个模板",httpMethod = "GET",notes = "根据模板id,查询模板")
    @RequestMapping(value = "/{areaId}/{columnId}/{templetId}",method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "areaId", value = "地区id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long")
    })
    @ResponseBody
    public TempletVo selectTempletVo(@PathVariable Integer areaId, @PathVariable Integer columnId, @PathVariable Integer templetId){
        irColumnService.checkColumnAndArea(areaId,columnId);
        RColumn rColumn = irColumnService.getById(templetId);
        TempletVo templetVo = new TempletVo();
        BeanUtils.copyProperties(rColumn,templetVo);
        return templetVo;
    }

    @ApiOperation(value = "查询所有模板",httpMethod = "GET",notes = "根据栏目id,查询所有模板")
    @RequestMapping(value = "/{areaId}/{columnId}",method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "areaId", value = "地区id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
    })
    @ResponseBody
    public List<TempletVo> selectTempletVo(@PathVariable Integer areaId, @PathVariable Integer columnId){
        irColumnService.checkColumnAndArea(areaId,columnId);
        List<RColumn> rColumns = irColumnService.list(new QueryWrapper<RColumn>().lambda().eq(RColumn::getParColumnId,columnId));
        return rColumns.stream().filter(Objects::nonNull).map(rColumn -> {
            TempletVo templetVo = new TempletVo();
            BeanUtils.copyProperties(rColumn,templetVo);
            return templetVo;
        }).collect(Collectors.toList());
    }


}