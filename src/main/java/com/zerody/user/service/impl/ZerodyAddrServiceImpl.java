package com.zerody.user.service.impl;

import com.zerody.common.bean.DataResult;
import com.zerody.user.mapper.ZerodyAddrMapper;
import com.zerody.user.pojo.ZerodyAddr;
import com.zerody.user.service.ZerodyAddrService;
import com.zerody.user.vo.ZerodyAddrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    private ZerodyAddrMapper zerodyAddrMapper;


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
    public List<ZerodyAddrVo> getAddr() {
        //查询得到所有地址
        List<ZerodyAddrVo> addrs = zerodyAddrMapper.selectAllAddr();
        if(addrs.size() == 0){
            return new ArrayList<>();
        }
        //返回树形结构地址
        return getChild(0,addrs);
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          设置地址 层级关系
     * @date                 2020/12/31 13:48
     * @param                [parentCode, allAddrs]
     * @return               java.util.List<com.zerody.user.vo.ZerodyAddrVo>
     */
    private List<ZerodyAddrVo> getChild(Integer parentCode, List<ZerodyAddrVo> allAddrs){
        //获取到父级节点code值为parentCode的所有地址
        List<ZerodyAddrVo> childs = allAddrs.stream().filter(a -> parentCode.equals(a.getPAddrCode())  ).collect(Collectors.toList());
        //没有就返回空集合
        if(childs.size() == 0){
            return new ArrayList<>();
        }
        //当前的code再遍历看节点下面是否还有子节点
        for (ZerodyAddrVo addr : childs){
            addr.setChildrens(getChild(addr.getCode(),allAddrs));
        }
        //返回结果
        return childs;
    }
}
