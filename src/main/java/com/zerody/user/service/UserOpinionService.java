package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.UserOpinion;
import com.zerody.user.dto.UserOpinionDto;
import com.zerody.user.dto.UserOpinionQueryDto;
import com.zerody.user.dto.UserReplyDto;
import com.zerody.user.vo.UserOpinionDetailVo;
import com.zerody.user.vo.UserOpinionPageVo;
import com.zerody.user.vo.UserOpinionVo;

import java.util.List;

/**
 * @author kuang
 */
public interface UserOpinionService extends IService<UserOpinion> {

    void addUserOpinion(UserOpinionDto param);

    void addUserReply(UserReplyDto param);

    IPage<UserOpinionVo> queryUserOpinionUser(UserOpinionQueryDto queryDto);

    IPage<UserOpinionPageVo> queryUserOpinionPage(UserOpinionQueryDto dto);

    UserOpinionDetailVo getOpinionDetail(String id);

    Object getOpinionAndReplySum(UserOpinionQueryDto dto);

    void modifyOpinionStateById(String id);
}
