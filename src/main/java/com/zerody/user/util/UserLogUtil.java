package com.zerody.user.util;

import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.log.api.constant.ModuleCodeType;
import com.zerody.log.api.constant.SystemCodeType;
import com.zerody.log.api.dto.OperatorLogDto;
import com.zerody.log.api.util.Log;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.vo.UserCompar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author kuang
 */
@Slf4j
@Component
public class UserLogUtil {


    private static SysStaffInfoService sysStaffInfoService;

    @Autowired
    public void setFeign(SysStaffInfoService userFeignService) {
        UserLogUtil.sysStaffInfoService = userFeignService;
    }
    public static void  addUserLog(SysUserInfo userInfo, UserVo operator, String content, List<String> contentList, String dataCode){

        StringBuilder contentBuilder = new StringBuilder(content);
        for(String s : contentList) {
            if(contentBuilder.length() >0) {
                contentBuilder.append(", ");
            }
            contentBuilder.append(s);
        }
        content = contentBuilder.toString();
        addUserLog(userInfo, operator, content, dataCode);
    }

    public static void addUserLog(SysUserInfo userInfo, UserVo operator, Integer status, String dataCode){
        String name1= -1 == userInfo.getStatus() ?"已解约": 0 == userInfo.getStatus() ?"合约中":3 == userInfo.getStatus()  ? "合作":"";
        String name2=1==status ?"已解约": 0==status?"合约中": 3==status ? "合作":"";
        String content = "将状态["+name1 +"]更改为["+name2+"]";
        addUserLog(userInfo, operator, content, dataCode);
    }

    public static void  addUserLog(SysUserInfo userInfo, UserVo operator, String content, String dataCode){
        log.info("伙伴修改项对比结果:{}",content);
        OperatorLogDto logDto = new OperatorLogDto();
        logDto.setSystemCode(SystemCodeType.SYSTEM_CRM_PC);
        logDto.setModuleCode(ModuleCodeType.CUSTOMER);
        logDto.setDataCode(dataCode);
        logDto.setName(userInfo.getUserName());
        logDto.setMobile(userInfo.getPhoneNumber());
        logDto.setUserId(userInfo.getId());
        logDto.setContent(content);
        logDto.setOperatorId(operator.getUserId());
        logDto.setOperator(operator.getUserName());

        StaffInfoVo infoVo = sysStaffInfoService.getStaffInfo(userInfo.getId());
        logDto.setCompanyId(infoVo.getCompanyId());
        logDto.setCompanyName(infoVo.getCompanyName());
        logDto.setDepartId(infoVo.getDepartId());
        logDto.setDepartName(infoVo.getDepartmentName());
        logDto.setRoleName(infoVo.getRoleName());
        Log.addCustomerLog(logDto);
    }
}
