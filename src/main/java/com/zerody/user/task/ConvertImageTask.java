package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.ConvertImage;
import com.zerody.user.service.ConvertImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName ConvertImageTask
 * @DateTime 2023/2/24 15:52
 */
@Slf4j
@Component
public class ConvertImageTask {

    @Autowired
    private ConvertImageService convertImageService;


    @XxlJob("convert-image-task")
    public ReturnT<String> execute(String param) {
        ReturnT<String> returnT = ReturnT.SUCCESS;
        String resultStr = "转换 %s条, 成功 %s条";
        int count = 0, success = 0;
        try {
            List<ConvertImage> converts = this.convertImageService.dohaveNotConvert();
            if (DataUtil.isEmpty(converts)) {
                return returnT;
            }
            count = converts.size();
            for (ConvertImage c : converts) {
                try {
                    this.convertImageService.doConvertToImage(c);
                    success++;
                } catch (Exception e) {
                    log.error("转换图片出错:{}", e, e);
                }
            }
        } catch (Exception e) {
            log.error("转换图片出错:{}", e, e);
        }
        returnT.setMsg(String.format(resultStr, count, success));
        return returnT;
    }
}
