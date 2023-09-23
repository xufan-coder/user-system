package com.zerody.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : xufan
 * @create 2023/9/23 9:08
 */
@AllArgsConstructor
public enum  BlacklistApproveStateEnum {
    APPROVAL("审批中"),
    FAIL("拒绝"),
    SUCCESS("已通过");

    @Getter
    private String text;

    public static BlacklistApproveStateEnum getByCode(String code) {
        for (BlacklistApproveStateEnum status : BlacklistApproveStateEnum.values()){
            if(code.equals(status.name())){
                return status;
            }
        }
        return null;
    }


    public static BlacklistApproveStateEnum getValueOf(String enumName){
        try {
            return BlacklistApproveStateEnum.valueOf(enumName);
        } catch (Exception e) {
            return null;
        }
    }
}
