package com.zerody.user.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zerody.common.constant.YesNo;
import com.zerody.common.utils.DataUtil;
import com.zerody.flow.api.event.FlowEvent;
import com.zerody.flow.api.event.FlowEventData;
import com.zerody.flow.api.event.FlowEventType;
import com.zerody.flow.api.event.TaskData;
import com.zerody.flow.api.state.FlowState;
import com.zerody.flow.client.event.FlowEventHandler;
import com.zerody.user.domain.SysUserIdentifier;
import com.zerody.user.enums.ApproveStatusEnum;
import com.zerody.user.enums.IdentifierEnum;
import com.zerody.user.service.SysUserIdentifierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 解绑设备的【流程】结束事件类型
 * @author  DaBai
 * @date  2022/4/19 16:09
 */

@Component
public class UnbindDeviceProcessEvent implements FlowEventHandler {
    private static final Logger log = LoggerFactory.getLogger(UnbindDeviceProcessEvent.class);

    @Autowired
    private SysUserIdentifierService service;
    @Override
    public void handle(FlowEventData eventData) {
        log.info("全局处理器收到消息-【{}】业务线-结束的【{}】：{}",
                eventData.getProcessDefKey(),
                eventData.getFlowData().getProcessName(),
                JSON.toJSONString(eventData)
        );
        TaskData taskData = eventData.getTaskData();
        String processInstId = eventData.getProcessInstId();
        //监听流程变量是撤销则修改状态为撤销
        FlowState flowState = eventData.getFlowData().getFlowState();
        if(DataUtil.isNotEmpty(flowState)) {
            QueryWrapper<SysUserIdentifier> qw =new QueryWrapper<>();
            qw.lambda().eq(SysUserIdentifier::getProcessId,processInstId);
            SysUserIdentifier identifier = this.service.getOne(qw);
            if(DataUtil.isNotEmpty(identifier)) {
                if (flowState.getState() == FlowState.repealed.getState()) {
                    //处理撤销逻辑
                    identifier.setApproveState(ApproveStatusEnum.REVOKE.name());
                    identifier.setState(IdentifierEnum.INVALID.getValue());
                    identifier.setUpdateTime(new Date());
                    this.service.updateById(identifier);
                    //添加一行新的回退原来的状态
                    this.service.addIdentifier(identifier);
                } else if (flowState.getState() == FlowState.rejected.getState()) {
                    //处理拒绝逻辑
                    this.service.addApproveByProcess(identifier.getId(), YesNo.NO);
                }else if (flowState.getState() == FlowState.approved.getState()) {
                    //处理通过逻辑
                    this.service.addApproveByProcess(identifier.getId(), YesNo.YES);
                }
            }
        }
        FlowEventType eventType = eventData.getEventType();
        log.info("【事件类型】:{}", eventType.getStateName());
        log.debug("【流程变量】：{}", eventData.getVariables());
        log.info("------------------------------------------------------------");
    }

    @Override
    public FlowEvent getEvent() {
		FlowEvent event = new FlowEvent("UnbindDevice", null, FlowEventType.PROCESS_END);
        return event;
    }
}
