package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.statis.UserStatisQueryDto;
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
}
