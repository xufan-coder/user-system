package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.CommonFile;
import com.zerody.user.mapper.CommonFileMapper;
import com.zerody.user.service.CommonFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author  DaBai
 * @date  2022/11/28 17:23
 */

@Slf4j
@Service
public class CommonFileServiceImpl extends ServiceImpl<CommonFileMapper, CommonFile> implements CommonFileService {

    @Override
    public void addFiles(QueryWrapper<CommonFile> removeQw, List<CommonFile> files) {
        if (DataUtil.isNotEmpty(removeQw)) {
            this.remove(removeQw);
        }
        if (CollectionUtils.isEmpty(files)) {
            return;
        }
        this.saveBatch(files);

    }

    @Override
    public List<String> getListFiles(String connectId, String type) {
        QueryWrapper<CommonFile> queryWrapper =new QueryWrapper<>();
        queryWrapper.lambda().eq(CommonFile::getConnectId,connectId).eq(CommonFile::getFileType,type).orderByDesc(CommonFile::getCreateTime);
        return this.list(queryWrapper).stream().map(s -> s.getFileUrl()).collect(Collectors.toList());
    }
}
