package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.enums.TimeOperate;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.DateUtil;
import com.zerody.common.vo.IdCardDate;
import com.zerody.user.domain.Data;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.statis.UserAgeStatisQueryDto;
import com.zerody.user.dto.statis.UserSexStatisQueryDto;
import com.zerody.user.dto.statis.UserStatisQueryDto;
import com.zerody.user.handler.user.UserStatisHandle;
import com.zerody.user.mapper.UserStatisMapper;
import com.zerody.user.service.UserStatisService;
import com.zerody.user.vo.StatisticsDataDetailsVo;
import com.zerody.user.vo.statis.UserAgeStatisQueryVo;
import com.zerody.user.vo.statis.UserSexStatisQueryVo;
import com.zerody.user.vo.statis.UserTrendQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName UserStatisServiceImpl
 * @DateTime 2023/4/29 10:57
 */
@Service
public class UserStatisServiceImpl implements UserStatisService {

    @Autowired
    private UserStatisMapper baseMapper;

    private final int num = 7;

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
        Integer total = 0; // 总人数
        List<UserAgeStatisQueryVo> result = UserStatisHandle.getStatisUserAge();
        //获取人数
        for (UserAgeStatisQueryVo statis : result) {
            if (DataUtil.isNotEmpty(statis.getBeginAge())) {
                param.setBegin(Date.from(LocalDate.now().atStartOfDay().minusYears(statis.getBeginAge()).minusDays(-1).toInstant(ZoneOffset.of("+8"))));
            }
            if (DataUtil.isNotEmpty(statis.getEndAge())) {
                param.setBegin(Date.from(LocalDate.now().atStartOfDay().minusYears(statis.getEndAge()).toInstant(ZoneOffset.of("+8"))));
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
        int lastAgencyNum = 0;
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
            //签约中(累计每日每约的新签约)
            lastAgencyNum = newAgencyNum + lastAgencyNum;

            vo.setDateStr(timeOperate.getFormat(param.getBegin()));
            vo.setNewAgencyNum(newAgencyNum);
            vo.setTerminationNum(terminationNum);
            vo.setNetIncreaseNum(netIncreaseNum);
            vo.setAgencyNum(lastAgencyNum);
            result.add(vo);
            startTime = param.getBegin();
        }
        return result;
    }
}
