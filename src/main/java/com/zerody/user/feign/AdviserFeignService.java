package com.zerody.user.feign;

import com.zerody.adviser.api.service.AdviserRemoteService;
import com.zerody.common.api.bean.DataResult;
import com.zerody.user.domain.AdviserDepartChangePush;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author kuang
 */
@FeignClient(url = "${supplier.tsz.requestAdviserUrl}",value = "${zerody-adviser.name:zerody-adviser}", contextId = "zerody-adviser")
public interface AdviserFeignService extends AdviserRemoteService {

    @PostMapping("/depart/sync/inner")
    void doSyncDepart(@RequestBody List<Map<String, String>> data);
}
