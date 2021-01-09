package com.zerody.user.feign;

import com.zerody.customer.api.service.ClewRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author PengQiang
 * @ClassName CustomerFeignService
 * @DateTime 2021/1/9_13:49
 * @Deacription TODO
 */
@FeignClient(value = "${zerody-customer.name:zerody-customer}", contextId = "zerody-customer-clew")
public interface CustomerFeignService extends ClewRemoteService {
}
