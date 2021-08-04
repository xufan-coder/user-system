package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.domain.StaffBlacklist;
import com.zerody.user.dto.FrameworkBlacListQueryPageDto;
import com.zerody.user.vo.FrameworkBlacListQueryPageVo;
import org.apache.ibatis.annotations.Param;

/**
 * @author peneqiang
 */
public interface StaffBlacklistMapper extends BaseMapper<StaffBlacklist> {

    /**
     *
     *  查询员工黑名单
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/8/4 11:46
     * @param                param
     * @param                iPage
     * @return               com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.FrameworkBlacListQueryPageVo>
     */
    IPage<FrameworkBlacListQueryPageVo> getPageBlackList(@Param("param") FrameworkBlacListQueryPageDto param, IPage<FrameworkBlacListQueryPageVo> iPage);
}