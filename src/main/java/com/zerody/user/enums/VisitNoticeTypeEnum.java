package com.zerody.user.enums;
/**
 * @author  DaBai
 * @date  2021/1/11 17:59
 */

public enum VisitNoticeTypeEnum {
    DAY7(1, "7天未联系跟进提醒"),
    DAY15(2, "15天未联系跟进提醒"),
    DAY30(3, "30天未联系跟进提醒");

    private Integer code;

    private String desc;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    VisitNoticeTypeEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static VisitNoticeTypeEnum getCodeByDesc(String desc){
        for (VisitNoticeTypeEnum status : VisitNoticeTypeEnum.values()){
            if(desc.equals(status.getDesc())){
                return status;
            }
        }
        return null;
    }
}
