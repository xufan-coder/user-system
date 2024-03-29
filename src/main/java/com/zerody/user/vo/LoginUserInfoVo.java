package com.zerody.user.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author  DaBai
 * @date  2021/1/4 10:09
 */

@Data
public class LoginUserInfoVo {

    /**
    *   企业名称
    */
    private String companyName;

    /**
    *    员工id
    */
    private String staffId;

    /**
    *    用户姓名
    */
    private String userName;

    /**
    *    手机号
    */
    private String phoneNumber;

    /**
    *    邮箱
    */
    private String email;

    /**
    *    头像
    */
    private String avatar;

    /**
    *    状态: 用户: 1.enable,0. disable ,-1 deleted；员工:0.生效、1.离职、2.删除、3.合作
    */
    private Integer status;

    /**
     * 部门id
     */
    private String departId;

    /**
    *    部门名称
    */
    private String departName;

    /**
     * 岗位id
     */
    private String positionId;

    /**
    *    岗位名称
    */
    private String positionName;

    /**
     *   是否有上传身份证照片
     */
    private Boolean hasIdCard;

    /**
     *   身份证正面
     */
    private String idCardFront;

    public Boolean getHasIdCard() {
        if(this.idCardFront!=null){
            return true;
        }
        return false;
    }

    /** 是否预备高管 0表示否 1表示是 2表示退学*/
    private Integer isPrepareExecutive;

}
