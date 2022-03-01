package com.zerody.user.vo;

import lombok.Data;

import java.util.List;

/**
 * @author  DaBai
 * @date  2022/3/1 13:57
 */
@Data
public class UseControlVo {

    /**
    *   是否全部禁止登录 1 是 0 否
    */
    private Integer closeAll;

    private String companyId;

    private List<UseControlTimeVo> weekInfo;

}
