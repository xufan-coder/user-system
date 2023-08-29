package com.zerody.user.feign;

import com.zerody.common.api.bean.DataResult;
import com.zerody.contract.api.service.SignOrderRemoteService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author : chenKeFeng
 * @date : 2023/1/4 11:08
 */
@FeignClient(value = "${zerody-contract.name:zerody-contract}", contextId = "zerody-contract-sign")
public interface SignOrderFeignService extends SignOrderRemoteService {

    @GetMapping("/sign-order/get/user/inner")
    DataResult<List<String>> getCustomerId(String userId);

}
