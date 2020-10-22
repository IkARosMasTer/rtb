/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: templetVo
 * Author: Emiya
 * Date: 2020/10/15 9:39
 * Description: 模板（二级栏目）视图
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
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 〈功能简述〉<br>
 * 〈模板（二级栏目）视图〉
 *  <p>
 * @author Emiya
 * @create 2020/10/15 9:39
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TempletVo implements Serializable {


    private static final long serialVersionUID = -4349206529549966780L;
    @ApiModelProperty(name = "templetId" , value = "模板id")
    @JsonProperty("templetId")
    private Integer columnId;

    @ApiModelProperty(name = "templateId" , value = "模板的模板样式id")
    @NotNull(message = "模板的模板样式id不能为空")
    private Integer templateId;

    @ApiModelProperty(name = "areaId" , value = "地区id")
    @NotNull(message = "地區id不能為空")
    private Integer areaId;

    @ApiModelProperty(name = "bgPhoto" , value = "模板图片地址")
    private String bgPhoto;

    @ApiModelProperty(name = "templetName" , value = "模板标题")
    @JsonProperty("templetName")
    @NotEmpty(message = "模板标题不能为空")
    private String columnName;

    @ApiModelProperty(name = "parColumnId" , value = "模板父id（栏目）")
    private Integer parColumnId;

    @ApiModelProperty(name = "level" , value = "级别")
    private String level;

    @ApiModelProperty(name = "templetOrder" , value = "模板顺序")
    @JsonProperty("templetOrder")
    private Integer columnOrder;

    @ApiModelProperty(name = "subtitle" , value = "副标题")
    private String subtitle;

    @ApiModelProperty(name = "backgroundImg" , value = "背景图片")
    private String backgroundImg;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "updateTime" , value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(name = "elementVoList" , value = "推荐位列表")
    @JsonProperty("elementVoList")
    private List<ElementVo> elementVoList;

    @ApiModelProperty(name = "temTemplateVo" , value = "模板的模板样式视图")
    private TemplateVo temTemplateVo;

//    @ApiModelProperty(name = "temTemplate" , value = "模板的模板样式视图")
    @JsonIgnore
    private Template temTemplate;


}