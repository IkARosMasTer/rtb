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
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanhua.rtb.entity.ContentSpxlDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    private Long copyrightId;

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

    @ApiModelProperty(name = "fileList" , value = "彩铃文件数组")
    private List<ContentSpxlDetailVo> fileList;
}