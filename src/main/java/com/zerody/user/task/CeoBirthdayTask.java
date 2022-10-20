package com.zerody.user.task;

import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.DateUtil;
import com.zerody.expression.Expression;
import com.zerody.im.api.dto.SendRobotMessageDto;
import com.zerody.im.feign.SendMsgFeignService;
import com.zerody.im.util.IM;
import com.zerody.user.config.BirthdayMsgConfig;
import com.zerody.user.domain.UserBirthdayTemplate;
import com.zerody.user.dto.FlowMessageDto;
import com.zerody.user.feign.JPushFeignService;
import com.zerody.user.mapper.SysDepartmentInfoMapper;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.service.CeoUserInfoService;
import com.zerody.user.service.CompanyAdminService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.UserBirthdayTemplateService;
import com.zerody.user.vo.AppCeoUserNotPushVo;
import com.zerody.user.vo.AppUserNotPushVo;
import com.zerody.user.vo.CompanyAdminVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class CeoBirthdayTask {

    @Autowired
    private CeoUserInfoService ceoUserInfoService;

    @Autowired
    private CompanyAdminService companyAdminService;

    @Autowired
    private UserBirthdayTemplateService userBirthdayTemplateService;

    @Autowired
    private SendMsgFeignService sendMsgFeignService;

    @Autowired
    private BirthdayMsgConfig birthdayMsgConfig;

    @Value("${jpush.template.user-system.user-birthday:}")
    private String monthBirthdayTemplate;
    /**
     * 消息的类型：流程提醒
     */
    private static final int MESSAGE_TYPE_FLOW = 1010;



    @XxlJob("ceo_user_birthday_task")
    public ReturnT<String> execute(String param){
        // 获取当前时间 时分
        UserBirthdayTemplate template = userBirthdayTemplateService.getTimeTemplate(new Date());
        // 进入推送时间 查询生日伙伴进行推送
        if(template != null) {
            // 获取当天日期  月-日
            String month = DateUtil.getMonth();
            String day = DateUtil.getDay();

            //获取当天生日的ceo信息
            List<AppCeoUserNotPushVo> ceoBirthdayUserList = this.ceoUserInfoService.getCeoBirthdayUserIds(month, day);

            for (AppCeoUserNotPushVo ceoUser : ceoBirthdayUserList) {
                log.info("推送给自己 {}", ceoUser);
                Map params = new HashMap();
                params.put("userId", ceoUser.getCeoId());
                params.put("name", ceoUser.getUserName());
                params.put("dept", "");
                params.put("avatar", ceoUser.getAvatar());
                params.put("text", template.getBlessing());
                params.put("image", template.getPosterUrl());
                //推给自己
                this.sendPush(birthdayMsgConfig.getBirthdayUrl(),birthdayMsgConfig.getTitle(),template.getBlessing(), ceoUser.getCeoId(),params);

                // 推送给他关联的企业 总经理和副总
                // 查询出他关联的企业companyIds
                List<String> companyIds = ceoUser.getCompanyIds();
                log.info("--------companyIds:{}",companyIds);
                //查询总经理与副总
                List<CompanyAdminVo> companyAdmin = companyAdminService.getCompanyAdmin(companyIds);

                for (CompanyAdminVo companyAdminVo : companyAdmin) {
                    log.info("总经理 和副总推送: {}", companyAdminVo);
                    params.put("userId", companyAdminVo.getUserId());
                    params.put("text",birthdayMsgConfig.getContent2());
                    this.sendPush(birthdayMsgConfig.getUrl(),birthdayMsgConfig.getTitle2(), birthdayMsgConfig.getContent2(), companyAdminVo.getStaffId(), params);
                }

                //推送给其他ceo  不包含自己
                /*List<AppCeoUserNotPushVo> otherCeoBirthdayUser = this.ceoUserInfoService.getCeoBirthdayUserIds(null,null);
                for (AppCeoUserNotPushVo otherCeo : otherCeoBirthdayUser) {
                    // 过滤当前生日的ceo
                    if(ceoUser.getCeoId().equals(otherCeo.getCeoId())) {
                        continue;
                    }
                    log.info("推送给其他ceo {}", ceoUser);
                    params.put("userId", otherCeo.getCeoId());
                    // 推送给其他ceo  不包含自己
                    this.sendPush(birthdayMsgConfig.getUrl(),birthdayMsgConfig.getTitle2(), birthdayMsgConfig.getContent2(), ceoUser.getCeoId(), params);
                }*/
            }
        }
        return ReturnT.SUCCESS;
    }


    private void sendPush(String url, String title, String content,String userId, Map params){
        FlowMessageDto dto = new FlowMessageDto();
        dto.setTitle(title);
        dto.setMessageSource("extend");
        dto.setUrl(url);
        String query = Expression.parse(birthdayMsgConfig.getQuery(), params);
        Object parse = JSONObject.parse(query);
        dto.setQuery(parse);
        String arguments = Expression.parse(birthdayMsgConfig.getArguments(), params);
        Object argumentsParse = JSONObject.parse(arguments);
        dto.setQuery(parse);
        dto.setArguments(argumentsParse);

        SendRobotMessageDto data = new SendRobotMessageDto();
        data.setContent(Expression.parse(content, params));
        dto.setContent(Expression.parse(content, params));
        data.setSender(IM.ROBOT_CARE);
        data.setTarget(userId);
        data.setContentPush(Expression.parse(content, params));
        data.setContentExtra(com.zerody.flow.client.util.JsonUtils.toString(dto));
        data.setType(MESSAGE_TYPE_FLOW);
        DataResult<Long> result = this.sendMsgFeignService.send(data);
        log.info("推送IM入参:{} -----结果:{}", JSONObject.toJSONString(data),com.zerody.flow.client.util.JsonUtils.toString(result));


       /* AddJdPushDto push = new AddJdPushDto();
        push.setTemplateCode(monthBirthdayTemplate);
        push.setUserId(userId);
        push.setData(JSON.toJSONString(params));
        puthService.doAuroraPush(push);*/
    }
}
