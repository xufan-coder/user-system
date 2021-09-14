package com.zerody.user.feign;

import com.zerody.common.api.bean.DataResult;
import com.zerody.customer.api.service.ClewRemoteService;
import com.zerody.customer.api.service.CustomerRemoteService;
import com.zerody.user.dto.StaffCustomerDetailsDto;
import org.omg.CORBA.INTERNAL;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author PengQiang
 * @ClassName CustomerFeignService
 * @DateTime 2021/1/9_13:49
 * @Deacription TODO
 */
@FeignClient(value = "${zerody-customer.name:zerody-customer}", contextId = "zerody-customer-customer")
public interface CustomerFeignService extends CustomerRemoteService {

    @GetMapping("/customer/get/staff-details")
    DataResult<Integer> getStaffCustomerDetailsCount(@RequestParam("userId") String userId, @RequestParam("state")String state);
}
