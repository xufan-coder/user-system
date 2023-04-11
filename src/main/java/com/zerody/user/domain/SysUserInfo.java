package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.constant.CheckCompare;
import com.zerody.user.domain.base.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 伙伴(员工)信息表
 *
 * @param
 * @author PengQiang
 * @description DELL
 * @date 2021/1/19 14:28
 * @return
 */
@Data
public class SysUserInfo extends BaseModel {

    /**
     * 用户名
     **/
    @NotBlank(message = "用户名不能为空")
    @CheckCompare(value = "userName", name = "名称")
    private String userName;

    /**
     * 性别(0:男，1:女，3:未知)
     **/
    @CheckCompare(value = "gender", name = "性别")
    private Integer gender;

    /**
     * 手机号
     **/
    @NotBlank(message = "手机号不能为空")
    @Size(max = 11, min = 11, message = "手机号必须为11位")
    @CheckCompare(value = "phoneNumber", name = "手机号")
    private String phoneNumber;

    /**
     * 邮箱
     **/
    @CheckCompare(value = "email", name = "邮箱")
    private String email;

    /**
     * 昵称
     **/
    @CheckCompare(value = "nickname", name = "昵称")
    private String nickname;

    /**
     * 头像(相对路径)
     **/
    private String avatar;

    /**
     * 出生日期
     **/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @CheckCompare(value = "birthday", name = "出生日期")
    private Date birthday;

    /**
     * 证件号码
     **/
    @CheckCompare(value = "certificateCard", name = "证件号码")
    private String certificateCard;

    /**
     * 身份证地址
     **/
    @CheckCompare(value = "certificateCardAddress", name = "户籍地址")
    private String certificateCardAddress;

    /**
     * 省市区
     **/
    private String provCityDistrict;

    /**
     * 联系地址
     **/
    @CheckCompare(value = "contactAddress", name = "居住地址")
    private String contactAddress;

    /**
     * 注册时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registerTime;

    /**
     * 民族
     **/
    @CheckCompare(value = "nation", name = "民族")
    private String nation;

    /**
     * 籍贯
     **/
    @CheckCompare(value = "ancestral", name = "籍贯")
    private String ancestral;

    /**
     * 备注
     **/
    @CheckCompare(value = "description", name = "备注")
    private String description;

    /**
     * 状态: 1.enable,0. disable ,-1 deleted
     **/
    @CheckCompare(value = "status", name = "状态")
    private Integer status;

    /**
     * 最高学历(枚举)
     * PRIMARY_SCHOOL("小学"), JUNIOR_HIGH("初中"), TECHNICAL_SECONDARY("中专"), SENIOR_HIGH("高中"),
     * JUNIOR_COLLEGE("大专"), REGULAR_COLLEGE("本科"), MASTER("硕士"), DOCTOR("博士");
     **/
    @CheckCompare(value = "highestEducation", name = "最高学历")
    private String highestEducation;

    /**
     * 毕业院校
     **/
    @CheckCompare(value = "graduatedFrom", name = "毕业院校")
    private String graduatedFrom;

    /**
     * 所学专业
     **/
    @CheckCompare(value = "major", name = "所学专业")
    private String major;

    /**
     * 婚姻状态(未婚0 ，已婚1， 2离婚)
     **/
    @CheckCompare(value = "maritalStatus", name = "婚姻状态")
    private Integer maritalStatus;

    /**
     * crmOpenId
     **/
    private String crmOpenId;

    /**
     * scrmOpenId
     **/
    private String scrmOpenId;

    /**
     * 微信unionId
     */
    private String unionId;

    /**
     * 紧急联系人姓名
     */
    @CheckCompare(value = "urgentName", name = "紧急联系人姓名")
    private String urgentName;

    /**
     * '紧急联系人关系'
     */
    @CheckCompare(value = "urgentRelation", name = "紧急联系人关系")
    private String urgentRelation;

    /**
     * '紧急联系人电话'
     */
    @CheckCompare(value = "urgentPhone", name = "紧急联系人电话")
    private String urgentPhone;

    /**
     * '家庭成员姓名'
     */
    @CheckCompare(value = "familyName", name = "家庭成员姓名")
    private String familyName;

    /**
     * '家庭成员关系'
     */
    @CheckCompare(value = "familyName", name = "家庭成员姓名")
    private String familyRelation;

    /**
     * '家庭成员电话'
     */
    @CheckCompare(value = "familyPhone", name = "家庭成员电话")
    private String familyPhone;

    /**
     * '家庭成员职业'
     */
    @CheckCompare(value = "familyJob", name = "家庭成员职业")
    private String familyJob;

    /**
     * '家庭成员地址'
     */
    @CheckCompare(value = "familyAddr", name = "家庭成员地址")
    private String familyAddr;

    /**
     * 业绩查看密码
     */
    private String performanceShowPassword;

    /**
     * 是否有签单
     */
    private Integer isSignOrder;

    /**
     * 角色名称
     */
    @TableField(exist = false)
    @CheckCompare(value = "roleName", name = "角色")
    private String roleName;

    /**
     * 头像修改时间
     */
    private Date avatarUpdateTime;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 是否修改用户名称名称(1.是，0.否)
     */
    private Integer isUpdateName;

    /**
     * 是否操作用户(1是 0否)
     */
    private Integer isEdit;

    /**
     * im状态
     */
    private String imState;

    /**
     * 顾问关联同步(0-未同步 1-已同步)
     */
    private Integer adviserPush;

    /**
     * 生日月
     */
    private Integer birthdayMonth;
    /**
     * 生日天
     */
    private Integer birthdayDay;

    /**
     * 用户状态是否修改(1.是、0.否)
     */
    private Integer statusEdit;


    /**
     * 身份证照片国徽面(正面)
     */
    @CheckCompare(value = "idCardFront", name = "身份证照片国徽面")
    private String idCardFront;

    /**
     * 身份证照片人像面(反面)
     */
    @CheckCompare(value = "idCardReverse", name = "身份证照片人像面")
    private String idCardReverse;

    /**
    *    培训班次
    */
    @CheckCompare(value = "trainNo", name = "培训班次")
    private String trainNo;

    /**
     * 账号状态 0正常   1已冻结
     */
    @CheckCompare(value = "useState", name = "账号状态")
    private Integer useState;

    /**
     * 岗位名称
     */
    @TableField(exist = false)
    @CheckCompare(value = "roleName", name = "岗位名称")
    private String positionName;

    /**
     * 部门名称
     */
    @TableField(exist = false)
    @CheckCompare(value = "roleName", name = "部门名称")
    private String departName;

    /** 是否预备高管 0表示否 1表示是 2表示退学*/
    private Integer isPrepareExecutive;
}
