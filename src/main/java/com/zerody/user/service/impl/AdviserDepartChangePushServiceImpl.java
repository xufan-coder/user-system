package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.AdviserDepartChangePush;
import com.zerody.user.domain.AppUserPush;
import com.zerody.user.mapper.AdviserDepartChangePushMapper;
import com.zerody.user.service.AdviserDepartChangePushService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author  DaBai
 * @date  2024/1/18 10:45
 */

@Service
public class AdviserDepartChangePushServiceImpl extends ServiceImpl<AdviserDepartChangePushMapper, AdviserDepartChangePush> implements AdviserDepartChangePushService {


    @Override
    public void saveAdviserSync(String departId, String userId) {
        AdviserDepartChangePush entity=new AdviserDepartChangePush();
        entity.setCreateTime(new Date());
        entity.setDeleted(YesNo.NO);
        entity.setResend(0);
        entity.setDeptId(departId);
        entity.setUserId(userId);
        entity.setState(YesNo.NO);
        this.save(entity);
    }

    @Override
    public List<AdviserDepartChangePush> selectAll() {
        QueryWrapper<AdviserDepartChangePush> qw =new QueryWrapper<>();
        qw.lambda().eq(AdviserDepartChangePush::getState,YesNo.NO)
                .eq(AdviserDepartChangePush::getDeleted,YesNo.NO)
        .le(AdviserDepartChangePush::getResend,5);
        return this.list(qw);
    }
}
