package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.bean.DataResult;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.common.util.ResultEnum;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.user.dto.SysAuthMenuPageDto;
import com.zerody.user.mapper.SysAuthMenuMapper;
import com.zerody.user.mapper.SysAuthRoleInfoMapper;
import com.zerody.user.mapper.SysStaffInfoMapper;
import com.zerody.user.pojo.SysAuthMenu;
import com.zerody.user.service.SysAuthMenuService;
import com.zerody.user.vo.SysAuthMenuVo;
import com.zerody.user.vo.SysAuthRoleInfoVo;
import com.zerody.user.vo.SysStaffInfoVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author PengQiang
 * @ClassName SysAuthMenuServiceImpl
 * @DateTime 2020/12/16_11:35
 * @Deacription TODO
 */
@Service
@Slf4j
public class SysAuthMenuServiceImpl implements SysAuthMenuService  {

    @Autowired
    private SysAuthMenuMapper sysAuthMenuMapper;

    @Autowired
    private SysStaffInfoMapper sysStaffInfoMapper;

    @Autowired
    private SysAuthRoleInfoMapper sysAuthRoleInfoMapper;

    @Override
    public DataResult getMenuCodeList(String compId) {
        String userId = UserUtils.getUserId();  //获取当前登录用户
        SysStaffInfoVo staffVo = sysStaffInfoMapper.selectByUserIdAndCompId(userId,compId);
        if(Objects.isNull(staffVo)){
            return  new DataResult(ResultCodeEnum.RESULT_ERROR, false, "没有该员工", null);
        }
        List<SysAuthRoleInfoVo> roles = sysAuthRoleInfoMapper.selectRolesByStaffId(staffVo.getId());
        if(roles.size()<1){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "没有角色", null);
        }
        // admin当前用户是否是超级管理员
        boolean admin = false;
        for (SysAuthRoleInfoVo role: roles
             ) {
            //查看有没有超级管理员权限
            if ("0".equals(role.getRoleType())){
                admin = !admin;
            }
        }
        //超级管理员拥有全部的菜单权限
        if(admin){
            return new DataResult(sysAuthMenuMapper.selectCodeAdmin());
        }
        return new DataResult(sysAuthMenuMapper.selectCodeByRoleId(roles));
    }

    /**
    * @Author               PengQiang
    * @Description //TODO    分页查询所有菜单信息
    * @Date                 2020/12/17 9:29
    * @Param                [sysAuthMenuPageDto] 查询条件与 当前页与当前页显示条数
    * @return               com.zerody.common.bean.DataResult
    */
    @Override
    public DataResult getMenuPage(SysAuthMenuPageDto sysAuthMenuPageDto) {
        Integer pageNum = sysAuthMenuPageDto.getPageNum() == 0 ? 1 : sysAuthMenuPageDto.getPageNum();
        Integer pageSize = sysAuthMenuPageDto.getPageSize() == 0 ? 1 : sysAuthMenuPageDto.getPageSize();
        //设置给定当前页与当前页显示数据条数
        IPage<SysAuthMenuVo> iPage = new Page<>(pageNum, pageSize);

        //使用条件构造器设置查询条件
        iPage = sysAuthMenuMapper.getMenuPage(sysAuthMenuPageDto,iPage);
        return new DataResult(iPage);
    }

    @Override
    public DataResult getMenuBySysId(String sysId, String roleId) {
        if(StringUtils.isEmpty(sysId)){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "系统id为空", null);
        }
        if(StringUtils.isEmpty(roleId)){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "角色id为空", null);
        }
        List<SysAuthMenuVo> menus = sysAuthMenuMapper.getMenuBySysId(sysId, roleId);
        //parentId为""时 为顶级菜单
        menus = getChildens("",menus);
        return new DataResult(menus);
    }

    //获取子级菜单
    private List<SysAuthMenuVo> getChildens(String parentId, List<SysAuthMenuVo> menus){
        List<SysAuthMenuVo> childs = menus.stream().filter(m -> parentId.equals(m.getParentId())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(childs)){
            return new ArrayList<>();
        }
        for (SysAuthMenuVo menu : childs){
            menu.setChildrens(getChildens(menu.getId(), menus));
        }
        return childs;
    }
}
