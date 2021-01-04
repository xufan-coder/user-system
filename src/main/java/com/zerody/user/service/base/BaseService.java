package com.zerody.user.service.base;


import com.alibaba.nacos.common.utils.Objects;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.util.UserUtils;
import com.zerody.user.domain.base.BaseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @Description: TODO
 * @auther: fumin
 * @date: 2019/3/19
 */

public abstract class BaseService<M extends BaseMapper<T>, T extends BaseModel> extends ServiceImpl<BaseMapper<T>, T> {

	@Autowired
	protected BaseMapper<T> mapper;

	/**
	 * 分页辅助类
	 * 
	 * @author zcm
	 * @param params
	 * @return
	 */
	public Page<T> getPageHelper(Map<String, Object> params) {
		Integer current = 1;
		Integer size = 10;
		if (Objects.nonNull(params.get("pageNum"))) {
			current = Integer.valueOf(params.get("pageNum").toString());
		}
		if (Objects.nonNull(params.get("pageSize"))) {
			size = Integer.valueOf(params.get("pageSize").toString());
		}
		return new Page<T>(current, size);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(T entity) {
		//此处少了企业条件
		if(!Objects.isNull(entity.getId())){
			entity.setUpdateTime(new Date());
			//判断当前登录员工不为空
			entity.setUpdateId(UserUtils.getUserId());
			entity.setUpdateUser(UserUtils.getUserName());
			return this.updateById(entity);
		}else{
			//判断当前登录员工不为空
			entity.setId(UUIDutils.getUUID32());
			entity.setCreateId(UserUtils.getUserId());
			entity.setCreateUser(UserUtils.getUserName());
			entity.setCreateTime(new Date());
			return retBool(baseMapper.insert(entity));
		}
	}





}
