package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.user.api.service.AddrRemoteService;
import com.zerody.user.service.ZerodyAddrService;
import com.zerody.user.vo.ZerodyAddrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.List;
import java.util.Map;

/**
 * @author PengQiang
 * @ClassName ZerodyAddrController
 * @DateTime 2020/12/23_10:09
 * @Deacription TODO
 */
@RestController
@RequestMapping("/zerody_addr")
public class ZerodyAddrController implements AddrRemoteService {

    @Autowired
    private ZerodyAddrService zerodyAddrService;

    /**
     *
     *
     * @author               PengQiang
     * @description          根据code值返回子数据
     * @date                 2020/12/31 11:55
     * @param
     * @return               com.zerody.common.bean.DataResult
     */
    @RequestMapping(value = "/{parentCode}", method = RequestMethod.GET)
    public DataResult<List<ZerodyAddrVo>> getAddr(@PathVariable(name = "parentCode") Integer parentId){
        return R.success(zerodyAddrService.getAddr(parentId));
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          通过code查询地址名称
     * @date                 2021/2/7 17:09
     * @param                [provinceCode, cityCode, areaCode]
     * @return               com.zerody.common.api.bean.DataResult<java.util.Map<java.lang.String,java.lang.String>>
     */
    @Override
    @GetMapping(value = "/get/addr-name/inner")
    public DataResult<Map<String, String>> getAddrName(
            @RequestParam(value = "provinceCode", required = false) String provinceCode,
            @RequestParam(value = "cityCode", required = false) String cityCode,
            @RequestParam(value = "areaCode", required = false) String areaCode){
        return R.success(zerodyAddrService.getAddrName(provinceCode, cityCode, areaCode));
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          获取全部市地址
     * @date                 2021/2/7 16:53
     * @param                []
     * @return               com.zerody.common.api.bean.DataResult<java.util.List<com.zerody.user.vo.ZerodyAddrVo>>
     */
    @RequestMapping(value = "/get/all-city", method = RequestMethod.GET)
    public DataResult<List<ZerodyAddrVo>> getAllCity(){
        return R.success(zerodyAddrService.getAllCity());
    }


    /**
     *
     *
     * @author               PengQiang
     * @description          通过地址名称查询code值
     * @date                 2021/2/7 17:09
     * @param                [provinceCode, cityCode, areaCode]
     * @return               com.zerody.common.api.bean.DataResult<java.util.Map<java.lang.String,java.lang.String>>
     */
    @Override
    @GetMapping(value = "/get/addr-code/inner")
    public DataResult<Map<String, String>> getAddrCode(
            @RequestParam(value = "provinceName", required = false) String provinceName,
            @RequestParam(value = "cityName", required = false) String cityName,
            @RequestParam(value = "areaName", required = false) String areaName){
        return R.success(zerodyAddrService.getAddCode(provinceName, cityName, areaName));
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          根据地址等级获取树形结构
     * @date                 2021/2/24 16:44
     * @param                [level]
     * @return               com.zerody.common.api.bean.DataResult<java.util.List<com.zerody.user.vo.ZerodyAddrVo>>
     */
    @GetMapping(value = "/get/addr-tree")
    public DataResult<List<ZerodyAddrVo>> getAddrTreeByLevel(@RequestParam(name = "level") Integer level){
        return R.success(zerodyAddrService.getAddrTreeByLevel(level));
    }
}
