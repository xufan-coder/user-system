package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.domain.CallControlRecord;
import com.zerody.user.dto.CallControlRecordPageDto;
import com.zerody.user.vo.CallControlRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CallControlRecordMapper extends BaseMapper<CallControlRecord> {
    List<CallControlRecordVo> getList(@Param("dto")CallControlRecordPageDto dto);
}
