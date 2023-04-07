package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.PrepareExecutiveRecord;
import com.zerody.user.dto.PrepareExecutiveRecordDto;
import com.zerody.user.vo.PrepareExecutiveRecordVo;

import java.util.List;

/**
 * @Author : xufan
 * @create 2023/4/3 11:46
 */
public interface PrepareExecutiveRecordService extends IService<PrepareExecutiveRecord> {

    void addPrepareExecutiveRecord(PrepareExecutiveRecordDto param , UserVo userVo);

    List<PrepareExecutiveRecordVo> getPrepareExecutiveRecordList(String userId);

    PrepareExecutiveRecordVo getPrepareExecutiveRecord(String userId);
}
