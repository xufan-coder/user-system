package com.zerody.user.controller;

import com.alibaba.fastjson.JSON;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.enums.UserTypeEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.SysCompanyInfo;
import com.zerody.user.dto.StaffByCompanyDto;
import com.zerody.user.service.SysAddressBookService;
import com.zerody.user.service.SysCompanyInfoService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.DepartInfoVo;
import com.zerody.user.vo.StaffInfoByAddressBookVo;
import com.zerody.user.vo.StaffInfoByCompanyVo;
import com.zerody.user.vo.SysAddressBookVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    private SysCompanyInfoService sysCompanyInfoService;
    @Autowired
    private SysAddressBookService sysAddressBookService;

    @Autowired
    private CheckUtil checkUtil;

    /***
     * 2022-6-25只用作boss下拉列表
     * @description 获取公司
     * @author zhangpingping
     * @date 2021/9/24
     * @param []
     * @return
     */
    @GetMapping(value = "/address-book")
    public DataResult<List<SysAddressBookVo>> queryAddressBook() {
        List<String> companyIds=null;
        Integer isProData=null;
        SysCompanyInfo byId=null;
        try {
            if (UserUtils.getUser().isBack()){
                companyIds =this.checkUtil.setBackCompany(UserUtils.getUserId());
            }else if (UserUtils.getUser().isCEO()){
                companyIds = this.checkUtil.setCeoCompany(UserUtils.getUserId());
            }else {
                String companyId = UserUtils.getUser().getCompanyId();
                if(DataUtil.isEmpty(companyId)){
                    return R.error("获取公司失败,请求企业错误！");
                }
                byId = sysCompanyInfoService.getById(companyId);
                if(DataUtil.isEmpty(byId)){
                    return R.error("获取公司失败,请求企业错误！");
                }
                isProData=byId.getIsProData();
            }
            List<SysAddressBookVo> sysAddressBookVos = this.sysAddressBookService.queryAddressBook(companyIds,isProData);
            return R.success(sysAddressBookVos);
        } catch (Exception e) {
            log.error("获取公司错误:{}", e);
            return R.error("获取公司失败,请求异常");
        }
    }


    /**************************************************************************************************
     **
     * 关联企业版本修改使用为本接口2022-6-25
     *  通讯录接口
     *
     * @param null
     * @return {@link null }
     * @author DaBai
     * @date 2022/6/25  16:24
     */
    @GetMapping(value = "/partner/address-book")
    public DataResult<List<SysAddressBookVo>> queryPartnerAddressBook() {
        SysCompanyInfo byId=null;
        Integer isProData=null;
        try {
            if (UserUtils.getUser().isBack()||UserUtils.getUser().isCEO()){
                //默认给有效企业
                isProData=1;
            }else {
                String companyId = UserUtils.getUser().getCompanyId();
                if(DataUtil.isEmpty(companyId)){
                    return R.error("获取公司失败,请求企业错误！");
                }
                byId = sysCompanyInfoService.getById(companyId);
                if(DataUtil.isEmpty(byId)){
                    return R.error("获取公司失败,请求企业错误！");
                }
            }
            isProData=byId.getIsProData();
            List<SysAddressBookVo> sysAddressBookVos = this.sysAddressBookService.queryAddressBook(null,isProData);
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
            if (StringUtils.isEmpty(id)) {
                id = UserUtils.getUser().getCompanyId();
            }
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
            if (StringUtils.isEmpty(id)) {
                id = UserUtils.getUser().getCompanyId();
            }
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
            if (UserUtils.getUser().isBack()){
                staffByCompanyDto.setCompanyIds(this.checkUtil.setBackCompany(UserUtils.getUserId()));
            }else if (UserUtils.getUser().isCEO()){
                staffByCompanyDto.setCompanyIds(this.checkUtil.setCeoCompany(UserUtils.getUserId()));
            }else {
                String companyId = UserUtils.getUser().getCompanyId();
                if(DataUtil.isEmpty(companyId)){
                    return R.error("获取公司失败,请求企业错误！");
                }
                SysCompanyInfo byId = sysCompanyInfoService.getById(companyId);
                if(DataUtil.isEmpty(byId)){
                    return R.error("获取公司失败,请求企业错误！");
                }
                staffByCompanyDto.setIsProData(byId.getIsProData());
            }

            return R.success(sysAddressBookService.getStaffByCompany(staffByCompanyDto));
        } catch (Exception e) {
            log.error("获取员工错误:{}", JSON.toJSONString(staffByCompanyDto), e);
            return R.error("获取员工失败,请求异常");
        }
    }


}
