package com.zerody.user.service.impl;

import com.zerody.user.dto.StaffByCompanyDto;
import com.zerody.user.mapper.SysAddressBookMapper;
import com.zerody.user.service.SysAddressBookService;
import com.zerody.user.vo.DepartInfoVo;
import com.zerody.user.vo.StaffInfoByAddressBookVo;
import com.zerody.user.vo.StaffInfoByCompanyVo;
import com.zerody.user.vo.SysAddressBookVo;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年09月24日 16:22
 */
@Slf4j
@Service
public class SysAddressBookServiceImpl implements SysAddressBookService {
    @Resource
    private SysAddressBookMapper sysMailListMapper;

    @Override
    public List<SysAddressBookVo> queryAddressBook() {
        List<SysAddressBookVo> sysMailListVos = this.sysMailListMapper.queryAddressBook();
        return sysMailListVos;
    }

    @Override
    public List<DepartInfoVo> queryDepartInfo(String id) {
        List<DepartInfoVo> departInfoVos= sysMailListMapper.queryDepartInfo(id);
        return departInfoVos;
    }

    @Override
    public List<DepartInfoVo> queryTeam(String id) {
        List<DepartInfoVo> departInfoVoList=sysMailListMapper.queryTeam(id);
        return departInfoVoList;
    }

    @Override
    public List<StaffInfoByAddressBookVo> getStaffByCompany(StaffByCompanyDto staffByCompanyDto) {
        return sysMailListMapper.getStaffByCompany(staffByCompanyDto);
    }
}
