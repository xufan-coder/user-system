package com.zerody.user.service.base;

import com.alibaba.druid.util.StringUtils;
import com.zerody.common.util.UserUtils;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.AdminVo;
import com.zerody.user.dto.bean.UserPositionPageParam;
import com.zerody.user.dto.bean.UserPositionParam;
import com.zerody.user.service.SysStaffInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author PengQiang
 * @ClassName CheckUtil
 * @DateTime 2021/3/14_23:16
 * @Deacription TODO
 */
@Component
public class CheckUtil {

    @Autowired
    private SysStaffInfoService staffService;

    public <T extends UserPositionPageParam>  void SetUserPositionInfo(T  param){
        if (UserUtils.getUser().isBackAdmin()){
            return;
        }
        UserVo user = UserUtils.getUser();
        param.setCompanyId(user.getCompanyId());
        if (StringUtils.isEmpty(param.getUserId()) && StringUtils.isEmpty(param.getDepartId())) {
            AdminVo admin = this.staffService.getIsAdmin(user);
            if(admin.getIsCompanyAdmin()){

            } else if (admin.getIsDepartAdmin()) {
                param.setDepartId(user.getDeptId());
            } else {
                param.setUserId(user.getUserId());
            }
        }
    }

    public <T extends UserPositionParam>  void SetUserPositionInfo(T  param){
        if (UserUtils.getUser().isBackAdmin()){
            return;
        }
        UserVo user = UserUtils.getUser();
        param.setCompanyId(user.getCompanyId());
        if (StringUtils.isEmpty(param.getUserId()) && StringUtils.isEmpty(param.getDepartId())) {
            AdminVo admin = this.staffService.getIsAdmin(user);
            if(admin.getIsCompanyAdmin()){

            } else if (admin.getIsDepartAdmin()) {
                param.setDepartId(user.getDeptId());
            } else {
                param.setUserId(user.getUserId());
            }
        }
    }
}
