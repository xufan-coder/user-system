package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


/**
 * 呼叫限制白名单
 * @author  DaBai
 * @date  2022/11/9 10:21
 */

@Data
public class CallUseControl {
    /**
    * id
    */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
    * 伙伴id
    */
    private String userId;

    /**
    * 企业id
    */
    private String companyId;

    /**
    * 伙伴名称
    */
    private String userName;

    /**
    * 所属部门id
    */
    private String deptId;

    /**
    * 所属部门
    */
    private String deptName;

    /**
    * 手机号码
    */
    private String mobile;
}
