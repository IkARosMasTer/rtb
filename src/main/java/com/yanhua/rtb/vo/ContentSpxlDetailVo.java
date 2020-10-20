/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: ContentSpxlDetail
 * Author: Emiya
 * Date: 2020/10/16 17:59
 * Description: 视频彩铃文件详细视图
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 〈功能简述〉<br>
 * 〈视频彩铃文件详细视图〉
 *  <p>
 * @author Emiya
 * @create 2020/10/16 17:59
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ContentSpxlDetailVo implements Serializable {

    private static final long serialVersionUID = -594743203477113659L;

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