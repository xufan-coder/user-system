package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.CompanyWeek;

import java.util.List;

/**
 * 业务层
 *
 * @author chenKeFeng
 * @date 2022-08-31 11:22:16
 */
public interface CompanyWeekService extends IService<CompanyWeek> {

    /**
    * 分页查询列表
    *
    * @param companyWeek
    * @return
    */
    public List<CompanyWeek> getPageCompanyWeek(CompanyWeek companyWeek);

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    public CompanyWeek getCompanyWeekById(String id);

    /**
     * 新增
     *
     * @param companyWeek
     * @return
     */
    public Integer addCompanyWeek(CompanyWeek companyWeek);

    /**
     * 修改
     *
     * @param companyWeek
     * @return
     */
    public Integer updateCompanyWeek(CompanyWeek companyWeek);
    
    /**
    * @Author: chenKeFeng
    * @Description: 删除
    * @Date: 2022/8/31 17:25
    */
    public Integer deleteCompanyWeek(String id);

}

