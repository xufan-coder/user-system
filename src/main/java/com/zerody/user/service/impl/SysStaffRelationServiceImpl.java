package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.domain.SysStaffRelation;
import com.zerody.user.dto.SysStaffRelationDto;
import com.zerody.user.mapper.SysStaffRelationMapper;
import com.zerody.user.service.SysStaffRelationService;
import com.zerody.user.vo.SysStaffRelationVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public void addRelation(SysStaffRelationDto sysStaffRelationDto) {
        SysStaffRelation sysStaffRelation = new SysStaffRelation();
        //赋值
        setSysStaffRelationVo(sysStaffRelation, sysStaffRelationDto);
        sysStaffRelation.setDeletd(YesNo.NO);
        sysStaffRelation.setCreateTime(new Date());
        this.save(sysStaffRelation);
    }

    @Override
    public void removeRelation(SysStaffRelationDto sysStaffRelationDto) {
        if (Objects.nonNull(sysStaffRelationDto)) {
            if (StringUtils.isEmpty(sysStaffRelationDto.getRelationStaffId())) {
                throw new DefaultException("RelationStaffId不能为空");
            }
        }else{
            throw new DefaultException("参数不能为空");
        }
        //逻辑删除
        UpdateWrapper<SysStaffRelation> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(SysStaffRelation::getDeletd, YesNo.YES);
        updateWrapper.lambda().eq(StringUtils.isNotEmpty(sysStaffRelationDto.getStaffId()),SysStaffRelation::getStaffId, sysStaffRelationDto.getStaffId())
                .or().eq(StringUtils.isNotEmpty(sysStaffRelationDto.getRelationStaffId()),SysStaffRelation::getRelationStaffId, sysStaffRelationDto.getRelationStaffId());
        this.update(updateWrapper);
    }

    @Override
    public List<SysStaffRelationVo> queryRelationList(SysStaffRelationDto sysStaffRelationDto) {
        List<SysStaffRelationVo> sysStaffRelationVos = Lists.newArrayList();
        QueryWrapper<SysStaffRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysStaffRelation::getStaffId, sysStaffRelationDto.getStaffId());
        queryWrapper.lambda().eq(SysStaffRelation::getRelationStaffId, sysStaffRelationDto.getRelationStaffId());
        queryWrapper.lambda().eq(SysStaffRelation::getDeletd, YesNo.NO);
        List<SysStaffRelation> sysStaffRelations = this.list(queryWrapper);
        sysStaffRelations.forEach(item -> {
            SysStaffRelationVo sysStaffRelationVo = new SysStaffRelationVo();
            sysStaffRelationVo.setId(item.getId());
            sysStaffRelationVo.setStaffId(item.getStaffId());
            sysStaffRelationVo.setUserName(item.getStaffName());
            sysStaffRelationVo.setDepartId(item.getDepartId());
            sysStaffRelationVo.setDepartName(item.getDepartName());
            sysStaffRelationVo.setDesc(item.getDescribe());
            sysStaffRelationVo.setRelationStaffId(item.getRelationStaffId());
            sysStaffRelationVo.setRelationStaffName(item.getRelationStaffName());
            sysStaffRelationVos.add(sysStaffRelationVo);
        });
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
    }
}
