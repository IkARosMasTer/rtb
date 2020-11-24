/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: CtccContentSpxlDetailVo
 * Author: Emiya
 * Date: 2020/11/6 15:58
 * Description: 电信彩铃文件视图
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 〈功能简述〉<br>
 * 〈电信彩铃文件视图〉
 *  <p>
 * @author Emiya
 * @create 2020/11/6 15:58
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CtccContentSpxlDetailVo implements Serializable ,Comparable<CtccContentSpxlDetailVo>{
    private static final long serialVersionUID = -402290661622212494L;
    @JSONField(name = "audio_format")
    private String audioFormat;
    @JSONField(name = "video_format")
    private String videoFormat;
    @JSONField(name = "audio_bitrate")
    private String audioBitrate;
    @JSONField(name = "file_size")
    private Long fileSize;
    private Integer quality;
    private Long duration;
    @JSONField(name = "audio_rate")
    private Integer audioRate;
    @JSONField(name = "video_bitrate")
    private Integer videoBitrate;
    @JSONField(name = "file_format")
    private String fileFormat;
    private Integer type;
    @JSONField(name = "video_frame_rate")
    private Integer videoFrameRate;
    private Integer channel;
    private String filePath;

    @Override
    public int compareTo(CtccContentSpxlDetailVo o) {
        return this.quality.compareTo(o.getQuality());
    }
}