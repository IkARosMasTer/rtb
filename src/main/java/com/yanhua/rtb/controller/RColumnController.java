/**
 * @filename:RColumnController 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.controller;

import com.yanhua.rtb.service.ITemplateService;
import com.yanhua.rtb.vo.ColumnVo;
import com.yanhua.rtb.common.EngineException;
import com.yanhua.rtb.common.ResponseResult;
import com.yanhua.rtb.entity.RColumn;
import com.yanhua.rtb.service.IAreaService;
import com.yanhua.rtb.service.IRColumnService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static com.yanhua.rtb.common.ResultCodeEnum.*;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * @ApiImplicitParam 的paramType值：
 *  *     header-->放在请求头。请求参数的获取：@RequestHeader(代码中接收注解)
 *  *     query-->用于get请求的参数拼接。请求参数的获取：@RequestParam(代码中接收注解)
 *  *     path（用于restful接口）-->请求参数的获取：@PathVariable(代码中接收注解)
 *  *     body-->放在请求体。请求参数的获取：@RequestBody(代码中接收注解)
 *  *     form（不常用），文件类型
 * <p>说明： 栏目表API接口层</P>
 * @version V1.0
 * @author Emiya
 * @create 2020年10月01日
 *
 */
@Slf4j
@Api(tags="栏目控制器" )
@RestController
@ResponseResult
@RequestMapping("/rColumn")
public class RColumnController {
    @Autowired
    private IAreaService areaService;
    @Autowired
    private IRColumnService columnService;
    @Autowired
    private ITemplateService iTemplateService;


    @ApiOperation(value = "查询地区下栏目的所有信息",httpMethod = "GET",notes = "默认返回该栏目以及其下的专题以及元素")
    @RequestMapping(value = "/{channelId}/{columnId}",method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "channelId", value = "渠道id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "level", value = "层级", required = false, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "startPage", value = "起始页", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "pageSize", value = "请求的记录数", required = true, dataType = "Long"),
    })
    @ResponseBody
    public List<ColumnVo> findColumnAllByAreaIdAndColumnId(@PathVariable Integer channelId, @PathVariable Integer columnId,
                                                           @RequestParam(required = false) Integer level, @RequestParam Integer startPage, @RequestParam Integer pageSize ){
        //检查
        columnService.checkColumnAndArea(channelId,columnId);
        if (startPage==null||startPage<0||pageSize==null||pageSize<0){
            throw new EngineException(PARAM_IS_BLANK);
        }
        return columnService.getAllColumnVoList(channelId,columnId,startPage,pageSize);
    }

    @ApiOperation(value = "查询地区下的所有栏目信息",httpMethod = "GET",notes = "默认返回该渠道下的所有栏目集合")
    @RequestMapping(value = "/{channelId}",method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "channelId", value = "渠道id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "level", value = "层级", required = false, dataType = "Long")
    })
    public List<ColumnVo> findColumnByAreaId(@PathVariable Integer channelId
            , @RequestParam(required = false) Integer level){
        areaService.checkArea(channelId);
        return columnService.getColumnVoList(channelId);
    }

    @ApiOperation(value = "更新/新增单个栏目基础信息",httpMethod = "POST",notes = "更新/新增元數據,生成栏目id")
    @RequestMapping(value = "/{channelId}",method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "channelId", value = "渠道id", required = true, dataType = "Long")
    })
    public ColumnVo saveOrUpdateColumnVo(@PathVariable Integer channelId,@RequestBody  @Valid ColumnVo columnVo){
        //todo:RequestBodyAdvice 来统一做创建时间或更新时间判断
        areaService.checkArea(channelId);
        columnVo.setChannelId(channelId);
        if (columnVo.getCreateTime()==null){
            columnVo.setCreateTime(new Date());
        }
        if (columnVo.getUpdateTime()==null){
            columnVo.setUpdateTime(new Date());
        }
        //TODO：目前栏目写死只有一级
        columnVo.setLevel("1");
        columnVo.setParColumnId(-1);
        columnVo.setColumnOrder(columnService.countNumByParColumnId(-1)+1);
        //获取模板样式
        columnVo.setColTemplateVo(iTemplateService.getTemplateVo(columnVo.getTemplateId()));
        RColumn rColumn = new RColumn();
        BeanUtils.copyProperties(columnVo,rColumn);
        if (columnService.saveOrUpdate(rColumn)){
            columnVo.setColumnId(rColumn.getColumnId());
            return columnVo;
        }else {
            log.error("更新/新增某个栏目基础信息==========>失败,channelId{}",channelId);
            throw new EngineException("更新/新增栏目基础信息失败");
        }
    }

    @ApiOperation(value = "更新/新增某个栏目的所有信息",httpMethod = "POST",notes = "更新/新增元數據,生成栏目id")
    @RequestMapping(value = "/{channelId}/overAll",method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "channelId", value = "渠道id", required = true, dataType = "Long")
    })
    public String saveOrUpdateColumnAll(@PathVariable Integer channelId,@RequestBody @Valid ColumnVo columnVo){
        //todo:RequestBodyAdvice 来统一做创建时间或更新时间判断
        areaService.checkArea(channelId);
        columnVo.setChannelId(channelId);
        return columnService.saveOrUpdateColumnVo(columnVo);
    }

    @ApiOperation(value = "删除某个栏目的所有信息",httpMethod = "GET",notes = "删除元數據以及相关数据,返回信息")
    @RequestMapping(value = "/delete/{channelId}/{columnId}",method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "channelId", value = "渠道id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),

    })
    public String deleteColumnAll(@PathVariable Integer channelId,@PathVariable Integer columnId){
        columnService.checkColumnAndArea(channelId, columnId);
        return columnService.deleteColumn(columnId);
    }



}