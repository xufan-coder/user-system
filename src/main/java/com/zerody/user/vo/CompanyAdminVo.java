package com.zerody.user.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author : chenKeFeng
 * @date : 2022/10/20 16:17
 */
@Data
public class CompanyAdminVo {

    /**id**/
    private String id;
    /**员工ID**/
    private String staffId;
    /**状态:1在职、2离职、3合作**/
    private Integer state;
    /**企业id**/
    private String companyId;

    /**
     * 用户id
     **/
    private String userId;

    /**
     * 员工头像
     */
    private String avatar;

    /**
     * 用户姓名
     **/
    private String userName;

}
