package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.enums.user.StaffBlacklistApproveState;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.constant.ImageTypeInfo;
import com.zerody.user.domain.Image;
import com.zerody.user.domain.StaffBlacklist;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.FrameworkBlacListQueryPageDto;
import com.zerody.user.dto.StaffBlacklistAddDto;
import com.zerody.user.enums.StaffStatusEnum;
import com.zerody.user.mapper.StaffBlacklistMapper;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.service.ImageService;
import com.zerody.user.service.StaffBlacklistService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.FrameworkBlacListQueryPageVo;
import com.zerody.user.vo.MobileBlacklistQueryVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName StaffBlacklistServiceImpl
 * @DateTime 2021/8/4_9:26
 * @Deacription TODO
 */
@Slf4j
@Service
public class StaffBlacklistServiceImpl extends ServiceImpl<StaffBlacklistMapper, StaffBlacklist> implements StaffBlacklistService {


    @Autowired
    private ImageService imageService;

    @Autowired
    private SysStaffInfoService staffInfoService;

    @Autowired
    private SysUserInfoService userInfoService;

    @Autowired
    private CheckUtil checkUtil;

    @Override
    public void addStaffBlaklist(StaffBlacklistAddDto param) {
        StaffBlacklist blac = param.getBlacklist();
        if (StringUtils.isEmpty(blac.getId())) {
            QueryWrapper<StaffBlacklist> blacQw = new QueryWrapper<>();
            blacQw.lambda().eq(StaffBlacklist::getMobile, blac.getMobile());
            blacQw.lambda().eq(StaffBlacklist::getCompanyId, blac.getCompanyId());
            StaffBlacklist oldBlac = this.getOne(blacQw);
            if (DataUtil.isNotEmpty(oldBlac)) {
                if (StaffBlacklistApproveState.APPROVE.name().equals(oldBlac.getState())) {
                    throw new DefaultException("该员工已被拉黑正在审批中！无法重复发起");
                }
                if (StaffBlacklistApproveState.PASS.name().equals(oldBlac.getState())) {
                    throw new DefaultException("该员工已被拉黑无法！无法重复发起");
                }
            }
            this.remove(blacQw);
            SysStaffInfo staff = this.staffInfoService.getById(blac.getApplicantStaffId());
            blac.setCreateTime(new Date());
            blac.setCompanyId(staff.getCompId());
            blac.setState(StaffBlacklistApproveState.APPROVE.name());
            blac.setId(UUIDutils.getUUID32());
            this.save(blac);
        } else {
            this.updateById(blac);
        }
        //把员工设为离职
        if (StaffBlacklistApproveState.PASS.name().equals(blac.getState())) {
            QueryWrapper<SysStaffInfo> staffQw = new QueryWrapper<>();
            staffQw.lambda().inSql(true, SysStaffInfo::getId,  "SELECT sb.staff_id FROM staff_blacklist AS sb WHERE sb.id = '".concat(blac.getId()).concat("'"));
            SysStaffInfo  staff = this.staffInfoService.getOne(staffQw);
            UpdateWrapper<SysUserInfo> userUw = new UpdateWrapper<>();
            userUw.lambda().set(SysUserInfo::getIsEdit, YesNo.YES);
            userUw.lambda().set(SysUserInfo::getStatus, StatusEnum.stop.getValue());
            userUw.lambda().eq(true, SysUserInfo::getId, staff.getUserId());
            this.userInfoService.update(userUw);
            staff.setStatus(StatusEnum.stop.getValue());
            this.staffInfoService.updateById(staff);
            this.checkUtil.removeUserToken(staff.getUserId());
        }
        if (CollectionUtils.isEmpty(param.getImages())) {
            return;
        }
        List<String> images = param.getImages();
        List<Image> imageAdds = new ArrayList<>();
        Image image;
        for (String s : images) {
            image = new Image();
            image.setConnectId(blac.getId());
            image.setId(UUIDutils.getUUID32());
            image.setImageType(ImageTypeInfo.STAFF_BLACKLIST);
            image.setImageUrl(s);
            image.setCreateTime(new Date());
            imageAdds.add(image);
        }
        QueryWrapper<Image> imageRemoveQw = new QueryWrapper<>();
        imageRemoveQw.lambda().eq(Image::getConnectId, blac.getId());
        imageRemoveQw.lambda().eq(Image::getImageType, ImageTypeInfo.STAFF_BLACKLIST);
        this.imageService.addImages(imageRemoveQw, imageAdds);
    }

    @Override
    public IPage<FrameworkBlacListQueryPageVo> getPageBlackList(FrameworkBlacListQueryPageDto param) {
        IPage<FrameworkBlacListQueryPageVo> iPage = new Page<>(param.getCurrent(), param.getPageSize());
        iPage = this.baseMapper.getPageBlackList(param, iPage);
        return iPage;
    }

    @Override
    public void doRelieveByStaffId(String staffId) {
        QueryWrapper<StaffBlacklist> removeQw = new QueryWrapper<>();
        removeQw.lambda().eq(StaffBlacklist::getStaffId, staffId);
        this.remove(removeQw);
//        UpdateWrapper<SysUserInfo> userUw = new UpdateWrapper<>();
//        userUw.lambda().set(SysUserInfo::getIsEdit, YesNo.YES);
//        userUw.lambda().set(SysUserInfo::getStatus, StatusEnum.stop.getValue());
//        userUw.lambda().inSql(true, SysUserInfo::getId,
//                "SELECT ssi.user_id FROM sys_staff_info AS ssi  WHERE ssi.id = '".concat(staffId).concat("'")
//        );
//        this.userInfoService.update(userUw);
    }

    @Override
    public MobileBlacklistQueryVo getBlacklistByMobile(String mobile) {
        MobileBlacklistQueryVo  result = new MobileBlacklistQueryVo();
        List<String> companys = this.baseMapper.getBlacklistByMobile(mobile);
        result.setIsBlock(CollectionUtils.isNotEmpty(companys));
        result.setCompanyNames(companys);
        return result;
    }
}
