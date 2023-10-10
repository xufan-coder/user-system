package com.zerody.user.task;

import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.util.JsonUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.DateUtil;
import com.zerody.expression.Expression;
import com.zerody.im.api.dto.SendRobotMessageDto;
import com.zerody.im.feign.SendMsgFeignService;
import com.zerody.im.util.IM;
import com.zerody.user.domain.ResignationApplication;
import com.zerody.user.dto.ExpireTimeNoticeDto;
import com.zerody.user.dto.FlowMessageDto;
import com.zerody.user.service.ResignationApplicationService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.vo.ExpireTimeNoticeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.alibaba.nacos.client.utils.EnvUtil.LOGGER;

/**
 * 合约到期伙伴通知
 * @author  DaBai
 * @date  2023/10/7 10:43
 */

@Component
@Slf4j
public class ExpireTimeNoticeTask {
    public static String tip= "%s您好，您和%s在%s签订的合约即将在%s到期，请您及时联系相关人员进行处理，以免影响您的工作。";
    public static String title= "合约到期提醒";
    @Autowired
    private SendMsgFeignService sendMsgFeignService;
    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @XxlJob("staff_expire_notice")
    public ReturnT<String> execute(String param){
        ReturnT r=ReturnT.SUCCESS;
        try {
            //查询一个月或/三个月后到期的伙伴
            Date date = new Date();
            ExpireTimeNoticeDto dto=new ExpireTimeNoticeDto();
            dto.setThirty(DateUtil.addDay(date, 30));
            dto.setNinety(DateUtil.addDay(date, 90));
            List<ExpireTimeNoticeVo> list=sysStaffInfoService.getExpireTimeStaff(dto);
            if(DataUtil.isNotEmpty(list)){
                //IM通知
                for (ExpireTimeNoticeVo expireTimeNoticeVo : list) {
                    this.sendPush(expireTimeNoticeVo);
                }
            }

        } catch (Exception e) {
            log.error("合约到期通知出错:{}", e, e);
            r.setMsg(e.getMessage());
        }
        return r;
    }


    private void sendPush(ExpireTimeNoticeVo vo){
        FlowMessageDto dto = new FlowMessageDto();
        // 标题
        dto.setTitle(title);
        DateFormat dfs = new SimpleDateFormat("yyyy年MM月dd日");
        String dateJoin = dfs.format(vo.getDateJoin());
        String expireTime = dfs.format(vo.getExpireTime());
        String message = String.format(tip,vo.getUserName(),vo.getCompanyName(),dateJoin,expireTime);
        dto.setContent(message);

        SendRobotMessageDto data = new SendRobotMessageDto();
        data.setContent(message);
        data.setSender(IM.ROBOT_XIAOZANG);
        data.setTarget(vo.getId());
        data.setContentPush(message);
        data.setContentExtra(JsonUtils.toString(dto));
        data.setType(1014);
        com.zerody.common.api.bean.DataResult<Long> result = this.sendMsgFeignService.send(data);
        LOGGER.info(vo.getUserName()+"-合约到期通知推送IM结果:{}", com.zerody.flow.client.util.JsonUtils.toString(result));

    }
}
