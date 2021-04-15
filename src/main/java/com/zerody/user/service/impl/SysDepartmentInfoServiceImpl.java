package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.MQ;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.mq.RabbitMqService;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.RedisUtils;
import com.zerody.common.vo.UserVo;
import com.zerody.customer.api.dto.SetUserDepartDto;
import com.zerody.customer.api.service.ClewRemoteService;
import com.zerody.user.api.dto.DeptInfo;
import com.zerody.user.api.vo.AdminVo;
import com.zerody.user.api.vo.UserDepartInfoVo;
import com.zerody.user.domain.*;
import com.zerody.user.domain.base.BaseModel;
import com.zerody.user.dto.SetAdminAccountDto;
import com.zerody.user.mapper.*;
import com.zerody.user.service.SysDepartmentInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.SysDepartmentInfoVo;
import com.zerody.user.vo.SysJobPositionVo;
import com.zerody.user.vo.UserStructureVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private SysStaffInfoMapper stafffMapper;

    @Autowired
    private UnionStaffDepartMapper unionStaffDepartMapper;

    @Autowired
    private ClewRemoteService clewService;

    @Autowired
    private SysCompanyInfoMapper companyMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RabbitMqService mqService;

    // todo 添加部门redis 自动增长key
    private final static String ADD_DEPART_REIDS_KEY = "dept:increase";

    // todo 添加部门redis 自动增长 过期时间 单位天
    private final static Long ADD_DEPART_REDIS_OUTTIME = 2L;

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
        depQW.lambda().ne(SysDepartmentInfo::getStatus, StatusEnum.deleted.getValue());
        depQW.lambda().eq(SysDepartmentInfo::getDepartName, sysDepartmentInfo.getDepartName());
        depQW.lambda().eq(SysDepartmentInfo::getCompId, sysDepartmentInfo.getCompId());
        Integer count = sysDepartmentInfoMapper.selectCount(depQW);
        if(count > 0){
            throw new DefaultException("该部门名称已存在!");
        }
        sysDepartmentInfo.setStatus(StatusEnum.activity.getValue());
        log.info("B端添加部门入库-{}",sysDepartmentInfo);
        SimpleDateFormat simdf = new SimpleDateFormat("yyMMdd");
        String id  = simdf.format(new Date());
        boolean find = true;
        do {
            if (find) {
                // todo 获取redis 自增长
                long departDayCount = redisUtils.increase(ADD_DEPART_REIDS_KEY, 0, ADD_DEPART_REDIS_OUTTIME, TimeUnit.DAYS);
                id = id.concat(String.valueOf(departDayCount));
                // todo 如果是子部门 把改部门的父级部门id拼在前面
                if (StringUtils.isNotEmpty(sysDepartmentInfo.getParentId())){
                    id = sysDepartmentInfo.getParentId().concat("_").concat(id);
                }
            }
            // todo 判断改id数据库是否存在
            find = DataUtil.isNotEmpty(this.sysDepartmentInfoMapper.selectById(id));
        } while (find);
        sysDepartmentInfo.setId(id);
        // TODO: 2021/4/15 添加部门修改状态为 没有修改名称
        sysDepartmentInfo.setIsUpdateName(YesNo.NO);
        //名称不存在 保存添加
        this.save(sysDepartmentInfo);
    }
    @Override
    public void updateDepartment(SysDepartmentInfo sysDepartmentInfo) {
        log.info("B端添加部门入参-{}",sysDepartmentInfo);
        // TODO 查看部门名称是否存在
        QueryWrapper<SysDepartmentInfo> depQW =  new QueryWrapper<>();
        depQW.lambda().ne(SysDepartmentInfo::getStatus,StatusEnum.deleted.getValue());
        depQW.lambda().eq(SysDepartmentInfo::getDepartName, sysDepartmentInfo.getDepartName());
        depQW.lambda().ne(SysDepartmentInfo::getId, sysDepartmentInfo.getId());
        Integer count = sysDepartmentInfoMapper.selectCount(depQW);
        if(count > 0){
              throw new DefaultException("该部门名称已存在!");
        }
        SysDepartmentInfo departInfo = this.getById(sysDepartmentInfo.getId());
        // TODO: 2021/4/15 查询部门名称是否有修改 有修改 mq发送消息修改冗余的部门名称
        if (!departInfo.getDepartName().equals(sysDepartmentInfo.getDepartName())) {
            // TODO: 2021/4/15 设置修改名称状态为已修改
            sysDepartmentInfo.setIsUpdateName(YesNo.YES);
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
        depUw.lambda().set(SysDepartmentInfo::getStatus, StatusEnum.deleted.getValue());
        depUw.lambda().like(SysDepartmentInfo::getId, depId);
        this.update(depUw);
        //删除部门下的岗位
        QueryWrapper<SysJobPosition> jobQw = new QueryWrapper<>();
        jobQw.lambda().like(SysJobPosition::getDepartId, depId);
        jobQw.lambda().ne(SysJobPosition::getStatus, StatusEnum.deleted.getValue());
        SysJobPosition job = new SysJobPosition();
        job.setStatus(StatusEnum.deleted.getValue());
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
        qw.lambda().ne(BaseModel::getStatus,StatusEnum.deleted.getValue());
        return sysDepartmentInfoMapper.selectList(qw);
    }

    @Override
    public SysDepartmentInfo getByName(String name,String compId) {
        QueryWrapper<SysDepartmentInfo> qw =new QueryWrapper<>();
        qw.lambda().eq(SysDepartmentInfo::getCompId,compId);
        qw.lambda().eq(SysDepartmentInfo::getDepartName,name);
        qw.lambda().ne(BaseModel::getStatus,StatusEnum.deleted.getValue());
        return sysDepartmentInfoMapper.selectOne(qw);
    }

    @Override
    public void updateAdminAccout(SetAdminAccountDto dto) {
        if(StringUtils.isEmpty(dto.getId())){
            throw new DefaultException("部门id为空");
        }
        if(StringUtils.isEmpty(dto.getStaffId())){
            throw new DefaultException("员工id为空");
        }
        QueryWrapper<UnionStaffDepart> usdQW = new QueryWrapper<>();
        usdQW.lambda().eq(UnionStaffDepart::getStaffId, dto.getStaffId());
        UnionStaffDepart dep  = this.unionStaffDepartMapper.selectOne(usdQW);
        if (!dto.getId().equals(dep.getDepartmentId())){
            SetUserDepartDto userDepart = new SetUserDepartDto();
            userDepart.setDepartId(dto.getId());
            SysStaffInfo staffInfo = this.stafffMapper.selectById(dto.getStaffId());
            userDepart.setUserId(staffInfo.getUserId());
            DataResult r = clewService.updateCustomerAndClewDepartIdByUser(userDepart);
            if (!r.isSuccess()){
                throw new DefaultException("修改线索负责人失败");
            }
        }
        UpdateWrapper<SysDepartmentInfo> depUw = new UpdateWrapper<>();
        depUw.lambda().set(SysDepartmentInfo::getAdminAccount, dto.getStaffId());
        depUw.lambda().eq(SysDepartmentInfo::getId, dto.getId());
        this.update(depUw);
        QueryWrapper<UnionStaffDepart> staffDepQw = new QueryWrapper<>();
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

    @Override
    public List<UserDepartInfoVo> getSubordinateDirectlyDepart(String departId) {
        return this.sysDepartmentInfoMapper.getSubordinateDirectlyDepart(departId);
    }

    @Override
    public List<UserStructureVo> getDirectLyDepartOrUser(String companyId, String departId,  UserVo user) {
        List<UserStructureVo> userStructureVos = userStructureVos = new ArrayList<>();
        if (!DataUtil.isEmpty(user)) {
            AdminVo admin =  this.staffInfoService.getIsAdmin(UserUtils.getUser());
            if (admin.getIsCompanyAdmin()) {
                //如果企业id并且 部门id为空 并且 是企业管理员 则返回企业名称跟id 类型为1
                UserStructureVo companyInfo =  this.companyMapper.getCompanyNameById(user.getCompanyId());
                userStructureVos.add(companyInfo);
            } else if (admin.getIsDepartAdmin()) {
                //如果企业id并且 部门id为空 并且 是部门管理员 则返回部门名称跟id 类型为2
                UserStructureVo departInfo = this.sysDepartmentInfoMapper.getDepartNameById(user.getDeptId());
                userStructureVos.add(departInfo);
            } else {
                return null;
            }
            return userStructureVos;
        }
        if (StringUtils.isNotEmpty(departId)) {
            userStructureVos = this.stafffMapper.getUserNameByDepartId(departId, UserUtils.getUser().getUserId());
        }
        List<UserStructureVo> departInfos = this.sysDepartmentInfoMapper.getDepartNameByCompanyIdOrParentId(companyId, departId);
        userStructureVos.addAll(departInfos);
        return userStructureVos;
    }

    @Override
    public com.zerody.user.api.vo.SysUserInfo getChargeUser(String departId) {
        SysDepartmentInfo sysDepartmentInfo = this.sysDepartmentInfoMapper.selectById(departId);
        if(DataUtil.isNotEmpty(sysDepartmentInfo)){
            String adminAccount = sysDepartmentInfo.getAdminAccount();
            if(DataUtil.isNotEmpty(adminAccount)){
                SysUserInfo userById = sysUserInfoService.getUserById(adminAccount);
                if(DataUtil.isNotEmpty(userById)){
                    com.zerody.user.api.vo.SysUserInfo result=new com.zerody.user.api.vo.SysUserInfo();
                    BeanUtils.copyProperties(userById,result);
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    public List<UserDepartInfoVo> getJurisdictionDirectly(String userId) {
        List<UserDepartInfoVo> departVos = new ArrayList<>();
        UserVo userVo = new UserVo();
        userVo.setUserId(userId);
        String companyId = this.stafffMapper.getCompanyIdByUserId(userId);
        userVo.setCompanyId(companyId);
        //获取用户最高权限
        AdminVo adminVo = this.staffInfoService.getIsAdmin(userVo);
        if(adminVo.getIsCompanyAdmin()) {
            List<UserStructureVo> departInfos = this.sysDepartmentInfoMapper.getDepartNameByCompanyIdOrParentId(companyId, null);
            for (UserStructureVo vo : departInfos) {
                departVos.add(new UserDepartInfoVo());
                BeanUtils.copyProperties(vo, departVos.get(departVos.size() - 1));
            }
        } else if (adminVo.getIsDepartAdmin()){
            String departId = this.staffInfoService.getDepartId(userId);
            departVos = this.sysDepartmentInfoMapper.getSubordinateDirectlyDepart(departId);
        } else {
            return null;
        }
        return departVos;
    }

    @Override
    public Integer getDepartType(String departId) {
        // todo 默认为部门
        Integer deaprtType = 1;
        QueryWrapper<SysDepartmentInfo> departQw = new QueryWrapper<>();
        departQw.lambda().eq(SysDepartmentInfo::getParentId, departId);
        departQw.lambda().eq(SysDepartmentInfo::getStatus, StatusEnum.activity.getValue());
        Integer count = this.count(departQw);
        // todo 如果没有下级部门 则为团队部门
        if (count.equals(0)) {
            deaprtType = 0;
        }
        return deaprtType;
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


    @Override
    public void updateRedundancyDepartName() {
        // TODO: 2021/4/15 获取到名称修改状态 为修改的部门 
        List<DeptInfo> depts = this.sysDepartmentInfoMapper.getModilyDepartName();
        // TODO: 2021/4/15 如果没有修改名称的部门 不做后面的操作
        if (CollectionUtils.isEmpty(depts)) {
            return;
        }
        // TODO: 2021/4/15 把部门名称修改状态修改回 未修改状态
        this.sysDepartmentInfoMapper.updateDepartIsUpdateName(depts);
        depts.stream().forEach(dep -> {
            // TODO: 2021/4/15 发送修改部门名称通知
            mqService.send(dep, MQ.QUEUE_DEPT_NAME);
        });
        log.info("发送部门名称修改通知:{}", JSON.toJSONString(depts));
    }

}
