package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.user.dto.StaffByCompanyDto;
import com.zerody.user.service.SysAddressBookService;
import com.zerody.user.vo.DepartInfoVo;
import com.zerody.user.vo.StaffInfoByAddressBookVo;
import com.zerody.user.vo.StaffInfoByCompanyVo;
import com.zerody.user.vo.SysAddressBookVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年09月24日 16:36
 */
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
        List<SysAddressBookVo> sysAddressBookVos = this.sysAddressBookService.queryAddressBook();
        return R.success(sysAddressBookVos);
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
        List<DepartInfoVo> departInfoVoList = this.sysAddressBookService.queryDepartInfo(id);
        return R.success(departInfoVoList);
    }

    /***
     * @description 团队
     * @author zhangpingping
     * @date 2021/9/25
     * @param [id]
     * @return
     */
    @GetMapping(value = "/team")
    public DataResult<List<DepartInfoVo>> queryTeam(String id) {
        List<DepartInfoVo> departInfoVoList = this.sysAddressBookService.queryTeam(id);
        return R.success(departInfoVoList);
    }

    /**
     *   按企业获取员工
     */
    @GetMapping(value = "/get/by-company")
    public DataResult<List<StaffInfoByAddressBookVo>> getStaffByCompany(StaffByCompanyDto staffByCompanyDto){
        return R.success(sysAddressBookService.getStaffByCompany(staffByCompanyDto));
    }


}
