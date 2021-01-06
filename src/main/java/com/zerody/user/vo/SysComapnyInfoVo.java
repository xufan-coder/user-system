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
     *企业联系人(管理员)
     */
    private String contactName;

    /**
     *
     *联系人手机
     */
    private String contactPhone;

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
     *
     *部门集合(树形结构)
     */
    private List<SysDepartmentInfoVo> departs;

}
