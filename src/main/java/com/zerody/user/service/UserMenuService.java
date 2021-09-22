package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.UserMenu;

import java.util.List;
import java.util.Map;

/**
 * @author  DaBai
 * @date  2021/9/10 15:15
 */

public interface UserMenuService extends IService<UserMenu> {



    List<Map> getUserMenu(String userId,Integer type);

    void addOrUpdateUserMenu(List<Map<String, Object>> param, UserVo user,Integer type);

}
