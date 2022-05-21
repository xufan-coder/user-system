package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.Dict;
import com.zerody.user.vo.dict.DictQuseryVo;

import java.util.List;

/**
 *@ClassName DictService
 *@author    PengQiang
 *@DateTime  2022/4/28_10:15
 *@Deacription TODO
 */
public interface DictService extends IService<Dict> {


    /**
     *
     * 获取字典信息
     * @author               PengQiang
     * @description          DELL
     * @date                 2022/4/28 11:15
     * @param                type
     * @return               java.util.List<com.zerody.user.vo.dict.DictQuseryVo>
     */
    List<DictQuseryVo> getListByType(String type);


    /**
     *
     * 获取字典信息
     * @author               PengQiang
     * @description          DELL
     * @date                 2022/4/28 11:15
     * @param                id
     * @return               java.util.List<com.zerody.user.vo.dict.DictQuseryVo>
     */
    DictQuseryVo getListById(String id);

    void addDict(List<Dict> entity);
}
