package com.zerody.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.CommonFile;
import com.zerody.user.domain.Image;

import java.util.List;

/**
 * @author  DaBai
 * @date  2022/11/28 17:22
 */

public interface CommonFileService extends IService<CommonFile> {


    void addFiles(QueryWrapper<CommonFile> removeQw, List<CommonFile> files);

    List<String> getListFiles(String connectId, String type) ;
}
