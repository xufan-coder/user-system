package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.UserMenu;
import com.zerody.user.service.UserMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author  DaBai
 * @date  2021/9/10 15:10
 */

@Slf4j
@RestController
@RequestMapping("/user-menu")
public class UserMenuControlller {

    @Autowired
    private UserMenuService service;


    @PostMapping("/edit")
    public DataResult addOrUpdateUserMenu(@RequestBody List<Map<String,Object>> param){
        try {
            UserVo user = UserUtils.getUser();
            if(DataUtil.isEmpty(user)){
                throw new DefaultException("常用菜单获取失败！");
            }
            this.service.addOrUpdateUserMenu(param,user);
            return R.success();
        } catch (DefaultException e) {
            log.error("编辑菜单错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("编辑菜单错误：{}", e, e);
            return R.error("编辑菜单错误" + e.getMessage());
        }
    }


    @GetMapping("/get")
    public DataResult<List<Map>> getUserMenu(){
        try {
            List<Map> result = this.service.getUserMenu(UserUtils.getUserId());
            return R.success(result);
        } catch (DefaultException e) {
            log.error("获取常用菜单出错：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取常用菜单出错：{}", e, e);
            return R.error("获取常用菜单出错" + e.getMessage());
        }
    }

}
