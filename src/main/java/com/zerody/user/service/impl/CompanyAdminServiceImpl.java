package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.CompanyAdmin;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.mapper.CompanyAdminMapper;
import com.zerody.user.mapper.SysDepartmentInfoMapper;
import com.zerody.user.mapper.SysStaffInfoMapper;
import com.zerody.user.service.CompanyAdminService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.vo.CompanyAdminVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

	@Autowired
	private SysStaffInfoService sysStaffInfoService;

	@Autowired
	private SysDepartmentInfoMapper sysDepartmentInfoMapper;


	@Override
	public CompanyAdmin addCompanyAdmin(CompanyAdmin data) throws Exception {
		log.info("添加权限  ——> 入参：{}, 操作者信息：{}", JSON.toJSONString(data), JSON.toJSONString(UserUtils.getUser()));
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
		log.info("编辑权限  ——> 入参：{}, 操作者信息：{}", JSON.toJSONString(data), JSON.toJSONString(UserUtils.getUser()));
		this.updateById(data);
	}

	@Override
	public CompanyAdmin getAdminByStaffId(String staffId) {
		QueryWrapper<CompanyAdmin> qw =new QueryWrapper<>();
		qw.lambda().eq(CompanyAdmin::getStaffId,staffId);
		return this.getOne(qw);

	}

	@Override
	public StaffInfoVo getAdminInfoByCompanyId(String companyId) {
		return this.baseMapper.getAdminInfoByCompanyId(companyId);
	}

	@Override
	public List<CompanyAdminVo> getCompanyAdmin(List<String> companyIds) {
		log.info("companyIds {}", companyIds);
		List<CompanyAdminVo> array = new ArrayList<>();
		LambdaQueryWrapper<CompanyAdmin> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(CompanyAdmin::getCompanyId, companyIds);
		//获取总经理
		List<CompanyAdmin> companyAdmins = this.baseMapper.selectList(wrapper);
		log.info("companyAdmins {}", companyAdmins);
		for (CompanyAdmin companyAdmin : companyAdmins) {
			CompanyAdminVo companyAdminVo = new CompanyAdminVo();
			//获取员工信息
			SysStaffInfo userInfo = sysStaffInfoService.getUserInfo(companyAdmin.getStaffId());
			if (DataUtil.isNotEmpty(userInfo)) {
				BeanUtils.copyProperties(userInfo, companyAdminVo);
			}
			companyAdminVo.setCompanyId(companyAdmin.getCompanyId());
			array.add(companyAdminVo);
		}
		//获取副总
		for (String companyId : companyIds) {
			List<CompanyAdminVo> companyAdminList = sysDepartmentInfoMapper.queryVicePresident(companyId);
			array.addAll(companyAdminList);
		}
		return array;
	}
}
