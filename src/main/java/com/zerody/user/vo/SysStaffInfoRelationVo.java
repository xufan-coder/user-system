package com.zerody.user.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zhangpingping
 * @date 2022年02月19日 17:00
 */
 @Data
public class SysStaffInfoRelationVo {
     /**关系*/
    private List<SysStaffRelationVo> sysStaffRelationVos;
    /**一级推荐*/
    private RecommendInfoVo recommendInfoVoOne;
    /**二级推荐*/
    private RecommendInfoVo recommendInfoVoTow;
}
