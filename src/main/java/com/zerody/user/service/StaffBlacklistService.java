package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.StaffBlacklist;
import com.zerody.user.dto.FrameworkBlacListQueryPageDto;
import com.zerody.user.dto.StaffBlacklistAddDto;
import com.zerody.user.vo.FrameworkBlacListQueryPageVo;
import com.zerody.user.vo.MobileBlacklistQueryVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    StaffBlacklistAddDto addStaffBlaklist(StaffBlacklistAddDto param);

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

    /**
     *
     *  根据手机号码查询是否离职
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/8/5 15:53
     * @param                mobile
     * @return               com.zerody.user.vo.MobileBlacklistQueryVo
     */
    MobileBlacklistQueryVo getBlacklistByMobile(String mobile);

    /**
     * 【行政角色添加黑名单】
    *   新增加入黑名单
    */
    void addStaffBlaklistJoin(StaffBlacklistAddDto param);

    /**
     *
     * 导入内控名单
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/12/2 14:48
     * @param                file
     * @param                user
     * @return               void
     */
    String doBlacklistExternalImport(MultipartFile file, UserVo user) throws IOException;

    FrameworkBlacListQueryPageVo getInfoById(String id);
}
