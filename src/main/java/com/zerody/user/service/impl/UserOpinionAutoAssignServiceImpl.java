package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.UserOpinionAssistantRef;
import com.zerody.user.domain.UserOpinionAutoAssign;
import com.zerody.user.domain.UserOpinionRef;
import com.zerody.user.dto.UserOpinionAutoAssignDto;
import com.zerody.user.mapper.UserOpinionAutoAssignMapper;
import com.zerody.user.service.UserOpinionAutoAssignService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author : xufan
 * @create 2024/2/29 16:16
 */
@Service
public class UserOpinionAutoAssignServiceImpl extends ServiceImpl<UserOpinionAutoAssignMapper, UserOpinionAutoAssign> implements UserOpinionAutoAssignService {


    @Override
    public Boolean isAutoAssign(String userId) {
        QueryWrapper<UserOpinionAutoAssign> qw = new QueryWrapper<>();
        qw.lambda().eq(UserOpinionAutoAssign::getUserId,userId);
        UserOpinionAutoAssign one = this.getOne(qw);
        if (DataUtil.isEmpty(one)){
            return Boolean.FALSE;
        }else {
            if (one.getAutoAssign() == YesNo.YES){
                return Boolean.TRUE;
            }else if (one.getAutoAssign() == YesNo.NO){
                return Boolean.FALSE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public void addUserAutoAssign(UserOpinionAutoAssignDto param) {
        QueryWrapper<UserOpinionAutoAssign> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserOpinionAutoAssign::getUserId, param.getUserId());
        this.remove(queryWrapper);

        UserOpinionAutoAssign autoAssign = new UserOpinionAutoAssign();
        autoAssign.setUserId(param.getUserId());
        autoAssign.setId(UUIDutils.getUUID32());
        autoAssign.setAutoAssign(param.getAutoAssign());
        autoAssign.setCreateTime(new Date());
    }


}
