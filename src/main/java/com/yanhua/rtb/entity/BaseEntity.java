/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: BaseEntity
 * Author: Emiya
 * Date: 2020/9/30 16:03
 * Description: 基础pojo类
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 〈功能简述〉<br>
 * 〈基础pojo类〉
 *  <p>
 * @author Emiya
 * @create 2020/9/30 16:03
 * @version 1.0.0
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 5761434796892079266L;

    @TableId(type = IdType.AUTO )
    private Integer id;
    @TableField(value = "create_time" )
    private Date createTime;
    @TableField(value = "update_time" ,updateStrategy = FieldStrategy.NOT_EMPTY)
    private Date updateTime;
//    @TableField(value = "delete_time")
//    private Date deleteTime;


}