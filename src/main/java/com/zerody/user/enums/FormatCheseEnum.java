package com.zerody.user.enums;

/**
 * @author  kuang
 * @date  2021/5/6 15:38
 */

public  enum FormatCheseEnum {
    status("状态"),
    maritalStatus("婚姻状态"),
    gender("性别"),
    highestEducation("最高学历"),
    birthday("出生日期"),
    useState("账号状态"),
    isDiamondMember("是否钻石会员"),
    dateLeft("离职时间"),
    dateJoin("签约时间"),
    recommendType("推荐类型")
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
