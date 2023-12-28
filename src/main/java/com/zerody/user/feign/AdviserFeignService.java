package com.zerody.user.feign;

import com.zerody.adviser.api.service.AdviserRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author kuang
 */
@FeignClient(url = "${supplier.tsz.requestAdviserUrl}",value = "${zerody-adviser.name:zerody-adviser}", contextId = "zerody-adviser")
public interface AdviserFeignService extends AdviserRemoteService {
}
