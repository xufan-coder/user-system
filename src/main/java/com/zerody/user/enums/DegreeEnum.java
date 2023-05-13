package com.zerody.user.enums;

import com.zerody.common.utils.DataUtil;

import java.util.Objects;

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
    SENIOR_HIGH_UNDER("高中及以下"),
    JUNIOR_COLLEGE("大专"),
    REGULAR_COLLEGE("本科"),
    MASTER("硕士"),
    DOCTOR("博士");

    /**
     * 学历描述
     */
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


    /**
     * 中文转英文
     * @param text 中文描述
     * @return
     */
    public static String getByCode(String text) {
        if (DataUtil.isEmpty(text)) {
            return null;
        }
        for (DegreeEnum status : DegreeEnum.values()){
            if(status.getText().equals(text)){
                return status.name();
            }
        }
        return null;
    }


    /**
     * 英文转中文
     * @param name 英文描述
     * @return
     */
    public static String getByText(String name) {
        for (DegreeEnum status : DegreeEnum.values()){
            if(status.name().equals(name)){
                return status.getText();
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
