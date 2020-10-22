/**
 * @filename:Element 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanhua.rtb.vo.TemplateVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**   
 * @description 元素（推荐位）表实体类
 *      <p/>
 * @version V1.0
 * @author Emiya
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Element extends Model<Element> {

	private static final long serialVersionUID = 1601732870472L;
	
	@ApiModelProperty(name = "columnId" , value = "模板id")
	private Integer columnId;
    
	@TableId(value = "element_id", type = IdType.AUTO)
	@ApiModelProperty(name = "elementId" , value = "元素（推荐位）ID")
	private Integer elementId;
    
	@ApiModelProperty(name = "elementName" , value = "元素（推荐位）名称")
	private String elementName;
    
	@ApiModelProperty(name = "elementType" , value = "元素（推荐位）类别")
	private Integer elementType;
    
	@ApiModelProperty(name = "templateId" , value = "元素（推荐位）模板")
	private Integer templateId;

	@ApiModelProperty(name = "contentSpxlId" , value = "元素（推荐位）对应彩铃内容id")
	private String contentSpxlId;
    
	@ApiModelProperty(name = "imagesType" , value = "图片类型")
	private Integer imagesType;
    
	@ApiModelProperty(name = "elementImg" , value = "元素（推荐位）竖图地址")
	private String elementImg;

	@ApiModelProperty(name = "order" , value = "元素（推荐位）排序")
	private Integer elementOrder;
    
	@ApiModelProperty(name = "elementImgH" , value = "元素（推荐位）横图地址")
	private String elementImgH;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "updateTime" , value = "更新时间")
	private Date updateTime;

	@TableField(exist = false)
	private TemplateVo eleTemplateVo;
	@TableField(exist = false)
	private Template eleTemplate;


    

}
