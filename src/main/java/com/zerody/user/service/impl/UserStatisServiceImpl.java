package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.enums.TimeOperate;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.statis.UserStatisQueryDto;
import com.zerody.user.mapper.UserStatisMapper;
import com.zerody.user.service.UserStatisService;
import com.zerody.user.vo.statis.UserTrendQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName UserStatisServiceImpl
 * @DateTime 2023/4/29 10:57
 */
@Service
public class UserStatisServiceImpl  implements UserStatisService {

    @Autowired
    private UserStatisMapper baseMapper;

    @Override
    public List<UserTrendQueryVo> getUserTrendS(UserStatisQueryDto param) {
        final int num = 7;
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
}
