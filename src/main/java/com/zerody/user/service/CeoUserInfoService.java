package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.api.vo.CeoUserInfoVo;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.dto.CeoUserInfoPageDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.vo.*;

import java.util.List;

/**
 * @author  DaBai
 * @date  2021/4/20 14:27
 */

public interface CeoUserInfoService extends IService<CeoUserInfo> {

    CeoUserInfo getByPhone(String userName);

    void updateCeoById(CeoUserInfoVo ceoUserInfoVo);

    CeoUserInfo getUserById(String id);

    void addCeoUser(CeoUserInfo ceoUserInfo);

    void updateCeoUser(CeoUserInfo ceoUserInfo);

    void deleteCeoUserById(String id);

    IPage<CeoUserInfo> selectCeoUserPage(CeoUserInfoPageDto ceoUserInfoPageDto);

    List<SubordinateUserQueryVo> getList();

    List<SubordinateUserQueryVo> getListCompany(String companyId);

    IPage<BosStaffInfoVo> getCeoPage(SysStaffInfoPageDto param);

    SysUserInfoVo getCeoInfoByUserId(String userId);

    List<StaffInfoVo> getStaffInfoByIds(List<String> userId);

    List<String> getAllCeo();

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 获取当天生日的ceo信息
    * @Date: 2022/10/20 15:18
    */
    List<AppCeoUserNotPushVo> getCeoBirthdayUserIds(String month, String day);

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 推送给其它ceo
    * @Date: 2022/10/20 15:18
    */
    List<AppCeoUserNotPushVo> getOtherCEOsBirthdayUser(String month, String day);

    /**
     * @Author: chenKeFeng
     * @param
     * @Description: 获取启动ceo账号
     * @Date: 2023/1/4 9:54
     */
    List<CeoUserVo> queryCeoList();

}
