package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.bean.DataResult;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.AdminUserInfo;
import com.zerody.user.api.vo.CeoUserInfoVo;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.SetUpdateAvatarDto;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.vo.CheckLoginVo;
import com.zerody.user.vo.LoginUserInfoVo;
import com.zerody.user.vo.SysLoginUserInfoVo;
import com.zerody.user.vo.UserTypeInfoVo;

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
}
