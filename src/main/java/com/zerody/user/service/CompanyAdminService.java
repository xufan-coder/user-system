package com.zerody.user.service;

import java.util.List;

import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.dto.CompanyAdminPageDto;
import com.zerody.user.domain.CompanyAdmin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 
 * 企业管理员业务接口 
 * @author 黄华盛 
 * @date 2021-01-04
 */
public interface CompanyAdminService extends IService<CompanyAdmin>{

	/**
	 * 
	 * 添加企业管理员
	 * @param data
	 * @return
	 * @throws Exception
	 * @author 黄华盛
	 * @date 2021-01-04
	 */
	public CompanyAdmin addCompanyAdmin(CompanyAdmin data) throws Exception ;
	
	/**
	 * 
	 * 修改企业管理员
	 * @param data
	 * @return
	 * @throws Exception
	 * @author 黄华盛
	 * @date 2021-01-04
	 */
	public void updateCompanyAdmin(CompanyAdmin data) throws Exception;

	/**************************************************************************************************
	 **
	 * 根据员工ID查询管理员
	 *
	 * @param staffId
	 * @return {@link CompanyAdmin }
	 * @author DaBai
	 * @date 2021/1/8  16:37
	 */
	CompanyAdmin getAdminByStaffId(String staffId);


	StaffInfoVo getAdminInfoByCompanyId(String companyId);
}
