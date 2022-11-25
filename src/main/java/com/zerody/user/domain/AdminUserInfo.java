package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 后台管理员账户表
 *
 * @author DaBai
 * @date 2021/1/11 9:56
 */
@Data
public class AdminUserInfo {

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 姓名
     */
    private String userName;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 密码
     */
    private String userPwd;
    /**
     * 是否删除(0存在 1删除)
     */
    private String staffId;
    /**
     *
     */
    private Integer deleted;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 状态
     */
    private Byte status;
    /**
     * 备注
     */
    private String remark;

    /**
     * 是否能查看完整的手机号
     */
    private Integer isShowMobile;
}