package com.zerody.user.dto;

import com.zerody.common.vo.UserVo;
import lombok.Data;
/**
 * @author kuang
 **/

@Data
public class SysUserIdentifierDto {

    private String id;

    private Integer state;

    private String userId;

    private UserVo user;
}
