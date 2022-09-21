package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.Data;
import com.zerody.user.dto.data.DataAddDto;
import com.zerody.user.mapper.DataMapper;
import com.zerody.user.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author PengQiang
 * @ClassName DataServiceImpl
 * @DateTime 2022/9/15_14:37
 * @Deacription TODO
 */
@Slf4j
@Service
public class DataServiceImpl extends ServiceImpl<DataMapper, Data> implements DataService {

    @Override
    public void addData(DataAddDto param) {
        //查询key是否已存
        QueryWrapper<Data> dataQw = new QueryWrapper<>();
        dataQw.lambda().eq(Data::getDataKey, param.getDataKey());
        dataQw.lambda().eq(Data::getUserId, param.getUser().getUserId());
        dataQw.lambda().last("limit 0, 1");
        Data entity = this.getOne(dataQw);
        if (DataUtil.isEmpty(entity)) {
            entity = new Data();
            entity.setCreateTime(new Date());
        }
        BeanUtils.copyProperties(param, entity);
        entity.setUserId(param.getUser().getUserId());
        // 存key覆盖value
        this.saveOrUpdate(entity);
    }

    @Override
    public String getValueByKey(String key, UserVo user) {
        if (DataUtil.isEmpty(key)) {
            throw new DefaultException("key为空");
        }
        //查询key是否已存
        QueryWrapper<Data> dataQw = new QueryWrapper<>();
        dataQw.lambda().eq(Data::getDataKey, key);
        dataQw.lambda().eq(Data::getUserId, user.getUserId());
        dataQw.lambda().last("limit 0, 1");
        Data entity = this.getOne(dataQw);
        if (DataUtil.isNotEmpty(entity)) {
            return entity.getDataValue();
        }
        return null;
    }
}
