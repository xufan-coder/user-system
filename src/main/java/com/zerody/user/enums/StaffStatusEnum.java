package com.zerody.user.enums;

/**
 * @author PengQiang
 * @ClassName StaffStatusEnum
 * @DateTime 2020/12/22_16:43
 * @Deacription TODO
 */
public enum  StaffStatusEnum {
    BE_ON_THE_JOB(0, "在职"),
    DIMISSION(1, "离职"),
    DELETE(2, "删除"),
    COLLABORATE(3, "合作");


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

    StaffStatusEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static StaffStatusEnum getCodeByDesc(String desc){
        for (StaffStatusEnum status : StaffStatusEnum.values()){
            if(status.desc.equals(desc)){
                return status;
            }
        }
        return null;
    }
}
