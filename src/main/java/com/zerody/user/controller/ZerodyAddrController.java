package com.zerody.user.controller;

import com.zerody.common.bean.DataResult;
import com.zerody.user.service.ZerodyAddrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PengQiang
 * @ClassName ZerodyAddrController
 * @DateTime 2020/12/23_10:09
 * @Deacription TODO
 */
@RestController
@RequestMapping("/zerodyAddr")
public class ZerodyAddrController {

    @Autowired
    private ZerodyAddrService zerodyAddrService;

    //查询地址 树形接口返回
    @GetMapping("/getAddr")
    public DataResult getAddr(){

        return zerodyAddrService.getAddr();
    }
}
