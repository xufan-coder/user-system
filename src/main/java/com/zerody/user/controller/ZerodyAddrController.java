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

    @Override
    @GetMapping(value = "/get/addr-name/inner")
    public DataResult<Map<String, String>> getAddrName(
            @RequestParam(value = "provinceCode", required = false) String provinceCode,
            @RequestParam(value = "cityCode", required = false) String cityCode,
            @RequestParam(value = "areaCode", required = false) String areaCode){
        return R.success(zerodyAddrService.getAddrName(provinceCode, cityCode, areaCode));
    }
}
