package com.zerody.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysAuthRoleDto
 * @DateTime 2020/12/22_10:59
 * @Deacription TODO
 */
@Data
public class SysAuthRoleDto {

    private List<String> staffIds;

    private String roleId;

    private List<String> menuIds;
}
