package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.bean.DataResult;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.AdminVo;
import com.zerody.user.dto.AdminsPageDto;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.api.vo.UserDeptVo;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.vo.BosStaffInfoVo;
import com.zerody.user.vo.CopyStaffInfoVo;
import com.zerody.user.vo.SysUserClewCollectVo;
import com.zerody.user.vo.SysUserInfoVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoService
 * @DateTime 2020/12/17_17:30
 * @Deacription TODO
 */
public interface SysStaffInfoService {
    static String getInitPwd() {
        StringBuffer sb = new StringBuffer();
        sb.append((char)(Math.random()*26+'A'));
        sb.append((int)((Math.random()*10)*100000));
        sb.append((char)(Math.random()*26+'a'));
        return sb.toString();
    }

    SysStaffInfo addStaff(SetSysUserInfoDto setSysUserInfoDto);

    IPage<BosStaffInfoVo> getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto);

    void updateStaffStatus(String staffId, Integer status);


    void updateStaff(SetSysUserInfoDto setSysUserInfoDto);

    SysUserInfoVo selectStaffById(String id);

    void deleteStaffById(String staffId);

    List<String> getStaffRoles(String userId, String companyId);

    Map<String, Object> batchImportStaff(MultipartFile file) throws Exception;

    Map<String, Object> batchImportCompanyUser(MultipartFile file) throws Exception;

    List<BosStaffInfoVo> getStaff(String companyId, String departId, String positionId);

    IPage<BosStaffInfoVo> getAdmins(AdminsPageDto dto);

    /**
     * 获取员工数据，包含公司，部门，岗位
     * @param userId
     * @return
     */
	UserDeptVo getUserDeptVo(String userId);

	/**
	 *  获取员工下属部门
	 * @param userId
	 * @return
	 */
	List<String> getUserSubordinates(String userId);


    /**
     *
     *  获取下级员工 线索汇总
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/9 12:52
     * @param                userId 当前登录用户id
     * @param                dto 分页参数
     * @return               java.lang.Object
     */
    IPage<SysUserClewCollectVo> getSubordinatesUserClewCollect(PageQueryDto dto, String userId);


    CopyStaffInfoVo selectStaffInfo(String staffId);

    SysUserInfoVo selectStaffByUserId(String userId);

    IPage<BosStaffInfoVo> getWxPageAllStaff(SysStaffInfoPageDto dto);

    void doEmptySubordinatesUserClew(String id);

    AdminVo getIsAdmin(UserVo user);

}
