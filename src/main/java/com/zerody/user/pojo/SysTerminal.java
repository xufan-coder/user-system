package com.zerody.user.pojo;

import com.zerody.user.pojo.base.BaseModel;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SysTerminal
 * @DateTime 2020/12/21_13:54
 * @Deacription TODO
 */
@Data
public class SysTerminal extends BaseModel {

    //终端名称
    private String sysName;

    //系统描述
    private String sysDesc;

    //编码
    private String sysCode;

    //资源clientid
    private String clientId;

    //备注
    private String remark;
}
