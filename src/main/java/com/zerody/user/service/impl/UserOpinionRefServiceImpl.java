package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.user.domain.UserOpinionRef;
import com.zerody.user.domain.UserOpinionType;
import com.zerody.user.mapper.UserOpinionRefMapper;
import com.zerody.user.mapper.UserOpinionTypeMapper;
import com.zerody.user.service.UserOpinionRefService;
import com.zerody.user.service.UserOpinionTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author kuang
 */
@Service
public class UserOpinionRefServiceImpl extends ServiceImpl<UserOpinionRefMapper, UserOpinionRef>  implements UserOpinionRefService {


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
}
