/**
 * @filename:ContentSpxlController 2020年10月13日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhua.rtb.common.EngineException;
import com.yanhua.rtb.common.ResponseResult;
import com.yanhua.rtb.entity.ContentSpxl;
import com.yanhua.rtb.entity.ContentSpxlDetail;
import com.yanhua.rtb.service.IContentSpxlDetailService;
import com.yanhua.rtb.service.IContentSpxlService;
import com.yanhua.rtb.vo.ContentSpxlDetailVo;
import com.yanhua.rtb.vo.ContentSpxlVo;
import com.yanhua.rtb.vo.TemplateVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.yanhua.rtb.common.ResultCodeEnum.PARAM_IS_BLANK;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： 联通入库表API接口层</P>
 * @version V1.0
 * @author Emiya
 * @time 2020年10月13日
 *
 */
@Api(tags = "联通彩铃控制器" )
@RestController
@ResponseResult
@RequestMapping("/contentSpxl")
public class ContentSpxlController {

    @Autowired
    private IContentSpxlService spxlService;
    @Autowired
    private IContentSpxlDetailService contentSpxlDetailService;

    @ApiOperation(value = "查询联通视频彩铃信息", httpMethod = "GET", notes = "根据视频彩铃id,返回视频彩铃信息视图")
    @RequestMapping(value = "/{contentSpxlId}", method = RequestMethod.GET)
    public ContentSpxlVo selectContentSpxlVo( @PathVariable String contentSpxlId) {
        ContentSpxl contentSpxl = spxlService.getById(contentSpxlId);
        ContentSpxlVo contentSpxlVo = new ContentSpxlVo();
        if (contentSpxl!=null){
            BeanUtils.copyProperties(contentSpxl,contentSpxlVo);
            String contentId = contentSpxl.getContentId();
            List<ContentSpxlDetail> contentSpxlDetails = contentSpxlDetailService.list(new QueryWrapper<ContentSpxlDetail>().lambda().eq(ContentSpxlDetail::getContentId,contentId));
            List<ContentSpxlDetailVo> contentSpxlDetailVos = contentSpxlDetails.stream().filter(Objects::nonNull).map(contentSpxlDetail -> {
                ContentSpxlDetailVo contentSpxlDetailVo = new ContentSpxlDetailVo();
                BeanUtils.copyProperties(contentSpxlDetail,contentSpxlDetailVo);
                return contentSpxlDetailVo;
            }).collect(Collectors.toList());
            contentSpxlVo.setFileList(contentSpxlDetailVos);
        }
        return contentSpxlVo;
    }
    @ApiOperation(value = "分页查询联通视频彩铃信息", httpMethod = "GET", notes = "根据条件参数,返回视频彩铃信息视图")
    @RequestMapping(value = "/operator/{operator}", method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "contentName", value = "彩铃名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "label", value = "彩铃分类", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "attribution", value = "彩铃来源", required = false, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "startPage", value = "起始页", required = false, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "pageSize", value = "请求的记录数", required = false, dataType = "Long"),
    })
    public List<ContentSpxlVo> selectContentSpxlBy(@PathVariable Integer operator,@RequestParam(required = false) String contentName, @RequestParam(required = false) String label,@RequestParam(required = false) Integer attribution,
                                               @RequestParam(required = false,defaultValue = "1") Integer startPage,@RequestParam(required = false,defaultValue = "20") Integer pageSize) {
        System.out.println("fgggggggg-------------------------------");
        //检查
        if (operator!=1&&operator!=2&&operator!=3){
            throw new EngineException(1001,"参数运营商无效");
        }
        if (startPage==null||startPage<0||pageSize==null||pageSize<0){
            throw new EngineException(PARAM_IS_BLANK);
        }
        LambdaQueryWrapper<ContentSpxl> queryWrapper = new QueryWrapper<ContentSpxl>().lambda().eq(ContentSpxl::getOperator,operator);
        if (attribution!=null&&attribution>-1){
            queryWrapper.eq(ContentSpxl::getAttribution,attribution);
        }
        if (StringUtils.isNotEmpty(contentName)){
            queryWrapper.like(ContentSpxl::getContentName,contentName);
        }
        if (StringUtils.isNotEmpty(label)){
            queryWrapper.like(ContentSpxl::getLabel,label);
        }
        IPage<ContentSpxlVo> contentSpxls = spxlService.pageVo(new Page<ContentSpxlVo>(startPage,pageSize),queryWrapper);
        return contentSpxls.getRecords();
    }
    @ApiOperation(value = "分页查询联通视频彩铃信息", httpMethod = "GET", notes = "无参数,返回视频彩铃信息视图")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "startPage", value = "起始页", required = false, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "pageSize", value = "请求的记录数", required = false, dataType = "Long"),
    })
    public List<ContentSpxl> selectContentSpxl(@RequestParam(required = false,defaultValue = "1") Integer startPage,@RequestParam(required = false,defaultValue = "20") Integer pageSize) {
        //检查
        if (startPage==null||startPage<0||pageSize==null||pageSize<0){
            throw new EngineException(PARAM_IS_BLANK);
        }
        IPage<ContentSpxl> contentSpxls = spxlService.page(new Page<ContentSpxl>(startPage,pageSize));
        return contentSpxls.getRecords();
    }

}