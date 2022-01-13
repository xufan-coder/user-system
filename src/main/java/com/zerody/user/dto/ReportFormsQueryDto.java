package com.zerody.user.dto;

import com.zerody.user.dto.bean.SetTimePeriodPage;
import lombok.Data;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName ReportFormsQueryDto
 * @DateTime 2021/12/15_10:30
 * @Deacription TODO
 */
@Data
public class ReportFormsQueryDto extends SetTimePeriodPage {

    private List<String> userIds;

    private List<String> salesmanRoles;

    private String title;

    /** 查询类型 */
    private String queryType;

}
