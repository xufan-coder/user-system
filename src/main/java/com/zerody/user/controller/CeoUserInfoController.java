package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.domain.AdminUserInfo;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.dto.CeoUserInfoPageDto;
import com.zerody.user.service.CeoUserInfoService;
import com.zerody.user.vo.AppCeoUserNotPushVo;
import com.zerody.user.vo.SubordinateUserQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author  DaBai
 * @date  2021/4/21 15:36
 */

@Slf4j
@RequestMapping("/ceo-user-info")
@RestController
public class CeoUserInfoController {


    @Autowired
    private CeoUserInfoService ceoUserInfoService;


    /**
     *    分页查询ceo用户
     */
    @GetMapping("/select-page")
    public DataResult selectCeoUserPage(CeoUserInfoPageDto ceoUserInfoPageDto){
        IPage<CeoUserInfo> ceoUserInfoIPage = ceoUserInfoService.selectCeoUserPage(ceoUserInfoPageDto);
        return R.success(ceoUserInfoIPage);
    }

    /**
     * 获取通讯录集团总经办（boss账户）
     * @author  DaBai
     * @date  2022/4/29 11:37
     */
    @RequestMapping(value = "/get/list", method = RequestMethod.GET)
    public DataResult<List<SubordinateUserQueryVo>> getList(){
        try {
            List<SubordinateUserQueryVo> list = this.ceoUserInfoService.getList();
            return R.success(list);
        } catch (DefaultException e){
            log.error("获取列表错误:{}" +  e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取列表错误:{}" +  e.getMessage());
            return R.error("获取列表错误,请求异常");
        }
    }

    /**
     *    id查询ceo用户
     */
    @GetMapping("/get/{id}")
    public DataResult getById(@PathVariable(value = "id") String id){
        CeoUserInfo byId = ceoUserInfoService.getById(id);
        return R.success(byId);
    }



    /**
     *    添加
     */
    @PostMapping("/add")
    public DataResult addCeoUser(@RequestBody CeoUserInfo ceoUserInfo){
         ceoUserInfoService.addCeoUser(ceoUserInfo);
        return R.success();
    }

    /**
     *    修改
     */
    @PutMapping("/update")
    public DataResult updateCeoUser(@RequestBody CeoUserInfo ceoUserInfo){
        ceoUserInfoService.updateCeoUser(ceoUserInfo);
        return R.success();
    }

    /**
     *    id删除
     */
    @DeleteMapping("/delete/{id}")
    public DataResult deleteCeoUserById(@PathVariable(value = "id") String id){
        ceoUserInfoService.deleteCeoUserById(id);
        return R.success();
    }

}
