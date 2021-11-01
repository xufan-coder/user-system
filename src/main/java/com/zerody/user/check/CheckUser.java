package com.zerody.user.check;

import com.zerody.common.bean.DataResult;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.util.IdCardUtil;
import org.apache.commons.lang.StringUtils;

/**
 * Description: 检查用户
 * Created by pengqiang on 2020/12/17 10:15
 */
public class CheckUser {


    /**
     * 用户操作参数校验
     */
    public static void checkParam(SysUserInfo SysUserInfo) {
        if (StringUtils.isBlank(SysUserInfo.getPhoneNumber())) {
            throw new DefaultException("手机号码不能为空");
        }
        if (!SysUserInfo.getPhoneNumber().matches("\\d{11}")) {
            throw new DefaultException("手机号码长度不正确");
        }
        if (StringUtils.isNotEmpty(SysUserInfo.getCertificateCard())) {
            if (!IdCardUtil.validate18Idcard(SysUserInfo.getCertificateCard())) {
                throw new DefaultException("身份证不合法");
            }
        } else {
//            throw new DefaultException("身份证不能为空");
        }
    }

}
