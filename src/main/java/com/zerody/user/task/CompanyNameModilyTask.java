package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.user.service.SysCompanyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author PengQiang
 * @ClassName CompanyNameModilyTask
 * @DateTime 2021/4/30_9:32
 * @Deacription TODO
 */
@Component
public class CompanyNameModilyTask {

    @Autowired
    private SysCompanyInfoService companyService;

    /**
     *
     *  修改冗余的企业名称
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/4/30 10:26
     * @param                param
     * @return               com.xxl.job.core.biz.model.ReturnT<java.lang.String>
     */
    @XxlJob("update_redundancy_company_name")
    public ReturnT<String> execute(String param) {
        this.companyService.updateRedundancyCompanyName();
        return ReturnT.SUCCESS;
    }
}
