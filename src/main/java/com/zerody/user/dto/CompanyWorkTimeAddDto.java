package com.zerody.user.dto;

import com.zerody.user.domain.UnionCompanyWorkTime;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author : chenKeFeng
 * @date : 2022/8/31 15:55
 */
@Data
public class CompanyWorkTimeAddDto {

    /**
     * 企业id
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 类型(1:用户、0:boss)
     */
    private Integer type;

    /**
     * boss账号id
     */
    private String ceoUserId;

    /**
     * 上班时间
     */
    private List<Integer> workingHours;

    /**
     * 打卡时间
     */
    private List<UnionCompanyWorkTimeDto> companyWorkTimes;

}
