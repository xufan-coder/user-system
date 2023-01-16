package com.zerody.user.service.impl;

import com.zerody.user.dto.DepartInfoDto;
import com.zerody.user.dto.StaffByCompanyDto;
import com.zerody.user.mapper.SysAddressBookMapper;
import com.zerody.user.service.SysAddressBookService;
import com.zerody.user.vo.DepartInfoVo;
import com.zerody.user.vo.StaffInfoByAddressBookVo;
import com.zerody.user.vo.StaffInfoByCompanyVo;
import com.zerody.user.vo.SysAddressBookVo;
import lombok.extern.slf4j.Slf4j;
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
    public List<SysAddressBookVo> queryAddressBook(List<String> list,Integer isProData) {
        List<SysAddressBookVo> sysMailListVos = this.sysMailListMapper.queryAddressBook(list,isProData);
        return sysMailListVos;
    }

    @Override
    public List<DepartInfoVo> queryDepartInfo(DepartInfoDto departInfoDto) {
        return sysMailListMapper.queryDepartInfo(departInfoDto);
    }

    @Override
    public List<DepartInfoVo> queryTeam(DepartInfoDto departInfoDto) {
        return sysMailListMapper.queryTeam(departInfoDto);
    }

    @Override
    public List<StaffInfoByAddressBookVo> getStaffByCompany(StaffByCompanyDto staffByCompanyDto) {
        return sysMailListMapper.getStaffByCompany(staffByCompanyDto);
    }
}
