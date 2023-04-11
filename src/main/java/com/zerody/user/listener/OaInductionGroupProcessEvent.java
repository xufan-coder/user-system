package com.zerody.user.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zerody.common.utils.DataUtil;
import com.zerody.flow.api.event.FlowEvent;
import com.zerody.flow.api.event.FlowEventData;
import com.zerody.flow.api.event.FlowEventType;
import com.zerody.flow.api.event.TaskData;
import com.zerody.flow.api.state.FlowState;
import com.zerody.flow.client.event.FlowEventHandler;
import com.zerody.user.domain.UserInductionRecord;
import com.zerody.user.domain.UserInductionSplitRecord;
import com.zerody.user.enums.ApproveStatusEnum;
import com.zerody.user.handler.ImMessageHandler;
import com.zerody.user.service.UserInductionRecordService;
import com.zerody.user.service.UserInductionSplitRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 流程结束监听器
 * @author  kuang
 * @date  2023/03/10 16:11
 */

@Component
public class OaInductionGroupProcessEvent implements FlowEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OaInductionGroupProcessEvent.class);

    @Autowired
    private UserInductionSplitRecordService inductionSplitRecordService;
    @Autowired
    private ImMessageHandler imMessageHandler;
    @Override
    public void handle(FlowEventData eventData) {
        log.info("全局处理器收到消息-【{}】业务-结束的【{}】：{}",
                eventData.getProcessDefKey(),
                eventData.getFlowData().getProcessName(),
                JSON.toJSONString(eventData)
        );
        String processInstId = eventData.getProcessInstId();
        //监听流程变量
        FlowState flowState = eventData.getFlowData().getFlowState();
        if(DataUtil.isNotEmpty(flowState)) {

            QueryWrapper<UserInductionSplitRecord> qw =new QueryWrapper<>();
            qw.lambda().eq(UserInductionSplitRecord::getProcessId,processInstId);
            UserInductionSplitRecord induction = inductionSplitRecordService.getOne(qw);
            if(DataUtil.isNotEmpty(induction)) {

                induction.setUpdateTime(new Date());

                if (flowState.getState() == FlowState.approved.getState()) {
                // 审批通过业务代码处理
                    inductionSplitRecordService.doRenewInduction(induction);
                }else if (flowState.getState() == FlowState.rejected.getState()) {
                // 拒绝业务代码处理
                    induction.setApproveState(ApproveStatusEnum.FAIL.name());
                    inductionSplitRecordService.updateById(induction);
                }
            }

        }
        imMessageHandler.handle(eventData);
        FlowEventType eventType = eventData.getEventType();
        log.info("【事件类型】:{}", eventType.getStateName());
        log.debug("【流程变量】：{}", eventData.getVariables());
        log.info("------------------------------------------------------------");
    }

    @Override
    public FlowEvent getEvent() {
		FlowEvent event = new FlowEvent("OA_induction_GROUP", null, FlowEventType.PROCESS_END);
        return event;
    }
}
