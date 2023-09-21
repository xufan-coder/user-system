package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.constant.ImageTypeInfo;
import com.zerody.user.domain.Image;
import com.zerody.user.domain.StaffBlacklistApprover;
import com.zerody.user.enums.ApproveStatusEnum;
import com.zerody.user.mapper.StaffBlacklistApproverMapper;
import com.zerody.user.service.ImageService;
import com.zerody.user.service.StaffBlacklistApproverService;
import com.zerody.user.service.SysStaffInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class StaffBlacklistApproverServiceImpl extends ServiceImpl<StaffBlacklistApproverMapper, StaffBlacklistApprover> implements StaffBlacklistApproverService {

    @Autowired
    private SysStaffInfoService staffInfoService;
    @Autowired
    private ImageService imageService;

    @Override
    public StaffBlacklistApprover addStaffBlaklistRecord(StaffBlacklistApprover staffBlacklistApprover) {
        staffBlacklistApprover.setId(UUIDutils.getUUID32());
        staffBlacklistApprover.setCreateTime(new Date());
        staffBlacklistApprover.setApproveState(ApproveStatusEnum.APPROVAL.name());
        if(DataUtil.isNotEmpty(staffBlacklistApprover.getUserId())){
            //填部门，企业名称 岗位 和角色
            StaffInfoVo staff = this.staffInfoService.getStaffInfo(staffBlacklistApprover.getUserId());
            staffBlacklistApprover.setDtptId(staff.getDepartId());
            staffBlacklistApprover.setDtptName(staff.getDepartmentName());
            staffBlacklistApprover.setPostName(staff.getPositionName());
            staffBlacklistApprover.setRoleName(staff.getRoleName());
            staffBlacklistApprover.setRoleId(staff.getRoleId());
        }
        this.save(staffBlacklistApprover);
        //保存图片
        List<String> images = staffBlacklistApprover.getImages();
        List<Image> imageAdds = new ArrayList<>();
        Image image;
        for (String s : images) {
            image = new Image();
            image.setConnectId(staffBlacklistApprover.getId());
            image.setId(UUIDutils.getUUID32());
            image.setImageType(ImageTypeInfo.STAFF_BLACKLIST_RECORD);
            image.setImageUrl(s);
            image.setCreateTime(new Date());
            imageAdds.add(image);
        }
        QueryWrapper<Image> imageRemoveQw = new QueryWrapper<>();
        imageRemoveQw.lambda().eq(Image::getConnectId, staffBlacklistApprover.getId());
        imageRemoveQw.lambda().eq(Image::getImageType, ImageTypeInfo.STAFF_BLACKLIST_RECORD);
        this.imageService.addImages(imageRemoveQw, imageAdds);
        return staffBlacklistApprover;
    }
}
