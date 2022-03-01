package com.zerody.user.enums;

/**
 * @author  DaBai
 * @date  2022/3/1 18:29
 */

public enum WeeKEnum {

    Mon("星期一",1),
    Tues("星期二",2),
    Wed("星期三",3),
    Thur("星期四",4),
    Fri("星期五",5),
    Sat("星期六",6),
    Sun("星期日",7);


    private String text;

    private Integer number;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    WeeKEnum(String text,Integer number){
        this.text = text;
        this.number = number;
    }

    public static Integer getNumberByText(String text){
        for (WeeKEnum weeKEnum : WeeKEnum.values()){
            if(text.equals(weeKEnum.getText())){
                return weeKEnum.getNumber();
            }
        }
        return null;
    }
}
