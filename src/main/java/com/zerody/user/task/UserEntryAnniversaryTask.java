package com.zerody.user.task;

import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.contract.api.vo.SignOrderDataVo;
import com.zerody.expression.Expression;
import com.zerody.im.api.dto.SendRobotMessageDto;
import com.zerody.im.feign.SendMsgFeignService;
import com.zerody.im.util.IM;
import com.zerody.user.config.EntryMsgConfig;
import com.zerody.user.domain.UserBirthdayTemplate;
import com.zerody.user.dto.FlowMessageDto;
import com.zerody.user.feign.ContractFeignService;
import com.zerody.user.feign.CustomerFeignService;
import com.zerody.user.mapper.SysDepartmentInfoMapper;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.service.UserBirthdayTemplateService;
import com.zerody.user.util.CommonUtils;
import com.zerody.user.vo.AppUserNotPushVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author: YeChangWei
 * @Date: 2022/11/10 10:01
 */
@Component
@Slf4j
public class UserEntryAnniversaryTask {
    @Autowired
    private EntryMsgConfig entryMsgConfig;
    @Autowired
    private SendMsgFeignService sendMsgFeignService;
    @Autowired
    private UserBirthdayTemplateService userBirthdayTemplateService;
    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;
    @Autowired
    private SysDepartmentInfoMapper sysDepartmentInfoMapper;
    @Autowired
    private ContractFeignService contractFeignService;
    @Autowired
    private CustomerFeignService customerFeignService;

    /**
     * 消息的类型：流程提醒
     */
    private static final int MESSAGE_TYPE_FLOW = 1010;



    @XxlJob("user_entry_anniversary_task")
    public ReturnT<String> execute(String param) {


        //查询所有在职的人员,入职时间是在当天的,和入职年限
        List<AppUserNotPushVo> lists = sysUserInfoMapper.getAnniversaryUserList(null);
            for (AppUserNotPushVo user : lists) {

                if(user.getNum() != null && user.getNum() > 0){
                    String year = user.getNum() > 10 ? "10+" : String.valueOf(user.getNum());

                    String chineseNum =user.getNum() > 10 ? CommonUtils.CHINESE_LIST[10] : CommonUtils.CHINESE_LIST[user.getNum()] ;
                    String content = "亲爱的,"+user.getUserName()+"\n 小微集团祝您签约 "+chineseNum+"周年快乐!";
                    user.setContent(content);
                    // 获取当前时间推送的模板 时分
                    UserBirthdayTemplate template = userBirthdayTemplateService.getEntryTimeTemplate(year,new Date(), YesNo.YES);
                    // 进入推送时间 查询入职周年伙伴进行推送
                    if(template != null ) {
                        //生日祝福
                        user.setBlessing(template.getBlessing());

                        if (StringUtils.isNotEmpty(user.getCompanyId())) {
                            //推送总经理 查询该企业年统计量

                            //查询签单数量 和放款金额 和放款数
                            DataResult<SignOrderDataVo> signOrderData = contractFeignService.getSignOrderData(user.getCompanyId(), null, null);
                            if (signOrderData.isSuccess()) {
                                user.setSignOrderNum(signOrderData.getData().getSignOrderNum());
                                user.setLoansMoney(signOrderData.getData().getLoansMoney());
                                user.setLoansNum(signOrderData.getData().getLoansNum());
                            }
                            //查询录入客户数量
                            DataResult<Integer> importCustomerNum = customerFeignService.getImportCustomerNum(user.getCompanyId(), null, null);
                            if (importCustomerNum.isSuccess()) {
                                user.setImportCustomerNum(importCustomerNum.getData());
                            }
                            this.sendPush(entryMsgConfig.getUrl(), entryMsgConfig.getTitle(), user.getBlessing() + entryMsgConfig.getContent2(), user.getUserId(), user);
                        } else if (StringUtils.isNotEmpty(user.getDepartmentId())) {
                            //推送副总或团队长  查询该部门年统计量

                            //查询签单数量 和放款金额 和放款数
                            DataResult<SignOrderDataVo> signOrderData = contractFeignService.getSignOrderData(null, user.getDepartmentId(), null);
                            if (signOrderData.isSuccess()) {
                                user.setSignOrderNum(signOrderData.getData().getSignOrderNum());
                                user.setLoansMoney(signOrderData.getData().getLoansMoney());
                                user.setLoansNum(signOrderData.getData().getLoansNum());
                            }
                            //查询录入客户数量
                            DataResult<Integer> importCustomerNum = customerFeignService.getImportCustomerNum(null, user.getDepartmentId(), null);
                            if (importCustomerNum.isSuccess()) {
                                user.setImportCustomerNum(importCustomerNum.getData());
                            }
                            this.sendPush(entryMsgConfig.getUrl(), entryMsgConfig.getTitle(), user.getBlessing() + entryMsgConfig.getContent2(), user.getUserId(), user);

                        } else if (StringUtils.isEmpty(user.getCompanyId()) && StringUtils.isEmpty(user.getDepartmentId())) {

                            // 推送部门伙伴

                            //查询签单数量 和放款金额 和放款数
                            DataResult<SignOrderDataVo> signOrderData = contractFeignService.getSignOrderData(null, null, user.getUserId());
                            if (signOrderData.isSuccess()) {
                                user.setSignOrderNum(signOrderData.getData().getSignOrderNum());
                                user.setLoansMoney(signOrderData.getData().getLoansMoney());
                                user.setLoansNum(signOrderData.getData().getLoansNum());
                            }
                            //查询录入客户数量
                            DataResult<Integer> importCustomerNum = customerFeignService.getImportCustomerNum(null, null, user.getUserId());
                            if (importCustomerNum.isSuccess()) {
                                user.setImportCustomerNum(importCustomerNum.getData());
                            }
                            this.sendPush(entryMsgConfig.getUrl(), entryMsgConfig.getTitle(), user.getBlessing() + entryMsgConfig.getContent(), user.getUserId(), user);
                        }
                    }

                }
            }

        return ReturnT.SUCCESS;
    }

    private void sendPush(String url, String title, String content,String userId,AppUserNotPushVo user){
            try {
                    FlowMessageDto dto = new FlowMessageDto();
                    dto.setTitle(title);
                    dto.setMessageSource("extend");
                    dto.setUrl(url);
                    String query = Expression.parse(entryMsgConfig.getQuery(), user);
                    String arguments = Expression.parse(entryMsgConfig.getArguments(), user);
                    Object parse = JSONObject.parse(query);
                    Object argumentsParse = JSONObject.parse(arguments);
                    dto.setQuery(parse);
                    dto.setArguments(argumentsParse);
                    SendRobotMessageDto data = new SendRobotMessageDto();
                    // 推送至人文关怀
                    String msg = Expression.parse(content, user);
                    data.setSender(IM.ROBOT_CARE);
                    dto.setContent(msg);
                    data.setContent(msg);
                    data.setTarget(userId);
                    data.setContentPush(msg);
                    data.setContentExtra(com.zerody.flow.client.util.JsonUtils.toString(dto));
                    data.setType(MESSAGE_TYPE_FLOW);
                    DataResult<Long> result = this.sendMsgFeignService.send(data);
                    log.info("推送IM结果:{}", com.zerody.flow.client.util.JsonUtils.toString(result));
            } catch (Exception e) {
                log.error("推送IM出错:{}", e, e);
            }
    }


}
