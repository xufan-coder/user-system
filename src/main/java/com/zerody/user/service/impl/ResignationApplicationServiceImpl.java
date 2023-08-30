package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.expression.Expression;
import com.zerody.im.api.dto.SendRobotMessageDto;
import com.zerody.im.feign.SendMsgFeignService;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.config.LoanCustomerConfig;
import com.zerody.user.config.LoanSuperiorConfig;
import com.zerody.user.domain.*;
import com.zerody.user.dto.FlowMessageDto;
import com.zerody.user.dto.ResignationPageDto;
import com.zerody.user.enums.ApproveStatusEnum;
import com.zerody.user.enums.VisitNoticeTypeEnum;
import com.zerody.user.feign.ContractFeignService;
import com.zerody.user.feign.SignOrderFeignService;
import com.zerody.user.mapper.ResignationApplicationMapper;
import com.zerody.user.service.PositionRecordService;
import com.zerody.user.service.ResignationApplicationService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.PrepareExecutiveRecordVo;
import com.zerody.user.vo.SysUserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  DaBai
 * @date  2021/8/5 15:10
 */

@Slf4j
@Service
public class ResignationApplicationServiceImpl extends ServiceImpl<ResignationApplicationMapper, ResignationApplication> implements ResignationApplicationService {

    @Autowired
    private SysStaffInfoService sysStaffInfoService;
    @Autowired
    private PositionRecordService positionRecordService;
    @Autowired
    private PrepareExecutiveRecordServiceImpl prepareExecutiveRecordService;
    @Autowired
    private SysUserInfoService sysUserInfoService;
    @Autowired
    private ContractFeignService contractFeignService;
    @Autowired
    private LoanCustomerConfig loanCustomerConfig;
    @Autowired
    private LoanSuperiorConfig loanSuperiorConfig;
    @Autowired
    private SendMsgFeignService sendMsgFeignService;
    /**
     * 消息的类型：流程提醒
     */
    private static final int MESSAGE_TYPE_FLOW = 1010;

    @Override
    public ResignationApplication addOrUpdateResignationApplication(ResignationApplication data) {
        SysUserInfoVo sysUserInfoVo = null;
        if (DataUtil.isNotEmpty(data.getUserId())) {
            sysUserInfoVo = sysStaffInfoService.selectStaffByUserId(data.getUserId(),null,false);
        }
        if(DataUtil.isNotEmpty(data.getId())){
            data.setApprovalTime(new Date());
            this.updateById(data);
        }else {
            if(DataUtil.isNotEmpty(data.getUserId())){
                if(DataUtil.isNotEmpty(sysUserInfoVo)){
                    data.setStaffId(sysUserInfoVo.getStaffId());
                    data.setName(sysUserInfoVo.getUserName());
                    data.setCompanyId(sysUserInfoVo.getCompanyId());
                    data.setCompanyName(sysUserInfoVo.getCompanyName());
                    data.setDepartId(sysUserInfoVo.getDepartId());
                    data.setDepartName(sysUserInfoVo.getDepartName());
                    data.setPositionId(sysUserInfoVo.getPositionId());
                    data.setPositionName(sysUserInfoVo.getPositionName());

                    //预备高管
                    PrepareExecutiveRecordVo prepareExecutiveRecord = prepareExecutiveRecordService.getPrepareExecutiveRecordInner(data.getUserId());
                    if(DataUtil.isNotEmpty(prepareExecutiveRecord)){
                        SysUserInfo byId = this.sysUserInfoService.getById(data.getUserId());
                        if(DataUtil.isNotEmpty(byId)){
                            if(prepareExecutiveRecord.getEnterDate().after(data.getResignationTime())){
                                throw new DefaultException("当前离职时间小于预备高管入学时间，不允许离职");
                            }
                            byId.setIsPrepareExecutive(2);
                            PrepareExecutiveRecord record= new PrepareExecutiveRecord();
                            BeanUtils.copyProperties(prepareExecutiveRecord,record);
                            record.setIsPrepareExecutive(2);
                            record.setOutDate(data.getResignationTime());
                            record.setOutReason(data.getReason());
                            log.info("记录-----------"+record);
                            this.prepareExecutiveRecordService.updateById(record);
                            this.sysUserInfoService.updateById(byId);
                        }

                    }

                }
            }
            data.setCreateTime(new Date());
            this.save(data);
        }
        return data;
    }

    @Override
    public IPage<ResignationApplication> selectPage(ResignationPageDto dto) {
        Page<ResignationApplication> page = new Page<ResignationApplication>();
        page.setCurrent(dto.getCurrent());
        page.setSize(dto.getPageSize());
        QueryWrapper<ResignationApplication> qw = new QueryWrapper<>();
        qw.lambda().orderByDesc(ResignationApplication::getCreateTime);
        qw.lambda().eq(DataUtil.isNotEmpty(dto.getCompanyId()),ResignationApplication::getCompanyId,dto.getCompanyId())
                .eq(DataUtil.isNotEmpty(dto.getDepartId()),ResignationApplication::getDepartId,dto.getDepartId())
                .eq(DataUtil.isNotEmpty(dto.getUserId()),ResignationApplication::getUserId,dto.getUserId());
        //过滤ceo和后台企业关联
        qw.lambda().in(DataUtil.isNotEmpty(dto.getCompanyIds()),ResignationApplication::getCompanyId,dto.getCompanyIds());

        IPage<ResignationApplication> pageResult = this.page(page,qw);
        return pageResult;
    }

    @Override
    public List<ResignationApplication> doLeaveUsers() {
        //当天已批准离职的
        QueryWrapper<ResignationApplication> qw = new QueryWrapper<>();
        qw.select("1 as leave_state", "id", "user_id", "reason","leave_type");
        qw.lambda().eq(ResignationApplication::getApprovalState,ApproveStatusEnum.SUCCESS);
        qw.lambda().eq(ResignationApplication::getLeaveState, YesNo.NO);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateFormat = format.format(new Date());
        qw.lambda().between(ResignationApplication::getResignationTime,nowDateFormat+" 00:00:00",nowDateFormat+" 23:59:59");
        List<ResignationApplication> result = this.list(qw);
        if (DataUtil.isNotEmpty(result)) {
            this.updateBatchById(result);
        }
        return result;
    }

    @Override
    public void leaveUserIm(String userId){
        DataResult<List<String>> customerIds = this.contractFeignService.getCustomerId(userId);
        if(customerIds.isSuccess()){
            SysUserInfo sysUserInfo = this.sysUserInfoService.getById(userId);
            List<String> customerId = customerIds.getData();
            log.info("是否有放款用户：{}",customerId);
            if(DataUtil.isNotEmpty(customerId) && customerId.size() > 0){
                //发送给上级
                StaffInfoVo staffInfoVo = this.sysUserInfoService.getSuperiorNotCompanyAdmin(userId);
                log.info("上级用户：{}",staffInfoVo);
                if(DataUtil.isNotEmpty(staffInfoVo)){
                    String queryStr = String.format(loanCustomerConfig.getQuery(),staffInfoVo.getUserId());
                    Object parse = JSONObject.parse(queryStr);

                    String msg =
                            String.format(loanCustomerConfig.getContent(),sysUserInfo.getUserName(),0,
                                    sysUserInfo.getId(),staffInfoVo.getDepartId(),staffInfoVo.getCompanyId());

                    String argumentsStr = String.format(loanCustomerConfig.getQuery(),staffInfoVo.getUserId());
                    Object parse1 = JSONObject.parse(argumentsStr);

                    JSONArray array = new JSONArray();
                    JSONObject json = new JSONObject();
                    json.put("name","查看签单记录");
                    json.put("h5Url",null);
                    json.put("query",parse);
                    json.put("arguments",parse1);
                    json.put("messageSource","extend");
                    array.add(json);

                    Map<String,Object> dataMap = new HashMap<>();
                    dataMap.put("title",loanCustomerConfig.getTitle());
                    dataMap.put("content",msg);
                    dataMap.put("buttons",array);

                    SendRobotMessageDto data = new SendRobotMessageDto();
                    data.setContent(msg);
                    data.setTarget(staffInfoVo.getUserId());
                    data.setContentPush(msg);
                    data.setContentExtra(JSONObject.toJSONString(dataMap));
                    data.setType(1013);
                    data.setConversationType(0);
                    data.setPersistFlag(3);
                    com.zerody.common.api.bean.DataResult<Long> result = sendMsgFeignService.send(data);
                    log.info("推送参数结果:{}", JSONObject.toJSONString(data));
                    log.info("推送IM结果:{}", JSONObject.toJSONString(result));

                    //发送给上上级
                    StaffInfoVo infoVo = this.sysUserInfoService.getSuperiorNotCompanyAdmin(staffInfoVo.getUserId());
                    log.info("上上级：{}",infoVo);
                    if(DataUtil.isNotEmpty(infoVo)){
                        String queryStrs = String.format(loanSuperiorConfig.getQuery(),infoVo.getUserId());
                        Object parses = JSONObject.parse(queryStrs);

                        String msgs =
                                String.format(loanCustomerConfig.getContent(),sysUserInfo.getUserName(),0,
                                        sysUserInfo.getId(),staffInfoVo.getDepartId(),staffInfoVo.getCompanyId());

                        String argumentsStrs = String.format(loanSuperiorConfig.getQuery(),infoVo.getUserId());
                        Object parse1s = JSONObject.parse(argumentsStrs);

                        JSONArray arrays = new JSONArray();
                        JSONObject jsons = new JSONObject();
                        jsons.put("name","查看签单记录");
                        jsons.put("h5Url",null);
                        jsons.put("query",parses);
                        jsons.put("arguments",parse1s);
                        jsons.put("messageSource","extend");
                        arrays.add(jsons);

                        Map<String,Object> dataMaps = new HashMap<>();
                        dataMaps.put("title",loanSuperiorConfig.getTitle());
                        dataMaps.put("content",msgs);
                        dataMaps.put("buttons",arrays);

                        SendRobotMessageDto datas = new SendRobotMessageDto();
                        datas.setContent(msgs);
                        datas.setTarget(infoVo.getUserId());
                        datas.setContentPush(msgs);
                        datas.setContentExtra(JSONObject.toJSONString(dataMaps));
                        datas.setType(1013);
                        datas.setConversationType(0);
                        datas.setPersistFlag(3);
                        com.zerody.common.api.bean.DataResult<Long> results = sendMsgFeignService.send(datas);
                        log.info("推送参数结果:{}", JSONObject.toJSONString(datas));
                        log.info("推送IM结果:{}", JSONObject.toJSONString(results));
                    }

                }

            }
        }

    }
}
