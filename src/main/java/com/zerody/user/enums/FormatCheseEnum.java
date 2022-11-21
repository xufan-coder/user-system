package com.zerody.user.enums;

/**
 * @author  DaBai
 * @date  2021/5/6 15:38
 */

public  enum FormatCheseEnum {
    status("状态"),
    maritalStatus("婚姻状态"),
    gender("性别"),
    highestEducation("最高学历"),
    birthday("出生日期")
    ;

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    FormatCheseEnum(String text){
        this.text = text;
    }


    public static FormatCheseEnum getByCode(String code) {
        for (FormatCheseEnum status : FormatCheseEnum.values()){
            if(code.equals(status.name())){
                return status;
            }
        }
        return null;
    }
    public static FormatCheseEnum getValueOf(String enumName){
        try {
            return FormatCheseEnum.valueOf(enumName);
        } catch (Exception e) {
            return null;
        }
    }

}
