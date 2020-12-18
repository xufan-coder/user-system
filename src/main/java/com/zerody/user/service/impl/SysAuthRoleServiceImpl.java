package com.zerody.user.service.impl;

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
        IPage<SysAuthRoleInfo> iPage = new Page<>(pageNum, pageSize);

        //查询条件设置
        QueryWrapper<SysAuthRoleInfo> roleQW = new QueryWrapper<>();
        roleQW.lambda().eq(SysAuthRoleInfo::getStatus, DataRecordStatusEnum.VALID.getCode());

        iPage = this.page(iPage,roleQW);
        return new DataResult(iPage);
    }

    @Override
    public DataResult addRole(SysAuthRoleInfo sysAuthRoleInfo) {
//        sysAuthRoleInfo.setCompId(); //企业id
        this.saveOrUpdate(sysAuthRoleInfo);
        return new DataResult();
    }

    @Override
    public DataResult updateRole(SysAuthRoleInfo sysAuthRoleInfo) {
        if(StringUtils.isEmpty(sysAuthRoleInfo.getId())){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "id不能为空", null);
        }
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
        this.removeById(roleId);
        return new DataResult("删除成功",null);
    }

    @Override
    public DataResult deleteBatchByIdds(List<String> roleIds) {
        this.removeByIds(roleIds);
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
