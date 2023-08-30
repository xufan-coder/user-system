package com.zerody.user.feign;

import com.zerody.common.api.bean.DataResult;
import com.zerody.contract.api.service.SignOrderRemoteService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(value = "${zerody-contract.name:zerody-contract}", contextId = "zerody-contract-sign")
public interface SignOrderFeignService extends SignOrderRemoteService {
}
