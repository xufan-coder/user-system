package com.zerody.user.feign;

import com.zerody.flow.api.service.ProcessRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author  DaBai
 * @date  2022/4/19 15:33
 */
@FeignClient(value = "${zerody-flow-server.name:zerody-flow-server}",contextId = "process")
public interface ProcessServerFeignService extends ProcessRemoteService {
}
