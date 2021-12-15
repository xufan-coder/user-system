package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 *@ClassName ImportInfo
 *@author    PengQiang
 *@DateTime  2021/12/14_15:50
 *@Deacription TODO
 */
@Data
public class ImportInfo {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
    * 导入人id
    */
    private String userId;

    /**
     * 导入文件名称
     */
    private String importFileName;

    /**
    * 导入人名称
    */
    private String userName;

    /**
    * 导入时间
    */
    private Date createTime;

    /**
    * excel 行数
    */
    private Integer excelRows;

    /**
    * 成功条数
    */
    private Integer successNum;

    /**
     * 失败条数
     */
    private Integer errorNum;

    /**
    * 所属企业id
    */
    private String companyId;

    /**
    * 所属企业
    */
    private String companyName;

    /**
    * 导入类型(1.外部内控名单导入)
    */
    private Integer importType;

    /**
     * 所属企业
     */
    private String importState;
}