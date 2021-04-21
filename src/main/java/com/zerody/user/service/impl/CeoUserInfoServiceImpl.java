package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zerody.common.bean.DataResult;
import com.zerody.common.constant.MQ;
import com.zerody.common.constant.UserTypeInfo;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.mq.RabbitMqService;
import com.zerody.common.util.MD5Utils;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.AdminVo;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.check.CheckUser;
import com.zerody.user.domain.*;
import com.zerody.user.domain.base.BaseModel;
import com.zerody.user.domain.base.BaseStringModel;
import com.zerody.user.dto.SetUpdateAvatarDto;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.mapper.*;
import com.zerody.user.service.CeoUserInfoService;
import com.zerody.user.service.SysLoginInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.*;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author  DaBai
 * @date  2021/4/20 14:29
 */

@Slf4j
@Service
public class CeoUserInfoServiceImpl extends BaseService<CeoUserInfoMapper, CeoUserInfo> implements CeoUserInfoService {

    private CeoUserInfoMapper ceoUserInfoMapper;
    @Override
    public CeoUserInfo getByPhone(String phone) {
        QueryWrapper<CeoUserInfo> qw=new QueryWrapper<>();
        qw.lambda().eq(CeoUserInfo::getPhoneNumber,phone).eq(CeoUserInfo::getDeleted, YesNo.NO);
        return this.getOne(qw);
    }

    @Override
    public void updateCeoById(com.zerody.user.api.vo.CeoUserInfoVo logInfo) {
        UpdateWrapper<CeoUserInfo> uw=new UpdateWrapper<>();
        if(DataUtil.isNotEmpty(logInfo.getLastCheckSms())){
            uw.lambda().set(CeoUserInfo::getLastCheckSms,logInfo.getLastCheckSms());
        }
        if(DataUtil.isNotEmpty(logInfo.getLoginTime())){
            uw.lambda().set(CeoUserInfo::getLoginTime,logInfo.getLoginTime());
        }
        uw.lambda().set(BaseModel::getUpdateTime,new Date())
                .eq(CeoUserInfo::getId,logInfo.getId());

        ceoUserInfoMapper.update(null,uw);
    }
}
