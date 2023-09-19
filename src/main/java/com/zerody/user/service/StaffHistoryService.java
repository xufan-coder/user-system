package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.StaffHistory;
import com.zerody.user.dto.StaffHistoryDto;
import com.zerody.user.dto.StaffHistoryQueryDto;
import com.zerody.user.vo.StaffHistoryVo;

import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年08月25日 17:24
 */

public interface StaffHistoryService extends IService<StaffHistory> {

    /***
     * @description 添加
     * @author zhangpingping
     * @date 2021/8/25
     * @param [staffHistoryDto]
     * @return
     */
    void addStaffHistory(StaffHistoryDto staffHistoryDto);

    /**
     * @param
     * @return
     * @description 删除
     * @author zhangpingping
     * @date 2021/8/25
     */
    void removeStaffHistory(StaffHistoryQueryDto staffHistoryQueryDto);

    /***
     * @description 更新
     * @author zhangpingping
     * @date 2021/8/25
     * @param [staffHistoryDto]
     * @return
     */
    void modifyStaffHistory(StaffHistoryDto staffHistoryDto);

    /***
     * @description 查询
     * @author zhangpingping
     * @date 2021/8/26
     * @param [staffHistoryQueryDto]
     * @return
     */
    List<StaffHistoryVo> queryStaffHistory(StaffHistoryQueryDto staffHistoryQueryDto);







    /***
     * @description 获取新增荣誉墙和惩罚墙
     * @author ljj
     * @date 2023/9/18
     * @param
     * @return
     */
    List<StaffHistoryVo> getHonorPunishmentWall(StaffHistoryDto staffHistoryDto);
}
