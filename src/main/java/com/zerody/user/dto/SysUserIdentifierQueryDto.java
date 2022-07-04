package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

import java.util.List;

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
     * 部门id
     **/
    private String departId;

    private String userId;

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

    /**
    *   boss后台隔离数据
    */
    private List<String> companyIds;
}
