package com.zerody.user.feign;

import com.zerody.common.api.bean.DataResult;
import com.zerody.contract.api.service.ContractRemoteService;
import com.zerody.user.dto.PerformanceInfoDto;
import com.zerody.user.vo.PerformanceInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author PengQiang
 * @ClassName ContractFeignService
 * @DateTime 2021/3/11_16:40
 * @Deacription TODO
 */
@FeignClient(value = "${zerody-contract.name:zerody-contract}", contextId = "zerody-contract-contract")
public interface ContractFeignService extends ContractRemoteService {
    /**************************************************************************************************
     **
     * 我的业绩报表
     *
     * @param null
     * @return {@link null }
     * @author DaBai
     * @date 2021/3/27  11:16
     */
    @RequestMapping(value = "/statis-achieve/performance-statement/contain-subordinate", method = RequestMethod.GET)
    DataResult<PerformanceInfoVo> getPerformanceInfoContainSubordinate(@RequestParam("userId") String userId);
}
