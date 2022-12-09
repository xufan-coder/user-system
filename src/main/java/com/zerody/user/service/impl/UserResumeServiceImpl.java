package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.UserResume;
import com.zerody.user.mapper.UserResumeMapper;
import com.zerody.user.service.UserResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author  DaBai
 * @date  2022/11/28 14:41
 */

@Slf4j
@Service
public class UserResumeServiceImpl extends ServiceImpl<UserResumeMapper, UserResume> implements UserResumeService {

    @Override
    public void saveOrUpdateBatchResume(List<UserResume> userResumes, StaffInfoVo staffInfoVo) {
        QueryWrapper<UserResume> rq = new QueryWrapper<>();
        rq.lambda().eq(UserResume::getUserId, staffInfoVo.getUserId());
        List<UserResume> list = this.list(rq);
        if(DataUtil.isNotEmpty(list)){
            this.remove(rq);
        }
        //先删除后保存
        for (UserResume userResume : userResumes) {
            userResume.setId(UUIDutils.getUUID32());
            userResume.setUserId(staffInfoVo.getUserId());
            userResume.setCreateTime(new Date());
        }
        this.saveBatch(userResumes);
    }
}
