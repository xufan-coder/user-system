package com.zerody.user.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.common.utils.DateUtil;
import com.zerody.expression.Expression;
import com.zerody.im.api.dto.SendRobotMessageDto;
import com.zerody.im.feign.SendMsgFeignService;
import com.zerody.im.util.IM;
import com.zerody.jpush.api.dto.AddJdPushDto;
import com.zerody.user.config.BirthdayMsgConfig;
import com.zerody.user.domain.SysDepartmentInfo;
import com.zerody.user.domain.UserBirthdayTemplate;
import com.zerody.user.dto.FlowMessageDto;
import com.zerody.user.feign.JPushFeignService;
import com.zerody.user.mapper.CompanyAdminMapper;
import com.zerody.user.mapper.SysDepartmentInfoMapper;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.service.CompanyAdminService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.UserBirthdayTemplateService;
import com.zerody.user.vo.AppUserNotPushVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kuang
 * 同事生日推送查询
 */
@Component
@Slf4j
public class UserBirthdayTask {

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SysDepartmentInfoMapper sysDepartmentInfoMapper;

    @Autowired
    private UserBirthdayTemplateService userBirthdayTemplateService;

    @Autowired
    private SendMsgFeignService sendMsgFeignService;

    @Autowired
    private JPushFeignService puthService;

    @Autowired
    private BirthdayMsgConfig birthdayMsgConfig;

    @Value("${jpush.template.user-system.user-birthday:}")
    private String monthBirthdayTemplate;
    /**
     * 消息的类型：流程提醒
     */
    private static final int MESSAGE_TYPE_FLOW = 1010;

    @XxlJob("user_birthday_task")
    public ReturnT<String> execute(String param){
        // 获取当前时间 时分
        UserBirthdayTemplate template = userBirthdayTemplateService.getTimeTemplate(new Date());
        // 进入推送时间 查询生日伙伴进行推送
        if(template != null) {
            // 获取当天日期  月-日
            String month = DateUtil.getMonth();
            String day = DateUtil.getDay();
            List<AppUserNotPushVo> userList =  this.sysUserInfoMapper.getBirthdayUserIds(month,day,null);
            for(AppUserNotPushVo user : userList) {
                Map params = new HashMap();
                params.put("userId",user.getUserId());
                params.put("name",user.getUserName());
                params.put("dept",user.getUserDepartmentName());
                params.put("avatar",user.getAvatar());
                params.put("text",template.getBlessing());
                params.put("image",template.getPosterUrl());
                this.sendPush(birthdayMsgConfig.getBirthdayUrl(),birthdayMsgConfig.getTitle(),template.getBlessing(),user.getUserId(),params);

                // 是否推送给他人
                if(template.getNoticeColleague() == YesNo.YES){
                    // 查询是否是总经理  推送 副总和团队长
                    if(StringUtils.isNotEmpty(user.getCompanyId())) {
                        // 是否通知管理层
                        if(template.getAdminColleague() == YesNo.YES){
                            // 查询企业下的所有副总 和团队长
                            List<String> admins = this.sysDepartmentInfoMapper.getUserIds(user.getCompanyId(),null);

                            // 发送im 提醒生日祝福
                            if(admins.size() > 0) {
                                for(String userId : admins){
                                    params.put("text",birthdayMsgConfig.getContent2());
                                    this.sendPush(birthdayMsgConfig.getUrl(),birthdayMsgConfig.getTitle2(),birthdayMsgConfig.getContent2(),userId,params);
                                }
                            }
                        }

                    } else if(StringUtils.isNotEmpty(user.getDepartmentId())){
                        List<String> userIds;
                        // 是否通知管理层
                        if(template.getAdminColleague() == YesNo.YES){
                            // 查询是否是副总 和团队长 推送 团队长
                            if(StringUtils.isEmpty(user.getParentId())){
                                // 副总
                                // 查询此部门下的团队长
                                userIds = this.sysDepartmentInfoMapper.getUserIds(null,user.getDepartmentId());
                                //发送im 提示生日祝福
                            }else {
                                // 团队长
                                // 查询部门伙伴
                                userIds = this.sysDepartmentInfoMapper.getUserIdsByDepartmentId(user.getDepartmentId());
                            }

                            //发送im 提示生日祝福
                            // 移除自己 避免重复发送
                            userIds.remove(user.getUserId());
                            for(String userId : userIds){
                                params.put("text",birthdayMsgConfig.getContent2());
                                this.sendPush(birthdayMsgConfig.getUrl(),birthdayMsgConfig.getTitle2(),birthdayMsgConfig.getContent2(),userId,params);
                            }
                        }

                    }else {
                        log.info("伙伴生日推送：{}",user);
                        // 伙伴  推送伙伴
                        // 查询部门伙伴
                        List<String> userIds = this.sysDepartmentInfoMapper.getUserIdsByDepartmentId(user.getUserDepartmentId());

                        //发送im 提示生日祝福
                        // 移除自己 避免重复发送
                        userIds.remove(user.getUserId());
                        for(String userId : userIds){
                            params.put("text",birthdayMsgConfig.getContent());
                            this.sendPush(birthdayMsgConfig.getUrl(),birthdayMsgConfig.getTitle2(),birthdayMsgConfig.getContent(),userId,params);
                        }
                    }
                }
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
