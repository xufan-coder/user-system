package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.user.domain.UserOpinion;
import com.zerody.user.dto.UserOpinionQueryDto;
import com.zerody.user.vo.UserOpinionDetailVo;
import com.zerody.user.vo.UserOpinionPageVo;
import com.zerody.user.vo.UserOpinionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author kuang
 */
public interface UserOpinionMapper extends BaseMapper<UserOpinion> {

    IPage<UserOpinionPageVo> queryUserOpinionPage(@Param("query") UserOpinionQueryDto query, IPage<UserOpinionPageVo> page);

    IPage<UserOpinionVo> queryUserOpinionUser(@Param("query") UserOpinionQueryDto query , Page<UserOpinionVo> iPage);

    UserOpinionDetailVo getOpinionDetail(@Param("id") String id);
}
