package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.domain.BlacklistOperationRecord;
import com.zerody.user.dto.BlacklistOperationRecordPageDto;
import com.zerody.user.vo.BlacklistOperationRecordPageVo;
import org.apache.ibatis.annotations.Param;

/**
 * @Author : xufan
 * @create 2023/3/8 19:03
 */

public interface BlacklistOperationRecordMapper extends BaseMapper<BlacklistOperationRecord> {

    /**
    * @Description:         分页查询内控名单操作记录
    * @Param:               [param, iPage]
    * @return:              com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.BlacklistOperationRecordPageVo>
    * @Author:              xufan
    * @Date:                2023/3/10 9:09
    */
    IPage<BlacklistOperationRecordPageVo> getPageBlacklistOperationRecord(@Param("param") BlacklistOperationRecordPageDto param, IPage<BlacklistOperationRecordPageVo> iPage);
}
