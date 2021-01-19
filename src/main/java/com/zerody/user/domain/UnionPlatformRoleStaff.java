package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
/**
 * @author  DaBai
 * @date  2021/1/11 10:33
 */

@Data
public class UnionPlatformRoleStaff {
    @TableId(type = IdType.UUID)
    private String id;

    /** 员工id **/
    private String staffId;

    /** 平台用户id **/
    private String adminUserId;

    /** 角色id **/
    private String roleId;

    /** 角色名 **/
    private String roleName;

}