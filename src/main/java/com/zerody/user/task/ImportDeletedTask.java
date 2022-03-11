package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.user.service.ImportInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author PengQiang
 * @ClassName ImportDeletedTask
 * @DateTime 2022/3/7_11:32
 * @Deacription TODO
 */
@Slf4j
@Component
@RefreshScope
public class ImportDeletedTask {

    @Autowired
    private ImportInfoService service;

    @XxlJob("import_deleted_task")
    public ReturnT<String> execute(String param){
        service.deletedImportTiming();
        return ReturnT.SUCCESS;
    }
}
