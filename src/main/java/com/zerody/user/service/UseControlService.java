package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.UseControl;
import com.zerody.user.domain.UsersUseControl;
import com.zerody.user.dto.UseControlDto;
import com.zerody.user.vo.UseControlVo;

/**
 * @author  DaBai
 * @date  2022/3/1 14:00
 */

public interface UseControlService extends IService<UseControl> {


    void addOrUpdate(UseControlDto param);

    UseControlVo getByCompany(String companyId);

    Boolean checkUserAuth(UserVo userId);

    Boolean checkUserAuth(String userId,String companyId);
}
