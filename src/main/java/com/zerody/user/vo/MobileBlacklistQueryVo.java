package com.zerody.user.vo;

import com.alibaba.nacos.common.utils.StringUtils;
import com.zerody.user.util.CommonUtils;
import lombok.Data;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName MobileBlacklistQueryVo
 * @DateTime 2021/8/5_15:41
 * @Deacription TODO
 */
@Data
public class MobileBlacklistQueryVo {

    /** 是否被拉黑 */
    private Boolean isBlock;

    /** 企业名称 */
    private String companyName;

    /** 企业id */
    private String companyId;

    /** 拉黑的原因 */
    private String reason;

    /** 状态：0.在职、1.离职、2.黑名单、3.合作中*/
    private Integer status;

    /** 用户姓名 */
    private String userName;

    /** 手机号 */
    private String phoneNumber;

    /** 部门名称 */
    private String departName;

    /** 手机号脱敏 */
    private String phoneNumberPri;

    public String getphoneNumberPri() {
        if(StringUtils.isNotEmpty(phoneNumber)){
            return CommonUtils.mobileEncrypt(phoneNumber);
        }
        return phoneNumber;
    }


}
