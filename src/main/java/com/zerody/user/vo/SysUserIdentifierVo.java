package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.domain.SysUserIdentifier;
import lombok.Data;

import java.util.Date;

/**
 * @author kuang
 **/
@Data
public class SysUserIdentifierVo extends SysUserIdentifier {

   /**
    * 是否绑定 (1 已绑定 0未绑定)
    **/
    private Integer binding;

    /**
     * 最后登录时间
     **/
    private Date lastLoginTime;

    private String username;
}
