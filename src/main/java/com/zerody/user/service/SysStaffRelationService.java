package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.SysStaffRelation;
import com.zerody.user.dto.SysStaffRelationDto;
import com.zerody.user.vo.SysStaffRelationVo;

import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年09月09日 16:16
 */

public interface SysStaffRelationService extends IService<SysStaffRelation> {
    /**
     * @param [sysStaffRelationDto]
     * @return
     * @description 添加
     * @author zhangpingping
     * @date 2021/9/9
     */
    void addRelation(SysStaffRelationDto sysStaffRelationDto);

    /***
     * @description 删除
     * @author zhangpingping
     * @date 2021/9/9
     * @param [sysStaffRelationDto]
     * @return
     */
    void removeRelation(SysStaffRelationDto sysStaffRelationDto);

    /***
     * @description 查询
     * @author zhangpingping
     * @date 2021/9/10
     * @param [sysStaffRelationDto]
     * @return
     */
    List<SysStaffRelationVo> queryRelationList(SysStaffRelationDto sysStaffRelationDto);

    /***
     * @description 查询
     * @author zhangpingping
     * @date 2021/9/10
     * @param [sysStaffRelationDto]
     * @return
     */
    List<SysStaffRelationVo> queryRelationByListId(SysStaffRelationDto sysStaffRelationDto);
}
