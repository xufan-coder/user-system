package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.StaffBlacklist;
import com.zerody.user.dto.FrameworkBlacListQueryPageDto;
import com.zerody.user.dto.InternalControlDto;
import com.zerody.user.dto.MobileAndIdentityCardDto;
import com.zerody.user.dto.StaffBlacklistAddDto;
import com.zerody.user.vo.BlackListCount;
import com.zerody.user.vo.FrameworkBlacListQueryPageVo;
import com.zerody.user.vo.InternalControlVo;
import com.zerody.user.vo.MobileBlacklistQueryVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    void doRelieve(String id,Integer state);

    /**
     *
     *  根据手机号码查询是否离职
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/8/5 15:53
     * @param
     * @return               com.zerody.user.vo.MobileBlacklistQueryVo
     */
    MobileBlacklistQueryVo getBlacklistByMobile(MobileAndIdentityCardDto dto, UserVo userVo,boolean isTraverse);

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

    List<BlackListCount> getBlacklistCount();

    void updateStaffBlacklist(StaffBlacklist param);

    void doRelieveByMobile(String mobile, Integer state,String relieveId);

    List<StaffBlacklist> updateRelieveByMobile(StaffBlacklist param);

    InternalControlVo updateInternalControl(InternalControlDto internalControlDto);

    void addStaffBlaklistProcessJoin(StaffBlacklistAddDto param,UserVo userVo);

}
