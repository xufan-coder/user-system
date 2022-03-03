package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.FamilyMember;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName FamilyMemberService
 * @DateTime 2022/3/3_14:37
 * @Deacription TODO
 */
public interface FamilyMemberService extends IService<FamilyMember> {

    void addBatchFamilyMember(List<FamilyMember> entitys, StaffInfoVo staff);

    void addFamilyMember(FamilyMember entity, StaffInfoVo staff);
}
