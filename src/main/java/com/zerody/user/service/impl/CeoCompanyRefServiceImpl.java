package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.DateUtil;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.AdminUserInfo;
import com.zerody.user.domain.CeoCompanyRef;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.dto.BackRefDto;
import com.zerody.user.dto.CeoRefDto;
import com.zerody.user.mapper.AdminUserMapper;
import com.zerody.user.mapper.CeoCompanyRefMapper;
import com.zerody.user.mapper.CeoUserInfoMapper;
import com.zerody.user.service.CeoCompanyRefService;
import com.zerody.user.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author  DaBai
 * @date  2022/6/18 11:52
 */

@Slf4j
@Service
public class CeoCompanyRefServiceImpl extends ServiceImpl<CeoCompanyRefMapper, CeoCompanyRef> implements CeoCompanyRefService {

    @Autowired
    private CeoUserInfoMapper ceoUserInfoMapper;
    @Autowired
    private CeoCompanyRefMapper ceoCompanyRefMapper;
    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public void saveCompanyRef(CeoRefDto data) {
        //校验此用户是否有关联，有则先删除后新增
        QueryWrapper<CeoCompanyRef> qw =new QueryWrapper();
        qw.lambda().eq(CeoCompanyRef::getCeoId,data.getCeoId());
        List<CeoCompanyRef> list = this.list(qw);
        if(DataUtil.isNotEmpty(list)){
            this.removeByIds(list.stream().map(CeoCompanyRef::getId).collect(Collectors.toList()));
        }
        if(DataUtil.isNotEmpty(data.getCompanyIds())){
            //保存关联
            List<CeoCompanyRef> datas = data.getCompanyIds().stream().map(companyId -> {
                CeoCompanyRef ceoCompanyRef = new CeoCompanyRef();
                ceoCompanyRef.setCeoId(data.getCeoId());
                ceoCompanyRef.setCreateTime(new Date());
                ceoCompanyRef.setId(UUIDutils.getUUID32());
                ceoCompanyRef.setCompanyId(companyId);
                ceoCompanyRef.setType(1);
                return ceoCompanyRef;
            }).collect(Collectors.toList());
            this.saveBatch(datas);
        }
    }

    @Override
    public CeoRefVo getCeoRef(String id) {
        //查询ceo信息和关联企业
        CeoUserInfo ceoUserInfo = this.ceoUserInfoMapper.selectById(id);
        if(DataUtil.isNotEmpty(ceoUserInfo)){
            CeoRefVo vo=new CeoRefVo();
            vo.setCeoId(ceoUserInfo.getId());
            vo.setPhoneNumber(ceoUserInfo.getPhoneNumber());
            vo.setUserName(ceoUserInfo.getUserName());
            vo.setCompanys(this.ceoCompanyRefMapper.getCeoRef(id));
            return vo;
        }else {
            log.error("账号查询异常：{}",id);
            throw new DefaultException("账号ID不存在！");
        }
    }

    @Override
    public void saveBackCompanyRef(BackRefDto data) {
        //校验此用户是否有关联，有则先删除后新增
        QueryWrapper<CeoCompanyRef> qw =new QueryWrapper();
        qw.lambda().eq(CeoCompanyRef::getCeoId,data.getBackUserId());
        List<CeoCompanyRef> list = this.list(qw);
        if(DataUtil.isNotEmpty(list)){
            this.removeByIds(list.stream().map(CeoCompanyRef::getId).collect(Collectors.toList()));
        }
        if(DataUtil.isNotEmpty(data.getCompanyIds())){
            //保存关联
            List<CeoCompanyRef> datas = data.getCompanyIds().stream().map(companyId -> {
                CeoCompanyRef ceoCompanyRef = new CeoCompanyRef();
                ceoCompanyRef.setCeoId(data.getBackUserId());
                ceoCompanyRef.setCreateTime(new Date());
                ceoCompanyRef.setId(UUIDutils.getUUID32());
                ceoCompanyRef.setCompanyId(companyId);
                ceoCompanyRef.setType(0);
                return ceoCompanyRef;
            }).collect(Collectors.toList());
            this.saveBatch(datas);
        }
    }

    @Override
    public BackUserRefVo getBackRef(String id) {
        //查询ceo信息和关联企业
        AdminUserInfo userInfo = this.adminUserMapper.selectById(id);
        if(DataUtil.isNotEmpty(userInfo)){
            BackUserRefVo vo=new BackUserRefVo();
            vo.setBackUserId(userInfo.getId());
            vo.setPhoneNumber(userInfo.getPhoneNumber());
            vo.setUserName(userInfo.getUserName());
            vo.setCompanys(this.ceoCompanyRefMapper.getCeoRef(id));
            return vo;
        }else {
            log.error("账号查询异常：{}",id);
            throw new DefaultException("账号ID不存在！");
        }
    }

    @Override
    public List<CeoCompanyRef> getBackRefById(String ceoId) {
        LambdaQueryWrapper<CeoCompanyRef> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CeoCompanyRef::getCeoId, ceoId);
        wrapper.eq(CeoCompanyRef::getType , YesNo.YES);
        return this.baseMapper.selectList(wrapper);
    }
}
