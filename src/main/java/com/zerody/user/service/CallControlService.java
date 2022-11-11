package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.CallControl;
import com.zerody.user.domain.CallUseControl;
import com.zerody.user.dto.CallControlPageDto;
import com.zerody.user.vo.CallControlVo;

public interface CallControlService extends IService<CallControl> {


    CallControlVo getByCompany(String companyId);

    void addOrUpdate(CallControlVo param);

    void submitCallControl(UserVo user);
}
