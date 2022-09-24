package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

/**
 *@ClassName Data
 *@author    PengQiang
 *@DateTime  2022/9/15_14:33
 *@Deacription TODO
 */
/**
    * 键值对数据
    */
@lombok.Data
public class Data {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
    * 数据key
    */
    private String dataKey;

    /**
    * 数据值
    */
    private String dataValue;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
     * 用户id
     */
    private String userId;
}