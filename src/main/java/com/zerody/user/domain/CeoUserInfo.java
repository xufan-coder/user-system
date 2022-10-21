package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zerody.user.domain.base.BaseModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author  DaBai
 * @date  2021/4/20 14:25
 */
@Data
public class CeoUserInfo extends BaseModel {

    private String cardUserId;

    private String roleId;

    private String roleName;

    private String userName;

    private String avatar;

    private String photo;

    private String position;

    private String company;

    private Date birthday;

    private String email;

    private String phoneNumber;

    private String userPwd;

    private String certificateCard;

    private String certificateCardAddress;

    private String contactAddress;

    private String provCityDistrict;

    private String nation;

    private String ancestral;

    private String highestEducation;

    private String graduatedFrom;

    private String major;

    private String maritalStatus;

    private String unionId;

    private String crmOpenId;

    private String scrmOpenId;

    private Date lastCheckSms;

    private Date loginTime;

    private Integer deleted;

    private String remark;

    private String imState;

    @TableField(exist = false)
    private List<String> companys;

}
