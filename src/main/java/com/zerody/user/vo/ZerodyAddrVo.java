package com.zerody.user.vo;

import lombok.Data;

/**
 * @author PengQiang
 * @ClassName ZerodyAddrVo
 * @DateTime 2020/12/23_10:16
 * @Deacription TODO
 */
@Data
public class ZerodyAddrVo {

    /**
     *
     *地址id
     */
    private String id;

    /**
     *
     *地址编码
     */
    private Integer code;

    /**
     *
     *地址名称
     *
     */
    private String address;

    /**
     *
     *父级地址编码
     */
    private Integer pAddrCode;

    /**
     *
     *行政等级（省市区分别是123级）
     */
    private Byte addrLevel;


}
