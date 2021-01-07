package com.zerody.user.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
/**
 * @author  DaBai
 * @date  2020/12/29 16:08
 */
@Data
public class SysLoginUserInfoVo {

	private String id;

	/**
	 * 用户姓名
	 */
	private String userName;
	/**
	 * enum('male','female','unknow')
	 */
	private Integer gender;
	/**
	 * 手机号
	 */
	private String phoneNumber;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 头像(相对路径)
	 */
	private String avatar;

	/**
	 * open_id
	 */
	@TableField("open_id")
	private String openId;

	/***
	* ------密码
	*/
	private String userPwd;
    /**
    *    企业ID
    */
	private String companyId;

	/**
	*   最后验证短信时间
	*/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date lastCheckSms;

	/**
	 *    部门ID
	 */
	private String deptId;
}