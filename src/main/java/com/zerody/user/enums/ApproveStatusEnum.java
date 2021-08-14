package com.zerody.user.enums;

/**
  * 通用审批状态
  * @author  DaBai
  * @date  2021/8/6 11:09
  */

public enum ApproveStatusEnum {


    APPROVAL("审批中"),
    FAIL("审批失败"),
    SUCCESS("审批成功")
    ;

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    ApproveStatusEnum(String text){
        this.text = text;
    }




    public static ApproveStatusEnum getByCode(String code) {
        for (ApproveStatusEnum status : ApproveStatusEnum.values()){
            if(code.equals(status.name())){
                return status;
            }
        }
        return null;
    }


    public static ApproveStatusEnum getValueOf(String enumName){
        try {
            return ApproveStatusEnum.valueOf(enumName);
        } catch (Exception e) {
            return null;
        }
    }
}
