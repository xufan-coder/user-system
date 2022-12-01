package com.zerody.user.listener;

import com.alibaba.fastjson.JSON;
import com.zerody.common.utils.DataUtil;
import com.zerody.flow.api.event.FlowEvent;
import com.zerody.flow.api.event.FlowEventData;
import com.zerody.flow.api.event.FlowEventType;
import com.zerody.flow.api.event.TaskData;
import com.zerody.flow.api.state.FlowState;
import com.zerody.flow.client.event.FlowEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 流程结束监听器
 * @author  DaBai
 * @date  2022/12/1 11:16
 */

@Component
public class InductionProcessEvent implements FlowEventHandler {
    private static final Logger log = LoggerFactory.getLogger(InductionProcessEvent.class);

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
            if (flowState.getState() == FlowState.repealed.getState()) {
               // 撤销业务代码处理

            }else  if (flowState.getState() == FlowState.approved.getState()) {
                // 审批通过业务代码处理

            }else if (flowState.getState() == FlowState.rejected.getState()) {
                // 拒绝业务代码处理

            }
        }

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
