package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 公司上下班时间表
 *
 * @author chenKeFeng
 * @date 2022-08-29 11:59:15
 */
@Data
@TableName("company_work_time")
public class CompanyWorkTime implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private String id;
	/**
	 * 公司id
	 */
	private Integer companyId;
	/**
	 * 上午上班时间
	 */
	private String morningWorkTime;
	/**
	 * 上午下班时间
	 */
	private String morningAfterTime;
	/**
	 * 下午上班时间
	 */
	private String afternoonWorkTime;
	/**
	 * 下午下班时间
	 */
	private String afternoonAfterTime;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 是否删除（0未删除，1删除）
	 */
	private Integer deleted;

}
