package com.zerody.user.feign;

import com.zerody.common.api.bean.DataResult;
import com.zerody.oauth.api.service.OauthRemoteService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author  DaBai
 * @date  2021/1/7 10:07
 */

@FeignClient(value = "${zerody-oauth2.name:zerody-oauth2}", contextId = "zerody-oauth2-oauth")
public interface OauthFeignService extends OauthRemoteService {

    /**
     *
     * 获取业务员角色
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/12/23 11:03
     * @param                companyId
     * @return               com.zerody.common.api.bean.DataResult<java.lang.String>
     */
    @GetMapping("/sys-auth-role/get/salesman/inner")
    DataResult<List<String>> getSalesmanRole(@RequestParam(value = "companyId", required = false) String companyId);
    
}
