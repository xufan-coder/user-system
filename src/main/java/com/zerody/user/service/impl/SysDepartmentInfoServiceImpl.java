package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.user.domain.SysCompanyInfo;
import com.zerody.user.domain.SysDepartmentInfo;
import com.zerody.user.domain.UnionStaffDepart;
import com.zerody.user.domain.base.BaseModel;
import com.zerody.user.dto.SetAdminAccountDto;
import com.zerody.user.mapper.SysDepartmentInfoMapper;
import com.zerody.user.mapper.SysJobPositionMapper;
import com.zerody.user.mapper.UnionStaffDepartMapper;
import com.zerody.user.service.SysDepartmentInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.SysDepartmentInfoVo;
import com.zerody.user.vo.SysJobPositionVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author PengQiang
 * @ClassName SysDepartmentInfoServiceImpl
 * @DateTime 2020/12/19_13:20
 * @Deacription TODO
 */
@Service
@Slf4j
public class SysDepartmentInfoServiceImpl extends BaseService<SysDepartmentInfoMapper, SysDepartmentInfo> implements SysDepartmentInfoService {


    @Autowired
    private SysDepartmentInfoMapper sysDepartmentInfoMapper;

    @Autowired
    private SysJobPositionMapper sysJobPositionMapper;

    @Autowired
    private UnionStaffDepartMapper unionStaffDepartMapper;

    @Override
    public void addDepartment(SysDepartmentInfo sysDepartmentInfo) {
        log.info("B端添加部门入参-{}",sysDepartmentInfo);
        //查看部门名称是否存在
        QueryWrapper<SysDepartmentInfo> depQW =  new QueryWrapper<>();
        depQW.lambda().ne(SysDepartmentInfo::getStatus, StatusEnum.删除.getValue());
        depQW.lambda().eq(SysDepartmentInfo::getDepartName, sysDepartmentInfo.getDepartName());
        depQW.lambda().eq(SysDepartmentInfo::getCompId, sysDepartmentInfo.getCompId());
        Integer count = sysDepartmentInfoMapper.selectCount(depQW);
        if(count > 0){
            throw new DefaultException("该部门名称已存在!");
        }
        sysDepartmentInfo.setStatus(StatusEnum.激活.getValue());
        log.info("B端添加部门入库-{}",sysDepartmentInfo);
        //名称不存在 保存添加
        this.saveOrUpdate(sysDepartmentInfo);
    }

    @Override
    public void updateDepartment(SysDepartmentInfo sysDepartmentInfo) {
        log.info("B端添加部门入参-{}",sysDepartmentInfo);
        //查看部门名称是否存在
        QueryWrapper<SysDepartmentInfo> depQW =  new QueryWrapper<>();
        depQW.lambda().ne(SysDepartmentInfo::getStatus,StatusEnum.删除.getValue());
        depQW.lambda().eq(SysDepartmentInfo::getDepartName, sysDepartmentInfo.getDepartName());
        depQW.lambda().ne(SysDepartmentInfo::getId, sysDepartmentInfo.getId());
        Integer count = sysDepartmentInfoMapper.selectCount(depQW);
        if(count > 0){
              throw new DefaultException("该部门名称已存在!");
        }
        log.info("B端修改部门入库-{}",sysDepartmentInfo);
        this.saveOrUpdate(sysDepartmentInfo);
    }

    @Override
    public void deleteDepartmentById(String depId) {
        QueryWrapper<UnionStaffDepart> unQw = new QueryWrapper<>();
        unQw.lambda().eq(UnionStaffDepart::getDepartmentId, depId);
        Integer count = unionStaffDepartMapper.selectCount(unQw);
        if (count > 0){
            throw new DefaultException("该部门下有员工不可删除!");
        }
        //逻辑删除部门
        SysDepartmentInfo dep = new SysDepartmentInfo();
        dep.setStatus(StatusEnum.删除.getValue());
        dep.setId(depId);
        this.saveOrUpdate(dep);
    }

    @Override
    public List<SysDepartmentInfoVo> getAllDepByCompanyId(String companyId) {
        List<SysDepartmentInfoVo> deps = sysDepartmentInfoMapper.getAllDepByCompanyId(companyId);
        List<SysJobPositionVo> jobs = sysJobPositionMapper.getAllJobByCompanyId(companyId);
        return getDepChildrens("", deps, jobs);
    }

    @Override
    public List<SysDepartmentInfo> getDepartmentByComp(String compId) {
        QueryWrapper<SysDepartmentInfo> qw =new QueryWrapper<>();
        qw.lambda().eq(SysDepartmentInfo::getCompId,compId);
        qw.lambda().ne(BaseModel::getStatus,StatusEnum.删除.getValue());
        return sysDepartmentInfoMapper.selectList(qw);
    }

    @Override
    public SysDepartmentInfo getByName(String name,String compId) {
        QueryWrapper<SysDepartmentInfo> qw =new QueryWrapper<>();
        qw.lambda().eq(SysDepartmentInfo::getCompId,compId);
        qw.lambda().eq(SysDepartmentInfo::getDepartName,name);
        qw.lambda().ne(BaseModel::getStatus,StatusEnum.删除.getValue());
        return sysDepartmentInfoMapper.selectOne(qw);
    }

    @Override
    public void updateAdminAccout(SetAdminAccountDto dto) {
        if(StringUtils.isEmpty(dto.getId())){
            throw new DefaultException("企业id为空");
        }
        if(StringUtils.isEmpty(dto.getStaffId())){
            throw new DefaultException("员工id为空");
        }
        UpdateWrapper<SysDepartmentInfo> comUw = new UpdateWrapper<>();
        comUw.lambda().set(SysDepartmentInfo::getAdminAccount, dto.getStaffId());
        comUw.lambda().eq(SysDepartmentInfo::getId, dto.getId());
        this.update(comUw);
    }

    private List<SysDepartmentInfoVo> getDepChildrens(String parentId, List<SysDepartmentInfoVo> deps, List<SysJobPositionVo> jobs){
        List<SysDepartmentInfoVo> childs = deps.stream().filter(d -> parentId.equals(d.getParentId())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(childs)){
            return new ArrayList<>();
        }
        for (SysDepartmentInfoVo dep : childs){
            List<SysJobPositionVo> jobChilds = jobs.stream().filter(j -> dep.getId().equals(j.getDepartId())).collect(Collectors.toList());
            dep.setJobChildrens(jobChilds);
            dep.setDepartChildrens(getDepChildrens(dep.getId(), deps, jobs));
        }
        return childs;
    }

    private List<SysJobPositionVo> getJobChildrens(String parentId, List<SysJobPositionVo> jobs){
        List<SysJobPositionVo> jobChilds = jobs.stream().filter(j -> parentId.equals(j.getParentId())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(jobChilds)){
            return new ArrayList<>();
        }
        for (SysJobPositionVo job : jobChilds){
            job.setJobChildrens(getJobChildrens(job.getId(), jobs));
        }
        return jobChilds;
    }
}
