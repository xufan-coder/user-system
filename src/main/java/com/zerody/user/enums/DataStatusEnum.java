package com.zerody.user.enums;

/**
 * 数据记录状态 1 有效 0 无效 -1 删除
 * @Description: 数据记录状态 1 有效 0 无效 -1 删除
 * @Author:        pengqiang
 * @CreateDate:     2020/12/17 10:08
 * @Version:        1.0
 */
public enum DataStatusEnum {

    VALID(1,"有效"),
    DELETED(0,"删除");

    private int code;

    private String text;

    DataStatusEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }


    public int getCode() {
        return code;
    }


    public void setCode(int code) {
        this.code = code;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static DataStatusEnum getByCode(Integer code){
        for(DataStatusEnum e : DataStatusEnum.values()){
            if(e.code == code){
                return e;
            }
        }
        return null;
    }
}
