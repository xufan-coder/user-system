package com.zerody.user.vo;

import lombok.Data;

/**
 * @author  DaBai
 * @date  2021/4/28 10:38
 */
@Data
public class UserCompar {
    private String key;
    private String keyName;
    private Object oldValue;
    private Object newValue;

    private Object oldSubmitValue;
    private Object newSubmitValue;

    private boolean approvalFlag;

}
