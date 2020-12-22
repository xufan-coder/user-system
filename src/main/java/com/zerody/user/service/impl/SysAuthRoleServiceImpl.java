package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.bean.DataResult;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.common.util.UUIDutils;
import com.zerody.user.enums.DataRecordStatusEnum;
import com.zerody.user.mapper.SysAuthRoleInfoMapper;
import com.zerody.user.mapper.UnionRoleStaffMapper;
import com.zerody.user.pojo.SysAuthRoleInfo;
import com.zerody.user.pojo.UnionRoleStaff;
import com.zerody.user.service.SysAuthRoleInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.SysAuthRoleInfoVo;
import com.zerody.user.dto.SysAuthRolePageDto;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysAuthRoleServiceImpl
 * @DateTime 2020/12/17_18:20
 * @Deacription TODO
 */
@Service
@Slf4j
public class SysAuthRoleServiceImpl extends BaseService<SysAuthRoleInfoMapper, SysAuthRoleInfo> implements SysAuthRoleInfoService {


    @Autowired
    private UnionRoleStaffMapper unionRoleStaffMapper;

    @Autowired
    private SysAuthRoleInfoMapper sysAuthRoleInfoMapper;

    @Override
    public DataResult selectRolePage(SysAuthRolePageDto sysAuthRolePageDto) {

        //pageNum 设置当前页当 当前页码为0时(int类型默认值为0) 当前页码为1,pageSize设置当前页数据显示条数
        Integer pageNum = sysAuthRolePageDto.getPageNum() == 0 ? 1 :sysAuthRolePageDto.getPageNum();
        Integer pageSize = sysAuthRolePageDto.getPageSize()  == 0 ? 10 :sysAuthRolePageDto.getPageSize();
        IPage<SysAuthRoleInfoVo> iPage = new Page<>(pageNum, pageSize);

        iPage = sysAuthRoleInfoMapper.selectRolePage(sysAuthRolePageDto,iPage);
        return new DataResult(iPage);
    }

    @Override
    public DataResult addRole(SysAuthRoleInfo sysAuthRoleInfo) {
        QueryWrapper<SysAuthRoleInfo> roleQW = new QueryWrapper<>();
//        roleQW.lambda().eq(SysAuthRoleInfo::getCompId,)
        roleQW.lambda().eq(SysAuthRoleInfo::getRoleName, sysAuthRoleInfo.getRoleName());
        roleQW.lambda().ne(SysAuthRoleInfo::getStatus, DataRecordStatusEnum.DELETED);
        Integer count = sysAuthRoleInfoMapper.selectCount(roleQW);
        if (count > 0){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "角色名称已存在", null);
        }
        sysAuthRoleInfo.setStatus(DataRecordStatusEnum.VALID.getCode());
//        sysAuthRoleInfo.setCompId(); //企业id
        this.saveOrUpdate(sysAuthRoleInfo);
        return new DataResult();
    }

    @Override
    public DataResult updateRole(SysAuthRoleInfo sysAuthRoleInfo) {
        log.info("B端修改角色入参-{}", JSON.toJSONString(sysAuthRoleInfo));
        if(StringUtils.isEmpty(sysAuthRoleInfo.getId())){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "id不能为空", null);
        }
        QueryWrapper<SysAuthRoleInfo> roleQW = new QueryWrapper<>();
//        roleQW.lambda().eq(SysAuthRoleInfo::getCompId,)
        roleQW.lambda().eq(SysAuthRoleInfo::getRoleName, sysAuthRoleInfo.getRoleName());
        roleQW.lambda().ne(SysAuthRoleInfo::getStatus, DataRecordStatusEnum.DELETED);
        Integer count = sysAuthRoleInfoMapper.selectCount(roleQW);
        if (count > 0){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "角色名称已存在", null);
        }
        log.info("B端修改角色入库参数-{}", JSON.toJSONString(sysAuthRoleInfo));
        this.saveOrUpdate(sysAuthRoleInfo);
        return new DataResult();
    }

    @Override
    public DataResult deleteRoleById(String roleId) {
        //判断角色id是否为空
        if(StringUtils.isEmpty(roleId)){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false , "id不能为空", null);
        }
        //查看当前角色下有没有人
        QueryWrapper<UnionRoleStaff> rsQW = new QueryWrapper<>();
        rsQW.lambda().eq(UnionRoleStaff::getRoleId, roleId);
        Integer count = unionRoleStaffMapper.selectCount(rsQW);
        if(count > 0){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "角色下有用户不能删除", null);
        }
        SysAuthRoleInfo role = new SysAuthRoleInfo();
        role.setStatus(DataRecordStatusEnum.DELETED.getCode());
        role.setId(roleId);
        this.saveOrUpdate(role);
        return new DataResult("删除成功",null);
    }

    @Override
    public DataResult deleteBatchByIdds(List<String> roleIds) {
        SysAuthRoleInfo role = new SysAuthRoleInfo();
        for (String id : roleIds){
            role.setId(id);
            role.setStatus(DataRecordStatusEnum.DELETED.getCode());
            this.saveOrUpdate(role);
            role = new SysAuthRoleInfo();
        }
        return new DataResult();
    }

    @Override
    public DataResult deleteRoleDownStaff(String roleId, List<String> staffIds) {
        UnionRoleStaff unionRoleStaff;
        for (String staffid : staffIds){
            unionRoleStaff  = new UnionRoleStaff();
            unionRoleStaff.setId(UUIDutils.getUUID32());
            unionRoleStaff.setRoleId(roleId);
            unionRoleStaff.setStaffId(staffid);
            unionRoleStaffMapper.insert(unionRoleStaff);
        }
        return new DataResult();
    }

    @Override
    public DataResult selectRoleByStaffId(String staffId) {

        List<SysAuthRoleInfoVo> roles = sysAuthRoleInfoMapper.selectRolesByStaffId(staffId);
        return new DataResult(roles);
    }
}
