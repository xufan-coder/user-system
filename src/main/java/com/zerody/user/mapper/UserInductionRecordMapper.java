package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.user.domain.UserInductionRecord;
import com.zerody.user.dto.UserInductionPage;
import com.zerody.user.vo.UserInductionRecordInfoVo;
import com.zerody.user.vo.UserInductionRecordVo;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

/**
 * @author kuang
 */
public interface UserInductionRecordMapper extends BaseMapper<UserInductionRecord> {

    Page<UserInductionRecordVo> getInductionPage(@Param("page") Page<UserInductionRecordVo> page,@Param("queryDto") UserInductionPage queryDto);

    UserInductionRecordInfoVo getInductionInfo(@Param("id") String id);
}
