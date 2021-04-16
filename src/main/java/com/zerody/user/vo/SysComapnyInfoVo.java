package com.zerody.user.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysComapnyInfoVo
 * @DateTime 2020/12/18_18:01
 * @Deacription TODO
 */
@Data
public class SysComapnyInfoVo {

    /**
     *
     *企业id
     */
    private String id;

    /**
     *
     *企业名称
     */
    private String companyName;

    /**
     *
     *企业联系人
     */
    private String contactName;

    /**
     *
     *联系人手机
     */
    private String contactPhone;

    /**
     *
     *公司地址省编码
     */
    private String companyAddrProvinceCode;

    /**
     *
     *公司地址市编码
     */
    private String companyAddressCityCode;

    /**
     *
     *公司地址
     */
    private String companyAddress;

    /**
     *
     *公司地址区编码
     */
    private String companyAddressAreaCode;

    /**
     *
     *企业状态状态
     */
    private Integer status;

    /**
     *
     *备注
     */
    private String remark;


    /**
     * 企业管理员id
     */
    private String adminAccount;

    /**
     * 管理员名称
     */
    private String adminName;
    /**
     *
     *部门集合(树形结构)
     */
    private List<SysDepartmentInfoVo> departs;

}
