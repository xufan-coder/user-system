package com.zerody.user.enums;

/**
 *  PRIMARY_SCHOOL("小学"), JUNIOR_HIGH("初中"), TECHNICAL_SECONDARY("中专"), SENIOR_HIGH("高中"),
 *  JUNIOR_COLLEGE("大专"), REGULAR_COLLEGE("本科"), MASTER("硕士"), DOCTOR("博士");
 * @author : chenKeFeng
 * @date : 2023/5/8 12:38
 */
public enum DegreeEnum {

    PRIMARY_SCHOOL("小学"),
    JUNIOR_HIGH("初中"),
    TECHNICAL_SECONDARY("中专"),
    SENIOR_HIGH("高中"),
    JUNIOR_COLLEGE("大专"),
    REGULAR_COLLEGE("本科"),
    MASTER("硕士"),
    DOCTOR("博士");

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    DegreeEnum(String text){
        this.text = text;
    }


    public static DegreeEnum getByCode(String code) {
        for (DegreeEnum status : DegreeEnum.values()){
            if(code.equals(status.name())){
                return status;
            }
        }
        return null;
    }


    public static DegreeEnum getValueOf(String enumName){
        try {
            return DegreeEnum.valueOf(enumName);
        } catch (Exception e) {
            return null;
        }
    }
}
