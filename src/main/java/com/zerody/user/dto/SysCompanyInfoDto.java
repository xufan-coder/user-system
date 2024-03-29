package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.bean.PageInfo;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SysCompanyInfoDto
 * @DateTime 2020/12/18_17:47
 * @Deacription TODO
 */
@Data
public class SysCompanyInfoDto extends PageQueryDto {

    /**
     *
     * 企业名称
     * @author               PengQiang
     * @description //TODO   DELL
     * @date                 2020/12/30 20:11
     * @param
     * @return
     */
    private String companyName;

    /**
     *  企业登录状态(1.启动、2.停用)
     *
     * @author               PengQiang
     * @description //TODO   DELL
     * @date                 2020/12/30 20:11
     * @param
     * @return
     */
    private Integer status;


    /** 企业id */
    private String companyId;

    /** 部门id */
    private String departId;
}
