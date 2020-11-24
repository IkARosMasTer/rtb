/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: CtccContentSpxlVo
 * Author: Emiya
 * Date: 2020/11/6 11:57
 * Description: 电信彩铃视图
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 〈功能简述〉<br>
 * 〈电信彩铃视图〉
 *  <p>
 * @author Emiya
 * @create 2020/11/6 11:57
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CtccContentSpxlVo  implements Serializable {

    private static final long serialVersionUID = 4240866164766323119L;
    private String videoName;
    private String actorName;
    private String resourceId;
    private String ringId;
    private BigDecimal price    ;
    private String expirationDate;
    private List<CtccContentSpxlImgVo> imageList;
    private List<CtccContentSpxlDetailVo> fileList;


}