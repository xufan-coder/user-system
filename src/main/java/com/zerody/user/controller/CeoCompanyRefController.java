package com.zerody.user.controller;

import com.alibaba.fastjson.JSON;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.domain.CompanyAdmin;
import com.zerody.user.dto.CeoRefDto;
import com.zerody.user.service.CeoCompanyRefService;
import com.zerody.user.vo.CeoRefVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * BOSS关联管理
 * @author  DaBai
 * @date  2022/6/18 11:48
 */

@Slf4j
@RestController
@RequestMapping("/ceo-ref")
public class CeoCompanyRefController {

	@Autowired
	private CeoCompanyRefService service;


	/**************************************************************************************************
	 **
	 *  保存关联
	 *
	 * @param data
	 * @return {@link DataResult }
	 * @author DaBai
	 * @date 2022/6/18  12:56
	 */
	@PostMapping("/save")
	public DataResult save(@RequestBody CeoRefDto data) {
		try {
			this.service.saveCompanyRef(data);
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
	 *  id查询ceo用户信息和关联
	 *
	 * @param id
	 * @return {@link CeoRefVo }
	 * @author DaBai
	 * @date 2022/6/18  12:56
	 */
	@GetMapping("/get/company-ref")
	public DataResult<CeoRefVo> getById(@RequestParam String id){
		try {
			CeoRefVo vo = this.service.getCeoRef(id);
			return R.success(vo);
		}catch (DefaultException e){
			return R.error(e.getMessage());
		}catch (Exception e) {
			log.error("查询出错:{}", e.getMessage(), e);
			return R.error("查询关联出错,请联系管理员！");
		}
	}



}
