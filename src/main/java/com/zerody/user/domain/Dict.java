package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 *@ClassName Dict
 *@author    PengQiang
 *@DateTime  2022/4/28_10:15
 *@Deacription TODO
 */
@Data
public class Dict {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
    * 字典名称
    */
    private String dictName;

    /**
    * 字典类型
    */
    private String dictType;

}