package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.util.CommonUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

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
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getIdCard() {
        String idCard = this.idCard;
        if (DataUtil.isEmpty(idCard)) {
            if (org.apache.commons.lang.StringUtils.isEmpty(this.idCard)) {
                return  "";
            }
            idCard = idCard;
        }
        return CommonUtils.idEncrypt(idCard, 2, 2);
    }

    public String getMobile() {
        if (StringUtils.isEmpty(this.mobile)) {
            return "";
        }
        return this.mobile.replaceAll("(\\d{3})\\d{4}(\\w{4})", "$1****$2");
    }

}
