/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: ContentSpxlVo
 * Author: Emiya
 * Date: 2020/10/16 17:52
 * Description: 视频彩铃视图
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanhua.rtb.util.CustomForEach;
import com.yanhua.rtb.util.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 〈功能简述〉<br>
 * 〈视频彩铃视图〉
 *  <p>
 * @author Emiya
 * @create 2020/10/16 17:52
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ContentSpxlVo implements Serializable {


    private static final long serialVersionUID = -1732503146184940974L;

    @ApiModelProperty(name = "id" , value = "主键")
    private Integer id;

    @ApiModelProperty(name = "serialNo" , value = "")
    private String serialNo;

    @ApiModelProperty(name = "operation" , value = "")
    private String operation;

    @ApiModelProperty(name = "contentId" , value = "内容id")
    private String contentId;

    @ApiModelProperty(name = "copyrightId" , value = "版权id")
    private String copyrightId;

    @ApiModelProperty(name = "songId" , value = "彩铃id")
    private Integer songId;

    @ApiModelProperty(name = "contentName" , value = "内容名称")
    private String contentName;

    @ApiModelProperty(name = "spid" , value = "")
    private Integer spid;

    @ApiModelProperty(name = "contType" , value = "")
    private Integer contType;

    @ApiModelProperty(name = "para1" , value = "")
    private String para1;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "publishTime" , value = "完成时间")
//    @JSONField(format = "yyyyMMddHHmmss")
    private Date publishTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "validDate" , value = "有效日期")
//    @JSONField(format = "yyyyMMdd")
    private Date validDate;

    @ApiModelProperty(name = "grantType" , value = "赠款类型")
    private String grantType;

    @ApiModelProperty(name = "useArea" , value = "使用地区")
    private String useArea;

    @ApiModelProperty(name = "spreadChannel" , value = "传播渠道")
    private String spreadChannel;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "grantTimes" , value = "授予时间")
//    @JSONField(format = "yyyyMMdd")
    private Date grantTimes;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
//    @JSONField(format = "yyyyMMdd")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "endDate" , value = "结束日期")
//    @JSONField(format = "yyyyMMdd")
    private Date endDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "onlineDate" , value = "在线日期")
//    @JSONField(format = "yyyyMMdd")
    private Date onlineDate;

    @ApiModelProperty(name = "format" , value = "格式")
    private String format;

    @ApiModelProperty(name = "feeType" , value = "费用类型")
    private Integer feeType;

    @ApiModelProperty(name = "hightestPrice" , value = "最高价")
    private BigDecimal hightestPrice;

    @ApiModelProperty(name = "lowestPrice" , value = "最低价")
    private BigDecimal lowestPrice;

    @ApiModelProperty(name = "suggestPrice" , value = "建议价")
    private BigDecimal suggestPrice;

    @ApiModelProperty(name = "discount" , value = "折扣")
    private String discount;

    @ApiModelProperty(name = "discountDesc" , value = "")
    private String discountDesc;

    @ApiModelProperty(name = "settlementParam" , value = "")
    private String settlementParam;

    @ApiModelProperty(name = "discountSettlementParam" , value = "")
    private String discountSettlementParam;

    @ApiModelProperty(name = "range" , value = "")
    private String spxlRange;

    //	@JSONField(name = "rang")
    public String getSpxlRange() {
        return spxlRange;
    }

    @JSONField(name = "rang")
    public void setSpxlRange(String spxlRange) {
        this.spxlRange = spxlRange;
    }

    @ApiModelProperty(name = "terminalType" , value = "终端类型")
    private String terminalType;

    @ApiModelProperty(name = "channelAbility" , value = "渠道")
    private String channelAbility;

    @ApiModelProperty(name = "contLevel" , value = "")
    private String contLevel;

    @ApiModelProperty(name = "listenFilePath" , value = "")
    private String listenFilePath;

    @ApiModelProperty(name = "sendFilePath1" , value = "")
    private String sendFilePath1;

    @ApiModelProperty(name = "label" , value = "标签")
    private String label;

    @ApiModelProperty(name = "price" , value = "")
    private Integer price;

    @ApiModelProperty(name = "spproductid" , value = "")
    private String spproductid;

    @ApiModelProperty(name = "single" , value = "")
    private String single;

    @ApiModelProperty(name = "ability" , value = "")
    private String ability;

    @ApiModelProperty(name = "sendFilePath2" , value = "")
    private String sendFilePath2;

//    @JSONField(name = "sendfilepath2")
    public String getSendFilePath2() {
        return sendFilePath2;
    }
    @JSONField(name = "sendfilepath2")
    public void setSendFilePath2(String sendFilePath2) {
        this.sendFilePath2 = sendFilePath2;
    }

    @ApiModelProperty(name = "foreShow" , value = "")
    private String foreShow;

    @ApiModelProperty(name = "wapicon" , value = "")
    private String wapicon;

    @ApiModelProperty(name = "wwwicon" , value = "")
    private String wwwicon;

    @ApiModelProperty(name = "poster" , value = "")
    private String poster;

    @ApiModelProperty(name = "verPoster" , value = "")
    private String verPoster;

    @ApiModelProperty(name = "screenshot" , value = "截图屏幕")
    private String screenshot;

    @ApiModelProperty(name = "descripiton" , value = "简介")
    private String descripiton;

    @ApiModelProperty(name = "sourceCompany" , value = "源公司")
    private String sourceCompany;

    @ApiModelProperty(name = "sourceCpId" , value = "")
    private String sourceCpId;

    @ApiModelProperty(name = "videoLevel" , value = "")
    private String videoLevel;

    @ApiModelProperty(name = "cpId" , value = "")
    private Integer cpId;

    @ApiModelProperty(name = "attribution",value = "归属：0,无;1,岩华;9,其他")
    private Integer attribution;

    @ApiModelProperty(name = "operator",value = "运营商（1：移动；2：联通：3：电信）")
    private Integer operator;

    @ApiModelProperty(name = "fileList" , value = "彩铃文件数组")
    private List<ContentSpxlDetailVo> fileList;

    @ApiModelProperty(name = "status" , value = "状态（0：商用；1：过期）")
    private Integer status;

    public ContentSpxlVo() {
    }

    public ContentSpxlVo(CmccContentSpxlVo cmccContentSpxlVo, String label, String copyrightId) throws ParseException {

        this.contentId = cmccContentSpxlVo.getContentId();
        this.contentName = cmccContentSpxlVo.getToneName();
        this.suggestPrice = cmccContentSpxlVo.getPrice();
        this.validDate = DateUtils.string2Date(cmccContentSpxlVo.getToneValidDay(),"yyyy-MM-dd");
        if (validDate.before(new Date())){
            //过期版权
            this.status = 1;
        }else {
            this.status = 0;
        }
        this.copyrightId = cmccContentSpxlVo.getMusicId();
        this.label = label;
        //来源
        this.attribution = "699052".equals(StringUtils.substring(copyrightId,0,6))?1:9;
        //cpId
        this.cpId = Integer.valueOf(StringUtils.substring(copyrightId,0,6));
        List<CmccContentSpxlDetailVo> cmccContentSpxlDetailVos = cmccContentSpxlVo.getFileInfos();
        if (cmccContentSpxlDetailVos !=null&& cmccContentSpxlDetailVos.size()>0){
            List<ContentSpxlDetailVo> list = new ArrayList<>();
            //处理图片
            CustomForEach.forEach(cmccContentSpxlDetailVos.stream().filter(cmccContentSpxlDetailVo -> cmccContentSpxlDetailVo !=null&&("113".equals(cmccContentSpxlDetailVo.getType())||"111".equals(cmccContentSpxlDetailVo.getType()))),
                    (ctcc,breaker) ->{
                        if ("113".equals(ctcc.getType())&& StringUtils.isNotEmpty(ctcc.getFilePath())){
                            this.verPoster = ctcc.getFilePath();
                            breaker.stop();
                        }else if ("111".equals(ctcc.getType())){
                            this.verPoster = ctcc.getFilePath();
                        }
                    });
            //处理视频
            CustomForEach.forEach(cmccContentSpxlDetailVos.stream().filter(cmccContentSpxlDetailVo -> cmccContentSpxlDetailVo !=null&&("3".equals(cmccContentSpxlDetailVo.getType())||"5".equals(cmccContentSpxlDetailVo.getType()))),
                    (ctcc,breaker) ->{
                        ContentSpxlDetailVo contentSpxlDetailVo = new ContentSpxlDetailVo();
                        contentSpxlDetailVo.setContentId(cmccContentSpxlVo.getContentId());
                        contentSpxlDetailVo.setFilePath(ctcc.getFilePath());
                        list.add(contentSpxlDetailVo);
                    });
            this.fileList = list;
        }
    }
    public ContentSpxlVo(CtccContentSpxlVo ctccContentSpxlVo, String label, String copyrightId) throws ParseException {

        this.contentId = ctccContentSpxlVo.getRingId();
        this.contentName = ctccContentSpxlVo.getVideoName();
        this.suggestPrice = ctccContentSpxlVo.getPrice();
        this.validDate = DateUtils.string2Date(ctccContentSpxlVo.getExpirationDate(), "yyyy-MM-dd");
        if (validDate.before(new Date())){
            //过期版权
            this.status = 1;
        }else {
            this.status = 0;
        }
        this.copyrightId = ctccContentSpxlVo.getResourceId() == null ? copyrightId : ctccContentSpxlVo.getResourceId();
        this.label = label;
        //来源 岩华科技
        this.attribution = 1;
//        this.cpId = Integer.valueOf(StringUtils.substring(copyrightId,0,6));
        List<CtccContentSpxlImgVo> ctccContentSpxlImgVos = ctccContentSpxlVo.getImageList();
        if (ctccContentSpxlImgVos != null && ctccContentSpxlImgVos.size() > 0) {
            //处理图片
            CustomForEach.forEach(ctccContentSpxlImgVos.stream(),
                    (ctcc, breaker) -> {
                        if (ctcc != null && ctcc.getType() == 2 && StringUtils.isNotEmpty(ctcc.getPath()) && "jpg".equals(ctcc.getFormat())) {
                            this.verPoster = ctcc.getPath();
                            breaker.stop();
                        }
                    });
        }
        List<ContentSpxlDetailVo> list = new ArrayList<>();
        List<CtccContentSpxlDetailVo> ctccContentSpxlDetailVos = ctccContentSpxlVo.getFileList();
        if (ctccContentSpxlDetailVos != null && ctccContentSpxlDetailVos.size() > 0) {
            //竖屏分辨率
            int[] ints = {1, 7, 8, 9, 13,14};
            //横屏分辨率
            int[] ints1 = {2,3,4,5,6,10,12};

            //处理视频
            Supplier<Stream<CtccContentSpxlDetailVo>> spxlDetailVoStream = () -> ctccContentSpxlDetailVos.stream().filter(ctccContentSpxlDetailVo -> ctccContentSpxlDetailVo != null && ArrayUtils.contains(ints, ctccContentSpxlDetailVo.getQuality())).sorted(Comparator.reverseOrder());
            Supplier<Stream<CtccContentSpxlDetailVo>> spxlDetailVoStream2 = () -> ctccContentSpxlDetailVos.stream().filter(ctccContentSpxlDetailVo -> ctccContentSpxlDetailVo != null && ArrayUtils.contains(ints1, ctccContentSpxlDetailVo.getQuality())).sorted(Comparator.reverseOrder());

            //订购类型
            //竖屏
            List<CtccContentSpxlDetailVo> ctccContentSpxlDetailVos1 = spxlDetailVoStream.get().filter(ctccContentSpxlDetailVo -> ctccContentSpxlDetailVo.getType() == 1).collect(Collectors.toList());
//            if (ctccContentSpxlDetailVos1.size() <1) {
//                ctccContentSpxlDetailVos1 = ctccContentSpxlDetailVos.stream().filter(ctccContentSpxlDetailVo2 -> ctccContentSpxlDetailVo2 != null && ctccContentSpxlDetailVo2.getType()==1&&ctccContentSpxlDetailVo2.getQuality()==14).collect(Collectors.toList());
//            }
            if (ctccContentSpxlDetailVos1.size()>0){
                CtccContentSpxlDetailVo ctccContentSpxlDetailVo = ctccContentSpxlDetailVos1.get(0);
                ContentSpxlDetailVo contentSpxlDetailVo = new ContentSpxlDetailVo();
                contentSpxlDetailVo.setContentId(ctccContentSpxlVo.getRingId());
                contentSpxlDetailVo.setFilePath(ctccContentSpxlDetailVo.getFilePath());
                contentSpxlDetailVo.setFileSize(ctccContentSpxlDetailVo.getFileSize().doubleValue());
                contentSpxlDetailVo.setFilePlayTime(ctccContentSpxlDetailVo.getDuration().intValue());
                contentSpxlDetailVo.setFileCode(ctccContentSpxlDetailVo.getVideoFormat());
                contentSpxlDetailVo.setSamplebitrate(ctccContentSpxlDetailVo.getVideoBitrate());
                contentSpxlDetailVo.setSamplingrate(ctccContentSpxlDetailVo.getAudioRate().toString());
                contentSpxlDetailVo.setResolution(ctccContentSpxlDetailVo.getChannel() == 1 ? "单声道" : "双声道");
                list.add(contentSpxlDetailVo);
            }
            //横屏
            List<CtccContentSpxlDetailVo> ctccContentSpxlDetailVos3 = ctccContentSpxlDetailVos.stream().filter(ctccContentSpxlDetailVo -> ctccContentSpxlDetailVo != null &&ctccContentSpxlDetailVo.getType() == 1&&ctccContentSpxlDetailVo.getQuality()==11).collect(Collectors.toList());
            if (ctccContentSpxlDetailVos3.size() <1) {
                //订购类型
                ctccContentSpxlDetailVos3 = spxlDetailVoStream2.get().filter(ctccContentSpxlDetailVo -> ctccContentSpxlDetailVo.getType() == 1).collect(Collectors.toList());
            }
            if (ctccContentSpxlDetailVos3.size()>0){
                CtccContentSpxlDetailVo ctccContentSpxlDetailVo = ctccContentSpxlDetailVos3.get(0);
                ContentSpxlDetailVo contentSpxlDetailVo = new ContentSpxlDetailVo();
                contentSpxlDetailVo.setContentId(ctccContentSpxlVo.getRingId());
                contentSpxlDetailVo.setFilePath(ctccContentSpxlDetailVo.getFilePath());
                contentSpxlDetailVo.setFileSize(ctccContentSpxlDetailVo.getFileSize().doubleValue());
                contentSpxlDetailVo.setFilePlayTime(ctccContentSpxlDetailVo.getDuration().intValue());
                contentSpxlDetailVo.setFileCode(ctccContentSpxlDetailVo.getVideoFormat());
                contentSpxlDetailVo.setSamplebitrate(ctccContentSpxlDetailVo.getVideoBitrate());
                contentSpxlDetailVo.setSamplingrate(ctccContentSpxlDetailVo.getAudioRate().toString());
                contentSpxlDetailVo.setResolution(ctccContentSpxlDetailVo.getChannel() == 1 ? "单声道" : "双声道");
                list.add(contentSpxlDetailVo);
            }
            //试看类型
            List<CtccContentSpxlDetailVo> ctccContentSpxlDetailVos2 = spxlDetailVoStream.get().filter(ctccContentSpxlDetailVo -> ctccContentSpxlDetailVo.getType() == 2).collect(Collectors.toList());
//            if (ctccContentSpxlDetailVos2.size() <1) {
//                ctccContentSpxlDetailVos2 = ctccContentSpxlDetailVos.stream().filter(ctccContentSpxlDetailVo2 -> ctccContentSpxlDetailVo2 != null && ctccContentSpxlDetailVo2.getType()==2&&ctccContentSpxlDetailVo2.getQuality()==14).collect(Collectors.toList());
//            }
            if (ctccContentSpxlDetailVos2.size() > 0) {
                CtccContentSpxlDetailVo ctccContentSpxlDetailVo = ctccContentSpxlDetailVos2.get(0);
                ContentSpxlDetailVo contentSpxlDetailVo = new ContentSpxlDetailVo();
                contentSpxlDetailVo.setContentId(ctccContentSpxlVo.getRingId());
                contentSpxlDetailVo.setFilePath(ctccContentSpxlDetailVo.getFilePath());
                contentSpxlDetailVo.setFileSize(ctccContentSpxlDetailVo.getFileSize().doubleValue());
                contentSpxlDetailVo.setFilePlayTime(ctccContentSpxlDetailVo.getDuration().intValue());
                contentSpxlDetailVo.setFileCode(ctccContentSpxlDetailVo.getVideoFormat());
                contentSpxlDetailVo.setSamplebitrate(ctccContentSpxlDetailVo.getVideoBitrate());
                contentSpxlDetailVo.setSamplingrate(ctccContentSpxlDetailVo.getAudioRate().toString());
                contentSpxlDetailVo.setResolution(ctccContentSpxlDetailVo.getChannel() == 1 ? "单声道" : "双声道");
                list.add(contentSpxlDetailVo);
            }
            //横屏
            List<CtccContentSpxlDetailVo> ctccContentSpxlDetailVos4 = ctccContentSpxlDetailVos.stream().filter(ctccContentSpxlDetailVo -> ctccContentSpxlDetailVo != null &&ctccContentSpxlDetailVo.getType() == 2&&ctccContentSpxlDetailVo.getQuality()==11).collect(Collectors.toList());
            if (ctccContentSpxlDetailVos4.size() <1) {
                //订购类型
                ctccContentSpxlDetailVos4 = spxlDetailVoStream2.get().filter(ctccContentSpxlDetailVo -> ctccContentSpxlDetailVo.getType() == 2).collect(Collectors.toList());
            }
            if (ctccContentSpxlDetailVos4.size()>0){
                CtccContentSpxlDetailVo ctccContentSpxlDetailVo = ctccContentSpxlDetailVos4.get(0);
                ContentSpxlDetailVo contentSpxlDetailVo = new ContentSpxlDetailVo();
                contentSpxlDetailVo.setContentId(ctccContentSpxlVo.getRingId());
                contentSpxlDetailVo.setFilePath(ctccContentSpxlDetailVo.getFilePath());
                contentSpxlDetailVo.setFileSize(ctccContentSpxlDetailVo.getFileSize().doubleValue());
                contentSpxlDetailVo.setFilePlayTime(ctccContentSpxlDetailVo.getDuration().intValue());
                contentSpxlDetailVo.setFileCode(ctccContentSpxlDetailVo.getVideoFormat());
                contentSpxlDetailVo.setSamplebitrate(ctccContentSpxlDetailVo.getVideoBitrate());
                contentSpxlDetailVo.setSamplingrate(ctccContentSpxlDetailVo.getAudioRate().toString());
                contentSpxlDetailVo.setResolution(ctccContentSpxlDetailVo.getChannel() == 1 ? "单声道" : "双声道");
                list.add(contentSpxlDetailVo);
            }

            this.fileList = list;
        }
    }
}