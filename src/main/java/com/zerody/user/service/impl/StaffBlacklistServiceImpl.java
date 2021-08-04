package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.enums.user.StaffBlacklistApproveState;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.constant.ImageTypeInfo;
import com.zerody.user.domain.Image;
import com.zerody.user.domain.StaffBlacklist;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.dto.FrameworkBlacListQueryPageDto;
import com.zerody.user.dto.StaffBlacklistAddDto;
import com.zerody.user.mapper.StaffBlacklistMapper;
import com.zerody.user.service.ImageService;
import com.zerody.user.service.StaffBlacklistService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.vo.FrameworkBlacListQueryPageVo;
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
    }
}
