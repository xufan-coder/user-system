package com.zerody.user.check;

import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.PhoneHomeLocationUtils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.FamilyMember;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.domain.UserResume;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.util.IdCardUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: 检查用户
 * Created by pengqiang on 2020/12/17 10:15
 */
@Slf4j
public class CheckUser {

    public static void checkParam(SysUserInfo sysUserInfo, List<FamilyMember> familyMembers) {
        checkParam(sysUserInfo, familyMembers, true);
    }

    /**
     * 用户操作参数校验
     */
    public static void checkParam(SysUserInfo sysUserInfo, List<FamilyMember> familyMembers, boolean filter) {
        if (StringUtils.isBlank(sysUserInfo.getPhoneNumber())) {
            throw new DefaultException("手机号码不能为空");
        }
        if (!sysUserInfo.getPhoneNumber().matches("\\d{11}")) {
            log.info("手机号码 {}", sysUserInfo.getPhoneNumber());
            throw new DefaultException("手机号码长度不正确");
        }
        if (StringUtils.isNotEmpty(sysUserInfo.getCertificateCard())) {
            if (!IdCardUtil.validate18Idcard(sysUserInfo.getCertificateCard())) {
                throw new DefaultException("身份证不合法");
            }
        } else {
            throw new DefaultException("身份证不能为空");
        }

//        if (DataUtil.isEmpty(sysUserInfo.getExpireTime()) && !filter) {
//            throw new DefaultException("请选择合约结束时间");
//        }

        if (StringUtils.isNotEmpty(sysUserInfo.getUrgentPhone()) && !PhoneHomeLocationUtils.checkPhoneBoolean(sysUserInfo.getUrgentPhone())) {
            throw new DefaultException("紧急联系人电话不合法");
        }
        if (CollectionUtils.isNotEmpty(familyMembers)) {
            Iterator<FamilyMember> iterator = familyMembers.iterator();
            for (int index = 1; iterator.hasNext(); index++){
                FamilyMember family = iterator.next();
                if (io.micrometer.core.instrument.util.StringUtils.isEmpty(family.getRelationship())) {
                    throw new DefaultException("家庭成员" + index + "关系为空");
                }
                if (io.micrometer.core.instrument.util.StringUtils.isEmpty(family.getName())) {
                    throw new DefaultException("家庭成员" + index + "姓名为空");
                }
                if (io.micrometer.core.instrument.util.StringUtils.isEmpty(family.getMobile())) {
                    throw new DefaultException("家庭成员" + index + "联系电话为空");
                }
                String regex = "^(1[3-9]\\d{9}$)";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(family.getMobile());
                if (!m.matches()) {
                    throw new DefaultException("家庭成员" + index + "联系电话不合法");
                }
            }
        }
        if(DataUtil.isNotEmpty(sysUserInfo.getStatus()) && sysUserInfo.getStatus()!=1){
            if (StringUtils.isBlank(sysUserInfo.getAvatar())) {
                throw new DefaultException("个人照片不能为空");
            }
            if (StringUtils.isBlank(sysUserInfo.getIdCardFront())) {
                throw new DefaultException("身份证照片国徽面(正面)不能为空");
            }
            if (StringUtils.isBlank(sysUserInfo.getIdCardReverse())) {
                throw new DefaultException("身份证照片人像面(反面)不能为空");
            }
        }
    }

    public static void checkParamList(SetSysUserInfoDto setSysUserInfoDto) {
        //合规承诺书
        List<String> complianceCommitments = setSysUserInfoDto.getComplianceCommitments();
        List<String> diplomas = setSysUserInfoDto.getDiplomas();
        if (DataUtil.isEmpty(complianceCommitments)) {
            throw new DefaultException("请上传合规承诺书！");
        }
//        if (DataUtil.isEmpty(diplomas)) {
//            throw new DefaultException("请上传学历证书！");
//        }

        List<UserResume> userResumes = setSysUserInfoDto.getUserResumes();
        if (DataUtil.isEmpty(userResumes)) {
            throw new DefaultException("请填写履历！");
        }
        if (CollectionUtils.isNotEmpty(userResumes)) {
            Iterator<UserResume> iterator = userResumes.iterator();
            for (int index = 1; iterator.hasNext(); index++){
                UserResume resume = iterator.next();
                if (io.micrometer.core.instrument.util.StringUtils.isEmpty(resume.getCompanyName())) {
                    throw new DefaultException("履历" + index + "任职企业为空");
                }
                if (io.micrometer.core.instrument.util.StringUtils.isEmpty(resume.getPost())) {
                    throw new DefaultException("履历" + index + "任职岗位为空");
                }
                if (io.micrometer.core.instrument.util.StringUtils.isEmpty(resume.getWorkDuration())) {
                    throw new DefaultException("履历" + index + "任职时间为空");
                }
//                if (io.micrometer.core.instrument.util.StringUtils.isEmpty(resume.getJobDescription())) {
//                    throw new DefaultException("履历" + index + "工作职责为空");
//                }
            }
        }
    }

}
