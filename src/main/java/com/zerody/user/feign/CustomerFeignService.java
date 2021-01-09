package com.zerody.user.feign;

import com.zerody.customer.api.service.ClewRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author PengQiang
 * @ClassName CustomerFeignService
 * @DateTime 2021/1/9_13:49
 * @Deacription TODO
 */
@FeignClient("zerody-customer.name:zerody-customer")
public interface CustomerFeignService extends ClewRemoteService {
}
