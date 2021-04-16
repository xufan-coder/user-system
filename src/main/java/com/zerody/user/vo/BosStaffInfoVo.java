package com.zerody.user.vo;

import com.zerody.common.enums.StatusEnum;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.enums.StaffStatusEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author  DaBai
 * @date  2021/1/5 11:26
 */

@Data
public class BosStaffInfoVo {

    /**
    *   ID
    */
    private String id;

    /**
    *   员工ID
    */
    private String staffId;

    /**
     *   角色ID
     */
    private String roleId;

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
    private String departName;

    /**
    *    角色
    */
    private String roleName;

    /** 员工状态 */
    private Integer staffStatus;

    public String getStaffStatusString(){
         if (DataUtil.isEmpty(this.staffStatus)) {
             return "";
         }
        StatusEnum statusEnum = StatusEnum.getByValue(this.staffStatus);
         if (DataUtil.isEmpty(statusEnum)) {
             return "";
         }
         return statusEnum.getDesc();
    }
}
