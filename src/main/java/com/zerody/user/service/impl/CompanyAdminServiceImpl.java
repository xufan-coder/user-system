package com.zerody.user.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.mapper.*;
import com.zerody.user.domain.CompanyAdmin;
import com.zerody.user.dto.CompanyAdminPageDto;
import com.zerody.user.service.CompanyAdminService;
/**
 * 
 * 企业管理员业务实现类
 * @author 黄华盛 
 * @date 2021-01-04
 */
@Service
public class CompanyAdminServiceImpl extends ServiceImpl<CompanyAdminMapper, CompanyAdmin> implements CompanyAdminService {
	private static final Logger log = LoggerFactory.getLogger(CompanyAdminServiceImpl.class);
	
	public CompanyAdmin addCompanyAdmin(CompanyAdmin data) throws Exception {
		this.save(data);
		return data;
	}
	public void updateCompanyAdmin(CompanyAdmin data) throws Exception {
		this.updateById(data);
	}
	@Override
	public IPage<CompanyAdmin> pageData(CompanyAdminPageDto pageQuery) {
		Page<CompanyAdmin> page = new Page<CompanyAdmin>();
		page.setCurrent(pageQuery.getCurrent());
		page.setSize(pageQuery.getPageSize());
		QueryWrapper<CompanyAdmin> qw = new QueryWrapper<>();
		IPage<CompanyAdmin> pageResult = this.page(page,qw);
		return pageResult;
	}

	@Override
	public List<CompanyAdmin> findData() {
		return this.query().list();
	}
}
