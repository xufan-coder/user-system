package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.domain.base.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author  DaBai
 * @date  2021/9/10 15:06
 */

@Data
public class UserMenu {

    /** id **/
    @TableId(
            value = "id",
            type = IdType.UUID
    )
    private String id;
    /** 用户id **/
    private String userId;
    /** 用户名 **/
    private String userName;

    /** 菜单json **/
    private String menuJson;
    /** 创建时间 **/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    /** 修改时间 **/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    /**类型：1小程序 2APP'**/
    private Integer type;
}
