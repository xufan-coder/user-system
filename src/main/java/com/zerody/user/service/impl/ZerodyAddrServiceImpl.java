package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zerody.user.domain.ZerodyAddr;
import com.zerody.user.mapper.ZerodyAddrMapper;
import com.zerody.user.service.ZerodyAddrService;
import com.zerody.user.vo.ZerodyAddrVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.util.*;
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
        if(StringUtils.isNotEmpty(provinceCode)){
            addrMap.put(provinceCode, this.getAddrName(provinceCode));
        }
        if(StringUtils.isNotEmpty(cityCode)){
            addrMap.put(cityCode, this.getAddrName(cityCode));
        }
        if(StringUtils.isNotEmpty(areaCode)){
            addrMap.put(areaCode, this.getAddrName(areaCode));
        }
        return addrMap;
    }

    @Override
    public List<ZerodyAddrVo> getAllCity() {
        return this.mapper.getAllCity();
    }

    @Override
    public Map<String, String> getAddCode(String provinceName, String cityName, String areaName) {

        Map<String, String> addrMap = new HashMap<>();
        if(StringUtils.isNotEmpty(provinceName)){
            addrMap.put(provinceName, this.getAddrCode(provinceName));
        }
        if(StringUtils.isNotEmpty(cityName)){
            addrMap.put(cityName, this.getAddrCode(cityName));
        }
        if(StringUtils.isNotEmpty(areaName)){
            addrMap.put(areaName, this.getAddrCode(areaName));
        }
        return addrMap;
    }

    @Override
    public List<ZerodyAddrVo> getAddrTreeByLevel(Integer level) {
        List<ZerodyAddrVo> addrs = this.mapper.getAddrTreeByLevel(level);
        addrs = this.getChildrenAddr("0", addrs);
        return addrs;
    }


    @Override
    public Map<String, String> getCodeByLikeName(String provinceName, String cityName, String areaName) {
        Map<String, String> addrMap = new HashMap<>();
        if(StringUtils.isNotEmpty(provinceName)){
            addrMap.put(provinceName, this.getCodeByLikeName(provinceName,1));
        }
        if(StringUtils.isNotEmpty(cityName)){
            addrMap.put(cityName, this.getCodeByLikeName(cityName,2));
        }
        if(StringUtils.isNotEmpty(areaName)){
            addrMap.put(areaName, this.getCodeByLikeName(areaName,3));
        }
        return addrMap;
    }

    @Cacheable(value = "addrCode", key = "#method+#name")
    private String getCodeByLikeName(String name,Integer level){
       return this.mapper.getCodeByLikeName(name ,level);
    }


    @Cacheable(value = "addrName", key = "#code")
    private String getAddrName(String code){
       return this.mapper.getAddrName(code);
    }


    @Cacheable(value = "addrCode", key = "#method+#name")
    private String getAddrCode(String name){
        return this.mapper.getAddrCode(name);
    }


    private List<ZerodyAddrVo> getChildrenAddr(String parentId, List<ZerodyAddrVo> addrs){
        List<ZerodyAddrVo> childrens = addrs.stream().filter(a -> parentId.equals(a.getpAddrCode())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(childrens)){
            for (ZerodyAddrVo addr : childrens){
                addr.setChildrenAddr(getChildrenAddr(addr.getCode(), addrs));
            }
            return childrens;
        }
        return new ArrayList<>();
    }
}
