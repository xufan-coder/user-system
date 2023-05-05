package com.zerody.user.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.UserInductionRecord;
import com.zerody.user.dto.UserInductionPage;
import com.zerody.user.dto.UserInductionVerificationDto;
import com.zerody.user.dto.statis.UserStatisQueryDto;
import com.zerody.user.vo.SysStaffRelationVo;
import com.zerody.user.vo.UserInductionRecordInfoVo;
import com.zerody.user.vo.UserInductionRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author kuang
 */
public interface UserInductionRecordService extends IService<UserInductionRecord> {

    Page<UserInductionRecordVo> getInductionPage(UserInductionPage queryDto);

    UserInductionRecordInfoVo getInductionInfo(String id);

    UserInductionRecord addOrUpdateRecord(UserInductionRecord param);

    void doRenewInduction(UserInductionRecord induction);

    JSONObject verification(UserInductionVerificationDto param);

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 统计二次签约
    * @Date: 2023/5/5 11:31
    */
    List<SysStaffRelationVo> statistics(@Param("param") UserStatisQueryDto param);

}
