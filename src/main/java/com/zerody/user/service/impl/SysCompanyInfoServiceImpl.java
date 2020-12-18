package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.bean.DataResult;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.enums.CompanyLoginStatusEnum;
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
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysCompanyInfoServiceImpl
 * @DateTime 2020/12/18_15:52
 * @Deacription TODO
 */
@Service
public class SysCompanyInfoServiceImpl extends BaseService<SysCompanyInfoMapper, SysCompanyInfo> implements SysCompanyInfoService {


    @Autowired
    private SysLoginInfoMapper sysLoginInfoMapper;

    @Autowired
    private SysStaffInfoMapper sysStaffInfoMapper;

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Override
    public DataResult addCompany(SysCompanyInfo sysCompanyInfo) {
        this.saveOrUpdate(sysCompanyInfo);
        return new DataResult();
    }

    /**
    * @Author               PengQiang
    * @Description //TODO   修改企业状态的同时修改该企业下的用户的登录状态
    * @Date                 2020/12/18 17:46
    * @Param                [companyId, loginStatus]
    * @return               com.zerody.common.bean.DataResult
    */
    @Override
    public DataResult updateComanyStatus(String companyId, Integer loginStatus) {
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
        this.saveOrUpdate(sysCompanyInfo);
        //查询得到该企业下所有的用户id
        List<Integer> userIds = sysStaffInfoMapper.selectUserByCompanyId(companyId);
        //当企业状态修改为禁用的时候 登录用户也改为禁用
        loginStatus = CompanyLoginStatusEnum.BLOCK_UP.getCode() == loginStatus ?
                UserLoginStatusEnum.COMPANY_DEPARTMENT_BLOCK_UP.getCode() :
                UserLoginStatusEnum.ENABLE.getCode();
        //设置修改参数
        SysLoginInfo sysLoginInfo = new SysLoginInfo();
        sysLoginInfo.setActiveFlag(loginStatus );
        //修改状态
        QueryWrapper<SysLoginInfo> loginQW = new QueryWrapper<>();
        loginQW.lambda().ne(SysLoginInfo::getActiveFlag, loginStatus);
        loginQW.lambda().in(SysLoginInfo::getUserId,userIds);
        sysLoginInfoMapper.update(sysLoginInfo, loginQW);
        return new DataResult("操作成功", null);
    }

    @Override
    public DataResult getPageCompany(SysCompanyInfoDto companyInfoDto) {
        //设置分页参数
        Integer pageNum = companyInfoDto.getPageNum() == 0 ? 1 : companyInfoDto.getPageNum();
        Integer pageSize =  companyInfoDto.getPageSize() == 0 ? 10 : companyInfoDto.getPageSize();
        IPage<SysCompanyInfo> iPage = new Page<>(pageNum,pageSize);
        QueryWrapper<SysCompanyInfo> companyQW = new QueryWrapper<>();
        iPage = this.page(iPage,companyQW);
        return new DataResult();
    }


}
