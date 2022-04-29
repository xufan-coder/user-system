package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.util.UUIDutils;
import com.zerody.user.constant.ImageTypeInfo;
import com.zerody.user.domain.Image;
import com.zerody.user.domain.UserReply;
import com.zerody.user.dto.UserReplyDto;
import com.zerody.user.mapper.ImageMapper;
import com.zerody.user.mapper.UserReplyMapper;
import com.zerody.user.service.ImageService;
import com.zerody.user.service.UserReplyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author kuang
 */
@Service
public class UserReplyServiceImpl extends ServiceImpl<UserReplyMapper, UserReply> implements UserReplyService {

    @Resource
    private ImageService imageService;


}
