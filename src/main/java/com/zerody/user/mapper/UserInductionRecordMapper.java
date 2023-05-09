package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.user.domain.UserInductionRecord;
import com.zerody.user.dto.UserInductionPage;
import com.zerody.user.dto.statis.UserStatisQueryDto;
import com.zerody.user.vo.SysStaffRelationVo;
import com.zerody.user.vo.UserInductionRecordInfoVo;
import com.zerody.user.vo.UserInductionRecordVo;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author kuang
 */
public interface UserInductionRecordMapper extends BaseMapper<UserInductionRecord> {

    Page<UserInductionRecordVo> getInductionPage(@Param("page") Page<UserInductionRecordVo> page,@Param("queryDto") UserInductionPage queryDto);

    UserInductionRecordInfoVo getInductionInfo(@Param("id") String id);

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 统计二次签约
    * @Date: 2023/5/5 11:32
    */
    List<SysStaffRelationVo> statistics(@Param("param")UserStatisQueryDto param);

}
