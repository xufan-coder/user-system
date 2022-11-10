package com.zerody.user.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 呼叫限制记录导出VO
 * @author  DaBai
 * @date  2022/11/9 10:22
 */

@Data
public class CallControlRecordVo {


    /**
    * 伙伴名称
    */
    private String userName;

    /**
    * 企业名称
    */
    private String companyName;

    /**
    * 所属部门
    */
    private String deptName;

    /**
    * 手机号码
    */
    private String mobile;

    /**
    * 角色名称
    */
    private String role;

    /**
     * 累计限制次数
     */
    private Integer num;


}
