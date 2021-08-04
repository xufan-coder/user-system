package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.StaffBlacklist;
import com.zerody.user.dto.FrameworkBlacListQueryPageDto;
import com.zerody.user.dto.StaffBlacklistAddDto;
import com.zerody.user.vo.FrameworkBlacListQueryPageVo;

/**
 * @author PengQiang
 * @ClassName StaffBlacklistService
 * @DateTime 2021/8/4_9:25
 * @Deacription TODO
 */
public interface StaffBlacklistService extends IService<StaffBlacklist> {

    /**
     *
     *  添加员工黑名单
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/8/4 9:58
     * @param                param
     * @return               void
     */
    void addStaffBlaklist(StaffBlacklistAddDto param);

    /**
     *
     *  分页查询黑名单
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/8/4 11:44
     * @param                param
     * @return               com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.FrameworkBlacListQueryPageVo>
     */
    IPage<FrameworkBlacListQueryPageVo> getPageBlackList(FrameworkBlacListQueryPageDto param);

    /**
     *
     *  解除黑名单
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/8/4 16:02
     * @param                staffId
     * @return               void
     */
    void doRelieveByStaffId(String staffId);
}
