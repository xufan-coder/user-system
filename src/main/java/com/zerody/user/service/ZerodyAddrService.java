package com.zerody.user.service;

import com.zerody.common.bean.DataResult;
import com.zerody.user.vo.ZerodyAddrVo;

import java.util.List;
import java.util.Map;

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

    Map<String, String> getAddrName(String provinceCode, String cityCode, String areaCode);

    List<ZerodyAddrVo> getAllCity();

    Map<String, String> getAddCode(String provinceName, String cityName, String areaName);

    List<ZerodyAddrVo> getAddrTreeByLevel(Integer level);

    /**
     *  根据 地址名称模糊查询
     * @param provinceName
     * @param cityName
     * @param areaName
     * @return
     */
    Map<String, String> getCodeByLikeName(String provinceName, String cityName, String areaName);


    Map<String, String> getAddrNamesByCodes(String... codes);
}
