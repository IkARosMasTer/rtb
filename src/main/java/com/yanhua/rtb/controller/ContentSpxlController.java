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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
@Slf4j
@Api(tags = "彩铃控制器" )
@RestController
@ResponseResult
@RequestMapping("/contentSpxl")
public class ContentSpxlController {

    @Autowired
    private IContentSpxlService spxlService;
    @Autowired
    private IContentSpxlDetailService contentSpxlDetailService;

    @ApiOperation(value = "查询视频彩铃信息", httpMethod = "GET", notes = "根据视频彩铃id,返回视频彩铃信息视图")
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
    @ApiOperation(value = "分页查询视频彩铃信息", httpMethod = "GET", notes = "根据条件参数,返回视频彩铃信息视图")
    @RequestMapping(value = "/operator/{operator}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "operator", value = "运营商", required = false, dataType = "Long"),
            @ApiImplicitParam(paramType = "query", name = "contentName", value = "彩铃名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "label", value = "彩铃分类", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "attribution", value = "彩铃来源", required = false, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "startPage", value = "起始页", required = false, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "pageSize", value = "请求的记录数", required = false, dataType = "Long"),
    })
    public Map<String, Object> selectContentSpxlBy(@PathVariable Integer operator, @RequestParam(required = false) String contentName, @RequestParam(required = false) String label, @RequestParam(required = false) Integer attribution,
                                                   @RequestParam(required = false,defaultValue = "1") Integer startPage, @RequestParam(required = false,defaultValue = "20") Integer pageSize) {
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
        Map<String,Object> ret = new HashMap<>();
        ret.put("contentSpxls",contentSpxls.getRecords());
        ret.put("current",contentSpxls.getCurrent());
        ret.put("size",contentSpxls.getRecords().size());
        ret.put("pages",contentSpxls.getPages());
        ret.put("total",contentSpxls.getTotal());
        return ret;
    }
    @ApiOperation(value = "分页查询视频彩铃信息", httpMethod = "GET", notes = "无参数,返回视频彩铃信息视图")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "startPage", value = "起始页", required = false, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "pageSize", value = "请求的记录数", required = false, dataType = "Long"),
    })
    public Map<String, Object> selectContentSpxl(@RequestParam(required = false,defaultValue = "1") Integer startPage, @RequestParam(required = false,defaultValue = "20") Integer pageSize) {
        //检查
        if (startPage==null||startPage<0||pageSize==null||pageSize<0){
            throw new EngineException(PARAM_IS_BLANK);
        }
        IPage<ContentSpxl> contentSpxls = spxlService.page(new Page<ContentSpxl>(startPage,pageSize));
        Map<String,Object> ret = new HashMap<>();
        ret.put("contentSpxls",contentSpxls.getRecords());
        ret.put("current",contentSpxls.getCurrent());
        ret.put("size",contentSpxls.getRecords().size());
        ret.put("pages",contentSpxls.getPages());
        ret.put("total",contentSpxls.getTotal());
        return ret;
    }
    @ApiOperation(value = "分页查询即将过期视频彩铃信息", httpMethod = "GET", notes = "无参数,返回视频彩铃信息视图")
    @RequestMapping(value = "/expired", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "startPage", value = "起始页", required = false, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "pageSize", value = "请求的记录数", required = false, dataType = "Long"),
    })
    public Map<String, Object> selectExpiredContentSpxl(@RequestParam(required = false,defaultValue = "1") Integer startPage, @RequestParam(required = false,defaultValue = "20") Integer pageSize) {
        //检查
        if (startPage==null||startPage<0||pageSize==null||pageSize<0){
            throw new EngineException(PARAM_IS_BLANK);
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = localDateTime.plusMonths(1);
        String time = localDateTime1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("未来一个月的时间===="+time);
//        Date date = Date.from(localDateTime1.toInstant(ZoneOffset.of("+8")));
        Date date = Date.from(localDateTime1.atZone( ZoneId.systemDefault()).toInstant());
        IPage<ContentSpxl> contentSpxls = spxlService.page(new Page<ContentSpxl>(startPage,pageSize),new QueryWrapper<ContentSpxl>().lambda().le(ContentSpxl::getValidDate,date).orderByAsc(ContentSpxl::getValidDate));
        Map<String,Object> ret = new HashMap<>();
        ret.put("contentSpxls",contentSpxls.getRecords());
        ret.put("current",contentSpxls.getCurrent());
        ret.put("size",contentSpxls.getRecords().size());
        ret.put("pages",contentSpxls.getPages());
        ret.put("total",contentSpxls.getTotal());
        return ret;
    }


    @ApiOperation(value = "修改视频彩铃标签", httpMethod = "POST", notes = "")
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="path", name = "id", value = "彩铃主键id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "labeld", value = "标签", required = true, dataType = "String"),
    })
    public String updateContentSpxl(@PathVariable Integer id,@RequestParam String labeld){
        if (StringUtils.startsWith(labeld,"#")){
            labeld = StringUtils.substring(labeld,1,labeld.length());
        }
        if (StringUtils.endsWith(labeld,"#")){
            labeld = StringUtils.substring(labeld,0,labeld.length()-1);
        }
        ContentSpxl contentSpxl = new ContentSpxl();
        contentSpxl.setId(id);
        contentSpxl.setLabel(labeld);
        if (spxlService.updateById(contentSpxl)){
            return "视频彩铃标签修改成功";
        }else {
            return "视频彩铃标签修改失败";
        }
    }

}