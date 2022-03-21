package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.Msg;
import com.zerody.user.domain.UsersUseControl;
import com.zerody.user.dto.UsersUseControlDto;
import com.zerody.user.dto.UsersUseControlListDto;
import com.zerody.user.dto.UsersUseControlPageDto;
import com.zerody.user.mapper.UsersUseControlMapper;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.UsersUseControlService;
import com.zerody.user.vo.LoginUserInfoVo;
import com.zerody.user.vo.ReportFormsQueryVo;
import com.zerody.user.vo.SysUserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author  DaBai
 * @date  2022/3/1 14:01
 */

@Slf4j
@Service
public class UsersUseControlServiceImpl extends ServiceImpl<UsersUseControlMapper, UsersUseControl> implements UsersUseControlService {

    @Autowired
    private SysStaffInfoService sysStaffInfoService;
    @Override
    public void addNameList(UsersUseControlDto param) {
        List<String> userIds = param.getUserIds();
        for (String userId : userIds) {
            QueryWrapper<UsersUseControl> qw =new QueryWrapper<>();
            qw.lambda().eq(UsersUseControl::getUserId,userId);
            qw.lambda().eq(UsersUseControl::getType,param.getType());
            UsersUseControl one = this.getOne(qw);
            if(DataUtil.isNotEmpty(one)){
                throw new DefaultException("伙伴"+one.getUserName()+"已在名单中！");
            }else {
                SysUserInfoVo sysUserInfoVo = sysStaffInfoService.selectStaffByUserId(userId);
                UsersUseControl usersUseControl=new UsersUseControl();
                BeanUtils.copyProperties(param,usersUseControl);
                usersUseControl.setDeptId(sysUserInfoVo.getDepartId());
                usersUseControl.setDeptName(sysUserInfoVo.getDepartName());
                usersUseControl.setUserName(sysUserInfoVo.getUserName());
                usersUseControl.setUserId(sysUserInfoVo.getId());
                usersUseControl.setMobile(sysUserInfoVo.getPhoneNumber());
                usersUseControl.setCompanyId(sysUserInfoVo.getCompanyId());
                this.save(usersUseControl);
            }
        }
    }

    @Override
    public void removeNameList(String id) {
        this.removeById(id);
    }

    @Override
    public IPage<UsersUseControl> getPageList(UsersUseControlPageDto pageDto) {
        Page<UsersUseControl> page = new Page<>();
        page.setCurrent(pageDto.getCurrent());
        page.setSize(pageDto.getPageSize());
        QueryWrapper<UsersUseControl> qw = new QueryWrapper<>();
        qw.lambda().eq(UsersUseControl::getType,pageDto.getType());
        if(DataUtil.isNotEmpty(pageDto.getCompanyId())){
            qw.lambda().eq(UsersUseControl::getCompanyId,pageDto.getCompanyId());
        }
        IPage<UsersUseControl> pageResult = this.page(page,qw);
        return pageResult;
    }

    @Override
    public List<String> getListUserId(UsersUseControlListDto dto) {
        QueryWrapper<UsersUseControl> qw = new QueryWrapper<>();
        qw.lambda().eq(UsersUseControl::getType,dto.getType());
        if(DataUtil.isNotEmpty(dto.getCompanyId())){
            qw.lambda().eq(UsersUseControl::getCompanyId,dto.getCompanyId());
        }
        List<UsersUseControl> list = this.list(qw);
        List<String> userIds = list.stream().map(UsersUseControl::getUserId).collect(Collectors.toList());
        return userIds;
    }
}
