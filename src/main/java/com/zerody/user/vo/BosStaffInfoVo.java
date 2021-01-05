package com.zerody.user.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author  DaBai
 * @date  2021/1/5 11:26
 */

@Data
public class BosStaffInfoVo {

    /**
    *   员工ID  
    */
    private String staffId;

    /**
    *   企业ID 
    */
    private String compId;

    /**
    *   企业名称
    */
    private String companyName;
    
    /**
    *   员工姓名 
    */
    private String userName;

    /**
    *   手机号
    */
    private String phone;

    /**
    *   岗位名称
    */
    private String positionName;

    /**
    *    部门名称
    */
    private String deartName;

}
