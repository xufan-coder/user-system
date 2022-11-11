package com.zerody.user.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "伙伴名称",orderNum = "1",width = 20)
    private String userName;

    /**
    * 企业名称
    */
    @Excel(name = "企业名称",orderNum = "3",width = 20)
    private String companyName;

    /**
    * 所属部门
    */
    @Excel(name = "所属部门",orderNum = "4",width = 20)
    private String deptName;

    /**
    * 手机号码
    */
    @Excel(name = "手机号码",orderNum = "2",width = 20)
    private String mobile;

    /**
    * 角色名称
    */
    @Excel(name = "角色名称",orderNum = "5",width = 20)
    private String role;

    /**
     * 累计限制次数
     */
    @Excel(name = "发生次数",orderNum = "6",width = 20)
    private Integer num;


}
