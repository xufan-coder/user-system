package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
/**
 * 名片小程序用户信息
 *
 * @author  DaBai
 * @date  2021/1/12 17:40
 */

@Data
public class CardUserInfo {
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 密码
     */
    private String userPwd;

    /**
     * openId
     */
    private String openId;
    /**
     * 微信union_id
     */
    private String unionId;
    /**
     * 创建者
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
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 注册时的openID[不可变]
     */
    private String regOpenId;
    /**
     * 是否修改绑定手机号码
     */
    private Integer isUpdatePhone;

}
