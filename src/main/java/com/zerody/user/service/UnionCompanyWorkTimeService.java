package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.UnionCompanyWorkTime;
import com.zerody.user.dto.UnionCompanyWorkTimeDto;

import java.util.List;

/**
 * 业务层
 *
 * @author chenKeFeng
 * @date 2022-08-31 11:22:16
 */
public interface UnionCompanyWorkTimeService extends IService<UnionCompanyWorkTime> {

    /**
    * 分页查询列表
    *
    * @param unionCompanyWorkTime
    * @return
    */
    public List<UnionCompanyWorkTime> getUnionCompanyWorkTime(UnionCompanyWorkTime unionCompanyWorkTime);

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    public UnionCompanyWorkTime getUnionCompanyWorkTimeById(String id);

    /**
     * 新增
     *
     * @param unionCompanyWorkTimeDto
     * @return
     */
    public Integer addUnionCompanyWorkTime(UnionCompanyWorkTimeDto unionCompanyWorkTimeDto);

    /**
     * 修改
     *
     * @param unionCompanyWorkTime
     * @return
     */
    public Integer editUnionCompanyWorkTime(UnionCompanyWorkTime unionCompanyWorkTime);

    /**
    * @Author: chenKeFeng
    * @Description: 删除
    * @Date: 2022/8/31 17:19
    */
    public Integer deleteUnionCompanyWorkTime(String id);

}

