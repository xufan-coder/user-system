package com.zerody.user.dto;

import com.zerody.user.domain.SysUserInfo;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName setSysUserInfoDto
 * @DateTime 2020/12/22_15:41
 * @Deacription TODO
 */
@Data
public class SetSysUserInfoDto {

   //用户对象 添加员工先添加一个用户
    @Valid
    private SysUserInfo sysUserInfo;

    //角色id集合
    private List<String> roleIds;

    //岗位id
    private String positionId;

    //部门id
    private String departId;

    //员工状态
    private Integer status;
}
