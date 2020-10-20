/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: SpecialVo
 * Author: Emiya
 * Date: 2020/10/6 14:33
 * Description: 推荐位视图
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yanhua.rtb.entity.Template;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Persistent;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 〈功能简述〉<br>
 * 〈推荐位（元素、专题）视图〉
 *  <p>
 * @author Emiya
 * @create 2020/10/6 14:33
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SpecialVo implements Serializable {


    private static final long serialVersionUID = -3484401362288121642L;

    @ApiModelProperty(name = "templetId" , value = "模板id")
    @NotNull(message = "模板id不能為空")
    @JsonProperty("templetId")
    private Integer columnId;

    @ApiModelProperty(name = "specialId" , value = "推荐位ID")
    @JsonProperty("specialId")
    private Integer elementId;

    @ApiModelProperty(name = "contentId" , value = "推荐位内容ID")
    @NotNull(message = "推荐位内容id不能為空")
    private Integer contentId;

    @ApiModelProperty(name = "specialName" , value = "推荐位标题")
    @JsonProperty("specialName")
    private String elementName;

    @ApiModelProperty(name = "specialType" , value = "推荐位类别")
    @JsonProperty("specialType")
    private Integer elementType;

    @ApiModelProperty(name = "templateId" , value = "推荐位模板样式id")
//    @NotNull(message = "推荐位模板样式id不能為空")
    private Integer templateId;

    @ApiModelProperty(name = "imagesType" , value = "图片类型")
    private Integer imagesType;

    @ApiModelProperty(name = "specialImg" , value = "推荐位竖图地址")
    @JsonProperty("specialImg")
    private String elementImg;

    @ApiModelProperty(name = "specialImgH" , value = "推荐位横图地址")
    @JsonProperty("specialImgH")
    private String elementImgH;

    @ApiModelProperty(name = "specialOrder" , value = "推荐位排序")
    @JsonProperty("specialOrder")
    private Integer elementOrder;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "updateTime" , value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(name = "speTemplateVo",value = "推荐位模板")
    private TemplateVo speTemplateVo;

//    @ApiModelProperty(name = "specialId" , value = "推荐位id")
//    private Integer columnId;
//
//    @JsonProperty("specialId")
//    public Integer getColumnId() {
//        return columnId;
//    }
//
//    @JsonProperty("specialId")
//    public void setColumnId(Integer columnId) {
//        this.columnId = columnId;
//    }
//
//    @JsonProperty("specialOrder")
//    public Integer getColumnOrder() {
//        return columnOrder;
//    }
//
//    @JsonProperty("specialOrder")
//    public void setColumnOrder(Integer columnOrder) {
//        this.columnOrder = columnOrder;
//    }
//
//    @ApiModelProperty(name = "templateId" , value = "栏目模板id")
//    @NotNull(message = "模板id不能為空")
//    private Integer templateId;
//
//    @ApiModelProperty(name = "areaId" , value = "地区id")
//    @NotNull(message = "推荐位地区id不能為空")
//    private Integer areaId;
//
//    @ApiModelProperty(name = "bgPhoto" , value = "栏目图片地址")
//    private String bgPhoto;
//
//    @ApiModelProperty(name = "columnName" , value = "栏目名称")
//    private String columnName;
//
//    @ApiModelProperty(name = "parColumnId" , value = "栏目父id")
//    @NotNull(message = "推荐位栏目父id不能為空")
//    private Integer parColumnId;
//
//    @ApiModelProperty(name = "level" , value = "级别")
//    private String level;
//
//    @ApiModelProperty(name = "columnOrder" , value = "顺序")
//    private Integer columnOrder;
//
//    @ApiModelProperty(name = "subtitle" , value = "副标题")
//    private String subtitle;
//
//    @ApiModelProperty(name = "backgroundImg" , value = "背景图片")
//    private String backgroundImg;
//
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    @ApiModelProperty(name = "createTime" , value = "创建时间")
//    private Date createTime;
//
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    @ApiModelProperty(name = "updateTime" , value = "更新时间")
//    private Date updateTime;


    @JsonIgnore
    @Persistent
    private Template speTemplate;

//    @ApiModelProperty(name = "elementList" , value = "元素列表")
//    private List<ElementVo> elementList;


}