package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.bean.DataResult;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.user.dto.SysJobPositionDto;
import com.zerody.user.enums.DataRecordStatusEnum;
import com.zerody.user.mapper.SysJobPositionMapper;
import com.zerody.user.domain.SysJobPosition;
import com.zerody.user.service.SysJobPositionService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.SysJobPositionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author PengQiang
 * @ClassName SysJobPositionServicImpl
 * @DateTime 2020/12/18_18:13
 * @Deacription TODO
 */
@Service
@Slf4j
public class SysJobPositionServicImpl extends BaseService<SysJobPositionMapper, SysJobPosition> implements SysJobPositionService {

    @Autowired
    private SysJobPositionMapper sysJobPositionMapper;

    @Override
    public DataResult getPageJob(SysJobPositionDto sysJobPositionDto) {
        IPage<SysJobPositionVo> iPage = new Page<>(sysJobPositionDto.getCurrent(),sysJobPositionDto.getPageSize());
        iPage = sysJobPositionMapper.getPageJob(sysJobPositionDto,iPage);
        return new DataResult(iPage);
    }

    @Override
    public DataResult addJob(SysJobPosition sysJobPosition) {

        //除了被删除的企业的岗位用名称查看数据库有没有这个名称
        QueryWrapper<SysJobPosition> jobQW = new QueryWrapper<>();
        jobQW.lambda().eq(SysJobPosition::getPositionName, sysJobPosition.getPositionName());
        jobQW.lambda().ne(SysJobPosition::getStatus, DataRecordStatusEnum.DELETED.getCode());
        //此处少了企业条件
        Integer jobNameCount = this.count(jobQW);
        if(jobNameCount > 0 ){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "该岗位名称已被占用",null);
        }
        sysJobPosition.setStatus(DataRecordStatusEnum.VALID.getCode());
        this.saveOrUpdate(sysJobPosition);
        return new DataResult();
    }

    @Override
    public DataResult deleteJobById(String jobId) {
        SysJobPosition job = new SysJobPosition();
        job.setStatus(DataRecordStatusEnum.DELETED.getCode());
        job.setId(jobId);
        this.updateJob(job);
        return new DataResult();
    }

    @Override
    public DataResult updateJob(SysJobPosition sysJobPosition) {

        //除了被删除的企业的岗位用名称查看数据库有没有这个名称
        QueryWrapper<SysJobPosition> jobQW = new QueryWrapper<>();
        jobQW.lambda().eq(SysJobPosition::getPositionName, sysJobPosition.getPositionName());
        jobQW.lambda().ne(SysJobPosition::getStatus, DataRecordStatusEnum.DELETED.getCode());
        jobQW.lambda().ne(SysJobPosition::getId, sysJobPosition.getId());
        //此处少了企业条件
        Integer jobNameCount = this.count(jobQW);
        if(jobNameCount > 0 ){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "该岗位名称已被占用",null);
        }
        this.saveOrUpdate(sysJobPosition);
        return new DataResult();
    }


}
