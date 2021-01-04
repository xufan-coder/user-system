package com.zerody.user.service;

import com.zerody.common.bean.DataResult;
import com.zerody.user.vo.ZerodyAddrVo;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName ZerodyAddr
 * @DateTime 2020/12/23_10:07
 * @Deacription TODO
 */
public interface ZerodyAddrService {
    /**
     *  获取树形地址
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2020/12/31 11:58
     * @param                []
     * @return               java.util.List<com.zerody.user.vo.ZerodyAddrVo>
     */
    List<ZerodyAddrVo> getAddr(Integer parentCode);
}
