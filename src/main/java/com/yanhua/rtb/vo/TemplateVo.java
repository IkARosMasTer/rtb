/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: TemplateVo
 * Author: Emiya
 * Date: 2020/10/8 22:53
 * Description: 模板实图
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 〈功能简述〉<br>
 * 〈模板样式视图〉
 *  <p>
 * @author Emiya
 * @create 2020/10/8 22:53
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TemplateVo implements Serializable {

    private static final long serialVersionUID = 146833730039033774L;
    @ApiModelProperty(name = "templateId" , value = "模板样式id")
    private Integer templateId;

    @ApiModelProperty(name = "templateCode" , value = "模板样式标识")
    @NotNull(message = "模板样式标识不能為空")
    private String templateCode;

    @ApiModelProperty(name = "templateTitle" , value = "模板样式标题")
    @NotNull(message = "模板样式标题不能為空")
    private String templateTitle;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "updateTime" , value = "更新时间")
    private Date updateTime;

}