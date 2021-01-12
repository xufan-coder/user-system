package com.zerody.user.feign;

import com.zerody.card.api.service.CardRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author  DaBai
 * @date  2021/1/12 17:54
 */

@FeignClient(value = "${zerody-card.name:zerody-card}", contextId = "zerody-card")
public interface CardFeignService extends CardRemoteService {
}
