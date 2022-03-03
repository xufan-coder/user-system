package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.FamilyMember;
import com.zerody.user.mapper.FamilyMemberMapper;
import com.zerody.user.service.FamilyMemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName FamilyMemberServiceImpl
 * @DateTime 2022/3/3_14:37
 * @Deacription TODO
 */
@Slf4j
@Service
public class FamilyMemberServiceImpl extends ServiceImpl<FamilyMemberMapper, FamilyMember> implements FamilyMemberService {
    @Override
    public void addBatchFamilyMember(List<FamilyMember> entitys, StaffInfoVo staff) {
        if (DataUtil.isEmpty(staff) || StringUtils.isEmpty(staff.getUserId()) || StringUtils.isEmpty(staff.getStaffId())) {
            throw new DefaultException("家庭成员保存失败");
        }
        QueryWrapper<FamilyMember> removeQw = new QueryWrapper<>();
        removeQw.lambda().eq(FamilyMember::getUserId, staff.getUserId());
        this.remove(removeQw);
        if (CollectionUtils.isEmpty(entitys)) {
            return;
        }
        int order = 1;
        for (FamilyMember entity : entitys) {
            entity.setId(null);
            entity.setCreateTime(new Date());
            entity.setOrderNum(order++);
            entity.setStaffId(staff.getStaffId());
            entity.setUserId(staff.getUserId());
        }
        this.saveBatch(entitys);
    }

    @Override
    public void addFamilyMember(FamilyMember entity, StaffInfoVo staff) {
        entity.setId(null);
        entity.setCreateTime(new Date());
        entity.setOrderNum(1);
        entity.setStaffId(staff.getStaffId());
        entity.setUserId(staff.getUserId());
        this.save(entity);
    }
}
