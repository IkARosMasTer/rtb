package com.yanhua.rtb.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Emiya
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user")
public class User extends BaseEntity {
    private static final long serialVersionUID = -9132096439315300502L; //,Model<User>

//    private Integer id;

    private String userName;

    private String password;

//    @TableLogic(value = "0", delval = "1")
//    private Integer del;

//    @Version
//    @TableField(value="version", fill = FieldFill.INSERT, update="%s+1")
//    protected Integer version;


}
