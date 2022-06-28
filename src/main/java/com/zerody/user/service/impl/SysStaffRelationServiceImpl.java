package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.domain.SysStaffRelation;
import com.zerody.user.dto.SysStaffRelationDto;
import com.zerody.user.mapper.SysStaffInfoMapper;
import com.zerody.user.mapper.SysStaffRelationMapper;
import com.zerody.user.service.SysStaffRelationService;
import com.zerody.user.vo.RecommendInfoVo;
import com.zerody.user.vo.SysStaffInfoRelationVo;
import com.zerody.user.vo.SysStaffRelationVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author zhangpingping
 * @date 2021年09月09日 16:16
 */
@Slf4j
@Service
public class SysStaffRelationServiceImpl extends ServiceImpl<SysStaffRelationMapper, SysStaffRelation> implements SysStaffRelationService {
    @Autowired
    private SysStaffInfoServiceImpl sysStaffInfoService;
    @Autowired
    private SysStaffInfoMapper sysStaffInfoMapper;

    @Override
    public void addRelation(SysStaffRelationDto sysStaffRelationDto) {
        SysStaffRelation sysStaffRelation = new SysStaffRelation();
        //赋值
        sysStaffRelation.setId(UUIDutils.getUUID32());
        setSysStaffRelationVo(sysStaffRelation, sysStaffRelationDto);
        sysStaffRelation.setDeletd(YesNo.NO);
        sysStaffRelation.setCreateTime(new Date());
        this.save(sysStaffRelation);

        SysStaffRelation sysStaffRelation1 = new SysStaffRelation();
        sysStaffRelation1.setId(UUIDutils.getUUID32());
        sysStaffRelation1.setStaffId(sysStaffRelationDto.getRelationStaffId());
        sysStaffRelation1.setStaffName(sysStaffRelationDto.getRelationStaffName());
        sysStaffRelation1.setRelationStaffId(sysStaffRelationDto.getStaffId());
        sysStaffRelation1.setRelationStaffName(sysStaffRelationDto.getUserName());
        sysStaffRelation1.setDepartId(sysStaffRelationDto.getDepartId());
        sysStaffRelation1.setDepartName(sysStaffRelationDto.getDepartName());
        sysStaffRelation1.setDescribe(sysStaffRelationDto.getDesc());
        sysStaffRelation1.setDeletd(YesNo.NO);
        sysStaffRelation1.setStaffUserId(sysStaffRelationDto.getRelationUserId());
        sysStaffRelation1.setRelationUserId(sysStaffRelationDto.getStaffUserId());
        sysStaffRelation1.setCreateTime(new Date());
        this.save(sysStaffRelation1);
    }

    @Override
    public void removeRelation(SysStaffRelationDto sysStaffRelationDto) {
        if (Objects.nonNull(sysStaffRelationDto)) {
            if (StringUtils.isEmpty(sysStaffRelationDto.getRelationStaffId())) {
                throw new DefaultException("RelationStaffId不能为空");
            }
        } else {
            throw new DefaultException("参数不能为空");
        }
        //逻辑删除
        UpdateWrapper<SysStaffRelation> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(SysStaffRelation::getDeletd, YesNo.YES);
        updateWrapper.lambda().eq(StringUtils.isNotEmpty(sysStaffRelationDto.getStaffId()), SysStaffRelation::getStaffId, sysStaffRelationDto.getStaffId())
                .or().eq(StringUtils.isNotEmpty(sysStaffRelationDto.getRelationStaffId()), SysStaffRelation::getRelationStaffId, sysStaffRelationDto.getRelationStaffId());
        this.update(updateWrapper);
    }

    @Override
    public SysStaffInfoRelationVo queryRelationList(SysStaffRelationDto sysStaffRelationDto) {
        SysStaffInfoRelationVo sysStaffInfoRelationVo=new SysStaffInfoRelationVo();
        List<SysStaffRelationVo> sysStaffRelationVos = this.baseMapper.queryRelationList(sysStaffRelationDto);
        log.info("关系查询:{}", JSON.toJSONString(sysStaffRelationVos));
        sysStaffRelationVos.forEach(item -> {
            if (DataUtil.isNotEmpty(item.getStaffUserId())) {
                StaffInfoVo staffInfo = sysStaffInfoService.getStaffInfo(item.getStaffUserId());
                item.setCompanyName(staffInfo.getCompanyName());
                item.setDepartName(staffInfo.getDepartmentName());
                item.setPositionName(staffInfo.getPositionName());
                item.setCompanyId(staffInfo.getCompanyId());
                item.setDepartId(staffInfo.getDepartId());
            }
            if (DataUtil.isNotEmpty(item.getRelationUserId())) {
                StaffInfoVo staffInfoVo = sysStaffInfoService.getStaffInfo(item.getRelationUserId());
                item.setRelationCompanyName(staffInfoVo.getCompanyName());
                item.setRelationDepartName(staffInfoVo.getDepartmentName());
                item.setRelationPositionName(staffInfoVo.getPositionName());
                item.setRelationCompanyId(staffInfoVo.getCompanyId());
                item.setRelationDepartId(staffInfoVo.getDepartId());
            }
        });

        SysStaffInfo sysStaffInfo = sysStaffInfoService.getById(sysStaffRelationDto.getStaffId());
        if (Objects.nonNull(sysStaffInfo) &&StringUtils.isNotEmpty(sysStaffInfo.getRecommendId())&& sysStaffInfo.getRecommendType().intValue() == 1) {
            RecommendInfoVo recommendInfoVo=sysStaffInfoMapper.getRecommendInfo(sysStaffInfo.getRecommendId());
            sysStaffInfoRelationVo.setRecommendInfoVoOne(recommendInfoVo);
            if (Objects.nonNull(recommendInfoVo)&&StringUtils.isNotEmpty(recommendInfoVo.getRecommendId()) && recommendInfoVo.getRecommendType().intValue() == 1) {
                RecommendInfoVo recommendInfoVo1=sysStaffInfoMapper.getRecommendInfo(recommendInfoVo.getRecommendId());
                sysStaffInfoRelationVo.setRecommendInfoVoTwo(recommendInfoVo1);
            }
        }
        sysStaffInfoRelationVo.setSysStaffRelationVos(sysStaffRelationVos);
        return sysStaffInfoRelationVo;
    }

    @Override
    public List<SysStaffRelationVo> queryRelationByListId(SysStaffRelationDto sysStaffRelationDto) {
        List<SysStaffRelationVo> sysStaffRelationVos = this.baseMapper.queryRelationList(sysStaffRelationDto);
        return sysStaffRelationVos;
    }


    private void setSysStaffRelationVo(SysStaffRelation sysStaffRelation, SysStaffRelationDto sysStaffRelationDto) {
        sysStaffRelation.setStaffId(sysStaffRelationDto.getStaffId());
        sysStaffRelation.setStaffName(sysStaffRelationDto.getUserName());
        sysStaffRelation.setRelationStaffId(sysStaffRelationDto.getRelationStaffId());
        sysStaffRelation.setRelationStaffName(sysStaffRelationDto.getRelationStaffName());
        sysStaffRelation.setDepartId(sysStaffRelationDto.getDepartId());
        sysStaffRelation.setDepartName(sysStaffRelationDto.getDepartName());
        sysStaffRelation.setDescribe(sysStaffRelationDto.getDesc());
        sysStaffRelation.setStaffUserId(sysStaffRelationDto.getStaffUserId());
        sysStaffRelation.setRelationUserId(sysStaffRelationDto.getRelationUserId());
    }
}
