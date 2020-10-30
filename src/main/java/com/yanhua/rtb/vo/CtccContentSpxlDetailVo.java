/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: CtccContentSpxlDetailVo
 * Author: Emiya
 * Date: 2020/10/28 10:54
 * Description: 视频彩铃文件视图
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 〈功能简述〉<br>
 * 〈视频彩铃文件视图〉
 *  <p>
 * @author Emiya
 * @create 2020/10/28 10:54
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CtccContentSpxlDetailVo implements Serializable {


    private static final long serialVersionUID = -8000638540062179536L;

    private String resourceId;
    private String type;
    private String fileSize;
    private String filePath;;
}