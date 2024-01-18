package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.AdviserDepartChangePush;

import java.util.List;

/**
 * @author  DaBai
 * @date  2024/1/18 10:44
 */

public interface AdviserDepartChangePushService extends IService<AdviserDepartChangePush> {

    void saveAdviserSync(String departId, String userId);

    List<AdviserDepartChangePush> selectAll();
}
