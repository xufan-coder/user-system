package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.zerody.user.mapper.ZerodyAddrMapper;
import com.zerody.user.service.ZerodyAddrService;
import com.zerody.user.vo.ZerodyAddrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author PengQiang
 * @ClassName ZerodyAddrServiceImpl
 * @DateTime 2020/12/23_10:08
 * @Deacription TODO
 */
@Service
@Slf4j
public class ZerodyAddrServiceImpl implements ZerodyAddrService {

    @Autowired
    private ZerodyAddrMapper mapper;


    /**
     *
     *
     * @author               PengQiang
     * @description          获取树形地址
     * @date                 2020/12/31 11:59
     * @param                []
     * @return               java.util.List<com.zerody.user.vo.ZerodyAddrVo>
     */
    @Override
    public List<ZerodyAddrVo> getAddr(Integer parentCode) {
        //查询得到所有地址为parentId的值
        List<ZerodyAddrVo> addrs = this.mapper.selectAddr(parentCode);
        return addrs;
    }

    @Override
    public Map<String, String> getAddrName(String provinceCode, String cityCode, String areaCode) {
        Map<String, String> addrMap = new HashMap<>();
        addrMap.put("province", this.getAddrName(provinceCode));
        addrMap.put("city", this.getAddrName(provinceCode));
        addrMap.put("area", this.getAddrName(provinceCode));

        return addrMap;
    }

    @Cacheable(value = "addrName", key = "#code")
    private String getAddrName(String code){
       return this.mapper.getAddrName(code);
    }


}
