package com.zerody.user.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zerody.common.api.bean.R;
import com.zerody.common.constant.YesNo;
import com.zerody.common.utils.DataUtil;
import com.zerody.contract.api.mq.CustomerLoansProductInfo;
import com.zerody.flow.api.event.FlowEvent;
import com.zerody.flow.api.event.FlowEventData;
import com.zerody.flow.api.event.FlowEventType;
import com.zerody.flow.api.event.TaskData;
import com.zerody.flow.api.state.FlowState;
import com.zerody.flow.client.event.FlowEventHandler;
import com.zerody.user.domain.StaffBlacklistApprover;
import com.zerody.user.domain.SysUserIdentifier;
import com.zerody.user.domain.UserInductionRecord;
import com.zerody.user.dto.StaffBlacklistAddDto;
import com.zerody.user.enums.ApproveStatusEnum;
import com.zerody.user.enums.BlacklistTypeEnum;
import com.zerody.user.enums.IdentifierEnum;
import com.zerody.user.service.StaffBlacklistApproverService;
import com.zerody.user.service.StaffBlacklistService;
import com.zerody.user.service.SysUserIdentifierService;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 伙伴内控的【流程】结束事件类型
 * @author  DaBai
 * @date  2023/9/21 11:15
 */

@Component
public class EmployeeBlacklistProcessEvent implements FlowEventHandler {
    private static final Logger log = LoggerFactory.getLogger(EmployeeBlacklistProcessEvent.class);


    @Autowired
    private StaffBlacklistApproverService approverService;

    @Autowired
    private StaffBlacklistService staffBlacklistService;

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
        String json = eventData.getVariables().get("blackDto").toString();
        String lastHandlerName = eventData.getVariables().get("last_handler_name") != null ?
                eventData.getVariables().get("last_handler_name").toString() : "";
        if(DataUtil.isNotEmpty(flowState)) {

            QueryWrapper<StaffBlacklistApprover> qw =new QueryWrapper<>();
            qw.lambda().eq(StaffBlacklistApprover::getProcessId,processInstId);
            StaffBlacklistApprover one = approverService.getOne(qw);
            boolean approveFlag=false;
            if(DataUtil.isNotEmpty(one)){
                approveFlag=true;
            }
            if (flowState.getState() == FlowState.repealed.getState()) {
                //处理撤销逻辑
              if(approveFlag){
                  one.setApproveState(ApproveStatusEnum.REVOKE.name());
              }
            } else if (flowState.getState() == FlowState.rejected.getState()) {
                //处理拒绝逻辑
                if(approveFlag){
                    one.setApproveState(ApproveStatusEnum.FAIL.name());
                    one.setApproveTime(new Date());
                    one.setApproverName(lastHandlerName);
                }
            }else if (flowState.getState() == FlowState.approved.getState()) {
                //处理通过逻辑
                if(approveFlag){
                    one.setApproveState(ApproveStatusEnum.SUCCESS.name());
                    one.setApproveTime(new Date());
                    one.setApproverName(lastHandlerName);
                }
                //通过之后添加到内控
                if(DataUtil.isNotEmpty(json)){
                    StaffBlacklistAddDto dto = JSON.parseObject(json, StaffBlacklistAddDto.class);
                    if(BlacklistTypeEnum.EXTERNAL.getValue()== dto.getBlacklist().getType()){
                        this.staffBlacklistService.addStaffBlaklistJoin(dto);
                    }else {
                        this.staffBlacklistService.addStaffBlaklist(dto,null);
                    }
                }
            }
            if(DataUtil.isNotEmpty(one)){
                one.setUpdateTime(new Date());
                approverService.updateById(one);
            }
        }
        FlowEventType eventType = eventData.getEventType();
        log.info("【事件类型】:{}", eventType.getStateName());
        log.debug("【流程变量】：{}", eventData.getVariables());
        log.info("------------------------------------------------------------");
    }

    @Override
    public FlowEvent getEvent() {
		FlowEvent event = new FlowEvent("EmployeeBlacklist", null, FlowEventType.PROCESS_END);
        return event;
    }
}
