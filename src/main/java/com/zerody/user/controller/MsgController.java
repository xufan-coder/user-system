package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.api.bean.R;
import com.zerody.common.util.UserUtils;
import com.zerody.user.domain.Msg;
import com.zerody.user.dto.MsgPageDto;
import com.zerody.user.service.MsgService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * 
 * 消息控制类 
 * @author 黄华盛 
 * @date 2021-01-11
 */
@RestController
@RequestMapping("/msg")
public class MsgController {
	private static final Logger log=LoggerFactory.getLogger(MsgController.class);
	@Autowired
	private MsgService service;
	private static final int MODULE_CODE=1001;

	/**
	*   小程序首页轮播消息
	 *   查询当天的跟进消息
	*/
	@GetMapping("/index/tip")
	public DataResult<List<Msg>> getTipList() {
		try {
			List<Msg> data = this.service.getTipList(UserUtils.getUserId());
			return R.success(data);
		} catch (Exception e) {
			log.error("分页查询消息出错:{}", e, e);
			return R.error("分页查询消息出错:"+e.getMessage());
		}
	}

	/**
	 *   个人中心消息列表【分页】
	 */
	@GetMapping("/page/visit")
	public DataResult<IPage<Msg>> getPageList(@RequestBody PageQueryDto pageDto) {
		try {
			IPage<Msg> data = this.service.getPageList(pageDto);
			return R.success(data);
		} catch (Exception e) {
			log.error("分页查询消息出错:{}", e, e);
			return R.error("分页查询消息出错:"+e.getMessage());
		}
	}

	/**
	 *  消息分页查询 
	 * @param grid
	 * @param session
	 * @return
	 * @author 黄华盛
	 * @date 2021-01-11
	 */
	@ApiOperation(value = "分页查询")
	@GetMapping("/page")
	public DataResult<?> page(MsgPageDto pageDto) {
		try {
			Object data = this.service.pageData(pageDto);
			return R.success(data);
		} catch (Exception e) {
			log.error("分页查询消息出错:{}", e, e);
			return R.error("分页查询消息出错:"+e.getMessage());
		}
	}
	
	@ApiOperation(value = "删除消息")
	@DeleteMapping("/{id}")
	public DataResult<?> del(@PathVariable String id) {
		try {
			this.service.removeById(id);
			return R.success();
		} catch (Exception e) {
			log.error("删除消息出错:{}", e, e);
			return R.error("删除消息出错:"+e.getMessage());
		}
	}
	@ApiOperation(value = "获取消息")
	@GetMapping("/{id}")
	public DataResult<Msg> get(@PathVariable String id) {
		try {
			Msg obj = this.service.getById(id);
			if (obj == null) {
				return R.error("找不到消息");
			}
			return R.success(obj);
		} catch (Exception e) {
			log.error("获取消息出错:{}", e, e);
			return R.error("获取消息出错:"+e.getMessage());
		}
	}
}
