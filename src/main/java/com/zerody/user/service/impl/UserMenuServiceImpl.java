package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.util.JsonUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.UserMenu;
import com.zerody.user.enums.MenuSetTypeEnum;
import com.zerody.user.mapper.UserMenuMapper;
import com.zerody.user.service.ImageService;
import com.zerody.user.service.UserMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  DaBai
 * @date  2021/9/10 15:16
 */

@Slf4j
@Service
public class UserMenuServiceImpl extends ServiceImpl<UserMenuMapper, UserMenu> implements UserMenuService {


    @Override
    public List<Map> getUserMenu(String userId,Integer type) {
        QueryWrapper<UserMenu> qw =new QueryWrapper<>();
        qw.lambda().eq(UserMenu::getUserId,userId);
        qw.lambda().eq(UserMenu::getType, type);
        UserMenu one = this.getOne(qw);
        if(DataUtil.isNotEmpty(one)){
           return JsonUtils.json2List(one.getMenuJson(), Map.class);
        }
        return null;
    }

    @Override
    public void addOrUpdateUserMenu(List<Map<String, Object>> param, UserVo user,Integer type) {
        QueryWrapper<UserMenu> qw =new QueryWrapper<>();
        qw.lambda().eq(UserMenu::getUserId,user.getUserId());
        qw.lambda().eq(UserMenu::getType,type);
        UserMenu one = this.getOne(qw);
        if(DataUtil.isNotEmpty(one)){
            one.setMenuJson(JsonUtils.toString(param));
            one.setUpdateTime(new Date());
            this.updateById(one);
        }else {
            one=new UserMenu();
            one.setUserId(user.getUserId());
            one.setUserName(user.getUserName());
            one.setMenuJson(JsonUtils.toString(param));
            one.setCreateTime(new Date());
            one.setType(type);
            this.save(one);
        }
    }
}
