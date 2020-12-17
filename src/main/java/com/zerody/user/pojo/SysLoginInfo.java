package com.zerody.user.pojo;

import com.zerody.user.pojo.base.BaseStringModel;
import lombok.Data;

import java.util.Date;

@Data
public class SysLoginInfo extends BaseStringModel {


    //用户名
    private String loginName;

    //密码
    private String userPwd;

    //手机号
    private String mobileNumber;

    //是否激活
    private Byte activeFlag;

    //用户ID
    private String userId;

    //昵称
    private String nickname;

    //头像
    private String avatar;

    //状态：1:enable, 0:disable, -1:deleted
    private Integer status;

    //创建人id
    private String createId;

    //创建时间
    private Date createTime;

    //修改人id
    private String updateId;

    //修改时间
    private Date updateTime;


}