package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.domain.UsersUseControl;
import com.zerody.user.dto.UsersUseControlPageDto;
import com.zerody.user.mapper.UsersUseControlMapper;
import com.zerody.user.service.UsersUseControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author  DaBai
 * @date  2022/3/1 14:01
 */

@Slf4j
@Service
public class UsersUseControlServiceImpl extends ServiceImpl<UsersUseControlMapper, UsersUseControl> implements UsersUseControlService {

    @Override
    public void addNameList(UsersUseControl param) {

    }

    @Override
    public void removeNameList(String id) {

    }

    @Override
    public IPage<UsersUseControl> getPageList(UsersUseControlPageDto pageDto) {
        return null;
    }
}
