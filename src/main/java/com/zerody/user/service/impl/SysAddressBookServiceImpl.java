package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.user.dto.DepartInfoDto;
import com.zerody.user.dto.DepartureDetailsDto;
import com.zerody.user.dto.StaffByCompanyDto;
import com.zerody.user.mapper.SysAddressBookMapper;
import com.zerody.user.service.SysAddressBookService;
import com.zerody.user.vo.*;
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

    @Override
    public IPage<DepartureDetailsVo> getDepartureUserList(DepartureDetailsDto param) {
        Page<DepartureDetailsVo> page = new Page<>(param.getCurrent(), param.getPageSize());
        return this.sysMailListMapper.getDepartureUserList(param, page);
    }

}
