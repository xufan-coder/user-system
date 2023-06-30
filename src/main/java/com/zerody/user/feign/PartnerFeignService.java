package com.zerody.user.feign;

import com.zerody.partner.api.service.PartnerRemoteService;
import com.zerody.user.config.ApiDecoder;
import com.zerody.user.config.ApiEncoder;
import org.springframework.cloud.openfeign.FeignClient;

/**APP用户操作
 * @author  DaBai
 * @date  2021/7/9 19:11
 */


@FeignClient(url = "${supplier.tsz.requestUrl}",value = "${zerody-partner.name:zerody-partner}", contextId = "zerody-partner",
        configuration = {ApiEncoder.class, ApiDecoder.class})
public interface PartnerFeignService extends PartnerRemoteService {

}
