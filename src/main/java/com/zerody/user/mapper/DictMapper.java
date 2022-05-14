package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.domain.Dict;
import com.zerody.user.vo.dict.DictQuseryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName DictMapper
 * @DateTime 2022/4/28_10:15
 * @Deacription TODO
 */
public interface DictMapper extends BaseMapper<Dict> {
    /**
     * 获取字典信息
     *
     * @param type
     * @return java.util.List<com.zerody.user.vo.dict.DictQuseryVo>
     * @author PengQiang
     * @description DELL
     * @date 2022/4/28 11:16
     */
    List<DictQuseryVo> getListByType(@Param("type") String type);
}