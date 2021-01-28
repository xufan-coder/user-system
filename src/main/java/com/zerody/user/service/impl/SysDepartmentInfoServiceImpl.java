package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.AdminVo;
import com.zerody.user.domain.SysCompanyInfo;
import com.zerody.user.domain.SysDepartmentInfo;
import com.zerody.user.domain.SysJobPosition;
import com.zerody.user.domain.UnionStaffDepart;
import com.zerody.user.domain.base.BaseModel;
import com.zerody.user.dto.SetAdminAccountDto;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.mapper.SysDepartmentInfoMapper;
import com.zerody.user.mapper.SysJobPositionMapper;
import com.zerody.user.mapper.UnionStaffDepartMapper;
import com.zerody.user.service.SysDepartmentInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.SysDepartmentInfoVo;
import com.zerody.user.vo.SysJobPositionVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private SysStaffInfoService staffInfoService;

    @Autowired
    private UnionStaffDepartMapper unionStaffDepartMapper;

    @Override
    public void addDepartment(SysDepartmentInfo sysDepartmentInfo) {
        log.info("B端添加部门入参-{}",sysDepartmentInfo);
        if(!StringUtils.isEmpty(sysDepartmentInfo.getParentId())){
            String compId = this.sysDepartmentInfoMapper.selectById(sysDepartmentInfo.getParentId()).getCompId();
            sysDepartmentInfo.setCompId(compId);
        }
        if(StringUtils.isEmpty(sysDepartmentInfo.getCompId())){
            throw new DefaultException("非顶级部门时企业id不能为空");
        }
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


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = calendar.get(Calendar.MONTH) + 1 < 10 ? "0" : "";
        month = month.concat(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        String date = String.valueOf(calendar.get(Calendar.DATE));
        String id  = year.concat(month).concat(date).concat("xxxx");
        if (StringUtils.isNotEmpty(sysDepartmentInfo.getParentId())){
            id = sysDepartmentInfo.getParentId().concat(id);
        }
        boolean find = true;
        do {
            if (find) {
                Random random = new Random();
                id = id.substring(0, id.length() - 4).concat(String.valueOf(random.nextInt(9000)+1000));
            }
            find = DataUtil.isNotEmpty(this.sysDepartmentInfoMapper.selectById(id));
        } while (find);
        sysDepartmentInfo.setId(id);
        //名称不存在 保存添加
        this.save(sysDepartmentInfo);
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
        SysDepartmentInfo dep = this.getById(depId);
        List<SysDepartmentInfoVo> deps = sysDepartmentInfoMapper.getAllDepByCompanyId(dep.getCompId());
        List<String> list = this.getDepChildernsIds(depId, deps);
        list.add(0, depId);
        unQw = new QueryWrapper<>();
        unQw.lambda().like(UnionStaffDepart::getDepartmentId, depId);
        count = unionStaffDepartMapper.selectCount(unQw);
        if(count > 0){
            throw new DefaultException("该子级部门下有员工不可删除!");
        }
        //删除当前部门以及子级部门
        UpdateWrapper<SysDepartmentInfo> depUw = new UpdateWrapper<>();
        depUw.lambda().set(SysDepartmentInfo::getStatus, StatusEnum.删除.getValue());
        depUw.lambda().like(SysDepartmentInfo::getId, depId);
        this.update(depUw);
        //删除部门下的岗位
        QueryWrapper<SysJobPosition> jobQw = new QueryWrapper<>();
        jobQw.lambda().like(SysJobPosition::getDepartId, depId);
        jobQw.lambda().ne(SysJobPosition::getStatus, StatusEnum.删除.getValue());
        SysJobPosition job = new SysJobPosition();
        job.setStatus(StatusEnum.删除.getValue());
        this.sysJobPositionMapper.update(job, jobQw);
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
        UpdateWrapper<SysDepartmentInfo> depUw = new UpdateWrapper<>();
        depUw.lambda().set(SysDepartmentInfo::getAdminAccount, dto.getStaffId());
        depUw.lambda().eq(SysDepartmentInfo::getId, dto.getId());
        this.update(depUw);
        QueryWrapper<UnionStaffDepart> staffDepQw = new QueryWrapper<>();
        staffDepQw.lambda().eq(UnionStaffDepart::getDepartmentId, dto.getId());
        staffDepQw.lambda().eq(UnionStaffDepart::getStaffId, dto.getStaffId());
        unionStaffDepartMapper.delete(staffDepQw);
        UnionStaffDepart unSd = new UnionStaffDepart();
        unSd.setDepartmentId(dto.getId());
        unSd.setStaffId(dto.getStaffId());
        unionStaffDepartMapper.insert(unSd);
    }

    @Override
    public List<SysDepartmentInfoVo> getSubordinateStructure(UserVo user) {
        AdminVo admin = this.staffInfoService.getIsAdmin(user);
        List<SysDepartmentInfoVo> deps = null;
        String parentId = null;
        if(admin.getIsCompanyAdmin()) {
            deps = this.sysDepartmentInfoMapper.getAllDepByCompanyId(user.getCompanyId());
        } else if (admin.getIsDepartAdmin()) {
            deps =  this.sysDepartmentInfoMapper.getSubordinateStructure(user);
            parentId = this.getById(user.getDeptId()).getParentId();
        } else {
            return null;
        }
        return this.getDepChildrens(parentId, deps);
    }

    private List<SysDepartmentInfoVo> getDepChildrens(String parentId, List<SysDepartmentInfoVo> deps){
        return this.getDepChildrens(parentId, deps, null);
    }
    /**
     *
     *
     * @author               PengQiang
     * @description          返回树形结构
     * @date                 2021/1/21 9:48
     * @param                [parentId, deps, jobs]
     * @return               java.util.List<com.zerody.user.vo.SysDepartmentInfoVo>
     */
    private List<SysDepartmentInfoVo> getDepChildrens(String parentId, List<SysDepartmentInfoVo> deps, List<SysJobPositionVo> jobs){
        List<SysDepartmentInfoVo> childs = deps.stream().filter(d -> StringUtils.isEmpty(parentId) ? StringUtils.isEmpty(d.getParentId()) : parentId.equals(d.getParentId())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(childs)){
            return new ArrayList<>();
        }
        for (SysDepartmentInfoVo dep : childs){
            //部门下的岗位
            if(!CollectionUtils.isEmpty(jobs)){
                List<SysJobPositionVo> jobChilds = jobs.stream().filter(j -> dep.getId().equals(j.getDepartId())).collect(Collectors.toList());
                dep.setJobChildrens(jobChilds);
            }
            dep.setDepartChildrens(getDepChildrens(dep.getId(), deps, jobs));
        }
        return childs;
    }


    /**
     *
     *
     * @author               PengQiang
     * @description          获取子级部门
     * @date                 2021/1/21 17:40
     * @param                [parentId, deps]
     * @return               java.util.List<java.lang.String>
     */
    private List<String> getDepChildernsIds(String parentId, List<SysDepartmentInfoVo> deps){
       return this.getDepChildernsIds(parentId, deps, new ArrayList<>());
    }
    private List<String> getDepChildernsIds(String parentId, List<SysDepartmentInfoVo> deps, List<String> depChilderIds){
        List<SysDepartmentInfoVo> childs = deps.stream().filter(d -> StringUtils.isEmpty(parentId) ? StringUtils.isEmpty(d.getParentId()) : parentId.equals(d.getParentId())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(childs)){
            return new ArrayList<>();
        }
        for (SysDepartmentInfoVo dep : childs){
            depChilderIds.add(dep.getId());
            depChilderIds.addAll(getDepChildernsIds(dep.getId(), deps, new ArrayList<>()));
        }
        return depChilderIds;
    }

//    /**
//     *
//     *
//     * @author               PengQiang
//     * @description          获取子级岗位
//     * @date                 2021/1/21 9:50
//     * @param                [parentId, jobs]
//     * @return               java.util.List<com.zerody.user.vo.SysJobPositionVo>
//     */
//    private List<SysJobPositionVo> getJobChildrens(String parentId, List<SysJobPositionVo> jobs){
//        List<SysJobPositionVo> jobChilds = jobs.stream().filter(j -> parentId.equals(j.getParentId())).collect(Collectors.toList());
//        if(CollectionUtils.isEmpty(jobChilds)){
//            return new ArrayList<>();
//        }
//        for (SysJobPositionVo job : jobChilds){
//            job.setJobChildrens(getJobChildrens(job.getId(), jobs));
//        }
//        return jobChilds;
//    }
}
