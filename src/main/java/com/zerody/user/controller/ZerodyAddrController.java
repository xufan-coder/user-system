package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.user.service.ZerodyAddrService;
import com.zerody.user.vo.ZerodyAddrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName ZerodyAddrController
 * @DateTime 2020/12/23_10:09
 * @Deacription TODO
 */
@RestController
public class ZerodyAddrController {

    @Autowired
    private ZerodyAddrService zerodyAddrService;

    /**
     *
     *
     * @author               PengQiang
     * @description          获取地址基础数据(树形返回)
     * @date                 2020/12/31 11:55
     * @param
     * @return               com.zerody.common.bean.DataResult
     */
    @RequestMapping(value = "/zerody_addr", method = RequestMethod.GET)
    public DataResult<List<ZerodyAddrVo>> getAddr(){
        return R.success(zerodyAddrService.getAddr());
    }
}
