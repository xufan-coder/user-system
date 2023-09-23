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
import com.zerody.flow.client.util.JsonUtils;
import com.zerody.user.domain.StaffBlacklist;
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
import java.util.List;
import java.util.Map;


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
        String json =eventData.getVariables().get("blackDto") != null ?
                eventData.getVariables().get("blackDto").toString() : "";
        Object imgs =eventData.getVariables().get("imgs");
        Map<String, Object> variables = eventData.getVariables();
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
                StaffBlacklistAddDto dto =null;
                if(DataUtil.isNotEmpty(json)){
                    dto=JSON.parseObject(json, StaffBlacklistAddDto.class);
                    dto.getBlacklist().setProcessId(processInstId);
                    dto.getBlacklist().setProcessKey("EmployeeBlacklist");
                }else {
                    dto=new StaffBlacklistAddDto();
                    List<String> images= imgs != null? (List)imgs:null;
                    //组装参数 app发起只会是1类型的内部内控伙伴申请，所以只组装1类型的参数
                    dto.setImages(images);
                    StaffBlacklist staffBlacklist=new StaffBlacklist();
                    log.info("流程信息:{}", JsonUtils.toString(variables));
                    String type =variables.get("type")!= null?variables.get("type").toString():null;
                    String userId =variables.get("userId")!= null?variables.get("userId").toString():"";
                    String reason =variables.get("reason")!= null?variables.get("reason").toString():"";
                    String companyId =variables.get("companyId")!= null?variables.get("companyId").toString():"";
                    String startUserId =variables.get("start_user_id")!= null?variables.get("start_user_id").toString():"";
                    String startUseName =variables.get("start_user_name")!= null?variables.get("start_user_name").toString():"";
                    if(DataUtil.isNotEmpty(type)){
                        staffBlacklist.setType(Integer.parseInt(type));
                    }
                    staffBlacklist.setUserId(userId);
                    staffBlacklist.setReason(reason);
                    staffBlacklist.setCompanyId(companyId);
                    staffBlacklist.setProcessId(processInstId);
                    staffBlacklist.setProcessKey("EmployeeBlacklist");
                    staffBlacklist.setSubmitUserId(startUserId);
                    staffBlacklist.setSubmitUserName(startUseName);
                    dto.setBlacklist(staffBlacklist);
                }
                log.info("被拉黑的用户:{}", JsonUtils.toString(dto));
                if(BlacklistTypeEnum.EXTERNAL.getValue()== dto.getBlacklist().getType()){
                    this.staffBlacklistService.addStaffBlaklistJoin(dto);
                }else {
                    this.staffBlacklistService.addStaffBlaklist(dto,null);
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
