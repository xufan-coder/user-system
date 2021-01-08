package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zerody.common.bean.DataResult;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.user.domain.UnionStaffPosition;
import com.zerody.user.domain.base.BaseModel;
import com.zerody.user.mapper.SysJobPositionMapper;
import com.zerody.user.domain.SysJobPosition;
import com.zerody.user.mapper.UnionStaffPositionMapper;
import com.zerody.user.service.SysJobPositionService;
import com.zerody.user.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    private UnionStaffPositionMapper unionStaffPositionMapper;


    @Override
    public DataResult addJob(SysJobPosition sysJobPosition) {

        //除了被删除的企业的岗位用名称查看数据库有没有这个名称
        QueryWrapper<SysJobPosition> jobQW = new QueryWrapper<>();
        jobQW.lambda().eq(SysJobPosition::getPositionName, sysJobPosition.getPositionName());
        jobQW.lambda().eq(SysJobPosition::getCompId, sysJobPosition.getCompId());
        jobQW.lambda().ne(SysJobPosition::getStatus, StatusEnum.删除.getValue());
        Integer jobNameCount = this.count(jobQW);
        if(jobNameCount > 0 ){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "该岗位名称已被占用",null);
        }
        sysJobPosition.setStatus(StatusEnum.激活.getValue());
        this.saveOrUpdate(sysJobPosition);
        return new DataResult();
    }

    @Override
    public DataResult deleteJobById(String jobId) {
        //判断岗位是否有人使用
        QueryWrapper<UnionStaffPosition> qw=new QueryWrapper<>();
        qw.lambda().eq(UnionStaffPosition::getPositionId,jobId);
        if(unionStaffPositionMapper.selectCount(qw)>0){
            throw new DefaultException("该岗位已有员工存在，不可删除！");
        }
        SysJobPosition job = new SysJobPosition();
        job.setStatus(StatusEnum.删除.getValue());
        job.setId(jobId);
        this.updateJob(job);
        return new DataResult();
    }

    @Override
    public  List<SysJobPosition> getJob(String departId) {
        QueryWrapper<SysJobPosition> qw=new QueryWrapper<>();
        qw.lambda().eq(SysJobPosition::getDepartId,departId);
        qw.lambda().ne(BaseModel::getStatus,StatusEnum.删除.getValue());
        List<SysJobPosition> sysJobPositions = sysJobPositionMapper.selectList(qw);
        return sysJobPositions;
    }

    @Override
    public SysJobPosition getJobByDepart(String name, String compId, String departId) {
        QueryWrapper<SysJobPosition> qw=new QueryWrapper<>();
        qw.lambda().eq(SysJobPosition::getDepartId,departId);
        qw.lambda().eq(SysJobPosition::getCompId,compId);
        qw.lambda().eq(SysJobPosition::getPositionName,name);
        qw.lambda().ne(BaseModel::getStatus,StatusEnum.删除.getValue());
        return sysJobPositionMapper.selectOne(qw);
    }

    @Override
    public SysJobPosition getJobByComp(String name, String compId) {
        QueryWrapper<SysJobPosition> qw=new QueryWrapper<>();
        qw.lambda().eq(SysJobPosition::getCompId,compId);
        qw.lambda().eq(SysJobPosition::getPositionName,name);
        qw.lambda().ne(BaseModel::getStatus,StatusEnum.删除.getValue());
        return sysJobPositionMapper.selectOne(qw);
    }

    @Override
    public DataResult updateJob(SysJobPosition sysJobPosition) {

        //除了被删除的企业的岗位用名称查看数据库有没有这个名称
        QueryWrapper<SysJobPosition> jobQW = new QueryWrapper<>();
        jobQW.lambda().eq(SysJobPosition::getPositionName, sysJobPosition.getPositionName());
        jobQW.lambda().eq(SysJobPosition::getCompId, sysJobPosition.getCompId());
        jobQW.lambda().ne(SysJobPosition::getStatus, StatusEnum.删除.getValue());
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
