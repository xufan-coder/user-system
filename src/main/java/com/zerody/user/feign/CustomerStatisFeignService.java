package com.zerody.user.feign;

import com.zerody.customer.api.service.ClewRemoteService;
import com.zerody.customer.api.service.CustomerStatisRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author  DaBai
 * @date  2021/1/11 18:01
 */

@FeignClient(value = "${zerody-customer.name:zerody-customer}", contextId = "zerody-customer-statis")
public interface CustomerStatisFeignService extends CustomerStatisRemoteService {
}
