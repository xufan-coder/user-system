package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.UsersUseControl;
import com.zerody.user.dto.UsersUseControlPageDto;

/**
 * @author  DaBai
 * @date  2022/3/1 14:00
 */

public interface UsersUseControlService extends IService<UsersUseControl> {


    void addNameList(UsersUseControl param);

    void removeNameList(String id);

    IPage<UsersUseControl> getPageList(UsersUseControlPageDto pageDto);
}
