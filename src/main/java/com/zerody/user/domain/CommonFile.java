package com.zerody.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * 文件表
 * @author  DaBai
 * @date  2022/11/28 17:21
 */

@Data
public class CommonFile {

    private String id;

    /**
     * 连接id
     */
    private String connectId;


    /**
     * 文件名称
     */
    private String fileName;

    /**
    *   文件格式
    */
    private String format;

    /**
     * 文件路径路径
     */
    private String fileUrl;

    /**
     * 类型
     */
    private String fileType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人id
     */
    private String createBy;

    /**
     * 创建人名称
     */
    private String createUsername;
}
