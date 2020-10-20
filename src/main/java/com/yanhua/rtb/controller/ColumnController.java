///**
// * @filename:ColumnController 2020年10月01日
// * @project rtb  V1.0
// * Copyright(c) 2020 Emiya Co. Ltd.
// * All right reserved.
// */
//package com.yanhua.rtb.controller;
//
//import com.yanhua.rtb.Vo.ColumnVo;
//import com.yanhua.rtb.Vo.SpecialVo;
//import com.yanhua.rtb.common.EngineException;
//import com.yanhua.rtb.common.ResponseResult;
//import com.yanhua.rtb.entity.Area;
//import com.yanhua.rtb.entity.Column;
//import com.yanhua.rtb.mapper.AreaMapper;
//import com.yanhua.rtb.mapper.ColumnMapper;
//import com.yanhua.rtb.service.IAreaService;
//import com.yanhua.rtb.service.IColumnService;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import io.swagger.annotations.Api;
//
//import java.util.List;
//
//import static com.yanhua.rtb.common.ResultCodeEnum.*;
//
///**
// * <p>自动生成工具：mybatis-dsc-generator</p>
// * @ApiImplicitParam 的paramType值：
// *     header-->放在请求头。请求参数的获取：@RequestHeader(代码中接收注解)
// *     query-->用于get请求的参数拼接。请求参数的获取：@RequestParam(代码中接收注解)
// *     path（用于restful接口）-->请求参数的获取：@PathVariable(代码中接收注解)
// *     body-->放在请求体。请求参数的获取：@RequestBody(代码中接收注解)
// *     form（不常用），文件类型
// * <p>说明： 栏目表API接口层</P>
// * @version V1.0
// * @author Emiya
// * @create 2020年10月01日
// *
// */
//@Api(description = "栏目表",value="栏目表" )
//@RestController
//@ResponseResult
//@RequestMapping("/column")
//public class ColumnController {
//
//    @Autowired
//    private IAreaService areaService;
//    @Autowired
//    private IColumnService columnService;
//    @Autowired
//    private ColumnMapper columnMapper;
//
//
//    @ApiOperation(value = "查询地区下栏目的所有信息",httpMethod = "GET",notes = "默认返回该栏目以及其下的专题以及元素")
//    @RequestMapping(value = "/{areaId}/{columnId}",method = RequestMethod.GET)
//    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "areaId", value = "地区id", required = true, dataType = "Long"),
//    @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
//    @ApiImplicitParam(paramType="query", name = "level", value = "层级", required = false, dataType = "Long"),
//    @ApiImplicitParam(paramType="query", name = "startPage", value = "起始页", required = true, dataType = "Long"),
//    @ApiImplicitParam(paramType="query", name = "pageSize", value = "请求的记录数", required = true, dataType = "Long"),
//    })
//    public List<ColumnVo> findColumnAllByAreaIdAndColumnId(@PathVariable Integer areaId, @PathVariable Integer columnId,
//                                                          @RequestParam(required = false) Integer level,@RequestParam Integer startPage, @RequestParam Integer pageSize ){
//        if (areaId==null||columnId==null) {
//            throw new EngineException(PARAM_NOT_COMPLETE);
//        }
//        if (areaId<0||columnId<0){
//            throw new EngineException(PARAM_IS_INVALID);
//        }
//        if (startPage==null||startPage<0||pageSize==null||pageSize<0){
//            throw new EngineException(PARAM_IS_BLANK);
//        }
//        List<ColumnVo> columnVos = columnService.getAllColumnVoList(areaId,columnId,startPage,pageSize);
//        return columnVos;
//    }
//
//    @ApiOperation(value = "查询地区下的栏目信息",httpMethod = "GET",notes = "默认返回该地区下的所有栏目集合")
//    @RequestMapping(value = "/{areaId}",method = RequestMethod.GET)
//    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "areaId", value = "地区id", required = true, dataType = "Long"),
//            @ApiImplicitParam(paramType="query", name = "level", value = "层级", required = false, dataType = "Long")
//    })
//    public List<Column> findColumnByAreaId(@PathVariable Integer areaId
//            ,@RequestParam(required = false) Integer level){
//
//        if (areaId==null||areaId<0){
//            throw new EngineException(PARAM_IS_INVALID_OR_BLANK);
//        }
//        return columnService.getColumnVoList(areaId);
//    }
//
//}