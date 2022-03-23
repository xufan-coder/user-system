package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

/**
 * @author kuang
 **/
@Data
public class SysUserIdentifierQueryDto extends PageQueryDto {

    /**
     * 企业id
     **/
    private String companyId;

    /**
     * 伙伴名称(用户名称)
     **/
    private String userName;

    /**
     *
     * 用户手机号
     **/
    private String mobile;

    /**
     * 审批状态
     **/
    private String approveState;

}
