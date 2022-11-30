package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zerody.user.domain.base.BaseModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 总裁用户表
 *
 * @author  DaBai
 * @date  2021/4/20 14:25
 */
@Data
public class CeoUserInfo extends BaseModel {
    /**
     * 名片用户id
     */
    private String cardUserId;
    /**
     * 角色Id
     */
    private String roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 个人照片
     */
    private String photo;
    /**
     * 岗位职位
     */
    private String position;
    /**
     * 企业名称
     */
    private String company;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 密码
     */
    private String userPwd;
    /**
     * 身份证号
     */
    private String certificateCard;
    /**
     * 户籍地址
     */
    private String certificateCardAddress;
    /**
     * 居住地址
     */
    private String contactAddress;
    /**
     * 居住地址
     */
    private String provCityDistrict;
    /**
     * 民族
     */
    private String nation;
    /**
     * 籍贯
     */
    private String ancestral;
    /**
     * 最高学历
     */
    private String highestEducation;
    /**
     * 毕业院校
     */
    private String graduatedFrom;
    /**
     * 所学专业
     */
    private String major;
    /**
     * 婚姻状态
     */
    private String maritalStatus;
    /**
     * unionId
     */
    private String unionId;
    /**
     * crmOpenId
     */
    private String crmOpenId;
    /**
     * scrmOpenId
     */
    private String scrmOpenId;
    /**
     * 最后一次登录校验短信验证码时间
     */
    private Date lastCheckSms;
    /**
     * 最后一次登录时间
     */
    private Date loginTime;
    /**
     * 是否删除(0存在 1删除)
     */
    private Integer deleted;
    /**
     * 备注
     */
    private String remark;
    /**
     * im状态
     */
    private String imState;

    /**
     * 企业id集合
     */
    @TableField(exist = false)
    private List<String> companys;

}
