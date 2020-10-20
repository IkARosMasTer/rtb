/**
 * @filename:ContentSpxlController 2020年10月13日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanhua.rtb.common.ResponseResult;
import com.yanhua.rtb.entity.ContentSpxl;
import com.yanhua.rtb.entity.ContentSpxlDetail;
import com.yanhua.rtb.service.IContentSpxlDetailService;
import com.yanhua.rtb.service.IContentSpxlService;
import com.yanhua.rtb.vo.ContentSpxlDetailVo;
import com.yanhua.rtb.vo.ContentSpxlVo;
import com.yanhua.rtb.vo.TemplateVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @ResponseBody
    public ContentSpxlVo selectTemplateVo( @PathVariable String contentSpxlId) {
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
	
}