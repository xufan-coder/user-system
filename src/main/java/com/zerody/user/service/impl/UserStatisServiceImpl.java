package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.enums.TimeOperate;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.DateUtil;
import com.zerody.common.vo.IdCardDate;
import com.zerody.user.domain.Data;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.statis.UserAgeStatisQueryDto;
import com.zerody.user.dto.statis.UserSexStatisQueryDto;
import com.zerody.user.dto.statis.UserStatisQueryDto;
import com.zerody.user.enums.ApproveStatusEnum;
import com.zerody.user.enums.DegreeEnum;
import com.zerody.user.handler.user.UserStatisHandle;
import com.zerody.user.mapper.UserStatisMapper;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.UserStatisService;
import com.zerody.user.vo.StatisticsDataDetailsVo;
import com.zerody.user.vo.statis.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author PengQiang
 * @ClassName UserStatisServiceImpl
 * @DateTime 2023/4/29 10:57
 */
@Service
public class UserStatisServiceImpl implements UserStatisService {

    @Autowired
    private UserStatisMapper baseMapper;

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    private final int num = 6;

    @Override
    public List<UserTrendQueryVo> getUserTrendS(UserStatisQueryDto param) {
        List<UserTrendQueryVo> result = new ArrayList<>();
        TimeOperate timeOperate = TimeOperate.getTimeType(param.getTimePeriod());
        if (DataUtil.isEmpty(timeOperate)) {
            throw new DefaultException("日期类型错误");
        }
        //起始时间
        Date startTime = timeOperate.getNext();
        for (int i = 0; i < num; i++) {
            UserTrendQueryVo statis = new UserTrendQueryVo();
            param.setEnd(startTime);
            param.setBegin(timeOperate.getPrev(startTime));
            int signingNum = this.baseMapper.getStatisSigning(param);
            int unSigningNum = this.baseMapper.getStatisUnSigning(param);
            statis.setKey(timeOperate.getFormat(param.getBegin()));
            result.add(statis);
            statis.setSignNum(signingNum);
            statis.setUnSignNum(unSigningNum);
            startTime = param.getBegin();
        }
        return result;
    }

    @Override
    public List<UserAgeStatisQueryVo> getAgeStatis(UserAgeStatisQueryDto param) {
        // 总人数
        Integer total = 0;
        List<UserAgeStatisQueryVo> result = UserStatisHandle.getStatisUserAge();
        //获取人数
        for (UserAgeStatisQueryVo statis : result) {
            if (DataUtil.isNotEmpty(statis.getBeginAge())) {
                param.setBegin(Date.from(LocalDate.now().atStartOfDay().minusYears(statis.getBeginAge()).minusDays(-1).toInstant(ZoneOffset.of("+8"))));
            }
            if (DataUtil.isNotEmpty(statis.getEndAge())) {
                param.setEnd(Date.from(LocalDate.now().atStartOfDay().minusYears(statis.getEndAge()).toInstant(ZoneOffset.of("+8"))));
            }
            statis.setNumber(this.baseMapper.getStatisAge(param));
            total += statis.getNumber();
        }
        if (total == 0) {
            return result;
        }
        //计算比例
        for (UserAgeStatisQueryVo statis : result) {
            statis.setRate(new BigDecimal(statis.getNumber()).divide(new BigDecimal(total), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
        }
        return result;
    }


    @Override
    public List<UserSexStatisQueryVo> getSexStatis(UserSexStatisQueryDto param) {
        Integer total = 0;
        List<UserSexStatisQueryVo> result = new ArrayList<>();
        result.add(new UserSexStatisQueryVo("男", 1));
        result.add(new UserSexStatisQueryVo("女", 0));
        for (UserSexStatisQueryVo statis : result) {
            param.setSex(statis.getSexType());
            statis.setNumber(this.baseMapper.getSexStatis(param));
            total += statis.getNumber();
        }
        if (total == 0) {
            return result;
        }
        for (UserSexStatisQueryVo statis : result) {
            statis.setRate(new BigDecimal(statis.getNumber()).divide(new BigDecimal(total), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
        }
        return result;
    }

    @Override
    public List<StatisticsDataDetailsVo> statisticsDetails(UserStatisQueryDto param) {
        List<StatisticsDataDetailsVo> result = new ArrayList<>();
        TimeOperate timeOperate = TimeOperate.getTimeType(param.getTimePeriod());
        if (DataUtil.isEmpty(timeOperate)) {
            throw new DefaultException("日期类型错误");
        }
        Date startTime = timeOperate.getNext();
        //int lastAgencyNum = 0;
        for (int i = 0; i < num; i++) {
            StatisticsDataDetailsVo vo = new StatisticsDataDetailsVo();
            //结束时间
            param.setEnd(startTime);
            //开始时间
            param.setBegin(timeOperate.getPrev(startTime));
            //新签约
            int newAgencyNum = this.baseMapper.getStatisSigning(param);
            //解约
            int terminationNum = this.baseMapper.getStatisUnSigning(param);
            //净增
            int netIncreaseNum = newAgencyNum - terminationNum;

            //Date min = this.baseMapper.getDateJoinMin();
            UserStatisQueryDto dto = new UserStatisQueryDto();
            BeanUtils.copyProperties(param, dto);
            //总签约中(签约与合作中) 查询历史签约
            int agencyNum = this.baseMapper.getHistorySign(dto);
            /*if (i == 0) {
                //第一次不用递减(月份倒序)
                lastAgencyNum = agencyNum;
            } else {
                //签约中(累计每日每月的新签约)
                lastAgencyNum = agencyNum - newAgencyNum - terminationNum;
            }*/
            vo.setDateStr(timeOperate.getFormat(param.getBegin()));
            vo.setNewAgencyNum(newAgencyNum);
            vo.setTerminationNum(terminationNum);
            vo.setNetIncreaseNum(netIncreaseNum);
            vo.setAgencyNum(agencyNum);
            result.add(vo);
            startTime = param.getBegin();
        }
        return result;
    }

    @Override
    public UserStatisTrendVo getUserTrends(UserStatisQueryDto param) {
        UserStatisTrendVo userStatisTrendVo = new UserStatisTrendVo();

        UserSexStatisQueryDto userSexStatisQueryDto = new UserSexStatisQueryDto();
        BeanUtils.copyProperties(param, userSexStatisQueryDto);
        //性别
        List<UserSexStatisQueryVo> sexStatis = getSexStatis(userSexStatisQueryDto);
        userStatisTrendVo.setUserSexStatisQueryVoList(sexStatis);

        UserAgeStatisQueryDto userAgeStatisQueryDto = new UserAgeStatisQueryDto();
        BeanUtils.copyProperties(param, userAgeStatisQueryDto);
        //年龄
        List<UserAgeStatisQueryVo> ageStatis = getAgeStatis(userAgeStatisQueryDto);
        userStatisTrendVo.setAgeStatisQuery(ageStatis);

        List<DegreeVo> degreeVoList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        list.add(DegreeEnum.SENIOR_HIGH_UNDER.name());
        list.add(DegreeEnum.JUNIOR_COLLEGE.name());
        list.add(DegreeEnum.REGULAR_COLLEGE.name());
        list.add(DegreeEnum.MASTER.name());
        list.add(DegreeEnum.DOCTOR.name());
        //DegreeEnum[] values = DegreeEnum.values();
        //总人数
        int num = 0;
        for (String name : list) {
            param.setHighestEducation(name);
            int degreeNum= 0;
            if ("SENIOR_HIGH_UNDER".equals(name)) {
                degreeNum = sysStaffInfoService.getBelowHighSchool(param);
            } else {
                degreeNum = sysStaffInfoService.getDegree(param);
            }

            DegreeVo degreeVo = new DegreeVo();
            degreeVo.setNum(degreeNum);
            degreeVo.setDegree(name);
            num += degreeNum;
            degreeVoList.add(degreeVo);
        }
        for (DegreeVo degre : degreeVoList) {
            degre.setRate(reserveTwo(degre.getNum(), num));
            degre.setDegree(DegreeEnum.getByText(degre.getDegree()));

        }
        userStatisTrendVo.setDegreeVoList(degreeVoList);
        return userStatisTrendVo;
    }

    private static BigDecimal reserveTwo(Integer d, Integer num){
        if (d.equals(0)) {
            return BigDecimal.ZERO;
        }
        BigDecimal b = new BigDecimal(d).divide(new BigDecimal(num), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format(b);
        return new BigDecimal(format);
    }


}
