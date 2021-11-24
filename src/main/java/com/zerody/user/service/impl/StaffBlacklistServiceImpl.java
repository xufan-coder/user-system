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
import com.zerody.user.api.vo.StaffInfoVo;
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
    public StaffBlacklistAddDto addStaffBlaklist(StaffBlacklistAddDto param) {
        StaffBlacklist blac = param.getBlacklist();
        if (StringUtils.isEmpty(blac.getId())) {
            QueryWrapper<StaffBlacklist> blacQw = new QueryWrapper<>();
            StaffInfoVo staffInfo = staffInfoService.getStaffInfo(blac.getUserId());
            if(DataUtil.isNotEmpty(staffInfo)){
                blacQw.lambda().eq(StaffBlacklist::getMobile, staffInfo.getMobile());
                blacQw.lambda().eq(StaffBlacklist::getCompanyId, staffInfo.getCompanyId());
                StaffBlacklist oldBlac = this.getOne(blacQw);
                if (DataUtil.isNotEmpty(oldBlac)) {
                    throw new DefaultException("该员工已被拉黑无法！无法重复发起");
                }
            }
//            this.remove(blacQw);
            StaffInfoVo staff = this.staffInfoService.getStaffInfo(blac.getUserId());
            blac.setCreateTime(new Date());
            blac.setApprovalTime(new Date());
            blac.setCompanyId(staff.getCompanyId());
            blac.setState(StaffBlacklistApproveState.BLOCK.name());
            blac.setMobile(staff.getMobile());
            blac.setId(UUIDutils.getUUID32());
            this.save(blac);
        } else {
            this.updateById(blac);
        }
    //把员工设为离职
        QueryWrapper<SysStaffInfo> staffQw = new QueryWrapper<>();
        staffQw.lambda().eq(SysStaffInfo::getUserId, blac.getUserId());
        SysStaffInfo  staff = this.staffInfoService.getOne(staffQw);
        UpdateWrapper<SysUserInfo> userUw = new UpdateWrapper<>();
        userUw.lambda().set(SysUserInfo::getIsEdit, YesNo.YES);
        userUw.lambda().set(SysUserInfo::getStatus, StatusEnum.stop.getValue());
        userUw.lambda().eq(true, SysUserInfo::getId, blac.getUserId());
        this.userInfoService.update(userUw);
        staff.setStatus(StatusEnum.stop.getValue());
        staff.setLeaveReason(param.getBlacklist().getReason());
        this.staffInfoService.updateById(staff);
        this.checkUtil.removeUserToken(staff.getUserId());
        if (CollectionUtils.isEmpty(param.getImages())) {
            return param;
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
        return param;
    }

    @Override
    public IPage<FrameworkBlacListQueryPageVo> getPageBlackList(FrameworkBlacListQueryPageDto param) {
        IPage<FrameworkBlacListQueryPageVo> iPage = new Page<>(param.getCurrent(), param.getPageSize());
        iPage = this.baseMapper.getPageBlackList(param, iPage);
        return iPage;
    }

    @Override
    public void doRelieveByStaffId(String userId) {
        UpdateWrapper<StaffBlacklist> relieveUw = new UpdateWrapper<>();
        relieveUw.lambda().eq(StaffBlacklist::getUserId, userId);
        relieveUw.lambda().set(StaffBlacklist::getState, StaffBlacklistApproveState.RELIEVE.name());
        relieveUw.lambda().set(StaffBlacklist::getUpdateTime, new Date());
        this.update(relieveUw);
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
