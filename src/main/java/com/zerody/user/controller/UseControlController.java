package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.EntityUtils;
import com.zerody.common.util.UserUtils;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.service.RestrictRemoteService;
import com.zerody.user.domain.Msg;
import com.zerody.user.domain.UsersUseControl;
import com.zerody.user.dto.StaffBlacklistAddDto;
import com.zerody.user.dto.UseControlDto;
import com.zerody.user.dto.UsersUseControlPageDto;
import com.zerody.user.service.UseControlService;
import com.zerody.user.service.UsersUseControlService;
import com.zerody.user.vo.UseControlVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * 登录限制控制
 * @author  DaBai
 * @date  2022/3/1 14:05
 */

@RestController
@RequestMapping("/restrict")
public class UseControlController  implements RestrictRemoteService {
	private static final Logger log=LoggerFactory.getLogger(UseControlController.class);
	@Autowired
	private UseControlService useControlService;
	@Autowired
	private UsersUseControlService usesUseControlService;


	/**
	*   添加或修改时间限制
	*/
	@PostMapping("/addOrUpdate")
	public DataResult addOrUpdate(@RequestBody UseControlDto param){
		try {
			this.useControlService.addOrUpdate(param);
			return R.success();
		} catch (DefaultException e) {
			log.error("保存登录限制错误：{}", e, e);
			return R.error(e.getMessage());
		} catch (Exception e) {
			log.error("保存登录限制错误：{}", e, e);
			return R.error("保存登录限制错误" + e.getMessage());
		}
	}

	/**
	 *   获取企业时间配置
	 */
	@GetMapping("/get")
	public DataResult<UseControlVo> getByCompany(@RequestParam("companyId") String companyId) {
		try {
			UseControlVo data = this.useControlService.getByCompany(companyId);
			return R.success(data);
		} catch (Exception e) {
			log.error("获取企业时间配置错误:{}", e, e);
			return R.error("获取企业时间配置错误:"+e.getMessage());
		}
	}


	/**
	 *   添加黑/白名单
	 */
	@PostMapping("/users/name-list/add")
	public DataResult addNameList(@RequestBody UsersUseControl param){
		try {
			this.usesUseControlService.addNameList(param);
			return R.success();
		} catch (DefaultException e) {
			log.error("添加黑/白名单错误：{}", e, e);
			return R.error(e.getMessage());
		} catch (Exception e) {
			log.error("添加黑/白名单错误：{}", e, e);
			return R.error("添加黑/白名单错误" + e.getMessage());
		}
	}

	/**
	 *   移除黑/白名单
	 */
	@DeleteMapping("/users/name-list/remove/{id}")
	public DataResult removeNameList(@PathVariable String id){
		try {
			this.usesUseControlService.removeNameList(id);
			return R.success();
		} catch (DefaultException e) {
			log.error("移除黑/白名单错误：{}", e, e);
			return R.error(e.getMessage());
		} catch (Exception e) {
			log.error("移除黑/白名单错误：{}", e, e);
			return R.error("添加黑/白名单错误" + e.getMessage());
		}
	}

	/**
	 *   名单列表【分页】
	 */
	@GetMapping("/page")
	public DataResult<IPage<UsersUseControl>> getPageList(UsersUseControlPageDto pageDto) {
		try {
			IPage<UsersUseControl> data = this.usesUseControlService.getPageList(pageDto);
			return R.success(data);
		} catch (Exception e) {
			log.error("分页查询名单列表出错:{}", e, e);
			return R.error("分页查询名单列表出错:"+e.getMessage());
		}
	}

	/**
	* 网关查询是否禁用系统
	*/
	@Override
	@RequestMapping(value = "/get/auth/inner", method = RequestMethod.GET)
	public DataResult<Boolean> getRestrictAuth(@RequestParam("token") String token) {
		try {
			String key = Base64.getEncoder().encodeToString("ZERODY".getBytes());
			Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
			Object userInfo = claims.get("userInfo");
			Map<String, Object> map = (Map)userInfo;
			UserVo vo = EntityUtils.mapToEntity(map, UserVo.class);
			return R.success(this.useControlService.checkUserAuth(vo));
		} catch (Exception e) {
			log.error("校验限制配置出错:{}", e, e);
			return R.success(Boolean.FALSE);
		}

	}
}
