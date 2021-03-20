package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.domain.ZerodyAddr;
import com.zerody.user.vo.ZerodyAddrVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author PengQiang
 * @ClassName ZerodyAddrMapper
 * @DateTime 2020/12/23_10:07
 * @Deacription TODO
 */
@Mapper
public interface ZerodyAddrMapper extends BaseMapper<ZerodyAddr> {

    List<ZerodyAddrVo> selectAddr(Integer parentCode);

    String getAddrName(String code);

    List<ZerodyAddrVo> getAllCity();

    String getAddrCode(String name);

    List<ZerodyAddrVo> getAddrTreeByLevel(Integer level);

    String getCodeByLikeName(String name,Integer level);
}
