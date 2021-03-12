package com.zerody.user.feign;

import com.zerody.contract.api.service.ContractRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author PengQiang
 * @ClassName ContractFeignService
 * @DateTime 2021/3/11_16:40
 * @Deacription TODO
 */
@FeignClient(value = "${zerody-contract.name:zerody-contract}", contextId = "zerody-contract-contract")
public interface ContractFeignService extends ContractRemoteService {
}
