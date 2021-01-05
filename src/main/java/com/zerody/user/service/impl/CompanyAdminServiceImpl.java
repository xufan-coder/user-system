package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.domain.CompanyAdmin;
import com.zerody.user.mapper.CompanyAdminMapper;
import com.zerody.user.service.CompanyAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
/**
 * 
 * 企业管理员业务实现类
 * @author 黄华盛 
 * @date 2021-01-04
 */
@Service
public class CompanyAdminServiceImpl extends ServiceImpl<CompanyAdminMapper, CompanyAdmin> implements CompanyAdminService {
	private static final Logger log = LoggerFactory.getLogger(CompanyAdminServiceImpl.class);
	
	@Override
	public CompanyAdmin addCompanyAdmin(CompanyAdmin data) throws Exception {
		this.save(data);
		return data;
	}
	@Override
	public void updateCompanyAdmin(CompanyAdmin data) throws Exception {
		this.updateById(data);
	}
}
