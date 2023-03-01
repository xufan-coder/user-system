package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.ConvertImage;
import com.zerody.user.service.ConvertImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName ConvertImageTask
 * @DateTime 2023/2/24 15:52
 */
@Component
public class ConvertImageTask {

    @Autowired
    private ConvertImageService convertImageService;


    @XxlJob("convert-image-task")
    public ReturnT<String> execute(String param) {
        ReturnT<String> returnT = ReturnT.SUCCESS;
        List<ConvertImage> converts = this.convertImageService.dohaveNotConvert();
        returnT.setMsg(String.format("转换 %s 条", converts.size()));
        if (DataUtil.isEmpty(converts)) {
            return returnT;
        }
        this.convertImageService.convertToImage(converts);
        return returnT;
    }
}
