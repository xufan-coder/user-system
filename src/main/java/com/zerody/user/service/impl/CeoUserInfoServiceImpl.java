package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.card.api.dto.UserCardDto;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.MD5Utils;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.sms.api.dto.SmsDto;
import com.zerody.sms.feign.SmsFeignService;
import com.zerody.user.domain.CardUserInfo;
import com.zerody.user.domain.CardUserUnionUser;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.domain.base.BaseModel;
import com.zerody.user.dto.CeoUserInfoPageDto;
import com.zerody.user.feign.CardFeignService;
import com.zerody.user.mapper.CardUserInfoMapper;
import com.zerody.user.mapper.CardUserUnionCrmUserMapper;
import com.zerody.user.mapper.CeoUserInfoMapper;
import com.zerody.user.service.CeoUserInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  DaBai
 * @date  2021/4/20 14:29
 */

@Slf4j
@Service
public class CeoUserInfoServiceImpl extends BaseService<CeoUserInfoMapper, CeoUserInfo> implements CeoUserInfoService {
    @Value("${sms.template.userTip:}")
    String userTipTemplate;

    @Value("${sms.sign.tsz:唐叁藏}")
    String smsSign;
    @Autowired
    private SmsFeignService smsFeignService;
    @Autowired
    private CeoUserInfoMapper ceoUserInfoMapper;
    @Autowired
    private CardUserInfoMapper cardUserInfoMapper;
    @Autowired
    private CardFeignService cardFeignService;
    @Autowired
    private CardUserUnionCrmUserMapper cardUserUnionCrmUserMapper;

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
        if(DataUtil.isNotEmpty(logInfo.getUserPwd())){
            uw.lambda().set(CeoUserInfo::getUserPwd,logInfo.getUserPwd());
        }
        uw.lambda().set(BaseModel::getUpdateTime,new Date())
                .eq(CeoUserInfo::getId,logInfo.getId());

        ceoUserInfoMapper.update(null,uw);
    }

    @Override
    public CeoUserInfo getUserById(String id) {
        return this.getById(id);
    }

    @Override
    public void addCeoUser(CeoUserInfo ceoUserInfo) {
        ceoUserInfo.setCreateTime(new Date());
        ceoUserInfo.setCreateId(UserUtils.getUserId());
        //是否停用状态
        ceoUserInfo.setStatus(YesNo.NO);
        //初始化密码加密
        String initPwd = SysStaffInfoService.getInitPwd();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        ceoUserInfo.setUserPwd(passwordEncoder.encode(MD5Utils.MD5(initPwd)));


        this.save(ceoUserInfo);
        SmsDto smsDto=new SmsDto();
        smsDto.setMobile(ceoUserInfo.getPhoneNumber());
        Map<String, Object> content=new HashMap<>();
        content.put("userName",ceoUserInfo.getPhoneNumber());
        content.put("passWord",initPwd);
        smsDto.setContent(content);
        smsDto.setTemplateCode(userTipTemplate);
        smsDto.setSign(smsSign);
        //生成基础名片信息
        //先查询改手机号是否存在绑定了的名片用户
        QueryWrapper<CardUserInfo> qw=new QueryWrapper<>();
        qw.lambda().eq(CardUserInfo::getPhoneNumber,ceoUserInfo.getPhoneNumber());
        List<CardUserInfo> cardUserInfos = cardUserInfoMapper.selectList(qw);
        CardUserInfo cardUserInfo=null;
        if(DataUtil.isNotEmpty(cardUserInfos)){
            //如果内部员工先注册的名片用户，则检查是否制作了名片
            cardUserInfo = cardUserInfos.get(0);
            //如果没创建名片，则帮该员工创建一个名片
            DataResult<UserCardDto> cardByUserId = cardFeignService.getCardByUserId(cardUserInfo.getId());
            if(!cardByUserId.isSuccess()){
                throw new DefaultException("名片服务异常！");
            }
            UserCardDto data = cardByUserId.getData();
            if(DataUtil.isEmpty(data)){
                //生成基础名片信息
                saveCard(ceoUserInfo,cardUserInfo);
            }
        }else {
            cardUserInfo=new CardUserInfo();
            cardUserInfo.setUserName(ceoUserInfo.getUserName());
            cardUserInfo.setPhoneNumber(ceoUserInfo.getPhoneNumber());
            cardUserInfo.setUserPwd(ceoUserInfo.getUserPwd());
            cardUserInfo.setCreateBy(UserUtils.getUserId());
            cardUserInfo.setCreateTime(new Date());
            cardUserInfo.setStatus(StatusEnum.activity.getValue());
            cardUserInfoMapper.insert(cardUserInfo);
            //生成基础名片信息
            saveCard(ceoUserInfo,cardUserInfo);
        }

        //关联内部员工信息
        CardUserUnionUser cardUserUnionUser=new CardUserUnionUser();
        cardUserUnionUser.setId(UUIDutils.getUUID32());
        cardUserUnionUser.setCardId(cardUserInfo.getId());
        cardUserUnionUser.setUserId(ceoUserInfo.getId());
        cardUserUnionCrmUserMapper.insert(cardUserUnionUser);
        //最后发送短信
        smsFeignService.sendSms(smsDto);
    }

    @Override
    public void updateCeoUser(CeoUserInfo ceoUserInfo) {
        ceoUserInfo.setUpdateTime(new Date());
        ceoUserInfo.setUpdateId(UserUtils.getUserId());
        this.updateById(ceoUserInfo);
    }

    @Override
    public void deleteCeoUserById(String id) {
        UpdateWrapper<CeoUserInfo> uw=new UpdateWrapper<>();
        uw.lambda().set(CeoUserInfo::getDeleted,YesNo.YES)
                .set(BaseModel::getUpdateTime,new Date())
                .eq(BaseModel::getId,id);
        this.update(uw);
    }

    @Override
    public IPage<CeoUserInfo> selectCeoUserPage(CeoUserInfoPageDto ceoUserInfoPageDto) {
        IPage<CeoUserInfo> infoVoIPage = new Page<>(ceoUserInfoPageDto.getCurrent(),ceoUserInfoPageDto.getPageSize());
        QueryWrapper<CeoUserInfo> qw=new QueryWrapper<>();
        qw.lambda().orderByDesc(BaseModel::getCreateTime);
        if(DataUtil.isNotEmpty(ceoUserInfoPageDto.getPhone())){
            qw.lambda().eq(CeoUserInfo::getPhoneNumber,ceoUserInfoPageDto.getPhone());
        }
        if(DataUtil.isNotEmpty(ceoUserInfoPageDto.getUserName())){
            qw.lambda().eq(CeoUserInfo::getUserName,ceoUserInfoPageDto.getUserName());
        }
        IPage<CeoUserInfo> ceoUserInfoIPage = ceoUserInfoMapper.selectPage(infoVoIPage, qw);
        return  ceoUserInfoIPage;
    }



    public void saveCard(CeoUserInfo ceoUserInfo, CardUserInfo cardUserInfo){
        //生成基础名片信息
        UserCardDto cardDto=new UserCardDto();
        cardDto.setMobile(cardUserInfo.getPhoneNumber());
        cardDto.setUserName(cardUserInfo.getUserName());
        //crm用户ID
        cardDto.setUserId(ceoUserInfo.getId());
        //名片用户ID
        cardDto.setCustomerUserId(cardUserInfo.getId());
        cardDto.setAvatar(ceoUserInfo.getAvatar());
        cardDto.setCustomerUserId(cardUserInfo.getId());
        cardDto.setCreateBy(UserUtils.getUserId());

        cardDto.setAddressDetail(ceoUserInfo.getContactAddress());
        DataResult<String> card = cardFeignService.createCard(cardDto);
        if(!card.isSuccess()){
            throw new DefaultException("服务异常！");
        }
    }
}
