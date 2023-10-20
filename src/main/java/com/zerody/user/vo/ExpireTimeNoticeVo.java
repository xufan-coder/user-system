package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.common.enums.customer.EducationBackgroundEnum;
import com.zerody.user.domain.CommonFile;
import com.zerody.user.domain.FamilyMember;
import com.zerody.user.domain.UserResume;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author  DaBai
 * @date  2023/10/7 11:33
 */

@Data
public class ExpireTimeNoticeVo {

    /**
     * userId
     */
    private String id;
    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 员工id
     */
    private String staffId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 创建时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date dateJoin;

    /**
     *  合约结束时间
     **/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expireTime;
}
