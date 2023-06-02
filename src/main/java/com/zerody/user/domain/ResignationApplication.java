
package com.zerody.user.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 员工离职申请表
 *
 * @author DaBai
 * @date 2021/8/5 15:05
 */

@Data
public class ResignationApplication implements java.io.Serializable {
    /**
     * id
     **/
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * user_id
     **/
    private String userId;
    /**
     * 姓名
     */
    private String name;

    /**
     * 员工id
     */
    private String staffId;
    /**
     * 审批状态
     */
    private String approvalState;
    /**
     * 创建时间
     **/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approvalTime;
    /**
     * 离职时间
     **/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date resignationTime;

    /**
     * 企业id
     */
    private String companyId;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 部门id
     */
    private String departId;
    /**
     * 所属岗位id
     */
    private String departName;
    /**
     * 员工id
     */
    private String positionId;
    /**
     * 所属岗位
     */
    private String positionName;
    /**
     * 离职原因
     */
    private String reason;
    /**
     * '离职原因
     */
    private String remark;

    /**
     * 创建时间
     **/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 流程id
     */
    private String processId;
    /**
     * 流程key
     */
    private String processKey;

    /**
     * 离职状态(0.未离职、1.已离职)
     */
    private Integer leaveState;

}
