package com.zerody.user.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zerody.common.constant.UserTypeInfo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.CompanyAdmin;
import com.zerody.user.domain.SysDepartmentInfo;
import com.zerody.user.service.CompanyAdminService;
import com.zerody.user.service.SysDepartmentInfoService;
import com.zerody.user.service.SysStaffInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author PengQiang
 * @ClassName UserTypeUtil
 * @DateTime 2022/7/18_15:13
 * @Deacription TODO
 */
@Component
public class UserTypeUtil {



    @Autowired
    private CompanyAdminService companyAdminService;

    @Autowired
    private SysDepartmentInfoService sysDepartmentInfoService;

    private static CompanyAdminService companyAdminStaticService;


    private static SysDepartmentInfoService sysDepartmentInfoStaticService;

    @PostConstruct
    private void init() {
        companyAdminStaticService = this.companyAdminService;
        sysDepartmentInfoStaticService = this.sysDepartmentInfoService;
    }

    public static Map<String, Integer> getUserTypeByStaffIds(String... staffIds) {
        if (DataUtil.isEmpty(staffIds)) {
            return null;
        }
        return getUserTypeByStaffIds(Arrays.asList(staffIds));
    }

    public static Map<String, Integer> getUserTypeByStaffIds(List<String> staffIds) {
        // 从大到小 获取类型
        Map<String, Integer> userTypeMap = new HashMap<>();
        QueryWrapper<CompanyAdmin> companyAdminQw = new QueryWrapper<>();
        companyAdminQw.lambda().in(CompanyAdmin::getStaffId, staffIds);
        List<CompanyAdmin> companyAdmins = companyAdminStaticService.list(companyAdminQw);
        // 获取总经理用户类型(企业管理员类型)
        if (DataUtil.isNotEmpty(companyAdmins)) {
            companyAdmins.forEach(ca -> {
                if (DataUtil.isEmpty(ca)) {
                    return;
                }
                userTypeMap.put(ca.getStaffId(), UserTypeInfo.COMPANY_ADMIN);
                staffIds.remove(ca.getStaffId());
            });
        }
        // 最后查找是否存在伙伴类型的
        if (DataUtil.isEmpty(staffIds)) {
            return  userTypeMap;
        }
        QueryWrapper<SysDepartmentInfo> departAdminQw = new QueryWrapper<>();
        departAdminQw.lambda().in(SysDepartmentInfo::getAdminAccount, staffIds);
        List<SysDepartmentInfo> departAdmins = sysDepartmentInfoStaticService.list(departAdminQw);
        if (DataUtil.isNotEmpty(departAdmins)) {
            departAdmins.forEach(da -> {
                if (DataUtil.isEmpty(da)) {
                    return;
                }
                //一级部门为 副总用户类型 否则为团队长
                if (DataUtil.isEmpty(da.getParentId())) {
                    userTypeMap.put(da.getAdminAccount(), UserTypeInfo.DEPUTY_GENERAL_MANAGERv);
                    staffIds.remove(da.getAdminAccount());
                } else {
                    userTypeMap.put(da.getAdminAccount(), UserTypeInfo.LONG_TEAM);
                    staffIds.remove(da.getAdminAccount());
                }
            });
        }
        staffIds.forEach(s -> {
            userTypeMap.put(s , UserTypeInfo.PARTNER);
            staffIds.remove(s);
        });
        return userTypeMap;
    }


    public static String getRoleName(Integer userType) {
        if (DataUtil.isEmpty(userType)) {
            throw new DefaultException("用户类型为空");
        }
        switch (userType) {
            default:
                throw new DefaultException("用户类型错误");
            case UserTypeInfo.COMPANY_ADMIN :
                return "总经理";
            case UserTypeInfo.DEPUTY_GENERAL_MANAGERv :
                return "副总经理";
            case UserTypeInfo.LONG_TEAM :
                return "团队长";
            case UserTypeInfo.PARTNER:
                return "合作伙伴";
        }
    }
}
