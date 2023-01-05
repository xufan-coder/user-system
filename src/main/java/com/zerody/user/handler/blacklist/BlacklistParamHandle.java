package com.zerody.user.handler.blacklist;

import com.zerody.common.enums.user.StaffBlacklistApproveState;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.StaffBlacklist;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.domain.SysUserInfo;

import java.util.Date;

/**
 * @author PengQiang
 * @ClassName BlacklistParamHandle
 * @DateTime 2023/1/5 11:24
 * @Deacription TODO
 */
public class BlacklistParamHandle {



    public static StaffBlacklist insideStaffBlacklistParam(StaffInfoVo staff, UserVo loginUser) {
        StaffBlacklist entity = new StaffBlacklist();
        entity.setType(1);
        entity.setCompanyId(staff.getCompanyId());
        entity.setApprovalTime(new Date());
        entity.setCompanyName(staff.getCompanyName());
        entity.setCreateTime(new Date());
        entity.setMobile(staff.getMobile());
        entity.setIdentityCard(staff.getIdentityCard());
        entity.setState(StaffBlacklistApproveState.BLOCK.name());
        entity.setUserName(staff.getUserName());
        entity.setUserId(staff.getUserId());
        entity.setSubmitUserId(loginUser.getUserId());
        entity.setSubmitUserName(loginUser.getUserName());
        return entity;
    }
}
