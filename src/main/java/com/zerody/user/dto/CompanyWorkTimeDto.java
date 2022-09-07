package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

import java.util.Date;

/**
 * @author : chenKeFeng
 * @date : 2022/8/29 14:03
 */
@Data
public class CompanyWorkTimeDto extends PageQueryDto {

    /**
     * id
     */
    private String id;
    /**
     * 公司id
     */
    private String companyId;

    /**
     * 公司名称
     */
    private String companyName;

}
