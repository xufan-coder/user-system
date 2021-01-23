package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.zerody.user.mapper.ZerodyAddrMapper;
import com.zerody.user.service.ZerodyAddrService;
import com.zerody.user.vo.ZerodyAddrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
    public Map<Object, Object> getAllAddr() {
        List<Map<String, Object>> addrs = this.mapper.selectMaps(null);
        return addrs.stream().collect(Collectors.toMap(map -> map.get("code"), addr -> addr));
    }


}
