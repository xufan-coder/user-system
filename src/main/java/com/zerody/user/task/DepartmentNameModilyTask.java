package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.user.service.SysDepartmentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author PengQiang
 * @ClassName DepartmentNameModilyTask
 * @DateTime 2021/4/15_19:58
 * @Deacription TODO
 */
@Component
@Slf4j
public class DepartmentNameModilyTask {

    @Autowired
    private SysDepartmentInfoService departService;

    @XxlJob("update_redundancy_depart_name")
    public ReturnT<String> execute(String param) {
        departService.updateRedundancyDepartName();
        ReturnT r=ReturnT.SUCCESS;
        r.setMsg("成功");
       return r;
    }
}
