package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/**
 * 内控名单申请记录
* @Author:              xufan
* @Date:                2023/9/21 10:43
*/
@Data
@TableName("staff_blacklist_approver")
public class StaffBlacklistApprover implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 申请id
	 */
	@TableId
	private String id;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 审批时间
	 */
	private Date approveTime;
	/**
	 * 审批状态 APPROVAL审批中,FAIL拒绝,SUCCESS已通过
	 */
	private String approveState;
	/**
	 * 审批人姓名
	 */
	private String approverName;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 拉黑原因
	 */
	private String reason;
	/**
	 * 拉黑企业
	 */
	private String companyId;
	/**
	 * 拉黑人用户id
	 */
	private String userId;
	/**
	 * 提交人用户id
	 */
	private String submitUserId;
	/**
	 * 流程id
	 */
	private String processId;
	/**
	 * 流程key
	 */
	private String processKey;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 提交人姓名
	 */
	private String submitUserName;
	/**
	 * 黑名单类型：1企业内部 2外部人员
	 */
	private Integer type;
	/**
	 * 黑名单姓名
	 */
	private String userName;
	/**
	 * 身份证号码
	 */
	private String identityCard;
	/**
	 * 视频证据
	 */
	private String video;
	/**
	 * 部门id
	 */
	private String deptId;
	/**
	 * 部门名称
	 */
	private String deptName;
	/**
	 * 岗位名称
	 */
	private String postName;
	/**
	 * 角色id
	 */
	private String roleId;
	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 拉黑企业名称
	 */
	private String companyName;
}
