package com.zerody.user.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 *@ClassName ConvertImage
 *@author    PengQiang
 *@DateTime  2023/2/24 15:09
 */
/**
    * 转换图片表
    */
@Data
public class ConvertImage {
    /**
    * 转换id
    */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
    * 源文件路径
    */
    private String originalFileUlr;

    /**
    * 转换文件路径
    */
    private String convertFileUlr;

    /**
    * 转换状态(0.未转换、1.已转换)
    */
    private Integer convertStatus;

    /**
    * 用户id
    */
    private String userId;

    /**
    * 连接id
    */
    private String connectId;

    /**
    * 创建时间
    */
    private Date createTime;
}