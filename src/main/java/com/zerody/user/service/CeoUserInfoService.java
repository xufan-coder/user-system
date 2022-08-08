package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.AdminUserInfo;
import com.zerody.user.api.vo.CeoUserInfoVo;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.CeoUserInfoPageDto;
import com.zerody.user.dto.SetUpdateAvatarDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.vo.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

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

    IPage<BosStaffInfoVo> getCeoPage(SysStaffInfoPageDto param);

    SysUserInfoVo getCeoInfoByUserId(String userId);

    List<StaffInfoVo> getStaffInfoByIds(List<String> userId);

    List<String> getAllCeo();
}
