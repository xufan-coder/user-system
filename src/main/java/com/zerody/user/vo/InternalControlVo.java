package com.zerody.user.vo;

import com.zerody.common.utils.DataUtil;
import com.zerody.user.util.CommonUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class InternalControlVo {

    /** 用户id **/
    private String userId;

    /** 伙伴id **/
    private String staffId;

    /** 是否存在 0:否；1是**/
    private Integer isInternalControl;

    /** 用户名称 **/
    private String userName;

    /** 手机号 **/
    private String phone;

    /** 公司名称 **/
    private String companyName;

    /** 部门名称 **/
    private String departName;

    /** 角色名 **/
    private String roleName;

    /** 证件号码 **/
    private String certificateCard;

    public String getCertificateCard() {
        String idCard = this.certificateCard;
        if (DataUtil.isEmpty(idCard)) {
            if (StringUtils.isEmpty(this.certificateCard)) {
                return  "";
            }
            idCard = certificateCard;
        }
        return CommonUtils.idEncrypt(idCard, 2, 2);
    }

    public String getPhone() {
        if (StringUtils.isEmpty(this.phone)) {
            return "";
        }
        return this.phone.replaceAll("(\\d{3})\\d{4}(\\w{4})", "$1****$2");
    }

}
