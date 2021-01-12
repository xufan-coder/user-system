package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
/**
 * @author  DaBai
 * @date  2021/1/12 17:40
 */

@Data
public class CardUserInfo {
    @TableId(type = IdType.UUID)
    private String id;

    private String userName;

    private String phoneNumber;

    private String userPwd;

    private String openId;

    private String unionId;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private Integer status;

    private String remark;

}