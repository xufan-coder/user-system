package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.SysCodeEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.SpringUtil;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.DateUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.constant.CommonConstants;
import com.zerody.user.domain.CallControlRecord;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.domain.UseControl;
import com.zerody.user.domain.UsersUseControl;
import com.zerody.user.dto.UseControlDto;
import com.zerody.user.dto.UseControlTimeDto;
import com.zerody.user.enums.WeeKEnum;
import com.zerody.user.mapper.UseControlMapper;
import com.zerody.user.service.CallControlRecordService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.UseControlService;
import com.zerody.user.service.UsersUseControlService;
import com.zerody.user.vo.UseControlTimeVo;
import com.zerody.user.vo.UseControlVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author  DaBai
 * @date  2022/3/1 14:01
 */

@Slf4j
@Service
public class UseControlServiceImpl extends ServiceImpl<UseControlMapper, UseControl> implements UseControlService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UsersUseControlService usersUseControlService;
    @Autowired
    private SpringUtil springUtil;
    @Autowired
    private CallControlRecordService callControlRecordService;
    @Autowired
    private SysUserInfoService sysUserInfoService;
    @Override
    public void addOrUpdate(UseControlDto param) {
        //是否全局关闭
        if(YesNo.YES== param.getCloseAll()){
            //禁止使用系统
            this.stringRedisTemplate.opsForValue().set(CommonConstants.SYS_CLOSE, String.valueOf(YesNo.YES));
        }else {
            this.stringRedisTemplate.delete(CommonConstants.SYS_CLOSE);
        }
        List<UseControlTimeDto> weekInfo = param.getWeekInfo();
        //查询该企业配置
        QueryWrapper<UseControl> qw =new QueryWrapper<>();
        qw.lambda().eq(UseControl::getCompanyId,param.getCompanyId());
        List<UseControl> list = this.list(qw);
        if(DataUtil.isNotEmpty(list)){
            this.remove(qw);
        }
        list.clear();
        for (UseControlTimeDto useControlTimeDto : weekInfo) {
            UseControl useControl=new UseControl();
            BeanUtils.copyProperties(useControlTimeDto,useControl);
            useControl.setCompanyId(param.getCompanyId());
            useControl.setUpdateTime(new Date());
            list.add(useControl);
        }
        this.saveBatch(list);
    }

    @Override
    public UseControlVo getByCompany(String companyId) {
        UseControlVo vo =new UseControlVo();
        String s = this.stringRedisTemplate.opsForValue().get(CommonConstants.SYS_CLOSE);
        vo.setCloseAll(DataUtil.isEmpty(s)?YesNo.NO:YesNo.YES);
        //查询该企业配置
        QueryWrapper<UseControl> qw =new QueryWrapper<>();
        qw.lambda().eq(UseControl::getCompanyId,companyId);
        List<UseControl> list = this.list(qw);

        if(DataUtil.isNotEmpty(list)){
            List<UseControlTimeVo> weekInfo=new ArrayList<>();
            for (UseControl useControl : list) {
                UseControlTimeVo timeVo=new UseControlTimeVo();
                BeanUtils.copyProperties(useControl,timeVo);
                weekInfo.add(timeVo);
            }
            vo.setWeekInfo(weekInfo);
        }
        vo.setCompanyId(companyId);
        return vo;
    }

    @Override
    public Boolean checkUserAuth(UserVo vo) {
        return this.checkUserAuth(vo.getUserId(),vo.getCompanyId());
    }

    @Override
    public Boolean checkUserAuth(String userId,String companyId) {
        //1先判断全局token 是否全局禁用
        String s = this.stringRedisTemplate.opsForValue().get(CommonConstants.SYS_CLOSE);
        if(DataUtil.isNotEmpty(s)){
            throw new DefaultException("系统禁止登录，请联系管理员！");
        }
        //2在判断此人是否存在黑名单
        QueryWrapper<UsersUseControl> qw =new QueryWrapper<>();
        qw.lambda().eq(UsersUseControl::getUserId,userId).eq(UsersUseControl::getType,1);
        UsersUseControl one = this.usersUseControlService.getOne(qw);
        if(DataUtil.isNotEmpty(one)){
            throw new DefaultException("您已被禁止登录系统，如有疑问请联系公司行政人员！");
        }
        //2.1增加校验账号是否被冻结
        SysUserInfo userById = sysUserInfoService.getUserById(userId);
        if(DataUtil.isNotEmpty(userById)){
           // 账号状态 0正常   1已冻结
            if(userById.getUseState().equals(1)){
                throw new DefaultException("您的账号已被冻结，请联系管理员！");
            }
        }


        //3再校验此人白名单
        qw.clear();
        qw.lambda().eq(UsersUseControl::getUserId,userId).eq(UsersUseControl::getType,2);
        UsersUseControl authorize = this.usersUseControlService.getOne(qw);
        if(DataUtil.isNotEmpty(authorize)){
            return false;
        }
        //4.校验企业时间限制
        //当前周几 中文需要转换
        SimpleDateFormat formatter = new SimpleDateFormat("E", Locale.CHINA);
        String week = formatter.format(new Date());
        //当前时间 24小时制
        String hour = DateUtil.getHour();

        //查询该用户的企业允许登录的时间
        QueryWrapper<UseControl> UcQw =new QueryWrapper<>();
        UcQw.lambda().eq(UseControl::getCompanyId,companyId);
        UcQw.lambda().eq(UseControl::getWeek, WeeKEnum.getNumberByText(week));
        UcQw.lambda().eq(UseControl::getEnable, YesNo.YES);
        UseControl companyAuth = this.getOne(UcQw);
        if(DataUtil.isNotEmpty(companyAuth)){
            // 大于配置开始时间小于结束时间则允许登录使用
            if(Integer.parseInt(hour)>=companyAuth.getStart()
                    &&Integer.parseInt(hour)<companyAuth.getEnd()){
                return false;
            }else {
                StringBuffer tip = new StringBuffer();
                tip.append("本时间段禁止登录系统，请在以下时间登录：\r\n");
                UcQw.clear();
                UcQw.lambda().eq(UseControl::getCompanyId,companyId);
                UcQw.lambda().orderByAsc(UseControl::getWeek);
                List<UseControl> list = this.list(UcQw);
                if(DataUtil.isNotEmpty(list)){
                    for (UseControl useControl : list) {
                        tip.append(WeeKEnum.getTextByNumber(useControl.getWeek())+":"+useControl.getStart()+"时~"+useControl.getEnd()+"时；\r\n");
                    }
                }
                throw new DefaultException(tip.toString());
            }
        }
        //如果没有配置 或没有启用，则默认允许登录使用

        //2022-11-10增加呼叫超出次数限制
        String sysCode = springUtil.getRequest().getHeader("sys-code");
        if(!SysCodeEnum.ZERODY_SCRM_MINI.getCode().equals(sysCode)){
            //SCRM不限制
            QueryWrapper<CallControlRecord> cqw =new QueryWrapper<>();
            cqw.lambda().eq(CallControlRecord::getUserId,userId);
            cqw.lambda().eq(CallControlRecord::getState, YesNo.NO);
            CallControlRecord callControlRecord = this.callControlRecordService.getOne(cqw);
            if(DataUtil.isNotEmpty(callControlRecord)){
                String tip="为了保护客户信息安全，当前账号因呼叫客户次数已达到限制值，现已禁止登录，如需解除限制请联系公司行政或者集团客服！";
                throw new DefaultException(tip);
            }
        }
        return false;
    }

    @Override
    public List<UseControl> getTips(UserVo user) {
        //先判断全局token 是否全局禁用
        String s = this.stringRedisTemplate.opsForValue().get(CommonConstants.SYS_CLOSE);
        if(DataUtil.isNotEmpty(s)){
            return null;
        }
        //查询是否存在白名单  白名单的用户不提醒
        QueryWrapper<UsersUseControl> qw =new QueryWrapper<>();
        qw.lambda().eq(UsersUseControl::getUserId,user.getUserId()).eq(UsersUseControl::getType,2);
        UsersUseControl authorize = this.usersUseControlService.getOne(qw);
        if(DataUtil.isEmpty(authorize)){
            //查询企业时间限制
            //当前周几 中文需要转换
            SimpleDateFormat formatter = new SimpleDateFormat("E", Locale.CHINA);
            String week = formatter.format(new Date());
            //当前时间 24小时制
            String hour = DateUtil.getHour();
            QueryWrapper<UseControl> UcQw =new QueryWrapper<>();
            UcQw.lambda().eq(UseControl::getCompanyId,user.getCompanyId());
//            UcQw.lambda().eq(UseControl::getWeek, WeeKEnum.getNumberByText(week));
//            UcQw.lambda().eq(UseControl::getEnable, YesNo.YES);
            UcQw.lambda().orderByAsc(UseControl::getWeek);
            List<UseControl> list = this.list(UcQw);
            if(DataUtil.isNotEmpty(list)){
               return list;
            }
        }
        return null;
    }
}
