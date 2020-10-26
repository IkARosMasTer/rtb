/**
 * @filename:ElementController 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd.
 * All right reserved.
 */
package com.yanhua.rtb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanhua.rtb.common.ResponseResult;
import com.yanhua.rtb.entity.Element;
import com.yanhua.rtb.entity.RColumn;
import com.yanhua.rtb.service.IElementService;
import com.yanhua.rtb.service.IRColumnService;
import com.yanhua.rtb.service.ITempletService;
import com.yanhua.rtb.vo.ElementVo;
import com.yanhua.rtb.vo.TempletVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： 推荐位表API接口层</P>
 * @version V1.0
 * @author Emiya
 * @createTime 2020年10月01日  UpdateWrapper
 *
 */
@Api(tags = "推荐位控制器" )
@RestController
@ResponseResult
@RequestMapping("/element")
public class ElementController {

    @Autowired
    private IRColumnService irColumnService;
    @Autowired
    private ITempletService iTempletService;
    @Autowired
    private IElementService iElementService;


    @ApiOperation(value = "新增单个推荐位",httpMethod = "POST",notes = "生成推荐位id,返回推荐位视图")
    @RequestMapping(value = "/{areaId}/{columnId}/{templetId}",method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "areaId", value = "地区id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long")
    })
    @ResponseBody
    public ElementVo saveElement(@PathVariable Integer areaId, @PathVariable Integer columnId, @PathVariable Integer templetId, @RequestBody @Valid ElementVo elementVo){
        irColumnService.checkColumnAndArea(areaId,columnId);
        iTempletService.checkTemplet(templetId);
        elementVo.setColumnId(templetId);
        if (elementVo.getCreateTime()==null){
            elementVo.setCreateTime(new Date());
        }
        return iElementService.saveElement(elementVo);
    }

    @ApiOperation(value = "删除单个推荐位",httpMethod = "GET",notes = "根据推荐位id,删除推荐位")
    @RequestMapping(value = "/delete/{areaId}/{columnId}/{templetId}/{elementId}",method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "areaId", value = "地区id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "elementId", value = "推荐位id", required = true, dataType = "Long")
    })
    @ResponseBody
    public String deleteElement(@PathVariable Integer areaId, @PathVariable Integer columnId, @PathVariable Integer templetId, @PathVariable Integer elementId){
        irColumnService.checkColumnAndArea(areaId,columnId);
        //检查
        iTempletService.checkTemplet(templetId);
        return iElementService.deleteElement(elementId);
    }
    @ApiOperation(value = "删除模板下所有推荐位",httpMethod = "GET",notes = "根据模板id,删除所有推荐位")
    @RequestMapping(value = "/delete/{areaId}/{columnId}/{templetId}/",method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "areaId", value = "地区id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long")
    })
    @ResponseBody
    public String deleteElementAll(@PathVariable Integer areaId, @PathVariable Integer columnId, @PathVariable Integer templetId){
        irColumnService.checkColumnAndArea(areaId,columnId);
        //检查
        iTempletService.checkTemplet(templetId);
        return iElementService.deleteElementAll(templetId);
    }
    @ApiOperation(value = "查询模板下所有推荐位",httpMethod = "GET",notes = "根据模板id,查询所有推荐位")
    @RequestMapping(value = "/{areaId}/{columnId}/{templetId}/",method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "areaId", value = "地区id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long")
    })
    @ResponseBody
    public List<ElementVo> selectElementAll(@PathVariable Integer areaId,  @PathVariable Integer columnId,@PathVariable Integer templetId){
        irColumnService.checkColumnAndArea(areaId,columnId);
        //检查
        iTempletService.checkTemplet(templetId);
        List<Element> elements = iElementService.list(new QueryWrapper<Element>().lambda().eq(Element::getColumnId,templetId));
        List<ElementVo> elementVos =  elements.stream().filter(Objects::nonNull).map(element -> {
            ElementVo elementVo = new ElementVo();
            BeanUtils.copyProperties(element,elementVo);
            return elementVo;
        }).collect(Collectors.toList());
        return iElementService.getElementAlls(templetId);
    }
    @ApiOperation(value = "查询模板下单个推荐位",httpMethod = "GET",notes = "根据模板id,查询单个推荐位")
    @RequestMapping(value = "/{areaId}/{columnId}/{templetId}/{elementId}",method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "areaId", value = "地区id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "elementId", value = "推荐位id", required = true, dataType = "Long")
    })
    @ResponseBody
    public ElementVo selectElement(@PathVariable Integer areaId, @PathVariable Integer columnId, @PathVariable Integer templetId, @PathVariable Integer elementId){
        irColumnService.checkColumnAndArea(areaId,columnId);
        //检查
        iTempletService.checkTemplet(templetId);
        return iElementService.getElementAll(elementId);
    }

    //TODO：更新，伴随着模板一起更新，还会分开各自点击各自更新


    @ApiOperation(value = "更新单个推荐位",httpMethod = "PUT",notes = "根据推荐位id,更新推荐位")
    @RequestMapping(value = "/{areaId}/{columnId}/{templetId}/{elementId}",method = RequestMethod.PUT)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "areaId", value = "地区id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "elementId", value = "推荐位id", required = true, dataType = "Long")
    })
    @ResponseBody
    public String updateElement(@PathVariable Integer areaId, @PathVariable Integer columnId, @PathVariable Integer templetId, @PathVariable Integer elementId, @RequestBody @Valid ElementVo elementVo){
        irColumnService.checkColumnAndArea(areaId,columnId);
        iTempletService.checkTemplet(templetId);
        elementVo.setColumnId(templetId);
        if (elementVo.getUpdateTime()==null){
            elementVo.setUpdateTime(new Date());
        }
        elementVo.setElementId(elementId);
        return iElementService.updateElement(elementVo);
    }

    @ApiOperation(value = "更新所有推荐位",httpMethod = "PUT",notes = "根据模板id,更新推荐位")
    @RequestMapping(value = "/{areaId}/{columnId}/{templetId}/",method = RequestMethod.PUT)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "areaId", value = "地区id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long")
    })
    @ResponseBody
    public String  updateElementAll(@PathVariable Integer areaId, @PathVariable Integer columnId, @PathVariable Integer templetId, @RequestBody @Valid List<ElementVo> elementVos){
        irColumnService.checkColumnAndArea(areaId,columnId);
        iTempletService.checkTemplet(templetId);

        return "成功更新"+iElementService.updateElementAll(elementVos,templetId)+"条推荐位";
    }


}