package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.SysCodeEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.JsonUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.DateUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.expression.Expression;
import com.zerody.im.api.dto.SendRobotMessageDto;
import com.zerody.im.feign.SendMsgFeignService;
import com.zerody.im.util.IM;
import com.zerody.oauth.api.dto.RemoveTokenDto;
import com.zerody.oauth.api.dto.SendTemplateDataDto;
import com.zerody.user.constant.CommonConstants;
import com.zerody.user.domain.CallControl;
import com.zerody.user.domain.CallUseControl;
import com.zerody.user.domain.UseControl;
import com.zerody.user.domain.UsersUseControl;
import com.zerody.user.dto.FlowMessageDto;
import com.zerody.user.dto.UseControlTimeDto;
import com.zerody.user.enums.WeeKEnum;
import com.zerody.user.feign.OauthFeignService;
import com.zerody.user.service.*;
import com.zerody.user.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.zerody.user.mapper.CallControlMapper;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.alibaba.nacos.client.utils.EnvUtil.LOGGER;

@Service
public class CallControlServiceImpl extends ServiceImpl<CallControlMapper, CallControl> implements CallControlService{

    public static String call_tip = "亲爱的%s，目前您已经呼叫了%s个客户，" +
            "今日限制呼叫次数为%s个，如超过次数将限制您今天使用系统，" +
            "小藏提醒您今天辛苦了，注意劳逸结合，该休息了！";

    public static String leader_tip = "伙伴【%s-%s】因触发呼叫限制，已被强制退出登录系统，为确保客户信息安全，请及时了解情况！";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CallUseControlService callUseControlService;

    @Autowired
    private CallControlRecordService callControlRecordService;

    @Autowired
    private OauthFeignService oauthFeignService;

    @Autowired
    private CompanyAdminService companyAdminService;

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @Autowired
    private SendMsgFeignService sendMsgFeignService;

    @Override
    public CallControlVo getByCompany(String companyId) {
        CallControlVo vo =new CallControlVo();
        //查询该企业配置
        QueryWrapper<CallControl> qw =new QueryWrapper<>();
        qw.lambda().eq(CallControl::getCompanyId,companyId);
        List<CallControl> list = this.list(qw);

        if(DataUtil.isNotEmpty(list)){
            List<CallControlTimeVo> weekInfo=new ArrayList<>();
            for (CallControl callControl : list) {
                CallControlTimeVo timeVo=new CallControlTimeVo();
                BeanUtils.copyProperties(callControl,timeVo);
                weekInfo.add(timeVo);
            }
            vo.setWeekInfo(weekInfo);
        }
        vo.setCompanyId(companyId);
        return vo;
    }

    @Override
    public void addOrUpdate(CallControlVo param) {
        List<CallControlTimeVo> weekInfo = param.getWeekInfo();
        //查询该企业配置
        QueryWrapper<CallControl> qw =new QueryWrapper<>();
        qw.lambda().eq(CallControl::getCompanyId,param.getCompanyId());
        List<CallControl> list = this.list(qw);
        if(DataUtil.isNotEmpty(list)){
            //先清除
            this.remove(qw);
        }
        list.clear();
        for (CallControlTimeVo callControlTimeVo : weekInfo) {
            CallControl callControl=new CallControl();
            BeanUtils.copyProperties(callControlTimeVo,callControl);
            callControl.setCompanyId(param.getCompanyId());
            callControl.setUpdateTime(new Date());
            checkParams(callControl);
            list.add(callControl);
        }
        // 再新增配置
        this.saveBatch(list);
    }

    @Override
    public void submitCallControl(UserVo user) {
        //提交呼叫次数
        this.checkCallAuth(user);
    }


    public void checkCallAuth(UserVo user) {
        String companyId = user.getCompanyId();
        String userId = user.getUserId();
        String userName = user.getUserName();
        String deptId = user.getDeptId();
        //1.校验此人是否在白名单
        QueryWrapper<CallUseControl> qw =new QueryWrapper<>();
        qw.lambda().eq(CallUseControl::getUserId,userId);
        CallUseControl one = this.callUseControlService.getOne(qw);
        //白名单不控制呼叫次数限制
        if(DataUtil.isEmpty(one)){
            //2.查询当天该企业的配置， //查询该用户的企业允许呼叫的时间
            //当前周几 中文需要转换
            SimpleDateFormat formatter = new SimpleDateFormat("E", Locale.CHINA);
            String week = formatter.format(new Date());
            //当前时间 24小时制
            String hour = DateUtil.getHour();

            QueryWrapper<CallControl> cqw =new QueryWrapper<>();
            cqw.lambda().eq(CallControl::getCompanyId,companyId);
            cqw.lambda().eq(CallControl::getWeek,WeeKEnum.getNumberByText(week));
            CallControl callControl = this.getOne(cqw);
            if(DataUtil.isNotEmpty(callControl)){
                //判断是否在使用时间内
                if(Integer.parseInt(hour)>=callControl.getStart()
                        &&Integer.parseInt(hour)<=callControl.getEnd()){
                    String key=CommonConstants.CALL_FLAG+companyId+":"+userId;
                    //今天剩余的秒数作为过期时间
                    int second = 86400 - LocalTime.now().toSecondOfDay();
                    Integer count = invokeExceededTimes(key, second);
                    //预警次数
                    Integer tipNum = callControl.getTipNum();
                    //最大呼叫次数
                    Integer callNum = callControl.getCallNum();

                    if(count.equals(tipNum)){
                        String tip=String.format(call_tip,userName,tipNum,callNum);
                        throw new DefaultException(tip);
                    }

                    if(count.equals(callNum+1)){
                        //大于呼叫次数限制则强制退出
                        //新增一条限制记录
                        this.callControlRecordService.saveRecord(userId);
                        //SCRM不强制退出
                        oauthFeignService.removeToken(new RemoveTokenDto(userId,SysCodeEnum.ZERODY_SCRM_MINI.getCode()));
                        //通知上级
                        List<CompanyAdminVo> leaders = companyAdminService.getCompanyAdminByUserId(companyId, deptId);
                        sendIm(leaders,user);
                    }
                }else {
                    StringBuffer tip = new StringBuffer();
                    tip.append("本时间段禁止使用呼叫功能，请在以下时间使用：\r\n");
                    cqw.clear();
                    cqw.lambda().eq(CallControl::getCompanyId,companyId);
                    cqw.lambda().orderByAsc(CallControl::getWeek);
                    List<CallControl> list = this.list(cqw);
                    if(DataUtil.isNotEmpty(list)){
                        for (CallControl control : list) {
                            tip.append(WeeKEnum.getTextByNumber(control.getWeek())+":"+control.getStart()+"时~"+control.getEnd()+"时；\r\n");
                        }
                    }
                    throw new DefaultException(tip.toString());
                }
            }else {
                //如果没有配置，默认能呼叫，不限制次数
            }
        }
    }

    /**
    *   新增次数
    */
    public Integer invokeExceededTimes(String key, int time) {

        LOGGER.info("key值:{}",key);
        // 判断在redis中是否有key值
        Boolean redisKey = stringRedisTemplate.hasKey(key);
        if (redisKey) {
            // 对value进行加1操作
            stringRedisTemplate.opsForValue().increment(key,1);
            // 获取key所对应的value
            Integer count =Integer.parseInt(stringRedisTemplate.opsForValue().get(key));
            return count;
        }else {
            // 如果没有key值，对他进行添加到redis中
            stringRedisTemplate.opsForValue().set(key,"1",time, TimeUnit.SECONDS);
            return 1;
        }
    }


    public void sendIm(List<CompanyAdminVo> leaders,UserVo userVo){
        SysUserInfoVo sysUserInfoVo = sysStaffInfoService.selectStaffByUserId(userVo.getUserId());
        if(DataUtil.isNotEmpty(sysUserInfoVo)) {
            String message = String.format(leader_tip,sysUserInfoVo.getUserName(),sysUserInfoVo.getDepartName());
            for (CompanyAdminVo leader : leaders) {
                FlowMessageDto dto = new FlowMessageDto();
                // 标题
                dto.setTitle("伙伴呼叫限制通知");
                dto.setContent(message);

                SendRobotMessageDto data = new SendRobotMessageDto();
                data.setContent(message);
                data.setTarget(leader.getUserId());
                data.setContentPush(message);
                data.setContentExtra(JsonUtils.toString(dto));
                data.setType(1);
                com.zerody.common.api.bean.DataResult<Long> result = this.sendMsgFeignService.send(data);
                LOGGER.info(leader.getUserId()+"呼叫限制通知推送IM结果:{}", com.zerody.flow.client.util.JsonUtils.toString(result));
            }
        }
    }


    public void checkParams(CallControl callControl){
        if(DataUtil.isEmpty(callControl.getWeek())){
            throw new DefaultException("星期参数错误！");
        }
        if(DataUtil.isEmpty(callControl.getCallNum())){
            throw new DefaultException("限制次数参数错误！");
        }
        if(DataUtil.isEmpty(callControl.getTipNum())){
            throw new DefaultException("预警次数参数错误！");
        }
        if(DataUtil.isEmpty(callControl.getStart())){
            throw new DefaultException("开始时间参数错误！");
        }
        if(DataUtil.isEmpty(callControl.getEnd())){
            throw new DefaultException("结束时间参数错误！");
        }
    }
}
