package com.zerody.user.vo;

import lombok.Data;

/**
 * @author zhangpingping
 * @date 2021年09月24日 16:24
 */
@Data
public class SysAddressBookVo {
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 公司ID
     */
    private String compId;
    /**
     * 员工统计
     */
    private Integer staffCountId;

}
