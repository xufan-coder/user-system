package com.zerody.user.feign;

import com.zerody.partner.api.service.PartnerRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

/**APP用户操作
 * @author  DaBai
 * @date  2021/7/9 19:11
 */


@FeignClient(url = "${supplier.tsz.requestPartnerUrl}",value = "${zerody-partner.name:zerody-partner}", contextId = "zerody-partner")
public interface PartnerFeignService extends PartnerRemoteService {

}
