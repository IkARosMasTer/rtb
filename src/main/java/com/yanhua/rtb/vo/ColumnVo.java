/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: ColumnVo
 * Author: Emiya
 * Date: 2020/10/6 14:25
 * Description: 栏目类视图
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 〈功能简述〉<br>
 * 〈栏目类视图〉
 *  <p>
 * @author Emiya
 * @create 2020/10/6 14:25
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ColumnVo implements Serializable {

    private static final long serialVersionUID = 8739439109614289057L;

    @ApiModelProperty(name = "columnId" , value = "栏目id")
    private Integer columnId;

    @ApiModelProperty(name = "templateId" , value = "栏目模板id")
//    @NotNull(message = "模板id不能為空")
    private Integer templateId;

    @ApiModelProperty(name = "areaId" , value = "地区id")
    @NotNull(message = "地區id不能為空")
    private Integer areaId;

    @ApiModelProperty(name = "bgPhoto" , value = "栏目图片地址")
    private String bgPhoto;

    @ApiModelProperty(name = "columnName" , value = "栏目名称")
    private String columnName;

    @ApiModelProperty(name = "parColumnId" , value = "栏目父id")
    private Integer parColumnId;

    @ApiModelProperty(name = "level" , value = "级别")
    private String level;

    @ApiModelProperty(name = "columnOrder" , value = "顺序")
    private Integer columnOrder;

    @ApiModelProperty(name = "subtitle" , value = "副标题")
    private String subtitle;

    @ApiModelProperty(name = "backgroundImg" , value = "背景图片")
    private String backgroundImg;

    @ApiModelProperty(name = "colTemplateVo" , value = "栏目的模板样式视图")
    private TemplateVo colTemplateVo;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "updateTime" , value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(name = "templetVoList" , value = "模板列表")
    private List<TempletVo> templetVoList;


}