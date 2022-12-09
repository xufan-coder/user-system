package com.zerody.user.dto.company;

import lombok.Data;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysCompanQuery
 * @DateTime 2022/12/9_16:09
 * @Deacription TODO
 */
@Data
public class SysCompanyQueryDto {

    /** 企业id集合 */
    private List<String> ids;
}
