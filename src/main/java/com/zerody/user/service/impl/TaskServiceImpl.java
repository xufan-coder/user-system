package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.customer.api.dto.CustomerStatisUnContactMsgDto;
import com.zerody.user.domain.Msg;
import com.zerody.user.enums.VisitNoticeTypeEnum;
import com.zerody.user.feign.CustomerStatisFeignService;
import com.zerody.user.service.MsgService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author  DaBai
 * @date  2021/1/11 17:43
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private CustomerStatisFeignService customerStatisFeignService;
    @Autowired
    private SysUserInfoService sysUserInfoService;
    @Autowired
    private MsgService msgService;

    @Override
    public void buildVisitNoticeInfo() {
        //获取所有用户ID
        List<String> list= sysUserInfoService.selectAllUserId();
        List<Msg> dtos= new ArrayList<>();
        //统计客户跟进提醒三种类型的，客户数
        for (String userId : list) {
            DataResult<CustomerStatisUnContactMsgDto> uncontact = customerStatisFeignService.uncontact(userId);

            if(uncontact.isSuccess()&&DataUtil.isNotEmpty(uncontact.getData())){
                CustomerStatisUnContactMsgDto data = uncontact.getData();
                if(DataUtil.isNotEmpty(data.getDay7Msg())){
                    dtos.add(buildNotice(userId, data.getDay7Msg(), VisitNoticeTypeEnum.DAY7.getDesc()));
                }
                if(DataUtil.isNotEmpty(data.getDay15Msg())){
                    dtos.add(buildNotice(userId, data.getDay15Msg(), VisitNoticeTypeEnum.DAY15.getDesc()));
                }
                if(DataUtil.isNotEmpty(data.getDay30Msg())){
                    dtos.add(buildNotice(userId, data.getDay30Msg(), VisitNoticeTypeEnum.DAY21.getDesc()));
                }
            }
        }
        log.info("客户未跟进消息:"+JSONObject.toJSONString(dtos));
        msgService.saveBatch(dtos);
    }

    public Msg buildNotice(String userId, String msg, String title){
        Msg dto=new Msg();
        dto.setId(UUIDutils.getUUID32());
        dto.setCreateTime(new Date());
        dto.setMessageContent(msg);
        dto.setUserId(userId);
        dto.setMessageTile(title);
        return dto;
    }
}
