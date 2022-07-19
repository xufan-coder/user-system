package com.zerody.user.util;

import com.zerody.common.constant.UserTypeInfo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;

/**
 * @author PengQiang
 * @ClassName UserTypeUtil
 * @DateTime 2022/7/18_15:13
 * @Deacription TODO
 */
public class UserTypeUtil {

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
