package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.MD5Utils;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.oauth.api.vo.SysAuthRoleInfoVo;
import com.zerody.sms.api.dto.SmsDto;
import com.zerody.sms.feign.SmsFeignService;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.CardUserInfo;
import com.zerody.user.domain.CeoCompanyRef;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.domain.base.BaseModel;
import com.zerody.user.dto.CeoUserInfoPageDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.feign.CardFeignService;
import com.zerody.user.feign.OauthFeignService;
import com.zerody.user.mapper.CardUserInfoMapper;
import com.zerody.user.mapper.CardUserUnionCrmUserMapper;
import com.zerody.user.mapper.CeoUserInfoMapper;
import com.zerody.user.service.CeoCompanyRefService;
import com.zerody.user.service.CeoUserInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private OauthFeignService oauthFeignService;
    @Autowired
    private CheckUtil checkUtil;
    @Autowired
    private CeoCompanyRefService ceoCompanyRefService;

    @Override
    public CeoUserInfo getByPhone(String phone) {
        QueryWrapper<CeoUserInfo> qw=new QueryWrapper<>();
        qw.lambda().eq(CeoUserInfo::getPhoneNumber,phone).eq(CeoUserInfo::getDeleted, YesNo.NO)
        .eq(BaseModel::getStatus,YesNo.NO);
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
        ceoUserInfo.setDeleted(YesNo.NO);
        //是否停用状态
        ceoUserInfo.setStatus(YesNo.NO);
        //初始化密码加密
        String initPwd = SysStaffInfoService.getInitPwd();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        ceoUserInfo.setUserPwd(passwordEncoder.encode(MD5Utils.MD5(initPwd)));
        //初始化角色
        SysAuthRoleInfoVo vo=new SysAuthRoleInfoVo();
        vo.setRoleName("总裁");
        DataResult result = oauthFeignService.addRole(vo);
        if(!result.isSuccess()){
            throw new DefaultException("服务异常！");
        }
        JSONObject obj=(JSONObject) JSON.toJSON(result.getData());
        if(DataUtil.isNotEmpty(obj)) {
            ceoUserInfo.setRoleId(obj.get("id").toString());
            ceoUserInfo.setRoleName(obj.get("roleName").toString());
        }
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
//        QueryWrapper<CardUserInfo> qw=new QueryWrapper<>();
//        qw.lambda().eq(CardUserInfo::getPhoneNumber,ceoUserInfo.getPhoneNumber());
//        List<CardUserInfo> cardUserInfos = cardUserInfoMapper.selectList(qw);
//        CardUserInfo cardUserInfo=null;
//        if(DataUtil.isNotEmpty(cardUserInfos)){
            //如果内部员工先注册的名片用户，则检查是否制作了名片
//            cardUserInfo = cardUserInfos.get(0);
            //如果没创建名片，则帮该员工创建一个名片
//            DataResult<UserCardDto> cardByUserId = cardFeignService.getCardByUserId(cardUserInfo.getId());
//            if(!cardByUserId.isSuccess()){
//                throw new DefaultException("名片服务异常！");
//            }
//            UserCardDto data = cardByUserId.getData();
//            if(DataUtil.isEmpty(data)){
//                //生成基础名片信息
//                saveCard(ceoUserInfo,cardUserInfo);
//            }else {
//                    //把名片账户的名片绑定该新用户
//                UserCardReplaceDto userReplace = new UserCardReplaceDto();
//                userReplace.setNewUserId(ceoUserInfo.getId());
//                userReplace.setCardUserId(cardUserInfo.getId());
//                this.cardFeignService.updateUserBycardUser(userReplace);
//                //并且把关联改了
//                QueryWrapper<CardUserUnionUser> quW=new QueryWrapper<>();
//                quW.lambda().eq(CardUserUnionUser::getCardId,cardUserInfo.getId());
//                CardUserUnionUser cardUserUnionUser = cardUserUnionCrmUserMapper.selectOne(quW);
//                if(DataUtil.isNotEmpty(cardUserUnionUser)){
//                    UpdateWrapper<CardUserUnionUser> uw=new UpdateWrapper<>();
//                    uw.lambda().eq(CardUserUnionUser::getId,cardUserUnionUser.getId());
//                    uw.lambda().set(CardUserUnionUser::getUserId,ceoUserInfo.getId());
//                    cardUserUnionCrmUserMapper.update(null,uw);
//                }else {
//                    //关联内部员工信息
//                    CardUserUnionUser cuu=new CardUserUnionUser();
//                    cuu.setId(UUIDutils.getUUID32());
//                    cuu.setCardId(cardUserInfo.getId());
//                    cuu.setUserId(ceoUserInfo.getId());
//                    cardUserUnionCrmUserMapper.insert(cuu);
//                }
//                }
//        }else {
//            cardUserInfo=new CardUserInfo();
//            cardUserInfo.setUserName(ceoUserInfo.getUserName());
//            cardUserInfo.setPhoneNumber(ceoUserInfo.getPhoneNumber());
//            cardUserInfo.setUserPwd(ceoUserInfo.getUserPwd());
//            cardUserInfo.setCreateBy(UserUtils.getUserId());
//            cardUserInfo.setCreateTime(new Date());
//            cardUserInfo.setStatus(StatusEnum.activity.getValue());
//            cardUserInfoMapper.insert(cardUserInfo);
//            //生成基础名片信息
//            saveCard(ceoUserInfo,cardUserInfo);
//            //关联内部员工信息
//            CardUserUnionUser cardUserUnionUser=new CardUserUnionUser();
//            cardUserUnionUser.setId(UUIDutils.getUUID32());
//            cardUserUnionUser.setCardId(cardUserInfo.getId());
//            cardUserUnionUser.setUserId(ceoUserInfo.getId());
//            cardUserUnionCrmUserMapper.insert(cardUserUnionUser);
//        }
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
        //删除后清除token
        this.checkUtil.removeUserToken(id);
    }

    @Override
    public IPage<CeoUserInfo> selectCeoUserPage(CeoUserInfoPageDto ceoUserInfoPageDto) {
        IPage<CeoUserInfo> infoVoIPage = new Page<>(ceoUserInfoPageDto.getCurrent(),ceoUserInfoPageDto.getPageSize());
        QueryWrapper<CeoUserInfo> qw=new QueryWrapper<>();
        qw.lambda().orderByDesc(BaseModel::getCreateTime);
        qw.lambda().eq(CeoUserInfo::getDeleted,YesNo.NO);
        if(DataUtil.isNotEmpty(ceoUserInfoPageDto.getPhone())){
            qw.lambda().like(CeoUserInfo::getPhoneNumber,ceoUserInfoPageDto.getPhone());
        }
        if(DataUtil.isNotEmpty(ceoUserInfoPageDto.getUserName())){
            qw.lambda().like(CeoUserInfo::getUserName,ceoUserInfoPageDto.getUserName());
        }
        IPage<CeoUserInfo> ceoUserInfoIPage = ceoUserInfoMapper.selectPage(infoVoIPage, qw);
        List<CeoUserInfo> records = ceoUserInfoIPage.getRecords();
        if(DataUtil.isNotEmpty(records)){
            records.stream().forEach(item->{
                CeoRefVo ceoRef = ceoCompanyRefService.getCeoRef(item.getId());
                if(DataUtil.isNotEmpty(ceoRef)&&DataUtil.isNotEmpty(ceoRef.getCompanys())){
                    List<String> collect = ceoRef.getCompanys().stream().map(s -> s.getCompanyName()).collect(Collectors.toList());
                    item.setCompanys(collect);
                }
            });
        }
        return  ceoUserInfoIPage;
    }

    @Override
    public IPage<BosStaffInfoVo> getCeoPage(SysStaffInfoPageDto param) {
        IPage<BosStaffInfoVo> iPage = new Page<>(param.getCurrent(), param.getPageSize());
        iPage = this.ceoUserInfoMapper.getCeoPage(param, iPage);
        return iPage;
    }

    @Override
    public SysUserInfoVo getCeoInfoByUserId(String userId) {
        return this.ceoUserInfoMapper.getCeoInfoByUserId(userId);
    }

    @Override
    public List<StaffInfoVo> getStaffInfoByIds(List<String> userId) {
        return this.ceoUserInfoMapper.getStaffInfoByIds(userId);
    }

    @Override
    public List<String> getAllCeo() {
        return this.ceoUserInfoMapper.getAllCeoIds();
    }

    @Override
    public List<AppCeoUserNotPushVo> getCeoBirthdayUserIds(String month, String day) {
        return  ceoUserInfoMapper.getCeoBirthdayUserIds(month, day,null);
    }

    @Override
    public List<AppCeoUserNotPushVo> getOtherCEOsBirthdayUser(String month, String day) {
        List<AppCeoUserNotPushVo> arrayList = new ArrayList<>();
        List<AppCeoUserNotPushVo> ceoUserInfo = this.ceoUserInfoMapper.getOtherCEOsBirthdayUser(month, day);
        for (AppCeoUserNotPushVo userInfo : ceoUserInfo) {
            AppCeoUserNotPushVo appCeoUserNotPushVo = new AppCeoUserNotPushVo();
            BeanUtils.copyProperties(userInfo, appCeoUserNotPushVo);
            //获取ceo关联的企业信息
            List<CeoCompanyRef> ceoCompanyList = ceoCompanyRefService.getBackRefById(appCeoUserNotPushVo.getCeoId());
            if (DataUtil.isNotEmpty(ceoCompanyList)) {
                List<String> companyIds = ceoCompanyList.stream().map(CeoCompanyRef::getCompanyId).distinct().collect(Collectors.toList());
                appCeoUserNotPushVo.setCompanyIds(companyIds);
            }
            arrayList.add(appCeoUserNotPushVo);
        }
        return arrayList;
    }

    @Override
    public List<CeoUserVo> queryCeoList() {
        List<CeoUserVo> list =new ArrayList<>();
        LambdaQueryWrapper<CeoUserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CeoUserInfo::getStatus, YesNo.NO);
        wrapper.eq(CeoUserInfo::getDeleted, YesNo.NO);
        List<CeoUserInfo> ceoUserInfos = this.baseMapper.selectList(wrapper);
        for (CeoUserInfo ceoUserInfo : ceoUserInfos) {
            CeoUserVo ceoUserInfoVo = new CeoUserVo();
            BeanUtils.copyProperties(ceoUserInfo, ceoUserInfoVo);
            //获取ceo关联的企业信息
            List<CeoCompanyRef> ceoCompanyList = ceoCompanyRefService.getBackRefById(ceoUserInfo.getId());
            if (DataUtil.isNotEmpty(ceoCompanyList)) {
                List<String> companyIds = ceoCompanyList.stream().map(CeoCompanyRef::getCompanyId).distinct().collect(Collectors.toList());
                ceoUserInfoVo.setCompanyIds(companyIds);
            }
            list.add(ceoUserInfoVo);
        }
        return list;
    }

    @Override
    public List<String> getCeoByCompanyId(String companyId) {
        List<String> list =new ArrayList<>();
        //获取企业关联ceo信息
        List<CeoCompanyRef> ceoCompanyList = ceoCompanyRefService.getCompanyIdBackRefById(companyId);
     if(DataUtil.isNotEmpty(ceoCompanyList)){
         List<String> ceoId = ceoCompanyList.stream().map(CeoCompanyRef::getCeoId).distinct().collect(Collectors.toList());
         LambdaQueryWrapper<CeoUserInfo> wrapper = new LambdaQueryWrapper<>();
         wrapper.eq(CeoUserInfo::getStatus, YesNo.NO);
         wrapper.eq(CeoUserInfo::getDeleted, YesNo.NO);
         wrapper.in(CeoUserInfo::getId,ceoId);
         List<CeoUserInfo> ceoUserInfos = this.baseMapper.selectList(wrapper);
         if(DataUtil.isNotEmpty(ceoUserInfos)) {
             List<String> ids = ceoUserInfos.stream().map(CeoUserInfo::getId).distinct().collect(Collectors.toList());
             list.addAll(ids);
         }
     }
        return list;
    }
    @Override
    public List<SubordinateUserQueryVo> getList() {
        QueryWrapper<CeoUserInfo> qw=new QueryWrapper<>();
        qw.lambda().eq(CeoUserInfo::getDeleted, YesNo.NO)
                .eq(BaseModel::getStatus,YesNo.NO);
        List<CeoUserInfo> list = this.list(qw);
        if(DataUtil.isEmpty(list)){
            return null;
        }
        List<SubordinateUserQueryVo> collect = list.stream().map(item -> {
            SubordinateUserQueryVo vo = new SubordinateUserQueryVo();
            BeanUtils.copyProperties(item, vo);
            vo.setUserId(item.getId());
            vo.setMobile(item.getPhoneNumber());
            vo.setPositionName(item.getPosition());
            vo.setCompanyName(item.getCompany());
            return vo;
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<SubordinateUserQueryVo> getListCompany(String companyId) {
        QueryWrapper<CeoCompanyRef> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CeoCompanyRef::getCompanyId,companyId);
        List<CeoCompanyRef> refList = this.ceoCompanyRefService.list(wrapper);
        List<String> ceoIds = refList.stream().map(CeoCompanyRef::getCeoId).collect(Collectors.toList());
        QueryWrapper<CeoUserInfo> qw=new QueryWrapper<>();
        qw.lambda().eq(CeoUserInfo::getDeleted, YesNo.NO)
                .eq(BaseModel::getStatus,YesNo.NO).in(CeoUserInfo::getId,ceoIds);
        List<CeoUserInfo> list = this.list(qw);
        if(DataUtil.isEmpty(list)){
            return null;
        }
        List<SubordinateUserQueryVo> collect = list.stream().map(item -> {
            SubordinateUserQueryVo vo = new SubordinateUserQueryVo();
                BeanUtils.copyProperties(item, vo);
                vo.setUserId(item.getId());
                vo.setMobile(item.getPhoneNumber());
                vo.setPositionName(item.getPosition());
                vo.setCompanyName(item.getCompany());
            return vo;
        }).collect(Collectors.toList());

        return collect;
    }


    public void saveCard(CeoUserInfo ceoUserInfo, CardUserInfo cardUserInfo){
        //生成基础名片信息
//        UserCardDto cardDto=new UserCardDto();
//        cardDto.setMobile(cardUserInfo.getPhoneNumber());
//        cardDto.setUserName(cardUserInfo.getUserName());
//        cardDto.setCompany(ceoUserInfo.getCompany());
//        cardDto.setPosition(ceoUserInfo.getPosition());
//        //crm用户ID
//        cardDto.setUserId(ceoUserInfo.getId());
//        //名片用户ID
//        cardDto.setCustomerUserId(cardUserInfo.getId());
//        cardDto.setAvatar(ceoUserInfo.getAvatar());
//        cardDto.setCustomerUserId(cardUserInfo.getId());
//        cardDto.setCreateBy(UserUtils.getUserId());
//
//        cardDto.setAddressDetail(ceoUserInfo.getContactAddress());
//        DataResult<String> card = cardFeignService.createCard(cardDto);
//        if(!card.isSuccess()){
//            throw new DefaultException("服务异常！");
//        }
    }
}
