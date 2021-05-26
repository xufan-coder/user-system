package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.user.service.SysCompanyInfoService;
import com.zerody.user.service.SysDepartmentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 企业操作
 * @author PengQiang
 * @ClassName CompanyEditTask
 * @DateTime 2021/5/21_19:09
 * @Deacription TODO
 */
@Component
@Slf4j
public class DeprtmentEditTask {
    @Autowired
    private SysDepartmentInfoService departmentInfoService;

    @XxlJob("department_edit_table")
    public ReturnT<String> execute(String param){
        // 获取所有用户ID
        departmentInfoService.doDepartmentEditInfo();

        return ReturnT.SUCCESS;
    }
}
