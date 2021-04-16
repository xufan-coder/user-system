package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.user.service.CardUserService;
import com.zerody.user.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 修改名片用户手机号绑定
 * @author  DaBai
 * @date  2021/4/16 10:16
 */

@Component
@Slf4j
public class CardBindPhoneTask {

    @Autowired
    private CardUserService cardUserService;

    @XxlJob("card_user_bind")
    public ReturnT<String> execute(String param){
        cardUserService.updateCardUserBind();
        return ReturnT.SUCCESS;
    }
}
