package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.statis.UserAgeStatisQueryDto;
import com.zerody.user.dto.statis.UserSexStatisQueryDto;
import com.zerody.user.dto.statis.UserStatisQueryDto;
import com.zerody.user.vo.StatisticsDataDetailsVo;
import com.zerody.user.vo.statis.UserAgeStatisQueryVo;
import com.zerody.user.vo.statis.UserSexStatisQueryVo;
import com.zerody.user.vo.statis.UserStatisTrendVo;
import com.zerody.user.vo.statis.UserTrendQueryVo;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName UserStatisService
 * @DateTime 2023/4/29 10:56
 */
public interface UserStatisService {


    /**
     *
     * 伙伴趋势统计
     * @author PengQiang
     * @description 彭强
     * @date 2023/4/29 11:39
     * @param param 参数
     * @return java.util.List<com.zerody.user.vo.statis.UserTrendQueryVo>
     */
    List<UserTrendQueryVo> getUserTrendS(UserStatisQueryDto param);

    /**
     *
     * 伙伴年龄分布统计
     * @author PengQiang
     * @description 彭强
     * @date 2023/5/3 10:32
     * @param param 查询参数
     * @return java.util.List<com.zerody.user.vo.statis.UserAgeStatisQueryVo>
     */
    List<UserAgeStatisQueryVo> getAgeStatis(UserAgeStatisQueryDto param);

    /**
     *
     * 伙伴性别分布统计
     * @author PengQiang
     * @description 彭强
     * @date 2023/5/3 15:22
     * @param param 参数
     * @return java.util.List<com.zerody.user.vo.statis.UserSexStatisQueryVo>
     */
    List<UserSexStatisQueryVo> getSexStatis(UserSexStatisQueryDto param);

    /**
     * @Author: chenKeFeng
     * @param
     * @Description: 统计伙伴签约详情
     * @Date: 2023/4/29 10:30
     */
    List<StatisticsDataDetailsVo> statisticsDetails(UserStatisQueryDto param);

    UserStatisTrendVo getUserTrends(UserStatisQueryDto param);

}
