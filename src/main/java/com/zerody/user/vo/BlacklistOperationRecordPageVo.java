package com.zerody.user.vo;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;

import java.util.Date;

/**f
 * @Author : xufan
 * @create 2023/3/8 18:06
 */

@Data
public class BlacklistOperationRecordPageVo {

    /** 操作记录id */
    private String id;

    /** 内控名单id */
    private String blacklistId;

    /**内控伙伴名称*/
    private String blackName;

    /** 身份证号*/
    private String idCard;

    /** 手机号*/
    private String mobile;


    /** 内控伙伴所属部门名称*/
    private String blackDeptName;

    /** 内控伙伴所属公司名称*/
    private String blackCpyName;

    /** 被加入内控名单日期*/
    private String blackTime;

    /** 被加入内控原因*/
    private String reason;

    /** 操作类型（0：查询，1：编辑）*/
    private Integer type;

    /** 操作人姓名 */
    private String createName;

    /** 操作人所属部门名称 */
    private String operateDeptName;

    /** 操作人所属公司名称 */
    private String operateCpyName;

    /** 操作时间 */
    private Date createTime;

}
