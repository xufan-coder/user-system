package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.constant.CommonConstants;
import com.zerody.user.domain.Msg;
import com.zerody.user.domain.UsersUseControl;
import com.zerody.user.dto.UsersTokenControlDto;
import com.zerody.user.dto.UsersUseControlDto;
import com.zerody.user.dto.UsersUseControlListDto;
import com.zerody.user.dto.UsersUseControlPageDto;
import com.zerody.user.mapper.UsersUseControlMapper;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.UsersUseControlService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.LoginUserInfoVo;
import com.zerody.user.vo.ReportFormsQueryVo;
import com.zerody.user.vo.SysUserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author  DaBai
 * @date  2022/3/1 14:01
 */

@Slf4j
@Service
public class UsersUseControlServiceImpl extends ServiceImpl<UsersUseControlMapper, UsersUseControl> implements UsersUseControlService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SysStaffInfoService sysStaffInfoService;
    @Autowired
    private CheckUtil checkUtil;
    @Override
    public void addNameList(UsersUseControlDto param) {
       List<String> blackList=new ArrayList<>();
       List<String> whiteList=new ArrayList<>();

        List<String> userIds = param.getUserIds();
        for (String userId : userIds) {
            QueryWrapper<UsersUseControl> qw =new QueryWrapper<>();
            qw.lambda().eq(UsersUseControl::getUserId,userId);
            qw.lambda().eq(UsersUseControl::getType,param.getType());
            UsersUseControl one = this.getOne(qw);
            if(DataUtil.isNotEmpty(one)){
                throw new DefaultException("伙伴"+one.getUserName()+"已在名单中！");
            }else {
                SysUserInfoVo sysUserInfoVo = sysStaffInfoService.selectStaffByUserId(userId,null,false);
                UsersUseControl usersUseControl=new UsersUseControl();
                BeanUtils.copyProperties(param,usersUseControl);
                usersUseControl.setDeptId(sysUserInfoVo.getDepartId());
                usersUseControl.setDeptName(sysUserInfoVo.getDepartName());
                usersUseControl.setUserName(sysUserInfoVo.getUserName());
                usersUseControl.setUserId(sysUserInfoVo.getId());
                usersUseControl.setMobile(sysUserInfoVo.getPhoneNumber());
                usersUseControl.setCompanyId(sysUserInfoVo.getCompanyId());
                this.save(usersUseControl);
                if(usersUseControl.getType()==1){
                    blackList.add(usersUseControl.getUserId());
                }
                if(usersUseControl.getType()==2){
                    whiteList.add(usersUseControl.getUserId());
                }
            }
        }
        if(DataUtil.isNotEmpty(blackList)){
            for (String s : blackList) {
                stringRedisTemplate.opsForHash().put(CommonConstants.USE_CONTROL_BLACK_LIST_,s,"1");
            }

        }
        if(DataUtil.isNotEmpty(whiteList)){
            for (String s : whiteList) {
                stringRedisTemplate.opsForHash().put(CommonConstants.USE_CONTROL_WHITE_LIST,s,"1");
            }
        }
    }

    @Override
    public void removeNameList(String id) {
        UsersUseControl one = this.getById(id);
        if(DataUtil.isNotEmpty(one)){
            if(one.getType()==1){
                stringRedisTemplate.opsForHash().delete(CommonConstants.USE_CONTROL_BLACK_LIST_, one.getUserId());
            }
            if(one.getType()==2){
                stringRedisTemplate.opsForHash().delete(CommonConstants.USE_CONTROL_WHITE_LIST, one.getUserId());
            }
        }
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

    @Override
    public void removeToken(UsersTokenControlDto param) {
        List<String> list=new ArrayList<>();
        if(1==param.getType()){
            //1 企业id 下线所有企业的token
           list=sysStaffInfoService.getUserIdByCompanyIds(param.getRemoveIds());
        }else if(2==param.getType()){
            //2 部门id 下线所有部门下级的token
            list=sysStaffInfoService.getUserIdByDeptIds(param.getRemoveIds());
        }else if(3==param.getType()){
            //3 用户id 下线所有id的token
            list=param.getRemoveIds();
        }
        this.checkUtil.removeUserTokens(list);
    }
}
