package com.zerody.user.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.util.CommonUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class BlackOperationRecordVo {

    /** 加入内控的用户姓名 */
    @Excel(name = "内控伙伴姓名",width = 20,orderNum = "1")
    private String blackName;

    /** 加入内控的身份证 */
    @Excel(name = "身份证",width = 20,orderNum = "2")
    private String identityCard;

    /** 加入内控的手机号 */
    @Excel(name = "手机号",width = 20,orderNum = "3")
    private String mobile;

    /** 内控部门 */
    @Excel(name = "所属部门",width = 20,orderNum = "4")
    private String blackCompanyDept;

    /** 加入内控日期 */
    @Excel(name = "被加入内控日期",width = 20,orderNum = "5")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String blackTime;

    /** 加入内控的原因 */
    @Excel(name = "被加入内控原因",width = 40,orderNum = "6")
    private String blackReason;

    /** 操作类型（0：查询，1：编辑）*/
    @Excel(name = "操作类型",width = 20,orderNum = "7")
    private String type;

    /** 操作人姓名 */
    @Excel(name = "操作人",width = 20,orderNum = "8")
    private String createName;

    /** 操作人部门 */
    @Excel(name = "所属部门",width = 20,orderNum = "9")
    private String companyDept;

    /** 操作时间 */
    @Excel(name = "操作时间",width = 20,orderNum = "10")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    public String getIdentityCard() {
        String identityCard = this.identityCard;
        if (DataUtil.isEmpty(identityCard)) {
            if (StringUtils.isEmpty(this.identityCard)) {
                return  "";
            }
            identityCard = identityCard;
        }
        return CommonUtils.idEncrypt(identityCard, 2, 2);
    }

    public String getMobile() {
        if (StringUtils.isEmpty(this.mobile)) {
            return "";
        }
        return this.mobile.replaceAll("(\\d{3})\\d{4}(\\w{4})", "$1****$2");
    }

    private String getBlackName(){
        if(StringUtils.isEmpty(this.blackName)){
            return "";
        }
        return CommonUtils.protectedName(blackName);
    }
}
