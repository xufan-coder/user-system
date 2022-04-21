package com.zerody.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.domain.base.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @author kuang
 * @date 2022年03月22日 15:34
 * 登录设备绑定
 */
@Data
public class SysUserIdentifier{
    /**
     * 主键id
     */
    private String id;
    /**
     * 员工ID
     */
    private String userId;

    /**
     * 手机号
     */
    private String mobile ;

    /**
     * 企业id
     */
    private String companyId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 手机标识码
     **/
    @NotEmpty(message = "手机标识码不能为空")
    private java.lang.String equipmentNo;

    /**
     * 手机型号
     **/
    @NotEmpty(message = "手机型号不能为空")
    private String equipmentName;

    /**
     * 部门id
     */
    private String departId;

    /**
     * 部门名称
     */
    private String departName;

    /**
     * 岗位id
     */
    private String positionId;

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 审批状态 APPROVAL审批中,FAIL拒绝,SUCCESS已通过,REVOKE已撤销
     **/
    private String approveState;

    /**
     * 状态(1有效、-1删除(解除绑定)、0失效(审批已通过、已撤销、拒绝))
     */
    private Integer state;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人名称
     **/
    private String createUsername;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


    /**
     * 更新人id
     **/
    private String updateBy;

    /**
     * 更新人名称
     **/
    private String updateUsername;


    /**设备号*/
    private String deviceId;

    /**用户设备*/
    private String userDevice;

    /**流程id*/
    private String processId;

    /**流程key*/
    private String processKey;
}
