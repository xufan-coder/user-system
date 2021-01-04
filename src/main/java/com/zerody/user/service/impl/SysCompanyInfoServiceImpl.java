package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.CheckParamUtils;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.enums.DataRecordStatusEnum;
import com.zerody.user.mapper.SysCompanyInfoMapper;
import com.zerody.user.mapper.SysLoginInfoMapper;
import com.zerody.user.mapper.SysStaffInfoMapper;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.domain.SysCompanyInfo;
import com.zerody.user.domain.SysLoginInfo;
import com.zerody.user.service.SysCompanyInfoService;
import com.zerody.user.service.SysDepartmentInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.SysComapnyInfoVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysCompanyInfoServiceImpl
 * @DateTime 2020/12/18_15:52
 * @Deacription TODO
 */
@Slf4j
@Service
public class SysCompanyInfoServiceImpl extends BaseService<SysCompanyInfoMapper, SysCompanyInfo> implements SysCompanyInfoService {


    @Autowired
    private SysLoginInfoMapper sysLoginInfoMapper;

    @Autowired
    private SysStaffInfoMapper sysStaffInfoMapper;

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SysCompanyInfoMapper sysCompanyInfoMapper;

    @Autowired
    private SysDepartmentInfoService departmentInfoService;

    /**
    * @Author               PengQiang
    * @Description //TODO   添加企业
    * @Date                 2020/12/19 13:06
    * @Param                [sysCompanyInfo]
    * @return               com.zerody.common.bean.DataResult
    */
    @Override
    public void addCompany(SysCompanyInfo sysCompanyInfo) {
        log.info("B端添加企业入参--{}",sysCompanyInfo);
        //构造查询条件
        if(!CheckParamUtils.chkMobile(sysCompanyInfo.getContactPhone())){
            throw new DefaultException("企业联系人号码格式错误");
        }
        QueryWrapper<SysCompanyInfo> comQW = new QueryWrapper<>();
        comQW.lambda().ne(SysCompanyInfo::getStatus, DataRecordStatusEnum.DELETED);
        comQW.lambda().eq(SysCompanyInfo::getCompanyName, sysCompanyInfo.getCompanyName());
        //查询该企业是否存在
        Integer count = sysCompanyInfoMapper.selectCount(comQW);
        if(count > 0 ){
            throw new DefaultException("企业名称已被占用");
        }
        //添加企业默认为该企业为有效状态
        sysCompanyInfo.setStatus(DataRecordStatusEnum.VALID.getCode());
        log.info("B端添加企业入库参数--{}", JSON.toJSONString(sysCompanyInfo));
        this.saveOrUpdate(sysCompanyInfo);
    }

    /**
    * @Author               PengQiang
    * @Description //TODO   修改企业状态的同时修改该企业下的用户的登录状态
    * @Date                 2020/12/18 17:46
    * @Param                [companyId, loginStatus]
    * @return               com.zerody.common.bean.DataResult
    */
    @Override
    public void updateCompanyStatus(String companyId, Integer loginStatus) {
        log.info("B端修改企业登录状态入参--companyId = {},loginStatus = {}",companyId,loginStatus);
        //判断企业id 与修改的状态 是否为空
        if(StringUtils.isBlank(companyId)){
            throw new DefaultException("企业id不能为空");
        }
        if(loginStatus == null){
            throw new DefaultException("状态不能为空");
        }

        //修改企业的状态
        SysCompanyInfo sysCompanyInfo = new SysCompanyInfo();
        sysCompanyInfo.setLoginStatus(loginStatus);
        sysCompanyInfo.setId(companyId);
        //保存状态
        log.info("B端修改企业登录参数入库参数--{}",JSON.toJSONString(sysCompanyInfo));
        this.saveOrUpdate(sysCompanyInfo);
        //查询得到该企业下所有的用户id
        List<String> userIds = sysStaffInfoMapper.selectUserByCompanyId(companyId);
        //如果这个企业下没有员工那么久不用进行后面的操作直接返回成功
        if(userIds.size() == 0 ){
            return;
        }
        //设置修改参数
        SysLoginInfo sysLoginInfo = new SysLoginInfo();
        sysLoginInfo.setActiveFlag(loginStatus );
        //修改状态
        QueryWrapper<SysLoginInfo> loginQW = new QueryWrapper<>();
        loginQW.lambda().ne(SysLoginInfo::getActiveFlag, loginStatus);
        loginQW.lambda().in(SysLoginInfo::getUserId, userIds);
        log.info("B端修改修改企业登录状态后修改用户登录状态入库参数--{}",JSON.toJSONString(sysLoginInfo));
        sysLoginInfoMapper.update(sysLoginInfo, loginQW);
    }

    @Override
    public IPage<SysComapnyInfoVo> getPageCompany(SysCompanyInfoDto companyInfoDto) {
        //设置分页参数
        IPage<SysComapnyInfoVo> iPage = new Page<>(companyInfoDto.getCurrent(),companyInfoDto.getPageSize());
        return sysCompanyInfoMapper.getPageCompany(companyInfoDto,iPage);
    }

    @Override
    public void updataCompany(SysCompanyInfo sysCompanyInfo) {
        if(!CheckParamUtils.chkMobile(sysCompanyInfo.getContactPhone())){
            throw new DefaultException("企业联系人号码格式错误");
        }
        QueryWrapper<SysCompanyInfo> comQW = new QueryWrapper<>();
        comQW.lambda().ne(SysCompanyInfo::getStatus, DataRecordStatusEnum.DELETED);
        comQW.lambda().eq(SysCompanyInfo::getCompanyName, sysCompanyInfo.getCompanyName());
        comQW.lambda().ne(SysCompanyInfo::getId, sysCompanyInfo.getId());
        //查询该企业是否存在
        Integer count = sysCompanyInfoMapper.selectCount(comQW);
        if(count > 0 ){
            throw new DefaultException("企业名称已被占用");
        }
        this.saveOrUpdate(sysCompanyInfo);
    }

    /**
    * @Author               PengQiang
    * @Description //TODO   逻辑删除企业
    * @Date                 2020/12/28 9:45
    * @Param                companyId
    * @return               com.zerody.common.bean.DataResult
    */
    @Override
    public void deleteCompanyById(String companyId) {
        if (StringUtils.isEmpty(companyId)){
            throw new DefaultException("企业id为空");
        }
        SysCompanyInfo company = new SysCompanyInfo();
        company.setStatus(DataRecordStatusEnum.DELETED.getCode());
        company.setId(companyId);
        this.saveOrUpdate(company);
    }

    @Override
    public List<SysComapnyInfoVo> getAllCompany(String sysId) {

        List<SysComapnyInfoVo> companys = new ArrayList<>();
        if("aaa".equals(sysId)){
            SysComapnyInfoVo companyVo = new SysComapnyInfoVo();
//            companyVo.setCompanyName(UserUtils.get);
            return  companys;
        }

        companys = sysCompanyInfoMapper.getAllCompnay();
        for (SysComapnyInfoVo company : companys){
            company.setDeparts(departmentInfoService.getAllDepByCompanyId(company.getId()));
        }
       return companys;
    }

    @Override
    public SysComapnyInfoVo getCompanyInfoById(String id) {
        SysComapnyInfoVo companyInfo = sysCompanyInfoMapper.selectCompanyInfoById(id);
        return companyInfo;
    }


}
