package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.domain.UserOpinionRef;
import com.zerody.user.domain.UserOpinionType;
import com.zerody.user.mapper.UserOpinionRefMapper;
import com.zerody.user.mapper.UserOpinionTypeMapper;
import com.zerody.user.service.CeoUserInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.UserOpinionRefService;
import com.zerody.user.service.UserOpinionTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kuang
 */
@Service
public class UserOpinionRefServiceImpl extends ServiceImpl<UserOpinionRefMapper, UserOpinionRef>  implements UserOpinionRefService {


    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @Autowired
    private CeoUserInfoService ceoUserInfoService;

    @Override
    public void addOpinionRef(String opinionId, List<String> seeUserIds,Integer replyType) {
        List<UserOpinionRef> refList = new ArrayList<>();
        for(String userId : seeUserIds){
            UserOpinionRef ref = new UserOpinionRef();
            ref.setId(UUIDutils.getUUID32());
            ref.setUserId(userId);
            ref.setOpinionId(opinionId);
            ref.setCreateTime(new Date());
            ref.setReplyType(replyType);
            refList.add(ref);
        }
        this.saveBatch(refList);
    }

    @Override
    public List<String> getSeeUserIds(String opinionId){
        QueryWrapper<UserOpinionRef> qw = new QueryWrapper<>();
        qw.lambda().eq(UserOpinionRef::getOpinionId,opinionId);
        List<UserOpinionRef> refList = this.list(qw);
        List<String> collect = refList.stream().map(UserOpinionRef::getUserId).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<String> getReplyUserIds(String opinionId,Integer replyType) {
        QueryWrapper<UserOpinionRef> qw = new QueryWrapper<>();
        qw.lambda().eq(UserOpinionRef::getOpinionId,opinionId);
        qw.lambda().eq(UserOpinionRef::getReplyType,replyType);
        List<UserOpinionRef> refList = this.list(qw);
        return refList.stream().map(UserOpinionRef::getUserId).collect(Collectors.toList());
    }

    @Override
    public String getAppionterName(String opinionId, Integer replyType) {
        // 注意，此处还可以调用mapper的getAssistantByOpinionId方法得到协助人
        QueryWrapper<UserOpinionRef> qw = new QueryWrapper<>();
        qw.lambda().eq(UserOpinionRef::getOpinionId,opinionId);
        qw.lambda().eq(UserOpinionRef::getReplyType,replyType);
        List<UserOpinionRef> refList = this.list(qw);
        List<String> collect = refList.stream().map(UserOpinionRef::getUserId).collect(Collectors.toList());
        StringBuilder appionterName = new StringBuilder();
        boolean isFirst = Boolean.TRUE;
        for (String userId: collect) {
            CeoUserInfo ceoUserInfo = ceoUserInfoService.getById(userId);
            if (DataUtil.isNotEmpty(ceoUserInfo)){
                if (!isFirst){
                    appionterName.append(", ");
                }
                appionterName.append(ceoUserInfo.getUserName());
                isFirst = false;
                break;
            }
            StaffInfoVo staffInfo = sysStaffInfoService.getStaffInfo(userId);
            if (DataUtil.isNotEmpty(staffInfo)){
                if (!isFirst){
                    appionterName.append(", ");
                }
                appionterName.append(staffInfo.getUserName());
                isFirst = false;
            }
        }
        return appionterName.toString();
    }
}
