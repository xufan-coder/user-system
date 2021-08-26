package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.user.domain.Image;
import com.zerody.user.domain.StaffHistory;
import com.zerody.user.dto.StaffHistoryDto;
import com.zerody.user.dto.StaffHistoryQueryDto;
import com.zerody.user.enums.StaffHistoryTypeEnum;
import com.zerody.user.mapper.StaffHistoryMapper;
import com.zerody.user.service.ImageService;
import com.zerody.user.service.StaffHistoryService;
import com.zerody.user.vo.StaffHistoryVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author zhangpingping
 * @date 2021年08月25日 17:25
 */
@Slf4j
@Service
public class StaffHistoryServiceImpl extends ServiceImpl<StaffHistoryMapper, StaffHistory> implements StaffHistoryService {
    @Autowired
    private ImageService imageService;

    @Override
    public void addStaffHistory(StaffHistoryDto staffHistoryDto) {
        StaffHistory staffHistory = new StaffHistory();
        BeanUtils.copyProperties(staffHistoryDto, staffHistory);
        staffHistory.setId(UUIDutils.getUUID32());
        if (Objects.nonNull(staffHistoryDto.getImageList())) {
            staffHistoryDto.getImageList().forEach(item -> {
                item.setId(UUIDutils.getUUID32());
                item.setConnectId(staffHistory.getId());
                item.setImageType(staffHistoryDto.getType());
                item.setCreateTime(new Date());
                imageService.save(item);
            });
        }
        this.save(staffHistory);
    }

    @Override
    public void removeStaffHistory(StaffHistoryQueryDto staffHistoryQueryDto) {
        if (StringUtils.isEmpty(staffHistoryQueryDto.getId())) {
            throw new DefaultException("参数ID不能为空");
        }
        QueryWrapper<Image> imageQueryWrapper = new QueryWrapper<>();
        imageQueryWrapper.lambda().eq(Image::getConnectId, staffHistoryQueryDto.getId());
        imageQueryWrapper.lambda().eq(Image::getImageType, staffHistoryQueryDto.getType());
        this.imageService.removeById(imageQueryWrapper);
        QueryWrapper<StaffHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StaffHistory::getStaffId, staffHistoryQueryDto.getStaffId());
        queryWrapper.lambda().eq(StaffHistory::getType, staffHistoryQueryDto.getType());
        this.remove(queryWrapper);
    }

    @Override
    public void modifyStaffHistory(StaffHistoryDto staffHistoryDto) {
        if (StringUtils.isEmpty(staffHistoryDto.getId())) {
            throw new DefaultException("参数ID不能为空");
        }
        StaffHistory staffHistory = new StaffHistory();
        BeanUtils.copyProperties(staffHistoryDto, staffHistory);
        StaffHistoryQueryDto staffHistoryQueryDto=new StaffHistoryQueryDto();
        staffHistoryQueryDto.setStaffId(staffHistoryDto.getStaffId());
        staffHistoryQueryDto.setId(staffHistoryDto.getStaffId());
        staffHistoryQueryDto.setType(staffHistoryDto.getType());
        this.removeStaffHistory(staffHistoryQueryDto);
        if (Objects.nonNull(staffHistoryDto.getImageList())) {
            staffHistoryDto.getImageList().forEach(item -> {
                item.setId(UUIDutils.getUUID32());
                item.setConnectId(staffHistory.getId());
                item.setImageType(staffHistoryDto.getType());
                item.setCreateTime(new Date());
                imageService.save(item);
            });
            this.save(staffHistory);
        }
    }

    @Override
    public List<StaffHistoryVo> queryStaffHistory(StaffHistoryQueryDto staffHistoryQueryDto) {
        List<StaffHistoryVo> staffHistoryVos = Lists.newArrayList();
        StaffHistoryVo staffHistoryVo = new StaffHistoryVo();
        QueryWrapper<StaffHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StringUtils.isNotEmpty(staffHistoryQueryDto.getStaffId()), StaffHistory::getStaffId, staffHistoryQueryDto.getStaffId());
        queryWrapper.lambda().eq(StringUtils.isNotEmpty(staffHistoryQueryDto.getType()), StaffHistory::getType, staffHistoryQueryDto.getType());
        queryWrapper.lambda().orderByDesc(StaffHistory::getTime,StaffHistory::getCreateTime);
        List<StaffHistory> list = this.list(queryWrapper);
        if (Objects.nonNull(list)) {
            for (StaffHistory staffHistory : list) {
                BeanUtils.copyProperties(staffHistory, staffHistoryVo);
                QueryWrapper<Image> imageQueryWrapper = new QueryWrapper<>();
                imageQueryWrapper.lambda().eq(StringUtils.isNotEmpty(staffHistory.getId()), Image::getConnectId, staffHistory.getId());
                imageQueryWrapper.lambda().eq(StringUtils.isNotEmpty(staffHistory.getType()), Image::getImageType, staffHistory.getType());
                List<Image> imageList = this.imageService.list(imageQueryWrapper);
                staffHistoryVo.setImageList(imageList);
                staffHistoryVos.add(staffHistoryVo);
            }
        }
        return staffHistoryVos;
    }


}
