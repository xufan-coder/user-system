package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.UsersUseControl;
import com.zerody.user.dto.UsersTokenControlDto;
import com.zerody.user.dto.UsersUseControlDto;
import com.zerody.user.dto.UsersUseControlListDto;
import com.zerody.user.dto.UsersUseControlPageDto;

import java.util.List;

/**
 * @author  DaBai
 * @date  2022/3/1 14:00
 */

public interface UsersUseControlService extends IService<UsersUseControl> {


    void addNameList(UsersUseControlDto param);

    void removeNameList(String id);

    IPage<UsersUseControl> getPageList(UsersUseControlPageDto pageDto);

    List<String> getListUserId(UsersUseControlListDto dto);

    void removeToken(UsersTokenControlDto param);
}
