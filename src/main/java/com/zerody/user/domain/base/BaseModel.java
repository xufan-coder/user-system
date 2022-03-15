package com.zerody.user.domain.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author PengQiang
 * @ClassName baseModel
 * @DateTime 2020/12/15_18:04
 * @Deacription TODO
 */
@Data
public class BaseModel {

    //主键id
    @TableId(
            value = "id",
            type = IdType.ASSIGN_UUID
    )
    private String id;

    //状态:0生效、1离职、2删除 3合作
    @TableField("status")
    private Integer status;


    //创建人id
    @TableField("create_id")
    private String createId;

    //创建人
    @TableField("create_user")
    private String createUser;

    //创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("create_time")
    private Date createTime;

    //修改人id
    @TableField("update_id")
    private String updateId;

    //修改人
    @TableField("update_user")
    private String updateUser;

    //修改时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("update_time")
    private Date updateTime;
}
