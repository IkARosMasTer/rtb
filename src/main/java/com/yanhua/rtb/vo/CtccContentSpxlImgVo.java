/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: CtccContentSpxlDetailVo
 * Author: Emiya
 * Date: 2020/11/6 11:59
 * Description: 电信彩铃文件视图
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
 * 〈电信彩铃图片视图〉
 *  <p>
 * @author Emiya
 * @create 2020/11/6 11:59
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CtccContentSpxlImgVo implements Serializable {
    private static final long serialVersionUID = 2558543676389961655L;
    private String path;
    private String format;
    private Integer type;
}