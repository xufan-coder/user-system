package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.UserMenu;
import com.zerody.user.enums.MenuSetTypeEnum;
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
                throw new DefaultException("编辑失败！");
            }
            this.service.addOrUpdateUserMenu(param,user, MenuSetTypeEnum.MINI.getCode());
            return R.success();
        } catch (DefaultException e) {
            log.error("编辑菜单错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("编辑菜单错误：{}", e, e);
            return R.error("编辑菜单错误" + e.getMessage());
        }
    }


    /**
    *    APP 编辑常用菜单
    */
    @PostMapping("/app/edit")
    public DataResult updateAppUserMenu(@RequestBody List<Map<String,Object>> param){
        try {
            UserVo user = UserUtils.getUser();
            if(DataUtil.isEmpty(user)){
                throw new DefaultException("编辑失败！");
            }
            this.service.addOrUpdateUserMenu(param,user, MenuSetTypeEnum.APP.getCode());
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
            List<Map> result = this.service.getUserMenu(UserUtils.getUserId(), MenuSetTypeEnum.MINI.getCode());
            return R.success(result);
        } catch (DefaultException e) {
            log.error("获取常用菜单出错：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取常用菜单出错：{}", e, e);
            return R.error("获取常用菜单出错" + e.getMessage());
        }
    }

    /**
    *   APP 获取常用菜单
    */
    @GetMapping("/app/get")
    public DataResult<List<Map>> getAppUserMenu(){
        try {
            List<Map> result = this.service.getUserMenu(UserUtils.getUserId(), MenuSetTypeEnum.APP.getCode());
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
