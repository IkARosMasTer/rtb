/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: CtccContentSpxlVo
 * Author: Emiya
 * Date: 2020/10/28 10:46
 * Description: 移动彩铃视图
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 〈功能简述〉<br>
 * 〈移动彩铃视图〉
 *  <p>
 * @author Emiya
 * @create 2020/10/28 10:46
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmccContentSpxlVo implements Serializable {
    private static final long serialVersionUID = 5066567355476927312L;
    private String contentId;
    private String spName;
    private String toneName;
    private String toneNameLetter;
    private String singerNameLetter;
    private BigDecimal price    ;
    private String toneValidDay;
    private String type;
    private String musicId;
    private List<CmccContentSpxlDetailVo> fileInfos;
    private String libraryType;
    private boolean isLocalVrbt;


}