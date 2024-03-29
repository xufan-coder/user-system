package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.constant.CommonConstants;
import com.zerody.user.domain.CallControlRecord;
import com.zerody.user.dto.CallControlRecordPageDto;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.vo.CallControlRecordVo;
import com.zerody.user.vo.SysUserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.zerody.user.mapper.CallControlRecordMapper;
import com.zerody.user.service.CallControlRecordService;

import java.util.Date;
import java.util.List;

@Service
public class CallControlRecordServiceImpl extends ServiceImpl<CallControlRecordMapper,  CallControlRecord> implements CallControlRecordService{

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @Autowired
    private CallControlRecordMapper callControlRecordMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public IPage<CallControlRecord> getPageList(CallControlRecordPageDto pageDto) {
        Page<CallControlRecord> page = new Page<>();
        page.setCurrent(pageDto.getCurrent());
        page.setSize(pageDto.getPageSize());
        QueryWrapper<CallControlRecord> qw = new QueryWrapper<>();
        qw.lambda().eq(CallControlRecord::getState,YesNo.NO);
        if(DataUtil.isNotEmpty(pageDto.getCompanyId())){
            qw.lambda().eq(CallControlRecord::getCompanyId,pageDto.getCompanyId());
        }
        if(DataUtil.isNotEmpty(pageDto.getUserId())){
            qw.lambda().eq(CallControlRecord::getCompanyId,pageDto.getUserId());
        }
        if(DataUtil.isNotEmpty(pageDto.getDepartId())){
            pageDto.setDepartId(pageDto.getDepartId().concat("%"));
            qw.lambda().like(CallControlRecord::getCompanyId,pageDto.getDepartId());
        }
        IPage<CallControlRecord> pageResult = this.page(page,qw);
        return pageResult;
    }

    @Override
    public void doRelieveCallControlRecordList(List<String> ids) {
            ids.forEach(item->{
                doRelieveCallControlRecord(item);});
    }
    @Override
    public void doRelieveCallControlRecord(String id) {
        UpdateWrapper<CallControlRecord> uw =new UpdateWrapper<>();
        uw.lambda().eq(CallControlRecord::getId,id);
        uw.lambda().eq(CallControlRecord::getState, YesNo.NO);
        uw.lambda().set(CallControlRecord::getState,YesNo.YES)
                .set(CallControlRecord::getRemoveTime,new Date());
        this.update(uw);
        clearCount(id);
    }

    public void clearCount(String id){
        QueryWrapper<CallControlRecord> qw =new QueryWrapper<>();
        qw.lambda().eq(CallControlRecord::getId,id);
        CallControlRecord one = this.getOne(qw);
        if(DataUtil.isNotEmpty(one)){
            String key= CommonConstants.CALL_FLAG+one.getCompanyId()+":"+one.getUserId();
            stringRedisTemplate.delete(CommonConstants.CALL_CONTROL_USER_LIST+one.getUserId());
            stringRedisTemplate.delete(key);
        }
    }

    @Override
    public void saveRecord(String userId) {
            QueryWrapper<CallControlRecord> qw =new QueryWrapper<>();
            qw.lambda().eq(CallControlRecord::getUserId,userId);
            qw.lambda().orderByDesc(CallControlRecord::getCreateTime);
            qw.lambda().last(" limit 1");
            CallControlRecord one = this.getOne(qw);
            if(DataUtil.isNotEmpty(one)){
                one.setNum(one.getNum()+1);
                one.setId(UUIDutils.getUUID32());
                one.setCreateTime(new Date());
                one.setState(YesNo.NO);
                this.save(one);
            }else {
                SysUserInfoVo sysUserInfoVo = sysStaffInfoService.selectStaffByUserId(userId,null,false);
                CallControlRecord callControlRecord=new CallControlRecord();
                callControlRecord.setDeptId(sysUserInfoVo.getDepartId());
                callControlRecord.setDeptName(sysUserInfoVo.getDepartName());
                callControlRecord.setUserName(sysUserInfoVo.getUserName());
                callControlRecord.setUserId(sysUserInfoVo.getId());
                callControlRecord.setMobile(sysUserInfoVo.getSensitivePhone());
                callControlRecord.setCompanyId(sysUserInfoVo.getCompanyId());
                callControlRecord.setCompanyName(sysUserInfoVo.getCompanyName());
                callControlRecord.setRole(sysUserInfoVo.getRoleName());
                callControlRecord.setCreateTime(new Date());
                callControlRecord.setNum(1);
                callControlRecord.setState(YesNo.NO);
                this.save(callControlRecord);
            }
        stringRedisTemplate.opsForValue().set(CommonConstants.CALL_CONTROL_USER_LIST+userId,userId);
    }

    @Override
    public List<CallControlRecordVo> getList(CallControlRecordPageDto pageDto) {
        List<CallControlRecordVo> list = this.callControlRecordMapper.getList(pageDto);
        return list;
    }
}
