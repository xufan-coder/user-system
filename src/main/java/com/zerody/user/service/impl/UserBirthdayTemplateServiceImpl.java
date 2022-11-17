package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.DateUtil;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.vo.UserVo;
import com.zerody.contract.api.vo.SignOrderDataVo;
import com.zerody.expression.Expression;
import com.zerody.im.api.dto.SendRobotMessageDto;
import com.zerody.im.feign.SendMsgFeignService;
import com.zerody.user.domain.UnionBirthdayMonth;
import com.zerody.user.domain.UserBirthdayTemplate;
import com.zerody.user.dto.BlessIngParam;
import com.zerody.user.dto.TemplatePageDto;
import com.zerody.user.dto.UserBirthdayTemplateDto;
import com.zerody.user.feign.ContractFeignService;
import com.zerody.user.feign.CustomerFeignService;
import com.zerody.user.mapper.*;
import com.zerody.user.service.UnionBirthdayMonthService;
import com.zerody.user.service.UserBirthdayTemplateService;
import com.zerody.user.util.CommonUtils;
import com.zerody.user.vo.AppCeoUserNotPushVo;
import com.zerody.user.vo.AppUserNotPushVo;
import com.zerody.user.vo.SysStaffInfoDetailsVo;
import com.zerody.user.vo.UserBirthdayTemplateVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kuang
 */
@Slf4j
@Service
public class UserBirthdayTemplateServiceImpl extends ServiceImpl<UserBirthdayTemplateMapper, UserBirthdayTemplate> implements UserBirthdayTemplateService {

    @Autowired
    private UnionBirthdayMonthService unionBirthdayMonthService;

    @Autowired
    private SysStaffInfoMapper sysStaffInfoMapper;

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SendMsgFeignService sendMsgFeignService;

    @Autowired
    private CeoUserInfoMapper ceoUserInfoMapper;

    @Autowired
    private UserBirthdayTemplateService userBirthdayTemplateService;
    @Autowired
    private ContractFeignService contractFeignService;
    @Autowired
    private CustomerFeignService customerFeignService;

    /**
     * 消息的类型：流程提醒
     */
    private static final int MESSAGE_TYPE_FLOW = 1010;


    @Override
    public void addTemplate(UserVo user, UserBirthdayTemplateDto dto) {
        UserBirthdayTemplate template = new UserBirthdayTemplate();
        BeanUtils.copyProperties(dto,template);
        template.setCreateBy(user.getUserId());
        template.setCreateTime(new Date());
        template.setCreateUsername(user.getUserName());
        template.setId(UUIDutils.getUUID32());
        if(dto.getType()==YesNo.NO){
            int cnt = unionBirthdayMonthService.getMonthCount(null,dto.getMonthList(),dto.getType());
            if(cnt > 0) {
                throw new DefaultException("该生日月份已有模板记录");
            }
            this.save(template);
            unionBirthdayMonthService.addTemplateMonth(dto.getMonthList(),template.getId());
        }else if(dto.getType()==YesNo.YES){
            int cnt = unionBirthdayMonthService.getYearCount(null,dto.getYearList(),dto.getType());
            if(cnt > 0) {
                throw new DefaultException("该入职年份已有模板记录");
            }
            this.save(template);
            unionBirthdayMonthService.addTemplateYear(dto.getYearList(),template.getId());
        }
    }

    @Override
    public void modifyTemplate(UserVo user, UserBirthdayTemplateDto dto) {
        UserBirthdayTemplate template = new UserBirthdayTemplate();
        BeanUtils.copyProperties(dto,template);
        template.setUpdateBy(user.getUserId());
        template.setUpdateTime(new Date());

        if(dto.getType()==YesNo.NO){
            int cnt = unionBirthdayMonthService.getMonthCount(dto.getId(),dto.getMonthList(),dto.getType());
            if(cnt > 0) {
                throw new DefaultException("该生日月份已有模板记录");
            }
            this.updateById(template);
            unionBirthdayMonthService.addTemplateMonth(dto.getMonthList(),template.getId());
        }else if(dto.getType()==YesNo.YES){
            int cnt = unionBirthdayMonthService.getYearCount(dto.getId(),dto.getYearList(),dto.getType());
            if(cnt > 0) {
                throw new DefaultException("该入职年份已有模板记录");
            }
            this.updateById(template);
            unionBirthdayMonthService.addTemplateYear(dto.getYearList(),template.getId());
        }

    }

    @Override
    public Page<UserBirthdayTemplateVo> getTemplate(TemplatePageDto queryDto) {
        Page<UserBirthdayTemplateVo> page = new Page<>(queryDto.getCurrent(),queryDto.getPageSize());

        return this.baseMapper.getTemplatePage(queryDto,page);
    }

    @Override
    public UserBirthdayTemplateVo getTemplateInfo() {

        return this.baseMapper.getTemplateInfo( DateUtil.getMonth());
    }

    @Override
    public UserBirthdayTemplateVo getNoticeInfo(String userId) {
        UserBirthdayTemplateVo templateVo = this.baseMapper.getTemplateInfo( DateUtil.getMonth());
        if(templateVo != null) {
            // 设置同事生日模板
            SysStaffInfoDetailsVo detailsVo = sysStaffInfoMapper.getStaffinfoDetails(userId);
            if(detailsVo != null) {
                Map params = new HashMap();
                params.put("name", detailsVo.getUserName());
                params.put("dep", detailsVo.getDepartName());
                String noticeText = Expression.parse(templateVo.getNoticeText(), params);
                templateVo.setNoticeText(noticeText);
            }
        }
        return templateVo;
    }

    @Override
    public UserBirthdayTemplate getTimeTemplate(Date time,Integer type) {
        return this.baseMapper.getTemplateByTime( DateUtil.getMonth(), time,type);
    }

    @Override
    public UserBirthdayTemplate getEntryTimeTemplate(String year,Date time,Integer type) {
        return this.baseMapper.getTemplateByYear( year, time,type);
    }

    @Override
    public UserBirthdayTemplateVo getTemplateInfo(String templateId) {

        return this.baseMapper.getTemplateInfoById(templateId);
    }

    @Override
    public void modifyTemplateById(String id) {
        UpdateWrapper<UserBirthdayTemplate> uw = new UpdateWrapper<>();
        uw.lambda().set(UserBirthdayTemplate::getDeleted, YesNo.NO);
        uw.lambda().eq(UserBirthdayTemplate::getId,id);
        this.update(uw);

        QueryWrapper<UnionBirthdayMonth> birthdayMonth = new QueryWrapper<>();
        birthdayMonth.lambda().eq(UnionBirthdayMonth::getTemplateId,id);
        this.unionBirthdayMonthService.remove(birthdayMonth);
    }

    @Override
    public void modifyTemplate(List<String> ids) {

        UpdateWrapper<UserBirthdayTemplate> uw = new UpdateWrapper<>();
        uw.lambda().set(UserBirthdayTemplate::getDeleted, YesNo.NO);
        uw.lambda().in(UserBirthdayTemplate::getId,ids);
        this.update(uw);

        QueryWrapper<UnionBirthdayMonth> birthdayMonth = new QueryWrapper<>();
        birthdayMonth.lambda().in(UnionBirthdayMonth::getTemplateId,ids);
        this.unionBirthdayMonthService.remove(birthdayMonth);
    }

    @Override
    public boolean whetherBirthday(String userId) {
        String month = com.zerody.common.utils.DateUtil.getMonth();
        String day = com.zerody.common.utils.DateUtil.getDay();
        List<AppUserNotPushVo> userList =  this.sysUserInfoMapper.getBirthdayUserIds(month,day,userId);
        if(userList.size() == 0) {
            // 判断是否是ceo生日
            List<AppCeoUserNotPushVo> ceoList = ceoUserInfoMapper.getCeoBirthdayUserIds(month, day,userId);
            return ceoList.size() > 0;
        }
        return true;
    }

    @Override
    public void addBlessing(BlessIngParam param) {
        SendRobotMessageDto data = new SendRobotMessageDto();
        data.setContent(param.getBlessingText());
        data.setTarget(param.getUserId());
        data.setContentPush(param.getBlessingText());
        data.setSender(param.getBirthdayUserId());
        data.setContentExtra(com.zerody.flow.client.util.JsonUtils.toString(param));
        data.setType(MESSAGE_TYPE_FLOW);
        DataResult<Long> result = this.sendMsgFeignService.send(data);
        log.info("推送IM结果:{}", com.zerody.flow.client.util.JsonUtils.toString(result));

    }

    @Override
    public boolean whetherEntry(String userId) {
        List<AppUserNotPushVo> userList =  this.sysUserInfoMapper.getAnniversaryUserList(userId);
        return userList.size() > 0;
    }

    @Override
    public AppUserNotPushVo getEntryPullData(String userId) {
        //查询当前入职周年的信息
        List<AppUserNotPushVo> lists = sysUserInfoMapper.getAnniversaryUserList(userId);
        AppUserNotPushVo entryData;
        if(lists.size() == 0) {
            throw new DefaultException("查找周年庆错误");
        }
        entryData = lists.get(0);
        //获取模板
        UserBirthdayTemplate template = this.baseMapper.getTemplateInfoByYear(entryData.getNum().toString(), YesNo.YES);
        if(template == null) {
            throw new DefaultException("未配置"+entryData.getNum()+"周年模板");
        }
        entryData.setBlessing(template.getBlessing());
        entryData.setPosterUrl(template.getPosterUrl());
        if (StringUtils.isNotEmpty(entryData.getCompanyId())) {
            //总经理 查询该企业年统计量

            //查询签单数量 和放款金额 和放款数
            DataResult<SignOrderDataVo> signOrderData = contractFeignService.getSignOrderData(entryData.getCompanyId(), null, null);
            if (signOrderData.isSuccess()) {
                entryData.setSignOrderNum(signOrderData.getData().getSignOrderNum());
                entryData.setLoansMoney(signOrderData.getData().getLoansMoney());
                entryData.setLoansNum(signOrderData.getData().getLoansNum());
            }
            //查询录入客户数量
            DataResult<Integer> importCustomerNum = customerFeignService.getImportCustomerNum(entryData.getCompanyId(), null, null);
            if (importCustomerNum.isSuccess()) {
                entryData.setImportCustomerNum(importCustomerNum.getData());
            }
        } else if (StringUtils.isNotEmpty(entryData.getDepartmentId())) {
            //副总或团队长  查询该部门年统计量

            //查询签单数量 和放款金额 和放款数
            DataResult<SignOrderDataVo> signOrderData = contractFeignService.getSignOrderData(null, entryData.getDepartmentId(), null);
            if (signOrderData.isSuccess()) {
                entryData.setSignOrderNum(signOrderData.getData().getSignOrderNum());
                entryData.setLoansMoney(signOrderData.getData().getLoansMoney());
                entryData.setLoansNum(signOrderData.getData().getLoansNum());
            }
            //查询录入客户数量
            DataResult<Integer> importCustomerNum = customerFeignService.getImportCustomerNum(null, entryData.getDepartmentId(), null);
            if (importCustomerNum.isSuccess()) {
                entryData.setImportCustomerNum(importCustomerNum.getData());
            }

        }else {
            //伙伴数据
            //查询签单数量 和放款金额 和放款数
            DataResult<SignOrderDataVo> signOrderData = contractFeignService.getSignOrderData(null, null, userId);
            if (signOrderData.isSuccess()) {
                entryData.setSignOrderNum(signOrderData.getData().getSignOrderNum());
                entryData.setLoansMoney(signOrderData.getData().getLoansMoney());
                entryData.setLoansNum(signOrderData.getData().getLoansNum());
            }
            //查询录入客户数量
            DataResult<Integer> importCustomerNum = customerFeignService.getImportCustomerNum(null, null, userId);
            if (importCustomerNum.isSuccess()) {
                entryData.setImportCustomerNum(importCustomerNum.getData());
            }
        }
        String chineseNum =entryData.getNum() > 10 ? CommonUtils.CHINESE_LIST[10] : CommonUtils.CHINESE_LIST[entryData.getNum()] ;
        String blessing =  entryData.getBlessing().replace("${num}",chineseNum);
        entryData.setBlessing(blessing);
        entryData.setContent("亲爱的"+entryData.getUserName()+"</br>小微集团祝您签约"+entryData.getNum()+"快乐");
        return entryData;
    }
}
