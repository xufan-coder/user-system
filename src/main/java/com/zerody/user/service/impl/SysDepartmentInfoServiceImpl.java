package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.bean.DataResult;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.user.dto.SysDepartmentInfoDto;
import com.zerody.user.enums.DataRecordStatusEnum;
import com.zerody.user.mapper.SysDepartmentInfoMapper;
import com.zerody.user.mapper.SysJobPositionMapper;
import com.zerody.user.mapper.SysLoginInfoMapper;
import com.zerody.user.mapper.UnionStaffDepartMapper;
import com.zerody.user.pojo.SysDepartmentInfo;
import com.zerody.user.pojo.SysLoginInfo;
import com.zerody.user.pojo.UnionStaffDepart;
import com.zerody.user.service.SysDepartmentInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.SysComapnyInfoVo;
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
    private SysLoginInfoMapper sysLoginInfoMapper;

    @Autowired
    private UnionStaffDepartMapper unionStaffDepartMapper;

    @Override
    public DataResult getPageDepartment(SysDepartmentInfoDto sysDepartmentInfoDto) {
        //设置分页参数
        Integer pageNum = sysDepartmentInfoDto.getPageNum() == 0 ? 1 : sysDepartmentInfoDto.getPageNum();
        Integer pageSize =  sysDepartmentInfoDto.getPageSize() == 0 ? 10 : sysDepartmentInfoDto.getPageSize();
        IPage<SysDepartmentInfoVo> iPage = new Page<>(pageNum,pageSize);
//        sysDepartmentInfoDto.setCompanyId();
        iPage = sysDepartmentInfoMapper.getPageDepartment(sysDepartmentInfoDto,iPage);
        return new DataResult(iPage);
    }

    @Override
    public DataResult updateDepartmentStatus(String depId, Integer loginStauts) {
        log.info("修改部门登录状态 B端入参-depId:{} 、 loginStats:{}",depId,loginStauts);
        if(StringUtils.isEmpty(depId)){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "部门id为空", null);
        }
        if(loginStauts == null){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "状态为空", null);
        }
        SysDepartmentInfo sysDepartmentInfo = new SysDepartmentInfo();
        sysDepartmentInfo.setLoginStatus(loginStauts);
        sysDepartmentInfo.setId(depId);
        log.info("B端修改部门登录状态数据库入参--{}",sysDepartmentInfo);
        this.saveOrUpdate(sysDepartmentInfo);
        //查询得到该部门下所有的用户登录id
        List<String> ids = sysDepartmentInfoMapper.selectUserLoginIdByDepId(depId);
        //当部门下没有人就不执行以下操作
        if(ids.size() == 0){
            return new DataResult(true, "操作成功!",null);
        }
        SysLoginInfo login = new SysLoginInfo();
        login.setActiveFlag(loginStauts);
        QueryWrapper<SysLoginInfo> loginQW = new QueryWrapper<>();
        loginQW.lambda().ne(SysLoginInfo::getActiveFlag, loginStauts);
        loginQW.lambda().in(SysLoginInfo::getUserId, ids);
        log.info("B端修改部门下用户登录状态数据库入参--{}",sysDepartmentInfo);
        sysLoginInfoMapper.update(login,loginQW);
        return new DataResult(true, "操作成功!",null);
    }

    @Override
    public DataResult addDepartment(SysDepartmentInfo sysDepartmentInfo) {
        log.info("B端添加部门入参-{}",sysDepartmentInfo);
        //查看部门名称是否存在
        QueryWrapper<SysDepartmentInfo> depQW =  new QueryWrapper<>();
        depQW.lambda().ne(SysDepartmentInfo::getStatus, DataRecordStatusEnum.DELETED.getCode());
        depQW.lambda().eq(SysDepartmentInfo::getDepartName, sysDepartmentInfo.getDepartName());
        Integer count = sysDepartmentInfoMapper.selectCount(depQW);
        if(count > 0){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "该部门名称已存在", null);
        }
        sysDepartmentInfo.setStatus(DataRecordStatusEnum.VALID.getCode());
        log.info("B端添加部门入库-{}",sysDepartmentInfo);
        //名称不存在 保存添加
        this.saveOrUpdate(sysDepartmentInfo);
        return new DataResult("添加成功", null);
    }

    @Override
    public DataResult updateDepartment(SysDepartmentInfo sysDepartmentInfo) {
        log.info("B端添加部门入参-{}",sysDepartmentInfo);
        //查看部门名称是否存在
        QueryWrapper<SysDepartmentInfo> depQW =  new QueryWrapper<>();
        depQW.lambda().ne(SysDepartmentInfo::getStatus, DataRecordStatusEnum.DELETED.getCode());
        depQW.lambda().eq(SysDepartmentInfo::getDepartName, sysDepartmentInfo.getDepartName());
        depQW.lambda().ne(SysDepartmentInfo::getId, sysDepartmentInfo.getId());
        Integer count = sysDepartmentInfoMapper.selectCount(depQW);
        if(count > 0){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "该部门名称已存在", null);
        }
        log.info("B端修改部门入库-{}",sysDepartmentInfo);
        //名称不存在 保存添加
        this.saveOrUpdate(sysDepartmentInfo);
        return new DataResult("修改成功",null);
    }

    @Override
    public DataResult deleteDepartmentById(String depId) {
        QueryWrapper<UnionStaffDepart> unQw = new QueryWrapper<>();
        unQw.lambda().eq(UnionStaffDepart::getDepartmentId, depId);
        Integer count = unionStaffDepartMapper.selectCount(unQw);
        if (count > 0){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false,  "该部门下用员工不能删除", null);
        }
        //逻辑删除部门
        SysDepartmentInfo dep = new SysDepartmentInfo();
        dep.setStatus(DataRecordStatusEnum.DELETED.getCode());
        dep.setId(depId);
        this.saveOrUpdate(dep);
        return new DataResult(true, "删除成功", null);
    }

    @Override
    public DataResult getAllDepartment() {
        List<SysDepartmentInfoVo> deps = sysDepartmentInfoMapper.getAllDep();
        List<SysJobPositionVo> jobs = sysJobPositionMapper.getAllJob();
        deps = getDepChildrens("", deps, jobs);
        return new DataResult(deps);
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
