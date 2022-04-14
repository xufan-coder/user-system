package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.domain.UserOpinion;
import com.zerody.user.vo.UserOpinionDetailVo;
import com.zerody.user.vo.UserOpinionPageVo;
import com.zerody.user.vo.UserOpinionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author kuang
 */
public interface UserOpinionMapper extends BaseMapper<UserOpinion> {

    IPage<UserOpinionPageVo> queryUserOpinionPage(IPage<UserOpinionPageVo> page);

    List<UserOpinionVo> queryUserOpinionUser(String userId);

    UserOpinionDetailVo getOpinionDetail(@Param("id") String id);
}
