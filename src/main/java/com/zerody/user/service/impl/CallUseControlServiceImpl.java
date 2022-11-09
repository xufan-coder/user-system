package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.CallUseControl;
import com.zerody.user.domain.UsersUseControl;
import com.zerody.user.dto.CallControlPageDto;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.vo.SysUserInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.zerody.user.mapper.CallUseControlMapper;
import com.zerody.user.service.CallUseControlService;

import java.util.List;

@Service
public class CallUseControlServiceImpl extends ServiceImpl<CallUseControlMapper, CallUseControl> implements CallUseControlService{

    @Autowired
    private SysStaffInfoService sysStaffInfoService;


    @Override
    public IPage<CallUseControl> getPageList(CallControlPageDto pageDto) {
        Page<CallUseControl> page = new Page<>();
        page.setCurrent(pageDto.getCurrent());
        page.setSize(pageDto.getPageSize());
        QueryWrapper<CallUseControl> qw = new QueryWrapper<>();
        if(DataUtil.isNotEmpty(pageDto.getCompanyId())){
            qw.lambda().eq(CallUseControl::getCompanyId,pageDto.getCompanyId());
        }
        IPage<CallUseControl> pageResult = this.page(page,qw);
        return pageResult;
    }

    @Override
    public void removeNameList(String id) {
        this.removeById(id);
    }

    @Override
    public void addNameList(List<String> userIds) {
        for (String userId : userIds) {
            QueryWrapper<CallUseControl> qw =new QueryWrapper<>();
            qw.lambda().eq(CallUseControl::getUserId,userId);
            CallUseControl one = this.getOne(qw);
            if(DataUtil.isNotEmpty(one)){
                throw new DefaultException("伙伴"+one.getUserName()+"已在名单中！");
            }else {
                SysUserInfoVo sysUserInfoVo = sysStaffInfoService.selectStaffByUserId(userId);
                CallUseControl callUseControl=new CallUseControl();
                callUseControl.setDeptId(sysUserInfoVo.getDepartId());
                callUseControl.setDeptName(sysUserInfoVo.getDepartName());
                callUseControl.setUserName(sysUserInfoVo.getUserName());
                callUseControl.setUserId(sysUserInfoVo.getId());
                callUseControl.setMobile(sysUserInfoVo.getPhoneNumber());
                callUseControl.setCompanyId(sysUserInfoVo.getCompanyId());
                this.save(callUseControl);
            }
        }

    }
}
