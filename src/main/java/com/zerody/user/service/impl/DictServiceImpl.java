package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.constant.RescindType;
import com.zerody.user.domain.Dict;
import com.zerody.user.vo.dict.DictQuseryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.zerody.user.mapper.DictMapper;
import com.zerody.user.service.DictService;

import java.util.ArrayList;
import java.util.List;

/**
 *@ClassName DictServiceImpl
 *@author    PengQiang
 *@DateTime  2022/4/28_10:15
 *@Deacription TODO
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService{


    @Override
    public List<DictQuseryVo> getListByType(String type) {
        return this.baseMapper.getListByType(type);
    }

    @Override
    public DictQuseryVo getListById(String id) {
        DictQuseryVo result = new DictQuseryVo();
        Dict dict = this.getById(id);
        if (DataUtil.isEmpty(dict)) {
            return null;
        }
        BeanUtils.copyProperties(dict, result);
        return result;
    }

    @Override
    public void addDict(List<Dict> entity) {
     this.saveBatch(entity);
    }

    @Override
    public List<DictQuseryVo> rescindReason() {
        List<DictQuseryVo> list = new ArrayList<>();
        String[] ids = RescindType.IDS;
        for (String id : ids) {
            DictQuseryVo dict = getListById(id);
            list.add(dict);
        }
        return list;
    }
}
