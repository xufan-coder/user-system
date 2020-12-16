package com.zerody.user.service.base;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.util.UserUtils;
import com.zerody.user.pojo.base.BaseStringModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @Description: TODO
 * @auther: fumin
 * @date: 2019/3/19
 */

public abstract class BaseStringService<M extends BaseMapper<T>, T extends BaseStringModel> extends ServiceImpl<BaseMapper<T>, T> {

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
		if (!Objects.isNull(params.get("pageNum"))) {
			current = Integer.valueOf(params.get("pageNum").toString());
		}
		if (!Objects.isNull(params.get("pageSize"))) {
			size = Integer.valueOf(params.get("pageSize").toString());
		}
		return new Page<T>(current, size);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(T entity) {
		if(entity.getId() != null && entity.getId().length()>0){
			entity.setUpdateTime(new Date());
			entity.setCreateId(UserUtils.getUserId());
			entity.setCreateTime(new Date());
			return this.updateById(entity);
		}else{
			entity.setId(UUIDutils.getUUID32());
			entity.setCreateId(UserUtils.getUserId());
			entity.setCreateTime(new Date());
			entity.setCreateTime(new Date());
			return retBool(baseMapper.insert(entity));
		}
	}




}
