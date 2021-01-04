package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.domain.ZerodyAddr;
import com.zerody.user.vo.ZerodyAddrVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName ZerodyAddrMapper
 * @DateTime 2020/12/23_10:07
 * @Deacription TODO
 */
@Mapper
public interface ZerodyAddrMapper extends BaseMapper<ZerodyAddr> {

    List<ZerodyAddrVo> selectAllAddr(Integer parentCode);
}
