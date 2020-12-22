package com.zerody.user.check;

//import com.bonade.common.util.ResultCodeEnum;
//import com.bonade.user.exception.BizException;
//import com.bonade.user.pojo.ZtSysUserInfo;
//import com.bonade.user.pojo.vo.PlatformUserInfoVo;
//import com.bonade.user.utils.IdCardUtil;
import com.zerody.common.bean.DataResult;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.user.pojo.SysUserInfo;
import com.zerody.user.util.IdCardUtil;
import org.apache.commons.lang.StringUtils;

/**
 * Description: 检查用户
 * Created by pengqiang on 2020/12/17 10:15
 */
public class CheckUser {


//    /**
//     * 检查非实名用户的参数
//     * @return 是否是实名调用
//     */
//    public static boolean checkUnRealName(PlatformUserInfoVo vo){
//        if (StringUtils.isNotEmpty(vo.getCertificateCard())) {
//            if (!IdCardUtil.validate18Idcard(vo.getCertificateCard())) {
//                throw new BizException(ResultCodeEnum.RESULT_PARAMETER_ERROR, "身份证号码格式错误");
//            }
//        }
//        if (StringUtils.isNotEmpty(vo.getCertificateCard()) && StringUtils.isNotEmpty(vo.getUsername())) {
//            if (vo.getCertificateType()==null || vo.getGender()==null) {
//                throw new BizException(ResultCodeEnum.RESULT_PARAMETER_ERROR, "参数错误");
//            }
//        }
//        if (StringUtils.isNotEmpty(vo.getCertificateCard()) && StringUtils.isNotEmpty(vo.getUsername())){
//            if (vo.getCertificateType() == null || vo.getGender() == null){
//                throw new BizException("实名用户的证件类型和性别必填");
//            }
//        }
//        return StringUtils.isNotEmpty(vo.getCertificateCard()) && StringUtils.isNotEmpty(vo.getUsername());
//    }
//
    /**
     * 用户操作参数校验
     */
    public static DataResult checkParam(SysUserInfo SysUserInfo) {
        if (StringUtils.isBlank(SysUserInfo.getPhoneNumber())) {
            return new DataResult(ResultCodeEnum.RESULT_PARAMETER_ERROR, false,"手机号码不能为空",null);
        }
        if (!SysUserInfo.getPhoneNumber().matches("\\d{11}")) {
            return new DataResult(ResultCodeEnum.RESULT_PARAMETER_ERROR, false,"手机号码长度不正确",null);
        }
        if (StringUtils.isNotEmpty(SysUserInfo.getCertificateCard())) {
            if (!IdCardUtil.validate18Idcard(SysUserInfo.getCertificateCard())) {
                return new DataResult(ResultCodeEnum.RESULT_PARAMETER_ERROR, false,"身份证不合法",null);
            }
        }
        return new DataResult();
    }
//
//    /**
//     * 未实名
//     * 检查参数格式
//     */
//    public static void checkUnrealParam(ZtSysUserInfo ztSysUserInfo) {
//        if (StringUtils.isBlank(ztSysUserInfo.getPhoneNumber())) {
//            throw new BizException(ResultCodeEnum.RESULT_PARAMETER_ERROR, "手机号码不能为空");
//        }
//        if (!ztSysUserInfo.getPhoneNumber().matches("\\d{11}")) {
//            throw new BizException(ResultCodeEnum.RESULT_PARAMETER_ERROR, "手机号码长度不正确");
//        }
//    }

}
