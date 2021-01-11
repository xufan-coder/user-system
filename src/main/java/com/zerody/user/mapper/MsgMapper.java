package com.zerody.user.mapper;

import com.zerody.user.domain.Msg;
import java.io.Serializable;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
/**
 * @author 黄华盛 
 * @date 2021-01-11
 */
public interface MsgMapper extends  BaseMapper<Msg>{
}
