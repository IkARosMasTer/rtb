/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: ElementVo
 * Author: Emiya
 * Date: 2020/10/6 14:38
 * Description: 元素视图
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 〈功能简述〉<br>
 * 〈推荐位（元素、专题）视图〉
 *  <p>
 * @author Emiya
 * @create 2020/10/6 14:38
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ElementVo implements Serializable {


    private static final long serialVersionUID = 2519516095728870029L;

    @ApiModelProperty(name = "templetId" , value = "模板id")
    @NotNull(message = "模板id不能為空")
    @JsonProperty("templetId")
    private Integer columnId;

    @ApiModelProperty(name = "elementId" , value = "元素（推荐位）ID")
    private Integer elementId;

    @ApiModelProperty(name = "elementName" , value = "元素（推荐位）名称")
    private String elementName;

    @ApiModelProperty(name = "elementType" , value = "元素（推荐位）类别")
    private Integer elementType;

    @ApiModelProperty(name = "templateId" , value = "元素（推荐位）模板样式id")
//    @NotNull(message = "元素（推荐位）模板样式id不能為空")
    private Integer templateId;

    @ApiModelProperty(name = "contentSpxlId" , value = "元素（推荐位）对应彩铃内容id")
    private String contentSpxlId;

    @ApiModelProperty(name = "imagesType" , value = "图片类型")
    private Integer imagesType;

    @ApiModelProperty(name = "elementImg" , value = "元素（推荐位）竖图地址")
    private String elementImg;

    @ApiModelProperty(name = "elementImgH" , value = "元素（推荐位）横图地址")
    private String elementImgH;

    @ApiModelProperty(name = "elementOrder" , value = "元素（推荐位）排序")
    private Integer elementOrder;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "updateTime" , value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(name = "eleTemplateVo",value = "元素（推荐位）的模板样式视图")
    private TemplateVo eleTemplateVo;
}