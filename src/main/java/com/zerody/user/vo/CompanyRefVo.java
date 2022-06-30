package com.zerody.user.vo;

import lombok.Data;

import java.util.List;

/**
 * @author  DaBai
 * @date  2022/6/18 13:14
 */

@Data
public class CompanyRefVo {
    public CompanyRefVo() {
     super();
    }
    public CompanyRefVo(String id, String companyName) {
        this.id = id;
        this.companyName = companyName;
    }

    /**
     *
     *企业id
     */
    private String id;

    /**
     *
     *企业名称
     */
    private String companyName;


}
