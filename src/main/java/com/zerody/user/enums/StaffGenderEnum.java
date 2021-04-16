package com.zerody.user.enums;

/**
 * @author PengQiang
 * @ClassName StaffGenderEnum
 * @DateTime 2020/12/17_14:36
 * @Deacription TODO
 */
public enum StaffGenderEnum {
    //男
    MALE(0,"男"),
    //女
    FEMALE(1,"女"),
    //未知
    UNKNOW(2,"未知");

    private Integer value;

    private String desc;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    StaffGenderEnum(Integer value, String desc){
        this.desc = desc;
        this.value = value;
    }

    public static StaffGenderEnum checkVal(String desc){
        for (StaffGenderEnum gender : StaffGenderEnum.values()){
            if(gender.getValue().equals(desc)){
                return gender;
            }
        }
        return null;
    }
}
