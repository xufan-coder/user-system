package com.zerody.user.task;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.common.constant.YesNo;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.user.domain.SysCompanyInfo;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.service.SysCompanyInfoService;
import com.zerody.user.service.SysDepartmentInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.vo.CheckLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户短信发送
 * @author PengQiang
 * @ClassName CompanyEditTask
 * @DateTime 2021/5/21_19:09
 * @Deacription TODO
 */
@Component
@Slf4j
public class UserPwdSmsTask {
    @Autowired
    private SysCompanyInfoService companyInfoService;

    @Autowired
    private SysStaffInfoService staffInfoService;

    @XxlJob("user-pwd-sms")
    public ReturnT<String> execute(String param){
        List<String> companyIds =  this.companyInfoService.getNotSmsCompany();
        int row = 50;
        if (CollectionUtils.isEmpty(companyIds)) {
            return ReturnT.SUCCESS;
        }
        for (String companyId : companyIds) {
            List<CheckLoginVo> staffs = this.staffInfoService.getNotSendPwdSmsStaff(companyId);
            if (CollectionUtils.isEmpty(staffs)) {
                continue;
            }
            //指定数量发送短信防止事务超时
            for (int i = 0, size = staffs.size(); i <= size; i += row) {
                if (size - i < row) {
                    row = size - i;
                }
                this.staffInfoService.doSendStaffPwdSms(staffs.subList(i, row));
            }
        }
        return ReturnT.SUCCESS;
    }
}
