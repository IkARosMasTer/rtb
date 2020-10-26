/**
 * @filename:Area2 2020年10月13日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**   
 * @description 渠道表实体类
 *      <p/>
 * @version V1.0
 * @author Emiya
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Area extends Model<Area> {

	private static final long serialVersionUID = 1603440869624L;

	@TableId(value = "channel_id", type = IdType.AUTO)
	@ApiModelProperty(name = "channelId" , value = "渠道主键id")
	//	@NotNull(message = "渠道id不能為空")//加了notempty这个反而强制要求areaid是int，本来string是可以的
	private Integer channelId;
    
	@ApiModelProperty(name = "channelCode" , value = "渠道标识")
	@NotEmpty(message = "渠道标识不能為空")
	private String channelCode;
    
	@ApiModelProperty(name = "operator" , value = "运营商（1：移动；2：联通：3：电信）")
	@NotNull(message = "运营商不能為空")
	private Integer operator;
    
	@ApiModelProperty(name = "areaTitle" , value = "区域标题")
	private String areaTitle;
    
	@ApiModelProperty(name = "areaCode" , value = "区域标识")
	@NotNull(message = "区域标识不能為空")
	private Integer areaCode;
    
	@ApiModelProperty(name = "company" , value = "渠道公司名称")
	private String company;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "updateTime" , value = "更新时间")
	private Date updateTime;

	@TableLogic(value = "0", delval = "1")
	@ApiModelProperty(name = "status" , value = "状态（0：可用；1：不可用）")
	private Integer status;
    

}
