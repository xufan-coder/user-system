package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.DateUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.constant.CommonConstants;
import com.zerody.user.domain.UseControl;
import com.zerody.user.domain.UsersUseControl;
import com.zerody.user.dto.UseControlDto;
import com.zerody.user.dto.UseControlTimeDto;
import com.zerody.user.enums.WeeKEnum;
import com.zerody.user.mapper.UseControlMapper;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            qw.clear();
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
        }else {
            for (int i = 0; i < 7; i++) {
                UseControlTimeVo timeVo=new UseControlTimeVo();
                timeVo.setEnable(YesNo.YES);
                timeVo.setWeek(i+1);
                timeVo.setStart(0);
                timeVo.setEnd(24);
            }
        }
        vo.setCompanyId(companyId);
        return vo;
    }

    @Override
    public Boolean checkUserAuth(UserVo vo) {
        //1先判断全局token 是否全局禁用
        String s = this.stringRedisTemplate.opsForValue().get(CommonConstants.SYS_CLOSE);
        if(DataUtil.isNotEmpty(s)){
            return true;
        }
        //2在判断此人是否存在黑名单
        QueryWrapper<UsersUseControl> qw =new QueryWrapper<>();
        qw.lambda().eq(UsersUseControl::getUserId,vo.getUserId()).eq(UsersUseControl::getType,1);
        UsersUseControl one = this.usersUseControlService.getOne(qw);
        if(DataUtil.isNotEmpty(one)){
            return true;
        }
        //3再校验此人白名单
        qw.clear();
        qw.lambda().eq(UsersUseControl::getUserId,vo.getUserId()).eq(UsersUseControl::getType,2);
        UsersUseControl authorize = this.usersUseControlService.getOne(qw);
        if(DataUtil.isNotEmpty(authorize)){
            return false;
        }
        //4.校验企业时间限制
            //当前周几 中文需要转换
        String week = DateUtil.getWeek();
            //当前时间 24小时制
        String hour = DateUtil.getHour();

        //查询该用户的企业允许登录的时间
        QueryWrapper<UseControl> UcQw =new QueryWrapper<>();
        UcQw.lambda().eq(UseControl::getCompanyId,vo.getCompanyId());
        UcQw.lambda().eq(UseControl::getWeek, WeeKEnum.getNumberByText(week));
        UseControl companyAuth = this.getOne(UcQw);
        if(DataUtil.isNotEmpty(companyAuth)){
               // 大于配置开始时间小于结束时间则允许登录使用
            if(Integer.parseInt(hour)>=companyAuth.getStart()
                    ||Integer.parseInt(hour)<=companyAuth.getEnd()){
                return false;
            }else {
                return true;
            }
        }else {
            //如果没有配置，则默认允许登录使用
            return false;
        }
    }
}
