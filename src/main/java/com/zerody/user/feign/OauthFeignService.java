package com.zerody.user.feign;

import com.zerody.oauth.api.service.OauthRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author  DaBai
 * @date  2021/1/7 10:07
 */

@FeignClient(value = "${zerody-oauth2.name:zerody-oauth2}", contextId = "zerody-oauth2-oauth")
public interface OauthFeignService extends OauthRemoteService {
    
}
