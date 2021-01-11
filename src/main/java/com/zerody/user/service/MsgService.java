package com.zerody.user.service;

import java.util.List;

import com.zerody.user.dto.MsgPageDto;
import com.zerody.user.dto.MsgDto;
import com.zerody.user.domain.Msg;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 
 * 消息业务接口 
 * @author 黄华盛 
 * @date 2021-01-11
 */
public interface MsgService extends IService<Msg>{

	/**
	 * 
	 * 添加消息
	 * @param data
	 * @return
	 * @throws Exception
	 * @author 黄华盛
	 * @date 2021-01-11
	 */
	public Msg addMsg(MsgDto data) throws Exception ;
	
	/**
	 * 
	 * 修改消息
	 * @param data
	 * @return
	 * @throws Exception
	 * @author 黄华盛
	 * @date 2021-01-11
	 */
	public void updateMsg(MsgDto data) throws Exception;
   	 /**
	 * 分页查询
	 * 
	 * @param pageDto
	 * @return
	 */
	IPage<Msg> pageData(MsgPageDto pageDto);
	
	/**
	 * 查询全部数据
	 * 
	 * @return
	 */
	public List<Msg> findData();
}
