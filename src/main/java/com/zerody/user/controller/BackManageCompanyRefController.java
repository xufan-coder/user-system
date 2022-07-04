package com.zerody.user.controller;

import com.alibaba.fastjson.JSON;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.dto.BackRefDto;
import com.zerody.user.dto.CeoRefDto;
import com.zerody.user.service.CeoCompanyRefService;
import com.zerody.user.vo.BackUserRefVo;
import com.zerody.user.vo.CeoRefVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台关联管理
 * @author  DaBai
 * @date  2022/6/18 11:48
 */
@Slf4j
@RestController
@RequestMapping("/back-ref")
public class BackManageCompanyRefController {

	@Autowired
	private CeoCompanyRefService service;

	/**************************************************************************************************
	 **
	 *  后台保存关联
	 *
	 * @param data
	 * @return {@link DataResult }
	 * @author DaBai
	 * @date 2022/6/18  12:56
	 */
	@PostMapping("/save")
	public DataResult save(@RequestBody BackRefDto data) {
		try {
			this.service.saveBackCompanyRef(data);
			return R.success();
		}catch (DefaultException e){
			return R.error(e.getMessage());
		}catch (Exception e) {
			log.error("保存出错:{}", JSON.toJSONString(data), e);
			return R.error("保存关联出错,请联系管理员！");
		}
	}


	/**************************************************************************************************
	 **
	 *  id查询后台用户信息和关联
	 *
	 * @param id
	 * @return {@link CeoRefVo }
	 * @author DaBai
	 * @date 2022/6/18  12:56
	 */
	@GetMapping("/get/company-ref")
	public DataResult<BackUserRefVo> getById(@RequestParam String id){
		try {
			BackUserRefVo vo = this.service.getBackRef(id);
			return R.success(vo);
		}catch (DefaultException e){
			return R.error(e.getMessage());
		}catch (Exception e) {
			log.error("查询出错:{}", e.getMessage(), e);
			return R.error("查询关联出错,请联系管理员！");
		}
	}



}
