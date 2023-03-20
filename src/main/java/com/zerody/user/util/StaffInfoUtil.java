package com.zerody.user.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.MD5Utils;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.constant.FileTypeInfo;
import com.zerody.user.constant.ImageTypeInfo;
import com.zerody.user.domain.*;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.enums.StaffHistoryTypeEnum;
import com.zerody.user.feign.OauthFeignService;
import com.zerody.user.mapper.SysStaffInfoMapper;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.mapper.UnionRoleStaffMapper;
import com.zerody.user.service.*;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author kuang
 */
@Slf4j
@Component
public class StaffInfoUtil {

    private static ImageService imageService;
    private static CommonFileService commonFileService;
    private static ConvertImageService convertImageService;
    private static SysStaffInfoMapper sysStaffInfoMapper;
    private static SysUserInfoMapper sysUserInfoMapper;
    private static SysLoginInfoService sysLoginInfoService;

    private static StaffHistoryService staffHistoryService;
    private static SysStaffRelationService sysStaffRelationService;
    private static OauthFeignService oauthFeignService;
    private static UnionRoleStaffMapper unionRoleStaffMapper;
    private static FamilyMemberService familyMemberService;
    private static UserResumeService userResumeService;
    @Autowired
    public void setFeign(ImageService imageService,CommonFileService commonFileService,ConvertImageService convertImageService,
        SysStaffInfoMapper sysStaffInfoMapper,SysUserInfoMapper sysUserInfoMapper,SysLoginInfoService sysLoginInfoService,
        StaffHistoryService staffHistoryService,SysStaffRelationService sysStaffRelationService,
                         OauthFeignService oauthFeignService,UnionRoleStaffMapper unionRoleStaffMapper,
                         FamilyMemberService familyMemberService,UserResumeService userResumeService
    ) {
        StaffInfoUtil.imageService = imageService;
        StaffInfoUtil.commonFileService = commonFileService;
        StaffInfoUtil.convertImageService = convertImageService;
        StaffInfoUtil.sysStaffInfoMapper = sysStaffInfoMapper;
        StaffInfoUtil.sysUserInfoMapper = sysUserInfoMapper;
        StaffInfoUtil.sysLoginInfoService = sysLoginInfoService;
        StaffInfoUtil.staffHistoryService = staffHistoryService;
        StaffInfoUtil.sysStaffRelationService = sysStaffRelationService;
        StaffInfoUtil.oauthFeignService = oauthFeignService;
        StaffInfoUtil.unionRoleStaffMapper = unionRoleStaffMapper;
        StaffInfoUtil.familyMemberService = familyMemberService;
        StaffInfoUtil.userResumeService = userResumeService;
    }

    public static void saveSysUserInfo(SysUserInfo sysUserInfo, String initPwd){
        //查看手机号或登录名是否被占用
        Boolean flag = sysUserInfoMapper.selectUserByPhone(sysUserInfo.getPhoneNumber());
        if (flag) {
            throw new DefaultException("该手机号已存在！");
        }
        // 修改时添加身份证唯一校验 不包含离职账户
        StaffInfoVo staffInfo = sysStaffInfoMapper.getUserByCertificateCard(sysUserInfo.getCertificateCard(), YesNo.NO);
        if (DataUtil.isNotEmpty(staffInfo)) {
            String hintContent = "该身份证号码已在“".concat(staffInfo.getCompanyName());
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(staffInfo.getDepartmentName())) {
                hintContent = hintContent.concat("-");
                hintContent = hintContent.concat(staffInfo.getDepartmentName());
            }
            hintContent = hintContent.concat("”存在，请再次确认");
            throw new DefaultException(hintContent);
        }
        //效验通过保存用户信息
        sysUserInfo.setRegisterTime(new Date());
        log.info("添加用户入库参数--{}", JSON.toJSONString(sysUserInfo));
        sysUserInfo.setCreateTime(new Date());
        sysUserInfo.setCreateUser("系统");
        sysUserInfo.setStatus(StatusEnum.activity.getValue());
        String avatar = sysUserInfo.getAvatar();
        sysUserInfo.setIsEdit(YesNo.YES);
        //  设置token删除状态 添加默认不删除token
        sysUserInfo.setIsDeleted(YesNo.NO);
        //  设置修改名称状态 添加默认 没有修改
        sysUserInfo.setIsUpdateName(YesNo.NO);
        sysUserInfoMapper.insert(sysUserInfo);
        //用户信息保存添加登录信息
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        SysLoginInfo logInfo = new SysLoginInfo();
        logInfo.setUserId(sysUserInfo.getId());
        logInfo.setMobileNumber(sysUserInfo.getPhoneNumber());
        logInfo.setNickname(sysUserInfo.getNickname());
        logInfo.setAvatar(avatar);
        logInfo.setUserPwd(passwordEncoder.encode(MD5Utils.MD5(initPwd)));
        logInfo.setStatus(StatusEnum.activity.getValue());
//        logInfo.setCreateId(UserUtils.getUser().getUserId()); //内部调用接口无token
        log.info("添加用户后生成登录账户入库参数--{}", JSON.toJSONString(logInfo));
        sysLoginInfoService.addOrUpdateLogin(logInfo);
    }

    /**成员关系处理 添加关系 ,荣耀记录,惩罚记录*/
    public static void saveRelation(SetSysUserInfoDto setSysUserInfoDto,SysUserInfo sysUserInfo,SysStaffInfo staff){

        //添加关系
        if (DataUtil.isNotEmpty(setSysUserInfoDto.getStaffRelationDtoList())) {
            setSysUserInfoDto.getStaffRelationDtoList().forEach(item -> {
                item.setRelationStaffId(staff.getId());
                item.setRelationStaffName(sysUserInfo.getUserName());
                item.setRelationUserId(sysUserInfo.getId());
                item.setStaffUserId(sysUserInfo.getId());
                item.setDesc(item.getDescribe());
                sysStaffRelationService.addRelation(item);
            });
        }
        //荣耀记录
        if (Objects.nonNull(setSysUserInfoDto.getStaffHistoryHonor())) {
            setSysUserInfoDto.getStaffHistoryHonor().forEach(item -> {
                item.setType(StaffHistoryTypeEnum.HONOR.name());
                item.setStaffId(staff.getId());
                staffHistoryService.addStaffHistory(item);
            });
        }
        //惩罚记录
        if (Objects.nonNull(setSysUserInfoDto.getStaffHistoryPunishment())) {
            setSysUserInfoDto.getStaffHistoryPunishment().forEach(item -> {
                item.setType(StaffHistoryTypeEnum.PUNISHMENT.name());
                item.setStaffId(staff.getId());
                staffHistoryService.addStaffHistory(item);
            });
        }
    }

    /**用户扩展信息新增 家庭成员 履历 学历证书 合规承诺书 合作申请表*/
    public static void saveExpandInfo(SetSysUserInfoDto setSysUserInfoDto,String userId,String staffId){
        StaffInfoVo staffInfoVo = new StaffInfoVo();
        staffInfoVo.setStaffId(staffId);
        staffInfoVo.setUserId(userId);
        //家庭成员
        familyMemberService.addBatchFamilyMember(setSysUserInfoDto.getFamilyMembers(), staffInfoVo);
        //履历
        userResumeService.saveOrUpdateBatchResume(setSysUserInfoDto.getUserResumes(), staffInfoVo);
        //学历证书
        saveImage(setSysUserInfoDto.getDiplomas(), userId, ImageTypeInfo.DIPLOMA);
        //合规承诺书
        saveImage(setSysUserInfoDto.getComplianceCommitments(), userId, ImageTypeInfo.COMPLIANCE_COMMITMENT);
        //合作申请表
        //saveFile(setSysUserInfoDto.getCooperationFiles(), userId, FileTypeInfo.COOPERATION_FILE);
        saveImage(setSysUserInfoDto.getCooperationImages(), userId, ImageTypeInfo.COOPERATION_APPLY);
    }

    public static  void saveImage(List<String> images, String userId, String type){
        List<Image> imageAdds = new ArrayList<>();
        Image image;
        if(DataUtil.isNotEmpty(images)) {

            for (String s : images) {
                image = new Image();
                image.setConnectId(userId);
                image.setId(UUIDutils.getUUID32());
                image.setImageType(type);
                image.setImageUrl(s);
                image.setCreateTime(new Date());
                imageAdds.add(image);
            }
        }
        QueryWrapper<Image> imageRemoveQw = new QueryWrapper<>();
        imageRemoveQw.lambda().eq(Image::getConnectId, userId);
        imageRemoveQw.lambda().eq(Image::getImageType, type);
        imageService.addImages(imageRemoveQw, imageAdds);
    }

    public static void saveFile(List<CommonFile> cooperationFiles, String userId, String type){

        List<CommonFile> files = new ArrayList<>();
        //图片转换
        List<ConvertImage> convertImages = new ArrayList<>(cooperationFiles.size());
        CommonFile file;
        Date now = new Date();

        if(DataUtil.isNotEmpty(cooperationFiles)) {
            for (CommonFile s : cooperationFiles) {
                file = new CommonFile();
                file.setConnectId(userId);
                file.setId(UUIDutils.getUUID32());
                file.setFileType(type);
                file.setFileUrl(s.getFileUrl());
                file.setFileName(s.getFileName());
                file.setFormat(s.getFormat());
                file.setCreateTime(new Date());

                files.add(file);
            }
        }
        QueryWrapper<CommonFile> remQ = new QueryWrapper<>();
        remQ.lambda().eq(CommonFile::getConnectId, userId);
        remQ.lambda().eq(CommonFile::getFileType, type);
        //删除之前的文件，再批量新增文件
        commonFileService.addFiles(remQ, files);
        if (DataUtil.isNotEmpty(convertImages)) {
            convertImageService.saveBatch(convertImages);
        }
    }
}
