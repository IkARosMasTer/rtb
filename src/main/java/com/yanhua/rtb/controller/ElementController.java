/**
 * @filename:ElementController 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd.
 * All right reserved.
 */
package com.yanhua.rtb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanhua.rtb.common.EngineException;
import com.yanhua.rtb.common.ResponseResult;
import com.yanhua.rtb.entity.Area;
import com.yanhua.rtb.entity.ContentSpxl;
import com.yanhua.rtb.entity.Element;
import com.yanhua.rtb.entity.Template;
import com.yanhua.rtb.service.*;
import com.yanhua.rtb.util.CustomForEach;
import com.yanhua.rtb.util.FileUtil;
import com.yanhua.rtb.vo.ElementVo;
import com.yanhua.rtb.vo.TemplateVo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.yanhua.rtb.common.ResultCodeEnum.PARAM_IS_BLANK;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： 推荐位表API接口层</P>
 * @version V1.0
 * @author Emiya
 * @createTime 2020年10月01日  UpdateWrapper
 *
 */
@Slf4j
@Validated
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
    @Autowired
    private IContentSpxlService iContentSpxlService;
    @Autowired
    private IAreaService iAreaService;
    @Autowired
    private ITemplateService iTemplateService;
    @Autowired
    private Environment environment;


    @ApiOperation(value = "批量新增推荐位",httpMethod = "POST",notes = "生成推荐位id,返回推荐位视图,适合单个或批量")
    @RequestMapping(value = "/{channelId}/{columnId}/{templetId}",method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "channelId", value = "渠道id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long")
    })
    @ResponseBody
    public List<ElementVo> saveElement(@PathVariable Integer channelId, @PathVariable Integer columnId, @PathVariable Integer templetId, @RequestBody @Valid List<ElementVo> elementVos){
        Area area = iAreaService.checkArea(channelId);
        Integer operation = area.getOperator();
        irColumnService.checkColumn(columnId);
        iTempletService.checkTemplet(templetId);
        if (elementVos==null||elementVos.size()<1){
            throw new EngineException(PARAM_IS_BLANK);
        }
        //遍历vos补充时间+排序号，返回element
        AtomicInteger i = new AtomicInteger();
        List<Element> elements = elementVos.stream().filter(Objects::nonNull).map(elementVo -> {
            elementVo.setColumnId(templetId);
            if (elementVo.getCreateTime() == null) {
                elementVo.setCreateTime(new Date());
            }
            if (elementVo.getUpdateTime() == null) {
                elementVo.setUpdateTime(new Date());
            }
            Integer maxElementOrder = iElementService.getMaxElementOrder(templetId);
            //新增推荐位不能顺带指定序号，因为批量情况下序号指定和插入是分开的。
            elementVo.setElementOrder(maxElementOrder + i.incrementAndGet());
            Element element = new Element();
            BeanUtils.copyProperties(elementVo, element);
            //放入模板样式
            Integer templateId = elementVo.getTemplateId();
            //获取模板样式
            if (templateId!=null&&templateId>0){
                Template template = iTemplateService.getById(templateId);
                TemplateVo templateVo = new TemplateVo();
                BeanUtils.copyProperties(template,templateVo);
                element.setEleTemplateVo(templateVo);
            }
            return element;
        }).collect(Collectors.toList());
        //遍历检查对应的彩铃是否存在
        elements.forEach(element -> {
            //判断是否存在content_id
            ContentSpxl contentSpxl = iContentSpxlService.getOne(new QueryWrapper<ContentSpxl>().lambda().eq(ContentSpxl::getContentId,element.getContentSpxlId()).eq(ContentSpxl::getOperator,operation).last("LIMIT 1"));
            if (contentSpxl==null){
                //无对应的彩铃内容
                log.warn("saveElement=========>无对应的彩铃内容:模板id{}",templetId);
                throw new EngineException(1001,"模板"+templetId+"无对应的彩铃内容:"+element.getContentSpxlId());
            }
            //        elementVo.setElementName(contentSpxl.getContentName());
        });
        if (iElementService.saveBatch(elements)){
            //插入成功
            return elements.stream().map(element -> {
                ElementVo elementVo = new ElementVo();
                BeanUtils.copyProperties(element,elementVo);
                return elementVo;
            }).collect(Collectors.toList());
        }else {
            throw new EngineException("推价位插入失败");
        }
    }

    @ApiOperation(value = "删除单个推荐位",httpMethod = "GET",notes = "根据推荐位id,删除推荐位")
    @RequestMapping(value = "/delete/{channelId}/{columnId}/{templetId}/{elementId}",method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "channelId", value = "渠道id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "elementId", value = "推荐位id", required = true, dataType = "Long")
    })
    @ResponseBody
    public String deleteElement(@PathVariable Integer channelId, @PathVariable Integer columnId, @PathVariable Integer templetId, @PathVariable Integer elementId){
        irColumnService.checkColumnAndArea(channelId,columnId);
        //检查
        iTempletService.checkTemplet(templetId);
        return iElementService.deleteElement(elementId);
    }
    @ApiOperation(value = "删除模板下所有推荐位",httpMethod = "GET",notes = "根据模板id,删除所有推荐位")
    @RequestMapping(value = "/delete/{channelId}/{columnId}/{templetId}/",method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "channelId", value = "渠道id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long")
    })
    @ResponseBody
    public String deleteElementAll(@PathVariable Integer channelId, @PathVariable Integer columnId, @PathVariable Integer templetId){
        irColumnService.checkColumnAndArea(channelId,columnId);
        //检查
        iTempletService.checkTemplet(templetId);
        return iElementService.deleteElementAll(templetId);
    }
    @ApiOperation(value = "查询模板下所有推荐位",httpMethod = "GET",notes = "根据模板id,查询所有推荐位")
    @RequestMapping(value = "/{channelId}/{columnId}/{templetId}/",method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "channelId", value = "渠道id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long")
    })
    @ResponseBody
    public List<ElementVo> selectElementAll(@PathVariable Integer channelId,  @PathVariable Integer columnId,@PathVariable Integer templetId){
        irColumnService.checkColumnAndArea(channelId,columnId);
        //检查
        iTempletService.checkTemplet(templetId);
//        List<Element> elements = iElementService.list(new QueryWrapper<Element>().lambda().eq(Element::getColumnId,templetId));
//        List<ElementVo> elementVos =  elements.stream().filter(Objects::nonNull).map(element -> {
//            ElementVo elementVo = new ElementVo();
//            BeanUtils.copyProperties(element,elementVo);
//            return elementVo;
//        }).collect(Collectors.toList());
        return iElementService.getElementAlls(templetId);
    }
    @ApiOperation(value = "查询模板下单个推荐位",httpMethod = "GET",notes = "根据模板id,查询单个推荐位")
    @RequestMapping(value = "/{channelId}/{columnId}/{templetId}/{elementId}",method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "channelId", value = "渠道id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "elementId", value = "推荐位id", required = true, dataType = "Long")
    })
    @ResponseBody
    public ElementVo selectElement(@PathVariable Integer channelId, @PathVariable Integer columnId, @PathVariable Integer templetId, @PathVariable Integer elementId){
        irColumnService.checkColumnAndArea(channelId,columnId);
        //检查
        iTempletService.checkTemplet(templetId);
        return iElementService.getElementAll(elementId);
    }

    //TODO：更新，伴随着模板一起更新，还会分开各自点击各自更新


    @ApiOperation(value = "更新单个推荐位",httpMethod = "POST",notes = "根据推荐位id,更新推荐位")
    @RequestMapping(value = "/{channelId}/{columnId}/{templetId}/{elementId}",method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "channelId", value = "渠道id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "elementId", value = "推荐位id", required = true, dataType = "Long")
    })
    @ResponseBody
    public String updateElement(@PathVariable Integer channelId, @PathVariable Integer columnId, @PathVariable Integer templetId, @PathVariable Integer elementId, @RequestBody @Valid ElementVo elementVo){
        irColumnService.checkColumnAndArea(channelId,columnId);
        iTempletService.checkTemplet(templetId);
        elementVo.setColumnId(templetId);
        if (elementVo.getCreateTime()==null){
            elementVo.setCreateTime(new Date());
        }
        if (elementVo.getUpdateTime()==null){
            elementVo.setUpdateTime(new Date());
        }
        elementVo.setElementId(elementId);
        return iElementService.updateElement(elementVo);
    }

    @ApiOperation(value = "(废弃)更新所有推荐位",httpMethod = "POST",notes = "根据模板id,更新推荐位")
    @RequestMapping(value = "/{channelId}/{columnId}/{templetId}/",method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(paramType="path", name = "channelId", value = "渠道id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long")
    })
    @ResponseBody
    public String  updateElementAll(@PathVariable Integer channelId, @PathVariable Integer columnId, @PathVariable Integer templetId, @RequestBody @Valid List<ElementVo> elementVos){
        irColumnService.checkColumnAndArea(channelId,columnId);
        iTempletService.checkTemplet(templetId);

        return "成功更新"+iElementService.updateElementAll(elementVos,templetId)+"条推荐位";
    }

    @ApiOperation(value = "更新推荐位彩铃的图片",httpMethod = "POST",notes = "根据推荐位id以及彩铃id,更新图片",consumes="multipart/form-data")
    @RequestMapping(value = "/img/{channelId}/{columnId}/{templetId}/{elementId}",headers="content-type=multipart/form-data",consumes = "multipart/*", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="path", name = "channelId", value = "渠道id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "columnId", value = "栏目id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="path", name = "templetId", value = "模板id", required = true, dataType = "Long"),
                        @ApiImplicitParam(paramType="path", name = "elementId", value = "推荐位id", required = true, dataType = "Long")
    })
    @ResponseBody
    public String updateImg(@ApiParam(value = "上传的图片", required = true)
                            @RequestParam("imgFile") MultipartFile imgFile,  @PathVariable Integer channelId, @PathVariable Integer columnId, @PathVariable Integer templetId,@PathVariable Integer elementId) {
        Area area = iAreaService.checkArea(channelId);
        Integer operation = area.getOperator();
        irColumnService.checkColumn(columnId);
        iTempletService.checkTemplet(templetId);
        Element element = iElementService.getOne(new QueryWrapper<Element>().lambda().eq(Element::getElementId, elementId).eq(Element::getColumnId, templetId));
        if (element!=null){
            String contentId = element.getContentSpxlId();
            if (StringUtils.isNotEmpty(contentId)){
                //判断是否存在content_id
                ContentSpxl contentSpxl = iContentSpxlService.getOne(new QueryWrapper<ContentSpxl>().lambda().eq(ContentSpxl::getContentId,element.getContentSpxlId()).eq(ContentSpxl::getOperator,operation).last("LIMIT 1"));
                if (contentSpxl==null){
                    //无对应的彩铃内容
                    log.warn("saveElement=========>无对应的彩铃内容:模板id{}",templetId);
                    throw new EngineException(1001,"推荐位"+elementId+"无对应的彩铃内容:"+element.getContentSpxlId());
                }
                log.info("imgFile="+imgFile);
                long  gg = imgFile.getSize();
                if (imgFile.getSize()>80*1024L){
                    throw new EngineException("图片大小不得超过80kb");
                }
                String name = imgFile.getOriginalFilename();
                if (StringUtils.isNotEmpty(name)) {
                    try {
//                        BufferedImage bufferedImage =
//                                ImageIO.read(imgFile.getInputStream());
//                        int width = bufferedImage.getWidth();
//                        int height = bufferedImage.getHeight();
//                        if(width==0||height==0) {
//                            log.error("图片内容有误:"+width+height);
//                            throw new EngineException("图片内容有误");
//                        }
                        String suffix = name.substring(name.lastIndexOf(".")).toLowerCase();
//                        String targetUrl = FileUtil.createImgPath(name);
                        String targetUrl = environment.getProperty("mams.path")+FileUtil.createImgPath(name);
//                        targetUrl = "C:/Users/49 468/AppData/Local/Temp/tomcat.1992147345107226461.8088/work/Tomcat/localhost/rtb"+targetUrl;
                        log.info("tar="+targetUrl);
                        File targetFile = new File(targetUrl);
                        if (!targetFile.getParentFile().exists()) {
                            targetFile.getParentFile().mkdirs();
                        }
                        imgFile.transferTo(targetFile);
                        log.info("彩铃contentId{}的原竖图为{}",contentId,StringUtils.isEmpty(contentSpxl.getPoster())?contentSpxl.getVerPoster():contentSpxl.getPoster());
                        if (StringUtils.isEmpty(contentSpxl.getPoster())){
                            contentSpxl.setVerPoster(targetUrl);
                        }else {
                            contentSpxl.setPoster(targetUrl);
                        }
                        iContentSpxlService.updateById(contentSpxl);
                        return targetUrl;
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.error("图片保存IO错误:" + e.getMessage());
                        throw new EngineException("图片保存IO错误:"+e.getMessage());
                    }
                }else {
                    throw new EngineException(1002,"图片为空");
                }
            }else {
                throw new EngineException("该推荐位未绑定彩铃");
            }
        }else {
            throw new EngineException("无该推荐位");
        }
    }
}