package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.api.bean.DataResult;
import com.zerody.user.domain.SysUserIdentifier;
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

    void addApprove(String id, String approveState, String userId);

    void addApprove(String userId);

    Page<SysUserIdentifier> getPageUserIdentifier(SysUserIdentifierQueryDto queryDto);

    SysUserIdentifier getIdentifierInfo(String userId);

    SysUserIdentifier getIdentifierInfo(String userId,String id);

    SysUserIdentifierVo getUserIdentifierInfo(String userId);
}
