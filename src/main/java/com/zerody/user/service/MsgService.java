package com.zerody.user.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.user.domain.Msg;
import com.zerody.user.dto.MsgDto;
import com.zerody.user.dto.MsgPageDto;
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


	/**************************************************************************************************
	 **
	 *  根据当前用户ID查询最新提示
	 *
	 * @param userId
	 * @return {@link Msg }
	 * @author DaBai
	 * @date 2021/1/12  10:52
	 */
	List<Msg> getTipList(String userId);

	/**************************************************************************************************
	 **
	 * 分页查询消息列表 
	 *
	 * @param pageDto
	 * @return {@link null }
	 * @author DaBai
	 * @date 2021/1/12  14:52
	 */
	IPage<Msg> getPageList(PageQueryDto pageDto);

	/**
	 * 删除过期消息
	 * 
	 * @param userId
	 */
	void deleteExpiredMessage(String userId);
}
