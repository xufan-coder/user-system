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
import com.zerody.user.enums.ApproveStatusEnum;
import com.zerody.user.handler.ImMessageHandler;
import com.zerody.user.service.UserInductionRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * 流程结束监听器
 * @author  DaBai
 * @date  2022/12/1 11:16
 */

@Component
public class InductionProcessEvent implements FlowEventHandler {
    private static final Logger log = LoggerFactory.getLogger(InductionProcessEvent.class);

    @Autowired
    private UserInductionRecordService inductionRecordService;
    @Autowired
    private ImMessageHandler imMessageHandler;
    @Override
    public void handle(FlowEventData eventData) {
        log.info("全局处理器收到消息-【{}】业务-结束的【{}】：{}",
                eventData.getProcessDefKey(),
                eventData.getFlowData().getProcessName(),
                JSON.toJSONString(eventData)
        );
        TaskData taskData = eventData.getTaskData();
        String processInstId = eventData.getProcessInstId();
        //监听流程变量
        FlowState flowState = eventData.getFlowData().getFlowState();
        if(DataUtil.isNotEmpty(flowState)) {

            QueryWrapper<UserInductionRecord> qw =new QueryWrapper<>();
            qw.lambda().eq(UserInductionRecord::getProcessId,processInstId);
            UserInductionRecord induction = inductionRecordService.getOne(qw);
            if(DataUtil.isNotEmpty(induction)) {

                induction.setUpdateTime(new Date());

                if (flowState.getState() == FlowState.repealed.getState()) {
                    // 撤销业务代码处理
                    induction.setApproveState(ApproveStatusEnum.REVOKE.name());
                    inductionRecordService.updateById(induction);
                }else  if (flowState.getState() == FlowState.approved.getState()) {
                // 审批通过业务代码处理
                    inductionRecordService.doRenewInduction(induction);
                }else if (flowState.getState() == FlowState.rejected.getState()) {
                // 拒绝业务代码处理
                    induction.setApproveState(ApproveStatusEnum.FAIL.name());
                    inductionRecordService.updateById(induction);
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
		FlowEvent event = new FlowEvent("Induction", null, FlowEventType.PROCESS_END);
        return event;
    }
}
