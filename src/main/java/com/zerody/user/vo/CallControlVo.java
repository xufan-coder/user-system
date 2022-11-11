package com.zerody.user.vo;

import lombok.Data;

import java.util.List;

/**
 * @author  DaBai
 * @date  2022/11/9 10:35
 */

@Data
public class CallControlVo {

    private String companyId;

    private List<CallControlTimeVo> weekInfo;

}
