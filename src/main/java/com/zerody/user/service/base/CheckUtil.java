package com.zerody.user.service.base;

import com.alibaba.druid.util.StringUtils;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.AdminVo;
import com.zerody.user.domain.StaffBlacklist;
import com.zerody.user.dto.StaffBlacklistAddDto;
import com.zerody.user.dto.bean.UserPositionPageParam;
import com.zerody.user.dto.bean.UserPositionParam;
import com.zerody.user.feign.OauthFeignService;
import com.zerody.user.service.SysStaffInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName CheckUtil
 * @DateTime 2021/3/14_23:16
 * @Deacription TODO
 */
@Slf4j
@Component
public class CheckUtil {

    @Autowired
    private SysStaffInfoService staffService;

    @Autowired
    private OauthFeignService oauthFeignService;

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

    public void removeUserToken(String userId) {
        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        log.info("删除了id：{},的token ", userId);
        oauthFeignService.removeToken(userIds);
    }

    public void getCheckAddBlacListParam(StaffBlacklistAddDto param) {
        StaffBlacklist blac = param.getBlacklist();
        // 添加校验
        if (StringUtils.isEmpty(blac.getId())) {
            if (StringUtils.isEmpty(blac.getReason())) {
                throw new DefaultException("原因不能为空");
            }
            if (blac.getReason().length() > 200) {
                throw new DefaultException("原因最多200个字符");
            }
        }
        List<String> images =  param.getImages();
        if (CollectionUtils.isEmpty(images)) {
            return;
        }
        if (images.size() > 9) {
            throw new DefaultException("相关图片最多9张");
        }
    }
}
