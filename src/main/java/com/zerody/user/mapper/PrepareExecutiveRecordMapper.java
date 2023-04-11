package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.PrepareExecutiveRecord;
import com.zerody.user.vo.CreateInfoVo;
import org.apache.ibatis.annotations.Param;

/**
 * @Author : xufan
 * @create 2023/4/3 13:53
 */
public interface PrepareExecutiveRecordMapper extends BaseMapper<PrepareExecutiveRecord> {
    CreateInfoVo getCreateInfoByCreateId(@Param("param") UserVo param);

}
