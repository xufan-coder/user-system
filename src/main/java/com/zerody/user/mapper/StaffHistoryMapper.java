package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.domain.StaffHistory;
import com.zerody.user.dto.StaffHistoryDto;
import com.zerody.user.dto.statis.UserStatisQueryDto;
import com.zerody.user.vo.StaffHistoryVo;
import com.zerody.user.vo.SysAddressBookVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年08月25日 17:23
 */

public interface StaffHistoryMapper extends BaseMapper<StaffHistory> {


    /***
     * @description 获取新增荣誉墙和惩罚墙
     * @author ljj
     * @date 2023/9/18
     * @param
     * @return
     */
    List<StaffHistoryVo> getHonorPunishmentWall(@Param("param") StaffHistoryDto param);
}
