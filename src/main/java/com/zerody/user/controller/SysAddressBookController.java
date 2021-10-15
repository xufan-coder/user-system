package com.zerody.user.controller;

import com.alibaba.fastjson.JSON;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.dto.StaffByCompanyDto;
import com.zerody.user.service.SysAddressBookService;
import com.zerody.user.vo.DepartInfoVo;
import com.zerody.user.vo.StaffInfoByAddressBookVo;
import com.zerody.user.vo.StaffInfoByCompanyVo;
import com.zerody.user.vo.SysAddressBookVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年09月24日 16:36
 */
@Slf4j
@RequestMapping("/mail-list")
@RestController
public class SysAddressBookController {

    @Autowired
    private SysAddressBookService sysAddressBookService;

    /***
     * @description 获取公司
     * @author zhangpingping
     * @date 2021/9/24
     * @param []
     * @return
     */
    @GetMapping(value = "/address-book")
    public DataResult<List<SysAddressBookVo>> queryAddressBook() {
        try {
            List<SysAddressBookVo> sysAddressBookVos = this.sysAddressBookService.queryAddressBook();
            return R.success(sysAddressBookVos);
        } catch (Exception e) {
            log.error("获取公司错误:{}", e);
            return R.error("获取公司失败,请求异常");
        }
    }

    /***
     * @description 部门
     * @author zhangpingping
     * @date 2021/9/25
     * @param [id]
     * @return
     */
    @GetMapping(value = "/depart-info")
    public DataResult<List<DepartInfoVo>> queryDepartInfo(String id) {
        try {
            List<DepartInfoVo> departInfoVoList = this.sysAddressBookService.queryDepartInfo(id);
            return R.success(departInfoVoList);
        } catch (Exception e) {
            log.error("获取部门错误:{}", JSON.toJSONString(id), e);
            return R.error("获取部门失败,请求异常");
        }
    }

    /***
     * @description 团队
     * @author zhangpingping
     * @date 2021/9/25
     * @param [id]
     * @return
     */
    @GetMapping(value = "/team")
    public DataResult<List<DepartInfoVo>> queryTeam(String id, String departmentId) {
        try {
            List<DepartInfoVo> departInfoVoList = this.sysAddressBookService.queryTeam(id, departmentId);
            return R.success(departInfoVoList);
        } catch (Exception e) {
            log.error("获取团队错误:{}", JSON.toJSONString(id),JSON.toJSONString(departmentId), e);
            return R.error("获取团队失败,请求异常");
        }
    }

    /**
     * 按企业获取员工
     */
    @GetMapping(value = "/get/by-company")
    public DataResult<List<StaffInfoByAddressBookVo>> getStaffByCompany(StaffByCompanyDto staffByCompanyDto) {
        try {
            return R.success(sysAddressBookService.getStaffByCompany(staffByCompanyDto));
        } catch (Exception e) {
            log.error("获取员工错误:{}", JSON.toJSONString(staffByCompanyDto), e);
            return R.error("获取员工失败,请求异常");
        }
    }


}
