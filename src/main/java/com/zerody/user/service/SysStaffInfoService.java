package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.AdminsPageDto;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.vo.BosStaffInfoVo;
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
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    void addStaff(SetSysUserInfoDto setSysUserInfoDto);

    IPage<BosStaffInfoVo> getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto);

    void updateStaffStatus(String staffId, Integer status);


    void updateStaff(SetSysUserInfoDto setSysUserInfoDto);

    SysUserInfoVo selectStaffById(String id);

    void deleteStaffById(String staffId);

    List<String> getStaffRoles(String userId, String companyId);

    Map<String, Object> batchImportStaff(MultipartFile file) throws Exception;

    List<BosStaffInfoVo> getStaff(String companyId, String departId, String positionId);

    IPage<BosStaffInfoVo> getAdmins(AdminsPageDto dto);
}
