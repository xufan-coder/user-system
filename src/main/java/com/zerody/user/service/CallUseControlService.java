package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.CallUseControl;
import com.zerody.user.dto.CallControlPageDto;

import java.util.List;

public interface CallUseControlService extends IService<CallUseControl> {


    IPage<CallUseControl> getPageList(CallControlPageDto pageDto);

    void removeNameList(String id);

    void addNameList(List<String> userIds);

}
