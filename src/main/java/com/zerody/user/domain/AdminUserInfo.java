package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
/**
 * @author  DaBai
 * @date  2021/1/11 9:56
 */
@Data
public class AdminUserInfo {
    @TableId(type = IdType.UUID)
    private String id;

    private String userName;

    private String avatar;

    private String phoneNumber;

    private String userPwd;

    private String staffId;

    private Integer deleted;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private Byte status;

    private String remark;


    /** 是否能查看完整的手机号 */
    private Integer isShowMobile;
}