/**
 * @filename:ContentSpxlDetail 2020年10月13日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;

/**   
 * @description 联通入库文件表实体类
 *      <p/>
 * @version V1.0
 * @author Emiya
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ContentSpxlDetail extends Model<ContentSpxlDetail> {

	private static final long serialVersionUID = 1602570314473L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "主键")
	private Integer id;
    
	@ApiModelProperty(name = "contentId" , value = "内容id")
	private String contentId;
    
	@ApiModelProperty(name = "filePath" , value = "")
	private String filePath;
    
	@ApiModelProperty(name = "fileCode" , value = "")
	private String fileCode;
    
	@ApiModelProperty(name = "mime" , value = "")
	private String mime;
    
	@ApiModelProperty(name = "drmtype" , value = "")
	private String drmtype;
    
	@ApiModelProperty(name = "fileSize" , value = "")
	private Double fileSize;
    
	@ApiModelProperty(name = "filePlayPath" , value = "文件播放地址")
	private String filePlayPath;
    
	@ApiModelProperty(name = "filePlayTime" , value = "播放时长")
	private Integer filePlayTime;
    
	@ApiModelProperty(name = "lyricPath" , value = "歌词路径")
	private String lyricPath;
    
	@ApiModelProperty(name = "fileMl" , value = "")
	private String fileMl;
    
	@ApiModelProperty(name = "fileCyl" , value = "")
	private String fileCyl;
    
	@ApiModelProperty(name = "samplebitrate" , value = "频率")
	private Integer samplebitrate;
    
	@ApiModelProperty(name = "samplingrate" , value = "采样率")
	private String samplingrate;
    
	@ApiModelProperty(name = "resolution" , value = "")
	private String resolution;
    
	@ApiModelProperty(name = "aspectratio" , value = "")
	private String aspectratio;
    
	@ApiModelProperty(name = "filelhws" , value = "")
	private String filelhws;
    
	@ApiModelProperty(name = "fileSd" , value = "")
	private String fileSd;
    

}
