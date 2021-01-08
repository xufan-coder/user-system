package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.CompanyAdmin;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.mapper.CompanyAdminMapper;
import com.zerody.user.mapper.SysStaffInfoMapper;
import com.zerody.user.service.CompanyAdminService;
import com.zerody.user.service.SysStaffInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private SysStaffInfoMapper sysStaffInfoMapper;
	@Override
	public CompanyAdmin addCompanyAdmin(CompanyAdmin data) throws Exception {
		SysStaffInfo sysStaffInfo = sysStaffInfoMapper.selectById(data.getStaffId());
		if (DataUtil.isEmpty(sysStaffInfo)){
			throw new DefaultException("员工不存在！");
		}
		data.setCompanyId(sysStaffInfo.getCompId());
		this.save(data);
		return data;
	}
	@Override
	public void updateCompanyAdmin(CompanyAdmin data) throws Exception {
		this.updateById(data);
	}
}
