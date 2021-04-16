/**
 *
 */
package com.zerody.user.dto;

import lombok.Data;

/**
 * @author  DaBai
 * @date  2021/1/11 10:36
 */

@Data
public class AdminUserDto{

    /**
    *   选中的员工ID
    */
    private String staffId;
    /**
    *    平台角色ID
    */
    private String roleId;
    /**
     *    平台角色名称
     */
    private String roleName;

    /**
     *    id
     */
    private String id;

}
