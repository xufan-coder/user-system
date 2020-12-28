package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.bean.DataResult;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.enums.CompanyLoginStatusEnum;
import com.zerody.user.enums.DataRecordStatusEnum;
import com.zerody.user.enums.UserLoginStatusEnum;
import com.zerody.user.mapper.SysCompanyInfoMapper;
import com.zerody.user.mapper.SysLoginInfoMapper;
import com.zerody.user.mapper.SysStaffInfoMapper;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.pojo.SysCompanyInfo;
import com.zerody.user.pojo.SysLoginInfo;
import com.zerody.user.pojo.SysStaffInfo;
import com.zerody.user.pojo.SysUserInfo;
import com.zerody.user.service.SysCompanyInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.SysComapnyInfoVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
    * @Author               PengQiang
    * @Description //TODO   添加企业
    * @Date                 2020/12/19 13:06
    * @Param                [sysCompanyInfo]
    * @return               com.zerody.common.bean.DataResult
    */
    @Override
    public DataResult addCompany(SysCompanyInfo sysCompanyInfo) {
        log.info("B端添加企业入参--{}",sysCompanyInfo);
        //构造查询条件
        QueryWrapper<SysCompanyInfo> comQW = new QueryWrapper<>();
        comQW.lambda().ne(SysCompanyInfo::getStatus, DataRecordStatusEnum.DELETED);
        comQW.lambda().eq(SysCompanyInfo::getCompanyName, sysCompanyInfo.getCompanyName());
        //查询该企业是否存在
        Integer count = sysCompanyInfoMapper.selectCount(comQW);
        if(count > 0 ){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "企业名称已被占用", null);
        }
        //添加企业默认为该企业为有效状态
        sysCompanyInfo.setStatus(DataRecordStatusEnum.VALID.getCode());
        log.info("B端添加企业入库参数--{}", JSON.toJSONString(sysCompanyInfo));
        this.saveOrUpdate(sysCompanyInfo);
        return new DataResult(true,"添加成功",null);
    }

    /**
    * @Author               PengQiang
    * @Description //TODO   修改企业状态的同时修改该企业下的用户的登录状态
    * @Date                 2020/12/18 17:46
    * @Param                [companyId, loginStatus]
    * @return               com.zerody.common.bean.DataResult
    */
    @Override
    public DataResult updateCompanyStatus(String companyId, Integer loginStatus) {
        log.info("B端修改企业登录状态入参--companyId = {},loginStatus = {}",companyId,loginStatus);
        //判断企业id 与修改的状态 是否为空
        if(StringUtils.isBlank(companyId)){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "企业id不能为空", null);
        }
        if(loginStatus == null){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "状态不能为空", null);
        }
        //修改企业的状态
        SysCompanyInfo sysCompanyInfo = new SysCompanyInfo();
        sysCompanyInfo.setStatus(loginStatus);
        sysCompanyInfo.setId(companyId);
        //保存状态
        log.info("B端修改企业登录参数入库参数--{}",JSON.toJSONString(sysCompanyInfo));
        this.saveOrUpdate(sysCompanyInfo);
        //查询得到该企业下所有的用户id
        List<String> userIds = sysStaffInfoMapper.selectUserByCompanyId(companyId);
        //如果这个企业下没有员工那么久不用进行后面的操作直接返回成功
        if(userIds.size() == 0 ){
            return new DataResult(true, "操作成功", null);
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
        return new DataResult(true, "操作成功", null);
    }

    @Override
    public DataResult getPageCompany(SysCompanyInfoDto companyInfoDto) {
        //设置分页参数
        Integer pageNum = companyInfoDto.getPageNum() == 0 ? 1 : companyInfoDto.getPageNum();
        Integer pageSize =  companyInfoDto.getPageSize() == 0 ? 10 : companyInfoDto.getPageSize();
        IPage<SysComapnyInfoVo> iPage = new Page<>(pageNum,pageSize);
        iPage = sysCompanyInfoMapper.getPageCompany(companyInfoDto,iPage);
        return new DataResult(iPage);
    }

    @Override
    public DataResult updataCompany(SysCompanyInfo sysCompanyInfo) {
        this.saveOrUpdate(sysCompanyInfo);
        return new DataResult();
    }

    /**
    * @Author               PengQiang
    * @Description //TODO   逻辑删除企业
    * @Date                 2020/12/28 9:45
    * @Param                [companyId]
    * @return               com.zerody.common.bean.DataResult
    */
    @Override
    public DataResult deleteCompanyById(String companyId) {
        if (StringUtils.isEmpty(companyId)){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "企业id为空", null);
        }
        SysCompanyInfo company = new SysCompanyInfo();
        company.setStatus(DataRecordStatusEnum.DELETED.getCode());
        company.setId(companyId);
        this.saveOrUpdate(company);
        return new DataResult();
    }

    @Override
    public DataResult getAllCompany() {
        List<SysComapnyInfoVo> list = sysCompanyInfoMapper.getAllCompnay();
        return new DataResult(list);
    }


}
