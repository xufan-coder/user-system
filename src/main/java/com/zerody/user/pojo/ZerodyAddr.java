package com.zerody.user.pojo;

import lombok.Data;

/**
 * @author PengQiang
 * @ClassName ZerodyAddr
 * @DateTime 2020/12/23_10:01
 * @Deacription TODO
 */
@Data
public class ZerodyAddr {

    //地址id
    private String id;

    //地址编码
    private Integer code;

    //地址名称
    private String address;

    //父级地址编码
    private Integer pAddrCode;

    //行政等级（省市区分别是123级）
    private Byte addrLevel;

    //是否有效
    private Byte isEnabled;

}
