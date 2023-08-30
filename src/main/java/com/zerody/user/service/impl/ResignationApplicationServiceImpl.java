package com.zerody.user.service.impl;

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
                    //参数值
                    Map params = new HashMap();
                    params.put("userId",staffInfoVo.getUserId());
                    params.put("name",sysUserInfo.getUserName());
                    params.put("type",0);
                    params.put("departId",staffInfoVo.getDepartId());
                    params.put("companyId",staffInfoVo.getCompanyId());
                    params.put("title","客户跟进提醒");

                    //推送小藏参数
                    FlowMessageDto dto= new FlowMessageDto();
                    // 标题
                    dto.setTitle("客户跟进提醒");
                    //来源
                    dto.setMessageSource("extend");
                    // 跳转路径
                    dto.setUrl(loanCustomerConfig.getUrl());

                    //参数值替换
                    JSONObject json = new JSONObject(params);
                    dto.setQuery(json);
                    dto.setArguments(json);
                    log.info("查看参数：{}",dto.getArguments());
                    SendRobotMessageDto data = new SendRobotMessageDto();
                    String massage = Expression.parse(loanCustomerConfig.getContent(), params);
                    dto.setContent(massage);
                    data.setContent(massage);
                    data.setTarget(staffInfoVo.getUserId());
                    data.setContentPush(massage);
                    data.setContentExtra(com.zerody.flow.client.util.JsonUtils.toString(dto));
                    data.setType(MESSAGE_TYPE_FLOW);
                    log.info("参数"+JSONObject.toJSONString(data));
                    DataResult<Long> result = this.sendMsgFeignService.send(data);
                    log.info("推送IM结果:{}", JSONObject.toJSONString(result));

                    //发送给上上级
                    StaffInfoVo infoVo = this.sysUserInfoService.getSuperiorNotCompanyAdmin(staffInfoVo.getUserId());
                    log.info("上上级：{}",infoVo);
                    if(DataUtil.isNotEmpty(infoVo)){
                        //参数值
                        Map param = new HashMap();
                        params.put("userId",infoVo.getUserId());
                        params.put("name",infoVo.getUserName());
                        params.put("type",0);
                        params.put("departId",infoVo.getDepartId());
                        params.put("companyId",infoVo.getCompanyId());
                        param.put("title","客户跟进提醒");

                        //推送小藏参数
                        FlowMessageDto dtos= new FlowMessageDto();
                        // 标题
                        dtos.setTitle("客户跟进提醒");
                        //来源
                        dtos.setMessageSource("extend");
                        // 跳转路径
                        dtos.setUrl(loanSuperiorConfig.getUrl());

                        //参数值替换
                        JSONObject jsons = new JSONObject(param);
                        dtos.setQuery(jsons);
                        dtos.setArguments(jsons);

                        SendRobotMessageDto datas = new SendRobotMessageDto();
                        String massages = Expression.parse(loanSuperiorConfig.getContent(), param);
                        dtos.setContent(massages);
                        datas.setContent(massages);
                        datas.setTarget(infoVo.getUserId());
                        datas.setContentPush(massages);
                        datas.setContentExtra(com.zerody.flow.client.util.JsonUtils.toString(dtos));
                        datas.setType(MESSAGE_TYPE_FLOW);
                        log.info("参数"+JSONObject.toJSONString(datas));
                        DataResult<Long> results = this.sendMsgFeignService.send(datas);
                        log.info("推送IM结果:{}", JSONObject.toJSONString(results));
                    }
                }

            }
        }

    }
}
