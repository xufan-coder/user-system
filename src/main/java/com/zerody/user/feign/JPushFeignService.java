package com.zerody.user.feign;

import com.zerody.jpush.api.service.JpushRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author kuang
 */
@FeignClient(value = "${zerody-jpush.name:zerody-jpush}", contextId = "zerody-jpush-pull")
public interface JPushFeignService extends JpushRemoteService {
}
