package com.zerody.user.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.mapper.*;
import com.zerody.user.domain.Msg;
import com.zerody.user.dto.MsgDto;
import com.zerody.user.dto.MsgPageDto;
import com.zerody.user.service.MsgService;
/**
 * 
 * 消息业务实现类
 * @author 黄华盛 
 * @date 2021-01-11
 */
@Service
public class MsgServiceImpl extends ServiceImpl<MsgMapper, Msg> implements MsgService {
	private static final Logger log = LoggerFactory.getLogger(MsgServiceImpl.class);
	
	public Msg addMsg(MsgDto data) throws Exception {
		Msg po=new Msg();
		BeanUtils.copyProperties(data, po);
		po.setCreateTime(new Date());
		this.save(po);
		return po;
	}
	public void updateMsg(MsgDto data) throws Exception {
		Msg po=new Msg();
		BeanUtils.copyProperties(data, po);
		po.setCreateTime(new Date());
		this.updateById(po);
	}
	@Override
	public IPage<Msg> pageData(MsgPageDto pageQuery) {
		Page<Msg> page = new Page<Msg>();
		page.setCurrent(pageQuery.getCurrent());
		page.setSize(pageQuery.getPageSize());
		QueryWrapper<Msg> qw = new QueryWrapper<>();
		//createTime 创建时间
		Date createTimeStart=pageQuery.getCreateTimeStart();
		Date createTimeEnd=pageQuery.getCreateTimeEnd();
		if(createTimeStart!=null&&createTimeEnd!=null){
			qw.lambda().between( Msg::getCreateTime, createTimeStart, createTimeEnd);
		}else if(createTimeStart!=null&&createTimeEnd==null){
			qw.lambda().ge( Msg::getCreateTime, createTimeStart);
		}else if(createTimeStart==null&&createTimeEnd!=null){
			qw.lambda().le( Msg::getCreateTime,createTimeEnd);
		}
			//userId user_id
			String userIdValue=pageQuery.getUserId();
			if(StringUtils.isNotEmpty(userIdValue)){
					qw.lambda().like(StringUtils.isNotBlank(userIdValue), Msg::getUserId, userIdValue);				
			}
		IPage<Msg> pageResult = this.page(page,qw);
		return pageResult;
	}

	@Override
	public List<Msg> findData() {
		return this.query().list();
	}
}
