package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.dto.DepartInfoDto;
import com.zerody.user.dto.DepartureDetailsDto;
import com.zerody.user.dto.StaffByCompanyDto;
import com.zerody.user.mapper.SysAddressBookMapper;
import com.zerody.user.service.DictService;
import com.zerody.user.service.SysAddressBookService;
import com.zerody.user.vo.*;
import com.zerody.user.vo.dict.DictQuseryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DictService dictService;

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
        List<StaffInfoByAddressBookVo> staffByCompany = sysMailListMapper.getStaffByCompany(staffByCompanyDto);
        log.info("二次签约 {}", staffByCompanyDto.getIsSecondContract());
        if (!staffByCompanyDto.getIsSecondContract().equals(false)) {
            for (StaffInfoByAddressBookVo vo : staffByCompany) {
                vo.setIsSecondContract(true);
            }
        }
        return staffByCompany;
    }

    @Override
    public IPage<DepartureDetailsVo> getDepartureUserList(DepartureDetailsDto param) {
        Page<DepartureDetailsVo> page = new Page<>(param.getCurrent(), param.getPageSize());
        IPage<DepartureDetailsVo> departureUserList = this.sysMailListMapper.getDepartureUserList(param, page);
        List<DepartureDetailsVo> records = departureUserList.getRecords();
        for (DepartureDetailsVo record : records) {
            if (DataUtil.isNotEmpty(record.getLeaveType())) {
                DictQuseryVo dict = dictService.getListById(record.getLeaveType());
                record.setLeaveReason(dict.getDictName());
            }
        }
        return departureUserList;
    }

}
