/**
 * @filename:RColumn 2020年10月01日
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
import com.yanhua.rtb.vo.ElementVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**   
 * @description 栏目表实体类
 *      <p/>
 * @version V1.0
 * @author Emiya
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RColumn extends Model<RColumn> {

	private static final long serialVersionUID = 1602213497617L;

	@TableId(value = "column_id", type = IdType.AUTO)
	@ApiModelProperty(name = "columnId" , value = "栏目id")
	private Integer columnId;

	@ApiModelProperty(name = "templateId" , value = "栏目模板id")
	private Integer templateId;

	@ApiModelProperty(name = "channelId" , value = "地区id")
	private Integer channelId;

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

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "updateTime" , value = "更新时间")
	private Date updateTime;

	@TableField(exist = false)
	@ApiModelProperty(name = "elementVoList" , value = "推荐位视图数组")
	private List<ElementVo> elementVoList;

	@TableField(exist = false)
	@ApiModelProperty(name = "elementList" , value = "推荐位数组")
	private List<Element> elementList;

	@TableField(exist = false)
	@ApiModelProperty(name = "templetList" , value = "模板数组")
	private List<RColumn> templetList;

	@TableField(exist = false)
	private Template speTemplate;
    

}
