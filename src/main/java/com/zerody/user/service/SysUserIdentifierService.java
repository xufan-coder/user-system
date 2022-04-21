package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.SysUserIdentifier;
import com.zerody.user.dto.SysUserIdentifierDto;
import com.zerody.user.dto.SysUserIdentifierQueryDto;
import com.zerody.user.vo.SysUserIdentifierVo;

import java.util.List;

/**
 * @author kuang
 * @date 2022年03月22日 15:34
 **/
public interface SysUserIdentifierService  extends IService<SysUserIdentifier> {

    /**
     * @author kuang
     * @date  2022年03月22日 16:00
     * @param data 账号绑定设备参数
     **/
    void addSysUserIdentifier(SysUserIdentifier data);

    void addIdentifier(SysUserIdentifier data);

    void addApply(String id, Integer state, String userId);
    void addApplyV2(SysUserIdentifierDto dto);

    void addApprove(String id, Integer state, UserVo userVo);

    void addApproveByProcess(String id, Integer state);

    void addUnbound(String userId,String updateUserId);

    Page<SysUserIdentifier> getPageUserIdentifier(SysUserIdentifierQueryDto queryDto);

    SysUserIdentifier getIdentifierInfo(String userId);

    SysUserIdentifier getIdentifierInfo(String userId,String id);

    SysUserIdentifierVo getUserIdentifierInfo(String userId);

}
