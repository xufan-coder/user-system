package com.zerody.user.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysDepartmentInfoVo
 * @DateTime 2020/12/19_13:29
 * @Deacription TODO
 */
@Data
public class SysDepartmentInfoVo {

    /**
     *部门iD
     */
    private String id;

    /**
     *企业iD
     */
    private String compId;

    /**
     *
     *部门名称
     */
    private String departName;

    /**
     *
     *父级部门id
     */
    private String parentId;

    /**
     *
     *成立时间
     */
    private Date creatTime;

    /**
     *
     *联系电话
     */
    private String telephone;

    /**
     *
     *部门编码
     */
    private String departCode;

    /**
     *
     *部门描述
     */
    private String departDesc;

    /**
     *
     *子级部门集合
     */
    private List<SysDepartmentInfoVo> departChildrens;

    /**
     *
     *部门下的岗位
     */
    private List<SysJobPositionVo> jobChildrens;

    /**
     *
     *员工数量
     */
    private Integer staffCount;

    /**
     * 部门管理员 手机号
     */
    private String adminAccount;
}
