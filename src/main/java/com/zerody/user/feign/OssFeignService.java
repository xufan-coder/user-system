package com.zerody.user.feign;

import com.zerody.oss.api.service.OssRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author PengQiang
 * @ClassName OssFeignService
 * @DateTime 2023/2/24 10:33
 */
@FeignClient(value = "${zerody-oss.name:zerody-oss}", contextId = "zerody-oss-upload")
public interface OssFeignService extends OssRemoteService {
}
