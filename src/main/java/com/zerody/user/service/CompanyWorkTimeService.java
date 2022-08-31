package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.CompanyWorkTime;
import com.zerody.user.dto.CompanyWorkTimeDto;
import com.zerody.user.vo.CompanyWorkTimeVo;

import java.util.List;

/**
 * 公司上下班时间表业务层
 *
 * @author chenKeFeng
 * @date 2022-08-29 11:59:15
 */
public interface CompanyWorkTimeService extends IService<CompanyWorkTime> {

    /**
    * 分页查询企业上下班时间
    *
    * @param companyWorkTimeDto
    * @return
    */
    public IPage<CompanyWorkTimeVo> getPageCompanyWorkTime(CompanyWorkTimeDto companyWorkTimeDto);

    /**
    * @Author: chenKeFeng
    * @Description: 获取上下班时间
    * @Date: 2022/8/29 14:08
    */
    public List<CompanyWorkTimeVo> getCompanyWorkTimeList(CompanyWorkTimeDto companyWorkTimeDto);

    /**
     * 获取详情
     *
     * @param companyWorkTimeDto
     * @return
     */
    public CompanyWorkTime getCompanyWorkTimeById(CompanyWorkTimeDto companyWorkTimeDto);

    /**
     * 新增
     *
     * @param companyWorkTimeDto
     * @return
     */
    public Integer addCompanyWorkTime(CompanyWorkTimeDto companyWorkTimeDto);

    /**
     * 修改
     *
     * @param companyWorkTimeDto
     * @return
     */
    public Integer editCompanyWorkTime(CompanyWorkTimeDto companyWorkTimeDto);
    
    /**
    * @Author: chenKeFeng
    * @Description: 设置企业上下班时间
    * @Date: 2022/8/31 8:40
    */
    public Integer setCommuteTime(CompanyWorkTimeDto companyWorkTimeDto);

}

